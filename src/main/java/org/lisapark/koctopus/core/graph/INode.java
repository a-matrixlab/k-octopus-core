/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import com.google.gson.JsonObject;
import java.util.Map;

/**
 *
 * @author alexmy
 * @param <T>
 * @param <O>
 */
public interface INode<T, O> {
    /**
     * @return the id
     */
    public String getId();

    /**
     * @param id the id to set
     */
    public void setId(String id);

    /**
     * @return the type
     */
    public String getType();

    /**
     * @param type the type to set
     */
    public void setType(String type);

    /**
     * @return the label
     */
    public String getLabel();

    /**
     * @param label the label to set
     */
    public void setLabel(String label);

    /**
     * @return the metadata
     */
    public Map<String, O> getProperties();
    
    public Map<String, O> getPropertiesIn();
    
    public Map<String, O> getPropertiesOut();

    /**
     * @param properties
     */
    public void setProperties(Map<String, O> properties);
    
    public void setPropertiesIn(Map<String, O> propertiesIn);
    
    public void setPropertiesOut(Map<String, O> propertiesOut);
    
    public JsonObject toJson();
    
    public T fromJson(String json);
}
