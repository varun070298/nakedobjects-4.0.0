package org.nakedobjects.plugins.html.request;

import javax.servlet.http.HttpServletRequest;




public class ServletRequest implements Request {
    private Request forwardRequest;
    private final HttpServletRequest request;
    private final String requestType;

    public ServletRequest(final HttpServletRequest request) {
        this.request = request;

        final String path = request.getServletPath();
        final int from = path.lastIndexOf('/');
        final int to = path.lastIndexOf('.');
        requestType = path.substring(from + 1, to);
    }

    public void forward(final Request forwardRequest) {
        this.forwardRequest = forwardRequest;
    }

    public String getActionId() {
        return request.getParameter("action");
    }

    public String getProperty() {
        return request.getParameter("field");
    }

    public String getElementId() {
        return request.getParameter("element");
    }

    public String getFieldEntry(final int i) {
        return request.getParameter("fld" + i);
    }

    public String getTaskId() {
        return request.getParameter("id");
    }

    public Request getForward() {
        return forwardRequest;
    }

    public String getName() {
        return request.getParameter("name");
    }

    public String getObjectId() {
        return request.getParameter("id");
    }

    public String getRequestType() {
        return requestType;
    }

    public String getButtonName() {
        return request.getParameter("button");
    }

    @Override
    public String toString() {
        return "ServletRequest " + request.getRequestURI() + "?" + request.getQueryString();
    }

}

// Copyright (c) Naked Objects Group Ltd.
