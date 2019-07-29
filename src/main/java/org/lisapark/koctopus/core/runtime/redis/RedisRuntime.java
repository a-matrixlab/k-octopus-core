/* 
 * Copyright (C) 2019 Lisa Park, Inc. (www.lisa-park.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lisapark.koctopus.core.runtime.redis;

import static com.google.common.base.Preconditions.checkState;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.lisapark.koctopus.core.event.Event;
import org.lisapark.koctopus.core.runtime.StreamProcessingRuntime;

/**
 *
 * @author alexmy
 */
public class RedisRuntime implements StreamProcessingRuntime<StreamMessage<String, String>> {

    private final int threadPoolSize = 1;

    private final ExecutorService executorService;
    private final PrintStream standardOut;
    private final PrintStream standardError;

    private final String redisUrl;
    private final RedisClient client;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private State currentState = State.NOT_STARTED;

    public RedisRuntime(String redisUrl, PrintStream standardOut, PrintStream standardError) {
        this.standardOut = standardOut;
        this.standardError = standardError;
        this.redisUrl = redisUrl;
        this.client = RedisClient.create(redisUrl);
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void writeEvents(Map<String, Object> event, String className, UUID id) {
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStreamCommands<String, String> streamCommands = connection.sync();

        String name = className + ":" + id.toString();
        readLock.lock();
        try {
            checkState(currentState == RedisRuntime.State.RUNNING, "Cannot send an event unless the runtime has been started.");
            Map<String, String> map = valueToString(event);
            streamCommands.xadd(name, map);
        } finally {
            connection.close();
            readLock.unlock();
        }
    }

    /**
     *
     * @param className
     * @param id
     * @param range
     * @return
     */
    @Override
    public List<StreamMessage<String, String>> readEvents(String className, UUID id, int range) {
        List<StreamMessage<String, String>> messages;

        String streamName = className + ":" + id.toString();
        String offset = StreamOffset.lastConsumed(streamName).getOffset();

        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisStreamCommands<String, String> streamCommands = connection.sync();

            if (offset.equalsIgnoreCase(">")) {
                messages = streamCommands.xread(XReadArgs.Builder.count(range),
                        StreamOffset.from(streamName, "0"));
            } else {
                messages = streamCommands.xread(XReadArgs.Builder.count(range),
                        StreamOffset.lastConsumed(streamName));
            }
        }
        return messages;
    }

    /**
     *
     * @param className
     * @param id
     * @param offset
     * @param range
     * @return
     */
    @Override
    public List<StreamMessage<String, String>> readEvents(String className, UUID id, String offset, int range) {
        List<StreamMessage<String, String>> messages;
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisStreamCommands<String, String> streamCommands = connection.sync();
            String streamName = className + ":" + id.toString();
            messages = streamCommands
                    .xread(XReadArgs.Builder.count(range),
                            StreamOffset.from(streamName, offset));
        }
        return messages;
    }
/**
     *
     * @param className
     * @param id
     * @param offset
     * @return
     */
    @Override
    public List<StreamMessage<String, String>> readEvents(String className, UUID id, String offset) {
        List<StreamMessage<String, String>> messages;
        try (StatefulRedisConnection<String, String> connection = client.connect()) {
            RedisStreamCommands<String, String> streamCommands = connection.sync();
            String streamName = className + ":" + id.toString();
            messages = streamCommands
                    .xread(XReadArgs.StreamOffset.from(streamName, offset));
        }
        return messages;
    }
    /**
     *
     * @param data
     * @return
     */
    private Map<String, String> valueToString(Map<String, Object> data) {
        Map<String, String> map = new HashMap<>();
        data.forEach((k, v) -> {
            map.put(k, v.toString());
        });
        return map;
    }

    @Override
    public PrintStream getStandardOut() {
        return standardOut;
    }

    @Override
    public PrintStream getStandardError() {
        return standardError;
    }

    static enum State {
        NOT_STARTED, RUNNING, SHUTDOWN
    }

    @Override
    public void start() {
        writeLock.lock();
        try {
            if (currentState != State.NOT_STARTED) {
                throw new IllegalStateException(String.format("Cannot start runtime unless status is %s", State.NOT_STARTED));
            }
            currentState = RedisRuntime.State.RUNNING;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void shutdown() {
        boolean interrupted = false;
        boolean shutdownComplete = false;
        readLock.lock();
        try {
            checkState(currentState == RedisRuntime.State.RUNNING, "Cannot shutdown if the runtime is not running");
            while (!shutdownComplete) {
                executorService.shutdown();
                try {
                    shutdownComplete = executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            readLock.unlock();
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
