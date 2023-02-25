package org.nakedobjects.runtime.system;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.containsElementThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.nakedobjects.metamodel.specloader.internal.TypeExtractorMethodReturn;

public class TypeExtractorMethodReturnTest {

    @Test
    public void shouldFindGenericTypes() throws Exception {
        
        class Customer {}
        class CustomerRepository {
            public List<Customer> findCustomers(){ return null; }
        }
        
        Class<?> clazz = CustomerRepository.class;
        Method method = clazz.getMethod("findCustomers");
        
        TypeExtractorMethodReturn extractor = new TypeExtractorMethodReturn(method);
        
        assertThat(extractor.getClasses().size(), is(2));
        assertThat(extractor.getClasses(), containsElementThat(equalTo(java.util.List.class)));
        assertThat(extractor.getClasses(), containsElementThat(equalTo(Customer.class)));
    }

    @Test
    public void ignoresVoidType() throws Exception {
        
        class CustomerRepository {
            public void findCustomers(){ }
        }
        
        Class<?> clazz = CustomerRepository.class;
        Method method = clazz.getMethod("findCustomers");
        
        TypeExtractorMethodReturn extractor = new TypeExtractorMethodReturn(method);
        
        assertThat(extractor.getClasses().size(), is(0));
    }



}
// Copyright (c) Naked Objects Group Ltd.
