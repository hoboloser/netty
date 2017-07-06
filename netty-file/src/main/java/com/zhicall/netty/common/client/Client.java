package com.zhicall.netty.common.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Client {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .handler(new ClientInitializer());
		
		try {
			ChannelFuture sync = bootstrap.connect("127.0.0.1", 8888).sync();
			sync.channel().closeFuture().sync();
			group.shutdownGracefully();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
