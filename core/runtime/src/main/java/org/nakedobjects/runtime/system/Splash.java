package org.nakedobjects.runtime.system;

public enum Splash {
    SHOW(true),
    NO_SHOW(false);

    private final boolean show;

    private Splash(final boolean show) {
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }

    public static Splash lookup(boolean showSplash) {
        return showSplash ? Splash.SHOW : Splash.NO_SHOW;
    }
}
