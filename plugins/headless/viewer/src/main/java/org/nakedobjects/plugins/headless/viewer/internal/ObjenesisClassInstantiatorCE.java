package org.nakedobjects.plugins.headless.viewer.internal;

import org.objenesis.ObjenesisHelper;


class ObjenesisClassInstantiatorCE implements IClassInstantiatorCE {

    public Object newInstance(final Class<?> clazz) throws InstantiationException {
        return ObjenesisHelper.newInstance(clazz);
    }

}
