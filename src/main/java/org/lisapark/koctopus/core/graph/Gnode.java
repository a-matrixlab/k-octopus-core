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
import java.util.Map;

/**
 *
 * @author alexmy
 */
public final class Gnode implements INode<Gnode, Object>{
    
    public String id;
    public String type;
    public String label;    
    public Map<String, Object> properties;
    private Map<String, Object> propertiesIn;
    private Map<String, Object> propertiesOut;

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
     
    @Override
    public JsonObject toJson(){
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(new Gson().toJson(this, this.getClass()));
        return elem.getAsJsonObject();
    }
    
    @Override
    public Gnode fromJson(String json){
        return new Gson().fromJson(json, this.getClass());
    }
}
