package com.mei.socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TimerTask;

/**
 * 当客户端接入该服务器后,定时的给客户端发送信息，并且客户端能够接收到这些信息
 * @author Administrator
 */
public class SocketServer2 {

	public static void main(String[] args) {
		SocketServer2 server=new SocketServer2();
		server.startServer();
	}
	/**
	 * 启动服务
	 */
	public void startServer(){
		ServerSocket serverSocket=null;//服务器套接字
		BufferedReader reader=null;//读取客户端输入信息的输入流对象
		BufferedWriter writer=null;//向客户端发送信息的输出流对象
		Socket socket=null;
		try {
			serverSocket=new ServerSocket(9898);//监听9898端口
			System.out.println("server started.....");
			/**
			 * 侦听并接受到此套接字的连接,即接收客户端连接服务器的请求,如果没有客户端连如，则程序会暂停在此处,
			 * accept方法返回的是一个Socket套接字对象，该对象用于与客户端进行通信，即数据交换.此时就可认为
			 * 服务器端与客户端就建立起了一个连接。连接是一直保持的，除非出现异常或者主动断开连接.
			 */
			socket=serverSocket.accept();//等待客户端接入
			System.out.println("client->"+socket.hashCode()+" connected....");//hashCode标志是哪一个客户端连入的
			//1.接收客户端发送来的信息的reader对象
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//2.向客户端发送信息的writer对象
			writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sendMsgToClientTimer(writer);
			
			String receiveMsg;
			//1.1读取客户端发送来的信息
			receiveMsg=reader.readLine();
			while (receiveMsg!=null) {
				System.out.println("客户端发送来的信息是："+receiveMsg);
				//2.1服务器向客户端发送信息
				writer.write("server reply:"+receiveMsg+"\n");
				writer.flush();
				
				receiveMsg=reader.readLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
				reader.close();
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 定时向客户端发送信息
	 */
	public void sendMsgToClientTimer(final BufferedWriter writer){
		
		new java.util.Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					System.out.println("server's heart beat once.....");
					writer.write("server's heart beat once.....\n");
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 3000, 3000);
	}
}
