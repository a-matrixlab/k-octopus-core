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

import org.lisapark.koctopus.core.graph.api.INode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lisapark.koctopus.core.graph.api.GraphVocabulary;

/**
 *
 * @author alexmy
 */
public  class Gnode implements INode<NodePassport, NodeParams, NodeInputs, NodeOutput> {

    /**
     * @return the x
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    @Override
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    @Override
    public void setY(int y) {
        this.y = y;
    }
    
    private String id;
    private String type;
    private String label;
    private String transportUrl;
    private String color;
    private int x;
    private int y;
    
    private NodePassport passport;
    private NodeParams params;
    private NodeInputs input;
    private NodeOutput output;
   
    public Gnode(){
        this.color = GraphVocabulary.UNTOUCHED;
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

    /**
     * @return the transportUrl
     */
    @Override
    public String getTransportUrl() {
        return transportUrl;
    }

    /**
     * @param transportUrl the transportUrl to set
     */
    @Override
    public void setTransportUrl(String transportUrl) {
        this.transportUrl = transportUrl;
    }

    /**
     * @return the passport
     */
    @Override
    public NodePassport getPassport() {
        return passport;
    }

    /**
     * @param passport the passport to set
     */
    @Override
    public void setPassport(NodePassport passport) {
        this.passport = passport;
    }

    /**
     * @return the params
     */
    @Override
    public NodeParams getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    @Override
    public void setParams(NodeParams params) {
        this.params = params;
    }

    /**
     * @return the input
     */
    @Override
    public NodeInputs getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    @Override
    public void setInput(NodeInputs input) {
        this.input = input;
    }

    /**
     * @return the output
     */
    @Override
    public NodeOutput getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    @Override
    public void setOutput(NodeOutput output) {
        this.output = output;
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
     * @return the color
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    @Override
    public void setColor(String color) {
        this.color = color;
    }
}
