package org.nakedobjects.runtime.testsystem;

import java.util.Date;

import org.nakedobjects.metamodel.adapter.version.Version;


public class TestProxyVersion implements Version {
    private static final long serialVersionUID = 1L;
    private final int value;

    public TestProxyVersion() {
        this(1);
    }

    public TestProxyVersion(final int value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof TestProxyVersion) {
            final TestProxyVersion other = (TestProxyVersion) obj;
            return other.value == value;
        }

        return false;
    }

    public boolean different(final Version version) {
        return value != ((TestProxyVersion) version).value;
    }

    @Override
    public String toString() {
        return "Version#" + value;
    }

    public String getUser() {
        return "USER";
    }

    public Date getTime() {
        return new Date(0);
    }

    public Version next() {
        return new TestProxyVersion(value + 1);
    }

    public String sequence() {
        return "" + value;
    }

}
// Copyright (c) Naked Objects Group Ltd.
