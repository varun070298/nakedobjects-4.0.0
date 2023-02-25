package org.nakedobjects.plugins.xml.objectstore.internal.services.xml;

import java.util.Enumeration;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.plugins.xml.objectstore.internal.data.xml.XmlFile;
import org.nakedobjects.plugins.xml.objectstore.internal.services.ServiceManager;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


class ServiceElement {
    final SerialOid oid;
    final String id;

    public ServiceElement(final SerialOid oid, final String id) {
        Assert.assertNotNull("oid", oid);
        Assert.assertNotNull("id", id);
        this.oid = oid;
        this.id = id;
    }
}

class ServiceHandler extends DefaultHandler {
    Vector services = new Vector();

    @Override
    public void startElement(final String ns, final String name, final String tagName, final Attributes attrs)
            throws SAXException {
        if (tagName.equals("service")) {
            final long oid = Long.valueOf(attrs.getValue("oid"), 16).longValue();
            final String id = attrs.getValue("id");
            final ServiceElement service = new ServiceElement(SerialOid.createPersistent(oid), id);
            services.addElement(service);
        }
    }

}

public class XmlServiceManager implements ServiceManager {
    private static final String SERVICES_FILE_NAME = "services";
    private Vector services;
    private final XmlFile xmlFile;

    public XmlServiceManager(final XmlFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    private String encodedOid(final SerialOid oid) {
        return Long.toHexString(oid.getSerialNo()).toUpperCase();
    }

    public Oid getOidForService(final String name) {
        for (final Enumeration e = services.elements(); e.hasMoreElements();) {
            final ServiceElement element = (ServiceElement) e.nextElement();
            if (element.id.equals(name)) {
                return element.oid;
            }
        }
        return null;
    }

    public void loadServices() {
        final ServiceHandler handler = new ServiceHandler();
        xmlFile.parse(handler, SERVICES_FILE_NAME);
        services = handler.services;
    }

    public void registerService(final String name, final Oid oid) {
        final SerialOid soid = (SerialOid) oid;
        final ServiceElement element = new ServiceElement(soid, name);
        services.addElement(element);
        saveServices();
    }

    public final void saveServices() {
        final StringBuffer xml = new StringBuffer();
        final String tag = SERVICES_FILE_NAME;
        xml.append("<" + tag + ">\n");
        for (final Enumeration e = services.elements(); e.hasMoreElements();) {
            final ServiceElement element = (ServiceElement) e.nextElement();
            xml.append("  <service oid=\"");
            xml.append(encodedOid(element.oid));
            xml.append("\" id=\"");
            xml.append(element.id);
            xml.append("\" />\n");
        }
        xml.append("</" + tag + ">\n");
        xmlFile.writeXml(SERVICES_FILE_NAME, xml);
    }
}

// Copyright (c) Naked Objects Group Ltd.
