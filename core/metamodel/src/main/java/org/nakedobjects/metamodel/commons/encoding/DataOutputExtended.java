package org.nakedobjects.metamodel.commons.encoding;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;

public interface DataOutputExtended extends DataOutput, Flushable {

	public void writeBooleans(boolean[] booleans) throws IOException;
	public void writeChars(char[] chars) throws IOException;

	/**
	 * NB: only writes out <tt>byte</tt>.
	 */
	public void write(int b) throws IOException;
	/**
	 * Same as {@link #write(int)}.
	 */
	public void writeByte(int v) throws IOException;
	public void writeBytes(byte[] bytes) throws IOException;
	public void writeShorts(short[] shorts) throws IOException;
	public void writeInts(int[] ints) throws IOException;
	public void writeLongs(long[] longs) throws IOException;

	public void writeDoubles(double[] doubles) throws IOException;
	public void writeFloats(float[] floats) throws IOException;

	public void writeUTFs(String[] strings) throws IOException;

	public void writeEncodable(Object encodable) throws IOException;
	public void writeEncodables(Object[] encodables) throws IOException;

	public void writeSerializable(Object serializable) throws IOException;
	public void writeSerializables(Object[] serializables) throws IOException;
	
	DataOutputStream getDataOutputStream();
}
