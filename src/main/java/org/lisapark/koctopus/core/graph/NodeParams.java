/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import java.util.Map;
import org.lisapark.koctopus.core.graph.api.NiParam;
import org.lisapark.koctopus.core.graph.api.NiParams;

/**
 *
 * @author alexmy
 */
public class NodeParams implements NiParams<NodeParam> {

    Map<Integer, NodeParam> params;

    @Override
    public Map<Integer, NodeParam> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<Integer, NodeParam> params) {
        this.params = params;
    }
}
