package org.nakedobjects.metamodel.testutil;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public final class ReturnArgumentJMockAction implements Action {
    private final int i;

    public ReturnArgumentJMockAction(int i) {
        this.i = i;
    }

    public void describeTo(Description description) {
        description.appendText("parameter #" + i + " ");
    }

    public Object invoke(Invocation invocation) throws Throwable {
        return invocation.getParameter(i);
    }

    /**
     * Factory
     */
    public static Action returnArgument(final int i) {
    	return new ReturnArgumentJMockAction(i);
    }

}

// Copyright (c) Naked Objects Group Ltd.
