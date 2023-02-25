package org.nakedobjects.plugins.dnd.viewer.tree;

import org.nakedobjects.plugins.dnd.DummyContent;
import org.nakedobjects.plugins.dnd.DummyViewSpecification;
import org.nakedobjects.plugins.dnd.DummyWorkspaceView;
import org.nakedobjects.plugins.dnd.TestToolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.tree.MasterDetailPanel;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyConfiguration;


/*
 * Note that the frame only contains two views and no additional spacing, hence no drawing. The
 * width is the total of the two decorated views, while the height is the largest of the two.
 */
public class TreeBrowserFrameTest extends ProxyJunit3TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TreeBrowserFrameTest.class);
    }

    private DummyWorkspaceView dummyWorkspace;
    private MasterDetailPanel frame;
//    private DummyView leftView;
//    private DummyView rightView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        NakedObjectsContext.setConfiguration(new TestProxyConfiguration());

        dummyWorkspace = new DummyWorkspaceView();

        TestToolkit.createInstance();

        final DummyViewSpecification spec = new DummyViewSpecification();
        final DummyViewSpecification lhsSpec = new DummyViewSpecification();
        final DummyContent content = new DummyContent();
        frame = new MasterDetailPanel(content, spec, null, lhsSpec);
        frame.setParent(dummyWorkspace);
/*
        leftView = new DummyView();
        leftView.setMaximumSize(new Size(79, 184));
        frame.initLeftPane(leftView);

        rightView = new DummyView();
        rightView.setMaximumSize(new Size(150, 150));
        frame.showInRightPane(rightView);
        */
    }

    public void testDecoratedLeftViewSize() {
    // width => width of view (79) + scroll border (16) + resize border (5) => 100
    // height => height of view (184) + scroll border (16) => 200
    // assertEquals(new Size(100, 200), leftView.getView().getRequiredSize());
    }

    public void testDecoratedRightViewSize() {
        assertEquals(new Size(0, 0), rightView().getView().getRequiredSize(new Size()));
    }

    private View leftView() {
        return frame.getSubviews()[0];
    }
    
    private View rightView() {
        return frame.getSubviews()[1];
    }

    public void testTotalRequiredSize() {
    // assertEquals(new Size(250, 200), frame.getRequiredSize());
    }
/*
    public void testLayoutWhereFrameNeedsToBeReduced() {
        dummyWorkspace.setSize(new Size(200, 1000));

        frame.invalidateLayout();
        frame.layout(new Size());

        // assertEquals("retains original size", new Size(100, 200), leftView.getView().getSize());
        // scroll border 16 pixels; resize border 5 pixels; total 21 pixels
        // assertEquals("width reduces", new Size(100, 200), rightView.getSize());

        assertEquals(new Location(0, 0), leftView().getLocation());
        // assertEquals(new Location(100, 0), rightView.getLocation());
    }
    */
/*
    public void testLayoutWithNoNeedToReduceFrame() {
        dummyWorkspace.setSize(new Size(1000, 1000));

        frame.invalidateLayout();
        frame.layout(new Size());

        assertEquals("retains original size", new Size(79, 184), leftView().getSize());
        // assertEquals("height should be the same as left (including borders)", new Size(150, 200),
        // rightView.getSize());

        assertEquals(new Location(), leftView().getLocation());
        // assertEquals(new Location(100, 0), rightView.getLocation());
    }
*/
    public void testRequiredFrameSize() {
    // scroll border 16 pixels; resize border 5 pixels; total 21 pixels
    // assertEquals(new Size(79 + 21 + 150, 200), frame.getRequiredSize());
    }

    public void testSubviews() {
        final View[] subviews = frame.getSubviews();
        assertEquals(leftView().getView(), subviews[0]);
        assertEquals(rightView().getView(), subviews[1]);
    }
}
// Copyright (c) Naked Objects Group Ltd.
