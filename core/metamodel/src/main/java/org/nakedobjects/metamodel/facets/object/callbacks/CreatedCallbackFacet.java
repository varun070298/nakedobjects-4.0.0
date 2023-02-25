package org.nakedobjects.metamodel.facets.object.callbacks;

/**
 * Represents the mechanism to inform the object that it has just been created.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, this is represented by a <tt>created</tt> method. The
 * framework calls this once the object has been created via <tt>newTransientInstance</tt> or
 * <tt>newInstance</tt>. The method is <i>not</i> called when the object is subsequently resolved having been
 * persisted; for that see {@link LoadingCallbackFacet} and {@link LoadedCallbackFacet}.
 * 
 * @see LoadingCallbackFacet
 * @see LoadedCallbackFacet
 */
public interface CreatedCallbackFacet extends CallbackFacet {

}

// Copyright (c) Naked Objects Group Ltd.
