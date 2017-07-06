package com.zhicall.netty.file.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.ExecutorServiceFactory;

/**
 * netty 之 文件上传
 * @title 
 * TODO 文件上传服务器
 * @version 
 * @author binH
 *
 * 2017年6月20日 下午5:05:21
 */
public class FileUploadServer {
	
	/** 处理接收客户端线程组  */
	private EventLoopGroup bossGroup;
	
	/** 处理业务线程组  */
	private EventLoopGroup workerGroup;
	
	/** 绑定ip  */
	private String host;
	
	/** 绑定端口  */
	private int port;
	
	/** 线程池  */
	private ExecutorServiceFactory exectorFactory;
	
	/** 服务器配置项  */
	private ServerBootstrap sbootstrap;
	
	/** 最多允许执行数  */
	private int nEventLoops;
	//= Executors.newCachedThreadPool(threadFactory)
	//nEventLoops = Runtime.getRuntime().availableProcessors() * 2;
	public FileUploadServer(String host, int port) {
		this.host = host;
		this.port = port;
		if (nEventLoops == 0) {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();
		} else if (exectorFactory == null) {
			bossGroup = new NioEventLoopGroup(nEventLoops);
			workerGroup = new NioEventLoopGroup(nEventLoops);
		} else{
			bossGroup = new NioEventLoopGroup(nEventLoops, exectorFactory);
			workerGroup = new NioEventLoopGroup(nEventLoops, exectorFactory);
		}
		sbootstrap = new ServerBootstrap();
		sbootstrap.group(bossGroup, workerGroup)
		 .channel(NioServerSocketChannel.class)//通道
		 .option(ChannelOption.SO_BACKLOG, 1024)
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .childHandler(new FileUploadServerInitializer());
	}
	
	public void run() {
		try {
			ChannelFuture cf = sbootstrap.bind(host, port).sync();
			cf.channel().closeFuture().sync();
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(EventLoopGroup bossGroup) {
		this.bossGroup = bossGroup;
	}

	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(EventLoopGroup workerGroup) {
		this.workerGroup = workerGroup;
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

	public ServerBootstrap getSbootstrap() {
		return sbootstrap;
	}

	public void setSbootstrap(ServerBootstrap sbootstrap) {
		this.sbootstrap = sbootstrap;
	}

	public int getnEventLoops() {
		return nEventLoops;
	}

	public void setnEventLoops(int nEventLoops) {
		this.nEventLoops = nEventLoops;
	}
	
}
