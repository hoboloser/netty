package com.zhicall.netty.file.param;

public class RequestData {

	private int id;
	
	private String name;
	
	private byte[] filebit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFilebit() {
		return filebit;
	}

	public void setFilebit(byte[] filebit) {
		this.filebit = filebit;
	}
	
	
}
