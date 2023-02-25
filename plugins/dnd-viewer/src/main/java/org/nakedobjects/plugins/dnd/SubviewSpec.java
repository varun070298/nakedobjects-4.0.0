package org.nakedobjects.plugins.dnd;

public interface SubviewSpec {

    View createSubview(Content content, ViewAxis axis, int sequence);

    View decorateSubview(View view);
}
// Copyright (c) Naked Objects Group Ltd.
