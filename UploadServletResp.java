

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadServletResp
 */
@WebServlet("/UploadServletResp")
public class UploadServletResp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	String UserName,Password;
    public UploadServletResp() {
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
        		UserName = ck[i].getValue();
        	}
			if ("pwd".equals(ck[i].getName())){
        		Password = ck[i].getValue();
        	}
			
		}
		System.out.println(UserName+"   "+Password);
		byte barr[]=new RetrieveImage().retrieveImg(UserName, Password);
		 String FileLoc=  "d:\\Chat\\sonoo.jpg";      
		FileOutputStream fout=new FileOutputStream(FileLoc);  
		fout.write(barr);  
		              
		fout.close();
		String name=ck[0].getValue(); 
		request.getParameter("UserNam");
		response.setContentType("image/jpeg");
		//File f = new File("D:\\Chat\\myImg.jpg");
		ByteArrayInputStream bais = new ByteArrayInputStream(barr);
		BufferedImage bi = ImageIO.read(bais);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

}
