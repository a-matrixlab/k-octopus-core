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
package org.lisapark.koctopus.core.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.lisapark.koctopus.core.Input;
import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.ProcessingModel;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.graph.api.GraphVocabulary;
import org.lisapark.koctopus.core.graph.api.Vocabulary;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.processor.ProcessorOutput;
import org.lisapark.koctopus.core.runtime.redis.StreamReference;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.Source;
import org.lisapark.koctopus.core.source.external.ExternalSource;
import org.openide.util.Exceptions;

/**
 *
 * @author alexmy
 */
public class GraphUtils {

    public static void buildSource(ExternalSource source, Gnode gnode) {
        NodeParams gparams = (NodeParams) gnode.getParams();
        Set<Parameter> params = source.getParameters();
        params.forEach((Parameter param) -> {
            NodeParam _param = gparams.getParams().get(param.getId());
            if (_param != null) {
                try {
                    String value = _param.getValue() == null ? null : _param.getValue().toString();
                    param.setValueFromString(value);
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        final NodeOutput goutput = (NodeOutput) gnode.getOutput();
        Output output = source.getOutput();
        goutput.getAttributes().forEach((String name, NodeAttribute att) -> {
            if (output.getAttributeByName(name) == null) {
                Attribute newAttr;
                try {
                    newAttr = Attribute.newAttributeByClassName(att.getClassName(), name);
                    output.addAttribute(newAttr);
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        source.setOutput(output);
    }

    public static void buildSink(ExternalSink sink, Gnode gnode) {
        NodeParams gparams = (NodeParams) gnode.getParams();
        Set<Parameter> params = sink.getParameters();
        params.forEach((Parameter param) -> {
            NodeParam _param = (NodeParam) gparams.getParams().get(param.getId());
            if (_param != null) {
                try {
                    String value = _param.getValue() == null ? null : _param.getValue().toString();
                    param.setValueFromString(value);
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        final NodeInputs ginputs = (NodeInputs) gnode.getInput();
        List<? extends Input> inputs = sink.getInputs();
        inputs.forEach((Input input) -> {
            NodeInput _input = (NodeInput) ginputs.getSources().get(input.getName());
            if (_input != null) {           
                StreamReference ref = new StreamReference();
                ref.setReferenceClass(_input.getSourceClassName());
                ref.setReferenceId(_input.getSourceId());
                ref.setAttributes(_input.getAttributes());
                sink.getReferences().put(input.getName(), ref);
            }
        });
    }

    public static void buildProcessor(AbstractProcessor processor, Gnode gnode) {
        NodeParams gparams = (NodeParams) gnode.getParams();
        Set<Parameter> params = processor.getParameters();
        params.forEach((Parameter param) -> {
            NodeParam _param = (NodeParam) gparams.getParams().get(param.getId());
            if (_param != null) {
                try {
                    String value = _param.getValue() == null ? null : _param.getValue().toString();
                    param.setValueFromString(value);
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        final NodeInputs ginputs = (NodeInputs) gnode.getInput();
        List<? extends Input> inputs = processor.getInputs();
        inputs.forEach((Input input) -> {
            NodeInput _input = (NodeInput) ginputs.getSources().get(input.getName());
            if (_input != null) {
                StreamReference ref = new StreamReference();
                ref.setReferenceClass(_input.getSourceClassName());
                ref.setReferenceId(_input.getSourceId());
                ref.setAttributes(_input.getAttributes());
                processor.getReferences().put(input.getName(), ref);
            }
        });
        final NodeOutput goutput = (NodeOutput) gnode.getOutput();
        ProcessorOutput output = processor.getOutput();
        goutput.getAttributes().forEach((String name, NodeAttribute att) -> {
            if (output.getAttributeByName(name) == null) {
                Attribute newAttr;
                try {
                    newAttr = Attribute.newAttributeByClassName(att.getClassName(), name);
                    output.addAttribute(newAttr);
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
        processor.setOutput(output);
    }
    
    public static Graph compileGraph(ProcessingModel model) {
        Graph graph = new Graph();

        graph.setId(model.getId().toString());
        graph.setLabel(Vocabulary.MODEL);
        graph.setType(Vocabulary.OCTOPUS_RUNNER);
        graph.setTransportUrl(model.getTransportUrl());
        graph.setColor(GraphVocabulary.UNTOUCHED);
        graph.setDirected(Boolean.TRUE);

        NodeParams gparams = new NodeParams();
//        gparams.setParams(new HashMap<>());
//        NodeParam nodeparam = new NodeParam();
//        nodeparam.setId(0);
//        nodeparam.setName(Vocabulary.TRANSPORT_URL);
//        nodeparam.setClassName(new String().getClass().getCanonicalName());
//        nodeparam.setValue(model.getTransportUrl());
//        gparams.getParams().put(0, nodeparam);
        graph.setParams(gparams);
        
        List<Gnode> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        // Sources
        //======================================================================
        Set<ExternalSource> sources = model.getExternalSources();
        sources.stream().forEach((ExternalSource source) -> {
            Gnode sourceGnode = new Gnode();
            sourceGnode.setId(source.getId().toString());
            sourceGnode.setLabel(Vocabulary.SOURCE);
            sourceGnode.setType(source.getClass().getCanonicalName());
            sourceGnode.setTransportUrl(model.getTransportUrl());

            Set<Parameter> params = source.getParameters();
            NodeParams _params = new NodeParams();
            _params.setParams(new HashMap<>());
            params.stream().forEach((Parameter param) -> {
                NodeParam _param = new NodeParam();
                _param.setId(param.getId());
                _param.setName(param.getName());
                _param.setClassName(param.getType().getCanonicalName());
                _param.setValue(param.getValue());
                _params.getParams().put(param.getId(), _param);
            });
            sourceGnode.setParams(_params);

            Output output = source.getOutput();
            List<Attribute> attrs = source.getOutput().getAttributes();
            NodeOutput nodeOutput = new NodeOutput();
            nodeOutput.setName(output.getName());
            nodeOutput.setId(output.getId());

            NodeAttributes nodeattrs = new NodeAttributes();
            nodeattrs.setAttributes(new HashMap<>());
            attrs.stream().forEach((Attribute attr) -> {
                NodeAttribute nodeattr = new NodeAttribute();
                nodeattr.setName(attr.getName());
                nodeattr.setClassName(attr.getType().getCanonicalName());
                nodeattrs.getAttributes().put(attr.getName(), nodeattr);
            });
            nodeOutput.setAttributes(nodeattrs.getAttributes());
            sourceGnode.setOutput(nodeOutput);
            nodes.add(sourceGnode);
        });

        // Processors
        //======================================================================
        Set<AbstractProcessor> processors = model.getProcessors();
        processors.stream().forEach((AbstractProcessor proc) -> {
            Gnode procGnode = new Gnode();
            procGnode.setId(proc.getId().toString());
            procGnode.setLabel(Vocabulary.PROCESSOR);
            procGnode.setType(proc.getClass().getCanonicalName());
            procGnode.setTransportUrl(model.getTransportUrl());

            // Setting params
            Set<Parameter> params = proc.getParameters();
            NodeParams _params = new NodeParams();
            _params.setParams(new HashMap<>());
            params.stream().forEach((Parameter param) -> {
                NodeParam _param = new NodeParam();
                _param.setId(param.getId());
                _param.setName(param.getName());
                _param.setClassName(param.getType().getCanonicalName());
                _param.setValue(param.getValue());
                _params.getParams().put(param.getId(), _param);
            });
            procGnode.setParams(_params);

            // Setting inputs
            List<? extends Input> inputs = proc.getInputs();
            NodeInputs nodeInputs = new NodeInputs();
            nodeInputs.setSources(new HashMap<>());
            inputs.stream().forEach((Input input) -> {
                Source inputSource = input.getSource();
                NodeInput nodeInput = new NodeInput();
                nodeInput.setAttributes(new HashMap());
                nodeInput.setId(input.getId());
                nodeInput.setName(input.getName());
                nodeInput.setSourceId(inputSource.getId().toString());
                nodeInput.setSourceClassName(inputSource.getClass().getCanonicalName());
                List<Attribute> attrs = inputSource.getOutput().getAttributes();
                NodeAttributes nodeattrs = new NodeAttributes();
                nodeattrs.setAttributes(new HashMap<>());
                attrs.stream().forEach((Attribute attr) -> {
                    NodeAttribute nodeattr = new NodeAttribute();
                    nodeattr.setClassName(attr.getType().getCanonicalName());
                    nodeattr.setName(attr.getName());
                    nodeattrs.getAttributes().put(attr.getName(), nodeattr);
                });
                nodeInput.setAttributes(nodeattrs.getAttributes());
                nodeInputs.getSources().put(input.getName(), nodeInput);
                // Create edge
                Edge edge = new Edge();
                edge.setLabel(Vocabulary.MODEL);
                edge.setRelation(input.getName());
                edge.setDirected(true);
                edge.setSource(inputSource.getClass().getCanonicalName() + ":" + inputSource.getId().toString());
                edge.setTarget(proc.getClass().getCanonicalName() + ":" + proc.getId().toString());
                edges.add(edge);
            });            
            procGnode.setInput(nodeInputs);
            
            Output output = proc.getOutput();
            List<Attribute> attrs = proc.getOutput().getAttributes();
            NodeOutput nodeOutput = new NodeOutput();
            nodeOutput.setName(output.getName());
            nodeOutput.setId(output.getId());

            NodeAttributes nodeattrs = new NodeAttributes();
            nodeattrs.setAttributes(new HashMap<>());
            attrs.stream().forEach((Attribute attr) -> {
                NodeAttribute nodeattr = new NodeAttribute();
                nodeattr.setName(attr.getName());
                nodeattr.setClassName(attr.getType().getCanonicalName());
                nodeattrs.getAttributes().put(attr.getName(), nodeattr);
            });
            nodeOutput.setAttributes(nodeattrs.getAttributes());
            procGnode.setOutput(nodeOutput);
            nodes.add(procGnode);
        });

        // Sinks
        //======================================================================
        Set<ExternalSink> sinks = model.getExternalSinks();
        sinks.stream().forEach((ExternalSink sink) -> {
            Gnode sinkGnode = new Gnode();
            sinkGnode.setId(sink.getId().toString());
            sinkGnode.setLabel(Vocabulary.SINK);
            sinkGnode.setType(sink.getClass().getCanonicalName());
            sinkGnode.setTransportUrl(model.getTransportUrl());

            Set<Parameter> params = sink.getParameters();
            NodeParams _params = new NodeParams();
            _params.setParams(new HashMap<>());
            params.stream().forEach((Parameter param) -> {
                NodeParam _param = new NodeParam();
                _param.setId(param.getId());
                _param.setName(param.getName());
                _param.setClassName(param.getType().getCanonicalName());
                _param.setValue(param.getValue());
                _params.getParams().put(param.getId(), _param);
            });
            sinkGnode.setParams(_params);

            // Setting inputs
            List<? extends Input> inputs = sink.getInputs();
            NodeInputs nodeInputs = new NodeInputs();
            nodeInputs.setSources(new HashMap<>());
            inputs.stream().forEach((Input input) -> {
                Source inputSource = input.getSource();
                NodeInput nodeInput = new NodeInput();
                nodeInput.setAttributes(new HashMap());
                nodeInput.setId(input.getId());
                nodeInput.setName(input.getName());
                nodeInput.setSourceId(inputSource.getId().toString());
                nodeInput.setSourceClassName(inputSource.getClass().getCanonicalName());
                List<Attribute> attrs = inputSource.getOutput().getAttributes();
                NodeAttributes nodeattrs = new NodeAttributes();
                nodeattrs.setAttributes(new HashMap<>());
                attrs.stream().forEach((Attribute attr) -> {
                    NodeAttribute nodeattr = new NodeAttribute();
                    nodeattr.setClassName(attr.getType().getCanonicalName());
                    nodeattr.setName(attr.getName());
                    nodeattrs.getAttributes().put(attr.getName(), nodeattr);
                });
                nodeInput.setAttributes(nodeattrs.getAttributes());
                nodeInputs.getSources().put(input.getName(), nodeInput);
                // Create edge
                Edge edge = new Edge();
                edge.setLabel(Vocabulary.MODEL);
                edge.setRelation(input.getName());
                edge.setDirected(true);
                edge.setSource(inputSource.getClass().getCanonicalName() + ":" + inputSource.getId().toString());
                edge.setTarget(sink.getClass().getCanonicalName() + ":" + sink.getId().toString());
                edges.add(edge);
            });            
            sinkGnode.setInput(nodeInputs);
            nodes.add(sinkGnode);
        });
        graph.setNodes(nodes);
        graph.setEdges(edges);

        return graph;
    }
}
