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
package org.lisapark.koctopus.core.sink.external;

import java.util.Map;
import org.lisapark.koctopus.core.Persistable;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.transport.TransportReference;
import org.lisapark.koctopus.core.sink.Sink;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public interface ExternalSink extends Sink {

    CompiledExternalSink compile() throws ValidationException;

    <T extends ExternalSink> CompiledExternalSink compile(T sink) throws ValidationException;

    @Override
    Sink copyOf();

    Map<String, TransportReference> getReferences();

    void setReferences(Map<String, TransportReference> sourceref);
}
