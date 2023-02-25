package org.nakedobjects.metamodel.adapter;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class NakedObjectList extends AbstractList<NakedObject> {
    
    private final List<NakedObject> instances;
    private final NakedObjectSpecification instanceSpecification;

    public NakedObjectList(final NakedObjectSpecification instanceSpecification, final NakedObject[] instances) {
        this.instanceSpecification = instanceSpecification;
        this.instances = Collections.unmodifiableList(Arrays.asList(instances));
    }

    /**
     * Required implementation of {@link AbstractList}.
     */
    @Override
    public NakedObject get(int index) {
        return instances.get(index);
    }

    /**
     * Required implementation of {@link AbstractList}.
     */
    @Override
    public int size() {
        return instances.size();
    }


    /**
     * @deprecated - use {@link #iterator()}.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public Enumeration<NakedObject> elements() {
        return new IteratorEnumeration(iterator());
    }

    public NakedObjectSpecification getElementSpecification() {
        return instanceSpecification;
    }


    public String titleString() {
        return instanceSpecification.getPluralName() + ", " + size();
    }

    @Override
    public String toString() {
        final ToString s = new ToString(this);
        s.append("elements", instanceSpecification.getFullName());

        // title
        String title;
        try {
            title = "'" + this.titleString() + "'";
        } catch (final NullPointerException e) {
            title = "none";
        }
        s.append("title", title);

        s.append("vector", instances);

        return s.toString();
    }
    
}

// Copyright (c) Naked Objects Group Ltd.
