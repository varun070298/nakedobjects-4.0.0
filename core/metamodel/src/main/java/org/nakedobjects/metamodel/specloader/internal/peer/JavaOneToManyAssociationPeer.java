package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public class JavaOneToManyAssociationPeer extends JavaNakedObjectAssociationPeer implements NakedObjectAssociationPeer {

    public JavaOneToManyAssociationPeer(final Identifier name, SpecificationLoader specificationLoader) {
        super(name, null, true, specificationLoader);
    }

    /**
     * return the object type that the collection holds.
     */
    @Override
    public NakedObjectSpecification getSpecification() {
        return type == null ? getSpecificationLoader().loadSpecification(Object.class) : super.getSpecification();
    }

    @Override
    public String toString() {
        return "OneToManyAssociation [name=\"" + getIdentifier() + "\",type=" + getSpecification() + " ]";
    }

    public void setElementType(final Class<?> elementType) {
        setType(elementType);
    }

}
// Copyright (c) Naked Objects Group Ltd.
