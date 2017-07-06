package com.zhicall.netty.common.thread;

import java.net.InetAddress;
import java.util.HashMap;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import io.netty.channel.ChannelHandlerContext;

public class HeartBeatTask implements Runnable {
	
	private final ChannelHandlerContext ctx;
	
	private final InetAddress addr;
	

	public HeartBeatTask(final ChannelHandlerContext ctx,final InetAddress addr) {
	    this.ctx = ctx;
	    this.addr = addr;
	}

	public void run() {
		try {
		    RequestInfo info = new RequestInfo();
		    //ip
		    info.setIp(addr.getHostAddress());
	        Sigar sigar = new Sigar();
	        //cpu prec
	        CpuPerc cpuPerc = sigar.getCpuPerc();
	        HashMap<String, Object> cpuPercMap = new HashMap<String, Object>();
	        cpuPercMap.put("combined", cpuPerc.getCombined());
	        cpuPercMap.put("user", cpuPerc.getUser());
	        cpuPercMap.put("sys", cpuPerc.getSys());
	        cpuPercMap.put("wait", cpuPerc.getWait());
	        cpuPercMap.put("idle", cpuPerc.getIdle());
	        // memory
	        Mem mem = sigar.getMem();
			HashMap<String, Object> memoryMap = new HashMap<String, Object>();
			memoryMap.put("total", mem.getTotal() / 1024L);
			memoryMap.put("used", mem.getUsed() / 1024L);
			memoryMap.put("free", mem.getFree() / 1024L);
			info.setCpuPercMap(cpuPercMap);
		    info.setMemoryMap(memoryMap);
		    ctx.writeAndFlush(info);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}