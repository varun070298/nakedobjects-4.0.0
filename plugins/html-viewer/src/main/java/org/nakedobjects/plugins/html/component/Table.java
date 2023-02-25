package org.nakedobjects.plugins.html.component;

public interface Table extends Component {

    void setSummary(String string);

    void addColumnHeader(String name);

    void addRowHeader(Component component);

    void addCell(String string, boolean truncate);

    void addCell(Component component);

    void addEmptyCell();

}

// Copyright (c) Naked Objects Group Ltd.
