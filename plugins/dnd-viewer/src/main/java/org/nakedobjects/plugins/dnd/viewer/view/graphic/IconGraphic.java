package org.nakedobjects.plugins.dnd.viewer.view.graphic;

import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


/*
 *  TODO why does this pass out the baseline, and then expect it back when doing the drawing?
 */
public class IconGraphic {
    private int baseline;
    protected final Content content;
    protected Image icon;
    protected final int iconHeight;
    private String lastIconName;

    public IconGraphic(final View view, final int height, final int baseline) {
        content = view.getContent();
        iconHeight = height;
        this.baseline = baseline;
    }

    public IconGraphic(final View view, final int height) {
        content = view.getContent();
        iconHeight = height;
    }

    public IconGraphic(final View view, final Text style) {
        content = view.getContent();
        iconHeight = style.getTextHeight();
        this.baseline = style.getAscent();
    }

    public void draw(final Canvas canvas, final int x, final int baseline) {
        final int y = baseline - this.baseline;
        if (Toolkit.debug) {
            canvas.drawDebugOutline(new Bounds(new Location(x, y), getSize()), getBaseline(), Toolkit
                    .getColor(ColorsAndFonts.COLOR_DEBUG_BOUNDS_DRAW));
        }
        final Image icon = icon();
        if (icon == null) {
            canvas.drawSolidOval(x + 1, y, iconHeight, iconHeight, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3));
        } else {
            canvas.drawImage(icon, x + 1, y);
        }
    }

    public int getBaseline() {
        return baseline;
    }

    public Size getSize() {
        final Image icon = icon();
        final int iconWidth = icon == null ? iconHeight : icon.getWidth();
        return new Size(iconWidth + 2, iconHeight + 2);
    }

    protected Image icon() {
        final String iconName = content.getIconName();
        /*
         * If the graphic is based on a name provided by the object then the icon could be changed at any
         * time, so we won't lazily load it.
         */
        if (icon != null && (iconName == null || iconName.equals(lastIconName))) {
            return icon;
        }
        lastIconName = iconName;
        if (iconName != null) {
            icon = ImageFactory.getInstance().loadIcon(iconName, iconHeight, null);
        }
        if (icon == null) {
            icon = content.getIconPicture(iconHeight);
        }
        if (icon == null) {
            icon = ImageFactory.getInstance().loadDefaultIcon(iconHeight, null);
        }
        return icon;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("baseline", baseline);
        str.append("icon", icon);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
