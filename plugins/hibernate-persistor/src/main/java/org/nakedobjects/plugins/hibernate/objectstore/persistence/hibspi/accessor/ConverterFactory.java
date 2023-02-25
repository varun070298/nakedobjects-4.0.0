package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import java.util.HashMap;

import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


/**
 * Factory to return a {@link PropertyConverter} for conversion between a specific NakedObjects value holder type and
 * standard Java data type.
 * 
 * <p>
 * Additional Converters can be added by calling {@link #add(Class, PropertyConverter)}.
 * 
 * <p>
 * Note that this is a {@link #getInstance() singleton.}
 * 
 * <p>
 * TODO: find some way to hook into <tt>@Value</tt> support.
 */
public class ConverterFactory {
    
    private final HashMap<String, PropertyConverter> converters = new HashMap<String, PropertyConverter>();
    
    private static ConverterFactory instance = new ConverterFactory();

    private ConverterFactory() {
    }

    /**
     * Return a {@link PropertyConverter} which converts from a value holder to a persistent class.
     * 
     * @param nakedClass
     *            Class of the value holder to convert from eg WholeNumber.class
     */
    public PropertyConverter getConverter(final Class<?> nakedClass) {
        return getConverter(nakedClass.getName());
    }

    /**
     * Return a PropertyConverter which converts from a value holder to a persistent class.
     * 
     * @param nakedClass
     *            name of the value holder to convert from
     * @return the PropertyConverter, or null if none is registered
     */
    public PropertyConverter getConverter(final String nakedClass) {
        return (PropertyConverter) converters.get(nakedClass);
    }

    public PropertyConverter getConverter(final NakedObjectAssociation field) {
        return getConverter(field.getSpecification().getFullName());
    }

    /**
     * Add a converter for the specified value type
     */
    public void add(final Class<?> valueType, final PropertyConverter converter) {
        if (!converters.containsKey(valueType.getName())) {
            converters.put(valueType.getName(), converter);
        }
    }

    /**
     * Return the singleton ConverterFactory
     */
    public static ConverterFactory getInstance() {
        return instance;
    }
}
// Copyright (c) Naked Objects Group Ltd.
