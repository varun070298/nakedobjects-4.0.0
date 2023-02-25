package org.nakedobjects.metamodel.commons.matchers;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.number.IsGreaterThan;
import org.hamcrest.text.StringContains;
import org.hamcrest.text.StringEndsWith;
import org.hamcrest.text.StringStartsWith;
import org.nakedobjects.metamodel.commons.lang.StringUtils;


/**
 * Hamcrest {@link Matcher} implementations.
 * 
 */
public final class NofMatchers {

    private NofMatchers() {}

    @Factory
    public static Matcher<String> containsStripNewLines(final String expected) {
        final String strippedExpected = StringUtils.stripNewLines(expected);
        return new StringContains(strippedExpected) {
            @Override
            public boolean matchesSafely(final String actual) {
                return super.matchesSafely(StringUtils.stripNewLines(actual));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a string (ignoring new lines) containing").appendValue(strippedExpected);
            }
        };
    }

    @Factory
    public static Matcher<String> equalToStripNewLines(final String expected) {
        final String strippedExpected = StringUtils.stripNewLines(expected);
        return new IsEqual<String>(strippedExpected) {
            @Override
            public boolean matches(final Object actualObj) {
                final String actual = (String) actualObj;
                return super.matches(StringUtils.stripNewLines(actual));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a string (ignoring new lines) equal to").appendValue(strippedExpected);
            }
        };
    }

    @Factory
    public static Matcher<String> startsWithStripNewLines(final String expected) {
        final String strippedExpected = StringUtils.stripNewLines(expected);
        return new StringStartsWith(strippedExpected) {
            @Override
            public boolean matchesSafely(final String actual) {
                return super.matchesSafely(StringUtils.stripNewLines(actual));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a string (ignoring new lines) starting with").appendValue(strippedExpected);
            }
        };
    }

    @Factory
    public static Matcher<String> endsWithStripNewLines(final String expected) {
        final String strippedExpected = StringUtils.stripNewLines(expected);
        return new StringEndsWith(strippedExpected) {
            @Override
            public boolean matchesSafely(final String actual) {
                return super.matchesSafely(StringUtils.stripNewLines(actual));
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("a string (ignoring new lines) ending with").appendValue(strippedExpected);
            }
        };
    }

    public static <T> Matcher<T> anInstanceOf(final Class<T> expected) {
        return new TypeSafeMatcher<T>() {
            @Override
            public boolean matchesSafely(final T actual) {
                return expected.isAssignableFrom(actual.getClass());
            }

            public void describeTo(final Description description) {
                description.appendText("an instance of ").appendValue(expected);
            }
        };
    }

    @Factory
    public static Matcher<String> nonEmptyString() {
        return new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String str) {
                return str != null && str.length() > 0;
            }

            public void describeTo(Description description) {
                description.appendText("a non empty string");
            }
            
        };
    }

    @Factory
    @SuppressWarnings("unchecked")
    public static Matcher<String> nonEmptyStringOrNull() {
        return CoreMatchers.anyOf(nullValue(String.class), nonEmptyString());
    }

    
    public static Matcher<List<?>> containsElementThat(final Matcher<?> elementMatcher) {
        return new TypeSafeMatcher<List<?>>() {
            public boolean matchesSafely(List<?> list) {
                for(Object o: list) {
                    if (elementMatcher.matches(o)) {
                        return true;
                    }
                }
                return false;
            }

            public void describeTo(Description description) {
                description.appendText("contains element that ").appendDescriptionOf(elementMatcher);
            }
        };
    }


    @Factory
    public static <T extends Comparable<T>> Matcher<T> greaterThan(T c) {
        return new IsGreaterThan<T>(c);
    }


    @Factory
    public static Matcher<Class<?>> classEqualTo(final Class<?> operand) {

        class ClassEqualsMatcher extends TypeSafeMatcher<Class<?>> {
            private final Class<?> clazz;
            public ClassEqualsMatcher(final Class<?> clazz) {
                this.clazz = clazz;
            }

            @Override
            public boolean matchesSafely(final Class<?> arg) {
                return clazz == arg;
            }

            public void describeTo(final Description description) {
                description.appendValue(clazz);
            }
        }

        return new ClassEqualsMatcher(operand);
    }
    
}
