/* 
 * Copyright (C) 2013 Lisa Park, Inc. (www.lisa-park.net)
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
package org.lisapark;

import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.lisapark.koctopus.core.parameter.ConversionException;

public class App {

    public static void main(String[] args) {

        RedisClient client = RedisClient.create("redis://localhost");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStreamCommands<String, String> streamCommands = connection.sync();

        Map<String, String> body = Collections.singletonMap("key", "value");
        String messageId = streamCommands.xadd("my-stream", body);

        List<StreamMessage<String, String>> messages = streamCommands
                .xread(XReadArgs.Builder.count(1),
                        StreamOffset.from("my-stream", "0"));
       
        System.out.println("Messages: " + messages);
    }

    public static Integer parseValueFromString(String stringValue) throws ConversionException {
        String str = stringValue;
        try {
            // Check for decimal dot - AM
            int endIndex = stringValue.indexOf('.');
            if (endIndex > 0) {
                str = stringValue.substring(0, endIndex);
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ConversionException(String.format("Could not convert %s into a number", stringValue));
        }
    }
}
