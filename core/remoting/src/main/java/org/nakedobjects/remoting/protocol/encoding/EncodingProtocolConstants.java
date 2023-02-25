package org.nakedobjects.remoting.protocol.encoding;

import org.nakedobjects.metamodel.config.ConfigurationConstants;

public final class EncodingProtocolConstants {
	
	public static final String ENCODER_CLASS_NAME_LIST_DEPRECATED = ConfigurationConstants.ROOT + "criteria.encoders";
	public static final String ENCODER_CLASS_NAME_LIST = ConfigurationConstants.ROOT + "persistence-query.encoders";

	private EncodingProtocolConstants() {}

}
