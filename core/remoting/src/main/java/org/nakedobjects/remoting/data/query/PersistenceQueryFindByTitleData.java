package org.nakedobjects.remoting.data.query;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindByTitle;

public class PersistenceQueryFindByTitleData extends PersistenceQueryDataAbstract {
	
    private static final long serialVersionUID = 1L;
    
    private final String title;

    public PersistenceQueryFindByTitleData(
    		final NakedObjectSpecification noSpec, 
    		final String title) {
        super(noSpec);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getPersistenceQueryClass() {
        return PersistenceQueryFindByTitle.class;
    }
}

// Copyright (c) Naked Objects Group Ltd.
