package org.nakedobjects.plugins.xml.objectstore.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.transaction.NakedObjectTransaction;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class XmlDestroyObjectCommand extends AbstractXmlPersistenceCommand implements DestroyObjectCommand {
	private static final Logger LOG = Logger.getLogger(XmlDestroyObjectCommand.class);
	
	public XmlDestroyObjectCommand(final NakedObject adapter, final DataManager dataManager) {
		super(adapter, dataManager);
	}

	public void execute(final NakedObjectTransaction context) throws ObjectPersistenceException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("  destroy object " + onObject());
		}
	    final SerialOid oid = (SerialOid) onObject().getOid();
	    getDataManager().remove(oid);
	    onObject().setOptimisticLock(null);
	}

	@Override
	public String toString() {
	    return "DestroyObjectCommand [object=" + onObject() + "]";
	}
}