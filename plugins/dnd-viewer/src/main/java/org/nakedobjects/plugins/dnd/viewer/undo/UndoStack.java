package org.nakedobjects.plugins.dnd.viewer.undo;

import java.util.Vector;


public class UndoStack {
    
    private final Vector<Command> commands = new Vector<Command>();

    public void add(final Command command) {
        commands.addElement(command);
        command.execute();
    }

    public void undoLastCommand() {
        final Command lastCommand = commands.lastElement();
        lastCommand.undo();
        commands.removeElement(lastCommand);
    }

    public String descriptionOfUndo() {
        final Command lastCommand = commands.lastElement();
        return lastCommand.getDescription();
    }

    public boolean isEmpty() {
        return commands.isEmpty();
    }

    public String getNameOfUndo() {
        final Command lastCommand = commands.lastElement();
        return lastCommand.getName();
    }
}
// Copyright (c) Naked Objects Group Ltd.
