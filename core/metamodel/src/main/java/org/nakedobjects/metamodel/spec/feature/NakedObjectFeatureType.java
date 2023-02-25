package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Enumerates the features that a particular annotation can be applied to.
 * 
 * <p>
 * Modelled after Java 5 <tt>ElementType</tt>.
 * 
 * 
 * <p>
 * TODO: should rationalize this and {@link NakedObjectSpecification#getResultType()}. Note though that we don't
 * distinguish value properties and reference properties (and we probably shouldn't in
 * {@link NakedObjectSpecification}, either).
 */
public final class NakedObjectFeatureType {

    public final static NakedObjectFeatureType OBJECT = new NakedObjectFeatureType(0, "Object");
    public final static NakedObjectFeatureType PROPERTY = new NakedObjectFeatureType(1, "Property");
    public final static NakedObjectFeatureType COLLECTION = new NakedObjectFeatureType(2, "Collection");
    public final static NakedObjectFeatureType ACTION = new NakedObjectFeatureType(3, "Action");
    public final static NakedObjectFeatureType ACTION_PARAMETER = new NakedObjectFeatureType(4, "Parameter");

    public final static NakedObjectFeatureType[] COLLECTIONS_ONLY = new NakedObjectFeatureType[] { COLLECTION };
    public final static NakedObjectFeatureType[] ACTIONS_ONLY = new NakedObjectFeatureType[] { ACTION };
    public final static NakedObjectFeatureType[] PARAMETERS_ONLY = new NakedObjectFeatureType[] { ACTION_PARAMETER };
    public final static NakedObjectFeatureType[] ACTIONS_AND_PARAMETERS = new NakedObjectFeatureType[] { ACTION, ACTION_PARAMETER };
    public final static NakedObjectFeatureType[] COLLECTIONS_AND_ACTIONS = new NakedObjectFeatureType[] { COLLECTION, ACTION };
    public final static NakedObjectFeatureType[] PROPERTIES_AND_PARAMETERS = new NakedObjectFeatureType[] { PROPERTY,
            ACTION_PARAMETER };
    public final static NakedObjectFeatureType[] OBJECTS_PROPERTIES_AND_PARAMETERS = new NakedObjectFeatureType[] { OBJECT,
            PROPERTY, ACTION_PARAMETER };
    public final static NakedObjectFeatureType[] OBJECTS_AND_PROPERTIES = new NakedObjectFeatureType[] { OBJECT, PROPERTY };
    public final static NakedObjectFeatureType[] PROPERTIES_ONLY = new NakedObjectFeatureType[] { PROPERTY };
    public final static NakedObjectFeatureType[] OBJECTS_ONLY = new NakedObjectFeatureType[] { OBJECT };
    public final static NakedObjectFeatureType[] OBJECTS_PROPERTIES_AND_COLLECTIONS = new NakedObjectFeatureType[] { OBJECT,
            PROPERTY, COLLECTION };
    public final static NakedObjectFeatureType[] PROPERTIES_AND_COLLECTIONS = new NakedObjectFeatureType[] { PROPERTY, COLLECTION };
    public final static NakedObjectFeatureType[] PROPERTIES_COLLECTIONS_AND_ACTIONS = new NakedObjectFeatureType[] { PROPERTY,
            COLLECTION, ACTION };
    public final static NakedObjectFeatureType[] EVERYTHING_BUT_PARAMETERS = new NakedObjectFeatureType[] { OBJECT, PROPERTY,
            COLLECTION, ACTION };
    public final static NakedObjectFeatureType[] EVERYTHING = new NakedObjectFeatureType[] { OBJECT, PROPERTY, COLLECTION,
            ACTION, ACTION_PARAMETER };

    private final int num;
    private final String name;

    private NakedObjectFeatureType(final int num, final String nameInCode) {
        this.num = num;
        this.name = nameInCode;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
