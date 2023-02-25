package org.nakedobjects.runtime.testspec;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.testdomain.Movie;
import org.nakedobjects.runtime.testdomain.Person;
import org.nakedobjects.runtime.testsystem.TestProxyNakedObject;



class MovieDirectorField extends OneToOneAssociationTest {

    @Override
    public void clearAssociation(final NakedObject inObject) {
        getMovie(inObject).setDirector(null);
    }

    public String debugData() {
        return "";
    }

    public NakedObject get(final NakedObject fromObject) {
        final Person director = getMovie(fromObject).getDirector();
        if (director == null) {
            return null;
        } else {
            return getAdapterManager().adapterFor(director);
        }
    }

    public String getId() {
        return "director";
    }

    private Movie getMovie(final NakedObject inObject) {
        return (Movie) inObject.getObject();
    }

    public String getName() {
        return "Director";
    }

    public NakedObjectSpecification getSpecification() {
        return NakedObjectsContext.getSpecificationLoader().loadSpecification(Person.class);
    }

    public void initAssociation(final NakedObject inObject, final NakedObject associate) {
        getMovie(inObject).setDirector(associate == null ? null : (Person) associate.getObject());
    }

    public Consent isAssociationValid(final NakedObject inObject, final NakedObject associate) {
        return Allow.DEFAULT;
    }

    public void setAssociation(final NakedObject inObject, final NakedObject associate) {
        getMovie(inObject).setDirector((Person) associate.getObject());
    }

    public void set(NakedObject owner, NakedObject newValue) {
        setAssociation(owner, newValue);
    }

}

class MovieNameField extends ValueFieldTest {
    
    public boolean isOneToManyAssociation() {
        return false;
    }
    public void clearAssociation(final NakedObject inObject) {
        getMovie(inObject).setName("");
    }

    public String debugData() {
        return "";
    }

    public NakedObject get(final NakedObject fromObject) {
        final TestProxyNakedObject nakedObject = new TestProxyNakedObject();
        final String object = getMovie(fromObject).getName();
        nakedObject.setupObject(object);
        return nakedObject;
    }

    public String getId() {
        return "name";
    }

    private Movie getMovie(final NakedObject inObject) {
        return (Movie) inObject.getObject();
    }

    public String getName() {
        return "Name";
    }

    public NakedObjectSpecification getSpecification() {
        return new TestProxySpecification("java.lang.String");
    }

    public void initAssociation(final NakedObject inObject, final NakedObject association) {
        getMovie(inObject).setName((String) association.getObject());
    }

    public Consent isAssociationValid(final NakedObject inObject, final NakedObject association) {
        return Allow.DEFAULT;
    }

    public void setAssociation(final NakedObject inObject, final NakedObject association) {
        getMovie(inObject).setName((String) association.getObject());
    }

    public void set(NakedObject owner, NakedObject newValue) {
        setAssociation(owner, newValue);
    }

}

// Copyright (c) Naked Objects Group Ltd.

public class MovieSpecification extends TestProxySpecification {

    public MovieSpecification() {
        super(Movie.class);
        fields = new NakedObjectAssociation[] { new MovieNameField(), new MovieDirectorField(),
        // new MovieRolesField()
        };
    }

    @Override
    public NakedObjectAction getClassAction(
            final NakedObjectActionType type,
            final String name,
            final NakedObjectSpecification[] parameters) {
        return null;
    }

    @Override
    public String getFullName() {
        return Movie.class.getName();
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
        return "Movies";
    }

    @Override
    public String getShortName() {
        return "movie";
    }

    @Override
    public String getSingularName() {
        return "Movie";
    }

    @Override
    public String getTitle(final NakedObject naked) {
        return ((Movie) naked.getObject()).title();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Object newInstance() {
        return new Movie();
    }

    public InteractionContext createVisibleInteractionContext(
            final AuthenticationSession session,
            final NakedObject target,
            final InteractionInvocationMethod invocationMethod) {
        return null;
    }

    public InteractionContext createUsableInteractionContext(
            final AuthenticationSession session,
            final NakedObject target,
            final InteractionInvocationMethod invocationMethod) {
        return null;
    }

    
}
