/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import java.util.Map;
import org.lisapark.koctopus.core.graph.api.NiInput;
import org.lisapark.koctopus.core.graph.api.NiInputs;
/**
 *
 * @author alexmy
 */
public class NodeInputs implements NiInputs<NodeInput> {

    Map<String, NodeInput> sources;

    @Override
    public Map<String, NodeInput> getSources() {
        return sources;
    }

    @Override
    public void setSources(Map<String, NodeInput> sources) {
        this.sources = sources;
    }
}
