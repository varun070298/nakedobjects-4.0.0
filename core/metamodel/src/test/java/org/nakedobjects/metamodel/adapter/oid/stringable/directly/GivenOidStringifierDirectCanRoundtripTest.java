package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.oid.Oid;

public class GivenOidStringifierDirectCanRoundtripTest {
	
	private OidStringifierDirect oidStringifier;
	private DirectlyStringableOid directlyStringableOid;
	
	@Before
	public void setUp() {
		directlyStringableOid = new OidConformant();
		oidStringifier = new OidStringifierDirect(OidConformant.class);
	}
	
	@After
	public void tearDown() {
		directlyStringableOid = null;
		oidStringifier = null;
	}
	
	@Test
	public void withConformantOidClassThenCorrectlyInitialized() {
		String enString = oidStringifier.enString(directlyStringableOid);
		Oid deString = oidStringifier.deString(enString);
		assertThat(deString, is(not(nullValue())));
	}
	

}
