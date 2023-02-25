package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GivenOidStringifierDirectWhenInstantiateTest {
	
	@Test
	public void withConformantOidClassThenCorrectlyInitialized() {
		OidStringifierDirect oidStringifier = new OidStringifierDirect(OidConformant.class);
		assertThat(oidStringifier.getDestringMethod(), is(not(nullValue())));
		assertThat(oidStringifier.getDestringMethod().getName(), is("deString"));
		assertThat(oidStringifier.getOidClass(), is(not(nullValue())));
		assertEquals(OidConformant.class, oidStringifier.getOidClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void withOidClassWithNonPublicDestringMethodThenFails() {
		new OidStringifierDirect(OidWithNonPublicDestringMethod.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void withOidClassWithNonStaticDestringMethodThenFails() {
		new OidStringifierDirect(OidWithNonStaticDestringMethod.class);
	}

}
