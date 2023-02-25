package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.spec.Specification;


public abstract class InstanceAbstract implements Instance {

    private final NakedObject owner;
    private Specification specification;
    

    protected InstanceAbstract() {
        this(null, null);
    }

    protected InstanceAbstract(final NakedObject owner) {
        this(owner, null);
    }

    protected InstanceAbstract(final NakedObject owner, final Specification specification) {
        this.owner = owner;
        this.specification = specification;
    }

    public final NakedObject getOwner() {
        return owner;
    }

    public Specification getSpecification() {
        if (specification == null) {
            specification = loadSpecification();
        }
        return specification;
    }

    /**
     * Allows the specification to be lazily loaded.
   */
    protected Specification loadSpecification() {
        return null;
    }

    /**
     * Allows subclasses to get specification without necessarily
     * triggering {@link #loadSpecification() loading} if not
     * yet known.
     */
    protected final Specification getSpecificationNoLoad() {
        return specification;
    }




}
