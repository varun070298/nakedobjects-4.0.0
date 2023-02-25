package org.nakedobjects.plugins.xml.profilestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class UserProfileDataHandlerTest {

    @Test
    public void testParsing() throws Exception {
        Mockery mockery = new JUnit4Mockery();
        ArrayList<Object> servicesList = new ArrayList<Object>();
        TestServiceObject1 service = new TestServiceObject1();
        servicesList.add(service);
        NakedObjectSessionFactory executionContextFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    new PropertiesConfiguration(), 
                    mockery.mock(TemplateImageLoader.class), 
                    mockery.mock(SpecificationLoader.class), 
                    mockery.mock(AuthenticationManager.class), 
                    mockery.mock(AuthorizationManager.class),
                    mockery.mock(UserProfileLoader.class), 
                    mockery.mock(PersistenceSessionFactory.class), servicesList);
          
        NakedObjectsContextStatic.createRelaxedInstance(executionContextFactory);
 
        
        
        XMLReader parser = XMLReaderFactory.createXMLReader();
        UserProfileDataHandler handler = new UserProfileDataHandler();
        parser.setContentHandler(handler);
        parser.parse(new InputSource(new InputStreamReader(new FileInputStream("test.xml"))));

        UserProfile profile = handler.getUserProfile();
        assertNotNull(profile);
        
        assertEquals(null, profile.getOptions().value("device"));
        assertEquals("on", profile.getOptions().value("power"));

        List<String> list = profile.list();
        assertEquals(2, list.size());
        assertEquals("Library", list.get(0));
        assertEquals("Admin", list.get(1));
        
        assertEquals("Admin", profile.getPerspective().getName());
        assertEquals(1, profile.getPerspective().getServices().size());
        assertEquals(service, profile.getPerspective().getServices().get(0));
    }
}


// Copyright (c) Naked Objects Group Ltd.
