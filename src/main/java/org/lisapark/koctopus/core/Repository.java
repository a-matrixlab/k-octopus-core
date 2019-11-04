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

import java.net.MalformedURLException;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.source.external.AbstractExternalSource;

import org.lisapark.koctopus.core.sink.external.AbstractExternalSink;
import org.lisapark.koctopus.util.Pair;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface Repository {
   
    // Work with model list locally
    //==========================================================================
//    List<ProcessingModel>   getProcessingModelsByName(String name) throws RepositoryException;
    
    // Work with model list remotely
    //==========================================================================
//    List<ProcessingModel>   getProcessingModelsByNameOnServer(String name, String turl,
//            Integer tport, String tuid, String tpsw) throws RepositoryException;
//    
//    List<String>            getModelJsonList(String query, String jurl);
//    
//    List<String>            getModelNameList(String query, String jurl);

    // Get selected model
    //==========================================================================
//    ProcessingModel         getProcessingModelByName(String name) throws RepositoryException;
    
//    ProcessingModel         getProcessingModelByName(String name, String turl,
//            Integer tport, String tuid, String tpsw) throws RepositoryException;
    
//    void loadAllProcessors(List<AbstractExternalSource> sources, List<AbstractExternalSink> sinks, List<AbstractProcessor> processors);
    
    // Select processors
    //==========================================================================
//    List<AbstractExternalSink>          getAllExternalSinkTemplates(List<String> sinkJars) throws RepositoryException;    
//    Set<ExternalSink>           getExternalSinkTemplates(String name) throws RepositoryException;    
//    Set<ExternalSink>           getExternalSinkTemplateByClassName(String name) throws RepositoryException;
    
//    List<AbstractExternalSource>        getAllExternalSourceTemplates(List<String> sourceJars) throws RepositoryException;    
//    Set<ExternalSource>         getExternalSourceTemplates(String name) throws RepositoryException;    
//    Set<ExternalSource>         getExternalSourceTemplateByClassName(String name) throws RepositoryException;
    
//    List<AbstractProcessor>     getAllProcessorTemplates(List<String> processorJars) throws RepositoryException;    
//    Set<AbstractProcessor>      getProcessorTemplates(String name) throws RepositoryException;    
//    Set<AbstractProcessor>      getProcessorTemplateByClassName(String name) throws RepositoryException;
     
    // Save model
    //==========================================================================
//    void saveProcessingModel(ProcessingModel model) throws RepositoryException;

//    void saveProcessingModelOnServer(ProcessingModel model, String turl,
//            int tport, String tuid, String tpsw) throws RepositoryException;
    
        
    AbstractExternalSource  getAbstractExternalSourceByName(Pair<String, String> jar_type) 
            throws InstantiationException, IllegalAccessException, MalformedURLException;
    
    AbstractExternalSink getAbstractExternalSinkByName(Pair<String, String> jar_type) 
            throws InstantiationException, IllegalAccessException, MalformedURLException;
    
    AbstractProcessor getAbstractProcessorByName(Pair<String, String> jar_type) 
            throws InstantiationException, IllegalAccessException, MalformedURLException;
    
    Object getObjectByName(Pair<String, String> jar_type) throws InstantiationException, IllegalAccessException, MalformedURLException;
}
