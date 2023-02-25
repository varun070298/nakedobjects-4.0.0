package org.nakedobjects.metamodel.adapter.version;

import java.util.Date;

import org.nakedobjects.metamodel.commons.encoding.Encodable;


/**
 * Version marks a NakedObject as being a particular variant of that object.
 * 
 * <p>
 * This is normally done using some form of incrementing number or timestamp, which would be held within the
 * implementing class. The numbers, timestamps, etc should change for each changed object, and the different()
 * method shoud indicate that the two Version objects are different.
 * 
 * <p>
 * The user's name and a timestamp should alos be kept so that when an message is passed to the user it can be
 * of the form "user has change object at time"
 */
public interface Version  {

    /**
     * Compares this version against the specified version and returns true if they are different versions.
     * 
     * <p>
     * This is use for optimistic checking, where the existence of a different version will normally cause a
     * concurrency exception.
     */
    boolean different(Version version);

    /**
     * Returns the user who made the last change.
     */
    String getUser();

    /**
     * Returns the time of the last change.
     */
    Date getTime();

    /**
     * Returns the sequence for printing/display
     */
    String sequence();
}
// Copyright (c) Naked Objects Group Ltd.
