package org.nakedobjects.runtime.system.specpeer;

import java.util.Vector;

import junit.framework.Assert;


@SuppressWarnings("unchecked")
public class ExpectedCalls {
	private final Vector expectedObjects = new Vector();
    private final Vector actualObjects = new Vector();

    private void assertExpectedNoMoreThanActuals() {
        Assert.assertTrue("More actuals than expected; didn't expect call " + actualObjects.lastElement(),
                actualObjects.size() <= expectedObjects.size());
    }

    public void verify() {
        assertLastMethodsParametersCorrect();
        Assert.assertTrue("Too few calls added\n  Expected " + expectedObjects, actualObjects.size() == expectedObjects.size());
    }

    private void assertLastMethodsParametersCorrect() {
        final int lastActual = actualObjects.size() - 1;
        if (lastActual >= 0) {
            final ExpectedCall lastExpectedCall = (ExpectedCall) expectedObjects.elementAt(lastActual);
            final ExpectedCall lastActualCall = (ExpectedCall) actualObjects.elementAt(lastActual);

            final int actualParameterSize = lastActualCall.paramters.size();
            final int expectedParameterSize = lastExpectedCall.paramters.size();
            Assert.assertEquals("Method " + lastExpectedCall.name + " parameters incorrect; ", expectedParameterSize,
                    actualParameterSize);
        }
    }

    public void addExpectedMethod(final String name) {
        expectedObjects.addElement(new ExpectedCall(name));
    }

    public void addExpectedParameter(final Object value) {
        final ExpectedCall expected = (ExpectedCall) expectedObjects.lastElement();
        expected.addParameter(value);
    }

    public void addActualMethod(final String name) {
        assertLastMethodsParametersCorrect();

        final ExpectedCall actual = new ExpectedCall(name);

        actualObjects.addElement(actual);
        assertExpectedNoMoreThanActuals();

        final int element = actualObjects.size() - 1;

        final ExpectedCall expected = (ExpectedCall) expectedObjects.elementAt(element);
        Assert.assertEquals("Actual method does not match expected.\n", expected.name, name);

    }

    public void addActualParameter(final Object value) {
        final ExpectedCall actual = (ExpectedCall) actualObjects.lastElement();
        actual.addParameter(value);

        final int expectedElement = actualObjects.size() - 1;
        final ExpectedCall expectedCall = (ExpectedCall) expectedObjects.elementAt(expectedElement);

        final int parameterElement = actual.paramters.size() - 1;

        final int expectedParameterSize = expectedCall.paramters.size();
        if (parameterElement >= expectedParameterSize) {
            Assert.fail("Unexpected number of parameters; expected " + expectedParameterSize + ", but got "
                    + actual.paramters.size());
        }

        final Object expected = expectedCall.parameterAt(parameterElement);
        Assert.assertEquals(
                "Actual parameter (" + parameterElement + ") in " + expectedCall.name + " does not match expected.\n", expected,
                value);
    }

}

@SuppressWarnings("unchecked")
class ExpectedCall {
    String name;
	Vector paramters = new Vector();

    public ExpectedCall(final String name) {
        this.name = name;
    }

    public Object parameterAt(final int element) {
        return paramters.elementAt(element);
    }

    public void addParameter(final Object value) {
        paramters.addElement(value);
    }

    @Override
    public String toString() {
        return name + "(" + paramters + ")";
    }
}
// Copyright (c) Naked Objects Group Ltd.
