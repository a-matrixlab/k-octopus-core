/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph.api;

/**
 *
 * @author alexmy
 * @param <T>
 */
public interface NiParam<T> {

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
     * @return the className
     */
    public String getClassName();

    /**
     * @param className the className to set
     */
    public void setClassName(String className);

    /**
     * @return the value
     */
    public T getValue();

    /**
     * @param value the value to set
     */
    public void setValue(T value);
}
