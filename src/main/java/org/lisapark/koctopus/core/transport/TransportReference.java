/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.transport;

import java.util.Map;
import org.lisapark.koctopus.core.graph.NodeAttribute;

/**
 *
 * @author alexmy
 */
public class TransportReference  {

    private String referenceClass;
    private String referenceId;
    private Map<String, NodeAttribute> attributes;

    public TransportReference() {
        
    }

    public TransportReference(String referenceClass, String referenceId, Map<String, NodeAttribute> attributes) {
        this.referenceClass = referenceClass;
        this.referenceId = referenceId;
        this.attributes = attributes;
    }

    /**
     * @return the sourceClassRef
     */
    public String getReferenceClass() {
        return referenceClass;
    }

    /**
     * @param referenceClass the sourceClassRef to set
     */
    public void setReferenceClass(String referenceClass) {
        this.referenceClass = referenceClass;
    }

    /**
     * @return the sourceIdRef
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * @param referenceId
     */
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * @return the sourceAttributes
     */
    public Map<String, NodeAttribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes
     */
    public void setAttributes(Map<String, NodeAttribute> attributes) {
        this.attributes = attributes;
    }
}
