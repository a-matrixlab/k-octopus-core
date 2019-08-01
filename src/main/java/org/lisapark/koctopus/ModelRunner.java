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
package org.lisapark.koctopus;

import com.google.gson.Gson;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import org.lisapark.koctopus.core.ModelGraph;
import org.lisapark.koctopus.core.ProcessingModel;
import org.lisapark.koctopus.core.ProcessorBean;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.compiler.esper.EsperCompiler;
import org.lisapark.koctopus.core.parameter.Parameter;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.runtime.ProcessingRuntime;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.ExternalSource;
import org.openide.util.Exceptions;

/**
 *
 * @author Alex Mylnikov (alexmy@lisa-park.com)
 */
public class ModelRunner {
    
    public static final String MODEL_NAME = "modelname";
    public static final String MODEL_JSON = "modeljson";

    private ProcessingModel model;

    public static void main(String args[]) {

        ModelRunner runner = new ModelRunner();
        String string = "Alex. Mylnikov: - 1947/03/02";

        String canonical = runner.getCanonical(string);
    }

    ModelRunner() {}

    /**
     * 
     * @param model 
     */
    public ModelRunner(ProcessingModel model) {
        this.model = model;
    }
 
    /**
     * This constructor serves old version
     *
     * @param model
     * @param sourceParam
     * @param sinkParam
     */
    public ModelRunner(ProcessingModel model, Map sourceParam, Map sinkParam) {

        this.model = model;

        setSourceParams(sourceParam);
        setSinkParams(sinkParam);
    }

    // New version employes the whole model json converted to the ModelBean
    /**
     * New version employs the whole model JSON converted to the ModelBean
     *
     * @param model
     * @param modelGraph
     */
    public ModelRunner(ProcessingModel model, ModelGraph modelGraph) {

        this.model = model;

        if (modelGraph != null) {

            // Update source params
            Set<String> sources = modelGraph.getSources();

            if (sources != null) {
                sources.stream().map((proc) -> new Gson().fromJson(proc, ProcessorBean.class)).forEachOrdered((procBean) -> {
                    setSourceParams(procBean.getParams());
                });
            }

            //Update sink params
            Set<String> sinks = modelGraph.getSinks();

            if (sinks != null) {
                sinks.stream().map((proc) -> new Gson().fromJson(proc, ProcessorBean.class)).forEachOrdered((procBean) -> {
                    setSinkParams(procBean.getParams());
                });
            }

            // Update processors params
            Set<String> procs = modelGraph.getProcessors();

            if (procs != null) {
                procs.stream().map((proc) -> new Gson().fromJson(proc, ProcessorBean.class)).forEachOrdered((procBean) -> {
                    setProcessorParams(procBean.getParams());
                });
            }
        }
    }

    /**
     *
     */
    public void runModel() {

        if (getModel() != null) {
            org.lisapark.koctopus.core.compiler.Compiler compiler = new EsperCompiler();
            PrintStream stream = new PrintStream(System.out);
            compiler.setStandardOut(stream);
            compiler.setStandardError(stream);

            try {

                ProcessingRuntime runtime = compiler.compile(getModel());

                runtime.start();
                runtime.shutdown();
                
                // Reliese all resources used by the model
                getModel().complete();

            } catch (ValidationException e1) {
                System.out.println(e1.getLocalizedMessage() + "\n");
            }
        }
    }

    //Setters with Map argument
    //==========================================================================
    /**
     *
     * @param sourceParam
     */
    private void setSourceParams(Map sourceParams) {
        if (sourceParams != null) {
            Set<ExternalSource> extSources = this.getModel().getExternalSources();
            extSources.stream().map((extSource) -> extSource.getParameters()).forEachOrdered((params) -> {
                params.forEach((param) -> {
                    // get param name from the ProcessingModel
                    String paramName = param.getName();
                    // get corresponding param name from ModelBean
                    String beanParamName = containsKey(sourceParams, paramName);
                    if (!beanParamName.isEmpty()) {
                        try {
                            param.setValueFromString(sourceParams.get(beanParamName).toString());
                        } catch (ValidationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                });
            });
        }
    }

    private void setProcessorParams(Map processorParams) {
        if (processorParams != null) {
            Set<AbstractProcessor> processors = this.getModel().getProcessors();
            processors.stream().map((processor) -> processor.getParameters()).forEachOrdered((params) -> {
                params.forEach((param) -> {
                    // get param name from the ProcessingModel
                    String paramName = param.getName();
                    // get corresponding param name from ModelBean
                    String beanParamName = containsKey(processorParams, paramName);
                    if (!beanParamName.isEmpty()) {
                        try {
                            param.setValueFromString(processorParams.get(beanParamName).toString());
                        } catch (ValidationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                });
            });
        }
    }

    private void setSinkParams(Map sinkParams) {
        if (sinkParams != null) {
            Set<ExternalSink> extSinks = this.getModel().getExternalSinks();
            extSinks.stream().map((extSink) -> extSink.getParameters()).forEachOrdered((Set<Parameter> params) -> {
                params.forEach((param) -> {
                    // get param name from the ProcessingModel
                    String paramName = param.getName();
                    // get corresponding param name from ModelBean
                    String beanParamName = containsKey(sinkParams, paramName);
                    if (!beanParamName.isEmpty()) {
                        try {
                            param.setValueFromString(sinkParams.get(beanParamName).toString());
                        } catch (ValidationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                });
            });
        }
    }

    /**
     * Convert strings to the canonical view: only alphanumerical
     *
     * @param string
     * @return
     */
    private String getCanonical(String string) {
        String retString = string.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(" ", "");
        return retString.toLowerCase();

    }

    /**
     * Compares two canonical string and returns original name from the map
     *
     * @param map
     * @param string
     * @return
     */
    private String containsKey(Map<String, Object> map, String string) {

        String contains = "";

        for (String entry : map.keySet()) {
            String entryStr = getCanonical(entry);
            String stringStr = getCanonical(string);
            if (entryStr.equalsIgnoreCase(stringStr)) {
                contains = entry;
                break;
            }

        }

        return contains;
    }

    /**
     * @return the model
     */
    public ProcessingModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(ProcessingModel model) {
        this.model = model;
    }
}
