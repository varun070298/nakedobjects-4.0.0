package org.nakedobjects.plugins.dnd.viewer.view.field;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.facets.value.ImageValueFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.OneToOneField;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.image.AwtImage;


public class ImageField extends AbstractField {
    public static class Specification extends AbstractFieldSpecification {
        @Override
        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            NakedObjectSpecification specification = content.getSpecification();
            return specification != null && specification.containsFacet(ImageValueFacet.class);
        }

        public View createView(final Content content, final ViewAxis axis) {
            return new ImageField(content, this, axis);
        }

        public String getName() {
            return "Image";
        }
    }

    private static final Logger LOG = Logger.getLogger(ImageField.class);
    private static final MediaTracker mt = new MediaTracker(new java.awt.Canvas());

    public ImageField(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        super.contentMenuOptions(options);

        options.add(new AbstractUserAction("Load image from file...") {
            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final String file = getViewManager().selectFilePath("Load image", ".");
                loadImageFromFile(file);
            }

        });
    }

    private void copy() {}

    @Override
    public void draw(final Canvas canvas) {
        Color color;

        if (hasFocus()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY1);
        } else if (getParent().getState().isObjectIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_IDENTIFIED);
        } else if (getParent().getState().isRootViewIdentified()) {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2);
        } else {
            color = Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1);
        }

        int top = 0;
        int left = 0;

        final Size size = getSize();
        int w = size.getWidth() - 1;
        int h = size.getHeight() - 1;
        canvas.drawRectangle(left, top, w, h, color);
        left++;
        top++;
        w -= 1;
        h -= 1;

        final NakedObject value = getContent().getNaked();
        if (value != null) {
            final ImageValueFacet facet = value.getSpecification().getFacet(ImageValueFacet.class);
            final java.awt.Image image = facet.getImage(value);
            if (image != null) {
                final Size imageSize = new Size(facet.getWidth(value), facet.getHeight(value));
                if (imageSize.getWidth() <= w && imageSize.getHeight() <= h) {
                    canvas.drawImage(new AwtImage(image), left, top);
                } else {
                    canvas.drawImage(new AwtImage(image), left, top, w, h);
                }
            }
        }
    }

    @Override
    public int getBaseline() {
        return VPADDING + Toolkit.getText(ColorsAndFonts.TEXT_NORMAL).getAscent();
    }

    @Override
    public Size getMaximumSize() {
        final Size size = new Size(60, 60);
        return size;
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final NakedObject value = getContent().getNaked();
        if (value == null) {
            return super.getRequiredSize(maximumSize);
        } else {
            final ImageValueFacet facet = value.getSpecification().getFacet(ImageValueFacet.class);
            final int width = Math.min(120, Math.max(32, facet.getWidth(value)));
            final int height = Math.min(120, Math.max(32, facet.getHeight(value)));
            return new Size(width, height);
        }
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (canChangeValue().isVetoed()) {
            return;
        }

        final int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_CONTROL || keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT) {
            return;
        }

        final int modifiers = key.getModifiers();
        final boolean ctrl = (modifiers & InputEvent.CTRL_MASK) > 0;

        switch (keyCode) {
        case KeyEvent.VK_V:
            if (ctrl) {
                key.consume();
                pasteFromClipboard();
            }
            break;
        case KeyEvent.VK_C:
            if (ctrl) {
                key.consume();
                copy();
            }
            break;
        }
    }

    private void loadImage(final Image image) {
        mt.addImage(image, 1);
        try {
            mt.waitForAll();
        } catch (final InterruptedException e) {
            throw new NakedObjectException(e);
        }

      //  final NakedObject value = getContent().getNaked();
        ImageValueFacet facet = ((FieldContent) getContent()).getSpecification().getFacet(ImageValueFacet.class);
        NakedObject object = facet.createValue(image);
        ((OneToOneField) getContent()).setObject(object);
        //((TextParseableField) getContent()).entryComplete();
        invalidateLayout();
    }
/*
    private void loadImageFromURL(final String filename) {
        try {
            final URL url = new URL("file://" + filename);
            final Image image = java.awt.Toolkit.getDefaultToolkit().getImage(url);
            loadImage(image);
        } catch (final MalformedURLException e) {
            throw new NakedObjectException("Failed to load image from " + filename);
        }
    }
*/
    private void loadImageFromFile(final String filename) {
        final Image image = java.awt.Toolkit.getDefaultToolkit().getImage(filename);
        loadImage(image);
    }

    @Override
    protected void pasteFromClipboard() {
        final Clipboard cb = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = cb.getContents(this);

        try {
            if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                // treat a string as a file
                final String filename = (String) content.getTransferData(DataFlavor.stringFlavor);
                LOG.debug("pasted image from " + filename);
                loadImageFromFile("file://" + filename);

            } else {
                LOG.info("unsupported paste operation " + content);

                // note java does not support transferring images from the clipboard
                // although it has an image flavor for it !!?
                /*
                 * DataFlavor[] transferDataFlavors = content.getTransferDataFlavors(); for (int i = 0; i <
                 * transferDataFlavors.length; i++) { LOG.debug("data transfer as " +
                 * transferDataFlavors[i].getMimeType()); }
                 * 
                 * Image image = (Image) content.getTransferData(DataFlavor.imageFlavor); LOG.debug("pasted " +
                 * image);
                 */

            }

        } catch (final Throwable e) {
            LOG.error("invalid paste operation " + e);
        }

    }

    @Override
    protected void save() {}
}
