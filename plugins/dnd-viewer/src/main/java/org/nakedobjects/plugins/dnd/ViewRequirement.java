package org.nakedobjects.plugins.dnd;

public class ViewRequirement {
    public static final int NONE = 0;
    public static final int CLOSED = 0x01;
    public static final int SUMMARY = 0x02;
    public static final int OPEN = 0x04;
    
    public static final int EDITABLE = 0x10;
    
    public static final int REPLACEABLE = 0x100;
    public static final int EXPANDABLE = 0x200;
    
    public static final int ROOT = 0x1000;
    public static final int SUBVIEW = 0x2000;
    
    private final Content content;
    private final ViewAxis viewAxis;
    private final int status;
    
    public ViewRequirement(Content content, ViewAxis viewAxis, int status) {
        this.content = content;
        this.viewAxis = viewAxis;
        this.status = status;
        status = CLOSED;
    }
    
    public ViewRequirement(Content content, int status) {
        this(content, null, status);
    }

    public Content getContent() {
        return content;
    }

    public ViewAxis getAxis() {
        return viewAxis;
    }
    
    public boolean is(int status) {
        return (this.status & status) == status;
    }

    public boolean isClosed() {
        return is(CLOSED);
    }

    public boolean isEditable() {
        return is(EDITABLE);
    }

}


// Copyright (c) Naked Objects Group Ltd.
