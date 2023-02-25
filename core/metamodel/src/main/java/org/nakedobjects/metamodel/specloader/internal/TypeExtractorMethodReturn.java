package org.nakedobjects.metamodel.specloader.internal;

import java.lang.reflect.Method;

/**
 * Helper that finds all return types (including generic types) for the
 * provided {@link Method}.
 * 
 * <p>
 * For example,
 * <pre>
 * public class CustomerRepository {
 *     public List&lt;Customer&gt; findCustomers( ... ) { ... }
 * }
 * </pre>
 * <p>
 * will find both <tt>List</tt> and <tt>Customer</tt>. 
 */
public class TypeExtractorMethodReturn extends TypeExtractorAbstract implements Iterable<Class<?>>{
    
    public TypeExtractorMethodReturn(final Method method) {
        super(method);
        
        add(method.getReturnType());
        addParameterizedTypes(method.getGenericReturnType());
    }
}

// Copyright (c) Naked Objects Group Ltd.
