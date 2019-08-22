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

import com.fasterxml.uuid.Generators;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.lisapark.koctopus.core.event.Attribute;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lisapark.koctopus.core.graph.Gnode;
import org.lisapark.koctopus.core.parameter.StringParameter;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com) 03/06/2013 Alex Mylnikov
 * added toJson() method
 */
@Persistable
public class ProcessingModel extends AbstractNode implements Validatable {
    
    static final Logger LOG = Logger.getLogger(ProcessingModel.class.getName());

    private DateTime lastSaved;

    private final int MODEL_NAME_ID = 1;
    private final int TRANSPORT_URL_ID = 2;
    private final int DESCRIPTION_ID = 3;
    private final int AUTHOR_ID = 4;
    private final int AUTHOT_EMAIL_ID = 5;
    private final int SERVICE_URL_ID = 6;
    private final int LUCENE_INDEX_ID = 7;
    private final int MODEL_JSON_FILE_ID = 8;

    private final Set<ExternalSource> externalSources = Sets.newHashSet();
    private final Set<AbstractProcessor> processors = Sets.newHashSet();
    private final Set<ExternalSink> externalSinks = Sets.newHashSet();

    public ProcessingModel(String modelName) {
        super(Generators.timeBasedGenerator().generate());
        init(modelName, "redis://localhost");
    }

    public ProcessingModel(String modelName, String transportUrl) {
        super(Generators.timeBasedGenerator().generate());
        init(modelName, transportUrl);
    }

    private void init(String modelName, String transportUrl) {
        checkArgument(modelName != null, "modelName cannot be null");
        checkArgument(modelName != null, "modelRepo cannot be null");

        this.setName(modelName);
        this.addParameter(StringParameter.stringParameterWithIdAndName(MODEL_NAME_ID, "Model Name").defaultValue(modelName).build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(TRANSPORT_URL_ID, "Transport Url").defaultValue(transportUrl).build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(DESCRIPTION_ID, "Description").defaultValue("Short Model description.").build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(AUTHOR_ID, "Author").defaultValue("Full author name.").build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(AUTHOT_EMAIL_ID, "Author email").defaultValue("Author email.").build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(SERVICE_URL_ID, "Service Url").defaultValue("")
                .description("URL to K-Octopus Service (engine).").build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(LUCENE_INDEX_ID, "Model Lucene Index").defaultValue("")
                .description("Path or URL to Lucene Index Derectory for the current model.").build());
        this.addParameter(StringParameter.stringParameterWithIdAndName(MODEL_JSON_FILE_ID, "Json file path").defaultValue("")
                .description("Path or URL to Json file for the current model.").build());
    }

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
     * @return the serviceUrl
     */
    public String getServiceUrl() {
        return (String) this.getParameter(SERVICE_URL_ID).getValue();
    }

    /**
     * @param serviceUrl the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        try {
            this.getParameter(SERVICE_URL_ID).setValueFromString(serviceUrl);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    /**
     * @return the transportUrl
     */
    public String getTransportUrl() {
        return (String) this.getParameter(TRANSPORT_URL_ID).getValue();
    }

    /**
     * @param transportUrl the transportUrl to set
     */
    public void setTransportUrl(String transportUrl) {
        try {
            this.getParameter(TRANSPORT_URL_ID).setValueFromString(transportUrl);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    public String getLuceneIndex() {
        return (String) this.getParameter(LUCENE_INDEX_ID).getValue();
    }
    
    public void setLuceneIndex(String luceneIndex) {
        try {
            this.getParameter(LUCENE_INDEX_ID).setValueFromString(luceneIndex);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }

    public String getModelJsonFile() {
        return (String) this.getParameter(MODEL_JSON_FILE_ID).getValue();
    }
    
    public void setModelJsonFile(String jsonFile) {
        try {
            this.getParameter(MODEL_JSON_FILE_ID).setValueFromString(jsonFile);
        } catch (ValidationException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
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

    public void addProcessor(AbstractProcessor processor) {
        checkArgument(processor != null, "processor cannot be null");
        processors.add(processor);
    }

    /**
     * Removes the specified
     * {@link org.lisapark.koctopus.core.processor.AbstractProcessor} from this
     * model. Doing so will remove any connections between this processor and
     * any other sink or processor.
     *
     * @param processor to remove from model
     */
    public void removeProcessor(AbstractProcessor processor) {
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
        for (AbstractProcessor candidateProcessor : processors) {
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
        for (AbstractProcessor candidateProcessor : processors) {
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

    public Set<AbstractProcessor> getProcessors() {
        return ImmutableSet.copyOf(processors);
    }

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
        for (AbstractProcessor<?> processor : processors) {
            processor.validate();
        }
        for (ExternalSink sink : externalSinks) {
            sink.validate();
        }
    }

    /**
     * Relieves all resources by running complete() method for all model's
     * sources, processors and sinks.
     */
    @Override
    public void complete() {
        Set<ExternalSource> sourceset = getExternalSources();
        Set<AbstractProcessor> processorset = getProcessors();
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

    @Override
    public Reproducible newInstance() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Copyable copyOf() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Reproducible newInstance(Gnode gnode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public String toJson() {

        ModelGraph modelGraph = new ModelGraph();

        modelGraph.setName(getName());
        modelGraph.setSources(buildSources());
        modelGraph.setProcessors(buildProcessors());
        modelGraph.setSinks(buildSinks());

        return new Gson().toJson(modelGraph, ModelGraph.class);
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

        Set<AbstractProcessor> processorset = getProcessors();
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

}
