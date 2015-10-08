package com.mei.mina.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


/**
 * 自定义的字符串文本解密类，该类相较于直接实现ProtocolDecoder接口的类的好处是可以处理
 * 没有\n换行符的数据，保证了数据不缺失
 * @author Administrator
 */
public class MyTextLineCumulativeDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,ProtocolDecoderOutput out) throws Exception {
		
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
				return true;
			}
		}
		in.position(startPosition);//当没有发现换行符的时候，从新定位到开始的位置，即保存了数据，避免了数据的丢失
		return false;
	}

}
