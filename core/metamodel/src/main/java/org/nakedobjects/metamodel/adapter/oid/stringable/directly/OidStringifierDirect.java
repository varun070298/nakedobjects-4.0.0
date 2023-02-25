package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.oid.stringable.OidStringifier;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;

public class OidStringifierDirect implements OidStringifier {

	private final Class<? extends Oid> oidClass;
	private final Method deStringMethod;

	public OidStringifierDirect(Class<? extends Oid> oidClass) {
		Ensure.ensureThatArg(oidClass, is(not(nullValue(Class.class))));
		
		this.oidClass = oidClass;
		try {
			deStringMethod = oidClass.getMethod("deString", String.class);
		} catch (SecurityException ex) {
			throw new IllegalArgumentException("Trying to obtain 'deString(String)' method from  Oid class '" + oidClass.getName() + "'", ex);
		} catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("Trying to obtain 'deString(String)' method from  Oid class '" + oidClass.getName() + "'", ex);
		}
		if(!JavaClassUtils.isStatic(deStringMethod)) {
			throw new IllegalArgumentException("'deString(String)' method for Oid class '" + oidClass.getName() +
					"' must be static");
		}
		if(!JavaClassUtils.isPublic(deStringMethod)) {
			throw new IllegalArgumentException("'deString(String)' method for Oid class '" + oidClass.getName() +
			"' must be public");
		}
	}
	
    public String enString(final Oid oid) {
    	if (!(oid instanceof DirectlyStringableOid)) {
    		throw new IllegalArgumentException("Must be DirectlyStringableOid; oid class: " + oid.getClass().getName());
    	}
    	DirectlyStringableOid directlyStringableOid = (DirectlyStringableOid) oid;
    	return directlyStringableOid.enString();
    }

    public Oid deString(final String oidStr) {
    	try {
			return (Oid) deStringMethod.invoke(null, oidStr);
		} catch (IllegalAccessException ex) {
			throw new IllegalArgumentException("deString(String) method failed; ", ex);
		} catch (InvocationTargetException ex) {
			throw new IllegalArgumentException("deString(String) method failed; ", ex);
		}
    }

	public Class<? extends Oid> getOidClass() {
		return oidClass;
	}
	
	public Method getDestringMethod() {
		return deStringMethod;
	}

}
