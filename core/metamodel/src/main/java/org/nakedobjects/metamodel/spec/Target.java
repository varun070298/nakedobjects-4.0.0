package org.nakedobjects.metamodel.spec;

public class Target {
    String name;

    public Target(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Target) {
            final Target type = (Target) object;
            return name.equals(type.name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
