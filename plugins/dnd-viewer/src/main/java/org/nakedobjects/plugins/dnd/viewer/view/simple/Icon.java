package org.nakedobjects.plugins.dnd.viewer.view.simple;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;
import org.nakedobjects.plugins.dnd.viewer.view.text.TitleText;


public class Icon extends ObjectView {
    private IconGraphic icon;
    private boolean isVertical;
    private IconGraphic selectedGraphic;
    private TitleText title;
    private IconGraphic unselectedGraphic;

    public Icon(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);

        ensureHasIcon();

        int x = 0;
        int y = 0;
        icon.draw(canvas, x, getBaseline());
        if (isVertical) {
            final int w = title.getSize().getWidth();
            x = (w > icon.getSize().getWidth()) ? x : getSize().getWidth() / 2 - w / 2;
            y = icon.getSize().getHeight() + Toolkit.getText(ColorsAndFonts.TEXT_ICON).getAscent() + VPADDING;
        } else {
            x += icon.getSize().getWidth();
            x += View.HPADDING;
            y = icon.getBaseline();
        }
        title.draw(canvas, x, y);
        

        if (getState().isActive()) {
            Image busyImage = ImageFactory.getInstance().loadIcon("busy", 16, null);
            canvas.drawImage(busyImage, icon.getSize().getWidth() - 16 - 4, 4);
        }

    }

    private void ensureHasIcon() {
        if (icon == null) {
            icon = selectedGraphic;
        }
    }

    @Override
    public void entered() {
        icon = selectedGraphic;
        markDamaged();
        super.entered();
    }

    @Override
    public void exited() {
        icon = unselectedGraphic;
        markDamaged();
        super.exited();
    }

    @Override
    public int getBaseline() {
        ensureHasIcon();
        return icon.getBaseline();
    }

    @Override
    public Size getMaximumSize() {
        if (icon == null) {
            icon = selectedGraphic;
        }

        final Size size = icon.getSize();
        final Size textSize = title.getSize();
        if (isVertical) {
            size.extendHeight(VPADDING + textSize.getHeight() + VPADDING);
            size.ensureWidth(textSize.getWidth());
        } else {
            size.extendWidth(View.HPADDING);
            size.extendWidth(textSize.getWidth());
        }
        return size;
    }

    /**
     * Set up the graphic to be used when displaying the icon and the icon is selected.
     */
    public void setSelectedIcon(final IconGraphic selectedGraphic) {
        this.selectedGraphic = selectedGraphic;
    }

    /**
     * Set up the title to be used when displaying the icon.
     */
    public void setTitle(final ObjectTitleText text) {
        title = text;
    }

    /**
     * Set up the graphic to be used when displaying the icon and the icon is unselected. If this returns null
     * the graphic will not be changed when the icon becomes unselected.
     */
    public void setUnselectedGraphic(final IconGraphic unselectedGraphic) {
        this.unselectedGraphic = unselectedGraphic;
    }

    /**
     * Specifies if the graphic should be aligned vertical above the label; otherwise aligned horizontally.
     */
    public void setVertical(final boolean isVertical) {
        this.isVertical = isVertical;
    }

    @Override
    public void update(final NakedObject object) {
        final View p = getParent();
        if (p != null) {
            p.invalidateLayout();
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
