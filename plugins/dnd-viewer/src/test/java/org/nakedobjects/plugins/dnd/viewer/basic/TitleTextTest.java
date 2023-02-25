package org.nakedobjects.plugins.dnd.viewer.basic;

import org.nakedobjects.plugins.dnd.DummyCanvas;
import org.nakedobjects.plugins.dnd.DummyView;
import org.nakedobjects.plugins.dnd.TestToolkit;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.DummyText;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;


public class TitleTextTest extends ProxyJunit3TestCase {
    private TitleText titleText;
    private String title;
    // private TestCanvas canvas;
    private DummyView view;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TitleTextTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        TestToolkit.createInstance();

        view = new DummyView();
        final Text style = new DummyText();
        titleText = new TitleText(view, style, Toolkit.getColor(0)) {
            @Override
            protected String title() {
                return title;
            }
        };
        // canvas = new TestCanvas();
    }

    // TODO these tests won't work on server that doesn't have graphics - eg a Linux box without X
    /*
     * public void XXtestDrawCanvas() { title = "abcde";
     * 
     * TestCanvas canvas = new TestCanvas() ;
     * 
     * 
     * titleText.draw(canvas, 10, 20); }
     * 
     * public void testDrawCanvasDefaultColor() { titleText.draw(canvas, 10, 20);
     * assertEquals(Toolkit.getColor("black"), canvas.color); }
     * 
     * public void testDrawCanvasCanDrop() { view.getState().setCanDrop();
     * 
     * titleText.draw(canvas, 10, 20); assertEquals(Toolkit.getColor("")VALID, canvas.color); }
     * 
     * public void testDrawCanvasCantDrop() { view.getState().setCantDrop();
     * 
     * titleText.draw(canvas, 10, 20); assertEquals(Toolkit.getColor("")INVALID, canvas.color); }
     * 
     * public void testDrawCanvasIdentifier() { view.getState().setObjectIdentified();
     * 
     * titleText.draw(canvas, 10, 20); assertEquals(Toolkit.getColor("primary1"), canvas.color); }
     * 
     * 
     * public void testDrawingLocation() { titleText.draw(canvas, 10, 20); assertEquals(10 + View.HPADDING,
     * canvas.x); assertEquals(20, canvas.y); }
     * 
     * public void testDrawingText() { title = "test string"; titleText.draw(canvas, 10, 20);
     * assertEquals("test string", canvas.text); }
     * 
     * 
     * public void testDrawingTextTruncated() { /* Word boundaries at 4, 11, 16, 21, 24 & 34 / title = "test
     * string that will be truncated";
     * 
     * titleText.draw(canvas, 10, 20, 340); assertEquals("test string that will be truncated", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 339); assertEquals("test string that will be...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 210 + 30); assertEquals("test string that will...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 199 + 30); assertEquals("test string that...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 140); assertEquals("test string...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 139); assertEquals("test...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 70); assertEquals("test...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 60); assertEquals("tes...", canvas.text); }
     * 
     * 
     * 
     * public void testDrawingTextTruncatedBeforeCommasEtc() { title = "test string, that? is truncated";
     * 
     * titleText.draw(canvas, 10, 20, 210); assertEquals("test string, that...", canvas.text);
     * 
     * titleText.draw(canvas, 10, 20, 199); assertEquals("test string...", canvas.text); }
     */

    public void testGetSize() {
        title = "abcde";

        assertEquals(10 * 5, titleText.getSize().getWidth());
        assertEquals(8, titleText.getSize().getHeight());
    }

}

class TestCanvas extends DummyCanvas {
    String text;
    int x;
    int y;
    Color color;

    @Override
    public void drawText(final String text, final int x, final int y, final Color color, final Text style) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
    }

}
// Copyright (c) Naked Objects Group Ltd.
