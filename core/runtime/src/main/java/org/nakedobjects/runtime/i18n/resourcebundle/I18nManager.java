package org.nakedobjects.runtime.i18n.resourcebundle;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;


/**
 * Authorises the user in the current session view and use members of an object.
 */
public interface I18nManager extends ApplicationScopedComponent {

    /**
     * Get the localized description for the specified identified action/property. Returns null if no
     * description available.
     */
    String getDescription(Identifier identifier);

    /**
     * Get the localized name for the specified identified action/property. Returns null if no name available.
     */
    String getName(Identifier identifier);

    /**
     * Get the localized help text for the specified identified action/property. Returns null if no help text
     * available.
     */
    String getHelp(Identifier identifier);

    /**
     * Get the localized parameter names for the specified identified action/property. Returns null if no
     * parameters are available. Otherwise returns an array of String objects the size of the number of
     * parameters, where each element is the localised name for the corresponding parameter, or is null if no
     * parameter name is available.
     */
    String[] getParameterNames(Identifier identifier);
}
// Copyright (c) Naked Objects Group Ltd.
