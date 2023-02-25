package org.nakedobjects.plugins.dnd.viewer.list;

import java.util.List;

import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.facets.value.IntegerValueFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;

public class HistogramSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    static List<? extends NakedObjectAssociation> availableFields(CollectionContent content) {
    List<? extends NakedObjectAssociation> associationList = 
        content.getElementSpecification().getAssociationList(new AbstractFilter<NakedObjectAssociation>() {
        public boolean accept(NakedObjectAssociation t) {
            return t.getSpecification().containsFacet(IntegerValueFacet.class);
        }
    });
    return associationList;
    }

    
    public HistogramSpecification() {
        builder = new HistogramLayout(new CollectionElementBuilder(this));
    }

    public boolean canDisplay(Content content, ViewRequirement requirement) {
        return content.isCollection() && availableFields((CollectionContent) content).size() > 0;
    }
    
    public View createSubview(Content content, ViewAxis axis, int sequence) {
        return new HistogramBar(content, this, axis);
    }

    @Override
    protected ViewAxis axis(Content content) {
        return new HistogramAxis(content);
    }
    
    public String getName() {
        return "Histogram";
    }

    @Override
    public boolean isAligned() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public boolean isSubView() {
        return false;
    }

}


// Copyright (c) Naked Objects Group Ltd.
