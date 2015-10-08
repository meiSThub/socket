package com.mei.mina.server;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;


/**
 * @author Administrator
 * 数据加密的类
 */
public class MyTextLineEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	/**
	 * 加密
	 */
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput output)
			throws Exception {
		String string=null;
		if (message instanceof String){//如果message是String类型
			string=(String)message;
		}
		
		if (string!=null) {
			CharsetEncoder charsetEncoder=(CharsetEncoder) session.getAttribute("encoder");
			if (charsetEncoder==null) {
				charsetEncoder=Charset.defaultCharset().newEncoder();//获取系统自带的编码格式
				session.setAttribute("encoder", charsetEncoder);//把系统的编码格式存放到session对象当中，方便重用
			}
			IoBuffer ioBuffer=IoBuffer.allocate(string.length());//开辟内存
			ioBuffer.setAutoExpand(true);//让开辟的内存空间可以根据信息message的大小而扩充
			ioBuffer.putString(string, charsetEncoder);//把信息message按照编码charsetEncoder转化成字节
			ioBuffer.flip();
			
			output.write(ioBuffer);//把IoBuffer对象写出
		}

	}

}
