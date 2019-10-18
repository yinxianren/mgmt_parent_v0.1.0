package com.rxh.pojo.cross;

/**
 * stream��
 * @author xie
 *
 */
public class StreamBuffer {

	private byte[] buffer;
	private int size;

	public StreamBuffer(byte[] b, int s) {
		buffer = b;
		size = s;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


}
