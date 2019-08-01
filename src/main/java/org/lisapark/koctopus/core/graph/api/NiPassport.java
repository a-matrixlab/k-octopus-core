/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lisapark.koctopus.core.graph.api;

import java.awt.Point;
import java.util.UUID;
import javax.swing.Icon;

/**
 *
 * @author alexmy
 */
public interface NiPassport {

    /**
     * @return the id
     */
    public UUID getId();

    /**
     * @return the name
     */
    public String getName();

    /**
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * @return the authorEmail
     */
    public String getAuthorEmail();

    /**
     * @param authorEmail the authorEmail to set
     */
    public void setAuthorEmail(String authorEmail) ;

    /**
     * @return the description
     */
    public String getDescription() ;

    /**
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * @return the location
     */
    public Point getLocation() ;

    /**
     * @param location the location to set
     */
    public void setLocation(Point location);

    /**
     * @return the icon
     */
    public Icon getIcon();

    /**
     * @param icon the icon to set
     */
    public void setIcon(Icon icon);;
}
