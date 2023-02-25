package org.nakedobjects.runtime.system.specpeer;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;


public class ExpectedCallsTest extends TestCase {

    private ExpectedCalls calls;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ExpectedCallsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        calls = new ExpectedCalls();

        /*
         * calls.addExpectedMethod("testMethod1");
         * 
         * calls.addExpectedMethod("testMethod2"); calls.addExpectedParameter("param1");
         * 
         * calls.addExpectedMethod("testMethod3"); calls.addExpectedParameter("param1");
         * calls.addExpectedParameter("param2");
         */
        calls.addExpectedMethod("testMethod4");
        calls.addExpectedParameter("param1");
        calls.addExpectedParameter("param2");
        calls.addExpectedParameter("param3");

        calls.addExpectedMethod("testMethod1");
        calls.addExpectedParameter("param1");
    }

    public void testNoMethodsCalled() {
        try {
            calls.verify();
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testAllMethodsCalled() {
        /*
         * calls.addActualMethod("testMethod1");
         * 
         * calls.addActualMethod("testMethod2"); calls.addActualParameter("param1");
         * 
         * calls.addActualMethod("testMethod3"); calls.addActualParameter("param1");
         * calls.addActualParameter("param2");
         */
        calls.addActualMethod("testMethod4");
        calls.addActualParameter("param1");
        calls.addActualParameter("param2");
        calls.addActualParameter("param3");

        calls.addActualMethod("testMethod1");
        calls.addActualParameter("param1");

        calls.verify();
    }

    public void testMethodNameWrong() {
        try {
            calls.addActualMethod("testMethod2");
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testMethodParameterWrong() {
        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param3");
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testMethodWithTooManyParameters() {
        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param2");
            calls.addActualParameter("param3");
            calls.addActualParameter("param4");
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testTooManyMethods() {
        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param2");
            calls.addActualParameter("param3");

            calls.addActualMethod("testMethod1");
            calls.addActualParameter("param1");

            calls.addActualMethod("testMethod1");
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testTooFewMethods() {
        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param2");
            calls.addActualParameter("param3");

            calls.verify();
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testMethodWithTooFewParametersWhenNewMethodStarted() {

        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param2");

            calls.addActualMethod("testMethod1");

        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

    public void testMethodWithTooFewParametersWhenVerifying() {

        try {
            calls.addActualMethod("testMethod4");
            calls.addActualParameter("param1");
            calls.addActualParameter("param2");
            calls.addActualParameter("param3");

            calls.addActualMethod("testMethod1");

            calls.verify();
        } catch (final AssertionFailedError e) {
            return;
        }
        fail();
    }

}
// Copyright (c) Naked Objects Group Ltd.
