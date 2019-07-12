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
package org.lisapark.koctopus.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.processor.Processor;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.gson.Gson;
import java.util.UUID;
import org.lisapark.koctopus.core.parameter.ConversionException;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.core.parameter.StringParameter;
import org.openide.util.Exceptions;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com) 03/06/2013 Alex Mylnikov
 * added toJson() method
 */
@Persistable
public class ProcessingModel extends AbstractNode implements Validatable {

    private DateTime lastSaved;
    
    private final Set<ExternalSource> externalSources = Sets.newHashSet();
    private final Set<Processor> processors = Sets.newHashSet();
    private final Set<ExternalSink> externalSinks = Sets.newHashSet();
//    private String modelAuthor;
//    private String modelRepo;
    private String modelName;

    public ProcessingModel(String modelName) {
        super(UUID.randomUUID());
        checkArgument(modelName != null, "modelName cannot be null");
        this.setModelName(modelName);
        this.addParameter(StringParameter.stringParameterWithIdAndName(1, "Model Name").defaultValue(modelName).build());
    }
    
    public ProcessingModel(String modelName, String modelRepo) {
        super(UUID.randomUUID());
        checkArgument(modelName != null, "modelName cannot be null");
        checkArgument(modelName != null, "modelRepo cannot be null");
        
        this.setModelName(modelName);
        this.addParameter(StringParameter.stringParameterWithIdAndName(1, "Model Name").defaultValue(modelName).build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(1, "Model Repo").defaultValue("localhost:6379").build());
    }

//    public void setModelName(String modelName) {
//        checkArgument(modelName != null, "modelName cannot be null");
//        this.modelName = modelName;
//        this.addParameter(StringParameter.stringParameterWithIdAndName(1, "Model Name").defaultValue(modelName).build());
//    }

    public DateTime getLastSaved() {
        return lastSaved;
    }

    public void setLastSaved(DateTime lastSaved) {
        checkArgument(lastSaved != null, "lastSaved cannot be null");
        this.lastSaved = lastSaved;
    }

    public void addExternalEventSource(ExternalSource source) {
        checkArgument(source != null, "source cannot be null");
        externalSources.add(source);
    }

    /**
     * Removes the specified
     * {@link org.lisapark.koctopus.core.source.external.ExternalSource} from
     * this model. Doing so will remove any connections between this source and
     * any other sink or processor.
     *
     * @param source to remove from model
     */
    public void removeExternalEventSource(ExternalSource source) {
        checkArgument(externalSources.contains(source), "Model does not contain source " + source);

        externalSinks.stream().filter((candidateSink) -> (candidateSink.isConnectedTo(source))).forEachOrdered((candidateSink) -> {
            candidateSink.disconnect(source);
        });

        processors.stream().filter((candidateProcessor) -> (candidateProcessor.isConnectedTo(source))).forEachOrdered((candidateProcessor) -> {
            candidateProcessor.disconnect(source);
        });

        externalSources.remove(source);
    }

    public void addExternalSink(ExternalSink sink) {
        checkArgument(sink != null, "sink cannot be null");
        externalSinks.add(sink);
    }

    /**
     * Removes the specified
     * {@link org.lisapark.koctopus.core.sink.external.ExternalSink} from this
     * model.
     *
     * @param sink to remove from model
     */
    public void removeExternalEventSink(ExternalSink sink) {
        checkArgument(externalSinks.contains(sink), "Model does not contain sink " + sink);

        externalSinks.remove(sink);
    }

    public void addProcessor(Processor processor) {
        checkArgument(processor != null, "processor cannot be null");
        processors.add(processor);
    }

    /**
     * Removes the specified
     * {@link org.lisapark.koctopus.core.processor.Processor} from this model.
     * Doing so will remove any connections between this processor and any other
     * sink or processor.
     *
     * @param processor to remove from model
     */
    public void removeProcessor(Processor processor) {
        checkArgument(processors.contains(processor), "Model does not contain sink " + processor);

        externalSinks.stream().filter((candidateSink) -> (candidateSink.isConnectedTo(processor))).forEachOrdered((candidateSink) -> {
            candidateSink.disconnect(processor);
        });
        processors.stream().filter((candidateProcessor) -> (candidateProcessor.isConnectedTo(processor))).forEachOrdered((candidateProcessor) -> {
            candidateProcessor.disconnect(processor);
        });
        processors.remove(processor);
    }

    /**
     * Returns true if the specified source's {@link Attribute} is in use
     * anywhere in the current model.
     *
     * @param source of attribute
     * @param attribute to check usage of
     * @return true if the attribute of the specified source is in use
     */
    public boolean isExternalSourceAttributeInUse(ExternalSource source, Attribute attribute) {
        checkArgument(source != null, "source cannot be null");
        checkArgument(attribute != null, "attribute cannot be null");
        boolean inUse = false;

        for (ExternalSink candidateSink : externalSinks) {
            if (candidateSink.isConnectedTo(source)) {
                inUse = true;
            }
        }
        for (Processor candidateProcessor : processors) {
            if (candidateProcessor.isConnectedTo(source, attribute)) {
                inUse = true;
            }
        }
        return inUse;
    }

    /**
     * Returns true if the specified {@link ExternalSource} is in use anywhere
     * in the current model.
     *
     * @param source to check
     * @return true if the specified source is in use
     */
    public boolean isExternalSourceInUse(ExternalSource source) {
        checkArgument(source != null, "source cannot be null");
        boolean inUse = false;

        for (ExternalSink candidateSink : externalSinks) {
            if (candidateSink.isConnectedTo(source)) {
                inUse = true;
            }
        }

        for (Processor candidateProcessor : processors) {
            if (candidateProcessor.isConnectedTo(source)) {

                inUse = true;
            }
        }

        return inUse;
    }

    public Set<ExternalSource> getExternalSources() {
        return ImmutableSet.copyOf(externalSources);
    }

    public Set<ExternalSink> getExternalSinks() {
        return ImmutableSet.copyOf(externalSinks);
    }

    public Set<Processor> getProcessors() {
        return ImmutableSet.copyOf(processors);
    }

//    public String getModelName() {
//        return modelName;
//    }

    /**
     * Validates the {@link #externalSources}, {@link #processors} and
     * {@link #externalSinks} for this model.
     *
     * @throws ValidationException thrown if any source, processor, or sink is
     * invalid.
     */
    @Override
    public void validate() throws ValidationException {
        // todo verify all connections??
        for (ExternalSource source : externalSources) {
            source.validate();
        }

        for (Processor<?> processor : processors) {
            processor.validate();
        }

        for (ExternalSink sink : externalSinks) {
            sink.validate();
        }
    }

    /**
     * Relieves all resources by running complete() method for all
     * model's sources, processors and sinks.
     */
    @Override
    public void complete() {
        Set<ExternalSource> sourceset = getExternalSources();
        Set<Processor> processorset = getProcessors();
        Set<ExternalSink> sinkset = getExternalSinks();

        sourceset.forEach((item) -> {
            item.complete();
        });
        processorset.forEach((item) -> {
            item.complete();
        });
        sinkset.forEach((item) -> {
            item.complete();
        });
    }

    /**
     *
     * @return
     */
    @Override
    public String toJson() {

        ModelGraph modelBean = new ModelGraph();

        modelBean.setModelName(getModelName());
        modelBean.setSources(buildSources());
        modelBean.setProcessors(buildProcessors());
        modelBean.setSinks(buildSinks());

        return new Gson().toJson(modelBean, ModelGraph.class);
//        return new Gson().toJson(this, this.getClass());
    }

    @Override
    public String toString() {
        return toJson();
    }

    // Set of supporting private methods
    //==========================================================================
    private Set<String> buildSources() {

        Set<ExternalSource> sourceset = getExternalSources();
        Set<String> sources = Sets.newHashSet();

        sourceset.forEach((item) -> {
            sources.add(item.toJson());
        });

        return sources;
    }

    private Set<String> buildProcessors() {

        Set<Processor> processorset = getProcessors();
        Set<String> _processors = Sets.newHashSet();

        processorset.forEach((item) -> {
            _processors.add(item.toJson());
        });

        return _processors;
    }

    private Set<String> buildSinks() {

        Set<ExternalSink> sinkset = getExternalSinks();
        Set<String> sinks = Sets.newHashSet();

        sinkset.forEach((item) -> {
            sinks.add(item.toJson());
        });

        return sinks;
    }

//    public String getModelAuthor() {
//        return this.modelAuthor;
//    }

    /**
     * @param modelAuthor the modelAuthor to set
     */
//    public void setModelAuthor(String modelAuthor) {
//        this.modelAuthor = modelAuthor;
//    }

//    public String getModelRepo() {
//        return modelRepo;
//    }

    /**
     * @param modelRepo the modelRepo to set
     * @return 
     */
//    public void setModelRepo(String modelRepo) {
//        this.modelRepo = modelRepo;
//    }

    @Override
    public Reproducible newInstance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Copyable copyOf() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param modelName the modelName to set
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
