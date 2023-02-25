package org.nakedobjects.metamodel.spec;


import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResult;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.aggregated.AggregatedFacet;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.object.value.ValueFacet;
import org.nakedobjects.metamodel.interactions.InteractionContext;
import org.nakedobjects.metamodel.interactions.ObjectTitleContext;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionContainer;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationContainer;


public interface NakedObjectSpecification extends Specification, NakedObjectActionContainer, NakedObjectAssociationContainer, Hierarchical, Dirtiable, DefaultProvider {

    // REVIEW why is there no Help method for classes?

    /**
     * Returns the name of this specification. This will be the fully qualified name of the Class object that
     * this object represents (i.e. it includes the package name).
     */
    String getFullName();

    /**
     * Returns the name of an icon to use for the specified object.
     */
    String getIconName(NakedObject object);

    /**
     * Returns the plural name for objects of this specification.
     */
    String getPluralName();

    /**
     * Returns the class name without the package. Removes the text up to, and including the last period
     * (".").
     */
    String getShortName();

    /**
     * Returns the description, if any, of the specification.
     */
    String getDescription();

    /**
     * Returns the singular name for objects of this specification.
     */
    String getSingularName();

    /**
     * Returns the title string for the specified object.
     */
    String getTitle(NakedObject adapter);

    boolean isAbstract();


    ////////////////////////////////////////////////////////////////
    // TitleContext
    ////////////////////////////////////////////////////////////////

    /**
     * Create an {@link InteractionContext} representing an attempt to read the object's title.
     */
    ObjectTitleContext createTitleInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject targetNakedObject);



    ////////////////////////////////////////////////////////////////
    // ValidityContext, Validity
    ////////////////////////////////////////////////////////////////
    
    /**
     * Create an {@link InteractionContext} representing an attempt to save the object.
     */
    ObjectValidityContext createValidityInteractionContext(
            AuthenticationSession session,
            InteractionInvocationMethod invocationMethod,
            NakedObject targetNakedObject);

    /**
     * Determines whether the specified object is in a valid state (for example, so 
     * can be persisted); represented as a {@link Consent}.
     */
    Consent isValid(NakedObject adapter);

    /**
     * Determines whether the specified object is in a valid state (for example, so can
     * be persisted); represented as a {@link InteractionResult}.
     */
    InteractionResult isValidResult(NakedObject adapter);

    
    ////////////////////////////////////////////////////////////////
    // Facets
    ////////////////////////////////////////////////////////////////

    /**
     * Determines if objects of this specification can be persisted or not. If it can be persisted (i.e. it
     * return something other than {@link Persistability}.TRANSIENT NakedObject.isPersistent() will indicated
     * whether the object is persistent or not. If they cannot be persisted then {@link NakedObject}.
     * {@link #persistability()} should be ignored.
     */
    Persistability persistability();

    /**
     * Determines if the object represents an object (value or otherwise).
     * 
     * <p>
     * In effect, means that it doesn't have the {@link CollectionFacet}, and therefore will return
     * NOT {@link #isCollection()}
     * 
     * @see #isCollection().
     */
    boolean isObject();

    /**
     * Determines if objects represents a collection.
     * 
     * <p>
     * In effect, means has got {@link CollectionFacet}, and therefore will return NOT {@link #isObject()}.
     * 
     * @see #isObject()
     */
    boolean isCollection();

    /**
     * Whether objects of this type are a collection or are intrinsically aggregated.
     * 
     * <p>
     * In effect, means has got a {@link CollectionFacet} and/or got the {@link AggregatedFacet}.
     */
    boolean isCollectionOrIsAggregated();

    /**
     * Determines if objects of this type are aggregated.
     * 
     * <p>
     * In effect, means has got {@link AggregatedFacet} or {@link ValueFacet}.
     */
    boolean isValueOrIsAggregated();

    /**
     * Determines if objects of this type can be set up from a text entry string.
     * 
     * <p>
     * In effect, means has got a {@link ParseableFacet}.
     */
    boolean isParseable();

    /**
     * Determines if objects of this type can be converted to a data-stream.
     * 
     * <p>
     * In effect, means has got {@link EncodableFacet}.
     */
    boolean isEncodeable();

    /**
     * Whether has the {@link ImmutableFacet}.
     */
    boolean isImmutable();

    
    ////////////////////////////////////////////////////////////////
    // Creation
    ////////////////////////////////////////////////////////////////

    /**
     * Used by {@link NakedObjectSpecification#createObject(CreationMode)}
     */
    public enum CreationMode {
    	/**
    	 * Default all properties and call any {@link CreatedCallbackFacet created callbacks}.
    	 */
    	INITIALIZE,
    	NO_INITIALIZE
    }

    /**
     * Create and optionally {@link CreationMode#INITIALIZE initialize} object.
     */
    Object createObject(CreationMode creationMode);


    ////////////////////////////////////////////////////////////////
    // Service
    ////////////////////////////////////////////////////////////////

    boolean isService();


    
    ////////////////////////////////////////////////////////////////
    // Introspection
    ////////////////////////////////////////////////////////////////


}
// Copyright (c) Naked Objects Group Ltd.
