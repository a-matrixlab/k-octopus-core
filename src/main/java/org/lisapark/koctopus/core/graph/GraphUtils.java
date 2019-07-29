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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import org.lisapark.koctopus.core.Input;
import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.core.processor.Processor;
import org.lisapark.koctopus.core.processor.ProcessorOutput;
import org.lisapark.koctopus.core.runtime.redis.StreamReference;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;
import org.lisapark.koctopus.util.Pair;
import org.openide.util.Exceptions;

/**
 *
 * @author alexmy
 */
public class GraphUtils {

    public static final Type HASH_MULTIMAP_PAIR = new TypeToken<HashMultimap<String, Pair<String, String>>>() {
    }.getType();

    public static final Type HASH_MULTIMAP_STRING = new TypeToken<HashMultimap<String, String>>() {
    }.getType();

    static class MultimapAdapter implements JsonDeserializer<Multimap<String, ?>>, JsonSerializer<Multimap<String, ?>> {

        private final Multimap<String, Object> multimap;

        MultimapAdapter(Multimap multimap) {
            this.multimap = multimap;
        }

        @Override
        public Multimap<String, ?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            final Map<String, Collection<Object>> map = context.deserialize(json, multimapTypeToMapType(type));
            map.entrySet().forEach((e) -> {
                multimap.putAll(e.getKey(), e.getValue());
            });
            return multimap;
        }

        @Override
        public JsonElement serialize(Multimap<String, ?> src, Type type, JsonSerializationContext context) {
            final Map<?, ?> map = src.asMap();
            return context.serialize(map);
        }

        private <V> Type multimapTypeToMapType(Type type) {
            final Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            assert typeArguments.length == 2;
            @SuppressWarnings("unchecked")
            final TypeToken<Map<String, Collection<V>>> mapTypeToken = new TypeToken<Map<String, Collection<V>>>() {
            }.where(new TypeParameter<V>() {
            }, (TypeToken<V>) TypeToken.of(typeArguments[1]));
            return mapTypeToken.getType();
        }
    }

    /**
     *
     * @param multimap
     * @return
     */
    public static Gson gsonGnodeMeta(Multimap<String, ?> multimap) {
        final MultimapAdapter multimapAdapter = new MultimapAdapter(multimap);
        final Gson gson = new GsonBuilder()
                //                .setPrettyPrinting()
                .registerTypeAdapter(multimap.getClass(), multimapAdapter)
                .create();
        return gson;
    }

    /**
     *
     * @param json
     * @return
     */
    public static Multimap<String, Pair<String, String>> multimapFromString(String json) {
        Multimap<String, Pair<String, String>> map = HashMultimap.create();
        Gson gson = GraphUtils.gsonGnodeMeta(map);
        map = gson.fromJson(json, GraphUtils.HASH_MULTIMAP_PAIR);
        return map;
    }

    /**
     *
     * @param params
     * @param multimap
     * @return
     * @throws ValidationException
     */
    public static Set<Parameter> processParams(Set<Parameter> params, Multimap<String, Pair<String, String>> multimap) throws ValidationException {
        for (Parameter param : params) {
            String key = String.valueOf(param.getId());
            Collection<Pair<String, String>> pairs = multimap.get(key);
            for (Pair<String, String> pair : pairs) {
                String entryName = pair.getFirst();
                switch (entryName) {
                    case NodeVocabulary.NAME:
                        param.setName(pair.getSecond());
                        break;
                    case NodeVocabulary.VALUE:
                        param.setValueFromString(pair.getSecond());
                    default:
                        break;
                }
            }
        }
        return params;
    }

    /**
     *
     * @param output
     * @param outputJson
     * @return
     * @throws org.lisapark.koctopus.core.ValidationException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public static Output processOutput(Output output, String outputJson) throws ValidationException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Multimap<String, Pair<String, String>> outputMap = GraphUtils.multimapFromString(outputJson);
        Set<String> keys = outputMap != null ? outputMap.keySet() : new HashSet<>();
        for (String key : keys) {
            Collection<Pair<String, String>> pairs = outputMap.get(key) == null ? new HashSet<>() : outputMap.get(key);
            for (Pair<String, String> pair : pairs) {
                String entryName = pair.getFirst();
                // Check if attribute doesn't exist (if exist we don/t need to add it)
                // and the value of attribute type is not empty.
                if (output.getAttributeByName(key) == null && pair.getSecond() != null) {
                    if (NodeVocabulary.TYPE.equalsIgnoreCase(entryName)) {
                        Attribute newAttr = Attribute.newAttributeByClassName(pair.getSecond(), key);
                        output.addAttribute(newAttr);
                    }
                }
            }
        }
        return output;
    }

    public static Map<String, StreamReference> processInput(Map<String, StreamReference> references, String inputJson) throws ValidationException {

        Multimap<String, Pair<String, String>> inputMap = GraphUtils.multimapFromString(inputJson);
        Set<String> keys = inputMap != null ? inputMap.keySet() : new HashSet<>();
        for (String key : keys) {
            StreamReference streamreference = new StreamReference();
            Collection<Pair<String, String>> pairs = inputMap.get(key) == null ? new HashSet<>() : inputMap.get(key);
            Map<String, String> pairmap = pairsToMap(pairs);
            streamreference.getEventType().addAttribute(Attribute.newAttributeByClassName(
                    pairmap.get(NodeVocabulary.TYPE), pairmap.get(NodeVocabulary.NAME)));
            streamreference.setReferenceClass(pairmap.get(NodeVocabulary.SOURCE_CLASS));
            streamreference.setReferenceId(pairmap.get(NodeVocabulary.SOURCE_ID));
            references.put(key, streamreference);
        }
        return references;
    }

    private static Map<String, String> pairsToMap(Collection<Pair<String, String>> pairs) {
        Map<String, String> map = new HashMap<>();
        pairs.forEach((Pair<String, String> pair) -> {
            map.put(pair.getFirst(), pair.getSecond());
        });
        return map;
    }

    public static void buildSource(ExternalSource source, Gnode gnode) {
        String paramsJson = gnode.getParams();
        Multimap<String, Pair<String, String>> paramMap = GraphUtils.multimapFromString(paramsJson);
        Set<Parameter> params = source.getParameters();
        try {
            GraphUtils.processParams(params, paramMap);
            String outputJson = gnode.getOutput();
            Output output = source.getOutput();
            output = GraphUtils.processOutput(output, outputJson);
            source.setOutput(output);
        } catch (ValidationException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void buildSink(ExternalSink sink, Gnode gnode) {
        String paramsJson = gnode.getParams();
        Multimap<String, Pair<String, String>> paramMap = GraphUtils.multimapFromString(paramsJson);
        Set<Parameter> params = sink.getParameters();
        try {
            GraphUtils.processParams(params, paramMap);
            String inputJson = gnode.getInput();
            List<? extends Input> inputs = sink.getInputs();
            inputs.stream().forEach((Input input) -> {
                try {
                    sink.setReferences(GraphUtils.processInput(sink.getReferences(), inputJson));
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            });
        } catch (ValidationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void buildProcessor(Processor processor, Gnode gnode) {
        String paramsJson = gnode.getParams();
        Multimap<String, Pair<String, String>> paramMap = GraphUtils.multimapFromString(paramsJson);
        Set<Parameter> params = processor.getParameters();
        try {
            GraphUtils.processParams(params, paramMap);
            String inputJson = gnode.getInput();
            List<? extends Input> inputs = processor.getInputs();
            inputs.stream().forEach((Input input) -> {
                try {
                    processor.setReferences(GraphUtils.processInput(processor.getReferences(), inputJson));
                } catch (ValidationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            });
            String outputJson = gnode.getOutput();
            ProcessorOutput output = processor.getOutput();
            output = (ProcessorOutput) GraphUtils.processOutput(output, outputJson);
            processor.setOutput(output);
        } catch (ValidationException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
