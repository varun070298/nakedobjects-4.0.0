package org.nakedobjects.metamodel.facets.properties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.names.NameUtils;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.MethodScope;
import org.nakedobjects.metamodel.facets.actions.MandatoryFacetOverriddenByMethod;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAlways;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacetViaAccessor;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacet;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacetInferred;
import org.nakedobjects.metamodel.facets.properties.choices.PropertyChoicesFacetViaMethod;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacetViaMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetViaClearMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyInitializationFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetViaModifyMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.validate.PropertyValidateFacetViaMethod;
import org.nakedobjects.metamodel.java5.PropertyOrCollectionIdentifyingFacetFactoryAbstract;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.util.InvokeUtils;


public class PropertyMethodsFacetFactory extends PropertyOrCollectionIdentifyingFacetFactoryAbstract implements RuntimeContextAware {

    private static final Logger LOG = Logger.getLogger(PropertyMethodsFacetFactory.class);

    protected static final String CLEAR_PREFIX = "clear";
    protected static final String GET_PREFIX = "get";
    protected static final String IS_PREFIX = "is";
    protected static final String MODIFY_PREFIX = "modify";
    protected static final String SET_PREFIX = "set";
    private static final String OPTIONAL_PREFIX = "optional";

    private static final String[] PREFIXES = { CLEAR_PREFIX, IS_PREFIX, GET_PREFIX, MODIFY_PREFIX, SET_PREFIX, OPTIONAL_PREFIX, };

	private RuntimeContext runtimeContext;

    public PropertyMethodsFacetFactory() {
        super(PREFIXES, NakedObjectFeatureType.PROPERTIES_ONLY);
    }

    @Override
    public boolean process(Class<?> cls, final Method getMethod, final MethodRemover methodRemover, final FacetHolder property) {

        final String capitalizedName = NameUtils.javaBaseName(getMethod.getName());
        final Class<?> returnType = getMethod.getReturnType();
        final Class<?>[] paramTypes = new Class[] { returnType };

        final List<Facet> facets = new ArrayList<Facet>();

        removeMethod(methodRemover, getMethod);
        facets.add(new PropertyAccessorFacetViaAccessor(getMethod, property));

        final Method setMethod = findAndRemoveSetterMethod(facets, methodRemover, cls, capitalizedName, paramTypes, property);
        findAndRemoveModifyMethod(facets, methodRemover, cls, capitalizedName, paramTypes, property);

        final Method clearMethod = findAndRemoveClearMethod(facets, methodRemover, cls, capitalizedName, paramTypes, property);
        ensureClearViaSetterIfRequired(facets, property, clearMethod, setMethod);

        findAndRemoveChoicesMethod(facets, methodRemover, cls, capitalizedName, returnType, property);
        findAndRemoveDefaultMethod(facets, methodRemover, cls, capitalizedName, returnType, property);
        findAndRemoveValidateMethod(facets, methodRemover, cls, paramTypes, capitalizedName, returnType, property);

        findAndRemoveNameMethod(facets, methodRemover, cls, capitalizedName, property);
        findAndRemoveDescriptionMethod(facets, methodRemover, cls, capitalizedName, property);

        findAndRemoveAlwaysHideMethod(facets, methodRemover, cls, capitalizedName, property);
        findAndRemoveProtectMethod(facets, methodRemover, cls, capitalizedName, property);
        findAndRemoveOptionalMethod(facets, methodRemover, cls, capitalizedName, returnType, property);

        findAndRemoveHideForSessionMethod(facets, methodRemover, cls, capitalizedName, paramTypes, property);
        findAndRemoveDisableForSessionMethod(facets, methodRemover, cls, capitalizedName, paramTypes, property);
        findAndRemoveHideMethod(facets, methodRemover, cls, OBJECT, capitalizedName, paramTypes, property);
        findAndRemoveHideMethod(facets, methodRemover, cls, OBJECT, capitalizedName, new Class[] {}, property);
        findAndRemoveDisableMethod(facets, methodRemover, cls, OBJECT, capitalizedName, paramTypes, property);
        findAndRemoveDisableMethod(facets, methodRemover, cls, OBJECT, capitalizedName, new Class[] {}, property);

        return FacetUtil.addFacets(facets);
    }

    /**
     * Sets up the {@link PropertySetterFacetViaSetterMethod} to invoke the property's setter if available,
     * but if none then marks the property as {@link DerivedFacet derived} and {@link DisabledFacet disabled}
     * otherwise.
     */
    private Method findAndRemoveSetterMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String capitalizedName,
            final Class<?>[] params,
            final FacetHolder property) {
        Method method = findMethod(cls, OBJECT, SET_PREFIX + capitalizedName, void.class, params);
        removeMethod(methodRemover, method);

        if (method != null) {
            propertyFacets.add(new PropertySetterFacetViaSetterMethod(method, property));
            propertyFacets.add(new PropertyInitializationFacetViaSetterMethod(method, property));
        } else {
            propertyFacets.add(new DerivedFacetInferred(property));
            propertyFacets.add(new DisabledFacetAlways(property));
        }

        return method;
    }

    private Method findAndRemoveModifyMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String capitalizedName,
            final Class<?>[] params,
            final FacetHolder property) {
        final Method method = findMethod(cls, OBJECT, MODIFY_PREFIX + capitalizedName, void.class, params);
        removeMethod(methodRemover, method);
        if (method != null) {
            propertyFacets.add(new PropertySetterFacetViaModifyMethod(method, property));
        }
        return method;
    }

    private Method findAndRemoveClearMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String capitalizedName,
            final Class<?>[] params,
            final FacetHolder property) {
        final Method method = findMethod(cls, OBJECT, CLEAR_PREFIX + capitalizedName, void.class, NO_PARAMETERS_TYPES);
        removeMethod(methodRemover, method);
        if (method != null) {
            propertyFacets.add(new PropertyClearFacetViaClearMethod(method, property));
        }
        return method;
    }

    private void ensureClearViaSetterIfRequired(
            final List<Facet> propertyFacets,
            final FacetHolder property,
            final Method clearMethod,
            final Method setMethod) {
        if (setMethod != null && clearMethod == null) {
            propertyFacets.add(new PropertyClearFacetViaSetterMethod(setMethod, property));
        }
    }

    private void findAndRemoveOptionalMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String name,
            final Class<?> returnType,
            final FacetHolder property) {
        boolean isOptional = false;

        final Method method = findMethod(cls, CLASS, OPTIONAL_PREFIX + name, boolean.class, NO_PARAMETERS_TYPES);
        removeMethod(methodRemover, method);
        if (method != null) {
            final Boolean optionalMethodReturnValue = (Boolean) InvokeUtils.invoke(method, new Object[0]);
            isOptional = optionalMethodReturnValue.booleanValue() | isOptional;
        }

        if (!isOptional) {
            return;
        }
        if (returnType.isPrimitive()) {
            LOG.warn(cls.getName() + "#" + name + " cannot be optional as it is a primitive; request ignored");
            return;
        }
        propertyFacets.add(new MandatoryFacetOverriddenByMethod(property));
    }

    private void findAndRemoveValidateMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final Class<?>[] params,
            final String capitalizedName,
            final Class<?> returnType,
            final FacetHolder property) {
        final Method method = findMethod(cls, OBJECT, VALIDATE_PREFIX + capitalizedName, String.class, params);
        removeMethod(methodRemover, method);
        if (method == null) {
            return;
        }
        propertyFacets.add(new PropertyValidateFacetViaMethod(method, property));
    }

    private void findAndRemoveDefaultMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String capitalizedName,
            final Class<?> returnType,
            final FacetHolder property) {
        final Method method = findMethod(cls, OBJECT, DEFAULT_PREFIX + capitalizedName, returnType, NO_PARAMETERS_TYPES);
        removeMethod(methodRemover, method);
        if (method == null) {
            return;
        }
        propertyFacets.add(new PropertyDefaultFacetViaMethod(method, property, getSpecificationLoader(), getRuntimeContext()));
    }

    private void findAndRemoveChoicesMethod(
            final List<Facet> propertyFacets,
            final MethodRemover methodRemover,
            final Class<?> cls,
            final String capitalizedName,
            final Class<?> returnType,
            final FacetHolder property) {
        final Method method = findMethod(cls, OBJECT, CHOICES_PREFIX + capitalizedName, null, NO_PARAMETERS_TYPES);
        if (method == null) {
            return;
        }
        methodRemover.removeMethod(method);
        propertyFacets.add(new PropertyChoicesFacetViaMethod(method, returnType, property, getSpecificationLoader(), getRuntimeContext()));
    }

    // ///////////////////////////////////////////////////////////////
    // PropertyOrCollectionIdentifyingFacetFactory impl.
    // ///////////////////////////////////////////////////////////////

	public boolean isPropertyOrCollectionAccessorCandidate(final Method method) {
        final String methodName = method.getName();
        if (methodName.startsWith(GET_PREFIX)) {
            return true;
        }
        if (methodName.startsWith(IS_PREFIX) && method.getReturnType() == boolean.class) {
            return true;
        }
        return false;
    }

    /**
     * The method way well represent a collection, but this facet factory does not have any opinion on the
     * matter.
     */
    public boolean isCollectionAccessor(final Method method) {
        return false;
    }

    public boolean isPropertyAccessor(final Method method) {
        if (!isPropertyOrCollectionAccessorCandidate(method)) {
            return false;
        }
        final Class<?> methodReturnType = method.getReturnType();
        return isCollectionOrArray(methodReturnType);
    }

    public void findAndRemovePropertyAccessors(final MethodRemover methodRemover, final List<Method> methodListToAppendTo) {
        final List<Method> isMethodList = methodRemover.removeMethods(MethodScope.OBJECT, IS_PREFIX, boolean.class, false, 0);
        methodListToAppendTo.addAll(isMethodList);
        final List<Method> getMethodList = methodRemover.removeMethods(MethodScope.OBJECT, GET_PREFIX, Object.class, false, 0);
        methodListToAppendTo.addAll(getMethodList);
    }

    public void findAndRemoveCollectionAccessors(
    		final MethodRemover methodRemover, 
    		final List<Method> methodListToAppendTo) {
    // does nothing
    }

    
    /////////////////////////////////////////////////////////
    // Dependencies (injected)
    /////////////////////////////////////////////////////////
    
    /**
     * as per {@link #setRuntimeContext(RuntimeContext)}.
     */
    private RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

    /**
     * Injected because {@link RuntimeContextAware}.
     */
    public void setRuntimeContext(final RuntimeContext runtimeContext) {
    	this.runtimeContext = runtimeContext;
    }

}
