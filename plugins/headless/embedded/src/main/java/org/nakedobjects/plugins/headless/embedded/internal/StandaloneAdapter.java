package org.nakedobjects.plugins.headless.embedded.internal;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectMM;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Specification;

/**
 * Only provides a concrete implementation of the methods corresponding to 
 * the {@link NakedObjectMM} interface.
 */
public class StandaloneAdapter implements NakedObject {

	private final NakedObjectSpecification spec;
	private final PersistenceState persistenceState;
	private Object domainObject;

    private TypeOfFacet typeOfFacet;

	public StandaloneAdapter(NakedObjectSpecification spec, Object domainObject, PersistenceState persistenceState) {
		this.spec = spec;
		this.domainObject = domainObject;
		this.persistenceState = persistenceState;
	}

	/**
	 * Returns the {@link NakedObjectSpecification} as provided in the constructor.
	 */
	public NakedObjectSpecification getSpecification() {
		return spec;
	}

	/**
	 * Returns the domain object as provided in the constructor.
	 */
	public Object getObject() {
		return domainObject;
	}
	
	/**
	 * Replaces the {@link #getObject() domain object}.
	 */
	public void replacePojo(Object pojo) {
		this.domainObject = pojo;
	}

	/**
	 * Whether the object is persisted.
	 * 
	 * <p>
	 * As per the {@link PersistenceState} provided in the constructor.
	 */
	public boolean isPersistent() {
		return persistenceState.isPersistent();
	}

	/**
	 * Whether the object is not persisted.
	 * 
	 * <p>
	 * As per the {@link PersistenceState} provided in the constructor.
	 */
	public boolean isTransient() {
		return persistenceState.isTransient();
	}

	/**
	 * Always returns <tt>null</tt>.
	 */
	public NakedObject getOwner() {
		return null;
	}

	public String titleString() {
		final NakedObjectSpecification specification = getSpecification();
        if (specification.isCollection()) {
            return "A collection of " + (" " + specification.getPluralName()).toLowerCase();
        } 
        
        String title = specification.getTitle(this);
        if (title != null) {
        	return title;
        }
        return "A " + specification.getSingularName().toLowerCase();
	}


	/**
	 * Returns the {@link #setTypeOfFacet(TypeOfFacet) overridden} {@link TypeOfFacet}, else
	 * looks up from {@link #getSpecification() specification} (if any).
	 */
	public TypeOfFacet getTypeOfFacet() {
        if (typeOfFacet == null) {
            return getSpecification().getFacet(TypeOfFacet.class);
        }
        return typeOfFacet;
	}
	
	/**
	 * Override (or set the) {@link TypeOfFacet}.
	 */
	public void setTypeOfFacet(TypeOfFacet typeOfFacet) {
        this.typeOfFacet = typeOfFacet;
	}



	///////////////////////////////////////////////////////////
	// Methods specified to NakedObject (as opposed to 
	// NakedObjectMM) do not need to be implemented.
	///////////////////////////////////////////////////////////
	
	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public ResolveState getResolveState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void changeState(ResolveState newState) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Oid getOid() {
		return null;
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public boolean isAggregated() {
		return false;
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public String getIconName() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Instance getInstance(Specification specification) {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public Version getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void checkLock(Version version) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void setOptimisticLock(Version version) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not supported, always throws an exception.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public void fireChangedEvent() {
		throw new UnsupportedOperationException();
	}

}
