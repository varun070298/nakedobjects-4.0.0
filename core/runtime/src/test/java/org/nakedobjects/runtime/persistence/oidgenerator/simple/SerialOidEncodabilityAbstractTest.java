package org.nakedobjects.runtime.persistence.oidgenerator.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;

public abstract class SerialOidEncodabilityAbstractTest extends EncodabilityContractTest {

	@Override
	protected void assertRoundtripped(
			Object decodedEncodable,
			Object originalEncodable) {
		SerialOid decoded = (SerialOid) decodedEncodable;
		SerialOid original = (SerialOid) originalEncodable;
		
		assertThat(decoded.equals(original), is(true));
		assertThat(decoded.hasPrevious(), is(original.hasPrevious()));
		assertThat(decoded.getPrevious(), is(original.getPrevious()));
	}
}
