package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class JavaNakedObjectAssociationPeer extends JavaNakedObjectMemberPeer implements NakedObjectAssociationPeer {

    private final boolean oneToMany;
    private final SpecificationLoader specificationLoader;
    protected Class<?> type;

    public JavaNakedObjectAssociationPeer(final Identifier identifier, final Class<?> returnType, final boolean oneToMany, SpecificationLoader specificationLoader) {
        super(identifier);
        type = returnType;
        this.oneToMany = oneToMany;
        this.specificationLoader = specificationLoader;
    }

    /**
     * return the object type, as a Class object, that the method returns.
     */
    public NakedObjectSpecification getSpecification() {
        return type == null ? null : getSpecificationLoader().loadSpecification(type);
    }

    public void setType(final Class<?> type) {
        this.type = type;
    }

    public final boolean isOneToMany() {
        return oneToMany;
    }

    public final boolean isOneToOne() {
        return !isOneToMany();
    }

    //////////////////////////////////////////////////////////////////////
    // Dependencies
    //////////////////////////////////////////////////////////////////////
    
    protected SpecificationLoader getSpecificationLoader() {
        return specificationLoader;
    }


}
// Copyright (c) Naked Objects Group Ltd.
