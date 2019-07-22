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

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

/**
 *
 * @author alexmy
 * @param <K>
 * @param <V1>
 * @param <V2>
 * @param <V3>
 */
public interface INode<K, V1, V2, V3, V4> {
    /**
     * @return the id
     */
    public String getId();

    /**
     * @param id the id to set
     */
    public void setId(String id);

    /**
     * @return the type
     */
    public String getType();

    /**
     * @param type the type to set
     */
    public void setType(String type);

    /**
     * @return the label
     */
    public String getLabel();

    /**
     * @param label the label to set
     */
    public void setLabel(String label);

    /**
     * @return the metadata as a JSON string of 
     * HashMultimap<String, Pair<String, String>>
     */
    public V1 getLayout();    
    public V2 getParams();    
    public V3 getInput();    
    public V4 getOutput();

    public void setLayout(V1 json);
    public void setParams(V2 json);    
    public void setInput(V3 json);    
    public void setOutput(V4 json);
    
    public JsonObject toJson();
    
    public INode fromJson(String json);
}
