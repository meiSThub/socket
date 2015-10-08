package com.mei.mina.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * 自定义读取文本行的Factory,把字符串文本数据进行相应的加解码
 * @author Administrator
 */
public class MyTextLineFactory implements ProtocolCodecFactory {

	private MyTextLineDecoder mDecoder;
	private MyTextLineCumulativeDecoder mCumulativeDecoder;
	private MyTextLineEncoder mEncoder;
	
	public MyTextLineFactory(){
		mDecoder=new MyTextLineDecoder();
		mEncoder=new MyTextLineEncoder();
		mCumulativeDecoder=new MyTextLineCumulativeDecoder();
	}
	/**
	 * 解密
	 */
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		
		return mCumulativeDecoder;
	}

	/**
	 * 加密
	 */
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		
		return mEncoder;
	}

}
