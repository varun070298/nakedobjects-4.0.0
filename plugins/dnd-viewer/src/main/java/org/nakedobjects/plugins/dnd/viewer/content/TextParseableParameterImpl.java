package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.ParseableEntryActionParameter;
import org.nakedobjects.metamodel.util.NakedObjectUtils;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableParameter;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public class TextParseableParameterImpl extends AbstractTextParsableContent implements TextParseableParameter {
    private NakedObject object;
    private final NakedObject[] options;
    private final ParseableEntryActionParameter parameter;
    private final ActionHelper invocation;
    private final int index;

    public TextParseableParameterImpl(
            final ParseableEntryActionParameter nakedObjectActionParameters,
            final NakedObject nakedObject,
            final NakedObject[] options,
            final int i,
            final ActionHelper invocation) {
        this.parameter = nakedObjectActionParameters;
        this.options = options;
        index = i;
        this.invocation = invocation;
        object = nakedObject;
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("name", parameter.getName());
        debug.appendln("required", isRequired());
        debug.appendln("object", object);
    }

    @Override
    public void entryComplete() {}

    public String getIconName() {
        return "";
    }

    @Override
    public Image getIconPicture(final int iconHeight) {
        return ImageFactory.getInstance().loadIcon("value", 12, null);
    }

    public NakedObject getNaked() {
        return object;
    }

    public int getNoLines() {
        return parameter.getNoLines();
    }

    public NakedObject[] getOptions() {
        return options;
    }

    @Override
    public boolean isEmpty() {
        return object == null;
    }

    public boolean isRequired() {
        return !parameter.isOptional();
    }

    public boolean canClear() {
        return true;
    }

    public boolean canWrap() {
        return parameter.canWrap();
    }

    @Override
    public void clear() {
        object = null;
    }

    @Override
    public boolean isTransient() {
        return true;
    }

    @Override
    public boolean isTextParseable() {
        return true;
    }

    public boolean isOptionEnabled() {
        return options != null && options.length > 0;
    }

    public String title() {
        return NakedObjectUtils.titleString(object);
    }

    @Override
    public String toString() {
        final ToString toString = new ToString(this);
        toString.append("object", object);
        return toString.toString();
    }

    public String getParameterName() {
        return parameter.getName();
    }

    public NakedObjectSpecification getSpecification() {
        return parameter.getSpecification();
    }

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public String titleString(final NakedObject value) {
        return titleString(value, parameter, parameter.getSpecification());
    }

    /**
     * @throws InvalidEntryException -
     *             turns the parameter red if invalid.
     */
    @Override
    public void parseTextEntry(final String entryText) {
        object = parse(entryText);
        final String reason = parameter.isValid(object, NakedObjectUtils.unwrap(object));
        if (reason != null) {
            throw new InvalidEntryException(reason);
        } else if (!parameter.isOptional() && object == null) {
            throw new InvalidEntryException("Mandatory parameter cannot be empty");
        }
        invocation.setParameter(index, object);
    }

    private NakedObject parse(final String entryText) {
        final NakedObjectSpecification parameterSpecification = parameter.getSpecification();
        final ParseableFacet p = parameterSpecification.getFacet(ParseableFacet.class);
        try {
        	return p.parseTextEntry(object, entryText);
        } catch(IllegalArgumentException ex) {
        	throw new InvalidEntryException(ex.getMessage(), ex);
        }
    }

    public String getDescription() {
        final String title = object == null ? "" : ": " + object.titleString();
        final String specification = getSpecification().getShortName();
        final String type = getParameterName().indexOf(specification) == -1 ? "" : " (" + specification + ")";
        return getParameterName() + type + title + " " + parameter.getDescription();
    }

    public String getHelp() {
        return null;
    }

    public String getId() {
        return null;
    }

    @Override
    public Consent isEditable() {
        return Allow.DEFAULT;
    }

    public int getMaximumLength() {
        return parameter.getMaximumLength();
    }

    public int getTypicalLineLength() {
        return parameter.getTypicalLineLength();
    }
}
// Copyright (c) Naked Objects Group Ltd.
