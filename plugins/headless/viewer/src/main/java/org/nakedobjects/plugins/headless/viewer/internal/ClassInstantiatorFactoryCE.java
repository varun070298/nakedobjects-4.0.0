package org.nakedobjects.plugins.headless.viewer.internal;

/**
 * Factory returning a {@link IClassInstantiatorCE}for the current JVM
 */
class ClassInstantiatorFactoryCE {

    private static IClassInstantiatorCE instantiator = new ObjenesisClassInstantiatorCE();

    // ///CLOVER:OFF
    private ClassInstantiatorFactoryCE() {}
    // ///CLOVER:ON

    /**
     * Returns the current JVM as specified in the Systtem properties
     * 
     * @return current JVM
     */
    public static String getJVM() {
        return System.getProperty("java.vm.vendor");
    }

    /**
     * Returns the current JVM specification version (1.5, 1.4, 1.3)
     * 
     * @return current JVM specification version
     */
    public static String getJVMSpecificationVersion() {
        return System.getProperty("java.specification.version");
    }

    public static boolean is1_3Specifications() {
        return getJVMSpecificationVersion().equals("1.3");
    }

    /**
     * Returns a class instantiator suitable for the current JVM
     * 
     * @return a class instantiator usable on the current JVM
     */
    public static IClassInstantiatorCE getInstantiator() {
        return instantiator;
    }
}
