package org.nakedobjects.runtime.authentication.standard;

public class RandomCodeGenerator10Chars implements RandomCodeGenerator {

    private static final int NUMBER_CHARACTERS = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public String generateRandomCode() {
        final StringBuilder buf = new StringBuilder(NUMBER_CHARACTERS);
        for (int i = 0; i < NUMBER_CHARACTERS; i++) {
            final int pos = (int) ((Math.random() * CHARACTERS.length()));
            buf.append(CHARACTERS.charAt(pos));
        }
        return buf.toString();
    }

}
