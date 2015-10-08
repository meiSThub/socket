package com.mei.mina.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {

	public static void main(String[] args) {
		try {
			//1.声明NioSocketAcceptor对象
			NioSocketAcceptor acceptor=new NioSocketAcceptor();
			//2.设置收发信息时的回调
			acceptor.setHandler(new MyServerHandler());
			//3.设置拦截器
			/*
			acceptor.getFilterChain()//获取链接器，在收发信息的时候，都会经过拦截之后再执行Handler中的相关方法
			.addLast("codec", 							//添加一个自己的拦截器
					new ProtocolCodecFilter(			//把二进制转化成对象的拦截器
							new TextLineCodecFactory()));//读取文本行的Factory,把字符串文本数据进行相应的加解码
		 	*/	
			
			acceptor.getFilterChain()//获取链接器，在收发信息的时候，都会经过拦截之后再执行Handler中的相关方法
			.addLast("codec", 							//添加一个自己的拦截器
					new ProtocolCodecFilter(			//把二进制转化成对象的拦截器
							new MyTextLineFactory()));  //使用自定义的factory
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 100);//设置服务器进入空闲状态的时间,单位秒
			
			//4.设置监听的端口
			acceptor.bind(new InetSocketAddress(9898));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
