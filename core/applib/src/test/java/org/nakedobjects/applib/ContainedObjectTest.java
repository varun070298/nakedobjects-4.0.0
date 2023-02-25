package org.nakedobjects.applib;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.applib.security.UserMemento;

import static org.junit.Assert.assertEquals;


public class ContainedObjectTest {

    private DomainObjectContainer container;
    private AbstractContainedObject object;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        container = context.mock(DomainObjectContainer.class);
        object = new AbstractContainedObject() {};
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
    public void testGetUser() throws Exception {
        final UserMemento memento = new UserMemento("Harry");
        context.checking(new Expectations() {{
            one (container).getUser();
            will(returnValue(memento));
            
        }});
        
        UserMemento user = object.getUser();
        assertEquals(memento, user);
        
        context.assertIsSatisfied();
    }

}


// Copyright (c) Naked Objects Group Ltd.
