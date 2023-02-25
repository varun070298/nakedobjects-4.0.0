package org.nakedobjects.applib;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class FactoryAndRepositoryTest {
    private DomainObjectContainer container;
    private AbstractFactoryAndRepository object;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        container = context.mock(DomainObjectContainer.class);
        object = new AbstractFactoryAndRepository() {};
        object.setContainer(container);  
    }
    
    @Test
    public void testContainer() throws Exception {
        assertEquals(container, object.getContainer());
    }
    
    @Test
    public void testInformUser() throws Exception {
        context.checking(new Expectations() {{
            one (container).informUser("message");  
        }});
        
        object.informUser("message");
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testWarnUser() throws Exception {
        context.checking(new Expectations() {{
            one (container).warnUser("message");  
        }});
        
        object.warnUser("message");
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testRaiseError() throws Exception {
        context.checking(new Expectations() {{
            one (container).raiseError("message");  
        }});
        
        object.raiseError("message");
        
        context.assertIsSatisfied();
    }
    
    @Test
    public void testAllInstances() throws Exception {
        final List<Object> list = new ArrayList<Object>();
        list.add(new TestDomainObject());
        list.add(new TestDomainObject());
        list.add(new TestDomainObject());
        

        context.checking(new Expectations() {{
            one (container).allInstances(TestDomainObject.class);
            will(returnValue(list));
        }});
        
        List<TestDomainObject> allInstances = object.allInstances(TestDomainObject.class);
        assertThat(allInstances.size(), is(3));
        assertThat(allInstances.get(0), notNullValue());
        assertThat(allInstances.get(0), equalTo(list.get(0)));
        assertThat(allInstances.get(1), equalTo(list.get(1)));
        assertThat(allInstances.get(2), equalTo(list.get(2)));

        context.assertIsSatisfied();
    }
    
}


// Copyright (c) Naked Objects Group Ltd.
