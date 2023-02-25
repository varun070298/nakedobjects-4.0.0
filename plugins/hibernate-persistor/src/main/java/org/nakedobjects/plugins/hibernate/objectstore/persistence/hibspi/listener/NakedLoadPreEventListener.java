package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PreLoadEvent;
import org.hibernate.event.PreLoadEventListener;


/**
 * Respond to events within Hibernate which need to be reflected within the Naked Objects System.
 */
public class NakedLoadPreEventListener extends NakedEventListenerAbstract
        implements PreLoadEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedLoadPreEventListener.class);

    
    /////////////////////////////////////////////////////////
    // loading
    /////////////////////////////////////////////////////////

    public void onPreLoad(final PreLoadEvent event) {
        // do nothing
    }



}
// Copyright (c) Naked Objects Group Ltd.
