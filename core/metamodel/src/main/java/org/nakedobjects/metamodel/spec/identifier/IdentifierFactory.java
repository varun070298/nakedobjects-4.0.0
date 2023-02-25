package org.nakedobjects.metamodel.spec.identifier;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.commons.ensure.Ensure;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public class IdentifierFactory {

    private IdentifierFactory() {}

    /**
     * Factory method.
     * 
     * @see #toIdentityString(int)
     */
    public static Identifier fromIdentityString(final String asString) {
        Ensure.ensureThatArg(asString, is(not(nullValue())));
        final int indexOfHash = asString.indexOf("#");
        final int indexOfOpenBracket = asString.indexOf("(");
        final int indexOfCloseBracket = asString.indexOf(")");
        final String className = asString.substring(0, indexOfHash == -1 ? asString.length() : indexOfHash);
        if (indexOfHash == -1 || indexOfHash == (asString.length() - 1)) {
            return Identifier.classIdentifier(className);
        }
        String name = null;
        if (indexOfOpenBracket == -1) {
            name = asString.substring(indexOfHash + 1);
            return Identifier.propertyOrCollectionIdentifier(className, name);
        }
        List<String> parmList = new ArrayList<String>();
        name = asString.substring(indexOfHash + 1, indexOfOpenBracket);
        final String allParms = asString.substring(indexOfOpenBracket + 1, indexOfCloseBracket).trim();
        if (allParms.length() > 0) {
            // use StringTokenizer for .NET compatibility
            final StringTokenizer tokens = new StringTokenizer(allParms, ",", false);
            for (int i = 0; tokens.hasMoreTokens(); i++) {
                String nextParam = tokens.nextToken();
                parmList.add(nextParam);
            }
        }
        return Identifier.actionIdentifier(className, name, parmList.toArray(new String[]{}));
    }

    /**
     * Helper, used within contructor chaining
     */
    private static String[] toParameterStringArray(final NakedObjectSpecification[] specifications) {
        final String[] parameters = new String[specifications == null ? 0 : specifications.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = specifications[i].getFullName();
        }
        return parameters;
    }

    public static Identifier create(
            final String className,
            final String methodName,
            final NakedObjectSpecification[] specifications) {
        return Identifier.actionIdentifier(className, methodName, toParameterStringArray(specifications));
    }

}
// Copyright (c) Naked Objects Group Ltd.
