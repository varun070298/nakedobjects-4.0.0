package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


public class TitleAccessor implements PropertyAccessor {
    // private final static Logger LOG = Logger.getLogger(OidAccessor.class);

    private static final class TitleSetter implements Setter {
        private static final long serialVersionUID = 1L;
        @SuppressWarnings("unused")
        private final Class<?> clazz;
        @SuppressWarnings("unused")
        private final String name;

        TitleSetter(final Class<?> clazz, final String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Method getMethod() {
            return null;
        }

        public String getMethodName() {
            return null;
        }

        public void set(final Object target, final Object value, final SessionFactoryImplementor factory)
                throws HibernateException {
        // do nothing
        }
    }

    private static final class TitleGetter implements Getter {
        private static final long serialVersionUID = 1L;
        private final Class<?> clazz;
        private final String name;

        TitleGetter(final Class<?> clazz, final String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Object get(final Object target) throws HibernateException {
            try {
                // LOG.info("Getting oid - class="+target);
                final NakedObject nakedTarget = getAdapterManager().getAdapterFor(target);
                // Hibernate might call to check what the unsaved value is - hence no adapter
                return nakedTarget == null ? null : nakedTarget.titleString();
            } catch (final Exception e) {
                throw new PropertyAccessException(e, "could not get a value by reflection", false, clazz, name);
            }
        }

        public Method getMethod() {
            return null;
        }

        public String getMethodName() {
            return null;
        }

        public Class<?> getReturnType() {
            return null;
        }

        @SuppressWarnings("unchecked")
        public Object getForInsert(final Object target, final Map mergeMap, final SessionImplementor session)
                throws HibernateException {
            return get(target);
        }
    }

    @SuppressWarnings("unchecked")
    public Setter getSetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        return new TitleSetter(theClass, propertyName);
    }

    @SuppressWarnings("unchecked")
    public Getter getGetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        return new TitleGetter(theClass, propertyName);
    }

    
    /////////////////////////////////////////////////////////////
    // Dependencies (from context)
    /////////////////////////////////////////////////////////////
    
    
    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }


}
