package http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServletResponse {

	private static WebXmlParser webXmlParser = new WebXmlParser("web.xml");
	
	private HttpServletRequest request;
	private OutputStream out ;
	public HttpServletResponse(HttpServletRequest request,OutputStream out){
		super();
		this.request = request;
		this.out = out;
	}
	
	public void commit() throws IOException{
		String suffix = request.getRequestURL().substring(request.getRequestURL().lastIndexOf(".")+1);
		
		String contentType = WebXmlParser.getContentType(suffix);
		
		String resp="HTTP/1.1 200 OK\r\n";
		resp +="Content-Type:"+contentType+"\r\n";
		resp +="\r\n";
		out.write(resp.getBytes());
		
		String rootPath="/Tomcat/webapps/photo";
		String filePath = request.getRequestURL();
		//判断文件是否存在
		String diskPath=rootPath = filePath;
		if(new File(diskPath).exists()==false){
			diskPath = rootPath+"/404.html";
		}
		FileInputStream fis = new FileInputStream(diskPath);
		byte[] buf = new byte[1024];
		int count;
		//向浏览器发送报文
		while((count =fis.read(buf))>0){
			out.write(buf,0,count);
		}
		fis.close();
	}
}
