/* 
 * Copyright (C) 2013 Lisa Park, Inc. (www.lisa-park.net)
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
package org.lisapark.koctopus.core.source.external;

import org.lisapark.koctopus.core.AbstractNode;
import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.Persistable;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.source.Source;

import java.util.UUID;
import org.lisapark.koctopus.core.parameter.Parameter;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public abstract class AbstractExternalSource extends AbstractNode implements Source {

    private static final int TRANSPORT_PARAMETER_ID = 77777;
    private static final int SERVICE_PARAMETER_ID = 88888;
    private static final int LUCENE_PARAMETER_ID = 99999;
    
    private Output output;

    protected AbstractExternalSource(UUID id, String name, String description) {
        super(id, name, description);
        
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

    protected AbstractExternalSource(UUID id) {
        super(id);
    }

    protected AbstractExternalSource(UUID id, AbstractExternalSource copyFromNode) {
        super(id, copyFromNode);
        setOutput(copyFromNode.getOutput().copyOf());
    }

    protected AbstractExternalSource(AbstractExternalSource copyFromNode) {
        super(copyFromNode);
        setOutput(copyFromNode.getOutput().copyOf());
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


    public abstract CompiledExternalSource compile() throws ValidationException;
    
    public abstract <T extends AbstractExternalSource> CompiledExternalSource compile(T source) throws ValidationException;
    
//    public abstract CompiledExternalSource compile(String json) throws ValidationException;

    @Override
    public abstract Source newInstance();

    @Override
    public abstract Source copyOf();
    
    @Override
    public Output getOutput() {
        return output;
    }

    public final void setOutput(Output output) {
        this.output = output;
    }
}

