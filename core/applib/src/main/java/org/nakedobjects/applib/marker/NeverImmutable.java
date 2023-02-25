package org.nakedobjects.applib.marker;

import org.nakedobjects.applib.annotation.Immutable;


/**
 * Marker interface to show that an object can always be changed, even after persisted.
 * 
 * Use {@link Immutable} annotation in preference to this marker interface.
 */
public interface NeverImmutable {

}
// Copyright (c) Naked Objects Group Ltd.
