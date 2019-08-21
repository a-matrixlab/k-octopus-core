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
package org.lisapark.koctopus.core.runtime;

import com.google.gson.Gson;
import org.lisapark.koctopus.core.ProcessingException;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.graph.Gnode;
import org.lisapark.koctopus.core.graph.Graph;
import org.lisapark.koctopus.core.graph.api.Vocabulary;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.runtime.redis.RedisRuntime;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;
import org.openide.util.Exceptions;

/**
 *
 * @author alexmy
 */
public class OctopusRunner extends AbstractRunner<Integer> {

    public OctopusRunner() {
        super();
    }

    public OctopusRunner(Graph graph) {
        super(graph);
    }

    @Override
    public String processNode(Gnode gnode, boolean forward) {

        String trnsUrl = gnode.getTransportUrl();
        RedisRuntime runtime = new RedisRuntime(trnsUrl, getStandardOut(), getStandardError());
        Integer status;

        try {
            String type;
            switch (gnode.getLabel()) {
                case Vocabulary.SOURCE:
                    type = gnode.getType();
                    ExternalSource sourceIns = (ExternalSource) Class.forName(type).newInstance();
                    ExternalSource source = (ExternalSource) sourceIns.newInstance(gnode);
                    status = (Integer) source.compile(source).startProcessingEvents(runtime);
                    getNodeStatus().put(gnode.getId(), status);

                    break;
                case Vocabulary.PROCESSOR:
                    type = gnode.getType();
                    AbstractProcessor processorIns = (AbstractProcessor) Class.forName(type).newInstance();
                    AbstractProcessor processor = (AbstractProcessor) processorIns.newInstance(gnode);
                    status = (Integer) processor.compile(processor).processEvent(runtime);
                    getNodeStatus().put(gnode.getId(), status);

                    break;
                case Vocabulary.SINK:
                    type = gnode.getType();
                    ExternalSink sinkIns = (ExternalSink) Class.forName(type).newInstance();
                    ExternalSink sink = (ExternalSink) sinkIns.newInstance(gnode);
                    status = (Integer) sink.compile(sink).processEvent(runtime);
                    getNodeStatus().put(gnode.getId(), status);

                    break;
                default:
                    break;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | ValidationException | ProcessingException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new Gson().toJson(gnode);
    }
}
