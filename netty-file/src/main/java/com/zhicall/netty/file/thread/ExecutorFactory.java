package com.zhicall.netty.file.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.netty.util.concurrent.ExecutorServiceFactory;

public class ExecutorFactory implements ExecutorServiceFactory {
	
	private ThreadFactory threadFactory;
	
//	@Override
	public ExecutorService newExecutorService(int parallelism) {
		ExecutorService service = Executors.newCachedThreadPool(threadFactory);
		return service;
	}

	
}
