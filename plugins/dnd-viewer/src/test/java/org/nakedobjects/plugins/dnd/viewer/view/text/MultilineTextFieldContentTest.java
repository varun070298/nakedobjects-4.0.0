package org.nakedobjects.plugins.dnd.viewer.view.text;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
import org.nakedobjects.plugins.dnd.viewer.view.text.CursorPosition;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextSelection;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public class MultilineTextFieldContentTest {

    private TextContent content;
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
        Logger.getRootLogger().setLevel(Level.OFF);

        servicesList = Collections.emptyList();
        
        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockSpecificationLoader = mockery.mock(SpecificationLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        
        mockery.checking(new Expectations() {
            {
                ignoring(mockTemplateImageLoader);
                ignoring(mockSpecificationLoader);
                ignoring(mockPersistenceSessionFactory);
                ignoring(mockUserProfileLoader);
                ignoring(mockAuthenticationManager);
                ignoring(mockAuthorizationManager);
            }
        });

        NakedObjectSessionFactoryDefault sessionFactory = new NakedObjectSessionFactoryDefault(
                DeploymentType.EXPLORATION, 
                new PropertiesConfiguration(), 
                mockTemplateImageLoader, 
                mockSpecificationLoader, 
                mockAuthenticationManager, 
		        mockAuthorizationManager,
                mockUserProfileLoader, 
                mockPersistenceSessionFactory, servicesList);
        NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
        sessionFactory.init();
                
        final TextBlockTarget target = new TextBlockTargetExample();

        content = new TextContent(target, 4, TextContent.WRAPPING);
        content.setText("Line one\nLine two\nLine three\nLine four that is long enough that it wraps");
    }

    @Test
    public void testDeleteOnSingleLine() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 1, 3));
        selection.extendTo(new CursorPosition(content, 1, 7));
        content.delete(selection);
        Assert.assertEquals("Line one\nLino\nLine three\nLine four that is long enough that it wraps", content.getText());
    }

    @Test
    public void testDeleteOnSingleLineWithStartAndEndOutOfOrder() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 1, 7));
        selection.extendTo(new CursorPosition(content, 1, 3));
        content.delete(selection);
        Assert.assertEquals("Line one\nLino\nLine three\nLine four that is long enough that it wraps", content.getText());
    }

    @Test
    public void testDeleteAcrossTwoLines() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 0, 6));
        selection.extendTo(new CursorPosition(content, 1, 6));
        content.delete(selection);
        Assert.assertEquals("Line owo\nLine three\nLine four that is long enough that it wraps", content.getText());
    }

    @Test
    public void testDeleteAcrossThreeLines() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 0, 6));
        selection.extendTo(new CursorPosition(content, 2, 6));
        content.delete(selection);
        Assert.assertEquals("Line ohree\nLine four that is long enough that it wraps", content.getText());
    }

    @Test
    public void testDeleteAcrossThreeLinesIncludingWrappedBlock() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 2, 5));
        selection.extendTo(new CursorPosition(content, 4, 5));
        content.delete(selection);
        Assert.assertEquals("Line one\nLine two\nLine enough that it wraps", content.getText());
    }

    @Test
    public void testDeleteWithinWrappedBlock() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 5, 0));
        selection.extendTo(new CursorPosition(content, 5, 3));
        content.delete(selection);
        Assert.assertEquals("Line one\nLine two\nLine three\nLine four that is long enough that wraps", content.getText());
    }

    @Test
    public void testDeleteMultipleLinesWithinWrappedBlock() {
        final TextSelection selection = new TextSelection(content);
        selection.resetTo(new CursorPosition(content, 3, 5));
        selection.extendTo(new CursorPosition(content, 5, 3));
        content.delete(selection);
        Assert.assertEquals("Line one\nLine two\nLine three\nLine wraps", content.getText());
    }

    @Test
    public void testLineBreaks() {
        Assert.assertEquals(6, content.getNoLinesOfContent());

        content.setNoDisplayLines(8);
        final String[] lines = content.getDisplayLines();

        Assert.assertEquals(8, lines.length);
        Assert.assertEquals("Line one", lines[0]);
        Assert.assertEquals("Line two", lines[1]);
        Assert.assertEquals("Line three", lines[2]);
        Assert.assertEquals("Line four that is ", lines[3]);
        Assert.assertEquals("long enough that ", lines[4]);
        Assert.assertEquals("it wraps", lines[5]);
        Assert.assertEquals("", lines[6]);
        Assert.assertEquals("", lines[7]);

    }

}
// Copyright (c) Naked Objects Group Ltd.
