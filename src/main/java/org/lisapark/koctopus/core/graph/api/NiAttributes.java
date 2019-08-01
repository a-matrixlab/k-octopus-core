/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph.api;

import java.util.Map;

/**
 *
 * @author alexmy
 * @param <A>
 */
public interface NiAttributes<A extends NiAttribute>  {
    Map<String, A> getAttributes();
    void setAttributes(Map<String, A> attrs);
}
