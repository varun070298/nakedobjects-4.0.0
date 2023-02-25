package org.nakedobjects.metamodel.value;

import static org.junit.Assert.assertEquals;

import java.awt.Image;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;

@RunWith(JMock.class)
public class ImageValueSemanticsProviderAbstractTest {
	
    protected Mockery mockery = new JUnit4Mockery();

    @Test
    public void testImageData() throws Exception {
    	RuntimeContext mockRuntimeContext = mockery.mock(RuntimeContext.class);
    	FacetHolder mockFacetHolder = mockery.mock(FacetHolder.class);
    	final TestImageSemanticsProvider adapter = new TestImageSemanticsProvider(mockFacetHolder , mockRuntimeContext);

    	String data = adapter.toEncodedString(null);
        int[][] array = (int[][]) adapter.doRestore(data);
        
        assertEquals(0xFF000000, array[0][0]);
        assertEquals(0xFF3F218A, array[0][1]);
        assertEquals(0xFF123456, array[0][3]);
        assertEquals(0xFF7FFFFF, array[0][4]);
        assertEquals(-0x7FFFFF, array[0][5]);
        assertEquals(-0x700000, array[0][6]);
    }
}

class TestImageSemanticsProvider extends ImageValueSemanticsProviderAbstract {

    TestImageSemanticsProvider(final FacetHolder holder, final RuntimeContext runtimeContext) {
        
		super(holder, null, null, null, runtimeContext);
	}

	@Override
    protected int[][] getPixels(final Object object) {
        int[][] array = new int[10][10];
        array[0][1] = 0x3F218A;
        array[0][3] = 0x123456;
        array[0][4] = 0x7FFFFF;
        array[0][5] = -0x7FFFFF;
        array[0][6] = -0x700000;
        return array;
    }

    @Override
    protected Object setPixels(final int[][] pixels) {
        return pixels;
    }

    public int getHeight(final NakedObject object) {
        return 0;
    }

    public Image getImage(final NakedObject object) {
        return null;
    }

    public int getWidth(final NakedObject object) {
        return 0;
    }

    public NakedObject setImage(final NakedObject object, final Image image) {
        return null;
    }

    public boolean isNoop() {
        return false;
    }

    public NakedObject createValue(Image image) {
        return null;
    }
}

// Copyright (c) Naked Objects Group Ltd.
