package org.nakedobjects.plugins.dnd;

public class KeyboardAction {
    public static final int NONE = 0;
    public static final int ABORT = 1;
    public static final int NEXT_VIEW = 2;
    public static final int NEXT_WINDOW = 3;
    public static final int PREVIOUS_VIEW = 4;
    public final static int PREVIOUS_WINDOW = 5;

    final int keyCode;
    final int modifiers;
    private boolean isConsumed;

    public KeyboardAction(final int keyCode, final int modifiers) {
        this.keyCode = keyCode;
        this.modifiers = modifiers;
        isConsumed = false;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void consume() {
        isConsumed = true;
    }

}
// Copyright (c) Naked Objects Group Ltd.
