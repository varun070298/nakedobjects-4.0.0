package org.nakedobjects.runtime.persistence.objectstore.algorithm.topdown;

import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmContractTest;


public class TopDownPersistAlgorithmContractTest extends PersistAlgorithmContractTest {

    @Override
    protected PersistAlgorithm createPersistAlgorithm() {
        return new TopDownPersistAlgorithm();
    }

}

// Copyright (c) Naked Objects Group Ltd.
