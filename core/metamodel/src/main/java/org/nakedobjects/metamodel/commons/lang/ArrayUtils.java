package org.nakedobjects.metamodel.commons.lang;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public final class ArrayUtils {

    private ArrayUtils() {}

    public static Object[] getObjectAsObjectArray(final Object option) {
        final Class<?> arrayType = option.getClass().getComponentType();
        if (!arrayType.isPrimitive()) {
            return (Object[]) option;
        }
        if (arrayType == char.class) {
            return convertCharToCharacterArray(option);
        } else {
            return convertPrimitiveToObjectArray(arrayType, option);
        }
    }


    private static Object[] convertPrimitiveToObjectArray(
    		final Class<?> arrayType, final Object originalArray) {
        Object[] convertedArray;
        try {
            final Class<?> wrapperClass = WrapperUtils.wrap(arrayType);
            final Constructor<?> constructor = wrapperClass.getConstructor(new Class[] { String.class });
            final int len = Array.getLength(originalArray);
            convertedArray = (Object[]) Array.newInstance(wrapperClass, len);
            for (int i = 0; i < len; i++) {
                convertedArray[i] = constructor.newInstance(new Object[] { Array.get(originalArray, i).toString() });
            }
        } catch (final NoSuchMethodException e) {
            throw new NakedObjectException(e);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new NakedObjectException(e);
        } catch (final IllegalArgumentException e) {
            throw new NakedObjectException(e);
        } catch (final InstantiationException e) {
            throw new NakedObjectException(e);
        } catch (final IllegalAccessException e) {
            throw new NakedObjectException(e);
        } catch (final InvocationTargetException e) {
            throw new NakedObjectException(e);
        }
        return convertedArray;
    }

	private static Object[] convertCharToCharacterArray(final Object originalArray) {
        final char[] original = (char[]) originalArray;
        final int len = original.length;
        final Character[] converted = new Character[len];
        for (int i = 0; i < converted.length; i++) {
            converted[i] = new Character(original[i]);
        }
        return converted;
    }

	public static <T> T[] combine(final T[]... arrays) {
	    final List<T> list = new ArrayList<T>();
	    for (final T[] array : arrays) {
	        for (final T t : array) {
	            list.add(t);
	        }
	    }
	    return list.toArray(arrays[0]); // using 1st element of arrays to specify the type
	}

	/**
	 * Creates a mutable copy of the provided array.
	 */
	public static <T> List<T> asList(final T[] items) {
	    final List<T> list = new ArrayList<T>();
	    for (final T item : items) {
	        list.add(item);
	    }
	    return list;
	}

	/**
	 * Creates a mutable copy of the provided array, eliminating duplicates.
	 * 
	 * <p>
	 * The order of the items will be preserved.
	 */
	public static <T> Set<T> asOrderedSet(final T[] items) {
	    final LinkedHashSet<T> list = new LinkedHashSet<T>();
	    if (items != null) {
	        for (final T item : items) {
	            list.add(item);
	        }
	    }
	    return list;
	}

	/**
	 * Creates a mutable list of the provided array, also appending the additional element(s).
	 */
	public static <T> List<T> concat(T[] elements, T... elementsToAppend) {
	    List<T> result = new ArrayList<T>();
	    for(T element: elements) {
	        result.add(element);
	    }
	    for(T element: elementsToAppend) {
	        if (element != null) {
	            result.add(element);
	        }
	    }
	    return result;
	}

	public static String[] append(String[] args, String... moreArgs) {
		ArrayList<String> argList = new ArrayList<String>();
		argList.addAll(Arrays.asList(args));
		argList.addAll(Arrays.asList(moreArgs));
		return argList.toArray(new String[]{});
	}

	/**
	 * Creates a mutable list of the provided array, also appending the additional element(s).
	 */
	public static <T> List<T> concat(T[] elements, List<T> elementsToAppend) {
	    List<T> result = new ArrayList<T>();
	    for(T element: elements) {
	        result.add(element);
	    }
	    for(T element: elementsToAppend) {
	        if (element != null) {
	            result.add(element);
	        }
	    }
	    return result;
	}

	@SuppressWarnings("unchecked")
	public static <D,S> D[] copy(S[] source, Class<D> cls) {
		if (source == null) {
			throw new IllegalArgumentException("Source array cannot be null");
		}
		D[] destination = (D[]) Array.newInstance(cls, source.length);
		System.arraycopy(source, 0, destination, 0, source.length);
		return destination;
	}

	private static <D,S> void copyFromInto(S[] source, D[] destination) {
		if (source == null) {
			throw new IllegalArgumentException("Source array cannot be null");
		}
		if (destination == null) {
			throw new IllegalArgumentException("Destination array cannot be null");
		}
		if (source.length != destination.length) {
			throw new IllegalArgumentException("Source and destination arrays must be same length");
		}
		System.arraycopy(source, 0, destination, 0, source.length);
	}


}

// Copyright (c) Naked Objects Group Ltd.
