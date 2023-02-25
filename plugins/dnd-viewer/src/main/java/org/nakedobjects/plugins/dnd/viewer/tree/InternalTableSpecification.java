package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.basic.TableFocusManager;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;
import org.nakedobjects.plugins.dnd.viewer.table.AbstractTableSpecification;
import org.nakedobjects.plugins.dnd.viewer.table.TableHeader;


public class InternalTableSpecification extends AbstractTableSpecification {
    public InternalTableSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
    }

    @Override
    public View doCreateView(final View view, final Content content, final ViewAxis axis) {
        final ScrollBorder scrollingView = new ScrollBorder(view);
        // note - the next call needs to be after the creation of the window border
        // so that it exists when the header is set up
        scrollingView.setTopHeader(new TableHeader(content, view.getViewAxis()));
        scrollingView.setFocusManager(new TableFocusManager(scrollingView));
        return scrollingView;
    }

    protected View decorateView(View view) {
        super.decorateView(view);
        
        final ScrollBorder scrollingView = new ScrollBorder(view);
        // note - the next call needs to be after the creation of the window border
        // so that it exists when the header is set up
        scrollingView.setTopHeader(new TableHeader(view.getContent(), view.getViewAxis()));
        scrollingView.setFocusManager(new TableFocusManager(scrollingView));
        return scrollingView;
    }
    
    @Override
    public String getName() {
        return "Table";
    }
}
// Copyright (c) Naked Objects Group Ltd.
