package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import org.nakedobjects.metamodel.adapter.oid.Oid;

public class OidConformant extends DirectlyStringableOidAbstract {

	public static Oid deString(String oidStr) {
		return new OidConformant();
	}
	
	public String enString() {
		return "CUS|1234567A";
	}
}
