package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import org.nakedobjects.metamodel.adapter.oid.Oid;

public class OidWithNonStaticDestringMethod extends DirectlyStringableOidAbstract implements DirectlyStringableOid {

	public Oid deString(String oidStr) {
		return new OidWithNonStaticDestringMethod();
	}
	
	public String enString() {
		return "CUS|1234567A";
	}
}
