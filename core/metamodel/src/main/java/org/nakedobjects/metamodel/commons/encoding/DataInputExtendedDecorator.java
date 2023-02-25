package org.nakedobjects.metamodel.commons.encoding;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;

public class DataInputExtendedDecorator implements DataInputExtended {
	
	private final DataInputExtended underlying;
	
	public DataInputExtendedDecorator(DataInputExtended underlying) {
		this.underlying = underlying;
	}
	
	public DataInputStream getDataInputStream() {
		return underlying.getDataInputStream();
	}

	//////////////////////////////////////////
	// Boolean, Char
	//////////////////////////////////////////
	
	public boolean readBoolean() throws IOException {
		return underlying.readBoolean();
	}

	public boolean[] readBooleans() throws IOException {
		return underlying.readBooleans();
	}


	public char readChar() throws IOException {
		return underlying.readChar();
	}
	
	public char[] readChars() throws IOException {
		return underlying.readChars();
	}


	//////////////////////////////////////////
	// Integral Numbers
	//////////////////////////////////////////

	public byte readByte() throws IOException {
		return underlying.readByte();
	}

	public int readUnsignedByte() throws IOException {
		return underlying.readUnsignedByte();
	}

	public byte[] readBytes() throws IOException {
		return underlying.readBytes();
	}

	public short readShort() throws IOException {
		return underlying.readShort();
	}

	public int readUnsignedShort() throws IOException {
		return underlying.readUnsignedShort();
	}

	public short[] readShorts() throws IOException {
		return underlying.readShorts();
	}
	
	public int readInt() throws IOException {
		return underlying.readInt();
	}

	public int[] readInts() throws IOException {
		return underlying.readInts();
	}
	
	public long[] readLongs() throws IOException {
		return underlying.readLongs();
	}

	public long readLong() throws IOException {
		return underlying.readLong();
	}

	
	//////////////////////////////////////////
	// Floating Point Numbers
	//////////////////////////////////////////

	public float readFloat() throws IOException {
		return underlying.readFloat();
	}

	public float[] readFloats() throws IOException {
		return underlying.readFloats();
	}

	public double readDouble() throws IOException {
		return underlying.readDouble();
	}

	public double[] readDoubles() throws IOException {
		return underlying.readDoubles();
	}
	

	//////////////////////////////////////////
	// Strings
	//////////////////////////////////////////

	public String readUTF() throws IOException {
		return underlying.readUTF();
	}

	public String[] readUTFs() throws IOException {
		return underlying.readUTFs();
	}



	//////////////////////////////////////////
	// Encodable and Serializable
	//////////////////////////////////////////

	public <T> T readEncodable(Class<T> encodableType) throws IOException {
		return underlying.readEncodable(encodableType);
	}

	public <T> T[] readEncodables(Class<T> encodableType) throws IOException {
		return underlying.readEncodables(encodableType);
	}

	public <T> T readSerializable(Class<T> serializableType) throws IOException {
		return underlying.readSerializable(serializableType);
	}
	
	public <T> T[] readSerializables(Class<T> serializableType) throws IOException {
		return underlying.readSerializables(serializableType);
	}

	
	//////////////////////////////////////////
	// Other
	//////////////////////////////////////////

	public void readFully(byte[] b) throws IOException {
		underlying.readFully(b);
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		underlying.readFully(b, off, len);
	}

	public String readLine() throws IOException {
		return underlying.readLine();
	}

	public int skipBytes(int n) throws IOException {
		return underlying.skipBytes(n);
	}


//	//////////////////////////////////////////
//	// Closeable
//	//////////////////////////////////////////
//
//	public void close() throws IOException {
//		if (underlying instanceof Closeable) {
//			Closeable closeable = (Closeable) underlying;
//			closeable.close();
//		}
//	}


}
