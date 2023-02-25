package org.nakedobjects.runtime.system;

import java.util.List;

import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.context.NakedObjectsContextThreadLocal;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;

/**
 * @see DeploymentType
 */
public enum ContextCategory {
    STATIC_RELAXED {
    	@Override
    	public void initContext(NakedObjectSessionFactory sessionFactory) {
    		NakedObjectsContextStatic.createRelaxedInstance(sessionFactory);
    	}
		@Override
		public boolean canSpecifyViewers(List<String> viewers) {
			// no more than one
			return viewers.size() <= 1;
		}
    },
    STATIC {
    	@Override
    	public void initContext(NakedObjectSessionFactory sessionFactory) {
    		NakedObjectsContextStatic.createInstance(sessionFactory);
    	}
		@Override
		public boolean canSpecifyViewers(List<String> viewers) {
			// no more than one
			return viewers.size() == 1;
		}
    },
    THREADLOCAL {
    	@Override
    	public void initContext(NakedObjectSessionFactory sessionFactory) {
    		NakedObjectsContextThreadLocal.createInstance(sessionFactory);
    	}
		@Override
		public boolean canSpecifyViewers(List<String> viewers) {
			// as many as you like
			return true;
		}
    };
    
	public abstract void initContext(NakedObjectSessionFactory sessionFactory);

	public abstract boolean canSpecifyViewers(List<String> viewers);

}


// Copyright (c) Naked Objects Group Ltd.
