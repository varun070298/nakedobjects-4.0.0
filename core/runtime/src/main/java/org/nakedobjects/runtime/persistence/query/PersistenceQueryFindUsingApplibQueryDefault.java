package org.nakedobjects.runtime.persistence.query;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.applib.query.Query;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.services.container.query.QueryCardinality;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


/**
 * Corresponds to an object-store specific implementation of {@link Query}.
 */
public class PersistenceQueryFindUsingApplibQueryDefault extends PersistenceQueryAbstract {
	
	private final String queryName;
	private final QueryCardinality cardinality;
	private final Map<String, NakedObject> argumentsAdaptersByParameterName;

    public PersistenceQueryFindUsingApplibQueryDefault(
    		final NakedObjectSpecification specification, 
    		final String queryName, 
    		final Map<String, NakedObject> argumentsAdaptersByParameterName, 
    		final QueryCardinality cardinality) {
        super(specification);
        this.queryName = queryName;
        this.cardinality = cardinality;
        this.argumentsAdaptersByParameterName = argumentsAdaptersByParameterName;
        initialized();
    }
    
    public PersistenceQueryFindUsingApplibQueryDefault(DataInputExtended input)
			throws IOException {
		super(input);
		this.queryName = input.readUTF();
		this.cardinality = QueryCardinality.valueOf(input.readUTF());
		// TODO: need to read from input
		this.argumentsAdaptersByParameterName = new HashMap<String, NakedObject>();
		initialized();
	}

    
    @Override
    public void encode(DataOutputExtended output) throws IOException {
    	super.encode(output);
    	output.writeUTF(queryName);
    	output.writeUTF(cardinality.name());
		// TODO: need to write to output
    	// ... this.argumentsAdaptersByParameterName....
    	
    }

    private void initialized() {
    	// nothing to do
    }
    
    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////



	public String getQueryName() {
		return queryName;
	}
    
    public Map<String, NakedObject> getArgumentsAdaptersByParameterName() {
		return Collections.unmodifiableMap(argumentsAdaptersByParameterName);
	}
    
    public QueryCardinality getCardinality() {
		return cardinality;
	}
    
    @Override
    public String toString() {
        final ToString str = ToString.createAnonymous(this);
        str.append("spec", getSpecification().getShortName());
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
