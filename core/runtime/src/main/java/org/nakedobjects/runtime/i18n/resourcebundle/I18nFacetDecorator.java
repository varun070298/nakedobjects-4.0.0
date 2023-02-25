package org.nakedobjects.runtime.i18n.resourcebundle;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.help.HelpFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.spec.identifier.Identified;
import org.nakedobjects.runtime.i18n.resourcebundle.facets.DescribedAsFacetWrapI18n;
import org.nakedobjects.runtime.i18n.resourcebundle.facets.HelpFacetWrapI18n;
import org.nakedobjects.runtime.i18n.resourcebundle.facets.NamedFacetWrapI18n;


public class I18nFacetDecorator extends FacetDecoratorAbstract {
    private final I18nManager i18nManager;

    public I18nFacetDecorator(final I18nManager manager) {
        i18nManager = manager;
    }

    public Facet decorate(final Facet facet, FacetHolder requiredHolder) {
    	if (!(requiredHolder instanceof Identified)) {
            return null;
        }
        
        Identified identified = (Identified) requiredHolder;
        final Identifier identifier = identified.getIdentifier();

        final Class<?> facetType = facet.facetType();
        if (facetType == NamedFacet.class) {
            final String i18nName = i18nManager.getName(identifier);
            if (i18nName == null) {
                return null;
            }
            NamedFacetWrapI18n decoratingFacet = new NamedFacetWrapI18n(i18nName, facet.getFacetHolder());
			return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }
        if (facetType == DescribedAsFacet.class) {
            final String i18nDescription = i18nManager.getDescription(identifier);
            if (i18nDescription == null) {
                return null;
            }
            DescribedAsFacetWrapI18n decoratingFacet = new DescribedAsFacetWrapI18n(i18nDescription, facet.getFacetHolder());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }
        if (facetType == HelpFacet.class) {
            final String i18nHelp = i18nManager.getHelp(identifier);
            if (i18nHelp == null) {
                return null;
            }
            HelpFacetWrapI18n decoratingFacet = new HelpFacetWrapI18n(i18nHelp, facet.getFacetHolder());
            return replaceFacetWithDecoratingFacet(facet, decoratingFacet, requiredHolder);
        }
        return facet;
    }

    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[] { NamedFacet.class, DescribedAsFacet.class, HelpFacet.class };
    }
}

// Copyright (c) Naked Objects Group Ltd.
