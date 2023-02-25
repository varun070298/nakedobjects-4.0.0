package org.nakedobjects.metamodel.commons.lang;

import junit.framework.TestCase;

import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;


public class NameConvertorTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(NameConvertorTest.class);
    }

    public void testNaturalNameAddsSpacesToCamelCaseWords() {
        assertEquals("Camel Case Word", NameConvertorUtils.naturalName("CamelCaseWord"));
    }

    public void testNaturalNameAddsSpacesBeforeNumbers() {
        assertEquals("One 2 One", NameConvertorUtils.naturalName("One2One"));
        assertEquals("Type 123", NameConvertorUtils.naturalName("Type123"));
        assertEquals("4321 Go", NameConvertorUtils.naturalName("4321Go"));
    }

    public void testNaturalNameRecognisesAcronymns() {
        assertEquals("TNT Power", NameConvertorUtils.naturalName("TNTPower"));
        assertEquals("Spam RAM Can", NameConvertorUtils.naturalName("SpamRAMCan"));
        assertEquals("DOB", NameConvertorUtils.naturalName("DOB"));
    }

    public void testNaturalNameWithShortNames() {
        assertEquals("At", NameConvertorUtils.naturalName("At"));
        assertEquals("I", NameConvertorUtils.naturalName("I"));
    }

    public void testNaturalNameNoChange() {
        assertEquals("Camel Case Word", NameConvertorUtils.naturalName("CamelCaseWord"));
        assertEquals("Almost Normal english sentence", NameConvertorUtils.naturalName("Almost Normal english sentence"));
    }

    public void testPluralNameAdd_S() {
        assertEquals("Cans", NameConvertorUtils.pluralName("Can"));
        assertEquals("Spaces", NameConvertorUtils.pluralName("Space"));
        assertEquals("Noses", NameConvertorUtils.pluralName("Nose"));
    }

    public void testPluralNameReplace_Y_With_IES() {
        assertEquals("Babies", NameConvertorUtils.pluralName("Baby"));
        assertEquals("Cities", NameConvertorUtils.pluralName("City"));
    }

    public void testPluralNameReplaceAdd_ES() {
        assertEquals("Foxes", NameConvertorUtils.pluralName("Fox"));
        assertEquals("Bosses", NameConvertorUtils.pluralName("Boss"));
    }

    public void testSimpleNameAllToLowerCase() {
        assertEquals("abcde", NameConvertorUtils.simpleName("ABCDE"));
        assertEquals("camelcaseword", NameConvertorUtils.simpleName("CamelCaseWord"));
    }

    public void testSimpleNameNoChanges() {
        assertEquals("nochanges", NameConvertorUtils.simpleName("nochanges"));
    }

    public void testSimpleNameRemoveSpaces() {
        assertEquals("abcde", NameConvertorUtils.simpleName("a bc  de "));
        assertEquals("twoparts", NameConvertorUtils.simpleName("two parts"));
    }

}
// Copyright (c) Naked Objects Group Ltd.
