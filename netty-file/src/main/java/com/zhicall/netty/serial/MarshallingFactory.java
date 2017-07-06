package com.zhicall.netty.serial;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingFactory {

	/**
	 * jboss 解码器
	 * @return
	 */
	public static MarshallingDecoder buildMarshallingDecoder(){
		//首先通过Marshalling工具类的精通方法获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象。
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		//创建了MarshallingConfiguration对象，配置了版本号为5 
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		//根据marshallerFactory和configuration创建provider
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
		//构建Netty的MarshallingDecoder对象，俩个参数分别为provider和单个消息序列化后的最大长度
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
		return decoder;
	}
	/**
	 * jboss 编码器
	 * @return
	 */
	public static MarshallingEncoder buildMarshallingEncoder(){
		final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(factory, config);
		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		return encoder;
	}
}
