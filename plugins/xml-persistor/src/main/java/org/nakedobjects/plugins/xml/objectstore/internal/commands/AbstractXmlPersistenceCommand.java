package org.nakedobjects.plugins.xml.objectstore.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectData;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.transaction.PersistenceCommandAbstract;

abstract class AbstractXmlPersistenceCommand extends PersistenceCommandAbstract {
	private static final Logger LOG = Logger.getLogger(AbstractXmlPersistenceCommand.class);
	
	private final DataManager dataManager;

	public AbstractXmlPersistenceCommand(final NakedObject adapter, final DataManager dataManager) {
		super(adapter);
		this.dataManager = dataManager;
	}

	protected DataManager getDataManager() {
		return dataManager;
	}

    protected ObjectData createObjectData(final NakedObject adapter, final boolean ensurePersistent) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("compiling object data for " + adapter);
    	}

        NakedObjectSpecification adapterSpec = adapter.getSpecification();
		ObjectData data = new ObjectData(adapterSpec, (SerialOid) adapter.getOid(), (FileVersion) adapter.getVersion());

        final NakedObjectAssociation[] associations = adapterSpec.getAssociations();
        for(NakedObjectAssociation association: associations) { 
			if (association.isDerived()) {
                continue;
            }

            final NakedObject associatedObject = association.get(adapter);
            final boolean isEmpty = association.isEmpty(adapter);
            final String associationId = association.getId();

            if (association.isOneToManyAssociation()) {
                saveCollection(associationId, data, associatedObject, ensurePersistent);
            } else if (association.getSpecification().isEncodeable()) {
                saveEncoded(data, associationId, associatedObject, isEmpty);
            } else if (association.isOneToOneAssociation()) {
                saveReference(data, associationId, associatedObject, ensurePersistent);
            }
        }

        return data;
    }

	private void saveReference(ObjectData data, final String associationId,
			final NakedObject associatedObject, final boolean ensurePersistent) {
		data.addAssociation(associatedObject, associationId, ensurePersistent);
	}

	private void saveCollection(final String associationId, ObjectData data,
			final NakedObject associatedObject, final boolean ensurePersistent) {
		data.addInternalCollection(associatedObject, associationId, ensurePersistent);
	}

	private void saveEncoded(ObjectData data, final String associationId,
			final NakedObject associatedObject, final boolean isEmpty) {
		if (associatedObject == null || isEmpty) {
		    data.saveValue(associationId, isEmpty, null);
		} else {
		    final EncodableFacet facet = associatedObject.getSpecification().getFacet(EncodableFacet.class);
		    final String encodedValue = facet.toEncodedString(associatedObject);
		    data.saveValue(associationId, isEmpty, encodedValue);
		}
	}
	

}