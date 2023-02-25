package org.nakedobjects.nof.reflect.remote.spec;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.reflect.NakedObjectActionParameter;
import org.nakedobjects.noa.reflect.NakedObjectActionParameter.Filter;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.util.NameConvertor;
import org.nakedobjects.nof.core.util.NotImplementedException;
import org.nakedobjects.nof.reflect.peer.ActionPeer;
import org.nakedobjects.nof.reflect.peer.MemberIdentifier;
import org.nakedobjects.nof.reflect.spec.DefaultOneToOneActionParameter;
import org.nakedobjects.nof.reflect.spec.DefaultValueActionParameter;
import org.nakedobjects.testing.TestSession;


public class DummyAction implements NakedObjectAction {
    public final ActionPeer peer;

	public DummyAction(final ActionPeer peer) {
		this.peer = peer;
	}

	public boolean[] canParametersWrap() {
        return null;
    }

	public String debugData() {
		return "";
	}

	public Naked execute(final NakedReference object, final Naked[] parameters) {
		return peer.execute(object, parameters);
	}

	public NakedObjectAction[] getActions() {
		return new NakedObjectAction[0];
	}

	public Naked[] getDefaultParameterValues(NakedReference target) {
		throw new NotImplementedException();
	}

	public String getDescription() {
		return "";
	}

	public Facet getFacet(final Class cls) {
		return null;
	}

	public Class[] getFacetTypes() {
		return new Class[0];
	}
	
	public Facet[] getFacets(Facet.Filter filter) {
		return new Facet[]{};
	}

	public void addFacet(Facet facet) {
	}

	public void removeFacet(Facet facet) {
	}

	public String getHelp() {
		return "";
	}

	public String getId() {
		return NameConvertor.simpleName(peer.getIdentifier().getName());
	}

	public MemberIdentifier getIdentifier() {
		throw new NotImplementedException();
	}

	public String getName() {
		return "";
	}
    
    public NakedObjectSpecification getOnType() {
        return peer.getOnType();
    }

	public Naked[][] getOptions(NakedReference target) {
		return null;
	}

    public int getParameterCount() {
		return peer.getParameterCount();
	}
    
	public String[] getParameterDescriptions() {
        return null;
    }

	public int[] getParameterMaxLengths() {
        return null;
    }

	public String[] getParameterNames() {
		return new String[]{};
	}

	public int[] getParameterNoLines() {
        return null;
    }

    /**
     * Build lazily by {@link #getParameters()}.
     */
    private NakedObjectActionParameter[] parameters;
    public NakedObjectActionParameter[] getParameters() {
        if (parameters == null) {
            parameters = new NakedObjectActionParameter[getParameterCount()];
            NakedObjectSpecification[] parameterTypes = getParameterTypes();
            String[] parameterNames = getParameterNames();
            String[] parameterDescriptions = getParameterDescriptions();
            boolean[] optionalParameters = getOptionalParameters();
            
            int[] parameterNoLines = getParameterNoLines();
            boolean[] canParametersWrap = canParametersWrap();
            int[] parameterMaxLengths = getParameterMaxLengths();
            int[] parameterTypicalLengths = getParameterTypicalLengths();
            
            for(int i=0; i<getParameterCount(); i++) {
                if (parameterTypes[i].getType() == NakedObjectSpecification.VALUE) {
                    parameters[i] = new DefaultValueActionParameter(
                            i, 
                            this,
                            null,
                            parameterTypes[i], 
                            parameterNames[i], parameterDescriptions[i], 
                            optionalParameters[i], 
                            parameterTypicalLengths[i], parameterMaxLengths[i],
                            parameterNoLines[i], canParametersWrap[i]);
                } else if (parameterTypes[i].getType() == NakedObjectSpecification.OBJECT) {
                    parameters[i] = new DefaultOneToOneActionParameter(
                            i, 
                            this,
                            null,
                            parameterTypes[i], 
                            parameterNames[i], parameterDescriptions[i], 
                            optionalParameters[i]);
                } else if (parameterTypes[i].getType() == NakedObjectSpecification.COLLECTION) {
                    // TODO: not supported; should we throw an exception of some sort here?
                    parameters[i] = null;
                }
                
            }
        }
        return parameters;
    }
    
    
    public NakedObjectActionParameter[] getParameters(
            Filter filter) {
        NakedObjectActionParameter[] allParameters = getParameters();
        
        NakedObjectActionParameter[] selectedParameters = new NakedObjectActionParameter[allParameters.length];
        int v = 0;
        for (int i = 0; i < allParameters.length; i++) {
            if (filter.accept(allParameters[i])) {
                selectedParameters[v++] = allParameters[i];
            }
        }

        NakedObjectActionParameter[] parameters = new NakedObjectActionParameter[v];
        System.arraycopy(selectedParameters, 0, parameters, 0, v);
        return parameters;
    }


//	public NakedObjectSpecification[] getParameterTypes() {
//		return peer.getParameterTypes();
//	}

	public int[] getParameterTypicalLengths() {
        return null;
    }

//	public boolean[] getOptionalParameters() {
//		return peer.getOptionalParameters();
//	}

	public NakedObjectSpecification getReturnType() {
		return peer.getReturnType();
	}

	public Target getTarget() {
		ExecutedFacet executedFacet = (ExecutedFacet) peer.getFacet(ExecutedFacet.class);
		return executedFacet == null?NakedObjectAction.DEFAULT:executedFacet.getTarget();
	}

	public Type getType() {
		DebugFacet debugFacet = (DebugFacet) peer.getFacet(DebugFacet.class);
		if (debugFacet != null) {
			return NakedObjectAction.DEBUG;
		}
		ExplorationFacet explorationFacet = (ExplorationFacet) peer.getFacet(ExplorationFacet.class);
		if (explorationFacet != null) {
			return NakedObjectAction.EXPLORATION;
		}
		return NakedObjectAction.USER;
	}

	public boolean hasReturn() {
		return false;
	}

	public boolean isOnInstance() {
		return peer.isOnInstance();
	}

    /**
     * Delegates to {@link #isParameterSetValidDeclaratively(NakedReference, Naked[])} and
     * then {@link #isParameterSetValidImperatively(NakedReference, Naked[])}, as per the
     * contract in the {@link NakedObjectAction implemented interface}.
     */
	public Consent isParameterSetValid(final NakedReference object,
			final Naked[] parameters) {
	    Consent consentDeclaratively = isParameterSetValidDeclaratively(object, parameters);
        if (consentDeclaratively.isVetoed()) {
            return consentDeclaratively;
        }
		return isParameterSetValidImperatively(object, parameters);
	}

    /**
     * Always returns an {@link Allow}.
     */
    public Consent isParameterSetValidDeclaratively(
            NakedReference object,
            Naked[] parameters) {
        return Allow.DEFAULT;
    }

    public Consent isParameterSetValidImperatively(
            NakedReference object,
            Naked[] parameters) {
        return peer.isParameterSetValidImperatively(object, parameters);
    }

    public Consent isUsable(final NakedReference target) {
		return peer.isUsable(target);
	}

    public Consent isUsable() {
        Consent usableDeclaratively = isUsableDeclaratively();
        if (usableDeclaratively.isVetoed()) {
            return usableDeclaratively;
        }
        return isUsableForSession();
    }

    public Consent isUsableDeclaratively() {
        return peer.isUsableDeclaratively();
    }

    public Consent isUsableForSession() {
        return peer.isUsableForSession(NakedObjectsContext.getSession());
    }

    public boolean isContributedMethodWithSuitableParameter() {
        return false;
    }
    
    public boolean isVisible() {
        return isVisibleDeclaratively() && isVisibleForSession();
    }

    public boolean isVisibleDeclaratively() {
		return peer.isVisibleDeclaratively();
	}

    public boolean isVisibleForSession() {
        return peer.isVisibleForSession(new TestSession());
    }

    public boolean isVisible(final NakedReference target) {
		return peer.isVisible(target);
	}

    public Naked[] parameterStubs() {
		throw new NotImplementedException();
	}
    
    public NakedReference realTarget(NakedReference target) {
        return target;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }




}
// Copyright (c) Naked Objects Group Ltd.
