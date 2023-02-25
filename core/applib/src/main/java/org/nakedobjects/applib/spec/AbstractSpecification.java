package org.nakedobjects.applib.spec;

import java.lang.reflect.Method;


/**
 * Adapter to make it easy to write {@link Specification}s.
 * 
 * <p>
 * Provides two main features:
 * <ul>
 * <li> first, is type-safe (with invalid type being either ignored or constituting a failure), and 
 * <li> second, checks for nulls (with a null either being ignore or again constituting a failure)
 * </ul>
 * 
 * <p>
 * Implementation note: inspired by (borrowed code from) Hamcrest's <tt>TypeSafeMatcher</tt>.
 */
public abstract class AbstractSpecification<T> implements Specification {

    public enum TypeChecking {
        ENSURE_CORRECT_TYPE,
        IGNORE_INCORRECT_TYPE,
    }

    public enum Nullability {
        ENSURE_NOT_NULL,
        IGNORE_IF_NULL
    }

    private static Class<?> findExpectedType(Class<?> fromClass) {
        for (Class<?> c = fromClass; c != Object.class; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (isSatisfiesSafelyMethod(method)) {
                    return method.getParameterTypes()[0];
                }
            }
        }
        
        throw new Error("Cannot determine correct type for satisfiesSafely() method.");
    }
    
    private static boolean isSatisfiesSafelyMethod(Method method) {
        return method.getName().equals("satisfiesSafely") 
            && method.getParameterTypes().length == 1
            && !method.isSynthetic(); 
    }
    

    private final Class<?> expectedType;
    private final Nullability nullability;
    private final TypeChecking typeChecking;

    protected AbstractSpecification() {
        this(Nullability.IGNORE_IF_NULL, TypeChecking.IGNORE_INCORRECT_TYPE);
    }

    protected AbstractSpecification(final Nullability nullability, final TypeChecking typeChecking) {
        this.expectedType = findExpectedType(getClass());
        this.nullability = nullability;
        this.typeChecking = typeChecking;
    }


    /**
     * Checks not null and is correct type, and delegates to {@link #satisfiesSafely(Object)}.
     */
    @SuppressWarnings({"unchecked"})
    public final String satisfies(final Object obj) {
        if (obj == null) {
            return nullability == Nullability.IGNORE_IF_NULL? null: "Cannot be null";
        }
        if (!expectedType.isInstance(obj)) {
            return typeChecking == TypeChecking.IGNORE_INCORRECT_TYPE? null: "Incorrect type";
        }
        T objAsT = (T) obj;
        return satisfiesSafely(objAsT);
    }

    /**
     * If <tt>null</tt> then satisfied, otherwise is reason why the specification is
     * not satisfied.
     * 
     * <p>
     * Subclasses should implement this. The item will already have been checked for
     * the specific type and will never be null.
     */
    public abstract String satisfiesSafely(T obj);


}
