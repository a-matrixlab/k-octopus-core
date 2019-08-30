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

import org.lisapark.koctopus.core.ProcessingModel;
import org.lisapark.koctopus.core.processor.AbstractProcessor;
import org.lisapark.koctopus.core.sink.external.ExternalSink;
import org.lisapark.koctopus.core.source.external.AbstractExternalSource;

import java.util.List;
import java.util.Set;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
public interface OctopusRepository {
   
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
    
    // Select processors
    //==========================================================================
    List<ExternalSink>          getAllExternalSinkTemplates() throws RepositoryException;    
//    Set<ExternalSink>           getExternalSinkTemplates(String name) throws RepositoryException;    
//    Set<ExternalSink>           getExternalSinkTemplateByClassName(String name) throws RepositoryException;
    

    List<AbstractExternalSource>        getAllExternalSourceTemplates() throws RepositoryException;    
//    Set<ExternalSource>         getExternalSourceTemplates(String name) throws RepositoryException;    
//    Set<ExternalSource>         getExternalSourceTemplateByClassName(String name) throws RepositoryException;
    
    List<AbstractProcessor>     getAllProcessorTemplates() throws RepositoryException;    
//    Set<AbstractProcessor>      getProcessorTemplates(String name) throws RepositoryException;    
//    Set<AbstractProcessor>      getProcessorTemplateByClassName(String name) throws RepositoryException;
     
    // Save model
    //==========================================================================
//    void saveProcessingModel(ProcessingModel model) throws RepositoryException;

//    void saveProcessingModelOnServer(ProcessingModel model, String turl,
//            int tport, String tuid, String tpsw) throws RepositoryException;
}
