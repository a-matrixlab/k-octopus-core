/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph.api;

import java.util.Map;
import org.lisapark.koctopus.core.graph.api.NiParam;

/**
 *
 * @author alexmy
 * @param <P>
 */
public interface NiParams <P extends NiParam>{
    Map<Integer, P> getParams();
    void setParams(Map<Integer, P> params);
}
