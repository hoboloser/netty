package com.zhicall.netty.file.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.ExecutorServiceFactory;

public class FileUploadClient {

	/** 处理业务线程组  */
	private EventLoopGroup group;
	
	/** 绑定ip  */
	private String host;
	
	/** 绑定端口  */
	private int port;
	
	/** 线程池  */
	private ExecutorServiceFactory exectorFactory;
	
	/** 客户端配置项  */
	private Bootstrap bootstrap;
	
	/** 最多允许执行数  */
	private int nEventLoops;
	
	public FileUploadClient(String host, int port) {
		this.host = host;
		this.port = port;
		if (nEventLoops == 0) {
			group = new NioEventLoopGroup();
		} else if (exectorFactory == null) {
			group = new NioEventLoopGroup(nEventLoops);
		} else{
			group = new NioEventLoopGroup(nEventLoops, exectorFactory);
		}
		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
		 .handler(new FileUploadClientInitializer());
	}
	
	public void run() {
		ChannelFuture sync;
		try {
			sync = bootstrap.connect(host, port).sync();
			sync.channel().closeFuture().sync();
			group.shutdownGracefully();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public EventLoopGroup getGroup() {
		return group;
	}

	public void setGroup(EventLoopGroup group) {
		this.group = group;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ExecutorServiceFactory getExectorFactory() {
		return exectorFactory;
	}

	public void setExectorFactory(ExecutorServiceFactory exectorFactory) {
		this.exectorFactory = exectorFactory;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public int getnEventLoops() {
		return nEventLoops;
	}

	public void setnEventLoops(int nEventLoops) {
		this.nEventLoops = nEventLoops;
	}
	
}
