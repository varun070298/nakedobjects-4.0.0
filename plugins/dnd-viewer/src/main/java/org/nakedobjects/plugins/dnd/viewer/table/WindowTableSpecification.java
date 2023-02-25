package org.nakedobjects.plugins.dnd.viewer.table;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.basic.TableFocusManager;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.StackLayout;


public class WindowTableSpecification extends org.nakedobjects.plugins.dnd.viewer.table.AbstractTableSpecification {
    public WindowTableSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this));
    }

    @Override
    public View doCreateView(final View view, final Content content, final ViewAxis axis) {
        if (true) return view;
        
        final ScrollBorder scrollingView = new ScrollBorder(view);
//        final WindowBorder viewWithWindowBorder = new WindowBorder(scrollingView, false);
        View viewWithWindowBorder = scrollingView;
        // note - the next call needs to be after the creation of the window border so
        // that it exists when the header is set up
        scrollingView.setTopHeader(new TableHeader(content, view.getViewAxis()));
        viewWithWindowBorder.setFocusManager(new TableFocusManager(viewWithWindowBorder));
        return viewWithWindowBorder;
    }
    
    protected View decorateView(View view) {
        super.decorateView(view);
        
            final ScrollBorder scrollingView = new ScrollBorder(view);
    //      final WindowBorder viewWithWindowBorder = new WindowBorder(scrollingView, false);
          View viewWithWindowBorder = scrollingView;
          // note - the next call needs to be after the creation of the window border so
          // that it exists when the header is set up
          scrollingView.setTopHeader(new TableHeader(view.getContent(), view.getViewAxis()));
          viewWithWindowBorder.setFocusManager(new TableFocusManager(viewWithWindowBorder));
          return viewWithWindowBorder;
    }

    @Override
    public String getName() {
        return "Table";
    }
   
    @Override
    public boolean isReplaceable() {
        return false;
    }


}
// Copyright (c) Naked Objects Group Ltd.
