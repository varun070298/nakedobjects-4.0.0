package org.nakedobjects.metamodel.commons.component;

/**
 * Indicate that the implementing component is scoped at the transaction level (within a single session).
 * 
 * <p>
 * Unlike {@link ApplicationScopedComponent} and {@link SessionScopedComponent} there are not lifecycle
 * (initialization/shutdown) methods here.  That is, this is strictly a marker interface.
 * 
 * <p>
 * Analogous to Hibernate's <tt>Transaction</tt>.
 * 
 * @see ApplicationScopedComponent
 * @see SessionScopedComponent
 */
public interface TransactionScopedComponent extends Component {


}
// Copyright (c) Naked Objects Group Ltd.
