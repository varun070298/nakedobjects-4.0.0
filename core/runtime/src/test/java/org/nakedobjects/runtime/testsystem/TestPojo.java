package org.nakedobjects.runtime.testsystem;

public class TestPojo {
    private static int nextId;
    private final int id = nextId++;
    private final String state = "pojo" + id;

    @Override
    public String toString() {
        return "Pojo#" + id;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj.getClass() == getClass()) {
            final TestPojo other = (TestPojo) obj;
            return other.state.equals(state);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

}

// Copyright (c) Naked Objects Group Ltd.
