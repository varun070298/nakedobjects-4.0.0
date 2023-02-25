package org.nakedobjects.metamodel.adapter.oid.stringable;

import org.nakedobjects.metamodel.adapter.oid.Oid;

public interface OidStringifier {

    public String enString(Oid oid);

    public Oid deString(String oidStr);
}
