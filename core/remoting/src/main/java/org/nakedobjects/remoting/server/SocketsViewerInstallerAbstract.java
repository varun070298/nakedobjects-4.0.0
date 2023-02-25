package org.nakedobjects.remoting.server;

import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoderDefault;
import org.nakedobjects.runtime.viewer.NakedObjectsViewer;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstallerAbstract;

public abstract class SocketsViewerInstallerAbstract extends NakedObjectsViewerInstallerAbstract {

	public SocketsViewerInstallerAbstract(String name) {
		super(name);
	}

	@Override
    public NakedObjectsViewer doCreateViewer() {
        ObjectEncoderDecoderDefault encoder = 
        	ObjectEncoderDecoderDefault.create(getConfiguration());
        return createSocketsViewer(encoder);
    }
	
    protected abstract NakedObjectsViewer createSocketsViewer(ObjectEncoderDecoder objectEncoderDecoder);

}
