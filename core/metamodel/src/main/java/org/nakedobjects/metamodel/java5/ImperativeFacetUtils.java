package org.nakedobjects.metamodel.java5;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.lang.CastUtils;
import org.nakedobjects.metamodel.facets.DecoratingFacet;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;


public final class ImperativeFacetUtils {

    private ImperativeFacetUtils() {}

    /**
     * Returns the provided {@link Facet facet} as an {@link ImperativeFacet} if it either is one or if it is
     * a {@link DecoratingFacet} that in turn wraps an {@link ImperativeFacet}.
     * 
     * <p>
     * Otherwise, returns <tt>null</tt>.
     */
    public static ImperativeFacet getImperativeFacet(final Facet facet) {
        if (facet instanceof ImperativeFacet) {
            return (ImperativeFacet) facet;
        }
        if (facet instanceof DecoratingFacet) {
            final DecoratingFacet<?> decoratingFacet = CastUtils.cast(facet);
            return getImperativeFacet(decoratingFacet.getDecoratedFacet());
        }
        return null;

    }

    public static boolean isImperativeFacet(final Facet facet) {
        return getImperativeFacet(facet) != null;
    }

    public static class ImperativeFacetFlags {
        private boolean impliesResolve;
        private boolean impliesObjectChanged;

        public void apply(ImperativeFacet imperativeFacet) {
            this.impliesResolve |= imperativeFacet.impliesResolve();
            this.impliesObjectChanged |= imperativeFacet.impliesObjectChanged();
        }

        public boolean bothSet() {
            return impliesResolve && impliesObjectChanged;
        }

        public boolean impliesResolve() {
            return impliesResolve;
        }

        public boolean impliesObjectChanged() {
            return impliesObjectChanged;
        }
    }

    public static ImperativeFacetFlags getImperativeFacetFlags(final NakedObjectMember member, final Method method) {
        ImperativeFacetFlags flags = new ImperativeFacetFlags();
        if (member == null) {
            return flags;
        }
        Facet[] allFacets = member.getFacets(AbstractFilter.noop(Facet.class));
        for (Facet facet : allFacets) {
            ImperativeFacet imperativeFacet = ImperativeFacetUtils.getImperativeFacet(facet);
            if (imperativeFacet == null) {
                continue;
            }
            List<Method> methods = imperativeFacet.getMethods();
            if (!methods.contains(method)) {
                continue;
            }
            flags.apply(imperativeFacet);

            // no need to search further
            if (flags.bothSet()) {
                break;
            }
        }
        return flags;
    }

}
