/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.runtime.redis;

import org.lisapark.koctopus.core.event.EventType;

/**
 *
 * @author alexmy
 */
public class StreamReference {

    private String referenceClass;
    private String referenceId;
    private EventType eventtype;

    public StreamReference() {
        this.eventtype = new EventType();
    }

    public StreamReference(String referenceClass, String referenceId, EventType eventtype) {
        this.referenceClass = referenceClass;
        this.referenceId = referenceId;
        this.eventtype = eventtype;
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
    public EventType getEventType() {
        return eventtype;
    }

    /**
     * @param eventtype
     */
    public void setEventType(EventType eventtype) {
        this.eventtype = eventtype;
    }
}
