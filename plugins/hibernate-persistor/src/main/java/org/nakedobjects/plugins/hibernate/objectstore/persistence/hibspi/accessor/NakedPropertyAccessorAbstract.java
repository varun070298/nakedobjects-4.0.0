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


/**
 * Access properties of an object in a NakedObjects system, where that property is a BusinessValueHolder or
 * some other object which cannot be mapped using the standard Hibernate property accessors.
 */
public abstract class NakedPropertyAccessorAbstract implements PropertyAccessor {

    public static final class NakedSetter implements Setter {
        private static final long serialVersionUID = 1L;
        private final Method getValueHolder;
        private final PropertyConverter converter;
        private final Class<?> clazz;
        private final String name;

        NakedSetter(final Method getValueHolder, final PropertyConverter converter, final Class<?> clazz, final String name) {
            this.getValueHolder = getValueHolder;
            this.converter = converter;
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
                final Object valueHolder = getValueHolder.invoke(target, (Object[]) null);
                converter.setValue(valueHolder, value);
            } catch (final Exception e) {
                throw new PropertyAccessException(e, "could not set a field value by reflection", true, clazz, name);
            }
        }
    }

    public static final class NakedGetter implements Getter {
        private static final long serialVersionUID = 1L;
        private final Method getValueHolder;
        private final PropertyConverter converter;
        private final boolean isNullable;
        private final Class<?> clazz;
        private final String name;

        NakedGetter(
                final Method getValueHolder,
                final PropertyConverter converter,
                final boolean isNullable,
                final Class<?> clazz,
                final String name) {
            this.getValueHolder = getValueHolder;
            this.converter = converter;
            this.isNullable = isNullable;
            this.clazz = clazz;
            this.name = name;
        }

        public Object get(final Object target) throws HibernateException {
            try {
                final Object valueHolder = getValueHolder.invoke(target, (Object[]) null);
                return converter.getPersistentValue(valueHolder, isNullable);
            } catch (final Exception e) {
                throw new PropertyAccessException(e, "could not get a field value by reflection", false, clazz, name);
            }
        }

        public Method getMethod() {
            return null;
        }

        public String getMethodName() {
            return null;
        }

        public Class<?> getReturnType() {
            return converter.getPersistentType();
        }

        @SuppressWarnings("unchecked")
        public Object getForInsert(final Object target, final Map mergeMap, final SessionImplementor session)
                throws HibernateException {
            return get(target);
        }
    }

    protected Method getValueHolderMethod(final Class<?> theClass, final String propertyName) throws PropertyNotFoundException {
        final String naturalName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

        Method getMethod = null;
        try {
            getMethod = theClass.getMethod("get" + naturalName, (Class[]) null);
        } catch (final SecurityException se) {
            throw new PropertyNotFoundException("Cannot access method get" + naturalName + " in " + theClass + " : "
                    + se.getMessage());
        } catch (final NoSuchMethodException nsme) {
            try {
                // handle .NET properties
                getMethod = theClass.getMethod("get_" + naturalName, (Class[]) null);
            } catch (final SecurityException se) {
                throw new PropertyNotFoundException("Cannot access method get_" + naturalName + " in " + theClass + " : "
                        + se.getMessage());
            } catch (final NoSuchMethodException nsme2) {
                throw new PropertyNotFoundException("Unknown property " + naturalName + " in " + theClass);
            }
        }
        return getMethod;
    }

    protected PropertyConverter getConverter(final Method getValueHolder) {
        return ConverterFactory.getInstance().getConverter(getValueHolder.getReturnType());
    }

    @SuppressWarnings("unchecked")
    public Setter getSetter(final Class theClass, final String propertyName) throws PropertyNotFoundException {
        final Method getValueHolder = getValueHolderMethod(theClass, propertyName);
        return new NakedSetter(getValueHolder, getConverter(getValueHolder), theClass, propertyName);
    }

    public Getter getGetter(final Class<?> theClass, final String propertyName, final boolean isNullable)
            throws PropertyNotFoundException {
        final Method getValueHolder = getValueHolderMethod(theClass, propertyName);
        return new NakedGetter(getValueHolder, getConverter(getValueHolder), isNullable, theClass, propertyName);
    }

}
// Copyright (c) Naked Objects Group Ltd.
