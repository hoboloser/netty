package com.zhicall.netty.common.client;

import com.zhicall.netty.common.server.ServerHandler;
import com.zhicall.netty.serial.MarshallingFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer  extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(MarshallingFactory.buildMarshallingEncoder());
		pipeline.addLast(MarshallingFactory.buildMarshallingDecoder());
		pipeline.addLast(new ClientHandler());
	}
}
