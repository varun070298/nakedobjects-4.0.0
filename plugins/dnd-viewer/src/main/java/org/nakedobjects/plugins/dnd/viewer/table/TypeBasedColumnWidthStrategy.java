package org.nakedobjects.plugins.dnd.viewer.table;

import java.util.Hashtable;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public class TypeBasedColumnWidthStrategy implements ColumnWidthStrategy {
    private final Hashtable types = new Hashtable();

    public TypeBasedColumnWidthStrategy() {
    /*
     * NakedObjectSpecificationLoader loader = NakedObjects.getSpecificationLoader();
     * addWidth(loader.loadSpecification(Logical.class), 25); addWidth(loader.loadSpecification(Date.class),
     * 65); addWidth(loader.loadSpecification(Time.class), 38);
     * addWidth(loader.loadSpecification(DateTime.class), 100);
     * addWidth(loader.loadSpecification(TextString.class), 80);
     */
    }

    public void addWidth(final NakedObjectSpecification specification, final int width) {
        types.put(specification, new Integer(width));
    }

    public int getMaximumWidth(final int i, final NakedObjectAssociation specification) {
        return 0;
    }

    public int getMinimumWidth(final int i, final NakedObjectAssociation specification) {
        return 15;
    }

    // TODO improve the width determination
    public int getPreferredWidth(final int i, final NakedObjectAssociation specification) {
        final NakedObjectSpecification type = specification.getSpecification();
        if (type == null) {
            return 200;
        }
        final Integer t = (Integer) types.get(type);
        if (t != null) {
            return t.intValue();
        } else if (type.isObject()) {
            return 120;
        } else {
            return 100;
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
