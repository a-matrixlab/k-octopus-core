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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author alexmy
 */
public final class Gnode implements INode<String, String, String, String, String> {

    public String id;
    public String type;
    public String label;
    private String transportUrl;
    private String layout;
    private String params;
    private String input;
    private String output;
    
    /**
     * @return the params
     */
    @Override
    public String getParams() {
        return params;
    }

    /**
     * @return the input
     */
    @Override
    public String getInput() {
        return input;
    }

    /**
     * @return the output
     */
    @Override
    public String getOutput() {
        return output;
    }

    /**
     * @return the layout
     */
    @Override
    public String getLayout() {
        return layout;
    }

    /**
     * @param layout the layout to set
     */
    @Override
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     * @param params the params to set
     */
    @Override
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * @param input the input to set
     */
    @Override
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @param output the output to set
     */
    @Override
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the label
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    @Override
    public void setLabel(String label) {
        this.label = label;
    } 

    @Override
    public JsonObject toJson() {
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(new Gson().toJson(this, this.getClass()));
        return elem.getAsJsonObject();
    }

    @Override
    public INode fromJson(String json) {
        return new Gson().fromJson(json, this.getClass());
    }

    /**
     * @return the transportUrl
     */
    public String getTransportUrl() {
        return transportUrl;
    }

    /**
     * @param transportUrl the transportUrl to set
     */
    public void setTransportUrl(String transportUrl) {
        this.transportUrl = transportUrl;
    }
}
