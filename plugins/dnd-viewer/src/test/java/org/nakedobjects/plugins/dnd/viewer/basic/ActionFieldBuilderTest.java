package org.nakedobjects.plugins.dnd.viewer.basic;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.MockControl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.DummyView;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.view.dialog.ActionFieldBuilder;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authentication.standard.exploration.ExplorationSession;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public class ActionFieldBuilderTest {
    private ActionFieldBuilder builder;

    private Mockery mockery = new JUnit4Mockery();
    
    private NakedObjectConfiguration configuration;
    private List<Object> servicesList;
    protected TemplateImageLoader mockTemplateImageLoader;
    protected SpecificationLoader mockSpecificationLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    private UserProfileLoader mockUserProfileLoader;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;
    

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        configuration = new PropertiesConfiguration();
        servicesList = Collections.emptyList();
        
        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        
        mockery.checking(new Expectations(){{
            ignoring(mockSpecificationLoader);
            ignoring(mockPersistenceSessionFactory);
            
            one(mockUserProfileLoader).getProfile(with(any(AuthenticationSession.class)));
            will(returnValue(new UserProfile()));

            ignoring(mockTemplateImageLoader);
            ignoring(mockAuthenticationManager);
            ignoring(mockAuthorizationManager);
        }});
        
        final SubviewSpec subviewSpec = new SubviewSpec() {
            public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
                return new DummyView();
            }

            public View decorateSubview(final View view) {
                return null;
            }
        };


		final NakedObjectSessionFactoryDefault sessionFactory = new NakedObjectSessionFactoryDefault(
		        DeploymentType.EXPLORATION, 
		        configuration, 
		        mockTemplateImageLoader, 
		        mockSpecificationLoader, 
		        mockAuthenticationManager, 
		        mockAuthorizationManager,
		        mockUserProfileLoader, 
		        mockPersistenceSessionFactory, servicesList);
        
        NakedObjectsContext.setConfiguration(sessionFactory.getConfiguration());
		NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
        NakedObjectsContextStatic.openSession(new ExplorationSession());
        
        builder = new ActionFieldBuilder(subviewSpec);

    }

    @After
    public void tearDown() {
        NakedObjectsContext.closeSession();
    }

    @Test
    public void testUpdateBuild() {
        final MockControl control = MockControl.createControl(View.class);
        final View view = (View) control.getMock();

        control.expectAndDefaultReturn(view.getView(), view);
        control.expectAndDefaultReturn(view.getContent(), null);

        /*
         * DummyView[] views = new DummyView[2]; views[1] = new DummyView(); views[1].setupContent(new
         * ObjectParameter("name", null, null, false, 1, actionContent)); view.setupSubviews(views);
         */

        control.replay();

        // builder.build(view);

        control.verify();
    }

    /*
     * // TODO fails on server as cant load X11 for Text class public void xxxtestNewBuild() {
     * view.setupSubviews(new View[0]);
     * 
     * view.addAction("add TextView0 null"); view.addAction("add MockView1/LabelBorder"); view.addAction("add
     * MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); } public void xxxtestUpdateBuildWhereParameterHasChangedFromNullToAnObject() {
     * DummyView[] views = new DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new
     * ObjectParameter("name", null, null, false, 1, actionContent); views[1].setupContent(objectParameter);
     * view.setupSubviews(views);
     * 
     * actionContent.setParameter(0, new DummyNakedObject());
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxxtestUpdateBuildWhereParameterHasChangedFromAnObjectToNull() { DummyView[] views = new
     * DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new ObjectParameter("name",
     * new DummyNakedObject(), null, false, 1, actionContent); views[1].setupContent(objectParameter);
     * view.setupSubviews(views);
     * 
     * objectParameter.setObject(null);
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxxtestUpdateBuildWhereParameterHasChangedFromOneObjectToAnother() { DummyView[] views =
     * new DummyView[2]; views[1] = new DummyView(); ObjectParameter objectParameter = new
     * ObjectParameter("name", new DummyNakedObject(), null, false, 1, actionContent);
     * views[1].setupContent(objectParameter); view.setupSubviews(views);
     * 
     * objectParameter.setObject(new DummyNakedObject());
     * 
     * view.addAction("replace MockView1 with MockView2/LabelBorder");
     * 
     * builder.build(view);
     * 
     * view.verify(); }
     * 
     * public void xxtestUpdateBuildWhereParameterObjectSetButToSameObject() { DummyView[] views = new
     * DummyView[2]; views[1] = new DummyView(); DummyNakedObject dummyNakedObject = new DummyNakedObject();
     * ObjectParameter objectParameter = new ObjectParameter("name", dummyNakedObject, null, false, 1,
     * actionContent); views[1].setupContent(objectParameter); view.setupSubviews(views);
     * 
     * actionContent.setParameter(0, dummyNakedObject); // objectParameter.setObject(dummyNakedObject);
     * 
     * builder.build(view);
     * 
     * view.verify(); } }
     * 
     * class MockActionHelper extends ActionHelper {
     * 
     * protected MockActionHelper( NakedObject target, Action action, String[] labels, Naked[] parameters,
     * NakedObjectSpecification[] parameterTypes, boolean[] required) { super(target, action, labels,
     * parameters, parameterTypes, required); }
     */
}
// Copyright (c) Naked Objects Group Ltd.
