package org.nakedobjects.remoting.protocol.encoding.internal;

import java.util.Hashtable;
import java.util.Vector;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

/**
 * Caches a sorted version of the fields for a specified NakedObjectSpecification. This is used to counteract
 * any differences in field ordering that the specification might have across different tiers
 * 
 * <p>
 * TODO: shouldn't this responsibility simply move onto {@link NakedObjectSpecification} ? 
 */
public class FieldOrderCache {
    
    private final Hashtable cache = new Hashtable();

    public NakedObjectAssociation[] getFields(final NakedObjectSpecification specification) {
        NakedObjectAssociation[] fields = (NakedObjectAssociation[]) cache.get(specification);
        if (fields == null) {
            fields = loadFields(specification);
            cache.put(specification, fields);
        }
        return fields;
    }

    private NakedObjectAssociation[] loadFields(final NakedObjectSpecification specification) {
        final NakedObjectAssociation[] originalFields = specification.getAssociations();
        final Vector sorted = new Vector(originalFields.length);
        outer: for (int i = 0; i < originalFields.length; i++) {
            final String fieldId = originalFields[i].getId();

            for (int j = 0; j < sorted.size(); j++) {
                final NakedObjectAssociation sortedElement = (NakedObjectAssociation) sorted.elementAt(j);
                final String sortedFieldId = sortedElement.getId();
                if (sortedFieldId.compareTo(fieldId) > 0) {
                    sorted.insertElementAt(originalFields[i], j);
                    continue outer;
                }
            }
            sorted.addElement(originalFields[i]);
        }

        final NakedObjectAssociation[] fields = new NakedObjectAssociation[originalFields.length];
        sorted.copyInto(fields);

        return fields;
    }

}
// Copyright (c) Naked Objects Group Ltd.
