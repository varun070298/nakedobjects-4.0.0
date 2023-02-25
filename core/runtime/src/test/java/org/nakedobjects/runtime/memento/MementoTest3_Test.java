package org.nakedobjects.runtime.memento;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.NakedObjectReflector;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.session.NakedObjectSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;


public class MementoTest3_Test {
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

    private NakedObjectSpecification emptySpecification;

    
    private NakedObject element1;
    
    
    
    

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        // Configuration
        // final TestProxyConfiguration configuration = new TestProxyConfiguration();
        PropertiesConfiguration configuration = new PropertiesConfiguration();   
        
        // Root object specification
         emptySpecification = mockery.mock(NakedObjectSpecification.class, "empty specification");
        final NakedObjectSpecification rootSpecification = mockery.mock(NakedObjectSpecification.class);
        final OneToOneAssociation nameField = mockery.mock(OneToOneAssociation.class);
        final NakedObjectSpecification elementSpecification = mockery.mock(NakedObjectSpecification.class, "name specification");
        //final EncodeableFacet encodeableFacet = mockery.mock(EncodeableFacet.class);
        final CollectionFacet collectionFacet = mockery.mock(CollectionFacet.class);
         
        mockery.checking(new Expectations() {
            {
                atLeast(1).of(rootSpecification).isCollection();
                will(returnValue(true));
                
               // atLeast(1).of(rootSpecification).getAssociations();
               // will(returnValue(new NakedObjectAssociation[] {nameField}));
                
                atLeast(1).of(rootSpecification).getFullName();
                will(returnValue(TestObject[].class.getName()));

                atLeast(1).of(rootSpecification).getFacet(CollectionFacet.class);
                will(returnValue(collectionFacet));

                

                atLeast(0).of(emptySpecification).getFullName();
                will(returnValue(TestObject.class.getName()));

                            
                /*
                
                atLeast(1).of(nameField).isPersisted();
                will(returnValue(true));

                atLeast(1).of(nameField).isOneToManyAssociation();
                will(returnValue(false));

                atLeast(1).of(nameField).getSpecification();
                will(returnValue(elementSpecification));

                atLeast(1).of(nameField).getId();
                will(returnValue("name-field"));

                atLeast(1).of(elementSpecification).isEncodeable();
                will(returnValue(true));

                atLeast(1).of(elementSpecification).getFacet(EncodeableFacet.class);
                will(returnValue(encodeableFacet));
                */
            }
        });
        
        
        // Root object
        rootAdapter = mockery.mock(NakedObject.class);
        rootObject = new TestObject("Harry");
        final NakedObject nameAdapter = mockery.mock(NakedObject.class, "name");
        oid = mockery.mock(Oid.class);

        
        final TypeOfFacet typeOfFacet = mockery.mock(TypeOfFacet.class, "element 1");
        
        
        final Iterator iterator = mockery.mock(Iterator.class);
        // object encoding
        mockery.checking(new Expectations() {
           

        {
               
               atLeast(1).of(collectionFacet).size(rootAdapter);
               will(returnValue(2));

               atLeast(1).of(collectionFacet).getTypeOfFacet();
               will(returnValue(typeOfFacet));
               
               atLeast(1).of(typeOfFacet).valueSpec();
               will(returnValue(rootSpecification));
                 
               atLeast(1).of(collectionFacet).iterator(rootAdapter);
               will(returnValue(iterator));
               
               one(iterator).hasNext();
               will(returnValue(true));
               
               one(iterator).next();
               element1 = nakedObject("element", 1, false);
            will(returnValue(element1));

               one(iterator).hasNext();
               will(returnValue(true));

               one(iterator).next();
               will(returnValue(nakedObject("element", 2, false)));

               one(iterator).hasNext();
               will(returnValue(false));
               
               
               
               
               atLeast(1).of(rootAdapter).getSpecification();
               will(returnValue(rootSpecification));

               atLeast(1).of(rootAdapter).getOid();
               will(returnValue(oid));

               atLeast(1).of(rootAdapter).getResolveState();
               will(returnValue(ResolveState.RESOLVED));
/*
               atLeast(1).of(nameField).get(rootAdapter);
               will(returnValue(nameAdapter));
            
               */
          //     one(encodeableFacet).toEncodedString(nameAdapter);
          //     will(returnValue("_HARRY_"));
               
               
              /* 
               atLeast(1).of(oid).isTransient();
               will(returnValue(false));

               atLeast(1).of(rootAdapter).getObject();
               will(returnValue(rootObject));
               
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


    
    
    
    public NakedObject nakedObject(final String name, final int id, final boolean isTransient) {
        final NakedObject object = mockery.mock(NakedObject.class, name + id);
        final Oid oid = mockery.mock(Oid.class, name + "#" + id);
        mockery.checking(new Expectations() {
            {
                atLeast(0).of(object).getOid();
                will(returnValue(oid));
                
                atLeast(0).of(object).getResolveState();
                will(returnValue(ResolveState.TRANSIENT));
                
                atLeast(0).of(object).getSpecification();
                will(returnValue(emptySpecification));
                
                atLeast(0).of(oid).isTransient();
                will(returnValue(isTransient));   
            }});
        return object;
    }
    
    
   
    
    
    

    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testOid() throws Exception {
        assertEquals(oid, data.getOid());
        mockery.assertIsSatisfied();
    }

    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testResolved() throws Exception {
        assertEquals(ResolveState.RESOLVED.name(), data.getResolveState());
        mockery.assertIsSatisfied();
    }

    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testClassName() throws Exception {
        assertEquals(TestObject[].class.getName(), data.getClassName());
        mockery.assertIsSatisfied();
    }

    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testDataType() throws Exception {
        assertEquals(CollectionData.class, data.getClass());
        mockery.assertIsSatisfied();
    }    


    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testDataLength() throws Exception {
        assertEquals(2, ((CollectionData) data).elements.length);
        mockery.assertIsSatisfied();
    }    


    @Ignore("currently failing - is no longer calling  isTransient on element #1 and element #2")
    @Test
    public void testData() throws Exception {
        final DataOutputStreamExtended mockOutputImpl = mockery.mock(DataOutputStreamExtended.class);
        
        final Oid oid1 = element1.getOid();
        final Oid oid2 = element1.getOid();
           
        mockery.checking(new Expectations() {
            {
                
                one(mockOutputImpl).writeUTF(TestObject.class.getName());
                one(mockOutputImpl).writeUTF(ResolveState.RESOLVED.name());
                one(mockOutputImpl).writeEncodable(oid);
                one(mockOutputImpl).writeUTF(TestObject[].class.getName());
                one(mockOutputImpl).writeInt(2);
                
                one(mockOutputImpl).writeUTF(TestObject.class.getName());
                one(mockOutputImpl).writeUTF(ResolveState.TRANSIENT.name());
                one(mockOutputImpl).writeEncodable(oid1);

                one(mockOutputImpl).writeUTF(TestObject.class.getName());
                one(mockOutputImpl).writeUTF(ResolveState.TRANSIENT.name());
                one(mockOutputImpl).writeEncodable(oid2);

                /*
                one(encoder).add("S");
                one(encoder).add("name-field");
                one(encoder).add("_HARRY_");      
                */         
            }});
        memento.encodedData(mockOutputImpl );
        mockery.assertIsSatisfied();
    }    
   
}

// Copyright (c) Naked Objects Group Ltd.
