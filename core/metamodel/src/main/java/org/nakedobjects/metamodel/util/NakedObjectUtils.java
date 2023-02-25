package org.nakedobjects.metamodel.util;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;


public class NakedObjectUtils {

    private NakedObjectUtils() {}

    public static boolean exists(final NakedObject nakedObject) {
        return nakedObject != null && nakedObject.getObject() != null;
    }

    public static boolean wrappedEqual(final NakedObject nakedObject1, final NakedObject nakedObject2) {
        final boolean defined1 = exists(nakedObject1);
        final boolean defined2 = exists(nakedObject2);
        if (defined1 && !defined2) {
            return false;
        }
        if (!defined1 && defined2) {
            return false;
        }
        if (!defined1 && !defined2) {
            return true;
        } // both null
        return nakedObject1.getObject().equals(nakedObject2.getObject());
    }

    public static Object unwrap(final NakedObject adapter) {
        return adapter != null ? adapter.getObject() : null;
    }

    public static Object[] unwrap(final NakedObject[] adapters) {
        if (adapters == null) {
            return null;
        }
        final Object[] unwrappedObjects = new Object[adapters.length];
        int i = 0;
        for (final NakedObject adapter : adapters) {
            unwrappedObjects[i++] = unwrap(adapter);
        }
        return unwrappedObjects;
    }

	@SuppressWarnings("unchecked")
	public static <T> List<T> unwrap(List<NakedObject> adapters) {
		List<T> list = new ArrayList<T>();
		for(NakedObject adapter: adapters) {
			list.add((T) unwrap(adapter));
		}
		return list;
	}


    public static String titleString(final NakedObject adapter) {
        return adapter != null ? adapter.titleString() : "";
    }

    public static boolean nullSafeEquals(final Object obj1, final Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1.equals(obj2)) {
            return true;
        }
        if (obj1 instanceof NakedObject && obj2 instanceof NakedObject) {
            final NakedObject nakedObj1 = (NakedObject) obj1;
            final NakedObject nakedObj2 = (NakedObject) obj2;
            return nullSafeEquals(nakedObj1.getObject(), nakedObj2.getObject());
        }
        return false;
    }

    
}

// Copyright (c) Naked Objects Group Ltd.
