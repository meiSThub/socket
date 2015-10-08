package com.mei.mina.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


/**
 * @author Administrator
 * 数据解密的类
 */
public class MyTextLineDecoder implements ProtocolDecoder {

	/**
	 * 解密
	 */
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		int startPosition=in.position();//记录开始取得时字节的起始位置
		while (in.hasRemaining()) {
			byte b=in.get();//从输入流中读取一个字节
			if (b=='\n') {
				int currentPosition=in.position();//当遇到换行符时位置
				int limit=in.limit();//默认截止位置为流的最后一个字节位置
				in.position(startPosition);//把流重新定向到开始的位置
				in.limit(currentPosition);//需要截取的字节中，最后一个字节的位置,即换行符\n那个字节的位置
				IoBuffer buffer=in.slice();	//把从开始读取字节到截止读取字节(即startPosition-currentPosition)这个区段上的字节截取下来
				
				//把IoBuffer先转换成字节，在把在转换成String类型
				byte[] dest=new byte[buffer.limit()];
				buffer.get(dest);
				String str=new String(dest);
				out.write(str);//把转换好的数据写出去
				
				in.position(currentPosition);//还原位置，即下一次需要从这个位置开始读取字节,如果没有这一步，那么每一次循环开始读取的都是第一个字节，会陷入死循环
				in.limit(limit);//同position
			}
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput arg1)
			throws Exception {
		
	}

}
