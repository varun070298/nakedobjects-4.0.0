package org.nakedobjects.metamodel.specloader.internal.peer;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Additional reflective details about field members.
 * 
 * @see org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer
 */
public interface NakedObjectAssociationPeer extends NakedObjectMemberPeer {

    /**
     * The {@link NakedObjectSpecification specification} of the associated object if {@link #isOneToOne()} is
     * <tt>true</tt>, or, the type of the associated object (rather than a <tt>Vector.class</tt>, say),
     * if {@link #isOneToMany()} is <tt>true</tt>.
     */
    NakedObjectSpecification getSpecification();

    /**
     * If this is a scalar association, representing (in old terminology) a reference to another entity or a
     * value.
     * 
     * <p>
     * Opposite of {@link #isOneToMany()}.
     */
    public boolean isOneToOne();

    /**
     * If this is a collection.
     * 
     * <p>
     * Opposite of {@link #isOneToOne()}.
     */
    public boolean isOneToMany();

}
// Copyright (c) Naked Objects Group Ltd.
