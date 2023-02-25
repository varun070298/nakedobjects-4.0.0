package org.nakedobjects.runtime.persistence.query;

import java.io.IOException;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Corresponds to an object-store specific implementation of {@link Query}.
 */
public class PersistenceQueryFindUsingApplibQuerySerializable extends PersistenceQueryAbstract {
	
    private final Query<?> query;
	private QueryCardinality cardinality;

    public PersistenceQueryFindUsingApplibQuerySerializable(
    		final NakedObjectSpecification specification, 
    		final Query<?> query, 
    		final QueryCardinality cardinality) {
        super(specification);
        this.query = query;
        this.cardinality = cardinality;
        initialized();
    }

    public PersistenceQueryFindUsingApplibQuerySerializable(
			DataInputExtended input) throws IOException {
		super(input);
        this.query = input.readSerializable(Query.class);
        this.cardinality = QueryCardinality.valueOf(input.readUTF());
        initialized();
	}


    @Override
    public void encode(DataOutputExtended output) throws IOException {
    	super.encode(output);
    	output.writeSerializable(query);
    	output.writeUTF(cardinality.name());
    }

	private void initialized() {
    	// nothing to do
    }
    
    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    
    public Query getApplibQuery() {
        return query;
    }
    
    public QueryCardinality getCardinality() {
		return cardinality;
	}
    
    @Override
    public String toString() {
        final ToString str = ToString.createAnonymous(this);
        str.append("spec", getSpecification().getShortName());
        str.append("query", query.getDescription());
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
