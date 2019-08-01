package org.lisapark.koctopus.core.graph.api;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Map;

/**
 *
 * @author alexmy
 * @param <I>
 */
public interface NiInputs <I extends NiInput> {
    Map<String, I> getSources();
    void setSources(Map<String, I> source);
}
