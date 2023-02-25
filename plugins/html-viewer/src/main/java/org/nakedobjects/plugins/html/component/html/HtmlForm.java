package org.nakedobjects.plugins.html.component.html;

import java.io.PrintWriter;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.html.component.ComponentComposite;
import org.nakedobjects.plugins.html.component.Form;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class HtmlForm extends ComponentComposite implements Form {
    private final boolean confirm;
    private final boolean hasNext;
    private final boolean hasPrevious;
    private final boolean isEditing;
    private final String id;

    public HtmlForm(final String id, final String action, final int page, final int noOfPages, final boolean isEditing) {
        this(id, action, page, noOfPages, false, isEditing);
    }

    private HtmlForm(
            final String id,
            final String action,
            final int page,
            final int noOfPages,
            final boolean confirm,
            final boolean isEditing) {
        this.id = id;
        this.confirm = confirm;
        this.isEditing = isEditing;
        hasPrevious = page >= 1;
        hasNext = page < noOfPages - 1;
    }

    private void addField(
            final String label,
            final String field,
            final String description,
            final boolean readOnly,
            final boolean required,
            final String errorMessage) {
        String error = "";
        if (errorMessage != null) {
            error = "<span class=\"error\"> " + errorMessage + "</span>";
        }
        String optional = "";
        if (!readOnly) {
            if (!required) {
                optional = "<span class=\"optional\"> (optional)</span>";
            } else {
                optional = "<span class=\"required\"> *</span>";
            }
        }
        add(new Html("<div class=\"field\" title=\"" + description + "\"><span class=\"label\">" + label
                + "</span><span class=\"separator\">: </span> " + field + optional + error + "</div>"));
    }

    public void addField(
            final NakedObjectSpecification spec,
            final String label,
            final String title,
            final String field,
            final String value,
            final int noLines,
            final boolean wrap,
            final int maxLength,
            final int typicalLength,
            final boolean required,
            final String error) {
        String inputField;
        /*
         * REVIEW the following qualification are a bit limited - it's the simplest thing than will work - we
         * need to determine from the specification whether something is boolean type or a password.
         * 
         * Also see the note in the Form I/F.
         */
        boolean ignoreMandatory = false;
        if (spec.isOfType(NakedObjectsContext.getSpecificationLoader().loadSpecification(boolean.class))
                || spec.isOfType(NakedObjectsContext.getSpecificationLoader().loadSpecification(Boolean.class))) {
            final String selected = (value != null && value.toLowerCase().equals("true")) ? "checked " : "";
            inputField = "<input class=\"value\" type=\"checkbox\" name=\"" + field + "\"" + selected + " value=\"true\"/>";
            ignoreMandatory = true;
        } else if (spec.getFullName().endsWith(".Password")) {
            final String typicalLengthStr = typicalLength == 0 ? "" : (" size=\"" + typicalLength + "\"");
            final String maxLengthStr = maxLength == 0 ? "" : (" maxlength=\"" + maxLength + "\"");
            inputField = "<input class=\"value\" type=\"password\" name=\"" + field + "\"" + typicalLengthStr + maxLengthStr
                    + "value=\"" + value + "\"/>";

        } else if (noLines > 1) {
            final int w = typicalLength > 0 ? typicalLength / noLines : 50;
            inputField = "<textarea class=\"value\" type=\"text\" name=\"" + field + "\" rows=\"" + noLines + "\" cols=\"" + w
                    + "\" wrap=\"" + (wrap ? "hard" : "off") + "\">" + value + "</textarea>";
        } else {
            final String typicalLengthStr = typicalLength == 0 ? "" : (" size=\"" + typicalLength + "\"");
            final String maxLengthStr = maxLength == 0 ? "" : (" maxlength=\"" + maxLength + "\"");
            inputField = "<input class=\"value\" type=\"text\" name=\"" + field + "\"" + typicalLengthStr + maxLengthStr
                    + "value=\"" + value + "\"/>";
        }
        addField(label, inputField, title, ignoreMandatory, required, error);
    }

    public void addFieldName(final String fieldLabel) {
        add(new Heading(fieldLabel, 4));
    }

    public void addLookup(
            final String fieldLabel,
            final String description,
            final String fieldId,
            final int selectedIndex,
            final String[] instances,
            final String[] ids,
            final boolean required,
            final String errorMessage) {
        final StringBuffer testInputField = new StringBuffer();
        testInputField.append("<select class=\"value\" name=\"" + fieldId + "\">");
        if (!required) {
            testInputField.append("<option");
            if (selectedIndex < 0) {
                testInputField.append(" selected");
            }
            testInputField.append(" value=\"");
            testInputField.append("null");
            testInputField.append("\" >[not set]</option>");
        }
        for (int i = 0; i < instances.length; i++) {
            testInputField.append("<option");
            if (i == selectedIndex) {
                testInputField.append(" selected");
            }
            testInputField.append(" value=\"");
            testInputField.append(ids[i]);
            testInputField.append("\" >");
            testInputField.append(instances[i]);
            testInputField.append("</option>");
        }
        testInputField.append("</select>");

        addField(fieldLabel, testInputField.toString(), description, false, required, errorMessage);
    }

    public void addReadOnlyField(final String fieldLabel, final String title, final String description) {
        addField(fieldLabel, "<span class=\"value\">" + title + "</span>", description, true, false, null);
    }

    @Override
    public void write(final PrintWriter writer) {
        super.write(writer);
    }

    @Override
    protected void writeAfter(final PrintWriter writer) {
        writer.println("<div class=\"field\">");
        if (hasNext) {
            writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Next\"/>");
            if (isEditing) {
                writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Save\"/>");
            } else {
                writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Finish\"/>");
            }
        } else {
            if (isEditing) {
                writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Save\"/>");
            } else {
                writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Ok\"/>");
            }
        }
        if (hasPrevious) {
            writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Previous\"/>");
        }
        writer.println("<input class=\"action-button\" type=\"submit\" name=\"button\" value=\"Cancel\"/>");
        writer.println("</div>");
        writer.println("</form>");
    }

    @Override
    protected void writeBefore(final PrintWriter writer) {
        writer.print("<form name=\"form\" action=\"");
        writer.print("task");
        writer.print(".app");
        writer.print("\" method=\"post\"");
        if (confirm) {
            writer.print(" onSubmit=\"return confirm('Are you sure')\"");
        }
        writer.println(">");
        writer.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
    }
}

// Copyright (c) Naked Objects Group Ltd.
