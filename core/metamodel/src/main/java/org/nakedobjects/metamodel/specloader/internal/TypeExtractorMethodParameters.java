package org.nakedobjects.metamodel.specloader.internal;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Helper that finds all parameter types (including generic types) for the
 * provided {@link Method}.
 * 
 * <p>
 * For example,
 * <pre>
 * public class CustomerRepository {
 *     public void filterCustomers(List<Customer> customerList) { ... }
 * }
 * </pre>
 * <p>
 * will find both <tt>List</tt> and <tt>Customer</tt>. 
 */
public class TypeExtractorMethodParameters extends TypeExtractorAbstract {
    
    private Class<?>[] parameterTypes;

    public TypeExtractorMethodParameters(final Method method) {
        super(method);
        
        parameterTypes = getMethod().getParameterTypes();
        for(Class<?> parameterType: parameterTypes) {
            add(parameterType);
        }
        
        Type[] genericTypes = getMethod().getGenericParameterTypes();
        for(Type genericType: genericTypes) {
            addParameterizedTypes(genericTypes);
        }
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    
}

// Copyright (c) Naked Objects Group Ltd.
