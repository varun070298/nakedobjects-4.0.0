package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import java.io.Serializable;
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
import org.nakedobjects.plugins.hibernate.objectstore.persistence.oidgenerator.HibernateOid;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


/**
 * Accesses NakedObjects oids. The object id must always he held, but if a class doesn't declare an id field
 * then this accessor will store it in the Naked Objects adapter.
 * <p>
 * It would be better to have a method on the naked object such as
 * 
 * <pre>
 * void setId(Long id) {
 *     this.id = id;
 * }
 * 
 * Long getId() {
 *     return id;
 * }
 * </pre>
 * 
 * and use a property accessor to access that property.
 */
public class OidAccessor implements PropertyAccessor {
    public static final class OidSetter implements Setter {
        private static final long serialVersionUID = 1L;
        private final Class<?> clazz;
        private final String name;

        OidSetter(final Class<?> clazz, final String name) {
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
            try {
                final NakedObject nakedTarget = getAdapterManager().getAdapterFor(target);
                if (nakedTarget != null) {
                    final HibernateOid hoid = (HibernateOid) nakedTarget.getOid();
                    hoid.setHibernateId((Serializable) value);
                } else {
                    throw new HibernateException("could not set a value by reflection, no adapter found- class=" + clazz
                            + ", id=" + value);
                }
            } catch (final Exception e) {
                throw new PropertyAccessException(e, "could not set a value by reflection", true, clazz, name);
            }
        }

    }

    public static final class OidGetter implements Getter {
        private static final long serialVersionUID = 1L;
        private final Class<?> clazz;
        private final String name;

        OidGetter(final Class<?> clazz, final String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Object get(final Object target) throws HibernateException {
            try {
                // Hibernate may call in here during initialization - do not want to create domain objects
                // before fixtures have run, dependency injection etc.
                if (HibernateUtil.hasInitRun()) {
                    final HibernateOid oid = (HibernateOid) getAdapterManager().getAdapterFor(target)
                            .getOid();
                    if (oid != null) {
                        return oid.getHibernateId();
                    }
                }
                return null;
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
        return new OidSetter(theClass, propertyName);
    }

    @SuppressWarnings("unchecked")
    public Getter getGetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        return new OidGetter(theClass, propertyName);
    }


    
    ///////////////////////////////////////////////////////
    // Dependencies (from context)
    ///////////////////////////////////////////////////////

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }
    

}
// Copyright (c) Naked Objects Group Ltd.
