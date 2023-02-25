package org.nakedobjects.metamodel.facets;

public abstract class EnumerationAbstract implements Enumeration {

    private final int num;
    private final String nameInCode;
    private final String friendlyName;

    protected EnumerationAbstract(final int num, final String nameInCode, final String friendlyName) {
        this.num = num;
        this.nameInCode = nameInCode;
        this.friendlyName = friendlyName;
    }

    public int getNum() {
        return num;
    }

    public String getNameInCode() {
        return nameInCode;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public int hashCode() {
        return getNum();
    }

    /**
     * Ensures same class (exactly) and same {@link #getNum() cardinal number}.
     */
    @Override
    public final boolean equals(final Object other) {
        return other.getClass() == this.getClass() && getNum() == ((EnumerationAbstract) other).getNum();
    }

    @Override
    public String toString() {
        return getNameInCode();
    }

}
