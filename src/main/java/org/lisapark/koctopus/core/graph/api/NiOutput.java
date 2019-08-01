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
public interface NiOutput<A extends NiAttribute> {

    /**
     * @return the id
     */
    public Integer getId();

    /**
     * @param id the id to set
     */
    public void setId(Integer id);

    /**
     * @return the name
     */
    public String getName();

    /**
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * @return the attributes
     */
    public Map<String, A> getAttributes();

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, A> attributes);
}
