package com.zhicall.netty.file.server;

public class FileMain {

	public static void main(String[] args) {
		String host = "172.16.60.30";
		int port = 6789;
		FileUploadServer server = new FileUploadServer(host, port);
		server.run();
	}
}
