package org.nakedobjects.plugins.headless.embedded.internal;

public enum PersistenceState {
	TRANSIENT(true,false),
	PERSISTENT(false,true),
	STANDALONE(false,false);
	private boolean trans;
	private boolean persistent;
	PersistenceState(boolean trans, boolean persistent) {
		this.trans = trans;
		this.persistent = persistent;
	}
	public boolean isTransient() {
		return trans;
	}
	public boolean isPersistent() {
		return persistent;
	}
}