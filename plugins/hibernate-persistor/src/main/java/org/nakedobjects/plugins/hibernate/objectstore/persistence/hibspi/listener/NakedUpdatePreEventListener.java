package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.listener;

import org.apache.log4j.Logger;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;


/**
 * Respond to events within Hibernate which need to be reflected within the Naked Objects System.
 */
public class NakedUpdatePreEventListener extends NakedEventListenerAbstract
        implements PreUpdateEventListener {

    private static final long serialVersionUID = 1L;
    private final static Logger LOG = Logger.getLogger(NakedUpdatePreEventListener.class);



    public boolean onPreUpdate(final PreUpdateEvent event) {
        // do nothing
        return false;
    }




}
// Copyright (c) Naked Objects Group Ltd.
