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
import java.util.List;

/**
 *
 * @author alexmy
 */
public final class Graph extends Gnode {
    
    private Boolean directed;

    private List<Gnode> nodes;
    private List<Edge> edges;    

    /**
     * @return the directed
     */
    public Boolean getDirected() {
        return directed;
    }

    /**
     * @param directed the directed to set
     */
    public void setDirected(Boolean directed) {
        this.directed = directed;
    }

    /**
     * @return the nodes
     */
    public List<Gnode> getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(List<Gnode> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the edges
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set
     */
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }
    
    @Override
    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(new Gson().toJson(this, this.getClass()));
        return elem.getAsJsonObject();
    }

    @Override
    public Graph fromJson(String json) {
        return new Gson().fromJson(json, this.getClass());
    }
}
