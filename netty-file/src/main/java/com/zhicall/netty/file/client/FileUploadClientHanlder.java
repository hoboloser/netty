package com.zhicall.netty.file.client;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class FileUploadClientHanlder extends ChannelHandlerAdapter{
	
	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		super.bind(ctx, localAddress, promise);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	
	
}
