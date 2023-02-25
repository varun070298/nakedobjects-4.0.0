package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.spec.Target;

public class NakedObjectActionConstants {

    public final static Target DEFAULT = new Target("DEFAULT");
    public final static Target LOCAL = new Target("LOCAL");
    public final static Target REMOTE = new Target("REMOTE");
    public final static NakedObjectActionType DEBUG = NakedObjectActionType.DEBUG;
    public final static NakedObjectActionType EXPLORATION = NakedObjectActionType.EXPLORATION;
    public final static NakedObjectActionType SET = NakedObjectActionType.SET;
    public final static NakedObjectActionType USER = NakedObjectActionType.USER;
}
