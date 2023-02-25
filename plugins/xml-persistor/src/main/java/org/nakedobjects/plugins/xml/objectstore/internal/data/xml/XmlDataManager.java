package org.nakedobjects.plugins.xml.objectstore.internal.data.xml;

import java.util.Vector;

import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.xml.objectstore.internal.data.CollectionData;
import org.nakedobjects.plugins.xml.objectstore.internal.data.Data;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectData;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectDataVector;
import org.nakedobjects.plugins.xml.objectstore.internal.data.PersistorException;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ReferenceVector;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlDataManager implements DataManager {
    private final XmlFile xmlFile;

    public XmlDataManager(final XmlFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    
    //////////////////////////////////////////////////////////
    // shutdown
    //////////////////////////////////////////////////////////
    
    public void shutdown() {}


    //////////////////////////////////////////////////////////
    // SAX Handlers
    //////////////////////////////////////////////////////////

    // TODO the following methods are being called repeatedly - is there no
    // caching? See the print statements
    private class DataHandler extends DefaultHandler {
        CollectionData collection;
        StringBuffer data = new StringBuffer();
        String fieldName;
        ObjectData object;

        @Override
        public void characters(final char[] ch, final int start, final int end) throws SAXException {
            data.append(new String(ch, start, end));
            // System.out.println("XML DataHandler " + data);
        }

        @Override
        public void endElement(final String ns, final String name, final String tagName) throws SAXException {
            if (object != null) {
                if (tagName.equals("value")) {
                    final String value = data.toString();
                    object.set(fieldName, value);
                    // System.out.println("XML DataHandler " + data);
                }
            }
        }

        @Override
        public void startElement(final String ns, final String name, final String tagName, final Attributes attributes)
                throws SAXException {
            if (object != null) {
                if (tagName.equals("value")) {
                    fieldName = attributes.getValue("field");
                    data.setLength(0);
                    // System.out.println("XML DataHandler" + fieldName);
                } else if (tagName.equals("association")) {
                    final String fieldName = attributes.getValue("field");
                    final long id = Long.valueOf(attributes.getValue("ref"), 16).longValue();
                    object.set(fieldName, SerialOid.createPersistent(id));
                } else if (tagName.equals("element")) {
                    final long id = Long.valueOf(attributes.getValue("ref"), 16).longValue();
                    object.addElement(fieldName, SerialOid.createPersistent(id));
                } else if (tagName.equals("multiple-association")) {
                    fieldName = attributes.getValue("field");
                    object.initCollection(fieldName);
                }
            } else if (collection != null) {
                if (tagName.equals("element")) {
                    final long id = Long.valueOf(attributes.getValue("ref"), 16).longValue();
                    collection.addElement(SerialOid.createPersistent(id));
                }
            } else {
                if (tagName.equals("naked-object")) {
                    final String typeName = attributes.getValue("type");
                    final long version = Long.valueOf(attributes.getValue("ver"), 16).longValue();
                    final String user = attributes.getValue("user");
                    final long id = Long.valueOf(attributes.getValue("id"), 16).longValue();
                    final NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(typeName);
                    final SerialOid oid = SerialOid.createPersistent(id);
                    object = new ObjectData(spec, oid, new FileVersion(user, version));
                } else if (tagName.equals("collection")) {
                    final String type = attributes.getValue("type");
                    final long version = Long.valueOf(attributes.getValue("ver"), 16).longValue();
                    final String user = attributes.getValue("user");
                    final long id = Long.valueOf(attributes.getValue("id"), 16).longValue();
                    final NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(type);
                    final SerialOid oid = SerialOid.createPersistent(id);
                    collection = new CollectionData(spec, oid, new FileVersion(user, version));
                } else {
                    throw new SAXException("Invalid data");
                }
            }
        }
    }

    private class InstanceHandler extends DefaultHandler {
        Vector<SerialOid> instances = new Vector<SerialOid>();

        @Override
        public void characters(final char[] arg0, final int arg1, final int arg2) throws SAXException {}

        @Override
        public void startElement(final String ns, final String name, final String tagName, final Attributes attrs)
                throws SAXException {
            if (tagName.equals("instance")) {
                final long oid = Long.valueOf(attrs.getValue("id"), 16).longValue();
                instances.addElement(SerialOid.createPersistent(oid));
            }
        }
    }

    private class NumberHandler extends DefaultHandler {
        boolean captureValue = false;
        long value = 0;

        @Override
        public void characters(final char[] arg0, final int arg1, final int arg2) throws SAXException {
            if (captureValue) {
                value = Long.valueOf(new String(arg0, arg1, arg2), 16).longValue();
            }
        }

        @Override
        public void startElement(final String ns, final String name, final String tagName, final Attributes attrs)
                throws SAXException {
            captureValue = tagName.equals("number");
        }
    }


    //////////////////////////////////////////////////////////
    // fixtures
    //////////////////////////////////////////////////////////

    public boolean isFixturesInstalled() {
        return xmlFile.isFixturesInstalled();
    }


    //////////////////////////////////////////////////////////
    // loadData
    //////////////////////////////////////////////////////////

    public Data loadData(final SerialOid oid) {
        final DataHandler handler = new DataHandler();
        xmlFile.parse(handler, filename(oid));

        if (handler.object != null) {
            return handler.object;
        } else {
            return handler.collection;
        }
    }

    
    //////////////////////////////////////////////////////////
    // getInstances, numberOfInstances
    //////////////////////////////////////////////////////////

    public ObjectDataVector getInstances(final ObjectData pattern) {
        final Vector<SerialOid> instances = loadInstances(pattern.getSpecification());

        if (instances == null) {
            return new ObjectDataVector();
        }

        final ObjectDataVector matches = new ObjectDataVector();
        for (SerialOid oid: instances) {
            final ObjectData instanceData = (ObjectData) loadData(oid);
            // TODO check loader first
            if (instanceData == null) {
                throw new NakedObjectException("No data found for " + oid + " (possible missing file)");
            }
            if (matchesPattern(pattern, instanceData)) {
                matches.addElement(instanceData);
            }
        }
        return matches;
    }


	public int numberOfInstances(final ObjectData pattern) {
        return getInstances(pattern).size();
    }

	private Vector<SerialOid> loadInstances(NakedObjectSpecification noSpec) {
		final InstanceHandler handler = new InstanceHandler();
		parseSpecAndSubclasses(handler, noSpec);
		return handler.instances;
	}


	private void parseSpecAndSubclasses(final InstanceHandler handler,
			NakedObjectSpecification noSpec) {
		parseIfNotAbstract(noSpec, handler);
		for(NakedObjectSpecification subSpec: noSpec.subclasses()) {
			parseSpecAndSubclasses(handler, subSpec);
		}
	}


	private void parseIfNotAbstract(NakedObjectSpecification noSpec,
			final InstanceHandler handler) {
		if (noSpec.isAbstract()) {
			return;
		}
		xmlFile.parse(handler, noSpec.getFullName());
	}


    
    /**
     * A helper that determines if two sets of data match. A match occurs when the types are the same and any
     * field in the pattern also occurs in the data set under test.
     */
    // TODO we need to be able to find collection instances as well
    protected boolean matchesPattern(final ObjectData patternData, final ObjectData candidateData) {
        if (patternData == null || candidateData == null) {
            throw new NullPointerException("Can't match on nulls " + patternData + " " + candidateData);
        }
        
        if (!candidateData.getSpecification().isOfType(patternData.getSpecification())) {
        	return false;
        }

        for(final String field: patternData.fields()) {
            final Object patternFieldValue = patternData.get(field);
            final Object candidateFieldValue = candidateData.get(field);

            if (candidateFieldValue instanceof ReferenceVector) {
                final ReferenceVector patternElements = (ReferenceVector) patternFieldValue;
                for (int i = 0; i < patternElements.size(); i++) {
                    final SerialOid requiredElement = patternElements.elementAt(i); // must
                    // have
                    // this
                    // element
                    boolean requiredFound = false;
                    final ReferenceVector testElements = ((ReferenceVector) candidateFieldValue);
                    for (int j = 0; j < testElements.size(); j++) {
                        if (requiredElement.equals(testElements.elementAt(j))) {
                            requiredFound = true;
                            break;
                        }
                    }
                    if (!requiredFound) {
                        return false;
                    }
                }
            } else {
                if (!patternFieldValue.equals(candidateFieldValue)) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Read in the next unique number for the object identifier.
     */
    protected long nextId() throws PersistorException {
        final NumberHandler handler = new NumberHandler();
        xmlFile.parse(handler, "oid");

        final StringBuffer data = new StringBuffer();
        data.append("<number>");
        data.append(handler.value + 1);
        data.append("</number>");
        xmlFile.writeXml("oid", data);

        return handler.value + 1;
    }

    //////////////////////////////////////////////////////////
    // insertObject, remove
    //////////////////////////////////////////////////////////

    /**
     * Save the data for an object and adds the reference to a list of instances
     */
    public final void insertObject(final ObjectData data) {
        if (data.getOid() == null) {
            throw new IllegalArgumentException("Oid must be non-null");
        }

        writeInstanceToItsDataFile(data);
        addReferenceToInstancesFile(data.getOid(), data.getSpecification());
    }

    

    private void addReferenceToInstancesFile(final SerialOid oid, final NakedObjectSpecification noSpec) {
    	final Vector<SerialOid> instances = loadInstances(noSpec);
		instances.addElement(oid);
		writeInstanceFile(noSpec, instances);
    }


    //////////////////////////////////////////////////////////
    // remove
    //////////////////////////////////////////////////////////

    public final void remove(final SerialOid oid) throws ObjectNotFoundException, ObjectPersistenceException {
        final Data data = loadData(oid);
        removeReferenceFromInstancesFile(oid, data.getSpecification());
        deleteData(oid);
    }

    /**
     * Delete the data for an existing instance.
     */
    private void deleteData(final SerialOid oid) {
        xmlFile.delete(filename(oid));
    }


    private void removeReferenceFromInstancesFile(final SerialOid oid, final NakedObjectSpecification noSpec) {
		final Vector<SerialOid> instances = loadInstances(noSpec);
		instances.removeElement(oid);
		writeInstanceFile(noSpec, instances);
    }


    
    //////////////////////////////////////////////////////////
    // helpers (used by both add & remove)
    //////////////////////////////////////////////////////////

    private void writeInstanceFile(final NakedObjectSpecification noSpec, final Vector<SerialOid> instances) {
    	writeInstanceFile(noSpec.getFullName(), instances);
    }

    private void writeInstanceFile(final String name, final Vector<SerialOid> instances) {
        final StringBuffer data = new StringBuffer();
        data.append("<instances name=\"" + name + "\">\n");

        
        for (SerialOid elementAt: instances) {
			data.append("  <instance id=\"" + encodedOid(elementAt) + "\"/>\n");
        }

        data.append("</instances>");
        xmlFile.writeXml(name, data);
    }


    //////////////////////////////////////////////////////////
    // save
    //////////////////////////////////////////////////////////

    /**
     * Save the data for latter retrieval.
     */
    public final void save(final Data data) {
        writeInstanceToItsDataFile(data);
    }

    private void writeInstanceToItsDataFile(final Data data) {
    	final StringBuffer xml = new StringBuffer();
		final boolean isObject = data instanceof ObjectData;
		final String tag = isObject ? "naked-object" : "collection";
		xml.append("<" + tag);
		xml.append(attribute("type", data.getTypeName()));
		xml.append(attribute("id", "" + encodedOid(data.getOid())));
		xml.append(attribute("user", "" + NakedObjectsContext.getAuthenticationSession().getUserName()));
		
		final long sequence = data.getVersion().getSequence();
		final String sequenceString = Long.toHexString(sequence).toUpperCase();
		xml.append(attribute("ver", "" + sequenceString));
		
		xml.append(">\n");
		
		if (isObject) {
		    writeObject(data, xml);
		} else {
		    writeCollection(data, xml);
		}
		
		xml.append("</" + tag + ">\n");
		xmlFile.writeXml(filename(data.getOid()), xml);
    }


	private void writeObject(final Data data, final StringBuffer xml) {
		final ObjectData object = (ObjectData) data;
		for(final String field: object.fields()) {
		    writeField(xml, object, field);
		}
	}


	private void writeField(final StringBuffer xml, final ObjectData object,
			final String field) {
		final Object entry = object.get(field);

		if (entry instanceof SerialOid) {
		    writeAssociationField(xml, field, entry);
		} else if (entry instanceof ReferenceVector) {
		    writeMultipleAssociationField(xml, field, entry);
		} else {
		    writeValueField(xml, field, entry);
		}
	}

	private void writeAssociationField(final StringBuffer xml, final String field,
			final Object entry) {
		Assert.assertFalse(((SerialOid) entry).isTransient());
		xml.append("  <association field=\"" + field + "\" ");
		xml.append("ref=\"" + encodedOid((SerialOid) entry) + "\"/>\n");
	}

	private void writeMultipleAssociationField(final StringBuffer xml,
			final String field, final Object entry) {
		final ReferenceVector references = (ReferenceVector) entry;
		final int size = references.size();

		if (size > 0) {
		    xml.append("  <multiple-association field=\"" + field + "\" ");
		    xml.append(">\n");
		    for (int i = 0; i < size; i++) {
		        final Object oid = references.elementAt(i);
		        Assert.assertFalse("transient oid", oid, ((SerialOid) oid).isTransient());
		        xml.append("    <element ");
		        xml.append("ref=\"" + encodedOid((SerialOid) oid) + "\"/>\n");
		    }
		    xml.append("  </multiple-association>\n");
		}
	}

	private void writeValueField(final StringBuffer xml, final String field,
			final Object entry) {
		xml.append("  <value field=\"" + field + "\">");
		xml.append(XmlFile.getValueWithSpecialsEscaped(entry.toString()));
		xml.append("</value>\n");
	}


	private void writeCollection(final Data data, final StringBuffer xml) {
		final CollectionData collection = (CollectionData) data;
		final ReferenceVector refs = collection.references();
		for (int i = 0; i < refs.size(); i++) {
		    final Object oid = refs.elementAt(i);
		    xml.append("  <element ");
		    xml.append("ref=\"" + encodedOid((SerialOid) oid) + "\"/>\n");
		}
	}


    private String attribute(final String name, final String value) {
        return " " + name + "=\"" + value + "\"";
    }


    //////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////


    private String filename(final SerialOid oid) {
        return encodedOid(oid);
    }

    private String encodedOid(final SerialOid oid) {
    	return Long.toHexString(oid.getSerialNo()).toUpperCase();
    }
    

    //////////////////////////////////////////////////////////
    // Debugging
    //////////////////////////////////////////////////////////

    public String getDebugData() {
        return "Data directory " + xmlFile.getDirectory();
    }


}
// Copyright (c) Naked Objects Group Ltd.
