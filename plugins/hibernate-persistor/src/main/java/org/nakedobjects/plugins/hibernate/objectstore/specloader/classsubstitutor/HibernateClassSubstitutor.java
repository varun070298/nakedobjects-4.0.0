package org.nakedobjects.plugins.hibernate.objectstore.specloader.classsubstitutor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.PersistentCollection;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorAbstract;


public class HibernateClassSubstitutor extends ClassSubstitutorAbstract {


    /**
     * Converts Hibernate's {@link PersistentCollection} classes to the correct collection type ( {@link List},
     * {@link Set} or {@link Map} ).
     * 
     * <p>
     * TODO: this approach prevents programmers from subclassing collection classes. We will therefore need to
     * revisit this area when we get around to adding actions to collection classes.
     */
    public Class<?> getClass(final Class<?> cls) {
        if (PersistentCollection.class.isAssignableFrom(cls)) {
            if (List.class.isAssignableFrom(cls)) {
                return List.class;
            }
            if (Set.class.isAssignableFrom(cls)) {
                return Set.class;
            }
            if (Map.class.isAssignableFrom(cls)) {
                return Map.class;
            }
        }
        // if (HibernateProxy.class.isAssignableFrom(cls)) {
        // return cls.getSuperclass();
        // }
        return cls;
    }

}
// Copyright (c) Naked Objects Group Ltd.
