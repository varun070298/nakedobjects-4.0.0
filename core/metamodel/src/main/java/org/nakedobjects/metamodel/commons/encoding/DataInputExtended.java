package org.nakedobjects.metamodel.commons.encoding;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public interface DataInputExtended extends DataInput {

	public boolean[] readBooleans() throws IOException;
	public char[] readChars() throws IOException;

	public byte[] readBytes() throws IOException;
	public short[] readShorts() throws IOException;
	public int[] readInts() throws IOException;
	public long[] readLongs() throws IOException;

	public float[] readFloats() throws IOException;
	public double[] readDoubles() throws IOException;

	public String[] readUTFs() throws IOException;

	public <T> T readEncodable(Class<T> encodableType) throws IOException;
	public <T> T[] readEncodables(Class<T> elementType) throws IOException;

	public <T> T readSerializable(Class<T> serializableType) throws IOException;
	public <T> T[] readSerializables(Class<T> elementType) throws IOException;
	
	/**
	 * Underlying {@link DataInputStream} to read in primitives.
	 */
	DataInputStream getDataInputStream();
}
