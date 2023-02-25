package org.nakedobjects.plugins.dnd.viewer.list;

import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;


public class InternalCollectionIconGraphic extends IconGraphic {

    public InternalCollectionIconGraphic(final View view, final Text text) {
        super(view, text);
    }

    /*
     * protected Image iconPicture(NakedObject object) { final InternalCollection cls = (InternalCollection)
     * object; final NakedObjectSpecification spec = cls.getElementSpecification(); Image icon =
     * loadIcon(spec, ""); if(icon == null) { icon = loadIcon(spec, ""); } return icon; }
     */
}
// Copyright (c) Naked Objects Group Ltd.
