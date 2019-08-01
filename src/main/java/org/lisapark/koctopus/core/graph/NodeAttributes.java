/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import java.util.Map;
import org.lisapark.koctopus.core.graph.api.NiAttribute;
import org.lisapark.koctopus.core.graph.api.NiAttributes;

/**
 *
 * @author alexmy
 */
public class NodeAttributes implements NiAttributes<NodeAttribute> {

    Map<String, NodeAttribute> attributes;
    
    @Override
    public Map<String, NodeAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(Map<String, NodeAttribute> attributes) {
        this.attributes = attributes;
    }

}
