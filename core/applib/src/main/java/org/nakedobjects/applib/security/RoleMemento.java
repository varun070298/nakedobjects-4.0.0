package org.nakedobjects.applib.security;

import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.NotPersistable;

@NotPersistable
public final class RoleMemento {
	

    /**
     * Creates a new role with the specified name. Description is left blank.
     */
    public RoleMemento(final String name) {
        this(name, "");
    }

    /**
     * Creates a new role with the specified name and description.
     */
    public RoleMemento(final String name, final String description) {
        if (name == null) {
            throw new IllegalArgumentException("Name not specified");
        }
        this.name = name;
        if (description == null) {
            throw new IllegalArgumentException("Description not specified");
        }
        this.description = description;
    }

    
    // {{ Identification
    public String title() {
        return name;
    }
    // }}

    // {{ Name
    private final String name;
    @MemberOrder(sequence="1.1")
    public String getName() {
        return name;
    }
    // }}

    // {{ Description
    private final String description;
    @MemberOrder(sequence="1.2")
    public String getDescription() {
        return description;
    }
    // }}
}
// Copyright (c) Naked Objects Group Ltd.
