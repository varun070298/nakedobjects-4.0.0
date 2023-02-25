package org.nakedobjects.metamodel.commons.threads;


public class ThreadRunner {

    public Thread startThread(Runnable target, String name) {
        final Thread thread = new Thread(target, name);
		thread.start();
		return thread;
    }

}
