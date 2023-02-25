package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class SerialOidEncodabilityWhenPersistentWithPreviousTest extends SerialOidEncodabilityAbstractTest {

	protected Encodable createEncodable() {
		SerialOid oid = SerialOid.createTransient(123L);
		oid.setId(456L);
		oid.makePersistent();
		return oid;
	}

}
