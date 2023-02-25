package org.nakedobjects.runtime.snapshot;

import java.io.OutputStream;
import java.io.Writer;

import org.w3c.dom.Element;


public interface DomSerializer {
    public String serialize(final Element domElement);

    public void serializeTo(final Element domElement, final OutputStream os) throws Exception;

    public void serializeTo(final Element domElement, final Writer w) throws Exception;
}
// Copyright (c) Naked Objects Group Ltd.
