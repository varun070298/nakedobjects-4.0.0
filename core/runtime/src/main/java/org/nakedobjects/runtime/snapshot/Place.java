package org.nakedobjects.runtime.snapshot;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.w3c.dom.Element;


/**
 * Represents a place in the graph to be navigated; really just wraps an object and an XML Element in its XML
 * document. Also provides the capability to extract the corresponding XSD element (associated with each XML
 * element).
 * 
 * The XML element (its children) is mutated as the graph of objects is navigated.
 */
final class Place {
    private static final String USER_DATA_XSD_KEY = "XSD";
	private final NakedObject object;
    private final Element element;

    Place(final NakedObject object, final Element element) {
        this.object = object;
        this.element = element;
    }

    public Element getXmlElement() {
        return element;
    }

    public NakedObject getObject() {
        return object;
    }

    public Element getXsdElement() {
        final Object o = element.getUserData(USER_DATA_XSD_KEY);
        if (o == null || !(o instanceof Element)) {
            return null;
        }
        return (Element) o;
    }

    // TODO: smelly; where should this responsibility lie?
    static void setXsdElement(final Element element, final Element xsElement) {
        element.setUserData(USER_DATA_XSD_KEY, xsElement, null);
    }

}
// Copyright (c) Naked Objects Group Ltd.
