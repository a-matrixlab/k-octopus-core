/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph;

import java.awt.Point;
import java.util.UUID;
import javax.swing.Icon;
import org.lisapark.koctopus.core.graph.api.NiPassport;

/**
 *
 * @author alexmy
 */
public class NodePassport implements NiPassport{

    UUID id;
    String name;
    String authorEmail;
    String description;
    Point location;
    Icon icon;
    
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthorEmail() {
        return authorEmail;
    }

    @Override
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public void setIcon(Icon icon) {
        this.icon = icon;
    }    
}
