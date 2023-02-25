package org.nakedobjects.applib.spec;

/**
 * An implementation of the <i>Specification</i> pattern, as described in
 * Eric Evans' <i>Domain Driven Design</i>, p224.
 */
public interface Specification {
    
    /**
     * If <tt>null</tt> then satisfied, otherwise is reason why the specification is
     * not satisfied.
     */
    public String satisfies(Object obj);
}
