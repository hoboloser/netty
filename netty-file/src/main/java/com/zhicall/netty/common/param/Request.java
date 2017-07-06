package com.zhicall.netty.common.param;

import java.io.Serializable;

public class Request<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private T t;
	
	private String token;

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
