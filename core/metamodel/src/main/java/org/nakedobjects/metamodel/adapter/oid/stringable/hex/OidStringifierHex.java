package org.nakedobjects.metamodel.adapter.oid.stringable.hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.oid.stringable.OidStringifier;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputStreamExtended;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class OidStringifierHex implements OidStringifier {

    public String enString(final Oid oid) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutputStreamExtended outputImpl = new DataOutputStreamExtended(baos);
        try {
			outputImpl.writeEncodable(oid);
			final byte[] byteArray = baos.toByteArray();
			return new String(Hex.encodeHex(byteArray));
		} catch (IOException e) {
			throw new NakedObjectException("Failed to write object", e);
		}
    }

    public Oid deString(final String oidStr) {
        final char[] oidCharArray = oidStr.toCharArray();
        byte[] oidBytes;
        try {
            oidBytes = Hex.decodeHex(oidCharArray);
            final ByteArrayInputStream bais = new ByteArrayInputStream(oidBytes);
            final DataInputStreamExtended inputImpl = new DataInputStreamExtended(bais);
            return inputImpl.readEncodable(Oid.class);
        } catch (IOException ex) {
        	throw new NakedObjectException("Failed to read object", ex);
		} catch (DecoderException ex) {
			throw new NakedObjectException("Failed to hex decode object", ex);
		}
    }

}
