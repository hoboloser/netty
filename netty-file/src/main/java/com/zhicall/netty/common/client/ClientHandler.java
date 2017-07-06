package com.zhicall.netty.common.client;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.zhicall.netty.common.thread.HeartBeatTask;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter{

	private InetAddress addr;
	
	private String key = "!@#$%^&*!";
	
	private static final String SUCCESS_KEY = "auth_success_key";
	
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    private ScheduledFuture<?> heartBeat;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
		ctx.writeAndFlush(ip + "," + key);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
        	if(msg instanceof String){
        		String ret = (String)msg;
        		if(SUCCESS_KEY.equals(ret)){
        	    	// 握手成功，主动发送心跳消息
        			this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeartBeatTask(ctx,addr), 0, 2, TimeUnit.SECONDS);
        		    System.out.println(msg);    			
        		}
        		else {
        			System.out.println(msg);
        		}
        	}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}