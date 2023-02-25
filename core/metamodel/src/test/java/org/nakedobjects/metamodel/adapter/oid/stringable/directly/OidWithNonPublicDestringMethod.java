package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import org.nakedobjects.metamodel.adapter.oid.Oid;

public class OidWithNonPublicDestringMethod extends DirectlyStringableOidAbstract implements DirectlyStringableOid {

	protected static Oid deString(String oidStr) {
		return new OidWithNonPublicDestringMethod();
	}
	
	public String enString() {
		return "CUS|1234567A";
	}
}
