

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SignInServlet
 */
@WebServlet("/SignInServlet")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String UserName, FriendName, Password;
	String UserName1, FriendName1, Password1;
	 Cookie UserNm,Token,FriendNm;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignInServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
			Cookie ck[]=request.getCookies();
			for(int i=0;i<ck.length;i++){
				if ("UserNm".equals(ck[i].getName())){
	        		UserName1 = ck[i].getValue();
	        	}
				if ("Token".equals(ck[i].getName())){
	        		Password1 = ck[i].getValue();
	        	}
				if ("FriendNm".equals(ck[i].getName())){
	        		FriendName1 = ck[i].getValue();
	        	}
			}
			if(request.getParameter("UserName") != null){
			System.out.println(UserName1+"   "+Password1);
			byte barr[]=new RetrieveImage_Signin().retrieveImg(UserName1,FriendName1, Password1);
			  
			response.setContentType("image/jpeg");
			//File f = new File("D:\\Chat\\myImg.jpg");
			ByteArrayInputStream bais = new ByteArrayInputStream(barr);
			BufferedImage bi = ImageIO.read(bais);
			OutputStream out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.close();
		}
			if(request.getParameter("FriendName") != null){
				System.out.println(UserName1+"   "+Password1);
				byte barr[]=new RetrieveImage().retrieveImg(FriendName1, Password1);
				  
				response.setContentType("image/jpeg");
				//File f = new File("D:\\Chat\\myImg.jpg");
				ByteArrayInputStream bais = new ByteArrayInputStream(barr);
				BufferedImage bi = ImageIO.read(bais);
				OutputStream out = response.getOutputStream();
				ImageIO.write(bi, "jpg", out);
				out.close();
			}
		
		
		
		
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String Authentication = null,Db = null,Password=null;
		UserName= request.getParameter("UserName");
		FriendName=request.getParameter("FriendName");
		Password=request.getParameter("Password");
		HashMap<String,String> hm=new SignInCheck().signInCheck(UserName,Password,FriendName);
		 for(Map.Entry m:hm.entrySet()){  
			   System.out.println(m.getKey()+" "+m.getValue());
			   if("Authentication".equals(m.getKey())){
				   Authentication=(String) m.getValue();
			   }
			   if("Db".equals(m.getKey())){
				   Db=(String) m.getValue(); 
			   }
			   if("Password".equals(m.getKey())){
				   Password=(String) m.getValue();
			   }
			  }
		System.out.println("here");
		if(UserName.equals(FriendName)){
			response.sendRedirect("./SignIn/main_error_you.html");
		}
		else{
		if(Authentication=="Success" && Db=="Success" && Password!=null)
		{
			UserNm=new Cookie("UserNm",UserName);
			FriendNm=new Cookie("FriendNm",FriendName);
			Token=new Cookie("Token",Password);
			UserNm.setMaxAge(60 * 60*2);
			Token.setMaxAge(60 * 60*2);
			FriendNm.setMaxAge(60 * 60*2);

	         response.addCookie( UserNm );
	         response.addCookie( Token );
	         response.addCookie(FriendNm);
			response.sendRedirect("./ChatUI/main.html");
		}
		else{
			if(Authentication=="NoFriend")
			{
				response.sendRedirect("./SignIn/main_NoFriend.html");
			}
			else
			response.sendRedirect("./SignIn/main_error.html");
		}
		}
		
		//doGet(request, response);
	}

}
