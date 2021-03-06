/* 
 * Copyright (C) 2019 Lisa Park, Inc. (www.lisa-park.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lisapark.koctopus.core.sink.external;

import java.util.UUID;
import org.lisapark.koctopus.core.AbstractNode;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.parameter.Parameter;

/**
 *
 * @author alexmy
 */
public abstract class AbstractExternalSink extends AbstractNode implements ExternalSink {

    private static final int TRANSPORT_PARAMETER_ID = 77777;
    private static final int SERVICE_PARAMETER_ID = 88888;
    private static final int LUCENE_PARAMETER_ID = 99999;
    
    protected AbstractExternalSink(UUID uuid, String name, String description) {
        super(uuid, name, description);
        super.addParameter(
                Parameter.stringParameterWithIdAndName(TRANSPORT_PARAMETER_ID, "Transport URL").
                        description("Transport URL.").
                        defaultValue(""));
        super.addParameter(
                Parameter.stringParameterWithIdAndName(SERVICE_PARAMETER_ID, "Service URL").
                        description("Service URL.").
                        defaultValue(""));
        super.addParameter(
                Parameter.stringParameterWithIdAndName(LUCENE_PARAMETER_ID, "Lucene URL").
                        description("Lucene URL.").
                        defaultValue(""));
    }    
    
    @SuppressWarnings("unchecked")
    public void setLuceneIndex(String luceneIndex) throws ValidationException {
        getParameter(LUCENE_PARAMETER_ID).setValue(luceneIndex);
    }

    public String getLuceneIndex() {
        return getParameter(LUCENE_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setTransportUrl(String transportUrl) throws ValidationException {
        getParameter(TRANSPORT_PARAMETER_ID).setValue(transportUrl);
    }

    public String getTransportUrl() {
        return getParameter(TRANSPORT_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setServiceUrl(String serviceUrl) throws ValidationException {
        getParameter(SERVICE_PARAMETER_ID).setValue(serviceUrl);
    }

    public String getServiceUrl() {
        return getParameter(SERVICE_PARAMETER_ID).getValueAsString();
    }

}
