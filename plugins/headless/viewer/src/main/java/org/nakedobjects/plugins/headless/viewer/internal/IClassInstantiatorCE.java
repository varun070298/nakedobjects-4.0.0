package org.nakedobjects.plugins.headless.viewer.internal;

/**
 * Used to instantiate a given class.
 */
interface IClassInstantiatorCE {

    /**
     * Return a new instance of the specified class. The recommended way is without calling any constructor.
     * This is usually done by doing like <code>ObjectInputStream.readObject()</code> which is JVM specific.
     * 
     * @param c
     *            Class to instantiate
     * @return new instance of clazz
     */
    Object newInstance(Class<?> clazz) throws InstantiationException;
}
