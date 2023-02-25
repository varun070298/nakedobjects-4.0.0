package org.nakedobjects.plugins.xml.objectstore.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectData;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class XmlCreateObjectCommand extends AbstractXmlPersistenceCommand implements CreateObjectCommand {
	private static final Logger LOG = Logger.getLogger(XmlDestroyObjectCommand.class);
	
	public XmlCreateObjectCommand(final NakedObject adapter, final DataManager dataManager) {
		super(adapter, dataManager);
	}

	public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("  create object " + onObject());
		}
	    final String user = getAuthenticationSession().getUserName();
	    onObject().setOptimisticLock(new FileVersion(user));
	    final ObjectData data = createObjectData(onObject(), true);
	    getDataManager().insertObject(data);
	}

	@Override
	public String toString() {
	    return "CreateObjectCommand [object=" + onObject() + "]";
	}
}