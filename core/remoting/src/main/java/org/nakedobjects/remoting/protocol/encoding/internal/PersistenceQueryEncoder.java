package org.nakedobjects.remoting.protocol.encoding.internal;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
import org.nakedobjects.remoting.protocol.encoding.EncodingProtocolConstants;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;

/**
 * TODO: this would be a good candidate for genericizing.
 */
public interface PersistenceQueryEncoder {

    Class<?> getPersistenceQueryClass();
    
    /**
     * Injected directly after instantiation (note that encoders can potentially be loaded
     * reflectively, from the {@link NakedObjectConfiguration configuration} using the
     * {@value EncodingProtocolConstants#ENCODER_CLASS_NAME_LIST} key.
     */
    void setObjectEncoder(ObjectEncoderDecoder objectEncoder);
    
    PersistenceQueryData encode(PersistenceQuery persistenceQuery);

    PersistenceQuery decode(PersistenceQueryData persistenceQueryData);
}

// Copyright (c) Naked Objects Group Ltd.
