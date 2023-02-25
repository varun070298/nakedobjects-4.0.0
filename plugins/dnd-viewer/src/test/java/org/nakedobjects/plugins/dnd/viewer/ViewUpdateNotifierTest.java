package org.nakedobjects.plugins.dnd.viewer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.dnd.DummyView;
import org.nakedobjects.plugins.dnd.DummyWorkspaceView;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.content.RootObject;
import org.nakedobjects.plugins.dnd.viewer.notifier.ViewUpdateNotifier;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationSession;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestProxySystem;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;

@RunWith(JMock.class)
public class ViewUpdateNotifierTest {

    private Mockery mockery = new JUnit4Mockery();
    
    private ExposedViewUpdateNotifier notifier;
    private TestProxyNakedObject object;
    
    protected TemplateImageLoader mockTemplateImageLoader;
    protected SpecificationLoader mockSpecificationLoader;
    private UserProfileLoader mockUserProfileLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    protected PersistenceSession mockPersistenceSession;
    protected NakedObjectTransactionManager mockTransactionManager;
    protected NakedObjectTransaction mockTransaction;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;

	private List<Object> servicesList;



    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        servicesList = Collections.emptyList();

        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockPersistenceSession = mockery.mock(PersistenceSession.class);
        mockTransactionManager = mockery.mock(NakedObjectTransactionManager.class);
        mockTransaction = mockery.mock(NakedObjectTransaction.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        
        mockery.checking(new Expectations() {{
            ignoring(mockTemplateImageLoader);
            ignoring(mockSpecificationLoader);
            ignoring(mockAuthenticationManager);
            ignoring(mockAuthorizationManager);

            one(mockUserProfileLoader).getProfile(with(any(AuthenticationSession.class)));
            will(returnValue(new UserProfile()));
            
            ignoring(mockUserProfileLoader);

            allowing(mockPersistenceSessionFactory).createPersistenceSession();
            will(returnValue(mockPersistenceSession));

            ignoring(mockPersistenceSessionFactory);
            
            allowing(mockPersistenceSession).getTransactionManager();
            will(returnValue(mockTransactionManager));

            ignoring(mockPersistenceSession);

            allowing(mockTransactionManager).getTransaction();
            will(returnValue(mockTransaction));
            
            ignoring(mockTransaction);
        }});

        NakedObjectSessionFactory sessionFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    new PropertiesConfiguration(), 
                    mockTemplateImageLoader, 
                    mockSpecificationLoader, 
                    mockAuthenticationManager, 
    		        mockAuthorizationManager,
                    mockUserProfileLoader, 
                    mockPersistenceSessionFactory, servicesList);
        sessionFactory.init();
        NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
        
        NakedObjectsContext.openSession(new ExplorationSession());

        notifier = new ExposedViewUpdateNotifier();

        object = new TestProxyNakedObject();
    }

    @After
    public void tearDown() {
        NakedObjectsContext.closeSession();
    }
    
    private DummyView createView(final NakedObject object) {
        final DummyView view = new DummyView();
        view.setupContent(new RootObject(object));
        return view;
    }

    @Test
    public void testAddViewWithNonObjectContent() {
        final DummyView view = createView(null);
        notifier.add(view);
        notifier.assertEmpty();
    }

    @Test
    public void testAddViewWithObjectContent() {
        final DummyView view = addViewForObject();
        notifier.assertContainsViewForObject(view, object);
    }

    private DummyView addViewForObject() {
        final DummyView view = createView(object);
        notifier.add(view);
        return view;
    }

    @Test
    public void testAddViewIsIgnoredAfterFirstCall() {
        final DummyView view = addViewForObject();
        try {
            notifier.add(view);
            fail();
        } catch (final NakedObjectException expected) {}
    }

    @Test
    public void testDebug() throws Exception {
        addViewForObject();
        final DebugString debugString = new DebugString();
        notifier.debugData(debugString);
        assertNotNull(debugString.toString());
    }

    @Test
    public void testRemoveView() {
        final Vector vector = new Vector();
        final DummyView view = createView(object);
        vector.addElement(view);
        notifier.setupViewsForObject(object, vector);

        notifier.remove(view);
        notifier.assertEmpty();
    }

    @Test
    public void testViewDirty() {
        // nasty ... need to tidy up the setup
        TestProxySystem testProxySystem = new TestProxySystem();
        testProxySystem.init();
        
        object.setupResolveState(ResolveState.RESOLVED);


        final Vector vector = new Vector();
        final DummyView view1 = createView(object);
        vector.addElement(view1);

        final DummyView view2 = createView(object);
        vector.addElement(view2);

        notifier.setupViewsForObject(object, vector);

        notifier.invalidateViewsForChangedObjects();
        assertEquals(0, view1.invalidateContent);
        assertEquals(0, view2.invalidateContent);

        NakedObjectsContext.getUpdateNotifier().addChangedObject(object);
        notifier.invalidateViewsForChangedObjects();
        
        assertEquals(1, view1.invalidateContent);
        assertEquals(1, view2.invalidateContent);
    }

    @Test
    public void testDisposedViewsRemoved() {
        // nasty ... need to tidy up the setup
        TestProxySystem testProxySystem = new TestProxySystem();
        testProxySystem.init();
        
        final DummyWorkspaceView workspace = new DummyWorkspaceView();

        final Vector vector = new Vector();
        final DummyView view1 = createView(object);
        view1.setParent(workspace);
        workspace.addView(view1);
        vector.addElement(view1);

        final DummyView view2 = createView(object);
        view2.setParent(workspace);
        workspace.addView(view2);
        vector.addElement(view2);

        notifier.setupViewsForObject(object, vector);

        notifier.invalidateViewsForChangedObjects();
        assertEquals(0, view1.invalidateContent);
        assertEquals(0, view2.invalidateContent);

        NakedObjectsContext.getUpdateNotifier().addDisposedObject(object);
        notifier.removeViewsForDisposedObjects();
        assertEquals(0, workspace.getSubviews().length);
        
    }
}

class ExposedViewUpdateNotifier extends ViewUpdateNotifier {

    public void assertContainsViewForObject(final View view, final NakedObject object) {
        Assert.assertTrue(viewListByAdapter.containsKey(object));
        final Vector viewsForObject = (Vector) viewListByAdapter.get(object);
        Assert.assertTrue(viewsForObject.contains(view));
    }

    public void setupViewsForObject(final NakedObject object, final Vector vector) {
        viewListByAdapter.put(object, vector);
    }

    public void assertEmpty() {
        Assert.assertTrue("Not empty", viewListByAdapter.isEmpty());
    }
}
// Copyright (c) Naked Objects Group Ltd.
