package org.nakedobjects.runtime.i18n.resourcebundle;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;


public class ResourceBasedI18nManager implements I18nManager {

    private static final Logger LOG = Logger.getLogger(ResourceBasedI18nManager.class);

    private static final String BASE_FILE_NAME = "i18n";

    private static final String PARAMETER = "parameter";
    private static final String DESCRIPTION = "description";
    private static final String PROPERTY = "property";
    private static final String NAME = "name";
    private static final String HELP = "help";
    private static final String ACTION = "action";

    private ResourceBundle bundle;

    @SuppressWarnings("unused")
    private NakedObjectConfiguration configuration;

    public ResourceBasedI18nManager(NakedObjectConfiguration configuration) {
        this.configuration = configuration;
    }

    public void init() {
        try {
            bundle = ResourceBundle.getBundle(BASE_FILE_NAME);
        } catch (final MissingResourceException e) {
            LOG.warn("Missing resource bundle: " + e.getMessage());
        }

    }

    public void shutdown() {}

    public String getName(final Identifier identifier) {
        return text(identifier, NAME);
    }

    public String getDescription(final Identifier identifier) {
        return text(identifier, DESCRIPTION);
    }

    public String getHelp(final Identifier identifier) {
        return text(identifier, HELP);
    }

    // TODO allow description and help to be found for parameters
    public String[] getParameterNames(final Identifier identifier) {
        if (bundle == null) {
            return null;
        } else {
            final String[] array = new String[identifier.getMemberParameterNames().length];
            for (int i = 0; i < array.length; i++) {
                final String key = identifier.getClassName() + "." + ACTION + "." + identifier.getMemberName() + "." + PARAMETER
                        + (i + 1) + "." + NAME;
                try {
                    array[i] = bundle.getString(key);
                } catch (final MissingResourceException e) {
                    array[i] = null;
                }
            }
            return array;
        }
    }

    private String text(final Identifier identifier, final String type) {
        if (bundle == null) {
            return null;
        } else {
            final String form = identifier.isPropertyOrCollection() ? PROPERTY : ACTION;
            final String key = identifier.getClassName() + "." + form + "." + identifier.getMemberName() + "." + type;
            try {
                return bundle.getString(key);
            } catch (final MissingResourceException e) {
                return null;
            }
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
