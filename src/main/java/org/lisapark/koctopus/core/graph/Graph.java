/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alexmy
 */
public final class Graph implements INode<Graph, Object> {
    
    public String id;
    public String type;
    public String label;
    public Map<String, Object> properties;
    public Map<String, Object> propertiesIn;
    public Map<String, Object> propertiesOut;
    
    public Boolean directed;
    
    public List<Gnode> nodes;
    public List<Edge> edges;

    @Override
    public JsonObject toJson(){
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(new Gson().toJson(this, this.getClass()));
        return elem.getAsJsonObject();
    }
    
    @Override
    public Graph fromJson(String json){
        return new Gson().fromJson(json, this.getClass());
    }

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
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the label
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the metadata
     */
    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties the metadata to set
     */
    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getPropertiesIn() {
        return propertiesIn;
    }

    @Override
    public Map<String, Object> getPropertiesOut() {
        return propertiesOut;
    }

    @Override
    public void setPropertiesIn(Map<String, Object> propertiesIn) {
        this.propertiesIn = propertiesIn;
    }

    @Override
    public void setPropertiesOut(Map<String, Object> propertiesOut) {
        this.propertiesOut = propertiesOut;
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

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }
}
