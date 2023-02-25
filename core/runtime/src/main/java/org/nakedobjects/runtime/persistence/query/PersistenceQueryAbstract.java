package org.nakedobjects.runtime.persistence.query;

import java.io.IOException;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public abstract class PersistenceQueryAbstract implements PersistenceQuery, Encodable {
	
    private final NakedObjectSpecification specification;

    public PersistenceQueryAbstract(
    		final NakedObjectSpecification specification) {
    	this.specification = specification;
    	initialized();
    }

    protected PersistenceQueryAbstract(DataInputExtended input) throws IOException {
    	String specName = input.readUTF();
        specification = getSpecificationLoader().loadSpecification(specName);
        initialized();
    }

    public void encode(DataOutputExtended output)
    		throws IOException {
    	output.writeUTF(specification.getFullName());
    }
    
    
    private void initialized() {
    	// nothing to do
    }
    
    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////


    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    
    /////////////////////////////////////////////////////////
    // equals, hashCode
    /////////////////////////////////////////////////////////

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersistenceQueryAbstract other = (PersistenceQueryAbstract) obj;
        if (specification == null) {
            if (other.specification != null) {
                return false;
            }
        } else if (!specification.equals(other.specification)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + 1231;
        result = PRIME * result + ((specification == null) ? 0 : specification.hashCode());
        return result;
    }

    /////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////

	protected static SpecificationLoader getSpecificationLoader() {
		return NakedObjectsContext.getSpecificationLoader();
	}


}

// Copyright (c) Naked Objects Group Ltd.
