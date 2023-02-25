package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class SerialOidEncodabilityWhenTransientTest extends SerialOidEncodabilityAbstractTest {

	protected Encodable createEncodable() {
		return SerialOid.createTransient(123L);
	}

}
