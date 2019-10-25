package h1;

/**
 * 实现请求转发
 * @author 詹皇吾皇
 *
 */
public class ForwardServlet extends HttpServlet{

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		//请求转发其实跟http协议无关
		//request.setRequestURL("/index.html");
		RequestDispatcher rd=request.getRequestDispatcher("/index.html");
		rd.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		doGet(request, response);
	}

	

	
	
}
