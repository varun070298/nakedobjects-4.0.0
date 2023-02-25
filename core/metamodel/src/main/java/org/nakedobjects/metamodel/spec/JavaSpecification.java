package org.nakedobjects.metamodel.spec;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.commons.lang.ArrayUtils;
import org.nakedobjects.metamodel.commons.lang.CastUtils;
import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;
import org.nakedobjects.metamodel.exceptions.ReflectionException;
import org.nakedobjects.metamodel.facetdecorator.FacetDecoratorSet;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetInferred;
import org.nakedobjects.metamodel.facets.object.callbacks.CreatedCallbackFacet;
import org.nakedobjects.metamodel.facets.object.dirty.ClearDirtyObjectFacet;
import org.nakedobjects.metamodel.facets.object.dirty.IsDirtyObjectFacet;
import org.nakedobjects.metamodel.facets.object.dirty.MarkDirtyObjectFacet;
import org.nakedobjects.metamodel.facets.object.ident.icon.IconFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacetInferred;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.facets.object.notpersistable.InitiatedBy;
import org.nakedobjects.metamodel.facets.object.notpersistable.NotPersistableFacet;
import org.nakedobjects.metamodel.facets.ordering.OrderSet;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.java5.ImperativeFacetUtils;
import org.nakedobjects.metamodel.runtimecontext.ObjectInstantiationException;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.spec.IntrospectableSpecificationAbstract;
import org.nakedobjects.metamodel.runtimecontext.spec.feature.NakedObjectActionSet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.internal.NakedObjectActionImpl;
import org.nakedobjects.metamodel.specloader.internal.OneToManyAssociationImpl;
import org.nakedobjects.metamodel.specloader.internal.OneToOneAssociationImpl;
import org.nakedobjects.metamodel.specloader.internal.introspector.JavaIntrospector;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectAssociationPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer;
import org.nakedobjects.metamodel.util.CallbackUtils;
import org.nakedobjects.metamodel.util.SpecUtils;


public class JavaSpecification extends IntrospectableSpecificationAbstract implements DebugInfo, FacetHolder {

    private final static Logger LOG = Logger.getLogger(JavaSpecification.class);

    private class SubclassList {
        private final List<NakedObjectSpecification> classes = new ArrayList<NakedObjectSpecification>();

        public void addSubclass(final NakedObjectSpecification subclass) {
            classes.add(subclass);
        }

        public boolean hasSubclasses() {
            return !classes.isEmpty();
        }

        public NakedObjectSpecification[] toArray() {
            return classes.toArray(new NakedObjectSpecification[0]);
        }
    }

    private final SubclassList subclasses;
    private final NakedObjectReflectorAbstract reflector;

    private JavaIntrospector introspector;

    private Persistability persistable;

    private String pluralName;
    private String shortName;
    private String singularName;
    private String description;

    private NakedObjectSpecification[] interfaces;

    private IconFacet iconMethod;
    private MarkDirtyObjectFacet markDirtyObjectFacet;
    private ClearDirtyObjectFacet clearDirtyObjectFacet;
    private IsDirtyObjectFacet isDirtyObjectFacet;

    /**
     * Lazily built by {@link #getMember(Method)}.
     */
    private Hashtable<Method, NakedObjectMember> membersByMethod = null;
    private Class<?> cls;

    private boolean whetherAbstract;
    private boolean whetherFinal;
    private boolean service;

    
    // //////////////////////////////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////////////////////////////

    public JavaSpecification(
    		final Class<?> cls, 
    		final NakedObjectReflectorAbstract reflector, 
    		final RuntimeContext runtimeContext) {
    	super(runtimeContext);
        this.introspector = new JavaIntrospector(cls, this, reflector);
        this.subclasses = new SubclassList();
        this.identifier = Identifier.classIdentifier(cls);
        this.reflector = reflector;
    }


    // //////////////////////////////////////////////////////////////////////
    // Class and stuff immediately derivable from class
    // //////////////////////////////////////////////////////////////////////

    @Override
    public void addSubclass(final NakedObjectSpecification subclass) {
        subclasses.addSubclass(subclass);
    }

    @Override
    public boolean hasSubclasses() {
        return subclasses.hasSubclasses();
    }

    @Override
    public NakedObjectSpecification[] interfaces() {
        return interfaces;
    }

    @Override
    public NakedObjectSpecification[] subclasses() {
        return subclasses.toArray();
    }

    // //////////////////////////////////////////////////////////////////////
    // Hierarchical
    // //////////////////////////////////////////////////////////////////////

    /**
     * Determines if this class represents the same class, or a subclass, of the specified class.
     */
    @Override
    public boolean isOfType(final NakedObjectSpecification specification) {
        if (specification == this) {
            return true;
        } else {
            if (interfaces != null) {
                for (int i = 0, len = interfaces.length; i < len; i++) {
                    if (interfaces[i].isOfType(specification)) {
                        return true;
                    }
                }
            }
            if (superClassSpecification != null) {
                return superClassSpecification.isOfType(specification);
            }
        }
        return false;
    }

    // //////////////////////////////////////////////////////////////////////
    // introspect
    // //////////////////////////////////////////////////////////////////////

    public synchronized void introspect(final FacetDecoratorSet decorator) {
        if (introspector == null) {
            throw new ReflectionException("Introspection already taken place, cannot introspect again");
        }

        cls = introspector.getIntrospectedClass();

        final boolean skipIntrospection = JavaClassUtils.isJavaClass(cls) || isValueClass(cls);
        if (skipIntrospection) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("skipping introspection of properties and actions for " + cls.getName() + " (java.xxx class)");
        	}
        }

        introspector.introspectClass();

        fullName = introspector.getFullName();
        shortName = introspector.shortName();
        NamedFacet namedFacet = (NamedFacet) getFacet(NamedFacet.class);
        if (namedFacet == null) {
            namedFacet = new NamedFacetInferred(NameConvertorUtils.naturalName(shortName), this);
            addFacet(namedFacet);
        }

        PluralFacet pluralFacet = (PluralFacet) getFacet(PluralFacet.class);
        if (pluralFacet == null) {
            pluralFacet = new PluralFacetInferred(NameConvertorUtils.pluralName(namedFacet.value()), this);
            addFacet(pluralFacet);
        }

        whetherAbstract = introspector.isAbstract();
        whetherFinal = introspector.isFinal();

        final String superclassName = introspector.getSuperclass();
        final String[] interfaceNames = introspector.getInterfaces();

        if (skipIntrospection) {
            fields = new NakedObjectAssociation[0];
            objectActions = new NakedObjectAction[0];
            interfaces = new NakedObjectSpecification[0];
            
        } else {
            final SpecificationLoader loader = getReflector();
            if (superclassName != null) {
                superClassSpecification = loader.loadSpecification(superclassName);
                if (superClassSpecification != null) {
                	if (LOG.isDebugEnabled()) {
                		LOG.debug("  Superclass " + superclassName);
                	}
                    superClassSpecification.addSubclass(this);
                }
            }

            List<NakedObjectSpecification> interfaceSpecList = new ArrayList<NakedObjectSpecification>();
            for (int i = 0; i < interfaceNames.length; i++) {
                
                Class<?> substitutedInterfaceClass = getSubstitutedClass(interfaceNames[i], getClassSubstitutor());
                if (substitutedInterfaceClass != null) {
                    NakedObjectSpecification interfacespec = loader.loadSpecification(substitutedInterfaceClass.getName());
                    interfaceSpecList.add(interfacespec);
                    interfacespec.addSubclass(this);
                }
            }
            interfaces = interfaceSpecList.toArray(new NakedObjectSpecification[]{});

            introspector.introspectPropertiesAndCollections();

            final OrderSet orderedFields = introspector.getFields();
            if (orderedFields != null) {
                fields = orderFields(orderedFields);
            }

            introspector.introspectActions();

            OrderSet orderedActions = introspector.getClassActions();

            orderedActions = introspector.getObjectActions();
            objectActions = orderActions(orderedActions);
        }

        decorateAllFacets(decorator);

        clearDirtyObjectFacet = (ClearDirtyObjectFacet) getFacet(ClearDirtyObjectFacet.class);
        markDirtyObjectFacet = (MarkDirtyObjectFacet) getFacet(MarkDirtyObjectFacet.class);
        isDirtyObjectFacet = (IsDirtyObjectFacet) getFacet(IsDirtyObjectFacet.class);
        namedFacet = (NamedFacet) getFacet(NamedFacet.class);
        singularName = namedFacet.value();

        pluralFacet = (PluralFacet) getFacet(PluralFacet.class);
        pluralName = pluralFacet.value();

        final DescribedAsFacet describedAsFacet = (DescribedAsFacet) getFacet(DescribedAsFacet.class);
        description = describedAsFacet.value();

        iconMethod = (IconFacet) getFacet(IconFacet.class);

        final NotPersistableFacet notPersistableFacet = (NotPersistableFacet) getFacet(NotPersistableFacet.class);
        final InitiatedBy initiatedBy = notPersistableFacet.value();
        if (initiatedBy == InitiatedBy.USER_OR_PROGRAM) {
            persistable = Persistability.TRANSIENT;
        } else if (initiatedBy == InitiatedBy.USER) {
            persistable = Persistability.PROGRAM_PERSISTABLE;
        } else {
            persistable = Persistability.USER_PERSISTABLE;
        }

        // indicates have now been introspected.
        introspector = null;
    }

    private boolean isValueClass(Class<?> type) {
        return  type.getName().startsWith("org.nakedobjects.applib.value.");
    }


    /**
     * Looks up the class and runs it through the {@link #getClassSubstitutor()} obtained
     * from in the constructor.
     */
    private Class<?> getSubstitutedClass(
    		final String fullyQualifiedClassName, 
    		final ClassSubstitutor classSubstitor) {
        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return classSubstitor.getClass(interfaceClass);
    }


    /**
     * Added to try to track down a race condition. 
     */
    @Override
	public boolean isIntrospected() {
		return introspector == null;
	}



    // //////////////////////////////////////////////////////////////////////
    // Facets
    // //////////////////////////////////////////////////////////////////////

    private void decorateAllFacets(final FacetDecoratorSet decoratorSet) {
        decoratorSet.decorateAllFacets(this);
        for (int i = 0; i < fields.length; i++) {
            NakedObjectAssociation nakedObjectAssociation = fields[i];
			decoratorSet.decorateAllFacets(nakedObjectAssociation);
        }
        for (int i = 0; i < objectActions.length; i++) {
            NakedObjectAction nakedObjectAction = objectActions[i];
			decoratorSet.decorateAllFacets(nakedObjectAction);
            final NakedObjectActionParameter[] parameters = objectActions[i].getParameters();
            for (int j = 0; j < parameters.length; j++) {
                decoratorSet.decorateAllFacets(parameters[j]);
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////
    // getMember, catalog...
    // //////////////////////////////////////////////////////////////////////

    public NakedObjectMember getMember(final Method method) {
        if (membersByMethod == null) {
            membersByMethod = new Hashtable<Method, NakedObjectMember>();
            cataloguePropertiesAndCollections(membersByMethod);
            catalogueActions(membersByMethod);
        }
        return membersByMethod.get(method);
    }

    private void cataloguePropertiesAndCollections(final Hashtable<Method, NakedObjectMember> membersByMethod) {
        Filter noop = AbstractFilter.noop(NakedObjectAssociation.class);
        final NakedObjectAssociation[] fields = getAssociations(noop);
        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation field = fields[i];
            final Facet[] facets = field.getFacets(ImperativeFacet.FILTER);
            for (int j = 0; j < facets.length; j++) {
                final ImperativeFacet facet = ImperativeFacetUtils.getImperativeFacet(facets[j]);
                for(Method imperativeFacetMethod: facet.getMethods()) {
                	membersByMethod.put(imperativeFacetMethod, field);
                }
            }
        }
    }

    private void catalogueActions(final Hashtable<Method, NakedObjectMember> membersByMethod) {
        final NakedObjectAction[] userActions = getObjectActions(NakedObjectActionConstants.USER);
        for (int i = 0; i < userActions.length; i++) {
            final NakedObjectAction userAction = userActions[i];
            final Facet[] facets = userAction.getFacets(ImperativeFacet.FILTER);
            for (int j = 0; j < facets.length; j++) {
                final ImperativeFacet facet = ImperativeFacetUtils.getImperativeFacet(facets[j]);
                for(Method imperativeFacetMethod: facet.getMethods()) {
                	membersByMethod.put(imperativeFacetMethod, userAction);
                }
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////
    // getStaticallyAvailableFields, getDynamically..Fields, getField
    // //////////////////////////////////////////////////////////////////////

    public NakedObjectAssociation getAssociation(final String id) {
        // TODO put fields into hash
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getId().equals(id)) {
                return fields[i];
            }
        }
        throw new NakedObjectSpecificationException("No field called '" + id + "' in '" + getSingularName() + "'");
    }

    // //////////////////////////////////////////////////////////////////////
    // getObjectAction, getAction, getActions, getClassActions
    // //////////////////////////////////////////////////////////////////////

    public NakedObjectAction getObjectAction(
            final NakedObjectActionType type,
            final String id,
            final NakedObjectSpecification[] parameters) {
        final NakedObjectAction[] availableActions = ArrayUtils.combine(objectActions, getServiceActions(type));
        return getAction(availableActions, type, id, parameters);
    }

    public NakedObjectAction getObjectAction(final NakedObjectActionType type, final String nameParmsIdentityString) {
        final NakedObjectAction[] availableActions = ArrayUtils.combine(objectActions, getServiceActions(type));
        return getAction2(availableActions, type, nameParmsIdentityString);
    }

    private NakedObjectAction getAction(
            final NakedObjectAction[] availableActions,
            final NakedObjectActionType type,
            final String actionName,
            final NakedObjectSpecification[] parameters) {
        outer: for (int i = 0; i < availableActions.length; i++) {
            final NakedObjectAction action = availableActions[i];
            if (action.getActions().length > 0) {
                // deal with action set
                final NakedObjectAction a = getAction(action.getActions(), type, actionName, parameters);
                if (a != null) {
                    return a;
                }
            } else {
                // regular action
                if (!action.getType().equals(type)) {
                    continue outer;
                }
                if (actionName != null && !actionName.equals(action.getId())) {
                    continue outer;
                }
                if (action.getParameters().length != parameters.length) {
                    continue outer;
                }
                for (int j = 0; j < parameters.length; j++) {
                    if (!parameters[j].isOfType(action.getParameters()[j].getSpecification())) {
                        continue outer;
                    }
                }
                return action;
            }
        }
        return null;
    }

    private NakedObjectAction getAction2(
            final NakedObjectAction[] availableActions,
            final NakedObjectActionType type,
            final String nameParmsIdentityString) {
        if (nameParmsIdentityString == null) {
            return null;
        }
        outer: for (int i = 0; i < availableActions.length; i++) {
            final NakedObjectAction action = availableActions[i];
            if (action.getActions().length > 0) {
                // deal with action set
                final NakedObjectAction a = getAction2(action.getActions(), type, nameParmsIdentityString);
                if (a != null) {
                    return a;
                }
            } else {
                // regular action
                if (!sameActionTypeOrNotSpecified(type, action)) {
                    continue outer;
                }
                if (!nameParmsIdentityString.equals(action.getIdentifier().toNameParmsIdentityString())) {
                    continue outer;
                }
                return action;
            }
        }
        return null;
    }

    @Override
    protected NakedObjectAction[] getActions(final NakedObjectAction[] availableActions, final NakedObjectActionType type) {
        final List<NakedObjectAction> actions = new ArrayList<NakedObjectAction>();
        for (final NakedObjectAction action : availableActions) {
            final NakedObjectActionType actionType = action.getType();
            if (actionType == NakedObjectActionConstants.SET) {
                final NakedObjectActionSet actionSet = (NakedObjectActionSet) action;
                final NakedObjectAction[] subActions = actionSet.getActions();
                for (final NakedObjectAction subAction : subActions) {
                    if (sameActionTypeOrNotSpecified(type, subAction)) {
                        actions.add(subAction);
                        break;
                    }
                }
            } else {
                if (sameActionTypeOrNotSpecified(type, action)) {
                    actions.add(action);
                }
            }
        }

        return actions.toArray(new NakedObjectAction[0]);
    }

    private boolean sameActionTypeOrNotSpecified(final NakedObjectActionType type, final NakedObjectAction action) {
        return type == null || action.getType().equals(type);
    }

    // //////////////////////////////////////////////////////////////////////
    // orderFields, orderActions
    // //////////////////////////////////////////////////////////////////////

    private NakedObjectAssociation[] orderFields(final OrderSet order) {
        final NakedObjectAssociation[] fields = new NakedObjectAssociation[order.size()];
        final Enumeration<NakedObjectAssociation> elements = CastUtils.enumerationOver(order.elements(),
                NakedObjectAssociation.class);
        int actionCnt = 0;
        while (elements.hasMoreElements()) {
            final Object element = elements.nextElement();
            if (element instanceof JavaNakedObjectAssociationPeer) {
                final JavaNakedObjectAssociationPeer javaNakedObjectAssociationPeer = (JavaNakedObjectAssociationPeer) element;
                final NakedObjectAssociation nakedObjectAssociation = createNakedObjectField(javaNakedObjectAssociationPeer);
                fields[actionCnt++] = nakedObjectAssociation;
            } else if (element instanceof OrderSet) {
                // Not supported at present
            } else {
                throw new UnknownTypeException(element);
            }
        }

        if (actionCnt < fields.length) {
            final NakedObjectAssociation[] actualActions = new NakedObjectAssociation[actionCnt];
            System.arraycopy(fields, 0, actualActions, 0, actionCnt);
            return actualActions;
        }
        return fields;
    }

    private NakedObjectAction[] orderActions(final OrderSet order) {
        final NakedObjectAction[] actions = new NakedObjectAction[order.size()];
        final Enumeration<NakedObjectAction> elements = CastUtils.enumerationOver(order.elements(), NakedObjectAction.class);
        int actionCnt = 0;
        while (elements.hasMoreElements()) {
            final Object element = elements.nextElement();
            if (element instanceof JavaNakedObjectActionPeer) {
                final JavaNakedObjectActionPeer javaNakedObjectActionPeer = (JavaNakedObjectActionPeer) element;
                final String actionId = javaNakedObjectActionPeer.getIdentifier().getMemberName();
                final NakedObjectAction nakedObjectAction = new NakedObjectActionImpl(actionId, javaNakedObjectActionPeer, getRuntimeContext());
                actions[actionCnt++] = nakedObjectAction;
            } else if (element instanceof OrderSet) {
                final OrderSet set = ((OrderSet) element);
                actions[actionCnt++] = new NakedObjectActionSet("", set.getGroupFullName(), orderActions(set), getRuntimeContext());
            } else {
                throw new UnknownTypeException(element);
            }
        }

        if (actionCnt < actions.length) {
            final NakedObjectAction[] actualActions = new NakedObjectAction[actionCnt];
            System.arraycopy(actions, 0, actualActions, 0, actionCnt);
            return actualActions;
        }
        return actions;
    }

    private NakedObjectAssociation createNakedObjectField(final NakedObjectAssociationPeer peer) {
        NakedObjectAssociation field;
        if (peer.isOneToOne()) {
            field = new OneToOneAssociationImpl(peer, getRuntimeContext());

        } else if (peer.isOneToMany()) {
            field = new OneToManyAssociationImpl(peer, getRuntimeContext());

        } else {
            throw new NakedObjectException();
        }
        return field;
    }

    @Override
    public boolean isCollectionOrIsAggregated() {
        return isCollection() || isValueOrIsAggregated();
    }

    @Override
    public boolean isAbstract() {
        return whetherAbstract;
    }

    @Override
    public boolean isFinal() {
        return whetherFinal;
    }

    @Override
    public boolean isService() {
        return service;
    }

    public String getShortName() {
        return shortName;
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    private TitleFacet titleFacet;

    public String getTitle(final NakedObject object) {
        if (titleFacet == null) {
            titleFacet = (TitleFacet) getFacet(TitleFacet.class);
        }
        if (titleFacet != null) {
            final String titleString = titleFacet.title(object);
            if (titleString != null && !titleString.equals("")) {
                return titleString;
            }
        }
        return (this.isService() ? "" : "Untitled ") + getSingularName();
    }

    @Override
    public String getIconName(final NakedObject reference) {
        if (iconMethod == null) {
            return null;
        } else {
            return iconMethod.iconName(reference);
        }
    }

    @Override
    public Persistability persistability() {
        return persistable;
    }

    // //////////////////////////////////////////////////////////////////////
    // createObject
    // //////////////////////////////////////////////////////////////////////

    @Override
    public Object createObject(CreationMode creationMode) {
        if (cls.isArray()) {
            return Array.newInstance(cls.getComponentType(), 0);
        }
        
        try {
            Object object = getRuntimeContext().instantiate(cls);
            
            if (creationMode == CreationMode.INITIALIZE) {
                final NakedObject adapter = getRuntimeContext().adapterFor(object);
                
                // initialize new object
                final NakedObjectAssociation[] fields = adapter.getSpecification().getAssociations();
        		for (int i = 0; i < fields.length; i++) {
        		    fields[i].toDefault(adapter);
        		}
        		getRuntimeContext().injectDependenciesInto(object);
        		
        		CallbackUtils.callCallback(adapter, CreatedCallbackFacet.class);
            }
			return object;
        } catch (final ObjectInstantiationException e) {
            throw new NakedObjectException("Failed to create instance of type " + cls.getName(), e);
        }
    }

    @Override
    public boolean isDirty(final NakedObject object) {
        return isDirtyObjectFacet == null ? false : isDirtyObjectFacet.invoke(object);
    }

    @Override
    public void clearDirty(final NakedObject object) {
        if (clearDirtyObjectFacet != null) {
            clearDirtyObjectFacet.invoke(object);
        }
    }

    @Override
    public void markDirty(final NakedObject object) {
        if (markDirtyObjectFacet != null) {
            markDirtyObjectFacet.invoke(object);
        }
    }

    // //////////////////////////////////////////////////////////////////////
    // markAsService
    // //////////////////////////////////////////////////////////////////////

    /**
     * TODO: should ensure that service has at least one user action; fix when specification knows of its
     * hidden methods.
     * 
     * <pre>
     * if (objectActions != null &amp;&amp; objectActions.length == 0) {
     *     throw new NakedObjectSpecificationException(&quot;Service object &quot; + getFullName() + &quot; should have at least one user action&quot;);
     * }
     * </pre>
     */
    public void markAsService() {
        final NakedObjectAssociation[] fields = getAssociations();
        if (fields != null && fields.length > 0) {
            String fieldNames = "";
            for (int i = 0; i < fields.length; i++) {
                final String name = fields[i].getId();
                if ("id".indexOf(name) == -1) {
                    fieldNames += (fieldNames.length() > 0 ? ", " : "") + name;
                }
            }
            if (fieldNames.length() > 0) {
                throw new NakedObjectSpecificationException("Service object " + getFullName()
                        + " should have no fields, but has: " + fieldNames);
            }
        }

        service = true;
    }



    // //////////////////////////////////////////////////////////////////////
    // Debug, toString
    // //////////////////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.blankLine();
        debug.appendln("Title", getFacet(TitleFacet.class));
        if (iconMethod != null) {
            debug.appendln("Icon", iconMethod);
        }
        debug.unindent();
    }

    public String debugTitle() {
        return "NO Member Specification";
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("class", fullName);
        str.append("type", SpecUtils.typeNameFor(this));
        str.append("persistable", persistable);
        str.append("superclass", superClassSpecification == null ? "Object" : superClassSpecification.getFullName());
        return str.toString();
    }



    // //////////////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    // //////////////////////////////////////////////////////////////////

    private NakedObjectReflectorAbstract getReflector() {
        return reflector;
    }

    /**
     * Derived from {@link #getReflector()}
     */
    private ClassSubstitutor getClassSubstitutor() {
        return reflector.getClassSubstitutor();
    }





}
// Copyright (c) Naked Objects Group Ltd.
