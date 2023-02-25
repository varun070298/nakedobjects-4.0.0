package org.nakedobjects.plugins.headless.viewer.internal.util;

import java.util.Arrays;
import java.util.LinkedHashSet;



public final class MethodPrefixFinder {

    // a Linked Hash Set is used to ensure that the ordering is preserved.
    public final static LinkedHashSet<String> ALL_PREFIXES = new LinkedHashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            // collection prefixes are added first because we want to
            // test validateAddTo and validateRemoveFrom before validate
            addAll(Arrays.asList(Constants.COLLECTION_PREFIXES));
            addAll(Arrays.asList(Constants.PROPERTY_PREFIXES));
            addAll(Arrays.asList(Constants.ACTION_PREFIXES));
        }
    };

    public String findPrefix(final String methodName) {
        for (final String prefix : ALL_PREFIXES) {
            if (methodName.startsWith(prefix)) {
                return prefix;
            }
        }
        return "";
    }

}
