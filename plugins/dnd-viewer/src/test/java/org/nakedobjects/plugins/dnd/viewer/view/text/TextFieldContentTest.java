package org.nakedobjects.plugins.dnd.viewer.view.text;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.view.text.CursorPosition;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextBlockTarget;
import org.nakedobjects.plugins.dnd.viewer.view.text.TextContent;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public class TextFieldContentTest {

    private TextContent content;

    private Mockery mockery = new JUnit4Mockery();
    
    protected TemplateImageLoader mockTemplateImageLoader;
    protected SpecificationLoader mockSpecificationLoader;
    private UserProfileLoader mockUserProfileLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    protected PersistenceSession mockPersistenceSession;
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

        final TextBlockTarget target = new TextBlockTargetExample();

        content = new TextContent(target, 4, TextContent.WRAPPING);
    }

    @Test
    public void testCreate() {
        assertEquals("", content.getText());
        assertEquals(1, content.getNoLinesOfContent());
    }

    @Test
    public void testDeleteLeft() {
        content.setText("abcdefghijklm");
        content.deleteLeft(new CursorPosition(content, 0, 2));
        assertEquals("acdefghijklm", content.getText());
    }

    @Test
    public void testDeleteRight() {
        content.setText("abcdefghijklm");
        content.deleteRight(new CursorPosition(content, 0, 2));
        assertEquals("abdefghijklm", content.getText());
    }

    @Test
    public void testDisplayLineCount() {
        assertEquals(4, content.getNoDisplayLines());
    }

    @Test
    public void testMinimalTextEqualsOneLine() {
        content.setText("test");
        assertEquals(1, content.getNoLinesOfContent());
    }

    @Test
    public void testInsert() {
    /*
     * CursorPosition cursor = new CursorPosition(0,0); content.insert(cursor, "test string");
     */
    }

    @Test
    public void testNumberOfDisplayLines() {
        assertEquals(4, content.getNoDisplayLines());
        assertEquals(4, content.getDisplayLines().length);
        assertEquals("", content.getDisplayLines()[0]);
        assertEquals("", content.getDisplayLines()[1]);
        assertEquals("", content.getDisplayLines()[2]);
        assertEquals("", content.getDisplayLines()[3]);

        content.setNoDisplayLines(6);
        assertEquals(6, content.getNoDisplayLines());
        assertEquals(6, content.getDisplayLines().length);
        assertEquals("", content.getDisplayLines()[0]);
        assertEquals("", content.getDisplayLines()[1]);
        assertEquals("", content.getDisplayLines()[2]);
        assertEquals("", content.getDisplayLines()[3]);
        assertEquals("", content.getDisplayLines()[4]);
        assertEquals("", content.getDisplayLines()[5]);
    }

    @Test
    public void testAlignField() {
        // the following text wraps so it takes up 9 line
        content
                .setText("Naked Objects - a framework that exposes behaviourally complete business objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group");

        assertEquals(9, content.getNoLinesOfContent());

        String[] lines = content.getDisplayLines();
        assertEquals(4, lines.length);
        assertEquals("Naked Objects - a ", lines[0]);
        assertEquals("framework that ", lines[1]);
        assertEquals("exposes ", lines[2]);
        assertEquals("behaviourally complete ", lines[3]);

        content.alignDisplay(6);
        assertEquals(4, content.getNoDisplayLines());
        lines = content.getDisplayLines();
        assertEquals(4, lines.length);
        // assertEquals("business objects ", lines[0]);
        assertEquals("directly to the user. ", lines[0]);
        assertEquals("Copyright (C) 2000 ", lines[1]);
        assertEquals("- 2005 Naked ", lines[2]);
        assertEquals("Objects Group", lines[3]);
    }

    @Test
    public void testInstert() {
        content.setText("at");
        final CursorPosition cursor = new CursorPosition(content, 0, 0);
        content.insert(cursor, "fl");

        assertEquals("flat", content.getText());
        assertEquals(4, content.getNoDisplayLines());
        assertEquals(1, content.getNoLinesOfContent());
    }

    @Test
    public void testInstertOverTheEndOfLine() {
        final CursorPosition cursor = new CursorPosition(content, 0, 0);
        content.insert(cursor, "test insert that is longer than the four lines that were originally allocated for this test");

        assertEquals("test insert that is longer than the four lines that were originally allocated for this test", content
                .getText());
        assertEquals(4, content.getNoDisplayLines());
        assertEquals(6, content.getNoLinesOfContent());
    }

    @Test
    public void testCursorPostioningAtCorner() {
        content.setText("test insert that is longer than a single line");
        assertEquals(0, content.cursorAtLine(new Location()));
        assertEquals(0, content.cursorAtCharacter(new Location(), 0));
    }

    @Test
    public void testCursorPostioningByLine() {
        content.setText("test insert that is longer than a single line");
        assertEquals(0, content.cursorAtLine(new Location(1000, 0)));
        assertEquals(0, content.cursorAtLine(new Location(1000, 10)));
        assertEquals(0, content.cursorAtLine(new Location(1000, 14)));

        assertEquals(1, content.cursorAtLine(new Location(1000, 15)));

        assertEquals(1, content.cursorAtLine(new Location(1000, 25)));
        assertEquals(1, content.cursorAtLine(new Location(1000, 29)));

        assertEquals(2, content.cursorAtLine(new Location(1000, 30)));
        assertEquals(2, content.cursorAtLine(new Location(1000, 44)));

        assertEquals(3, content.cursorAtLine(new Location(1000, 45)));
    }

    @Test
    public void testCursorPostioningByCharacter() {
        content.setText("test insert that");
        assertEquals(0, content.cursorAtCharacter(new Location(0, 1000), 0));
        assertEquals(0, content.cursorAtCharacter(new Location(3, 1000), 0));

        assertEquals(1, content.cursorAtCharacter(new Location(4, 1000), 0));
        assertEquals(1, content.cursorAtCharacter(new Location(13, 1000), 0));

        assertEquals(2, content.cursorAtCharacter(new Location(14, 1000), 0));
        assertEquals(2, content.cursorAtCharacter(new Location(23, 1000), 0));

        assertEquals(15, content.cursorAtCharacter(new Location(153, 1000), 0));

        assertEquals(16, content.cursorAtCharacter(new Location(154, 1000), 0));

        assertEquals(16, content.cursorAtCharacter(new Location(199, 1000), 0));
    }

    @Test
    public void testCursorPostioningByCharacterPastEnd() {
        content.setText("test insert that");
        assertEquals(16, content.cursorAtCharacter(new Location(190, 0), 0));
        assertEquals(0, content.cursorAtCharacter(new Location(0, 0), 0));
        assertEquals(16, content.cursorAtCharacter(new Location(35, 0), 2));
    }

    @Test
    public void testCursorPostioningByCharacterOnLine2() {
        content.setNoDisplayLines(4);
        content.setText("test insert that that spans three lines only");
        assertEquals(0, content.cursorAtCharacter(new Location(0, 1000), 2));
        assertEquals(0, content.cursorAtCharacter(new Location(3, 1000), 2));

        assertEquals(1, content.cursorAtCharacter(new Location(4, 1000), 2));
        assertEquals(1, content.cursorAtCharacter(new Location(13, 1000), 2));

        assertEquals(2, content.cursorAtCharacter(new Location(14, 1000), 2));
        assertEquals(2, content.cursorAtCharacter(new Location(23, 1000), 2));

        assertEquals(10, content.cursorAtCharacter(new Location(14, 1000), 3));
        assertEquals(10, content.cursorAtCharacter(new Location(23, 1000), 3));

    }

}
// Copyright (c) Naked Objects Group Ltd.
