package org.nakedobjects.runtime.authentication.standard;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jmock.integration.junit4.JMock;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.commons.encoding.EncodabilityContractTest;
import org.nakedobjects.metamodel.commons.encoding.Encodable;

@RunWith(JMock.class)
public class SimpleSessionEncodabilityNoRolesTest extends SimpleSessionEncodabilityTestAbstract {

	@Override
	protected Encodable createEncodable() {
		return new SimpleSession("joe", new String[]{});
	}

}


// Copyright (c) Naked Objects Group Ltd.
