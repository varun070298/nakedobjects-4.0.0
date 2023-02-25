package org.nakedobjects.metamodel.facets.actions.executed;

import org.nakedobjects.metamodel.facets.EnumerationAbstract;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.Target;


/**
 * Whether the action should be invoked locally, remotely, or on the default location depending on its
 * persistence state.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to annotating the action method using
 * <tt>@Executed</tt>.
 */
public interface ExecutedFacet extends Facet {

    public static final class Where extends EnumerationAbstract {

        public static Where DEFAULT = new Where(0, "DEFAULT", "Default");
        public static Where LOCALLY = new Where(1, "LOCAL", "Locally");
        public static Where REMOTELY = new Where(2, "REMOTE", "Remotely");

        private Where(final int num, final String nameInCode, final String friendlyName) {
            super(num, nameInCode, friendlyName);
        }

    }

    public Where value();

    public Target getTarget();
}
