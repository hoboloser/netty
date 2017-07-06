package com.zhicall.netty.common.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(boss, worker)
		 .channel(NioServerSocketChannel.class)
		 .option(ChannelOption.SO_BACKLOG, 1024)
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .childHandler(new ServerInitializer());
		try {
			ChannelFuture sync = b.bind(8888).sync();
			sync.channel().closeFuture().sync();
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
