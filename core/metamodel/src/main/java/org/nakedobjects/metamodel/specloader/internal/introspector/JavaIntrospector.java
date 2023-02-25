package org.nakedobjects.metamodel.specloader.internal.introspector;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.JavaClassUtils;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.commons.names.NameConvertorUtils;
import org.nakedobjects.metamodel.commons.names.NameUtils;
import org.nakedobjects.metamodel.exceptions.ReflectionException;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.object.facets.FacetsFacet;
import org.nakedobjects.metamodel.facets.ordering.OrderSet;
import org.nakedobjects.metamodel.facets.ordering.SimpleOrderSet;
import org.nakedobjects.metamodel.facets.ordering.actionorder.ActionOrderFacet;
import org.nakedobjects.metamodel.facets.ordering.fieldorder.FieldOrderFacet;
import org.nakedobjects.metamodel.facets.ordering.memberorder.DeweyOrderSet;
import org.nakedobjects.metamodel.spec.JavaSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.internal.facetprocessor.FacetProcessor;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionParamPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaOneToManyAssociationPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaOneToOneAssociationPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectAssociationPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectMemberPeer;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;


public class JavaIntrospector {

    private final class JavaIntrospectorMethodRemover implements MethodRemover {

        public void removeMethod(
                final MethodScope methodScope,
                final String methodName,
                final Class<?> returnType,
                final Class<?>[] parameterTypes) {
            MethodFinderUtils.removeMethod(
            		methods, methodScope, methodName, returnType, parameterTypes);
        }

        public List<Method> removeMethods(
                final MethodScope methodScope,
                final String prefix,
                final Class<?> returnType,
                final boolean canBeVoid,
                final int paramCount) {
            return MethodFinderUtils.removeMethods(methods, methodScope, prefix, returnType, canBeVoid, paramCount);
        }

        public void removeMethod(final Method method) {
            for (int i = 0; i < methods.length; i++) {
                if (methods[i] == null) {
                    continue;
                }
                if (methods[i].equals(method)) {
                    methods[i] = null;
                }
            }
        }

        public void removeMethods(final List<Method> methodList) {
            final List<Method> methodList2 = methodList;
            for (int i = 0; i < methods.length; i++) {
                if (methods[i] == null) {
                    continue;
                }
                for (final Method method : methodList2) {
                    if (methods[i].equals(method)) {
                        methods[i] = null;
                        break;
                    }
                }
            }
        }
    }

    private static final Logger LOG = Logger.getLogger(JavaIntrospector.class);

    private static final Object[] NO_PARAMETERS = new Object[0];
    private static final Class<?>[] NO_PARAMETERS_TYPES = new Class[0];

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    private final String className;
    private final Class<?> type;
    private final Method methods[];

    private OrderSet orderedFields;
    private OrderSet orderedObjectActions;
    private OrderSet orderedClassActions;

    private final NakedObjectReflectorAbstract reflector;
    private final JavaSpecification javaSpecification;

    // ////////////////////////////////////////////////////////////////////////////
    // Constructor
    // ////////////////////////////////////////////////////////////////////////////

    public JavaIntrospector(final Class<?> type, 
    		final JavaSpecification javaSpecification, 
    		final NakedObjectReflectorAbstract reflector) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("creating JavaIntrospector for " + type);
    	}

        // String prefix = "java.util.Arrays$";
        
        @SuppressWarnings("unused")
		final Class<?> classComponentType = type.getComponentType();
        

        this.type = type;
        this.javaSpecification = javaSpecification;
        this.reflector = reflector;


        // previously we tested that the adapted class was public, as in
        // !JavaClassUtils.isPublic(cls). However, there doesn't seem to be any
        // good reason to have this restriction, while having it prevents us from
        // using third party libraries for value types that have non-public
        // interfaces or non-public concrete implementations. Therefore the test
        // has been removed.

        methods = type.getMethods();
        className = type.getName();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Introspection Control Parameters
    // ////////////////////////////////////////////////////////////////////////////

    private SpecificationTraverser getSpecificationTraverser() {
    	return reflector.getSpecificationTraverser();
    }
    
    private FacetProcessor getFacetProcessor() {
        return reflector.getFacetProcessor();
    }

    private ClassSubstitutor getClassSubstitutor() {
        return reflector.getClassSubstitutor();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Class and stuff immediately derived from class
    // ////////////////////////////////////////////////////////////////////////////

    public Class<?> getIntrospectedClass() {
        return type;
    }

    /**
     * As per {@link Class#getName()}.
     */
    String className() {
        return className;
    }

    public String getFullName() {
        return type.getName();
    }

    public String[] getInterfaces() {
        return JavaClassUtils.getInterfaces(type);
    }

    public String getSuperclass() {
        return JavaClassUtils.getSuperclass(type);
    }

    public boolean isAbstract() {
        return JavaClassUtils.isAbstract(type);
    }

    public boolean isFinal() {
        return JavaClassUtils.isFinal(type);
    }

    public String shortName() {
        final String name = type.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }


    // ////////////////////////////////////////////////////////////////////////////
    // introspect
    // ////////////////////////////////////////////////////////////////////////////

    public void introspectClass() {
        LOG.info("introspecting " + className());
        if (LOG.isDebugEnabled()) {
        	LOG.debug("introspecting " + className() + ": class-level details");
        }

        // process facets at object level
        // this will also remove some methods, such as the superclass methods.
        final JavaIntrospectorMethodRemover methodRemover = new JavaIntrospectorMethodRemover();
        getFacetProcessor().process(type, methodRemover, javaSpecification);

        // if this class has additional facets (as per @Facets), then process them.
        final FacetsFacet facetsFacet = (FacetsFacet) javaSpecification.getFacet(FacetsFacet.class);
        if (facetsFacet != null) {
            final Class<? extends FacetFactory>[] facetFactories = facetsFacet.facetFactories();
            for (int i = 0; i < facetFactories.length; i++) {
                FacetFactory facetFactory = null;
                try {
                    facetFactory = (FacetFactory) facetFactories[i].newInstance();
                } catch (final InstantiationException e) {
                    throw new NakedObjectException(e);
                } catch (final IllegalAccessException e) {
                    throw new NakedObjectException(e);
                }
                getFacetProcessor().injectDependenciesInto(facetFactory);
                facetFactory.process(type, methodRemover, javaSpecification);
            }
        }
    }

    public void introspectPropertiesAndCollections() {
        LOG.debug("introspecting " + className() + ": properties and collections");

        // find the properties and collections (fields) ...
        final NakedObjectAssociationPeer[] findFieldMethods = findAndCreateFieldPeers();

        // ... and the ordering of the properties and collections
        String fieldOrder;
        final FieldOrderFacet fieldOrderFacet = (FieldOrderFacet) javaSpecification.getFacet(FieldOrderFacet.class);
        if (fieldOrderFacet == null) {
            fieldOrder = null;
        } else {
            fieldOrder = fieldOrderFacet.value();
        }

        if (fieldOrder == null) {
            // TODO: the calling of fieldOrder() should be a facet
            fieldOrder = invokeSortOrderMethod("field");
        }
        orderedFields = createOrderSet(fieldOrder, findFieldMethods);
    }

    public void introspectActions() {
        LOG.debug("introspecting " + className() + ": actions");

        // find the actions ...
        final NakedObjectActionPeer[] actionPeers1 = findActionMethodPeers(MethodScope.OBJECT, true);
        final NakedObjectActionPeer[] actionPeers2 = findActionMethodPeers(MethodScope.OBJECT, false);
        final NakedObjectActionPeer[] actionPeers = new NakedObjectActionPeer[actionPeers1.length + actionPeers2.length];
        System.arraycopy(actionPeers1, 0, actionPeers, 0, actionPeers1.length);
        System.arraycopy(actionPeers2, 0, actionPeers, actionPeers1.length, actionPeers2.length);


        // ... and the ordering of actions ...
        String actionOrder;
        final ActionOrderFacet actionOrderFacet = (ActionOrderFacet) javaSpecification.getFacet(ActionOrderFacet.class);
        if (actionOrderFacet == null) {
            actionOrder = null;
        } else {
            actionOrder = actionOrderFacet.value();
        }
        if (actionOrder == null) {
            // TODO: the calling of actionOrder() should be a facet
            actionOrder = invokeSortOrderMethod("action");
        }
        orderedObjectActions = createOrderSet(actionOrder, actionPeers);

        // find the class actions ...
        final NakedObjectActionPeer[] findClassActionMethods1 = findActionMethodPeers(MethodScope.CLASS, true);
        final NakedObjectActionPeer[] findClassActionMethods2 = findActionMethodPeers(MethodScope.CLASS, false);
        final NakedObjectActionPeer[] findClassActionMethods = new NakedObjectActionPeer[findClassActionMethods1.length + findClassActionMethods2.length];
        System.arraycopy(findClassActionMethods1, 0, findClassActionMethods, 0, findClassActionMethods1.length);
        System.arraycopy(findClassActionMethods2, 0, findClassActionMethods, findClassActionMethods1.length, findClassActionMethods2.length);

        // ... and the ordering of class actions
        actionOrder = null;
        if (actionOrder == null) {
            // TODO: the calling of classActionOrder() should be a facet
            actionOrder = invokeSortOrderMethod("classAction");
        }
        orderedClassActions = createOrderSet(actionOrder, findClassActionMethods);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // find Properties and Collections (fields)
    // ////////////////////////////////////////////////////////////////////////////

    private NakedObjectAssociationPeer[] findAndCreateFieldPeers() {
        LOG.debug("  looking for fields for " + type);

        // Ensure all return types are known
        final Set<Method> propertyOrCollectionCandidates = getFacetProcessor().findPropertyOrCollectionCandidateAccessors(
                methods, new HashSet<Method>());
        
        List<Class<?>> typesToLoad = new ArrayList<Class<?>>();
		for (final Method method : propertyOrCollectionCandidates) {
			getSpecificationTraverser().traverseTypes(method, typesToLoad);
		}
		reflector.loadSpecifications(typesToLoad, type);

        // now create FieldPeers for value properties, for collections and for reference properties
        final List<NakedObjectAssociationPeer> fieldPeers = new ArrayList<NakedObjectAssociationPeer>();

        findAndRemoveCollectionAccessorsAndCreateCorrespondingFieldPeers(fieldPeers);
        findAndRemovePropertyAccessorsAndCreateCorrespondingFieldPeers(fieldPeers);

        return toArray(fieldPeers);
    }

    private NakedObjectAssociationPeer[] toArray(final List<NakedObjectAssociationPeer> fields) {
        return (NakedObjectAssociationPeer[]) fields.toArray(new NakedObjectAssociationPeer[] {});
    }

    private void findAndRemoveCollectionAccessorsAndCreateCorrespondingFieldPeers(final List<NakedObjectAssociationPeer> associationPeers) {
        final List<Method> collectionAccessors = new ArrayList<Method>();
        getFacetProcessor().findAndRemoveCollectionAccessors(new JavaIntrospectorMethodRemover(), collectionAccessors);
        createCollectionPeersFromAccessors(collectionAccessors, associationPeers);
    }

    /**
     * Since the value properties and collections have already been processed, this will pick up the remaining
     * reference properties.
     */
    private void findAndRemovePropertyAccessorsAndCreateCorrespondingFieldPeers(final List<NakedObjectAssociationPeer> fields) {
        final List<Method> propertyAccessors = new ArrayList<Method>();
        getFacetProcessor().findAndRemovePropertyAccessors(new JavaIntrospectorMethodRemover(), propertyAccessors);

        findAndRemovePrefixedNonVoidMethods(MethodScope.OBJECT, GET_PREFIX, Object.class, 0, propertyAccessors);
        findAndRemovePrefixedNonVoidMethods(MethodScope.OBJECT, IS_PREFIX, Boolean.class, 0, propertyAccessors);

        createPropertyPeersFromAccessors(propertyAccessors, fields);
    }

    private void createCollectionPeersFromAccessors(final List<Method> collectionAccessors, final List<NakedObjectAssociationPeer> associationPeerListToAppendto) {
        for (final Method getMethod : collectionAccessors) {
            LOG.debug("  identified one-many association method " + getMethod);
            final String capitalizedName = NameUtils.javaBaseName(getMethod.getName());
            final String collectionNameName = Introspector.decapitalize(capitalizedName);

            final Identifier identifier = Identifier.propertyOrCollectionIdentifier(className(), collectionNameName);

            // create property and add facets
            final JavaOneToManyAssociationPeer collection = new JavaOneToManyAssociationPeer(identifier, getSpecificationLoader());
            getFacetProcessor().process(type, getMethod, new JavaIntrospectorMethodRemover(), collection,
                    NakedObjectFeatureType.COLLECTION);

            // figure out what the type is
            Class<?> elementType = Object.class;
            final TypeOfFacet typeOfFacet = collection.getFacet(TypeOfFacet.class);
            if (typeOfFacet != null) {
                elementType = typeOfFacet.value();
            }
            collection.setElementType(elementType);
            
            // skip if class strategy says so.
            if (getClassSubstitutor().getClass(elementType) == null) {
                continue;
            }

            associationPeerListToAppendto.add(collection);
        }
    }

    private void createPropertyPeersFromAccessors(final List<Method> propertyAccessors, final List<NakedObjectAssociationPeer> associationPeerListToAppendto)
            throws ReflectionException {

        for (final Method accessorMethod : propertyAccessors) {
            LOG.debug("  identified 1-1 association method " + accessorMethod);

            final String capitalizedName = NameUtils.javaBaseName(accessorMethod.getName());
            final String beanName = Introspector.decapitalize(capitalizedName);
            final Class<?> returnType = accessorMethod.getReturnType();
            
            // skip if class strategy says so.
            if (getClassSubstitutor().getClass(returnType) == null) {
                continue;
            }

            if (LOG.isDebugEnabled()) {
            	LOG.debug("one-to-one association " + capitalizedName + " ->" + accessorMethod);
            }
            final Identifier identifier = Identifier.propertyOrCollectionIdentifier(className, beanName);

            // create a 1:1 association peer
            final JavaOneToOneAssociationPeer associationPeer = new JavaOneToOneAssociationPeer(identifier, returnType, getSpecificationLoader());

            // process facets for the 1:1 association
            getFacetProcessor().process(type, accessorMethod, new JavaIntrospectorMethodRemover(), associationPeer,
                    NakedObjectFeatureType.PROPERTY);

            associationPeerListToAppendto.add(associationPeer);
        }
    }

    // ////////////////////////////////////////////////////////////////////////////
    // find Actions
    // ////////////////////////////////////////////////////////////////////////////

    private NakedObjectActionPeer[] findActionMethodPeers(final MethodScope methodScope, boolean skipRecognisedHelperMethod) {
    	if(LOG.isDebugEnabled()) {
    		LOG.debug("  looking for action methods");
    	}

        final List<NakedObjectActionPeer> actionPeers = new ArrayList<NakedObjectActionPeer>();

        eachMethod:
        for (int i = 0; i < methods.length; i++) {
            if (methods[i] == null) {
                continue;
            }
            final Method actionMethod = methods[i];
            if (!MethodFinderUtils.inScope(methodScope, actionMethod)) {
                continue;
            }

            List<Class<?>> typesToLoad = new ArrayList<Class<?>>();
            getSpecificationTraverser().traverseTypes(actionMethod, typesToLoad);

            boolean anyLoadedAsNull = reflector.loadSpecifications(typesToLoad);
            if (anyLoadedAsNull) {
                continue;
            }
            

            // build/validate action parameters
            // as for return type, if the reflector's class strategy says to skip any of the 
            // action's parameter types, then just ignore this action altogether.
            final Class<?>[] parameterTypes = actionMethod.getParameterTypes();
            
            // previously we wrapped primitives.  However, this prevents the lookup of
            // actions during remote authorization calls (using Identifier class).
            // ... should we remove ... ?
            // final Class<?>[] parameterClasses = WrapperUtils.wrapAsNecessary(parameterTypes);
            
            final Class<?>[] parameterClasses = parameterTypes;
            
            final int numParameters = parameterClasses.length;
            final JavaNakedObjectActionParamPeer[] actionParams = new JavaNakedObjectActionParamPeer[numParameters];

            for (int j = 0; j < numParameters; j++) {
                NakedObjectSpecification paramSpec = getSpecificationLoader().loadSpecification(parameterClasses[j]);
                if (paramSpec == null) {
                    continue eachMethod;
                }
                actionParams[j] = new JavaNakedObjectActionParamPeer(paramSpec);
            }
            

            if (getFacetProcessor().recognizes(actionMethod)) {
                if (actionMethod.getName().startsWith("set")) {
                  continue;
                }
                if (skipRecognisedHelperMethod) {
                    LOG.info("  skipping possible helper method " + actionMethod);
                    continue;
                }
            }

            if (LOG.isDebugEnabled()) {
            	LOG.debug("  identified action " + actionMethod);
            }
            methods[i] = null;


            // build action
            final String fullMethodName = actionMethod.getName();
            final Identifier identifier = Identifier.actionIdentifier(className, fullMethodName, parameterTypes);
            final JavaNakedObjectActionPeer action = new JavaNakedObjectActionPeer(identifier, actionParams);

            // process facets on the action & parameters
            getFacetProcessor()
                    .process(type, actionMethod, new JavaIntrospectorMethodRemover(), action, NakedObjectFeatureType.ACTION);
            for (int j = 0; j < actionParams.length; j++) {
                getFacetProcessor().processParams(actionMethod, j, actionParams[j]);
            }

            actionPeers.add(action);
        }

        return convertToArray(actionPeers);
    }

	private NakedObjectActionPeer[] convertToArray(final List<NakedObjectActionPeer> actions) {
        return (NakedObjectActionPeer[]) actions.toArray(new NakedObjectActionPeer[] {});
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Helpers for finding and removing methods.
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * Searches for specific method and returns it, also removing it from the {@link #methods array of
     * methods} if found.
     * 
     * @see MethodFinderUtils#removeMethod(Method[], boolean, String, Class, Class[])
     */
    private Method findAndRemoveMethod(
            final MethodScope methodScope,
            final String name,
            final Class<?> returnType,
            final Class<?>[] paramTypes) {
        return MethodFinderUtils.removeMethod(methods, methodScope, name, returnType, paramTypes);
    }

    /**
     * Searches for all methods matching the prefix and returns them, also removing it from the
     * {@link #methods array of methods} if found.
     * 
     * <p>
     * Void methods are not allowed.
     * 
     * @see MethodFinderUtils#removeMethods(Method[], boolean, String, Class, boolean, int, ClassSubstitutor)
     */
    private List<Method> findAndRemovePrefixedNonVoidMethods(
            final MethodScope methodScope,
            final String prefix,
            final Class<?> returnType,
            final int paramCount) {
        return findAndRemovePrefixedMethods(methodScope, prefix, returnType, false, paramCount);
    }

    /**
     * As per {@link #findAndRemovePrefixedNonVoidMethods(boolean, String, Class, int)}, but appends to
     * provided {@link List} (collecting parameter pattern).
     */
    private void findAndRemovePrefixedNonVoidMethods(
            final MethodScope methodScope,
            final String prefix,
            final Class<?> returnType,
            final int paramCount,
            final List<Method> methodListToAppendTo) {
        final List<Method> matchingMethods = findAndRemovePrefixedMethods(methodScope, prefix, returnType, false, paramCount);
        methodListToAppendTo.addAll(matchingMethods);
    }

    /**
     * Searches for all methods matching the prefix and returns them, also removing it from the
     * {@link #methods array of methods} if found.
     * @param objectFactory 
     * 
     * @see MethodFinderUtils#removeMethods(Method[], boolean, String, Class, boolean, int, ClassSubstitutor)
     */
    private List<Method> findAndRemovePrefixedMethods(
            final MethodScope methodScope,
            final String prefix,
            final Class<?> returnType,
            final boolean canBeVoid,
            final int paramCount) {
        return MethodFinderUtils.removeMethods(methods, methodScope, prefix, returnType, canBeVoid, paramCount);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Sort Member Peers
    // ////////////////////////////////////////////////////////////////////////////

    private OrderSet createOrderSet(final String order, final NakedObjectMemberPeer[] members) {
        if (order != null) {
            OrderSet set;
            set = SimpleOrderSet.createOrderSet(order, members);
            return set;
        } else {
            final OrderSet set = DeweyOrderSet.createOrderSet(members);
            return set;
        }
    }

    private String invokeSortOrderMethod(final String type) {
        final Method method = findAndRemoveMethod(MethodScope.CLASS, type + "Order", String.class, NO_PARAMETERS_TYPES);
        if (method == null) {
            return null;
        } else if (!JavaClassUtils.isStatic(method)) {
            LOG.warn("method " + className + "." + type + "Order() must be declared as static");
            return null;
        } else {
            String s;
            s = (String) invokeMethod(method, NO_PARAMETERS);
            if (s.trim().length() == 0) {
                return null;
            }
            return s;
        }
    }

    private Object invokeMethod(final Method method, final Object[] parameters) {
        try {
            return method.invoke(null, parameters);
        } catch (final IllegalAccessException ignore) {
            LOG.warn("method " + className + "." + method.getName() + "() must be declared as public");
            return null;
        } catch (final InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Resultant Members
    // ////////////////////////////////////////////////////////////////////////////

    public OrderSet getFields() {
        return orderedFields;
    }

    public OrderSet getClassActions() {
        return orderedClassActions;
    }

    public OrderSet getObjectActions() {
        return orderedObjectActions;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // finalize
    // ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOG.debug("finalizing reflector " + this);
    }


    // ////////////////////////////////////////////////////////////////////////////
    // (not used) - although it should be be Field and ActionOrder annotations
    // ////////////////////////////////////////////////////////////////////////////

    /*
     * REVIEW should we still support ordering of properties/actions via a string (this
     * could allow user specified ordered rather than coder based
     * 
     * Also a preference issue - it is clearer to read the order from an order string from the @FieldOrder and @MemberOrder although it is  more 
     * difficult to ensure validity.
     */
    /**
     * This was pulled out of JavaIntrospector, but doesn't seem to be used any more.
     * 
     * <p>
     * Have therefore made private.
     */
    private static NakedObjectAssociationPeer[] orderArray(final NakedObjectAssociationPeer[] original, final String[] order) {
        if (order == null) {
            return original;

        } else {
            for (int i = 0; i < order.length; i++) {
                order[i] = NameConvertorUtils.simpleName(order[i]);
            }

            final NakedObjectAssociationPeer[] ordered = new NakedObjectAssociationPeer[original.length];

            // work through each order element and find, if there is one, a
            // matching member.
            int orderedIndex = 0;
            ordering: for (int orderIndex = 0; orderIndex < order.length; orderIndex++) {
                for (int memberIndex = 0; memberIndex < original.length; memberIndex++) {
                    final NakedObjectAssociationPeer member = original[memberIndex];
                    if (member == null) {
                        continue;
                    }
                    if (member.getIdentifier().getMemberName().equalsIgnoreCase(order[orderIndex])) {
                        ordered[orderedIndex++] = original[memberIndex];
                        original[memberIndex] = null;

                        continue ordering;
                    }
                }

                if (!order[orderIndex].trim().equals("")) {
                    LOG.warn("invalid ordering element '" + order[orderIndex]);
                }
            }

            final NakedObjectAssociationPeer[] results = new NakedObjectAssociationPeer[original.length];
            int index = 0;
            for (int i = 0; i < ordered.length; i++) {
                final NakedObjectAssociationPeer member = ordered[i];
                if (member != null) {
                    results[index++] = member;
                }
            }
            for (int i = 0; i < original.length; i++) {
                final NakedObjectAssociationPeer member = original[i];
                if (member != null) {
                    results[index++] = member;
                }
            }

            return results;
        }
    }

    
    // ////////////////////////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    // ////////////////////////////////////////////////////////////////////////////
    
    private SpecificationLoader getSpecificationLoader() {
        return reflector;
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("class", className);
        return str.toString();
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
