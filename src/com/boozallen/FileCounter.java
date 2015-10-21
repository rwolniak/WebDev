package com.boozallen;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FileCounter which displays how many times a page has been loaded (count).
 * The count is kept persistent via being written to/from a file.
 */
@WebServlet("/FileCounter")
public class FileCounter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	int count;
	private DAO dao;
	
	/**
	 * Get the ip address of the client
	 * @param request
	 * @return ip address
	 */
	public static String getClientIpAddr(HttpServletRequest request) {  
		//check the various headers to see where the actual ip address is
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
	
    /**
     * Initialize FileCounter
     */
	@Override
	public void init() throws ServletException {
		dao = new DAO();
		try {
			count = dao.getCount();
		} catch (Exception e){
			getServletContext().log("An exception occurred in FileCounter", e);
			throw new ServletException("An exception occurred in Filecounter" + e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//create a user cookie (session) so that the counter only increases when new users load the page
		HttpSession session = request.getSession(true);
		//create a new session if the user is inactive for more than 5 seconds
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if(session.isNew()){
			count++;
		}
		//reset the counter to 0 when it reaches 10 (purely for testing purposes)
		if(count >= 10){
			dao.reset();
			count = 0;
		}
		
		//get the ip of the user and print it
		out.println("Your ip address is: " + getClientIpAddr(request));
		//print out how many times the site has been accessed
		out.println("This site has been accessed " + count + " times.");
	}

//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
	
	/**
	 * Shut down and save count
	 */
	public void destroy(){
		super.destroy();
		try {
			dao.save(count);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
