package org.nakedobjects.bytecode.cglib.persistence.objectfactory.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.nakedobjects.metamodel.commons.lang.ArrayUtils;
import org.nakedobjects.metamodel.java5.ImperativeFacet;
import org.nakedobjects.metamodel.java5.ImperativeFacetUtils;
import org.nakedobjects.metamodel.java5.ImperativeFacetUtils.ImperativeFacetFlags;
import org.nakedobjects.metamodel.spec.JavaSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectMember;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.runtime.bytecode.ObjectResolveAndObjectChangedEnhancerAbstract;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectChanger;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectResolver;

public class ObjectResolveAndObjectChangedEnhancer extends ObjectResolveAndObjectChangedEnhancerAbstract {

	private Callback callback;

	/**
	 * Cache of Enhancers, lazy populated.
	 */
	private final Map<Class<?>, Enhancer> enhancerByClass = new HashMap<Class<?>, Enhancer>();

	public ObjectResolveAndObjectChangedEnhancer(
			final ObjectResolver objectResolver,
			final ObjectChanger objectChanger,
			final SpecificationLoader specificationLoader) {
		super(objectResolver, objectChanger, specificationLoader);
		
		createCallback();
	}

	@Override
	protected void createCallback() {
		this.callback = new MethodInterceptor() {

			public Object intercept(
					final Object proxied, 
					final Method proxiedMethod,
					final Object[] args, 
					final MethodProxy proxyMethod) throws Throwable {

				boolean ignore = proxiedMethod.getDeclaringClass().equals(Object.class);
				ImperativeFacetFlags flags = null;
				
				if (!ignore) {
					final JavaSpecification targetObjSpec = getJavaSpecificationOfOwningClass(proxiedMethod);
					final NakedObjectMember member = targetObjSpec.getMember(proxiedMethod);
					
					flags = ImperativeFacetUtils.getImperativeFacetFlags(member, proxiedMethod);
					
					if (flags.impliesResolve()) {
						objectResolver.resolve(proxied, member.getName());
					}
				}

				final Object proxiedReturn = proxyMethod.invokeSuper(proxied, args);

				if (!ignore && flags.impliesObjectChanged()) {
					objectChanger.objectChanged(proxied);
				}

				return proxiedReturn;
			}

		};
	}
	
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> cls) {
		Enhancer enhancer = lookupOrCreateEnhancerFor(cls);
		return (T) enhancer.create();
	}
	
	private Enhancer lookupOrCreateEnhancerFor(Class<?> cls) {
		Enhancer enhancer = enhancerByClass.get(cls);
		if (enhancer == null) {
			enhancer = new Enhancer();
			enhancer.setSuperclass(cls);
			enhancer.setInterfaces(ArrayUtils.combine(
					cls.getInterfaces(),
					new Class<?>[] { CglibEnhanced.class }));
			enhancer.setCallback(callback);
			enhancerByClass.put(cls, enhancer);
		}
		return enhancer;
	}

}
