package org.nakedobjects.runtime.persistence.query;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.services.container.query.QueryFindByTitle;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Corresponds to {@link QueryFindByTitle}.
 */
public class PersistenceQueryFindByTitle extends PersistenceQueryBuiltInAbstract {
    private final String title;

    public PersistenceQueryFindByTitle(final NakedObjectSpecification specification, final String title) {
        super(specification);
        this.title = title == null ? "" : title.toLowerCase();
    }

    public String getTitle() {
        return title;
    }

    public boolean matches(final NakedObject object) {
        final String titleString = object.titleString();
        return matches(titleString);
    }

    public boolean matches(final String titleString) {
        final String objectTitle = titleString.toLowerCase();
        return objectTitle.indexOf(title) >= 0;
    }

    @Override
    public String toString() {
        final ToString str = ToString.createAnonymous(this);
        str.append("spec", getSpecification().getShortName());
        str.append("title", title);
        return str.toString();
    }

}
// Copyright (c) Naked Objects Group Ltd.
