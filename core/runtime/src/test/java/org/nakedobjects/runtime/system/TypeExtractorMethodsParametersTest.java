package org.nakedobjects.runtime.system;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.nakedobjects.metamodel.commons.matchers.NofMatchers;
import org.nakedobjects.metamodel.specloader.internal.TypeExtractorMethodParameters;

public class TypeExtractorMethodsParametersTest {

    @Test
    public void shouldFindGenericTypes() throws Exception {
        
        class Customer {}
        class CustomerRepository {
            public void filterCustomers(List<Customer> customerList){ ; }
        }
        
        Class<?> clazz = CustomerRepository.class;
        Method method = clazz.getMethod("filterCustomers", List.class);
        
        TypeExtractorMethodParameters extractor = new TypeExtractorMethodParameters(method);
        
        assertThat(extractor.getClasses().size(), is(2));
        assertThat(extractor.getClasses(), NofMatchers.containsElementThat(equalTo(java.util.List.class)));
        assertThat(extractor.getClasses(), NofMatchers.containsElementThat(equalTo(Customer.class)));
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
