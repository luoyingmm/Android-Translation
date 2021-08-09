package com.luoyingmm.jsbridge;

//webView实现
public interface LvUJsBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
	

}
