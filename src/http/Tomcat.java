package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Tomcat {

	public static void main(String[] args) throws IOException {
		Tomcat tomcat = new Tomcat();
		tomcat.startup();
}

	//启动命令
	private void startup() throws IOException {

		ServerSocket server = new ServerSocket(8080);
		boolean running = true;
		while(running){
			Socket socket =server.accept();
			new Thread(){
				public void run() {
					new Processer().process(socket);
				}
			}.start();
		}
		server.close();
	}
	//关闭服务器
	public void shutdown(){
		
	}
	
}
