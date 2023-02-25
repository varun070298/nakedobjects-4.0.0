package org.nakedobjects.runtime.persistence.adapterfactory;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.component.SessionScopedComponent;


/**
 * Polymorphic instantiation of {@link NakedObject}.
 * 
 * <p>
 * Introduced to allow subclasses of NakedObject with support for {@link OneToOneAssociationInstance} 
 * and so on.
 */
public interface AdapterFactory extends SessionScopedComponent, Injectable {

    public NakedObject createAdapter(final Object pojo, final Oid oid);
}
