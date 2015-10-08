package com.mei.socket.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 实现能主动接收服务器端发送给客户端的信息，即不需要客户端向服务器发送信息，只要服务器给
 * 客户端发送了信息，客户端就能够接收,即随时接收服务器端发送过来的信息,便于做到信息的
 * 推送.
 * @author Administrator
 */
public class SocketClient2 {

	public static void main(String[] args) {
		SocketClient2 client=new SocketClient2();
		client.start();
	}

	public void start(){
		BufferedReader inputReader=null;//读取控制台输入的信息的输入流对象
		BufferedReader serverReplyReader=null;//读取服务器端发送来的信息的输入流对象
		BufferedWriter writer=null;//用于向服务器发送信息的输出流对象
		Socket socket=null;//与服务器通信的Socket对象
		String inputContent;
		try {
			System.out.println("please input what message you want to push:");
			/**
			 * 创建一个流套接字并将其连接到指定 IP 地址的指定端口号。
			 * 即你将要连接的服务器地址和端口，以此来创建一个Socket对象，用于与服务器建立连接
			 */
			socket=new Socket("127.0.0.1", 9898);
			//1.向服务器端发送信息的writer对象
			writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			//2.接收服务器端发送来的信息的reader对象
			serverReplyReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			startServerReplyListener(serverReplyReader);
			//接收控制台输入的信息的reader对象
			inputReader=new BufferedReader(new InputStreamReader(System.in));
			
			inputContent=inputReader.readLine();//读取控制台输入的信息
			while (!inputContent.equals("bye")) {
				/**
				 * 1.1客户端向服务器发送信息
				 * 在服务器端也是调用BufferedReader对象的readLine()方法读取客户端发送过去的信息的，而此方法
				 * 是每次读取一行，一行结束的标识符就是换行符即"\n"，如果我们没有加换行符的话，服务器端就一直认为
				 * 信息还没有读取完毕，就会一直等待客户端的输入，这就会造成资源的浪费和异常。因此此处需要加入一个\n。
				 */
				writer.write(inputContent+"\n");//客户端向服务器端发送信息
				writer.flush();
				inputContent=inputReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
				inputReader.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 监听服务器端发送来的信息的线程
	 * @param reader
	 */
	public void startServerReplyListener(final BufferedReader reader){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String response;
					while ((response=reader.readLine())!=null) {
						System.out.println(response);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
