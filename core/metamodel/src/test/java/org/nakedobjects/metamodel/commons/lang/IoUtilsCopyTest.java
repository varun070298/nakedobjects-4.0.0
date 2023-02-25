package org.nakedobjects.metamodel.commons.lang;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class IoUtilsCopyTest {

    private final class ArrayMatcher extends TypeSafeMatcher<byte[]> {

        private final byte[] expectedBytes;

        public ArrayMatcher(final byte[] expectedBytes) {
            this.expectedBytes = expectedBytes;
        }

        @Override
        public boolean matchesSafely(final byte[] actualBytes) {
            if (actualBytes.length != expectedBytes.length) {
                return false;
            }
            for (int i = 0; i < actualBytes.length; i++) {
                if (actualBytes[i] != expectedBytes[i]) {
                    return false;
                }
            }
            return true;
        }

        public void describeTo(final Description arg0) {
            arg0.appendText("does not match expected array");
        }
    }

    private static int BUF_INTERNAL_SIZE = 1024;

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void handlesNullInputStream() throws Exception {
        final ByteArrayInputStream bais = null;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtils.copy(bais, baos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void handlesNullOutputStream() throws Exception {
        final byte[] bytes = createByteArray(10);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = null;
        IoUtils.copy(bais, baos);
    }

    @Test
    public void copiesEmptyInputStream() throws Exception {
        final byte[] bytes = createByteArray(0);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtils.copy(bais, baos);
        assertThat(baos.toByteArray(), arrayEqualTo(bytes));
    }

    @Test
    public void copiesInputStreamSmallerThanInternalBuffer() throws Exception {
        final byte[] bytes = createByteArray(BUF_INTERNAL_SIZE - 1);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtils.copy(bais, baos);
        assertThat(baos.toByteArray(), arrayEqualTo(bytes));
    }

    @Test
    public void copiesInputStreamLargerThanInternalBuffer() throws Exception {
        final byte[] bytes = createByteArray(BUF_INTERNAL_SIZE + 1);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtils.copy(bais, baos);
        assertThat(baos.toByteArray(), arrayEqualTo(bytes));
    }

    @Test
    public void copiesInputStreamExactlySameSizeAsInternalBuffer() throws Exception {
        final byte[] bytes = createByteArray(BUF_INTERNAL_SIZE);
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtils.copy(bais, baos);
        assertThat(baos.toByteArray(), arrayEqualTo(bytes));
    }

    private Matcher<byte[]> arrayEqualTo(final byte[] bytes) {
        return new ArrayMatcher(bytes);
    }

    private byte[] createByteArray(final int size) {
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) i;
        }
        return bytes;
    }

}

// Copyright (c) Naked Objects Group Ltd.
