package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.runtime.testsystem.TestProxySystem;


public class ExportTestObjects {

    public static void main(final String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        final TestProxySystem system = new TestProxySystem();
        system.init();
        // system.addSpecification(BiDirectional.class.getName());
        // system.addSpecification(SimpleSubClass.class.getName());
        String dir = ".";
        if (args.length > 0) {
            dir = args[0];
        }
        new Nof2HbmXml().exportHbmXml(dir);
        system.shutdown();
    }
}
// Copyright (c) Naked Objects Group Ltd.
