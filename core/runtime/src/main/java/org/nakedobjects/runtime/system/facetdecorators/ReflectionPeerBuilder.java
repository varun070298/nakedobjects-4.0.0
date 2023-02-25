package org.nakedobjects.runtime.system.facetdecorators;

import java.util.HashMap;
import java.util.Map;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.i18n.resourcebundle.I18nFacetDecorator;
import org.nakedobjects.runtime.i18n.resourcebundle.ResourceBasedI18nManager;
import org.nakedobjects.runtime.transaction.facetdecorator.standard.StandardTransactionFacetDecorator;


/**
 * @deprecated
 */
@Deprecated
public class ReflectionPeerBuilder implements ApplicationScopedComponent {
    private final Map<Class<? extends Facet>, FacetDecorator> facetDecoratorsByFacetType = new HashMap<Class<? extends Facet>, FacetDecorator>();

    // ////////////////////////////////////////////////////////////////
    // init, shutdown
    // ////////////////////////////////////////////////////////////////

    public void init() {
        // TODO these need to be added via configuration
        NakedObjectConfiguration configuration = NakedObjectsContext.getConfiguration();
        final ResourceBasedI18nManager resourceBasedI18nManager = new ResourceBasedI18nManager(configuration);
        resourceBasedI18nManager.init();
        addToFacetDecoratorsMap(new I18nFacetDecorator(resourceBasedI18nManager));
        addToFacetDecoratorsMap(new StandardTransactionFacetDecorator(configuration));
    }

    private void addToFacetDecoratorsMap(final FacetDecorator facetDecorator) {
        final Class<? extends Facet>[] forFacetTypes = facetDecorator.getFacetTypes();
        for (int i = 0; i < forFacetTypes.length; i++) {
            facetDecoratorsByFacetType.put(forFacetTypes[i], facetDecorator);
        }
    }

    public void shutdown() {}

    // ////////////////////////////////////////////////////////////////
    // Debug
    // ////////////////////////////////////////////////////////////////

    public void debugData(final DebugString str) {
        str.appendTitle("Reflective peers");
        for(final Class<? extends Facet> key: facetDecoratorsByFacetType.keySet()) {
            str.appendln(key.getName(), facetDecoratorsByFacetType.get(key));
        }
        str.appendln();
    }

}
// Copyright (c) Naked Objects Group Ltd.
