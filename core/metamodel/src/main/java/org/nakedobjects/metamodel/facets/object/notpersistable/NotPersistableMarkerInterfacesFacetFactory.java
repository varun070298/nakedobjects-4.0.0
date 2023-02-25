package org.nakedobjects.metamodel.facets.object.notpersistable;

import java.lang.reflect.Method;

import org.nakedobjects.applib.marker.NonPersistable;
import org.nakedobjects.applib.marker.ProgramPersistable;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class NotPersistableMarkerInterfacesFacetFactory extends FacetFactoryAbstract {

    public NotPersistableMarkerInterfacesFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    public boolean recognizes(final Method method) {
        return false;
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        InitiatedBy initiatedBy = null;
        if (ProgramPersistable.class.isAssignableFrom(type)) {
            initiatedBy = InitiatedBy.USER;
        } else if (NonPersistable.class.isAssignableFrom(type)) {
            initiatedBy = InitiatedBy.USER_OR_PROGRAM;
        }
        return FacetUtil.addFacet(create(initiatedBy, holder));
    }

    private NotPersistableFacet create(final InitiatedBy initiatedBy, final FacetHolder holder) {
        return initiatedBy != null ? new NotPersistableFacetMarkerInterface(initiatedBy, holder) : null;
    }

}
