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
import java.util.Set;
import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.util.Pair;

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
                .setPrettyPrinting()
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
     * @param multimap
     * @return 
     * @throws org.lisapark.koctopus.core.ValidationException
     * @throws java.lang.ClassNotFoundException
     */
    public static Output processAttributes(Output output, String outputJson) throws ValidationException, ClassNotFoundException {
        Multimap<String, Pair<String, String>> outputMap = GraphUtils.multimapFromString(outputJson);
        Set<String> keys = outputMap.keySet();
        for (String key : keys) {
            Collection<Pair<String, String>> pairs = outputMap.get(key);
            for (Pair<String, String> pair : pairs) {
                String entryName = pair.getFirst();
                if (output.getAttributeByName(entryName) == null) {
                    Class clazz = Class.forName(pair.getSecond()).getClass();
                    Attribute newAttr = Attribute.newAttribute(clazz, entryName);
                    output.addAttribute(newAttr);
                }
            }
        }        
        return output;
    }
}
