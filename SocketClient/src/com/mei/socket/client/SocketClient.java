package com.mei.socket.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * 只有被动的接收服务器端的信息，即只有在客户端给服务器端发送一条信息之后，
 * 服务器端才给客户端回复一条信息，比较被动，不能很好地实现信息推送功能.
 * @author Administrator
 */
public class SocketClient {

	public static void main(String[] args) {
		SocketClient client=new SocketClient();
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
			//接收控制台输入的信息的reader对象
			inputReader=new BufferedReader(new InputStreamReader(System.in));
			//1.向服务器端发送信息的writer对象
			writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			//2.接收服务器端发送来的信息的reader对象
			serverReplyReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
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
				
				//2.1客户端接收服务器端的发送来的信息
				String response=serverReplyReader.readLine();
				System.out.println(response);
				
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

}
