package org.nakedobjects.plugins.html.component;

public interface Page extends Component {

    void addDebug(String value);

    void addDebug(String label, String value);

    Block getNavigation();

    Block getPageHeader();

    ViewPane getViewPane();

    void setCrumbs(Component component);

    void setDebug(DebugPane debugPane);

    void setTitle(String title);

}

// Copyright (c) Naked Objects Group Ltd.
