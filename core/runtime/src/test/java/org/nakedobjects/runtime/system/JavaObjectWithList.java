package org.nakedobjects.runtime.system;

import java.util.ArrayList;
import java.util.List;


public class JavaObjectWithList {
    boolean visible = false;
    boolean valid = false;
    boolean modifiable = false;
    JavaReferencedObject added;
    JavaReferencedObject removed;
    
    List<JavaReferencedObject> collection = new ArrayList<JavaReferencedObject>();

    public List<JavaReferencedObject> getMethod() {
        return collection;
    }

    public void addToMethod(final JavaReferencedObject person) {
        added = person;
    }

    public void removeFromMethod(final JavaReferencedObject person) {
        removed = person;
    }

    public static String nameMethod() {
        return "my name";
    }
    /*
     * public void aboutMethod(final FieldAbout about, final JavaReferencedObject object, final boolean isAdd)
     * { if (about.mode().getValue().equals(AboutType.VISIBLE.getValue())) { if (!visible) {
     * about.invisible(); } } else if (about.mode().getValue().equals(AboutType.AVAILABLE.getValue())) { if
     * (!modifiable) { about.unmodifiable("NO"); } } else if
     * (about.mode().getValue().equals(AboutType.VALID.getValue())) { if (!valid) { about.invalid(); } } }
     */
}
// Copyright (c) Naked Objects Group Ltd.
