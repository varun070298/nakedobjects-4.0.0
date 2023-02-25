package org.nakedobjects.runtime.persistence.objectfactory;

import java.lang.reflect.Method;

public class MethodUtils {

    public static boolean isGetter(Method method) {
        String name = method.getName();
        if (name.startsWith("get") && name.length() > 3) {
        	return true;
        }
        if (name.startsWith("is") && name.length() > 2) {
        	return true;
        }
        return false;
    }

    public static boolean isSetter(Method method) {
        String name = method.getName();
        return name.startsWith("set") && name.length() > 3;
    }


}
