package org.nakedobjects.runtime.memento;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;


public class MementoTest2_Test {
    private Mockery mockery = new JUnit4Mockery(){{
    		setImposteriser(ClassImposteriser.INSTANCE);
    	}};

    private NakedObject rootAdapter;
    private NakedObject returnedAdapter;

    private TestObject rootObject;

    private TestObject recreatedObject;

    private Oid oid;

    private Data data;

    private Memento memento;


    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        // Configuration
        // final TestProxyConfiguration configuration = new TestProxyConfiguration();
        PropertiesConfiguration configuration = new PropertiesConfiguration();   
        
        // Root object specification
        final NakedObjectSpecification rootSpecification = mockery.mock(NakedObjectSpecification.class);
        final OneToOneAssociation nameField = mockery.mock(OneToOneAssociation.class);
        final NakedObjectSpecification nameSpecification = mockery.mock(NakedObjectSpecification.class, "name specification");
        final EncodableFacet encodeableFacet = mockery.mock(EncodableFacet.class);
         
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(rootSpecification).isCollection();
                will(returnValue(false));
                
                atLeast(1).of(rootSpecification).getAssociations();
                will(returnValue(new NakedObjectAssociation[] {nameField}));
                
                atLeast(1).of(rootSpecification).getFullName();
                will(returnValue(TestObject.class.getName()));
                
                atLeast(1).of(nameField).isDerived();
                will(returnValue(false));

                atLeast(1).of(nameField).isOneToManyAssociation();
                will(returnValue(false));

                atLeast(1).of(nameField).getSpecification();
                will(returnValue(nameSpecification));

                atLeast(1).of(nameField).getId();
                will(returnValue("name-field"));

                atLeast(1).of(nameSpecification).isEncodeable();
                will(returnValue(true));

                atLeast(1).of(nameSpecification).getFacet(EncodableFacet.class);
                will(returnValue(encodeableFacet));
            }
        });
        
        
        // Root object
        rootAdapter = mockery.mock(NakedObject.class);
        rootObject = new TestObject("Harry");
        final NakedObject nameAdapter = mockery.mock(NakedObject.class, "name");
        oid = mockery.mock(Oid.class);

        // object encoding
        mockery.checking(new Expectations() {
           {
               atLeast(1).of(rootAdapter).getSpecification();
               will(returnValue(rootSpecification));

               atLeast(1).of(rootAdapter).getOid();
               will(returnValue(oid));

               atLeast(1).of(rootAdapter).getResolveState();
               will(returnValue(ResolveState.RESOLVED));

               atLeast(1).of(nameField).get(rootAdapter);
               will(returnValue(nameAdapter));
            
               
               one(encodeableFacet).toEncodedString(nameAdapter);
               will(returnValue("_HARRY_"));
               
               
              /* 
               atLeast(1).of(oid).isTransient();
               will(returnValue(false));

               atLeast(1).of(rootAdapter).getObject();
               will(returnValue(rootObject));
               
               one(mockPersistenceSession).recreateAdapter(oid, rootSpecification);
               will(returnValue(recreatedAdapter));.

               atLeast(1).of(recreatedAdapter).getOid();
               will(returnValue(oid));

               one(recreatedAdapter).getResolveState();
               will(returnValue(ResolveState.GHOST));

               one(recreatedAdapter).changeState(ResolveState.UPDATING);

               atLeast(1).of(recreatedAdapter).getSpecification();
               will(returnValue(rootSpecification));
               
               atLeast(1).of(recreatedAdapter).getObject();
               will(returnValue(recreatedObject));
               
   /*            
               one(mockAdapterManager).adapterFor("Harry", originalAdapter, specification.getAssociation("name"));
               will(returnValue(nameAdapter)); 
               
               atLeast(1).of(nameAdapter).getObject();
               will(returnValue("Harry"));
               */
           }
       });
      

        // object decoding
        mockery.checking(new Expectations() {
           {
               /*
               atLeast(1).of(oid).isTransient();
               will(returnValue(false));

               atLeast(1).of(rootAdapter).getObject();
               will(returnValue(rootObject));
/*               
               one(mockPersistenceSession).recreateAdapter(oid, rootSpecification);
               will(returnValue(recreatedAdapter));

               atLeast(1).of(recreatedAdapter).getOid();
               will(returnValue(oid));

               one(recreatedAdapter).getResolveState();
               will(returnValue(ResolveState.GHOST));

               one(recreatedAdapter).changeState(ResolveState.UPDATING);

               atLeast(1).of(recreatedAdapter).getSpecification();
               will(returnValue(rootSpecification));
               
               atLeast(1).of(recreatedAdapter).getObject();
               will(returnValue(recreatedObject));
               
   /*            
               one(mockAdapterManager).adapterFor("Harry", originalAdapter, specification.getAssociation("name"));
               will(returnValue(nameAdapter)); 
               
               atLeast(1).of(nameAdapter).getObject();
               will(returnValue("Harry"));
               */
           }
       });
      

        // Persistence Session
        final NakedObjectReflector reflector = mockery.mock(NakedObjectReflector.class);
        final PersistenceSession mockPersistenceSession = mockery.mock(PersistenceSession.class);
        final NakedObjectSessionFactory sessionFactory = mockery.mock(NakedObjectSessionFactory.class);
        final AuthenticationSession mockSession = mockery.mock(AuthenticationSession.class);
        final NakedObjectSession session = mockery.mock(NakedObjectSession.class);
    //   final AdapterManager mockAdapterManager = mockery.mock(AdapterManager.class);
/**
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(sessionFactory).getSpecificationLoader();
                will(returnValue(reflector));
    
                atLeast(1).of(sessionFactory).openSession(mockSession);
                will(returnValue(session));
                
                atLeast(1).of(reflector).loadSpecification(TestObject.class.getName());
                will(returnValue(rootSpecification));
                
                atLeast(1).of(session).open();

                atLeast(1).of(session).getPersistenceSession();
                will(returnValue(mockPersistenceSession));
                     
            }});
*/
        
        

        NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);

 //      NakedObjectsContextStatic.getInstance().openSessionInstance(mockSession);

 //       final NakedObject recreatedAdapter = mockery.mock(NakedObject.class, "recreated");
        
 //       recreatedObject = new TestObject();

        
     
       /*
       returnedAdapter = mockery.mock(NakedObject.class, "recreated adapter");
       final Oid returnedOid = mockery.mock(Oid.class, "recreated oid");

       mockery.checking(new Expectations() {
           {
  
               
               atLeast(1).of(mockPersistenceSession).recreateAdapter(oid, rootSpecification);
               will(returnValue(returnedAdapter));
   
               atLeast(1).of(returnedAdapter).getOid();
               will(returnValue(returnedOid));
               
           }});
*/
       
        
        
        
        memento = new Memento(rootAdapter);
        data = memento.getData();
     }



    @Test
    public void testOid() throws Exception {
        assertEquals(oid, data.getOid());
        mockery.assertIsSatisfied();
    }

    @Test
    public void testResolved() throws Exception {
        assertEquals("Resolved", data.getResolveState());
        mockery.assertIsSatisfied();
    }

    @Test
    public void testClassName() throws Exception {
        assertEquals(TestObject.class.getName(), data.getClassName());
        mockery.assertIsSatisfied();
    }

    @Test
    public void testStringField() throws Exception {
        assertEquals(ObjectData.class, data.getClass());
        assertEquals("_HARRY_", ((ObjectData) data).getEntry("name-field"));
        mockery.assertIsSatisfied();
    }    

    @Test
    public void testEncode() throws Exception {
        final DataOutputStreamExtended mockOutputImpl = mockery.mock(DataOutputStreamExtended.class);
        
        mockery.checking(new Expectations() {
            {
            	one(mockOutputImpl).writeEncodable(with(any(ObjectData.class)));
//                one(mockOutputImpl).writeUTF(TestObject.class.getName());
//                one(mockOutputImpl).writeUTF(ResolveState.RESOLVED.name());
//                one(mockOutputImpl).writeEncodable(oid);
//                one(mockOutputImpl).writeInt(1);
//                one(mockOutputImpl).writeByte((byte)1); // indicates a string
//                one(mockOutputImpl).writeUTF("name-field");
//                one(mockOutputImpl).writeUTF("_HARRY_");               
            }});
        memento.encodedData(mockOutputImpl );
        mockery.assertIsSatisfied();
    }    
    
    
    /*
    @Test
    public void testDifferentAdaptersReturned() throws Exception {
        final Memento memento = new Memento(rootAdapter);
        returnedAdapter = memento.recreateObject();
        
        rootAdapter.getObject();
        returnedAdapter.getObject();
        
        assertNotSame(rootAdapter, returnedAdapter);
        mockery.assertIsSatisfied();
    }

    @Test
    public void testDifferentObjectsReturned() throws Exception {
        final Memento memento = new Memento(rootAdapter);
        returnedAdapter = memento.recreateObject();
        assertNotSame(rootAdapter.getObject(), returnedAdapter.getObject());
        mockery.assertIsSatisfied();
    }

    @Test
    public void testHaveSameOid() throws Exception {
        final Memento memento = new Memento(rootAdapter);
        returnedAdapter = memento.recreateObject();
        
        rootAdapter.getObject();
        returnedAdapter.getObject();
        
        assertEquals(rootAdapter.getOid(), returnedAdapter.getOid());
        mockery.assertIsSatisfied();
    }

    @Test
    public void testHaveSameSpecification() throws Exception {
        final Memento memento = new Memento(rootAdapter);
        returnedAdapter = memento.recreateObject();
        
        rootAdapter.getObject();
        returnedAdapter.getObject();
        
        assertEquals(rootAdapter.getSpecificatio        assertEquals("", ((ObjectData) data).getEntry("name-field"));
n(), returnedAdapter.getSpecification());
        mockery.assertIsSatisfied();
    }

*/
    
    /*
    @Test
    public void testEncoding() throws Exception {
        final TransferableWriter writer = mockery.mock(TransferableWriter.class);
        final Transferable object = mockery.mock(Transferable.class);
        mockery.checking(new Expectations() {
            {
                one(writer).writeObject(object );
             
                ignoring(object);
            }
        });
        
        final Memento memento = new Memento(rootAdapter);
        memento.writeData(writer);
        mockery.assertIsSatisfied();
     
    }
    */
/*
    @Test
    public void testName() throws Exception {
        final Memento memento = new Memento(rootAdapter);
        returnedAdapter = memento.recreateObject();
        assertEquals("Harry", ((TestObject) rootAdapter.getObject()).getName());
        assertEquals("Harry", ((TestObject) returnedAdapter.getObject()).getName());
    }
*/
    
/*
    @Test
    public void testNull2() throws Exception {
        final Memento memento = new Memento(null);
        Data data = memento.getData();
        
        assertEquals(null, data);
        // mockery.assertIsSatisfied();
    }
    
    @Test
    public void testNull() throws Exception {
        final Memento memento = new Memento(null);
        returnedAdapter = memento.recreateObject();
        Assert.assertNull(returnedAdapter);
        // mockery.assertIsSatisfied();
    }

*/
}

// Copyright (c) Naked Objects Group Ltd.
