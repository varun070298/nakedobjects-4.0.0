package org.nakedobjects.metamodel.facets.object.immutable;

import org.nakedobjects.applib.marker.AlwaysImmutable;
import org.nakedobjects.applib.marker.ImmutableOncePersisted;
import org.nakedobjects.applib.marker.ImmutableUntilPersisted;
import org.nakedobjects.applib.marker.NeverImmutable;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class ImmutableMarkerInterfacesFacetFactory extends FacetFactoryAbstract {

    public ImmutableMarkerInterfacesFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        When when = null;
        if (AlwaysImmutable.class.isAssignableFrom(cls)) {
            when = When.ALWAYS;
        } else if (ImmutableOncePersisted.class.isAssignableFrom(cls)) {
            when = When.ONCE_PERSISTED;
        } else if (ImmutableUntilPersisted.class.isAssignableFrom(cls)) {
            when = When.UNTIL_PERSISTED;
        } else if (NeverImmutable.class.isAssignableFrom(cls)) {
            when = When.NEVER;
        }
        return FacetUtil.addFacet(create(when, holder));
    }

    private ImmutableFacet create(final When when, final FacetHolder holder) {
        return when == null ? null : new ImmutableFacetMarkerInterface(when, holder);
    }

}
