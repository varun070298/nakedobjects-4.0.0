package org.nakedobjects.plugins.dnd.viewer.basic;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.dnd.DummyView;
import org.nakedobjects.plugins.dnd.TestToolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public class ScrollBorderTest {

    private Mockery mockery = new JUnit4Mockery();
    
    protected TemplateImageLoader mockTemplateImageLoader;
    protected SpecificationLoader mockSpecificationLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    private UserProfileLoader mockUserProfileLoader;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;

	private List<Object> servicesList;



    @Before
    public void setUp() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);

        servicesList = Collections.emptyList();
        
        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);

        mockery.checking(new Expectations() {
            {
                ignoring(mockTemplateImageLoader);
                ignoring(mockSpecificationLoader);
                ignoring(mockUserProfileLoader);
                ignoring(mockPersistenceSessionFactory);
                ignoring(mockAuthenticationManager);
                ignoring(mockAuthorizationManager);
            }
        });

        TestToolkit.createInstance();

        PropertiesConfiguration configuration = new PropertiesConfiguration();
        NakedObjectSessionFactory sessionFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    configuration, 
                    mockTemplateImageLoader, 
                    mockSpecificationLoader, 
                    mockAuthenticationManager, 
    		        mockAuthorizationManager,
                    mockUserProfileLoader, 
                    mockPersistenceSessionFactory, servicesList);
        sessionFactory.init();
        NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
    }

    @Test
    public void testScrollBar() {
        final View view = new ScrollBorder(new DummyView());
        view.setMaximumSize(new Size(100, 200));

        ViewAreaType type = view.viewAreaType(new Location(20, 190));
        Assert.assertEquals(ViewAreaType.INTERNAL, type);

        type = view.viewAreaType(new Location(95, 20));
        Assert.assertEquals(ViewAreaType.INTERNAL, type);
    }

    @Test
    public void testSetSizeSetsUpContentAndHeaderSizes() {
        final DummyView contentView = new DummyView();
        contentView.setMaximumSize(new Size(300, 400));

        final DummyView topHeader = new DummyView();
        topHeader.setMaximumSize(new Size(0, 20));

        final DummyView leftHeader = new DummyView();
        leftHeader.setMaximumSize(new Size(30, 0));

        final View scrollBorder = new ScrollBorder(contentView, leftHeader, topHeader);

        scrollBorder.setSize(new Size(100, 200));

        Assert.assertEquals(new Size(300, 400), contentView.getSize());
        Assert.assertEquals(new Size(300, 20), topHeader.getSize());
        Assert.assertEquals(new Size(30, 400), leftHeader.getSize());

    }

    @Test
    public void testSetSizeSetsUpContentAndHeaderSizes2() {
        final DummyView contentView = new DummyView();
        contentView.setMaximumSize(new Size(300, 400));

        final DummyView topHeader = new DummyView();
        topHeader.setMaximumSize(new Size(0, 20));

        final DummyView leftHeader = new DummyView();
        leftHeader.setMaximumSize(new Size(30, 0));

        final View scrollBorder = new ScrollBorder(contentView, leftHeader, topHeader);

        scrollBorder.setSize(new Size(100, 200));

    }
}
// Copyright (c) Naked Objects Group Ltd.
