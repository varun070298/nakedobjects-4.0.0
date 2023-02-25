package org.nakedobjects.runtime.snapshot;

import java.io.CharArrayWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;


public class DomSerializerJaxp implements DomSerializer {

    private final TransformerFactory transformerFactory;
	private final Transformer newTransformer;

	public DomSerializerJaxp() throws TransformerConfigurationException {
    	this.transformerFactory = TransformerFactory.newInstance();
    	this.newTransformer = transformerFactory.newTransformer();
	}
	
	public String serialize(final Element domElement) {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            serializeTo(domElement, caw);
            return caw.toString();
        } catch (final Exception e) {
            return null;
        }
    }

    public void serializeTo(final Element domElement, final OutputStream os) throws Exception {
        final OutputStreamWriter osw = new OutputStreamWriter(os);
        serializeTo(domElement, osw);
    }

    public void serializeTo(final Element domElement, final Writer writer) throws Exception {
		Source source = new DOMSource(domElement);
		Result result = new StreamResult(writer);
		newTransformer.transform(source, result);
    }

}
// Copyright (c) Naked Objects Group Ltd.
