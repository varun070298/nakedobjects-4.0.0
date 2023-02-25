package org.nakedobjects.bytecode.javassist.persistence.objectfactory.internal;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
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
	
	private MethodHandler methodHandler;

	public ObjectResolveAndObjectChangedEnhancer(
			final ObjectResolver objectResolver, 
			final ObjectChanger objectChanger, 
			final SpecificationLoader specificationLoader) {
		super(objectResolver, objectChanger, specificationLoader);
		
		createCallback();
	}

	protected void createCallback() {
		this.methodHandler = new MethodHandler() {
		    public Object invoke(
		    		final Object proxied, Method proxyMethod, 
		    		Method proxiedMethod, Object[] args) throws Throwable {

		    	boolean ignore = proxyMethod.getDeclaringClass().equals(Object.class);
				ImperativeFacetFlags flags = null;
		    	
				if (!ignore) {
					final JavaSpecification targetObjSpec = getJavaSpecificationOfOwningClass(proxiedMethod); 
	
					final NakedObjectMember member = targetObjSpec.getMember(proxiedMethod);
					flags = ImperativeFacetUtils.getImperativeFacetFlags(member, proxiedMethod);
					
					if (flags.impliesResolve()) {
						objectResolver.resolve(proxied, member.getName());
					}
				}

		        Object proxiedReturn = proxiedMethod.invoke(proxied, args);  // execute the original method.
		        
				if (!ignore && flags.impliesObjectChanged()) {
					objectChanger.objectChanged(proxied);
				}
			
				return proxiedReturn;
		    }
		};
	}
	
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> cls) {

		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setSuperclass(cls);
		proxyFactory.setInterfaces(ArrayUtils.combine(
				cls.getInterfaces(), new Class<?>[]{JavassistEnhanced.class}));
		
		proxyFactory.setFilter(new MethodFilter() {
		     public boolean isHandled(Method m) {
		         // ignore finalize()
		         return !m.getName().equals("finalize");
		     }
		});
		
		Class<T> proxySubclass = proxyFactory.createClass();
		try {
			T newInstance = proxySubclass.newInstance();
			ProxyObject proxyObject = (ProxyObject)newInstance;
			proxyObject.setHandler(methodHandler);
			
			return newInstance;
		} catch (InstantiationException e) {
			throw new NakedObjectException(e);
		} catch (IllegalAccessException e) {
			throw new NakedObjectException(e);
		}
	}

}
