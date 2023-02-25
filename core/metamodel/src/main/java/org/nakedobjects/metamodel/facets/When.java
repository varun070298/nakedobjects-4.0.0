package org.nakedobjects.metamodel.facets;

import org.nakedobjects.metamodel.adapter.NakedObject;


public final class When extends EnumerationAbstract {

    public static When ALWAYS = new When(0, "ALWAYS", "Always");
    public static When ONCE_PERSISTED = new When(1, "ONCE_PERSISTED", "Once Persisted");
    public static When UNTIL_PERSISTED = new When(1, "UNTIL_PERSISTED", "Until Persisted");
    public static When NEVER = new When(1, "NEVER", "Never");

    private When(final int num, final String nameInCode, final String friendlyName) {
        super(num, nameInCode, friendlyName);
    }

    /**
     * Whether the state of the supplied {@link NakedObject} corresponds to this 'when'.
     */
    public boolean isNowFor(final NakedObject targetAdapter) {
        final boolean isTransient = targetAdapter.isTransient();
        return this == When.ALWAYS || 
               this == When.ONCE_PERSISTED && !isTransient || 
               this == When.UNTIL_PERSISTED && isTransient;
    }

}
