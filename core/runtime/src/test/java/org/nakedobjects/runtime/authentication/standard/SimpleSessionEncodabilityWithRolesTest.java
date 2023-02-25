package org.nakedobjects.runtime.authentication.standard;

import org.jmock.integration.junit4.JMock;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

@RunWith(JMock.class)
public class SimpleSessionEncodabilityWithRolesTest extends SimpleSessionEncodabilityTestAbstract {

	@Override
	protected Encodable createEncodable() {
		return new SimpleSession("joe", new String[]{"role1", "role2"});
	}

}


// Copyright (c) Naked Objects Group Ltd.
