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
package org.lisapark.koctopus.core.graph.api;

import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;

/**
 *
 * @author alexmy
 * @param <V1>
 * @param <V2>
 * @param <V3>
 * @param <V4>
 */
public interface INode<V1, V2, V3, V4> {
    
    public String getId();
    public void setId(String id);
    
    public String getType();
    public void setType(String type);

    public String getLabel();
    public void setLabel(String label);
    
    public String getTransportUrl();
    public void setTransportUrl(String transportUrl);

    public String getColor();
    public void setColor(String color);
    
    public int getX();
    public void setX(int x);
    
    public int getY();
    public void setY(int y);
    
    public V1 getPassport();    
    public V2 getParams();    
    public V3 getInput();    
    public V4 getOutput();

    public void setPassport(V1 layout);
    public void setParams(V2 params);    
    public void setInput(V3 input);    
    public void setOutput(V4 output);
    
    public JsonObject toJson();    
    public INode fromJson(String json);
}
