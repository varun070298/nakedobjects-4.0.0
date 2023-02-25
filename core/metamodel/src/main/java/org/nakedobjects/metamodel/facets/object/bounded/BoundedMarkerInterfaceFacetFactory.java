package org.nakedobjects.metamodel.facets.object.bounded;

import java.lang.reflect.Method;

import org.nakedobjects.applib.marker.Bounded;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class BoundedMarkerInterfaceFacetFactory extends FacetFactoryAbstract {

    public BoundedMarkerInterfaceFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> clazz, final MethodRemover methodRemover, final FacetHolder holder) {
        final boolean implementsMarker = Bounded.class.isAssignableFrom(clazz);
        return FacetUtil.addFacet(create(implementsMarker, holder));
    }

    private BoundedFacet create(final boolean implementsMarker, final FacetHolder holder) {
        return implementsMarker ? new BoundedFacetMarkerInterface(holder) : null;
    }

    public boolean recognizes(final Method method) {
        return false;
    }

}
