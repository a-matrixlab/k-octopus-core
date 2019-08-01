/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import java.util.Map;
import org.lisapark.koctopus.core.graph.api.NiAttribute;
import org.lisapark.koctopus.core.graph.api.NiInput;

/**
 *
 * @author alexmy
 */
public class NodeInput implements NiInput<NodeAttribute> {

    Integer id;
    String name;
    String sourceClassName;
    String sourceId;
    Map<String, NodeAttribute> attributes;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }

    @Override
    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public Map<String, NodeAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(Map<String, NodeAttribute> attributes) {
        this.attributes = attributes;
    }
}
