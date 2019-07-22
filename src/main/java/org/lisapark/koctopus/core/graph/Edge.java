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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import reactor.util.function.Tuple2;

/**
 *
 * @author alexmy
 */
public final class Edge {
    public String source;
    public String relation;
    public String target;
    public boolean directed;
    public String label;
    
    public Map<String, Tuple2<String, String>> metadata;

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the relation
     */
    public String getRelation() {
        return relation;
    }

    /**
     * @param relation the relation to set
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * @return the directed
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * @param directed the directed to set
     */
    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the metadata
     */
    public Map<String, Tuple2<String, String>> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Map<String, Tuple2<String, String>> metadata) {
        this.metadata = metadata;
    }
    
    public JsonObject toJson(){
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(new Gson().toJson(this, this.getClass()));
        return elem.getAsJsonObject();
    }
    
    public Edge fromJson(String json){
        return new Gson().fromJson(json, this.getClass());
    }
}
