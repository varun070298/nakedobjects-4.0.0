package org.nakedobjects.nof.reflect.remote.spec;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.reflect.peer.ValuePeer;


public class DummyValueAssociation implements NakedObjectField {

    private final ValuePeer fieldPeer;

    public DummyValueAssociation(final ValuePeer fieldPeer) {
        this.fieldPeer = fieldPeer;
    }
    
    public String getBusinessKeyName() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public Object getDefault(
            NakedObject nakedObject) {
        return null;
    }
    
    public void toDefault(NakedObject target) {
    }
        
    public Naked[] getOptions(final NakedObject target) {
        return null;
    }
    
    public boolean isCollection() {
        return false;
    }

    public boolean isPersisted() {
        return true;
    }

    public boolean isEmpty(final NakedObject adapter) {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isOptionEnabled() {
        return false;
    }
    
    public boolean isValue() {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }


    public Naked get(final NakedObject fromObject) {
        return null;
    }

    public Facet getFacet(final Class cls) {
        return null;
    }
  
     public Class[] getFacetTypes() {
        return null;
    }

	public Facet[] getFacets(Facet.Filter filter) {
		return null;
	}

	public void addFacet(Facet facet) {
	}

	public void removeFacet(Facet facet) {
	}

    public String getName() {
        return "Id";
    }

    public String getId() {
        return fieldPeer.getIdentifier().getName();
    }

    public String getDescription() {
        return null;
    }

    public boolean isVisible() {
        return isVisibleDeclaratively() && isVisibleForSession();
    }

    public boolean isVisibleDeclaratively() {
        return false;
    }

    public boolean isVisibleForSession() {
        return false;
    }

    public Consent isUsable() {
        return Allow.DEFAULT;
    }

    public Consent isUsableDeclaratively() {
        return Allow.DEFAULT;
    }

    public Consent isUsableForSession() {
        return Allow.DEFAULT;
    }

    public Consent isUsable(final NakedReference target) {
        return null;
    }

    public boolean isVisible(final NakedReference target) {
        return true;
    }

    public String getHelp() {
        return null;
    }

    public String debugData() {
        return "";
    }



}
// Copyright (c) Naked Objects Group Ltd.
