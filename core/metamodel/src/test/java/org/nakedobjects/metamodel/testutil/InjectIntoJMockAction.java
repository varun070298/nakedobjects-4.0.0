package org.nakedobjects.metamodel.testutil;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public final class InjectIntoJMockAction implements Action {
    public void describeTo(Description description) {
        description.appendText("inject self");
    }

    // x.injectInto(y) ---> y.setXxx(x)
    public Object invoke(Invocation invocation) throws Throwable {
        Object injectable = invocation.getInvokedObject();
        Object toInjectInto = invocation.getParameter(0);
        Method[] methods = toInjectInto.getClass().getMethods();
        for(Method method: methods) {
            if (!method.getName().startsWith("set")) {
                continue;
            } 
            if (method.getParameterTypes().length != 1) {
                continue;
            }
            Class<?> methodParameterType = method.getParameterTypes()[0];
            if (methodParameterType.isAssignableFrom(injectable.getClass())) {
                method.invoke(toInjectInto, injectable);
                break;
            }
        }
        return null;
    }
    
    /**
     * Factory
     */
    public static Action injectInto() {
    	return new InjectIntoJMockAction();
    }

}

// Copyright (c) Naked Objects Group Ltd.
