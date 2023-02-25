package org.nakedobjects.plugins.dnd.viewer.basic;

import java.util.Enumeration;


public interface CollectionDisplayIterator {

    /**
     * Return cache to be viewed on current page
     */
    public Enumeration displayElements();

    /**
     * Position cursor at first element
     */
    public void first();

    public int getDisplaySize();

    /**
     * If true there is a next page to display, and 'next' and 'last' options are valid
     */
    public boolean hasNext();

    public boolean hasPrevious();

    /**
     * Position cursor at last
     */
    public void last();

    /**
     * Position cursor at beginning of next page
     */
    public void next();

    public int position();

    /**
     * Position cursor at beginning of previous page
     */
    public void previous();
}
// Copyright (c) Naked Objects Group Ltd.
