package org.nakedobjects.metamodel.config;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;

import org.nakedobjects.metamodel.commons.component.Injectable;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;


/**
 * (Mostly!) immutable set of properties representing the configuration of the running system.
 *
 * <p>
 * The {@link NakedObjectConfiguration} is one part of a mutable/immutable pair pattern
 * (cf {@link String} and {@link StringBuilder}).  What this means is, as components are 
 * loaded they can discover their own configuration resources.  These are added
 * to {@link ConfigurationBuilder}.
 * 
 * <p>
 * Thus the {@link NakedObjectConfiguration} held by different components may vary,
 * but with each being a possible superset of the previous.
 * 
 * <p>
 * TODO: we should make this strictly immutable by removing the {@link #add(String, String)} method.
 */
public interface NakedObjectConfiguration extends DebugInfo, Injectable {

    /**
     * Adds a name-value pair to the list of properties. If the named property already exists then the
     * specified value replaces the existing one.
     */
    void add(String name, String value);

    /**
     * Creates a new NakedObjectConfiguration containing the properties starting with the specified prefix.
     * The names of the new properties will have the prefixed stripped. This is similar to the
     * {@link #getProperties(String)} method, except the property names have their prefixes removed.
     * 
     * @see #getProperties(String)
     */
    NakedObjectConfiguration createSubset(String prefix);

    /**
     * Gets the boolean value for the specified name where no value or 'on' will result in true being
     * returned; anything gives false. If no boolean property is specified with this name then false is
     * returned.
     * 
     * @param name
     *            the property name
     */
    boolean getBoolean(String name);

    /**
     * Gets the boolean value for the specified name. If no property is specified with this name then the
     * specified default boolean value is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    boolean getBoolean(String name, boolean defaultValue);

    /**
     * Gets the color for the specified name. If no color property is specified with this name then null is
     * returned.
     * 
     * @param name
     *            the property name
     */
    Color getColor(String name);

    /**
     * Gets the color for the specified name. If no color property is specified with this name then the
     * specified default color is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    Color getColor(String name, Color defaultValue);

    /**
     * Gets the font for the specified name. If no font property is specified with this name then null is
     * returned.
     * 
     * @param name
     *            the property name
     */
    Font getFont(String name);

    /**
     * Gets the font for the specified name. If no font property is specified with this name then the
     * specified default font is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the color to use as a default
     */
    Font getFont(String name, Font defaultValue);

    /**
     * Returns a list of entries for the single configuration property with the specified name. 
     * 
     * <p>
     * If there is no matching property then returns an empty array.
     */
    String[] getList(String name);

    /**
     * Gets the number value for the specified name. If no property is specified with this name then 0 is
     * returned.
     * 
     * @param name
     *            the property name
     */
    int getInteger(String name);

    /**
     * Gets the number value for the specified name. If no property is specified with this name then the
     * specified default number value is returned.
     * 
     * @param name
     *            the property name
     * @param defaultValue
     *            the value to use as a default
     */
    int getInteger(String name, int defaultValue);

    /**
     * Creates a new NakedObjectConfiguration containing the properties starting with the specified prefix.
     * The names of the properties in the copy are the same as in the original, ie the prefix is not removed.
     * This is similar to the {@link #createSubset(String)} method except the names of the properties are not
     * altered when copied.
     * 
     * @see #createSubset(String)
     */
    NakedObjectConfiguration getProperties(String withPrefix);

    /**
     * Returns the configuration property with the specified name. If there is no matching property then null
     * is returned.
     */
    String getString(String name);

    String getString(String name, String defaultValue);

    boolean hasProperty(String name);

    boolean isEmpty();

    Enumeration<String> propertyNames();

    int size();

	/**
	 * The {@link ResourceStreamSource} that was used to build this configuration.
	 * 
	 * <p>
	 * This replaces the old <tt>rootPath()</tt> method.
	 * 
	 * @see ConfigurationBuilder#getResourceStreamSource()
	 */
	ResourceStreamSource getResourceStreamSource();
	

}
// Copyright (c) Naked Objects Group Ltd.
