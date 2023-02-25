package org.nakedobjects.runtime.system;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.nakedobjects.metamodel.commons.lang.MethodUtils;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.specloader.internal.introspector.MethodFinderUtils;


@RunWith(Parameterized.class)
public class MethodFinderUtilsTest {

    private static Method staticMethod;
    private static Method instanceMethod;

    static {
        staticMethod = MethodUtils.findMethodElseNull(MethodFinderUtilsTest.class, "someStaticMethod");
        instanceMethod = MethodUtils.findMethodElseNull(MethodFinderUtilsTest.class, "someInstanceMethod");
    }

    private final MethodScope methodScope;
    private final Method method;
    private final boolean result;

    @Before
    public void setUp() {
        assertThat(staticMethod, is(not(nullValue())));
        assertThat(instanceMethod, is(not(nullValue())));
    }

    @Parameters
    public static Collection parameters() {
        return Arrays.asList(new Object[][] { { MethodScope.OBJECT, staticMethod, false },
                { MethodScope.CLASS, staticMethod, true }, { MethodScope.OBJECT, instanceMethod, true },
                { MethodScope.CLASS, instanceMethod, false }, });
    }

    public static void someStaticMethod() {}

    public void someInstanceMethod() {}

    public MethodFinderUtilsTest(final MethodScope methodScope, final Method method, final boolean result) {
        this.methodScope = methodScope;
        this.method = method;
        this.result = result;
    }

    @Test
    public void all() {
        assertThat(MethodFinderUtils.inScope(methodScope, method), is(result));
    }

}

// Copyright (c) Naked Objects Group Ltd.
