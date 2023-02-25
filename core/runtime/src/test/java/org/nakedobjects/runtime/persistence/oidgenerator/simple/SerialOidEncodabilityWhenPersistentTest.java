package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

public class SerialOidEncodabilityWhenPersistentTest extends SerialOidEncodabilityAbstractTest {

	protected Encodable createEncodable() {
		return SerialOid.createPersistent(456L);
	}

}
