/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.runtime;

import io.lettuce.core.StreamMessage;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author alexmy
 */
public interface StreamingRuntime<T> {
    
    void writeEvents(Map<String, Object> event, String className, UUID id);

    List<T> readEvents(String className, UUID id, int range);

    List<T> readEvents(String className, UUID id, String offset, int range);

    List<T> readEvents(String className, UUID id, String offset);
    
    PrintStream getStandardOut();
   
    PrintStream getStandardError();

    static enum State {
        NOT_STARTED, RUNNING, SHUTDOWN
    }

    void start();

    void shutdown();
}
