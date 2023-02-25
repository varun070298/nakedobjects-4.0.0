package org.nakedobjects.plugins.html.component.html;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentFactory;
import org.nakedobjects.plugins.html.component.DebugPane;
import org.nakedobjects.plugins.html.component.Form;
import org.nakedobjects.plugins.html.component.Page;
import org.nakedobjects.plugins.html.component.Table;
import static org.nakedobjects.plugins.html.viewer.HtmlViewerConstants.*;


public class HtmlComponentFactory implements ComponentFactory {

    private final String footer;
    private final String header;
    private final String styleSheet;

    public HtmlComponentFactory() {
        final NakedObjectConfiguration configuration = NakedObjectsContext.getConfiguration();
        styleSheet = configuration.getString(STYLE_SHEET);
        String file = configuration.getString(HEADER_FILE);
        header = file == null ? configuration.getString(HEADER) : loadFile(file);
        file = configuration.getString(FOOTER_FILE);
        footer = file == null ? configuration.getString(FOOTER) : loadFile(file);
    }

    public Block createBlock(final String style, final String description) {
        return new Div(style, description);
    }

    public Component createBreadCrumbs(final String[] names, final boolean[] isLinked) {
        return new BreadCrumbs(names, isLinked);
    }

    public Component createCollectionIcon(final NakedObjectAssociation field, final NakedObject collection, final String id) {
        return new CollectionLink(field, collection, field.getDescription(), id);
    }

    public DebugPane createDebugPane() {
        return new HtmlDebug();
    }

    public Component createEditOption(final String id) {
        return new ActionComponent("edit", "Edit Object", "Edit the current object", id, null, null);
    }

    public Component createRemoveOption(final String id, final String elementId, final String fieldName) {
        return new ActionComponent("remove", "Remove", "Remove item from collection", id, elementId, fieldName);
    }

    public Component createAddOption(final String id, final String fieldName) {
        return new ActionComponent("add", "Add Item", "Add item to collection", id, null, fieldName);
    }

    public Component createErrorMessage(final Exception e, final boolean isDebug) {
        return new ErrorMessage(e, isDebug);
    }

    public Form createForm(final String id, final String actionName, final int step, final int noOfPages, final boolean isEditing) {
        return new HtmlForm(id, actionName, step, noOfPages, isEditing);
    }

    public Component createHeading(final String name) {
        return new Heading(name, 4);
    }

    public Component createInlineBlock(final String style, final String text, final String description) {
        return new Span(style, text, description);
    }

    public Component createCheckboxBlock(final boolean isEditable, final boolean isSet) {
        return new Checkbox(isSet, isEditable);
    }

    public Component createSubmenu(final String menuName, final Component[] items) {
        return new Submenu(menuName, items);
    }

    public Component createLink(final String link, final String name, final String description) {
        return new Link(link, name, description);
    }

    public Component createMenuItem(
            final String actionId,
            final String name,
            final String description,
            final String reasonDisabled,
            final NakedObjectActionType type,
            final boolean hasParameters,
            final String targetObjectId) {
        return new MenuItem(actionId, name, description, reasonDisabled, type, hasParameters, targetObjectId);
    }

    public Component createCollectionIcon(final NakedObject collection, final String collectionId) {
        return new CollectionIcon(collection, collection.getSpecification().getDescription(), collectionId);
    }

    public Component createObjectIcon(final NakedObject object, final String objectId, final String style) {
        return new ObjectIcon(object, object.getSpecification().getDescription(), objectId, style);
    }

    public Component createObjectIcon(
            final NakedObjectAssociation field,
            final NakedObject object,
            final String objectId,
            final String style) {
        return new ObjectIcon(object, field.getDescription(), objectId, style);
    }

    public Page createPage() {
        return new DynamicHtmlPage(styleSheet, header, footer);
    }

    public LogonFormPage createLogonPage(final String user, final String password) {
        return new LogonFormPage(styleSheet, header, footer, user, password);
    }

    public Component createService(final String objectId, final String title, final String iconName) {
        return new ServiceComponent(objectId, title, iconName);
    }

    public Table createTable(final int noColumns, final boolean withSelectorColumn) {
        return new HtmlTable(noColumns, withSelectorColumn);
    }

    public Component createUserSwap(final String name) {
        return new UserSwapLink(name);

    }

    public Component createParseableField(final NakedObjectAssociation field, final NakedObject value, final boolean isEditable) {
        final BooleanValueFacet facet = field.getSpecification().getFacet(BooleanValueFacet.class);
        if (facet != null) {
            return createCheckboxBlock(isEditable, facet.isSet(value));
        } else {
            final String titleString = value != null ? value.titleString() : "";

            final MultiLineFacet multiLineFacet = field.getSpecification().getFacet(MultiLineFacet.class);
            final boolean isWrapped = multiLineFacet != null && !multiLineFacet.preventWrapping();

            if (isWrapped) {
                return createInlineBlock("value", "<pre>" + titleString + "</pre>", null);
            } else {
                return createInlineBlock("value", titleString, null);
            }
        }
    }

    private String loadFile(final String file) {
        final StringBuffer content = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
        } catch (final FileNotFoundException e) {
            throw new WebViewerException("Failed to find file " + file);
        } catch (final IOException e) {
            throw new WebViewerException("Failed to load file " + file, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException ignore) {}
            }
        }
        return content.toString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
