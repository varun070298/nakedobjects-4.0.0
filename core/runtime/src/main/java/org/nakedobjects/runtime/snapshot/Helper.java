package org.nakedobjects.runtime.snapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Stateless utility methods for manipulating XML documents.
 */
public final class Helper {

    /**
     * Helper method
     */
    String trailingSlash(final String str) {
        return str.endsWith("/") ? str : str + "/";
    }

    /**
     * Utility method that returns just the class's name for the supplied fully qualified class name.
     * 
     * cf 'basename' in Unix.
     */
    String classNameFor(final String fullyQualifiedClassName) {
        final int fullNameLastPeriodIdx = fullyQualifiedClassName.lastIndexOf('.');
        if (fullNameLastPeriodIdx > 0 && fullNameLastPeriodIdx < fullyQualifiedClassName.length()) {
            return fullyQualifiedClassName.substring(fullNameLastPeriodIdx + 1);
        } else {
            return fullyQualifiedClassName;
        }
    }

    /**
     * Utility method that returns the package name for the supplied fully qualified class name, or
     * <code>default</code> if the class is in no namespace / in the default namespace.
     * 
     * cf 'dirname' in Unix.
     */
    String packageNameFor(final String fullyQualifiedClassName) {
        final int fullNameLastPeriodIdx = fullyQualifiedClassName.lastIndexOf('.');
        if (fullNameLastPeriodIdx > 0) {
            return fullyQualifiedClassName.substring(0, fullNameLastPeriodIdx);
        } else {
            return "default"; // TODO: should provide a better way to specify namespace.
        }
    }

    /**
     * Returns the root element for the element by looking up the owner document for the element, and from
     * that its document element.
     * 
     * If no document element exists, just returns the supplied document.
     */
    Element rootElementFor(final Element element) {
        final Document doc = element.getOwnerDocument();
        if (doc == null) {
            return element;
        }
        final Element rootElement = doc.getDocumentElement();
        if (rootElement == null) {
            return element;
        }
        return rootElement;
    }

    Document docFor(final Element element) {
        return element.getOwnerDocument();
    }

}
// Copyright (c) Naked Objects Group Ltd.
