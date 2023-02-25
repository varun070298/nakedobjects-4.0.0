package org.nakedobjects.plugins.xml.objectstore.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.xml.objectstore.internal.data.Data;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class XmlUpdateObjectCommand 
		extends AbstractXmlPersistenceCommand 
		implements SaveObjectCommand {
	
	private static final Logger LOG = Logger.getLogger(XmlDestroyObjectCommand.class);

	public XmlUpdateObjectCommand(final NakedObject adapter, final DataManager dataManager) {
		super(adapter, dataManager);
	}

	public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("  save object " + onObject());
		}
	    final String user = getAuthenticationSession().getUserName();
	    onObject().setOptimisticLock(new FileVersion(user));

	    final Data data = createObjectData(onObject(), true);
	    getDataManager().save(data);
	}

	@Override
	public String toString() {
	    return "SaveObjectCommand [object=" + onObject() + "]";
	}

	

}