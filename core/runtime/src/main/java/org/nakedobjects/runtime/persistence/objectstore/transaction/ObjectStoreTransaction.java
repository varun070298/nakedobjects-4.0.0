package org.nakedobjects.runtime.persistence.objectstore.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStoreTransactionManagement;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionAbstract;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.PersistenceCommand;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;


public class ObjectStoreTransaction extends NakedObjectTransactionAbstract  {
    private static final Logger LOG = Logger.getLogger(ObjectStoreTransaction.class);
    
    private final ObjectStoreTransactionManagement objectStore;
    private final List<PersistenceCommand> commands = new ArrayList<PersistenceCommand>();
    
    public ObjectStoreTransaction(           
            final NakedObjectTransactionManager transactionManager, 
            final MessageBroker messageBroker, 
            final UpdateNotifier updateNotifier,
            final ObjectStoreTransactionManagement objectStore) {
        super(transactionManager, messageBroker, updateNotifier);
        this.objectStore = objectStore;
        if (LOG.isDebugEnabled()) {
            LOG.debug("new transaction " + this);
        }
    }

    
    ////////////////////////////////////////////////////////////
    // Commands
    ////////////////////////////////////////////////////////////

    /**
     * Add the non-null command to the list of commands to execute at the end of the transaction.
     */
    public void addCommand(final PersistenceCommand command) {
        if (command == null) {
            return;
        }

        final NakedObject onObject = command.onObject();

        // Saves are ignored when preceded by another save, or a delete
        if (command instanceof SaveObjectCommand) {
            if (alreadyHasCreate(onObject) || alreadyHasSave(onObject)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ignored command as object already created/saved" + command);
                }
                return;
            }

            if (alreadyHasDestroy(onObject)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("ignored command " + command + " as object no longer exists");
                }
                return;
            }
        }

        // Destroys are ignored when preceded by a create
        if (command instanceof DestroyObjectCommand) {
            if (alreadyHasCreate(onObject)) {
                removeCreate(onObject);
                if (LOG.isInfoEnabled()) {
                    LOG.info("ignored both create and destroy command " + command);
                }
                return;
            }

            if (alreadyHasSave(onObject)) {
                removeSave(onObject);
                if (LOG.isInfoEnabled()) {
                    LOG.info("removed prior save command " + command);
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("add command " + command);
        }
        commands.add(command);
    }

    
    ////////////////////////////////////////////////////////////
    // abort
    ////////////////////////////////////////////////////////////
    
    @Override
    public void doAbort() {
    }


    ////////////////////////////////////////////////////////////
    // commit 
    ////////////////////////////////////////////////////////////

    @Override
    public void doFlush() {

        if (commands.size() > 0) {
            objectStore.execute(Collections.unmodifiableList(commands));
            
            for (final PersistenceCommand command: commands) {
                if (command instanceof DestroyObjectCommand) {
                    final NakedObject adapter = command.onObject();
                    adapter.setOptimisticLock(null);
                    adapter.changeState(ResolveState.DESTROYED);
                }
            }
            commands.clear();
        }
    }

    
    ////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////

    private boolean alreadyHasCommand(final Class<?> commandClass, final NakedObject onObject) {
        return getCommand(commandClass, onObject) != null;
    }

    private boolean alreadyHasCreate(final NakedObject onObject) {
        return alreadyHasCommand(CreateObjectCommand.class, onObject);
    }

    private boolean alreadyHasDestroy(final NakedObject onObject) {
        return alreadyHasCommand(DestroyObjectCommand.class, onObject);
    }

    private boolean alreadyHasSave(final NakedObject onObject) {
        return alreadyHasCommand(SaveObjectCommand.class, onObject);
    }

    private PersistenceCommand getCommand(final Class<?> commandClass, final NakedObject onObject) {
        for (PersistenceCommand command: commands) {
            if (command.onObject().equals(onObject)) {
                if (commandClass.isAssignableFrom(command.getClass())) {
                    return command;
                }
            }
        }
        return null;
    }

    private void removeCommand(final Class<?> commandClass, final NakedObject onObject) {
        final PersistenceCommand toDelete = getCommand(commandClass, onObject);
        commands.remove(toDelete);
    }

    private void removeCreate(final NakedObject onObject) {
        removeCommand(CreateObjectCommand.class, onObject);
    }

    private void removeSave(final NakedObject onObject) {
        removeCommand(SaveObjectCommand.class, onObject);
    }

    @Override
    protected ToString appendTo(final ToString str) {
        super.appendTo(str);
        str.append("commands", commands.size());
        return str;
    }

    
}
// Copyright (c) Naked Objects Group Ltd.
