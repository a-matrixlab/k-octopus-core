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
package org.lisapark.koctopus.core.graph;

/**
 *
 * @author alexmy
 */
public interface Vocabulary {
    
    // From DB Subsetting
    //==========================================================================
    public static final String COLOR = "color";
    public static final String STATUS = "status";
    public static final Integer COMPLETE = 1;
    public static final Integer BACK_LOG = 2;
    public static final Integer CANCEL = 3;
    
    public static final String START_NODE = "start-node";

    public static final String SOURCE_DB_URL = "source-db-url";
    public static final String TARGET_DB_URL = "target-db-url";

    // Graph objects
    public static final String PROCESSING_GRAPH = "PROCESSING_GRAPH";
    public static final String SOURCE = "SOURCE";
    public static final String PROCESSOR = "PROCESSOR";
    public static final String SINK = "SINK";
    public static final String PIPE = "PIPE";
    
    public static final String TRANSPORT_URL = "TRANSPORT_URL";
    
    public static final String DB = "db";
    public static final String TABLE = "table";
    public static final String VIEW = "view";
    public static final String PRIMARY_KEY = "pk";
    public static final String FOREIGN_KEY = "fk";
    public static final String ROW = "row";
    public static final String COLUMN = "column";

    // Constraint may be presented as a part of metadata
    public static final String CONSTRAINT = "constraint";

    // Graph relations
    public static final String DB_DB = "db-db";
    public static final String DB_TABLE = "db-table";
    public static final String TABLE_TABLE = "table-table";
    public static final String TABLE_VIEW = "table-view";
    public static final String TABLE_COLUMN = "table-column";
    public static final String COLUMN_COLUMN = "column-column";
    
    public static final String MODEL = "MODEL";

    // Samples
    public static final String SAMPLE = "sample";
    public static final String SAMPLE_SIZE = "sample-size";
    public static final String SAMPLE_TYPE = "sample-type";
    public static final String SAMPLE_RANDOM = "sample-random";
    public static final String SAMPLE_SYSTEMATIC = "sample-systematic";
    public static final String SAMPLE_CLUSTER = "sample-cluster";
    public static final String SAMPLE_STRATIFIED = "sample-stratified";

    // Graph Node status colors
    //==========================================================================
    public static final String UNTOUCHED = "untouched";  // Untouched node
    public static final String WHITE = "white";          // Node touched and data references is transfered for FORWARD direction
    public static final String BLUE = "blue";            // Node touched and data references is transfered forBACKWARD direction
    public static final String GREY = "grey";            // Node is in data processing and is trying to reach other connected nodes    
                                                         // to mark them "white"
    public static final String RED = "red";              // Node processing completed but some UNTOUCHED marked nodes connected to it 
                                                         // (in oposit to current direction) are still untouched
    public static final String BLACK = "black";          // Node processing completed and no untouched connected nodes left
    
    // New set of terms
    // 
    // JSON prefix
    //==========================================================================
    public static final String JSON = "JSON";
    public static final String JSON_GRAPH = "JSON:GRAPH";
    public static final String JSON_GRAPH_DB = "JSON:GRAPH:DB";
    public static final String ID = "id";
    
    // Initial list of attributes
    //==========================================================================
    public static final String LABEL        = "LABEL";
    public static final String NAME         = "NAME";       // Column name
    public static final String TYPE         = "TYPE";       // Column d
    public static final String ORIGINAL     = "ORIGINAL";
    public static final String DATA_TYPE    = "DATA_TYPE";
    public static final String SIZE         = "SIZE";
    public static final String PRECISION    = "PRECISION";
    public static final String REFERENCE    = "REFERENCE";
    public static final String DEFAULT      = "DEFAULT";
    public static final String REQUIRED     = "REQUIRED";
    public static final String SYNONYMS     = "SYNONYMS";
    public static final String PROFILE      = "PROFILE";
    public static final String CONTEXT      = "CONTEXT";  
   
    // DB attributes
    public static final String ORGANIZATION = "ORGANIZATION";               // Reference to the Organization
    public static final String APPLICATION = "APPLICATION";                 // Reference to the Application
    public static final String DATA_DEPENDANCY = "DATA_DEPENDANCY";         // Dependancies of the Table columns
    public static final String URL             = "URL";                     // Db URL
    public static final String USER            = "USER";
    public static final String JDBC_DRIVER     = "JDBC_DRIVER"; 
  
    // Table attributes
    public static final String DATA_BASE = "DATA_BASE";                     // Reference to the data base
    public static final String SUB_TABLE = "SUB_TABLE";                     // Projection of the table that represent cluster of related columns
    public static final String ORIGIN_SUB_TABLE = "ORIGIN_SUB_TABLE";       // Projection of the table that represent cluster of the columns originated in the current table   
    public static final String COMPLETENESS = "COMPLETENESS";               // How table covers possible use cases
    public static final String DUPLICATION = "DUPLICATION";                 // Level of row duplication in the table
    public static final String CONSISTENCY = "CONSISTENCY";                 // Level of table data consistency
    public static final String CONFORMITY = "CONFORMITY";                   // How table value conform policies and constraints
    public static final String INTEGRITY = "INTEGRITY";                     // Referencial integrity of the table
    public static final String TRUST = "TRUST";                             // Accuracy of the table data
    public static final String INTERNAL_DEPENDANCY = "INTERNAL_DEPENDANCY"; // Dependencies between columns of the table
    public static final String EXTERNAL_DEPENDANCY = "EXTERNAL_DEPENDANCY"; // Dependencies between columns of the columns in other tables
    public static final String COLUMNS              = "COLUMNS";
    
    // Column attributes
    public static final String PK           = "PK";
    public static final String FK           = "FK";
    public static final String PK_MAP       = "PK_MAP";
    public static final String FK_MAP       = "FK_MAP";
    public static final String NATURAL_KEY  = "NATURAL_KEY";                // Indicating that column is Natural Key
    public static final String COMPOSIT_KEY = "COMPOSIT_KEY";               // Indicating that column is Composite Key
    public static final String SURROGATE_KEY = "SURROGATE_KEY";             // Indicating that column is Surrodate Key
    public static final String ID_COLUMN = "ID_COLUMN";                     // Indicating that column is ID
    public static final String NULL_COUNT = "NULL_COUNT";                   // Number of NULLs in the column
    public static final String BLANK_COUNT = "BLANK_COUNT";                 // Number of BLANKs in the column
    public static final String DEFAULT_COUNT = "DEFAULT_COUNT";             // Number of default values (NA, TBD, N/A etc.)
    public static final String MIN = "MIN";                                 // Min value in the column
    public static final String MAX = "MAX";                                 // Max value in the column
    public static final String AVERAGE = "AVERAGE";                         // Average value of the column
    public static final String STD_DEVIATION = "STD_DEVIATION";             // Standard Deviation
    public static final String MOST_FREQ = "MOST_FREQ";                     // Most frequent value
    public static final String DUPL_COUNT = "DUPL_COUNT";                   // Number of duplicate for each duplicated value (value:count)
    public static final String PARENT = "PARENT";                           // Reference to the parent column and table
    public static final String CHILD = "CHILD";                             // Reference to the child column and table
    public static final String LENGTH_DISTR = "LENGTH_DISTR";               // Distribution of the value length in the column
    public static final String VALUE_DISTR = "VALUE_DISTR";                 // Distribution of the values in the column
    public static final String NUM_CLUSTER = "NUM_CLUSTER";                 // Reference to the Numeric Cluster for the value (For example: Small, Medium, Large) 
    public static final String SUB_TYPE = "SUB_TYPE";                       // Reference to the Sub-type
    public static final String OUT_STD_DEVIATION = "OUT_STD_DEVIATION";     // Standard Deviation
    public static final String TS_ANAMOLY_VALUE = "TS_ANAMOLY_VALUE";       // TimeSeries Anamoly values by Value (out of three sigma, for examle)
    public static final String TS_ANAMOLY_FREQ = "TS_ANAMOLY_FREQ";         // TimeSeries Anamoly value by Frequency of the value
 }
