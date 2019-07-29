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
package org.lisapark.koctopus.core.runtime;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.lisapark.koctopus.core.event.Event;

/**
 * @author alexmy
 * @param <M>
 */
public interface StreamProcessingRuntime<M> {

    void start();

    void shutdown();

    void writeEvents(Map<String, Object> event, String className, UUID id);

    PrintStream getStandardOut();

    PrintStream getStandardError();

    List<M> readEvents(String className, UUID id, int range);
    
    List<M> readEvents(String className, UUID id, String offset);

    List<M> readEvents(String className, UUID id, String offset, int range);
}