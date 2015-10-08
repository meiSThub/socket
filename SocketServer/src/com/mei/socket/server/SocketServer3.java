package com.mei.socket.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 之前写的两个服务器类都是只能允许一个客户端连入的，这是非常不科学的，
 * 所以此服务器类是为解决多客户连入的。
 * @author Administrator
 */
public class SocketServer3 {
	
	BufferedReader reader;//读取客户端输入信息的输入流对象
	BufferedWriter writer;//向客户端发送信息的输出流对象

	public static void main(String[] args) {
		SocketServer3 server=new SocketServer3();
		server.startServer();
	}
	/**
	 * 启动服务
	 */
	public void startServer(){
		ServerSocket serverSocket=null;//服务器套接字
		Socket socket=null;
		try {
			serverSocket=new ServerSocket(9898);//监听9898端口
			System.out.println("server started.....");
			while (true) {
				socket = serverSocket.accept();//等待客户端接入
				manageConnection(socket);
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
	 * 管理客户端的接入和通信
	 * @param socket	服务器与客户端建立起连接之后可以互相通信的Socket对象
	 */
	public void manageConnection(final Socket socket){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("client->"+socket.hashCode()+" connected....");//hashCode标志是哪一个客户端连入的
					//1.接收客户端发送来的信息的reader对象
					reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//2.向客户端发送信息的writer对象
					writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String receiveMsg;
					//1.1读取客户端发送来的信息
					receiveMsg=reader.readLine();
					while (receiveMsg!=null) {
						System.out.println("客户端"+socket.hashCode()+"发送来的信息是："+receiveMsg);
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
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
