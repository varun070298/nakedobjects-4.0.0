package org.nakedobjects.metamodel.spec;

import org.nakedobjects.metamodel.commons.lang.HashCodeUtils;


/**
 * A combination of a {@link NakedObjectSpecification} along with an object (possibly <tt>null</tt>) that
 * should be of the type represented by that object.
 * 
 * <p>
 * This class has value semantics.
 */
public class SpecObjectPair {

    private int hashCode;
    private boolean hashCodeCached = false;
    private final NakedObjectSpecification nakedObjectSpecification;
    private final Object object;

    public SpecObjectPair(final NakedObjectSpecification nakedObjectSpecification, final Object object) {
        this.nakedObjectSpecification = nakedObjectSpecification;
        this.object = object;
    }

    public NakedObjectSpecification getSpecification() {
        return nakedObjectSpecification;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (!(other.getClass() == SpecObjectPair.class)) {
            return false;
        }
        return equals((SpecObjectPair) other);
    }

    public boolean equals(final SpecObjectPair other) {
        if (other == null) {
            return false;
        }
        if (other.hashCode() != hashCode()) {
            return false;
        }
        return other.getSpecification() == getSpecification() && other.getObject() == getObject();
    }

    @Override
    public int hashCode() {
        if (!hashCodeCached) {
            hashCode = HashCodeUtils.SEED;
            hashCode = HashCodeUtils.hash(hashCode, getSpecification().getFullName());
            hashCode = HashCodeUtils.hash(hashCode, getObject());
            hashCodeCached = true;
        }
        return hashCode;
    }

}
