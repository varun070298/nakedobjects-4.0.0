package org.nakedobjects.plugins.html.component;

import java.util.List;

public interface ViewPane extends Component {

    void setIconName(String iconName);

    void setTitle(String title, String description);

    void add(Component content);

    void setMenu(Component[] component);

    void setWarningsAndMessages(List<String> list, List<String> list2);
}

// Copyright (c) Naked Objects Group Ltd.
