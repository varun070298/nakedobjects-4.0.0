package org.nakedobjects.runtime.transaction.messagebroker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.StringUtils;


public class MessageBrokerDefault implements MessageBroker, DebugInfo {
    
    private final List<String> messages = new ArrayList<String>();
    private final List<String> warnings = new ArrayList<String>();

    public MessageBrokerDefault() {
    }

    
    ////////////////////////////////////////////////////
    // Reset / ensureEmpty
    ////////////////////////////////////////////////////

    public void reset() {
        warnings.clear();
        messages.clear();
    }
    
    public void ensureEmpty() {
        if (warnings.size() > 0) {
            throw new NakedObjectException("Message broker still has warnings");
        }
        if (messages.size() > 0) {
            throw new NakedObjectException("Message broker still has messages");
        }
    }

    ////////////////////////////////////////////////////
    // Messages
    ////////////////////////////////////////////////////
    
    public List<String> getMessages() {
        return copyAndClear(messages);
    }

    public void addMessage(final String message) {
        messages.add(message);
    }

    public String getMessagesCombined() {
        List<String> x = messages;
        String string = StringUtils.combine(x);
        return string;
    }


    ////////////////////////////////////////////////////
    // Warnings
    ////////////////////////////////////////////////////

    public List<String> getWarnings() {
        return copyAndClear(warnings);
    }

    public void addWarning(final String message) {
        warnings.add(message);
    }
    
    public String getWarningsCombined() {
        List<String> x = warnings;
        String string = StringUtils.combine(x);
        return string;
    }

    ////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debugArray(debug, "Messages", messages);
        debugArray(debug, "Warnings", messages);
    }

    private void debugArray(final DebugString debug, final String title, final List<String> vector) {
        debug.appendln(title);
        debug.indent();
        if (vector.size() == 0) {
            debug.appendln("none");
        } else {
            for(String text: vector) {
                debug.appendln(text);
            }
        }
        debug.unindent();
    }

    public String debugTitle() {
        return "Simple Message Broker";
    }


    ////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////

    private List<String> copyAndClear(List<String> messages) {
        List<String> copy = Collections.unmodifiableList(new ArrayList<String>(messages));
        messages.clear();
        return copy;
    }



}
// Copyright (c) Naked Objects Group Ltd.
