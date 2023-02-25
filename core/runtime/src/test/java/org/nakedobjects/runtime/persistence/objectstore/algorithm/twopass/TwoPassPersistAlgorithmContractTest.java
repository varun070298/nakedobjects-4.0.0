package org.nakedobjects.runtime.persistence.objectstore.algorithm.twopass;

import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.PersistAlgorithmContractTest;


public class TwoPassPersistAlgorithmContractTest extends PersistAlgorithmContractTest {

    @Override
    protected PersistAlgorithm createPersistAlgorithm() {
        return new TwoPassPersistAlgorithm();
    }

}

// Copyright (c) Naked Objects Group Ltd.
