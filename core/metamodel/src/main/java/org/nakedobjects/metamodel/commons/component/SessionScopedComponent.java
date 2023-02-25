package org.nakedobjects.metamodel.commons.component;

/**
 * Indicate that the implementing component is scoped at the session level (for a single user) and 
 * might also need to be "opened" or "closed" at the start/end of the session.
 * 
 * <p>
 * Analogous to Hibernate's <tt>Session</tt>.
 * 
 * @see ApplicationScopedComponent
 * @see TransactionScopedComponent
 */
public interface SessionScopedComponent extends Component {

    /**
     * Indicates to the component that it is to initialise itself as it will soon be receiving requests.
     */
    void open();

    /**
     * Indicates to the component that no more requests will be made of it and it can safely release any
     * services it has hold of.
     */
    void close();

}
// Copyright (c) Naked Objects Group Ltd.
