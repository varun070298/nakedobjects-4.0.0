package org.nakedobjects.runtime.persistence.objectstore.algorithm.dflt;

import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmContractTest;


public class DefaultPersistAlgorithmContractTest extends PersistAlgorithmContractTest {

    @Override
    protected PersistAlgorithm createPersistAlgorithm() {
        return new DefaultPersistAlgorithm();
    }

}

// Copyright (c) Naked Objects Group Ltd.
