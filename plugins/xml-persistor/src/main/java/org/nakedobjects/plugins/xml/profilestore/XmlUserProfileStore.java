package org.nakedobjects.plugins.xml.profilestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.xml.objectstore.internal.data.xml.XmlFile;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.services.ServiceUtil;
import org.nakedobjects.runtime.userprofile.Options;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.nakedobjects.runtime.userprofile.UserProfileStore;

public class XmlUserProfileStore implements UserProfileStore {
    private static final String XML_DIR = ConfigurationConstants.ROOT + "xmluserprofile.dir";
    private final XmlFile xmlFile;

    public XmlUserProfileStore(NakedObjectConfiguration configuration) {
        String directory = configuration.getString(XML_DIR, "xml/profiles");
        xmlFile = new XmlFile(configuration, directory);
    }
    
    public UserProfile getUserProfile(String userName) {
        final UserProfileDataHandler handler = new UserProfileDataHandler();
        if (xmlFile.parse(handler, userName)) {
            return handler.getUserProfile();
        } else {
            return null;
        }
    }

    public boolean isFixturesInstalled() {
        return xmlFile.isFixturesInstalled();
    }

    public void save(final String userName, final UserProfile userProfile) {
        try {
			saveElseException(userName, userProfile);
		} catch (IOException e) {
			throw new NakedObjectException(e);
		}
    }

	private void saveElseException(final String userName,
			final UserProfile userProfile) throws IOException {
		final StringBuffer xml = new StringBuffer();
        xml.append("<profile>\n");
        
        xml.append("  <options>\n");
        Options options = userProfile.getOptions();
        Iterator<String> names = options.names();
        while (names.hasNext()) {
            String name = names.next();
            xml.append("      <option" + attribute("id", name)+ ">"+ options.value(name) + "</option>\n"); 
        }
        xml.append("  </options>\n");
        
        xml.append("  <perspectives>\n");
        for (String perspectiveName : userProfile.list()) {
            PerspectiveEntry perspective = userProfile.getPerspective(perspectiveName);

            xml.append("  <perspective" + attribute("name", perspectiveName)+ ">\n");
            xml.append("    <services>\n");
            for (Object service : perspective.getServices()) {
                xml.append("      <service " + attribute("id", ServiceUtil.id(service))+ "/>\n"); 
            }
            xml.append("    </services>\n");
            xml.append("    <objects>\n");
            for (Object object : perspective.getObjects()){
                NakedObject nakedObject = getPersistenceSession().getAdapterManager().adapterFor(object);
                OutputStream out = new ByteArrayOutputStream();
                DataOutputStreamExtended outputImpl   = new DataOutputStreamExtended(out);
                nakedObject.getOid().encode(outputImpl);
                // FIX need to sort out encoding
                // xml.append("      <object>" + out.toString() + "</object>\n");
            }
            xml.append("    </objects>\n");
            xml.append("  </perspective>\n");
        }
        xml.append("  </perspectives>\n");
   
        xml.append("</profile>\n");
        xmlFile.writeXml(userName, xml);
	}

    private String attribute(final String name, final String value) {
        return " " + name + "=\"" + value + "\"";
    }


    /////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////
    
    
	protected static PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}
    

}


// Copyright (c) Naked Objects Group Ltd.
