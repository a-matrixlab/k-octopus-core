/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.lucene;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lisapark.koctopus.core.ProcessingModel;
import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.lisapark.koctopus.core.graph.Graph;

/**
 *
 * @author alexmy
 */
public class ModelLuceneIndex {

    static final Logger LOG = Logger.getLogger(ModelLuceneIndex.class.getName());

    public static void indexModelWithDublinCore(ProcessingModel model, Graph graph, String luceneIndex, boolean create) {

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        if (create) {
            // Create a new index in the directory, removing any
            // previously indexed documents:
            indexWriterConfig.setOpenMode(OpenMode.CREATE);
        } else {
            // Add new documents to an existing index:
            indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        }

        try (IndexWriter writer = new IndexWriter(FSDirectory.open(Paths.get(luceneIndex)), indexWriterConfig)) {
            String jsonDoc = graph.toJson().toString();
            
            Metadata met = new Metadata();
            met.add(Metadata.CREATOR, "Lisa Park");
            met.add(Metadata.CREATOR, model.getAuthorEmail());
            met.set(Metadata.DATE, new Date());
            met.set(Metadata.FORMAT, "text/xml");
            met.set(DublinCore.SOURCE, model.getModelJsonFile());
            met.add(Metadata.SUBJECT, model.getName());
            met.add(Metadata.SUBJECT, model.getDescription());
            met.add(Metadata.SUBJECT, model.getServiceUrl());
            met.set(Property.externalClosedChoise(TikaCoreProperties.RIGHTS.getName(), "public", "private"), "public");

            org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();

            for (String key : met.names()) {
                String[] values = met.getValues(key);
                for (String val : values) {
                    if (val != null) {
                        document.add(new TextField(key, val, Field.Store.YES));
                    }
                }
            }
            document.add(new TextField("contents", jsonDoc, Field.Store.YES));
            
            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                writer.addDocument(document);
            } else {
                writer.updateDocument(new Term("path", model.getModelJsonFile()), document);
            }
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }
}
