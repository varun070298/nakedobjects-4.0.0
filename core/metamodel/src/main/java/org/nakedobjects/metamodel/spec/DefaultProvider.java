package org.nakedobjects.metamodel.spec;

public interface DefaultProvider {

    /**
     * Default value to be provided for properties or parameters that are not declared as <tt>@Optional</tt>
     * but where the UI has not (yet) provided a value.
     */
    Object getDefaultValue();

}
