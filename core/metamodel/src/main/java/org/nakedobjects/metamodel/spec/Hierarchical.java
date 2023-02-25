package org.nakedobjects.metamodel.spec;

public interface Hierarchical {

	/**
	 * Add the class for the specified specification as a subclass of this
	 * specification's class.
	 */
	void addSubclass(NakedObjectSpecification specification);

	/**
	 * Returns true if the <tt>subclasses()</tt> method will return an array of
	 * one or more elements (ie, not an empty array).
	 */
	boolean hasSubclasses();

	/**
	 * Get the list of specifications for all the interfaces that the class
	 * represented by this specification implements.
	 */
	NakedObjectSpecification[] interfaces();

	/**
	 * Determines if this specification represents the same specification, or a
	 * subclass, of the specified specification.
	 * 
	 * <p>
	 * <tt>subSpec.isOfType(superSpec)</tt> is equivalent to
	 * {@link Class#isAssignableFrom(Class) Java's}
	 * <tt>superType.isAssignableFrom(subType)</tt>.
	 */
	boolean isOfType(NakedObjectSpecification specification);

	/**
	 * Get the list of specifications for the subclasses of the class
	 * represented by this specification
	 */
	NakedObjectSpecification[] subclasses();

	/**
	 * Get the specification for this specification's class's superclass.
	 */
	NakedObjectSpecification superclass();

}
