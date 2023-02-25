package org.nakedobjects.metamodel.facets.naming;

import java.util.Comparator;

import org.nakedobjects.metamodel.spec.NamedAndDescribed;


/**
 * Compares {@link NamedAndDescribed}s by name.
 */
public class NamedAndDescribedNameComparator implements Comparator {

    public int compare(final Object o1, final Object o2) {
        return compare((NamedAndDescribed) o1, (NamedAndDescribed) o2);
    }

    public int compare(final NamedAndDescribed o1, final NamedAndDescribed o2) {

        final String name1 = o1.getName();
        final String name2 = o2.getName();

        if (name1 == null && name2 == null) {
            return 0;
        }
        if (name1 == null && name2 != null) {
            return -1;
        }
        if (name1 != null && name2 == null) {
            return +1;
        }

        return name1.compareTo(name2);
    }

}
