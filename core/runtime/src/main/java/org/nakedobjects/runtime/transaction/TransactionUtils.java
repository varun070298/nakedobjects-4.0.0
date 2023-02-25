package org.nakedobjects.runtime.transaction;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.runtime.transaction.facets.CollectionClearFacetWrapTransaction;


public class TransactionUtils {
    private final static Logger LOG = Logger.getLogger(CollectionClearFacetWrapTransaction.class);

    private TransactionUtils() {}

    public static void abort(final NakedObjectTransactionManager transactionManager, final FacetHolder holder) {
        LOG.info("exception executing " + holder + ", aborting transaction");
        try {
            transactionManager.abortTransaction();
        } catch (final Exception e2) {
            LOG.error("failure during abort", e2);
        }
    }

    /**
     * TODO: need to downcast the FacetHolder and fetch out an identifier.
     */
    public static String getIdentifierFor(final FacetHolder facetHolder) {
        return facetHolder.toString();
    }

}

// Copyright (c) Naked Objects Group Ltd.
