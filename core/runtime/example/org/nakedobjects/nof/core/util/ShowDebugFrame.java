package org.nakedobjects.nof.core.util;


//import org.nakedobjects.noa.util.DebugInfo;
//import org.nakedobjects.noa.util.DebugString;
/*
 * QualitaCorpus.class: we removed the lines above
 * because they are unnecessary.
 */

public class ShowDebugFrame {
    public static void main(final String[] args) {
        DebugFrame frame = new DebugFrame() {
            DebugInfo info1 = new DebugInfo() {
                public void debugData(final DebugString debug) {
                    debug.appendln("Debug data");
                }

                public String debugTitle() {
                    return "Debug title";
                }
            };

            DebugInfo info2 = new DebugInfo() {
                public void debugData(final DebugString debug) {
                    debug.appendln("Debug data 2");
                }

                public String debugTitle() {
                    return "Debug title 2";
                }
            };

            DebugInfo info3 = new DebugInfo() {
                public void debugData(final DebugString debug) {
                    debug.appendln("Debug data 3");
                }

                public String debugTitle() {
                    return "Debug 3";
                }
            };

            protected DebugInfo[] getInfo() {
                return new DebugInfo[] { info1, info2, info3 };
            }
        };

        frame.show(10, 10);
    }
}
// Copyright (c) Naked Objects Group Ltd.
