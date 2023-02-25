package org.nakedobjects.plugins.dnd;

public interface Feedback {

    void showException(final Throwable e);

    void showArrowCursor();

    void showCrosshairCursor();

    void showDefaultCursor();

    void showTextCursor();

    void showHandCursor();

    void showMoveCursor();

    void showResizeDownCursor();

    void showResizeDownLeftCursor();

    void showResizeDownRightCursor();

    void showResizeLeftCursor();

    void showResizeRightCursor();

    void showResizeUpCursor();

    void showResizeUpLeftCursor();

    void showResizeUpRightCursor();

    void setBusy(final View view, BackgroundTask task);

    void clearBusy(final View view);

    boolean isBusy(View view);

    String getStatusBarOutput();

    void showMessagesAndWarnings();

    void setViewDetail(String string);

    void setAction(String actionText);

    void addMessage(String string);

    void setError(String string);

    void clearAction();

    void clearError();

    void showBusyState(View view);

}

// Copyright (c) Naked Objects Group Ltd.
