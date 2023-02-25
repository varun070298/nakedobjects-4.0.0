package org.nakedobjects.plugins.xml.profilestore;

import java.util.List;

import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.services.ServiceUtil;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;
import org.nakedobjects.runtime.userprofile.UserProfile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class UserProfileDataHandler extends DefaultHandler {
    private final StringBuffer data = new StringBuffer();
    private final UserProfile userProfile = new UserProfile();
    private String optionName;
    private boolean isProfile;
    private boolean isOptions;
    private boolean isPerspectives;
    private PerspectiveEntry perspective;
    private boolean isServices;
    private boolean isObjects;

    public UserProfile getUserProfile() {
        return userProfile;
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int end) throws SAXException {
        data.append(new String(ch, start, end));
    }

    @Override
    public void endElement(final String ns, final String name, final String tagName) throws SAXException {
        if (tagName.equals("options")) {
            isOptions = false;
        } else if (tagName.equals("perspectives")) {
            isPerspectives = false;
        } else if (tagName.equals("perspective")) {
            // TODO add perspective to profile
            
            perspective = null;
        } else if (tagName.equals("services")) {
            isServices = false;
        } else if (tagName.equals("objects")) {
            isObjects = false;
        } else if (tagName.equals("option")) {
            final String value = data.toString();
            userProfile.addToOptions(optionName, value);
        } else if (tagName.equals("name")) {
            final String value = data.toString();
            System.out.println(value);
        }
    }

    @Override
    public void startElement(final String ns, final String name, final String tagName, final Attributes attributes)
            throws SAXException {

        if (isProfile) {
            if (isOptions) {
                if (tagName.equals("option")) {
                    optionName = attributes.getValue("id");
                    data.setLength(0);
                } else {
                    throw new SAXException("Invalid element in options: " + tagName);
                }
            } else if (isPerspectives) {
                if (perspective != null) {
                    if (isServices) {
                        if (tagName.equals("service")){
                            String serviceId = attributes.getValue("id");
                            List<Object> serviceObjects = NakedObjectsContext.getServices();
                            for (Object service : serviceObjects) {
                                if (ServiceUtil.id(service).equals(serviceId)) {
                                    perspective.addToServices(service);
                                    break;
                                }
                            }
                        } else {
                            throw new SAXException("Invalid element in services: " + tagName);
                        }
                    } else if (isObjects) {
                         // TODO reload objects
                    } else if (tagName.equals("services")) {
                        isServices = true;
                    } else if (tagName.equals("objects")) {
                        isObjects = true;
                    } else {
                        throw new SAXException("Invalid element in perspective: " + tagName);
                    }
                } else if (tagName.equals("perspective")) {
                    perspective = userProfile.newPerspective(attributes.getValue("name"));
                } else {
                    throw new SAXException("Invalid element in perspectives: " + tagName);
                }
            } else if (tagName.equals("options")) {
                isOptions = true;
            } else if (tagName.equals("perspectives") && !isOptions) {
                isPerspectives = true;
            } else {
                throw new SAXException("Invalid element in profile: " + tagName);
            }

        }
        /*
         * else { throw new SAXException("Invalid data"); } }
         */

        if (tagName.equals("profile")) {
            isProfile = true;
        }

    }

}

// Copyright (c) Naked Objects Group Ltd.
