package org.nakedobjects.runtime.persistence;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.ensure.Assert;


public class PersistorUtil {

    private static final Logger LOG = Logger.getLogger(PersistorUtil.class);

    private PersistorUtil() {}


    // //////////////////////////////////////////////////////////////////
    // update resolve state
    // //////////////////////////////////////////////////////////////////

    public static void start(final NakedObject object, final ResolveState state) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("start " + object + " as " + state.name());
    	}
        object.changeState(state);
    }

    /**
     * Marks the specified object as loaded: resolved, partly resolve or updated as specified by the second
     * parameter. Attempting to specify any other state throws a run time exception.
     */
    public static void end(final NakedObject object) {
        final ResolveState endState = object.getResolveState().getEndState();
        Assert.assertNotNull("end state required", endState);
        if (LOG.isDebugEnabled()) {
        	LOG.debug("end " + object + " as " + endState.name());
        }
        object.changeState(endState);
    }

}

// Copyright (c) Naked Objects Group Ltd.
