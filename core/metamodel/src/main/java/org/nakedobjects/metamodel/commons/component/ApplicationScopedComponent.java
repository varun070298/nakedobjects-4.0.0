package org.nakedobjects.metamodel.commons.component;

/**
 * Indicate that the implementing component is scoped at application level (shared across multiple 
 * sessions) and might also require initialization or being shutdown.
 * 
 * <p>
 * Analogous to Hibernate's <tt>SessionFactory</tt>.
 * 
 * @see SessionScopedComponent
 * @see TransactionScopedComponent
 */
public interface ApplicationScopedComponent extends Component {

    /**
     * Indicates to the component that it is to initialise itself.
     */
    void init();

    /**
     * Indicates to the component that it will no longer be used and should shut itself down cleanly.
     */
    void shutdown();

}
// Copyright (c) Naked Objects Group Ltd.
