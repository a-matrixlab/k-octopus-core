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
package org.lisapark.koctopus.core.processor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.lisapark.koctopus.core.AbstractNode;
import org.lisapark.koctopus.core.Input;
import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.Persistable;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.memory.Memory;
import org.lisapark.koctopus.core.memory.MemoryProvider;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.core.sink.Sink;
import org.lisapark.koctopus.core.source.Source;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.lisapark.koctopus.core.graph.Gnode;
import org.lisapark.koctopus.core.graph.api.Vocabulary;
import org.lisapark.koctopus.core.transport.TransportReference;

/**
 * A {@link AbstractProcessor} is a program unit that has one or more {@link Input}s and potentially produces an {@link Output}.In addition to {@link Input}s and an {@link Output}, a processor can be configured with additional {@link org.lisapark.koctopus.core.parameter.Parameter}s
 that affect the behavior of the processor.
 *
 * @author dave sinclair(david.sinclair@lisa-park.com), alex mylnikov (alexmy@lisa-park.com)
 * @param <MEMORY_TYPE>
 * @see org.lisapark.koctopus.core.Input
 * @see org.lisapark.koctopus.core.Output
 * @see org.lisapark.koctopus.core.parameter.Parameter
 */
@Persistable
public abstract class AbstractProcessor<MEMORY_TYPE> extends AbstractNode implements Source, Sink {
    
    private static final int KIND_PARAMETER_ID = 33333;
    private static final int REPO_PATH_PARAMETER_ID = 44444;
    private static final int VERSION_PARAMETER_ID = 55555;
    private static final int LANGUAGE_PARAMETER_ID = 66666;
    private static final int TRANSPORT_PARAMETER_ID = 77777;
    private static final int SERVICE_PARAMETER_ID = 88888;
    private static final int LUCENE_PARAMETER_ID = 99999;
    
    /**
     * A processor will be given zero or more inputs in order to perform its processing; this will be the
     * list of all of these inputs.
     */
    private final List<ProcessorInput> inputs = Lists.newLinkedList();

    /**
     * Any processor with more than one {@link ProcessorInput} requires a join for each pair of inputs; this is the
     * list of these joins
     */
    private final List<ProcessorJoin> joins = Lists.newLinkedList();

    /**
     * A processor produces an output after its processing.
     */
    private ProcessorOutput output;

    /**
     * Constructor that takes id of processor, name and description.
     *
     * @param id          of processor
     * @param name        of processor
     * @param description of processor
     */
    protected AbstractProcessor(UUID id, String name, String description) {
        super(id, name, description);
        
        super.addParameter(
                Parameter.stringParameterWithIdAndName(KIND_PARAMETER_ID, "Kind of processor").
                        description("Kind of processors one of 3: source, sink, processor.").
                        defaultValue(Vocabulary.PROCESSOR).required(true));
        
        super.addParameter(
                Parameter.stringParameterWithIdAndName(REPO_PATH_PARAMETER_ID, "Repo Path").
                        description("Path to processor Repository. Default value to local Maven Repository.").
                        defaultValue("java").required(true));
        
        super.addParameter(
                Parameter.stringParameterWithIdAndName(VERSION_PARAMETER_ID, "Version").
                        description("Path to processor Repository. Default value to local Maven Repository.").
                        defaultValue("set version").required(true));
        
        super.addParameter(
                Parameter.stringParameterWithIdAndName(LANGUAGE_PARAMETER_ID, "Program Lang").
                        description("Programming language used to right this processor.").
                        defaultValue("java"));
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

    /**
     * Copy constructor for creating a <b>new</b> processor based off of the copyFromProcessor. Note that we are using the
     * {@link org.lisapark.koctopus.core.Reproducible} interface on {@link Input}s, {@link org.lisapark.koctopus.core.parameter.Parameter}s and
     * {@link Output} if there is one.
     *
     * @param id                of new processor
     * @param copyFromProcessor that we are getting copies from
     */
    protected AbstractProcessor(UUID id, AbstractProcessor<MEMORY_TYPE> copyFromProcessor) {
        super(id, copyFromProcessor);
        deepCopyOf(copyFromProcessor);
    }

    /**
     * Copy constructor for creating a new processor based off of an <b>exact</b> copy of the copyFromProcessor.
     * Note that we are using the {@link org.lisapark.koctopus.core.Reproducible} interface on {@link Input}s,
     * {@link Parameter}s and {@link Output} if there is one.
     *
     * @param copyFromProcessor that we are getting copies from
     */
    protected AbstractProcessor(AbstractProcessor<MEMORY_TYPE> copyFromProcessor) {
        super(copyFromProcessor);
        deepCopyOf(copyFromProcessor);
    }
    
    @SuppressWarnings("unchecked")
    public void setKind(String kind) throws ValidationException {
        getParameter(KIND_PARAMETER_ID).setValue(kind);
    }

    public String getKind() {
        return getParameter(KIND_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setRepoPath(String repoPath) throws ValidationException {
        getParameter(REPO_PATH_PARAMETER_ID).setValue(repoPath);
    }

    public String getRepoPath() {
        return getParameter(REPO_PATH_PARAMETER_ID).getValueAsString();
    }

    @SuppressWarnings("unchecked")
    public void setVersion(String version) throws ValidationException {
        getParameter(VERSION_PARAMETER_ID).setValue(version);
    }

    public String getVersion() {
        return getParameter(VERSION_PARAMETER_ID).getValueAsString();
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

    private void deepCopyOf(AbstractProcessor<MEMORY_TYPE> copyFromProcessor) {
        
        copyFromProcessor.getInputs().forEach((input) -> {
            this.addInput(input.copyOf());
        });
        // we can't directly copy the joins, because they depend on the newly copied inputs
        copyFromProcessor.getJoins().forEach((joinToCopy) -> {
            // we need to find the new inputs by the original ID
            ProcessorInput firstInput = getInputById(joinToCopy.getFirstInput().getId());
            ProcessorInput secondInput = getInputById(joinToCopy.getSecondInput().getId());

            Attribute firstInputAttr = null;
            if (joinToCopy.getFirstInputAttribute() != null) {
                // note that this copy HAS to happen after the firstInput has been copied
                firstInputAttr = firstInput.getSource().getOutput().getAttributeByName(joinToCopy.getFirstInputAttributeName());
            }
            Attribute secondInputAttr = null;
            if (joinToCopy.getSecondInputAttribute() != null) {
                // note that this copy HAS to happen after the secondInput has been copied
                secondInputAttr = secondInput.getSource().getOutput().getAttributeByName(joinToCopy.getSecondInputAttributeName());
            }
            this.addJoin(new ProcessorJoin(firstInput, firstInputAttr, secondInput, secondInputAttr));
        });
        this.setOutput(copyFromProcessor.getOutput().copyOf());
    }

    protected void addInput(ProcessorInput.Builder input) {
        addInput(input.build());
    }

    protected void addInput(ProcessorInput input) {
        this.inputs.add(input);
    }

    protected void addJoin(ProcessorJoin join) {
        this.joins.add(join);
    }

    protected void addJoin(ProcessorInput firstInput, ProcessorInput secondInput) {
        this.joins.add(new ProcessorJoin(firstInput, secondInput));
    }

    protected void setOutput(ProcessorOutput.Builder output) throws ValidationException {
        setOutput(output.build());
    }

    protected ProcessorInput getInputById(int id) {
        ProcessorInput input = null;

        for (ProcessorInput candidateInput : inputs) {
            if (candidateInput.getId() == id) {
                input = candidateInput;
                break;
            }
        }

        return input;
    }

    @Override
    public ProcessorOutput getOutput() {
        return output;
    }

    public void setOutputAttributeName(String name) throws ValidationException {
        output.setAttributeName(name);
    }

    public String getOutputAttributeName() {
        return output.getAttributeName();
    }

    /**
     *
     * @return
     */
    @Override
    public List<ProcessorInput> getInputs() {
        return ImmutableList.copyOf(inputs);
    }

    public void setOutput(ProcessorOutput output) {
        this.output = output;
    }

    public List<ProcessorJoin> getJoins() {
        return ImmutableList.copyOf(joins);
    }

    /**
     * This method will check whether the source and attribute are in use on the any of the {@link #getInputs()} of
     * this processor.
     *
     * @param source    to check if it is in use by this processor
     * @param attribute to check if it is in use by this processor
     * @return true if the source and attribute is in use
     */
    public boolean isConnectedTo(Source source, Attribute attribute) {
        boolean connected = false;

        for (ProcessorInput input : inputs) {
            if (input.isConnectedTo(source, attribute)) {
                connected = true;
                break;
            }
        }

        return connected;
    }

    /**
     * This method will check whether the source is in use on the any of the {@link #getInputs()} of  this processor.
     *
     * @param source to check if it is in use by this processor
     * @return true if the source is in use
     */
    @Override
    public boolean isConnectedTo(Source source) {
        boolean connected = false;

        for (Input input : inputs) {
            if (input.isConnectedTo(source)) {
                connected = true;
                break;
            }
        }

        return connected;
    }

    /**
     * This method will disconnect, i.e. remove the specified source from any {@link #getInputs()} is attached to.
     *
     * @param source to disconnect
     */
    @Override
    public void disconnect(Source source) {
        inputs.stream().filter((input) -> (input.isConnectedTo(source))).forEachOrdered((input) -> {
            input.clearSource();
        });
    }

    /**
     * The {@link org.lisapark.koctopus.core.processor.AbstractProcessor} will validate it's {@link #parameters}, {@link #getInputs()},
     * {@link #joins} and {@link #output} in that order. Any subclass that wants to do cross parameter validation should override
     * this method to do so.
     *
     * @throws ValidationException if there is a validation problem
     */
    @Override
    public void validate() throws ValidationException {
        super.validate();

        for (ProcessorInput input : inputs) {
            input.validate();
        }
        for (ProcessorJoin join : joins) {
            join.validate();
        }

        if (output != null) {
            output.validate();
        } else {
            throw new ValidationException("Please specify the output for this processor");
        }
    }

    /**
     * Subclasses need to implement this method to return a <b>new</b> {@link AbstractProcessor} based on this one.
     *
     * @return new processor
     */
    @Override
    public abstract AbstractProcessor<MEMORY_TYPE> newInstance();
    
    @Override
    public abstract AbstractProcessor<MEMORY_TYPE> newInstance(Gnode gnode);

    @Override
    public abstract AbstractProcessor<MEMORY_TYPE> copyOf();

    public abstract CompiledProcessor<MEMORY_TYPE> compile() throws ValidationException;
    
    public abstract <T extends AbstractProcessor> CompiledProcessor<MEMORY_TYPE> compile(T processor) throws ValidationException;
    
//    public abstract CompiledProcessor<MEMORY_TYPE> compile(String json) throws ValidationException;

    public Memory<MEMORY_TYPE> createMemoryForProcessor(MemoryProvider memoryProvider) {
        return null;
    }
    
    public abstract Map<String, TransportReference> getReferences();

    public abstract void setReferences(Map<String, TransportReference> sourceref);
}
