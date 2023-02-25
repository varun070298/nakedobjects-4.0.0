package org.nakedobjects.runtime.testspec;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.runtime.testdomain.Person;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;
import org.nakedobjects.runtime.testsystem.TestSpecification;



class PersonNameField extends ValueFieldTest {

    public void clearAssociation(final NakedObject inObject) {
        getPerson(inObject).setName("");
    }

    public String debugData() {
        return "";
    }

    public NakedObject get(final NakedObject fromObject) {
        final TestProxyNakedObject nakedObject = new TestProxyNakedObject();
        nakedObject.setupObject(getPerson(fromObject).getName());
        return nakedObject;
    }

    public String getId() {
        return "name";
    }

    public String getName() {
        return "Name";
    }

    private Person getPerson(final NakedObject inObject) {
        return (Person) inObject.getObject();
    }

    public NakedObjectSpecification getSpecification() {
        return new TestSpecification("java.lang.String");
    }

    public void initAssociation(final NakedObject inObject, final NakedObject association) {
        getPerson(inObject).setName((String) association.getObject());
    }

    public Consent isAssociationValid(final NakedObject inObject, final NakedObject association) {
        return Allow.DEFAULT;
    }

    public void setAssociation(final NakedObject inObject, final NakedObject association) {
        getPerson(inObject).setName((String) association.getObject());
    }

    public void set(NakedObject owner, NakedObject newValue) {
        setAssociation(owner, newValue);
    }

}

public class PersonSpecification extends TestProxySpecification {

    public PersonSpecification() {
        super(Person.class);
        fields = new NakedObjectAssociation[] { new PersonNameField(), };
    }

    @Override
    public String getFullName() {
        return Person.class.getName();
    }

    @Override
    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String name) {
        return null;
    }

    @Override
    public NakedObjectAction getObjectAction(
            final NakedObjectActionType type,
            final String name,
            final NakedObjectSpecification[] parameters) {
        return null;
    }

    @Override
    public NakedObjectAction[] getObjectActions(final NakedObjectActionType... type) {
        return null;
    }

    @Override
    public String getPluralName() {
        return "People";
    }

    @Override
    public String getShortName() {
        return "person";
    }

    @Override
    public String getSingularName() {
        return "Person";
    }

    @Override
    public String getTitle(final NakedObject naked) {
        return ((Person) naked.getObject()).title();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Object newInstance() {
        return new Person();
    }
}

// Copyright (c) Naked Objects Group Ltd.

