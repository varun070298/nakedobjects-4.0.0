package org.nakedobjects.plugins.headless.viewer.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nakedobjects.applib.events.ActionArgumentEvent;
import org.nakedobjects.applib.events.ActionInvocationEvent;
import org.nakedobjects.applib.events.ActionUsabilityEvent;
import org.nakedobjects.applib.events.ActionVisibilityEvent;
import org.nakedobjects.applib.events.CollectionAccessEvent;
import org.nakedobjects.applib.events.CollectionAddToEvent;
import org.nakedobjects.applib.events.CollectionMethodEvent;
import org.nakedobjects.applib.events.CollectionRemoveFromEvent;
import org.nakedobjects.applib.events.CollectionUsabilityEvent;
import org.nakedobjects.applib.events.CollectionVisibilityEvent;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.ObjectTitleEvent;
import org.nakedobjects.applib.events.ObjectValidityEvent;
import org.nakedobjects.applib.events.PropertyAccessEvent;
import org.nakedobjects.applib.events.PropertyModifyEvent;
import org.nakedobjects.applib.events.PropertyUsabilityEvent;
import org.nakedobjects.applib.events.PropertyVisibilityEvent;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.ViewObject;
import org.nakedobjects.plugins.headless.applib.listeners.InteractionListener;

public class HeadlessViewerImpl implements HeadlessViewer {

    private final List<InteractionListener> listeners = new ArrayList<InteractionListener>();
    private final Map<Class<? extends InteractionEvent>, InteractionEventDispatcher> dispatchersByEventClass = new HashMap<Class<? extends InteractionEvent>, InteractionEventDispatcher>();

	private final RuntimeContext runtimeContext;

    public HeadlessViewerImpl(final RuntimeContext runtimeContext) {
    	this.runtimeContext = runtimeContext;
        dispatchersByEventClass.put(ObjectTitleEvent.class, new InteractionEventDispatcherTypeSafe<ObjectTitleEvent>() {
            @Override
            public void dispatchTypeSafe(final ObjectTitleEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.objectTitleRead(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(PropertyVisibilityEvent.class,
                new InteractionEventDispatcherTypeSafe<PropertyVisibilityEvent>() {
                    @Override
                    public void dispatchTypeSafe(final PropertyVisibilityEvent interactionEvent) {
                        for (final InteractionListener l : getListeners()) {
                            l.propertyVisible(interactionEvent);
                        }
                    }
                });
        dispatchersByEventClass.put(PropertyUsabilityEvent.class,
                new InteractionEventDispatcherTypeSafe<PropertyUsabilityEvent>() {
                    @Override
                    public void dispatchTypeSafe(final PropertyUsabilityEvent interactionEvent) {
                        for (final InteractionListener l : getListeners()) {
                            l.propertyUsable(interactionEvent);
                        }
                    }
                });
        dispatchersByEventClass.put(PropertyAccessEvent.class, new InteractionEventDispatcherTypeSafe<PropertyAccessEvent>() {
            @Override
            public void dispatchTypeSafe(final PropertyAccessEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.propertyAccessed(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(PropertyModifyEvent.class, new InteractionEventDispatcherTypeSafe<PropertyModifyEvent>() {
            @Override
            public void dispatchTypeSafe(final PropertyModifyEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.propertyModified(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(CollectionVisibilityEvent.class,
                new InteractionEventDispatcherTypeSafe<CollectionVisibilityEvent>() {
                    @Override
                    public void dispatchTypeSafe(final CollectionVisibilityEvent interactionEvent) {
                        for (final InteractionListener l : getListeners()) {
                            l.collectionVisible(interactionEvent);
                        }
                    }
                });
        dispatchersByEventClass.put(CollectionUsabilityEvent.class,
                new InteractionEventDispatcherTypeSafe<CollectionUsabilityEvent>() {
                    @Override
                    public void dispatchTypeSafe(final CollectionUsabilityEvent interactionEvent) {
                        for (final InteractionListener l : getListeners()) {
                            l.collectionUsable(interactionEvent);
                        }
                    }
                });
        dispatchersByEventClass.put(CollectionAccessEvent.class, new InteractionEventDispatcherTypeSafe<CollectionAccessEvent>() {
            @Override
            public void dispatchTypeSafe(final CollectionAccessEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.collectionAccessed(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(CollectionAddToEvent.class, new InteractionEventDispatcherTypeSafe<CollectionAddToEvent>() {
            @Override
            public void dispatchTypeSafe(final CollectionAddToEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.collectionAddedTo(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(CollectionRemoveFromEvent.class,
                new InteractionEventDispatcherTypeSafe<CollectionRemoveFromEvent>() {
                    @Override
                    public void dispatchTypeSafe(final CollectionRemoveFromEvent interactionEvent) {
                        for (final InteractionListener l : getListeners()) {
                            l.collectionRemovedFrom(interactionEvent);
                        }
                    }
                });
        dispatchersByEventClass.put(ActionVisibilityEvent.class, new InteractionEventDispatcherTypeSafe<ActionVisibilityEvent>() {
            @Override
            public void dispatchTypeSafe(final ActionVisibilityEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.actionVisible(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(ActionUsabilityEvent.class, new InteractionEventDispatcherTypeSafe<ActionUsabilityEvent>() {
            @Override
            public void dispatchTypeSafe(final ActionUsabilityEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.actionUsable(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(ActionArgumentEvent.class, new InteractionEventDispatcherTypeSafe<ActionArgumentEvent>() {
            @Override
            public void dispatchTypeSafe(final ActionArgumentEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.actionArgument(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(ActionInvocationEvent.class, new InteractionEventDispatcherTypeSafe<ActionInvocationEvent>() {
            @Override
            public void dispatchTypeSafe(final ActionInvocationEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.actionInvoked(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(ObjectValidityEvent.class, new InteractionEventDispatcherTypeSafe<ObjectValidityEvent>() {
            @Override
            public void dispatchTypeSafe(final ObjectValidityEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.objectPersisted(interactionEvent);
                }
            }
        });
        dispatchersByEventClass.put(CollectionMethodEvent.class, new InteractionEventDispatcherTypeSafe<CollectionMethodEvent>() {
            @Override
            public void dispatchTypeSafe(final CollectionMethodEvent interactionEvent) {
                for (final InteractionListener l : getListeners()) {
                    l.collectionMethodInvoked(interactionEvent);
                }
            }
        });
    }

    // /////////////////////////////////////////////////////////////
    // Views
    // /////////////////////////////////////////////////////////////

    public <T> T view(final T domainObject) {
        return view(domainObject, ExecutionMode.EXECUTE);
    }

    public <T> T view(final T domainObject, ExecutionMode mode) {
        if (isView(domainObject)) {
            return domainObject;
        }
        return Proxy.proxy(domainObject, this, mode, runtimeContext);
    }

    public boolean isView(final Object possibleView) {
        return possibleView instanceof ViewObject;
    }

    // /////////////////////////////////////////////////////////////
    // Listeners
    // /////////////////////////////////////////////////////////////

    public List<InteractionListener> getListeners() {
        return listeners;
    }

    public boolean addInteractionListener(final InteractionListener listener) {
        return listeners.add(listener);
    }

    public boolean removeInteractionListener(final InteractionListener listener) {
        return listeners.remove(listener);
    }

    public void notifyListeners(final InteractionEvent interactionEvent) {
        final InteractionEventDispatcher dispatcher = dispatchersByEventClass.get(interactionEvent.getClass());
        if (dispatcher == null) {
            throw new RuntimeException("Unknown InteractionEvent - register into dispatchers map");
        }
        dispatcher.dispatch(interactionEvent);
    }


}
