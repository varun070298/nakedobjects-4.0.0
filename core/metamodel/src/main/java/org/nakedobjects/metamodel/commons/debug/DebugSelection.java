package org.nakedobjects.metamodel.commons.debug;

/**
 * Provides a mechanism for providing a series of DebugInfo objects, keyed on name.
 */ 
public interface DebugSelection {

    DebugInfo debugSection(String sectionName);

    public String[] debugSectionNames();
    
//    DebugInfo[] debug();
}
// Copyright (c) Naked Objects Group Ltd.
