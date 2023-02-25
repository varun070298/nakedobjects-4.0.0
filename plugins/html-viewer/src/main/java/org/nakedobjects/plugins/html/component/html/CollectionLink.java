package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.html.component.ComponentAbstract;
import org.nakedobjects.plugins.html.image.ImageLookup;
import org.nakedobjects.plugins.html.request.Request;



class CollectionLink extends ComponentAbstract {
    private final String objectId;
    private final String fieldId;
    private final NakedObjectSpecification specification;
    private final String title;
    private final String description;

    public CollectionLink(
            final NakedObjectAssociation field,
            final NakedObject collection,
            final String description,
            final String objectId) {
        this.description = description;
        this.objectId = objectId;
        fieldId = field.getId();
        title = collection.titleString();
        specification = field.getSpecification();
    }

    public void write(final PrintWriter writer) {
        writer.print("<span class=\"value\"");
        if (description != null) {
            writer.print(" title=\"");
            writer.print(description);
            writer.print("\"");
        }
        writer.print(">");

        writer.print("<a href=\"");
        writer.print(Request.FIELD_COLLECTION_COMMAND + ".app?id=");
        writer.print(objectId);
        writer.print("&amp;field=");
        writer.print(fieldId);
        writer.print("\"");
        writer.print("><img src=\"");
        writer.print(ImageLookup.image(specification));
        writer.print("\" alt=\"icon\">");
        // writer.print(elementType);
        writer.print(title);
        writer.print("</a>");
        writer.println("</span>");
    }

}

// Copyright (c) Naked Objects Group Ltd.
