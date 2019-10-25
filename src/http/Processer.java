package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.omg.CORBA.Request;

public class Processer {

	public void process(Socket socket){
		InputStream in;
		OutputStream out;
		try{
			in = socket.getInputStream();
			out=socket.getOutputStream();
			
			//读取请求报文内容
			byte[] buf = new byte[1024];
			int count;
			count = in.read(buf);
			String content = new String(buf,0,count);
			System.out.println();
			//解析请求报文(暂未实现)
			HttpServletRequest request = parseRequest(content);
			
			/**
			 * 
			 */
			String suffix = request.getRequestURL().substring(request.getRequestURL().lastIndexOf(".")+1);
			String contentType;
			switch (suffix) {
			case "js":
				contentType ="application/x-javascript";
				break;
			case "css":
				contentType="text/css";break;
			case "jpg":
				contentType="image/jpeg";break;
			case "bmp":
				contentType="image/bmp";break;
			case "gif":
				contentType="image/gif";break;
			case "png":
				contentType="image/png";break;
			default:
				contentType="text/html";
			}
			//定义响应报文内容
			String resp = "HTTP/1.1 200 OK\r\n";
			resp +="Content-type:text/html\r\n";
			resp +="\r\n";//CRLF 空行
			out.write(resp.getBytes());
			
			
			String rootPath = "E:/新建文件夹/css5";
			String filePath=request.getRequestURL();
			//判断访问文件是否存在
			String diskPath = rootPath + filePath;
			if(new File(diskPath).exists()==false){
				diskPath = rootPath+"/404.html";
			}
			FileInputStream fis = new FileInputStream(rootPath + filePath);
			
			//向浏览器发送报文
			while( (count=fis.read(buf)) >0 ){
				out.write(buf,0,count);
			}
			fis.close();
			
			//如果访问的文件不存在，则返回404.html

			//根据请求的路径返回对应的文件 html
		}catch(IOException e){
			e.printStackTrace();
		}				
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//解析请求报文 （知道需要什么）
		
		//给与对应的相应
	}
	
	public HttpServletRequest parseRequest(String content){
		HttpServletRequest request = new HttpServletRequest(content);
		return request;
	}
}

