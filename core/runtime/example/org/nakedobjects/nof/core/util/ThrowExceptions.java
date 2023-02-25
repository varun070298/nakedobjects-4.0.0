package org.nakedobjects.nof.core.util;

import org.nakedobjects.noa.NakedObjectRuntimeException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


public class ThrowExceptions {
    private static final Logger LOG = Logger.getLogger(ThrowExceptions.class);

    public static void main(final String[] args) {
        method1();
    }

    private static void method1() {
        method2();
    }

    private static void method2() {
        method3();
    }

    private static void method3() {
        BasicConfigurator.configure();

        NakedObjectRuntimeException exception = new NakedObjectRuntimeException("exception message");

        LOG.info("Testing logging", exception);
        LOG.info("");
        LOG.info("");
        System.out.println();

        try {
            method4();
        } catch (Exception e) {
            NakedObjectRuntimeException exception2 = new NakedObjectRuntimeException("cascading exception message", e);

            LOG.info("Testing logging 2", exception2);
            LOG.info("");
            LOG.info("");
            System.out.println();

            throw exception2;
        }
    }

    private static void method4() {
        method5();
    }

    private static void method5() {
        throw new NullPointerException("system exception message");
    }
}
// Copyright (c) Naked Objects Group Ltd.
