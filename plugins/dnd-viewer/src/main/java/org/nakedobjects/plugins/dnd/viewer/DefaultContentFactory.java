package org.nakedobjects.plugins.dnd.viewer;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentFactory;
import org.nakedobjects.plugins.dnd.viewer.content.OneToManyFieldImpl;
import org.nakedobjects.plugins.dnd.viewer.content.OneToOneFieldImpl;
import org.nakedobjects.plugins.dnd.viewer.content.RootCollection;
import org.nakedobjects.plugins.dnd.viewer.content.RootObject;
import org.nakedobjects.plugins.dnd.viewer.content.TextParseableFieldImpl;


public class DefaultContentFactory implements ContentFactory {

    public Content createRootContent(final NakedObject object) {
        Assert.assertNotNull(object);
        final NakedObjectSpecification objectSpec = object.getSpecification();
        if (objectSpec.isCollection()) {
            return new RootCollection(object);
        }
        if (objectSpec.isObject()) {
            return new RootObject(object);
        } 
    
        throw new IllegalArgumentException("Must be an object or collection: " + object);
    }

    public Content createFieldContent(final NakedObjectAssociation field, final NakedObject object) {
        Content content;
        NakedObject associatedObject = field.get(object);
        if (field instanceof OneToManyAssociation) {
            content = new OneToManyFieldImpl(object, associatedObject, (OneToManyAssociation) field);
        } else if (field instanceof OneToOneAssociation) {
            final NakedObjectSpecification fieldSpecification = field.getSpecification();
            if (fieldSpecification.isParseable()) {
                content = new TextParseableFieldImpl(object, associatedObject, (OneToOneAssociation) field);
            } else {
                content = new OneToOneFieldImpl(object, associatedObject, (OneToOneAssociation) field);
            }
        } else {
            throw new NakedObjectException();
        }

        return content;
    }

}
// Copyright (c) Naked Objects Group Ltd.
