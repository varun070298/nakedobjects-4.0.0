package org.nakedobjects.applib;

/**
 * For use by repository implementations to allow a set of objects returned by a back-end objectstore to be
 * filtered before being returned to the caller.
 * 
 * <p>
 * Note that this is different from the pattern or criteria object accepted by some repositories'
 * <tt>findXxx</tt> methods. Such criteria objects are implementation-specific to the configured objectstore
 * and allow it to return an already-filtered set of rows. (For example, a Hibernate-based ObjectStore would
 * accept a representation of a HQL query; an XML-based objectstore might accept an XPath query, etc.)
 */
public interface Filter<T> {
	
    /**
     * Whether or not the supplied pojo meets this criteria.
     * 
     * @param pojo
     * @return <tt>true</tt> if this pojo is acceptable, <tt>false</tt> otherwise.
     */
    public boolean accept(T pojo);
}
