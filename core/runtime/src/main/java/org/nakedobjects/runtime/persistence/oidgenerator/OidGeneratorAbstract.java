package org.nakedobjects.runtime.persistence.oidgenerator;

import org.nakedobjects.metamodel.adapter.oid.stringable.OidStringifier;
import org.nakedobjects.metamodel.adapter.oid.stringable.hex.OidStringifierHex;



public abstract class OidGeneratorAbstract implements OidGenerator {


    private final OidStringifier oidStringifier;
    
    /**
     * Defaults to using the {@link OidStringifierHex} for {@link #getOidStringifier()}.
     * Subclasses can replace by calling {@link #OidGeneratorAbstract(OidStringifier)} instead.
     */
    public OidGeneratorAbstract() {
    	this(new OidStringifierHex());
    }

    public OidGeneratorAbstract(final OidStringifier oidStringifier) {
    	this.oidStringifier = oidStringifier;
    }

	////////////////////////////////////////////////////////////////
    // open, close (session scoped)
    ////////////////////////////////////////////////////////////////

    /**
     * Default implementation does nothing.
     */
    public void open() {}
    
    /**
     * Default implementation does nothing.
     */
    public void close() {}

    
    /**
     * Default implemenation returns {@link OidStringifierHex}.
     * 
     * <p>
     * Subclasses can replace through constructor if required.
     */
    public final OidStringifier getOidStringifier() {
    	return oidStringifier;
    }
    
    // ////////////////////////////////////////////////////////////////////
    // injectInto
    // ////////////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (OidGeneratorAware.class.isAssignableFrom(candidate.getClass())) {
            OidGeneratorAware cast = OidGeneratorAware.class.cast(candidate);
            cast.setOidGenerator(this);
        }
    }



}
// Copyright (c) Naked Objects Group Ltd.
