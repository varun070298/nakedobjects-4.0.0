package org.nakedobjects.runtime.authentication.standard;

import java.util.UUID;

public class RandomCodeGeneratorUUID implements RandomCodeGenerator {

    /**
     * Generates a random string in the form <tt>XXXX-XX-XX-XX-XXXXXX</tt> where X is a hexadecimal.
     * 
     * <p>
     * Implementation uses Java's own {@link UUID} class.
     * 
     * @see UUID#toString() for details on the formatting.
     */
    public String generateRandomCode() {
        return java.util.UUID.randomUUID().toString();
    }

}
