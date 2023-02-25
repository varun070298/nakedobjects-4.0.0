package org.nakedobjects.applib.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Title buffer is a utility class to help produce titles for objects without having to add lots of guard
 * code. It provides two basic method: one to concatenate a title to the buffer; another to append a title
 * with a joiner string, taking care adding in necessary spaces. The benefits of using this class is that null
 * references are safely ignored (rather than appearing as 'null'), and joiners (a space by default) are only
 * added when needed.
 */
public class TitleBuffer {
    private static final String SPACE = " ";

    /**
     * Determines if the specified object's title (from its <code>toString</code> method) is empty. Will
     * return true if either: the specified reference is null; the object's <code>toString</code> method
     * returns null; or if the <code>toString</code> returns an empty string.
     */
    public static boolean isEmpty(final Object object) {
        String title = titleFor(object);
        return title == null ||  title.equals("");
    }

    /**
     * Reflectively run the <tt>String title()</tt> method if it exists, else fall back to the <tt>toString()</tt> method.
     */
    private static String titleFor(Object object) {
        if(object == null) {return null;} 
        else {
            Method method;
            try {
                method = object.getClass().getMethod("title", new Class[0]);
                return (String) method.invoke(object, new Object[0]);
            } catch (SecurityException e) {
                throw new TitleBufferException(e);
            } catch (NoSuchMethodException e) {
                return object.toString();
            } catch (IllegalArgumentException e) {
                throw new TitleBufferException(e);
            } catch (IllegalAccessException e) {
                throw new TitleBufferException(e);
            } catch (InvocationTargetException e) {
                throw new TitleBufferException(e);
            }
        }
    }

    /**
     * Determines if the specified text is empty. Will return true if either: the specified reference is null;
     * or if the reference is an empty string.
     */
    public static boolean isEmpty(final String text) {
        return text == null || text.equals("");
    }

    private final StringBuffer title;

    /**
     * Creates a new, empty, title object.
     */
    public TitleBuffer() {
        title = new StringBuffer();
    }

    /**
     * Creates a new title object, containing the title of the specified object.
     */
    public TitleBuffer(final Object object) {
        this();
        concat(object);
    }

    /**
     * Creates a new title object, containing the title of the specified object.
     */
    public TitleBuffer(final Object object, final String defaultTitle) {
        this();
        if (isEmpty(object)) {
            concat(defaultTitle);
        } else {
            concat(object);
        }
    }

    /**
     * Creates a new title object, containing the specified text.
     */
    public TitleBuffer(final String text) {
        this();
        concat(text);
    }

    /**
     * 
     */
    public TitleBuffer append(final int number) {
        append(String.valueOf(number));
        return this;
    }

    /**
     * Append the title of the specified object.
     */
    public TitleBuffer append(final Object object) {
        if (!isEmpty(object)) {
            appendWithSpace(object);
        }
        return this;
    }

    /**
     * Appends the title of the specified object, or the specified text if the objects title is null or empty.
     * Prepends a space if there is already some text in this title object.
     * 
     * @param object
     *            the object whose title is to be appended to this title.
     * @param defaultValue
     *            a textual value to be used if the object's title is null or empty.
     * @return a reference to the called object (itself).
     */
    public TitleBuffer append(final Object object, final String defaultValue) {
        if (!isEmpty(object)) {
            appendWithSpace(object);
        } else {
            appendWithSpace(defaultValue);
        }
        return this;
    }

    /**
     * Appends a space (if there is already some text in this title object) and then the specified text.
     * 
     * @return a reference to the called object (itself).
     */
    public TitleBuffer append(final String text) {
        if (!isEmpty(text)) {
            appendWithSpace(text);
        }
        return this;
    }

    /**
     * Appends the joining string and the title of the specified object (from its <code>toString</code>
     * method). If the object is empty then nothing will be appended.
     * 
     * @see #isEmpty(Object)
     */
    public TitleBuffer append(final String joiner, final Object object) {
        if (!isEmpty(object)) {
            appendJoiner(joiner);
            appendWithSpace(object);
        }
        return this;
    }

    /**
     * Append the <code>joiner</code> text, a space, and the title of the specified naked object (<code>object</code>)
     * (got by calling the objects title() method) to the text of this TitleString object. If the title of the
     * specified object is null then use the <code>defaultValue</code> text. If both the objects title and
     * the default value are null or equate to a zero-length string then no text will be appended ; not even
     * the joiner text.
     * 
     * @param joiner
     *            text to append before the title
     * @param object
     *            object whose title needs to be appended
     * @param defaultTitle
     *            the text to use if the the object's title is null.
     * @return a reference to the called object (itself).
     */
    public TitleBuffer append(final String joiner, final Object object, final String defaultTitle) {
        appendJoiner(joiner);
        if (!isEmpty(object)) {
            appendWithSpace(object);
        } else {
            appendWithSpace(defaultTitle);
        }
        return this;
    }

    /**
     * Appends the joiner text, a space, and the text to the text of this TitleString object. If no text yet
     * exists in the object then the joiner text and space are omitted.
     * 
     * @return a reference to the called object (itself).
     */
    public TitleBuffer append(final String joiner, final String text) {
        if (!isEmpty(text)) {
            appendJoiner(joiner);
            appendWithSpace(text);
        }
        return this;
    }

    private void appendJoiner(final String joiner) {
        if (title.length() > 0) {
            title.append(joiner);
        }
    }

    /**
     * Append a space to the text of this TitleString object if, and only if, there is some existing text
     * i.e., a space is only added to existing text and will not create a text entry consisting of only one
     * space.
     * 
     * @return a reference to the called object (itself).
     */
    public TitleBuffer appendSpace() {
        if (title.length() > 0) {
            title.append(SPACE);
        }
        return this;
    }

    private void appendWithSpace(final Object object) {
        appendSpace();
        title.append(titleFor(object));
    }

    /**
     * Concatenate the the title value (the result of calling an objects label() method) to this TitleString
     * object. If the value is null the no text is added.
     * 
     * @param object
     *            the naked object to get a title from
     * @return a reference to the called object (itself).
     */
    public final TitleBuffer concat(final Object object) {
        concat(object, "");
        return this;
    }

    /**
     * Concatenate the the title value (the result of calling an objects label() method), or the specified
     * default value if the title is equal to null or is empty, to this TitleString object.
     * 
     * @param object
     *            the naked object to get a title from
     * @param defaultValue
     *            the default text to use when the naked object is null
     * @return a reference to the called object (itself).
     */
    public final TitleBuffer concat(final Object object, final String defaultValue) {
        if (isEmpty(object)) {
            title.append(defaultValue);
        } else {
            title.append(titleFor(object));
        }

        return this;
    }

    /**
     * Concatenate the specified text on to the end of the text of this TitleString.
     * 
     * @param text
     *            text to append
     * @return a reference to the called object (itself).
     */
    public final TitleBuffer concat(final String text) {
        title.append(text);
        return this;
    }

    public final TitleBuffer concat(final String joiner, final Object object) {
        if (!isEmpty(object)) {
            appendJoiner(joiner);
            concat(object, "");
        }
        return this;
    }

    public final TitleBuffer concat(final String joiner, final Object object, final String defaultValue) {
        if (isEmpty(object)) {
            appendJoiner(joiner);
            title.append(defaultValue);
        } else {
            appendJoiner(joiner);
            title.append(titleFor(object));
        }
        return this;
    }

    /**
     * Returns a String that represents the value of this object.
     */
    @Override
    public String toString() {
        return title.toString();
    }

    /**
     * Truncates this title so it has a maximum number of words. Spaces are used to determine words, thus two
     * spaces in a title will cause two words to be mistakenly identified.
     * 
     * @param noWords
     *            the number of words to show
     * @return a reference to the called object (itself).
     */
    public TitleBuffer truncate(final int noWords) {
        if (noWords < 1) {
            throw new IllegalArgumentException("Truncation must be to one or more words");
        }
        int pos = 0;
        int spaces = 0;

        while (pos < title.length() && spaces < noWords) {
            if (title.charAt(pos) == ' ') {
                spaces++;
            }
            pos++;
        }
        if (pos < title.length()) {
            title.setLength(pos - 1); // string.delete(pos - 1, string.length());
            title.append("...");
        }
        return this;
    }

}

// Copyright (c) Naked Objects Group Ltd.
