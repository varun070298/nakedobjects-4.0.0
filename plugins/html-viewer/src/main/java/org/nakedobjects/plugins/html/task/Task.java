package org.nakedobjects.plugins.html.task;

import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.value.PasswordValueFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * Represents a task that the user is working through. Is used for both editing objects and setting up
 * parameters for an action method.
 */
public abstract class Task {
    private static int nextID = 1;
    private int[] boundaries;
    private final String description;
    protected final String[] errors;
    protected String error;
    private final String[] entryText;
    protected final NakedObject[] initialState;
    private final String name;
    protected final String[] names;
    protected final String[] descriptions;
    protected final boolean[] optional;
    protected final boolean[] readOnly;
    protected final int numberOfEntries;
    private int step;
    private final String targetId;
    protected final NakedObjectSpecification[] fieldSpecifications;
    protected final int[] noLines;
    protected final boolean[] wraps;
    protected final int[] maxLength;
    protected final int[] typicalLength;
    protected final int id = nextID++;

    public Task(final Context context, final String name, final String description, final NakedObject target, final int noFields) {
        this.name = name;
        this.description = description;
        targetId = context.mapObject(target);

        initialState = new NakedObject[noFields];
        names = new String[noFields];
        descriptions = new String[noFields];
        optional = new boolean[noFields];
        readOnly = new boolean[noFields];
        fieldSpecifications = new NakedObjectSpecification[noFields];

        numberOfEntries = noFields;
        entryText = new String[noFields];
        errors = new String[noFields];

        noLines = new int[noFields];
        wraps = new boolean[noFields];
        maxLength = new int[noFields];
        typicalLength = new int[noFields];
    }

    public void init(final Context context) {
        for (int i = 0; i < entryText.length; i++) {
            final NakedObject obj = initialState[i];
            if (obj == null) {
                entryText[i] = "";
            } else if (obj.getSpecification().getFacet(PasswordValueFacet.class) != null) {
                final PasswordValueFacet facet = obj.getSpecification().getFacet(PasswordValueFacet.class);
                entryText[i] = facet.getEditText(obj);
            } else if (obj.getSpecification().isParseable()) {
                entryText[i] = obj.titleString();
            } else if (obj.getSpecification().isObject()) {
                if (readOnly[i]) {
                    entryText[i] = (obj).titleString();
                } else {
                    entryText[i] = context.mapObject(obj);
                }
            } else if (obj.getSpecification().isCollection()) {
                entryText[i] = (obj).titleString();
            }
        }

        divyUpWork();
    }

    public abstract NakedObject completeTask(Context context, Page page);

    private void copyForThisStep(final Object[] source, final Object[] destination) {
        for (int i = 0; i < noOfEntriesInThisStep(); i++) {
            destination[i] = source[firstEntryInThisStep() + i];
        }
    }

    private void copyForThisStep(final boolean[] source, final boolean[] destination) {
        for (int i = 0; i < noOfEntriesInThisStep(); i++) {
            destination[i] = source[firstEntryInThisStep() + i];
        }
    }

    private void copyForThisStep(final int[] source, final int[] destination) {
        for (int i = 0; i < noOfEntriesInThisStep(); i++) {
            destination[i] = source[firstEntryInThisStep() + i];
        }
    }

    public void checkInstances(final Context context, final NakedObject[] objects) {}

    public void debug(final DebugString debug) {
        debug.indent();
        debug.appendln("name", name);
        debug.appendln("number of steps ", numberOfSteps());
        debug.appendln("current step", step);
        debug.appendln("target", targetId);
        debug.appendln("steps (" + (boundaries.length - 1) + ")");
        debug.indent();
        for (int i = 0; i < boundaries.length - 1; i++) {
            debug.appendln("    " + (i + 1) + ". " + boundaries[i] + " - " + (boundaries[i + 1] - 1));
        }
        debug.unindent();
        debug.appendln("fields (" + names.length + ")");
        debug.indent();
        for (int i = 0; i < names.length; i++) {
            final String status = (readOnly[i] ? "R" : "-") + (optional[i] ? "O" : "M") + (errors[i] == null ? "-" : "E");
            debug.appendln("    " + i + "  " + names[i] + " (" + status + "):  " + fieldSpecifications[i].getFullName() + " -> "
                    + entryText[i]);
        }
        debug.unindent();
        debug.unindent();
    }

    private void divyUpWork() {
        if (numberOfEntries == 0) {
            boundaries = new int[2];
        } else {
            final int[] b = new int[numberOfEntries + 2];
            int count = 0;
            b[count++] = 0;

            NakedObjectSpecification type = fieldSpecifications[0];
            boolean direct = simpleField(type, 0);

            for (int i = 1; i < numberOfEntries; i++) {
                type = fieldSpecifications[i];
                if (true || direct && (simpleField(type, i))) {
                    continue;
                }
                b[count++] = i;
                direct = simpleField(type, i);
            }
            b[count++] = numberOfEntries;
            boundaries = new int[count];
            System.arraycopy(b, 0, boundaries, 0, count);
        }
    }

    protected boolean simpleField(final NakedObjectSpecification specification, final int i) {
        return readOnly[i] || (specification.isObject() && SpecificationFacets.isBoundedSet(specification));
    }

    private int firstEntryInThisStep() {
        return boundaries[step];
    }

    public String getDescription() {
        return description;
    }

    public String getError() {
        return error;
    }

    /**
     * Returns an array of errors, one for each element in the task.
     */
    public String[] getErrors() {
        final String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(errors, array);
        return array;
    }

    public String[] getFieldDescriptions() {
        final String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(descriptions, array);
        return array;
    }

    public String[] getEntryText() {
        final String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(entryText, array);
        return array;
    }

    public String getName() {
        return name;
    }

    public String[] getNames() {
        final String[] array = new String[noOfEntriesInThisStep()];
        copyForThisStep(names, array);
        return array;
    }

    public NakedObject[][] getOptions(final Context context) {
        return getOptions(context, firstEntryInThisStep(), noOfEntriesInThisStep());
    }

    protected NakedObject[][] getOptions(final Context context, final int from, final int len) {
        return new NakedObject[len][];
    }

    protected NakedObject[] getEntries(final Context context) {
        final NakedObject[] entries = new NakedObject[entryText.length];
        for (int i = 0; i < entries.length; i++) {
            if (entryText == null || readOnly[i]) {
                continue;
            }
            final NakedObjectSpecification fieldSpecification = fieldSpecifications[i];
            if (fieldSpecification.isParseable()) {
                final ParseableFacet parser = fieldSpecification.getFacet(ParseableFacet.class);
                try {
                    entries[i] = parser.parseTextEntry(initialState[i], entryText[i]);
                } catch (final InvalidEntryException e) {
                    errors[i] = e.getMessage();
                } catch (final TextEntryParseException e) {
                    errors[i] = e.getMessage();
                }
            } else if (fieldSpecification.isObject() && entryText[i] != null) {
                if (entryText[i].equals("null")) {
                    entries[i] = null;
                } else {
                    entries[i] = context.getMappedObject(entryText[i]);
                }
            }
        }
        return entries;
    }

    public String getId() {
        return "" + id;
    }

    public boolean[] getOptional() {
        final boolean[] array = new boolean[noOfEntriesInThisStep()];
        copyForThisStep(optional, array);
        return array;
    }

    public int[] getNoLines() {
        final int[] array = new int[noOfEntriesInThisStep()];
        copyForThisStep(noLines, array);
        return array;
    }

    public boolean[] getWraps() {
        final boolean[] array = new boolean[noOfEntriesInThisStep()];
        copyForThisStep(wraps, array);
        return array;
    }

    public int[] getMaxLength() {
        final int[] array = new int[noOfEntriesInThisStep()];
        copyForThisStep(maxLength, array);
        return array;
    }

    public int[] getTypicalLength() {
        final int[] array = new int[noOfEntriesInThisStep()];
        copyForThisStep(typicalLength, array);
        return array;
    }

    public boolean[] getReadOnly() {
        final boolean[] array = new boolean[noOfEntriesInThisStep()];
        copyForThisStep(readOnly, array);
        return array;
    }

    public int getStep() {
        return step;
    }

    public NakedObject getTarget(final Context context) {
        return context.getMappedObject(targetId);
    }

    public String[] getTrail() {
        final String[] trail = new String[boundaries.length - 1];
        for (int i = 0; i < trail.length; i++) {
            trail[i] = "step " + i;
        }
        return trail;
    }

    public NakedObjectSpecification[] getTypes() {
        final NakedObjectSpecification[] array = new NakedObjectSpecification[noOfEntriesInThisStep()];
        copyForThisStep(fieldSpecifications, array);
        return array;
    }

    public boolean isEditing() {
        return false;
    }

    public void nextStep() {
        step++;
    }

    private int noOfEntriesInThisStep() {
        return boundaries[step + 1] - boundaries[step];
    }

    public int numberOfSteps() {
        return boundaries.length - 1;
    }

    public void previousStep() {
        step--;
    }

    public void setFromFields(final Request request, final Context context) {
        int fldNo = 0;
        for (int i = boundaries[step]; i < boundaries[step + 1]; i++) {
            String textEntry = request.getFieldEntry(fldNo++);
            if (readOnly[i]) {
                continue;
            }
            final NakedObjectSpecification spec = fieldSpecifications[i];
            // deal with check boxes specially: expect 'true' if checked and no entry if not checked, hence
            // need to set as 'false'
            if (spec.isOfType(NakedObjectsContext.getSpecificationLoader().loadSpecification(boolean.class))
                    || spec.isOfType(NakedObjectsContext.getSpecificationLoader().loadSpecification(Boolean.class))) {
                if (textEntry == null || !textEntry.equals("true")) {
                    textEntry = "false";
                }
            }
            entryText[i] = textEntry;
            try {
                errors[i] = null;
                setFromField(context, i, spec, textEntry);
                if (!optional[i] && (textEntry == null || textEntry.equals(""))) {
                    errors[i] = "Field required";
                }
            } catch (final InvalidEntryException e) {
                errors[i] = e.getMessage();
            } catch (final TextEntryParseException e) {
                errors[i] = e.getMessage();
            }
        }
    }

    private void setFromField(final Context context, final int i, final NakedObjectSpecification spec, final String textEntry) {
        if (spec.isParseable()) {
            if (textEntry == null) {
                return;
            } else {
                // REVIEW this block uses the existing adapter as it contains the regex needed. This needs to
                // be reviewed in line with Dan's proposed changes to the reflector.
                final NakedObject nakedValue = initialState[i];
                final ParseableFacet parser = spec.getFacet(ParseableFacet.class);
                parser.parseTextEntry(nakedValue, textEntry);
                // REVIEW what do we do when an exception is thrown - a parse fails?
            }
        }
    }

    public abstract void checkForValidity(Context context);

}

// Copyright (c) Naked Objects Group Ltd.
