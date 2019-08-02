/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.runtime;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.lisapark.koctopus.core.ProcessingException;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.graph.Gnode;
import org.lisapark.koctopus.core.graph.api.Vocabulary;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;
import org.openide.util.Exceptions;

/**
 *
 * @author alexmy
 */
public class BaseController {

    enum Status {
        SUCCESS(200),
        ERROR(400);
        private final int statusCode;

        Status(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return this.statusCode;
        }
    }

    /**
     *
     * @param json
     * @param runtime
     * @return
     * @throws ValidationException
     * @throws ProcessingException
     */
    public static String process(String json, StreamingRuntime runtime) throws ValidationException, ProcessingException {
        String result = null;
        try {
            Gnode gnode = (Gnode) new Gnode().fromJson(json);
            String transportUrl = gnode.getTransportUrl();
            
            String type;
            switch (gnode.getLabel()) {
                case Vocabulary.SOURCE:
                    type = gnode.getType();
                    ExternalSource sourceIns = (ExternalSource) Class.forName(type).newInstance();
                    ExternalSource source = (ExternalSource) sourceIns.newInstance(gnode);
                    result = new Gson().toJson(sourceResponse(source, gnode, transportUrl));
                    source.compile(source).startProcessingEvents(runtime);

                    break;
                case Vocabulary.PROCESSOR:
                    type = gnode.getType();
                    AbstractProcessor processorIns = (AbstractProcessor) Class.forName(type).newInstance();
                    AbstractProcessor processor = (AbstractProcessor) processorIns.newInstance(gnode);
                    result = new Gson().toJson(processorResponse(processor, gnode, transportUrl));
                    processor.compile(processor).processEvent(runtime);

                    break;
                case Vocabulary.SINK:
                    type = gnode.getType();
                    ExternalSink sinkIns = (ExternalSink) Class.forName(type).newInstance();
                    ExternalSink sink = (ExternalSink) sinkIns.newInstance(gnode);
                    result = new Gson().toJson(sinkResponse(sink, gnode, transportUrl));
                    sink.compile(sink).processEvent(runtime, null);

                    break;
                default:
                    break;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        return result;
    }

    /**
     *
     * @param source
     * @param transportUrl
     * @return
     */
    private static Map<String, String> sourceResponse(ExternalSource source, Gnode gnode, String transportUrl) {
        Map<String, String> map = new HashMap<>();
        String attrsvalue = null;
        if (gnode.getOutput() != null && gnode.getOutput().getAttributes() != null) {
            attrsvalue = gnode.getOutput().getAttributes().keySet().toString();
        }
        map.put("Atts", attrsvalue);
        map.put("transportUrl", transportUrl);
        map.put("className", source.getClass().getCanonicalName());
        map.put("Id", source.getId().toString());

        return map;
    }

    private static Map<String, String> sinkResponse(ExternalSink sink, Gnode gnode, String transportUrl) {
        Map<String, String> map = new HashMap<>();
        String attrsvalue = null;
        if (gnode.getOutput() != null && gnode.getOutput().getAttributes() != null) {
            attrsvalue = gnode.getOutput().getAttributes().keySet().toString();
        }
        map.put("Atts", attrsvalue);
        map.put("transportUrl", transportUrl);
        map.put("className", sink.getClass().getCanonicalName());
        map.put("Id", sink.getId().toString());

        return map;
    }

    private static Map<String, String> processorResponse(AbstractProcessor processor, Gnode gnode, String transportUrl) {
        Map<String, String> map = new HashMap<>();
        String attrsvalue = null;
        if (gnode.getOutput() != null && gnode.getOutput().getAttributes() != null) {
            attrsvalue = gnode.getOutput().getAttributes().keySet().toString();
        }
        map.put("Atts", attrsvalue);
        map.put("transportUrl", transportUrl);
        map.put("className", processor.getClass().getCanonicalName());
        map.put("Id", processor.getId().toString());

        return map;
    }
}
