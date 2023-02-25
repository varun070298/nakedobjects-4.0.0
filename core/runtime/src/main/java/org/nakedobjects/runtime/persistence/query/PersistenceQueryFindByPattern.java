package org.nakedobjects.runtime.persistence.query;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.services.container.query.QueryFindByPattern;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

/**
 * Corresponds to {@link QueryFindByPattern}.
 */
public class PersistenceQueryFindByPattern extends PersistenceQueryBuiltInAbstract {
    private final NakedObject pattern;

    public NakedObject getPattern() {
        return pattern;
    }

    public PersistenceQueryFindByPattern(
            final NakedObjectSpecification specification,
            final NakedObject pattern) {
        super(specification);
        this.pattern = pattern;
    }

    public boolean matches(final NakedObject object) {
        final NakedObjectSpecification requiredType = pattern.getSpecification();
        return requiredType.equals(object.getSpecification()) && matchesPattern(pattern, object);
    }

    private boolean matchesPattern(final NakedObject pattern, final NakedObject instance) {
        final NakedObject object = instance;
        final NakedObjectSpecification nc = object.getSpecification();
        final NakedObjectAssociation[] fields = nc.getAssociations();

        for (int f = 0; f < fields.length; f++) {
            final NakedObjectAssociation fld = fields[f];

            // are ignoring internal collections - these probably should be considered
            // ignore non-persistent fields - there is no persisted field to compare against
            if (fld.isDerived()) {
                continue;
            }
            if (fld.isOneToOneAssociation()) {
                if (fld.getSpecification().isCollectionOrIsAggregated()) {
                    // if pattern contains empty value then it matches anything
                    if (fld.isEmpty(pattern)) {
                        continue;
                    }

                    // find the objects
                    final NakedObject reqd = fld.get(pattern);
                    final NakedObject search = fld.get(object);
                    
                    // compare the titles
                    final String r = reqd.titleString().toLowerCase();
                    final String s = search.titleString().toLowerCase();

                    // if the pattern occurs in the object
                    if (s.indexOf(r) == -1) {
                        return false;
                    }
                } else if (fld.isOneToOneAssociation()) {
                    // find the objects
                    final NakedObject reqd = fld.get(pattern);

                    // if pattern contains null reference then it matches anything
                    if (reqd == null) {
                        continue;
                    }

                    final NakedObject search = fld.get(object);

                    // otherwise there must be a reference, else they can never
                    // match
                    if (search == null) {
                        return false;
                    }

                    if (reqd != search) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
// Copyright (c) Naked Objects Group Ltd.
