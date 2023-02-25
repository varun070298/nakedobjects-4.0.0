package org.nakedobjects.runtime.snapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Stateless utility methods relating to the w3.org schema and schema-instance meta models.
 */
public final class XsMetaModel {

    private final Helper helper;
    /**
     * URI representing the namespace of the in-built xmlns namespace as defined by w3.org.
     * 
     * The NamespaceManager will not allow any namespaces with this URI to be added.
     */
    public static final String W3_ORG_XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    /**
     * Namespace prefix for {@link W3_ORG_XMLNS_URI}.
     * 
     * The NamespaceManager will not allow any namespace to use this prefix.
     */
    public static final String W3_ORG_XMLNS_PREFIX = "xmlns";
    /**
     * Namespace prefix for XML schema.
     */
    public static final String W3_ORG_XS_URI = "http://www.w3.org/2001/XMLSchema";
    /**
     * Namespace prefix for {@link W3_ORG_XS_URI}.
     * 
     * The NamespaceManager will not allow any namespace to use this prefix.
     */
    public static final String W3_ORG_XS_PREFIX = "xs";
    /**
     * Namespace prefix for XML schema instance.
     */
    public static final String W3_ORG_XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
    /**
     * Namespace prefix for {@link W3_ORG_XSI_URI}.
     * 
     * The NamespaceManager will not allow any namespace to use this prefix.
     */
    public static final String W3_ORG_XSI_PREFIX = "xsi";

    private final NofMetaModel nofMeta;

    public XsMetaModel() {
        this.helper = new Helper();
        this.nofMeta = new NofMetaModel();
    }

    /**
     * Creates an &lt;xs:schema&gt; element for the document to the provided element, attaching to root of
     * supplied Xsd doc.
     * 
     * In addition:
     * <ul>
     * <li>the elementFormDefault is set
     * <li>the NOF namespace is set
     * <li>the <code>xs:import</code> element referencing the NOF namespace is added as a child
     * </ul>
     */
    Element createXsSchemaElement(final Document xsdDoc) {
        if (xsdDoc.getDocumentElement() != null) {
            throw new IllegalArgumentException("XSD document already has content");
        }
        final Element xsSchemaElement = createXsElement(xsdDoc, "schema");

        xsSchemaElement.setAttribute("elementFormDefault", "qualified");

        nofMeta.addNamespace(xsSchemaElement);

        xsdDoc.appendChild(xsSchemaElement);
        final Element xsImportElement = createXsElement(xsdDoc, "import");
        xsImportElement.setAttribute("namespace", NofMetaModel.NOF_METAMODEL_NS_URI);
        xsImportElement.setAttribute("schemaLocation", NofMetaModel.DEFAULT_NOF_SCHEMA_LOCATION);

        xsSchemaElement.appendChild(xsImportElement);

        return xsSchemaElement;
    }

    Element createXsElementElement(final Document xsdDoc, final String className) {
        return createXsElementElement(xsdDoc, className, true);
    }

    Element createXsElementElement(final Document xsdDoc, final String className, final boolean includeCardinality) {
        final Element xsElementElement = createXsElement(xsdDoc, "element");
        xsElementElement.setAttribute("name", className);
        if (includeCardinality) {
            setXsCardinality(xsElementElement, 0, Integer.MAX_VALUE);
        }
        return xsElementElement;
    }

    /**
     * Creates an element in the XS namespace, adding the definition of the namespace to the root element of
     * the document if required,
     */
    Element createXsElement(final Document xsdDoc, final String localName) {

        final Element element = xsdDoc.createElementNS(XsMetaModel.W3_ORG_XS_URI, XsMetaModel.W3_ORG_XS_PREFIX + ":" + localName);
        // xmlns:xs="..." added to root
        helper.rootElementFor(element).setAttributeNS(XsMetaModel.W3_ORG_XMLNS_URI,
                XsMetaModel.W3_ORG_XMLNS_PREFIX + ":" + XsMetaModel.W3_ORG_XS_PREFIX, XsMetaModel.W3_ORG_XS_URI);
        return element;
    }

    // private Element addAnyToSequence(final Element xsSequenceElement) {
    // Element xsAnyElement = createXsElement(docFor(xsSequenceElement), "any");
    // xsAnyElement.setAttribute("namespace", "##other");
    // xsAnyElement.setAttribute("minOccurs", "0");
    // xsAnyElement.setAttribute("maxOccurs", "unbounded");
    // xsAnyElement.setAttribute("processContents", "lax");
    // xsSequenceElement.appendChild(xsAnyElement);
    // return xsSequenceElement;
    // }

    /**
     * Creates an xs:attribute ref="nof:xxx" element, and appends to specified owning element.
     */
    Element addXsNofAttribute(final Element parentXsElement, final String nofAttributeRef) {
        return addXsNofAttribute(parentXsElement, nofAttributeRef, null);
    }

    /**
     * Adds <code>xs:attribute ref="nof:xxx" fixed="yyy"</code> element, and appends to specified parent XSD
     * element.
     */
    Element addXsNofAttribute(final Element parentXsElement, final String nofAttributeRef, final String fixedValue) {
        return addXsNofAttribute(parentXsElement, nofAttributeRef, fixedValue, true);
    }

    /**
     * Adds <code>xs:attribute ref="nof:xxx" default="yyy"</code> element, and appends to specified parent
     * XSD element.
     * 
     * The last parameter determines whether to use <code>fixed="yyy"</code> rather than
     * <code>default="yyy"</code>.
     */
    Element addXsNofAttribute(
            final Element parentXsElement,
            final String nofAttributeRef,
            final String value,
            final boolean useFixed) {
        final Element xsNofAttributeElement = createXsElement(helper.docFor(parentXsElement), "attribute");
        xsNofAttributeElement.setAttribute("ref", NofMetaModel.NOF_METAMODEL_NS_PREFIX + ":" + nofAttributeRef);
        parentXsElement.appendChild(xsNofAttributeElement);
        if (value != null) {
            if (useFixed) {
                xsNofAttributeElement.setAttribute("fixed", value);
            } else {
                xsNofAttributeElement.setAttribute("default", value);
            }
        }
        return parentXsElement;
    }

    /**
     * Adds <code>xs:attribute ref="nof:feature" fixed="(feature)"</code> element as child to supplied XSD
     * element, presumed to be an <xs:complexType</code>.
     */
    Element addXsNofFeatureAttributeElements(final Element parentXsElement, final String feature) {
        final Element xsNofFeatureAttributeElement = createXsElement(helper.docFor(parentXsElement), "attribute");
        xsNofFeatureAttributeElement.setAttribute("ref", "nof:feature");
        xsNofFeatureAttributeElement.setAttribute("fixed", feature);
        parentXsElement.appendChild(xsNofFeatureAttributeElement);
        return xsNofFeatureAttributeElement;
    }

    /**
     * returns child <code>xs:complexType</code> element allowing mixed content for supplied parent XSD
     * element, creating and appending if necessary.
     * 
     * <p>
     * The supplied element is presumed to be one for which <code>xs:complexType</code> is valid as a child
     * (eg <code>xs:element</code>).
     */
    Element complexTypeFor(final Element parentXsElement) {
        return complexTypeFor(parentXsElement, true);
    }

    /**
     * returns child <code>xs:complexType</code> element, optionally allowing mixed content, for supplied
     * parent XSD element, creating and appending if necessary.
     * 
     * <p>
     * The supplied element is presumed to be one for which <code>xs:complexType</code> is valid as a child
     * (eg <code>xs:element</code>).
     */
    Element complexTypeFor(final Element parentXsElement, final boolean mixed) {
        final Element el = childXsElement(parentXsElement, "complexType");
        if (mixed) {
            el.setAttribute("mixed", "true");
        }
        return el;
    }

    /**
     * returns child <code>xs:sequence</code> element for supplied parent XSD element, creating and
     * appending if necessary.
     * 
     * The supplied element is presumed to be one for which <code>xs:simpleContent</code> is valid as a
     * child (eg <code>xs:complexType</code>).
     */
    Element sequenceFor(final Element parentXsElement) {
        return childXsElement(parentXsElement, "sequence");
    }

    /**
     * returns child <code>xs:choice</code> element for supplied parent XSD element, creating and appending
     * if necessary.
     * 
     * The supplied element is presumed to be one for which <code>xs:simpleContent</code> is valid as a
     * child (eg <code>xs:complexType</code>).
     */
    Element choiceFor(final Element parentXsElement) {
        return childXsElement(parentXsElement, "choice");
    }

    Element sequenceForComplexTypeFor(final Element parentXsElement) {
        return sequenceFor(complexTypeFor(parentXsElement));
    }

    Element choiceForComplexTypeFor(final Element parentXsElement) {
        return choiceFor(complexTypeFor(parentXsElement));
    }

    /**
     * Returns the <code>xs:choice</code> or <code>xs:sequence</code> element under the supplied XSD
     * element, or null if neither can be found.
     */
    Element choiceOrSequenceFor(final Element parentXsElement) {
        final NodeList choiceNodeList = parentXsElement.getElementsByTagNameNS(XsMetaModel.W3_ORG_XS_URI, "choice");
        if (choiceNodeList.getLength() > 0) {
            return (Element) choiceNodeList.item(0);
        }
        final NodeList sequenceNodeList = parentXsElement.getElementsByTagNameNS(XsMetaModel.W3_ORG_XS_URI, "sequence");
        if (sequenceNodeList.getLength() > 0) {
            return (Element) sequenceNodeList.item(0);
        }
        return null;
    }

    /**
     * returns child <code>xs:simpleContent</code> element for supplied parent XSD element, creating and
     * appending if necessary.
     * 
     * The supplied element is presumed to be one for which <code>xs:simpleContent</code> is valid as a
     * child (eg <code>xs:complexType</code>).
     */
    Element simpleContentFor(final Element parentXsElement) {
        return childXsElement(parentXsElement, "simpleContent");
    }

    /**
     * returns child <code>xs:extension</code> element for supplied parent XSD element, creating and
     * appending if nec; also sets the <code>base</code> attribute.
     * 
     * The supplied element is presumed to be one for which <code>xs:extension</code> is valid as a child
     * (eg <code>xs:complexType</code>).
     */
    Element extensionFor(final Element parentXsElement, final String base) {
        final Element childXsElement = childXsElement(parentXsElement, "extension");
        childXsElement.setAttribute("base", XsMetaModel.W3_ORG_XS_PREFIX + ":" + base);
        return childXsElement;
    }

    Element childXsElement(final Element parentXsElement, final String localName) {
        final NodeList nodeList = parentXsElement.getElementsByTagNameNS(XsMetaModel.W3_ORG_XS_URI, localName);
        if (nodeList.getLength() > 0) {
            return (Element) nodeList.item(0);
        }

        final Element childXsElement = createXsElement(helper.docFor(parentXsElement), localName);
        parentXsElement.appendChild(childXsElement);

        return childXsElement;
    }

    /**
     * @return the <code>xs:schema</code> element (the root element of the owning XSD Doc).
     */
    Element schemaFor(final Element xsElement) {
        return xsElement.getOwnerDocument().getDocumentElement();
    }

    /**
     * Sets the <code>minOccurs</code> and <code>maxOccurs</code> attributes for provided
     * <code>element</code> (presumed to be an XSD element for which these attributes makes sense.
     */
    Element setXsCardinality(final Element xsElement, final int minOccurs, final int maxOccurs) {
        if (maxOccurs >= 0) {
            xsElement.setAttribute("minOccurs", "" + minOccurs);
        }
        if (maxOccurs >= 0) {
            if (maxOccurs == Integer.MAX_VALUE) {
                xsElement.setAttribute("maxOccurs", "unbounded");
            } else {
                xsElement.setAttribute("maxOccurs", "" + maxOccurs);
            }
        }
        return xsElement;
    }

}
// Copyright (c) Naked Objects Group Ltd.
