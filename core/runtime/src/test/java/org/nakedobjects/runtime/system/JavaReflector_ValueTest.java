package org.nakedobjects.runtime.system;

import org.junit.Assert;
import org.junit.Test;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.JavaReflector;


public class JavaReflector_ValueTest extends JavaReflectorTestAbstract {

    @Override
    protected NakedObjectSpecification loadSpecification(final JavaReflector reflector) {
        return reflector.loadSpecification(String.class);
    }

    @Test
    public void testType() throws Exception {
        Assert.assertTrue(specification.isObject());
    }

    @Test
    public void testName() throws Exception {
        Assert.assertEquals(String.class.getName(), specification.getFullName());
    }

}

// Copyright (c) Naked Objects Group Ltd.
