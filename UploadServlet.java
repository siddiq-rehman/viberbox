

import java.io.IOException;
import redis.clients.jedis.Jedis; 
import java.security.SecureRandom;
import java.math.BigInteger;
import java.io.*;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	   
	   private boolean isMultipart;
	   private String filePath;
	   private int maxFileSize = 5000 * 1024;
	   private int maxMemSize = 4 * 1024;
	   private File file ;
	   private String UserName;
	   private String Password;
	   FileItem fi ;
	   InsertImage Insimg=new InsertImage();
	   Cookie UserNm,Pwd;
	   private String RandomNum;
	   private SecureRandom random = new SecureRandom();
	   Jedis jedis = new Jedis("localhost");
	   
	   public void init( ){
	      // Get the file location where it would be stored.
		   System.out.println("hello");
	    //  filePath = getServletContext().getInitParameter("d:\\Chat\\Temp"); 
	   }
	   public String PwdSessionId() {
	        return new BigInteger(130, random).toString(32);
	    }
	   public void doPost(HttpServletRequest request, HttpServletResponse response)
	      throws ServletException, java.io.IOException {
		   System.out.println(PwdSessionId());
	   UserName=request.getParameter("UserName");
	      // Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
	      java.io.PrintWriter out = response.getWriter( );
	      //RequestDispatcher view = request.getRequestDispatcher("/SignUpResp/main.html");
	     // RequestDispatcher view1 = request.getRequestDispatcher();
	        // don't add your web-app name to the path
//response.sendRedirect("./SignUpResp/main.html");
	        //view.forward(request, response);
	      //  view1.forward(request, response);
	      if( !isMultipart ) {
	         out.println("<html>");
	         out.println("<head>");
	         out.println("<title>Servlet upload</title>");  
	         out.println("</head>");
	         out.println("<body>");
	         out.println("<p>No file uploaded</p>"); 
	         out.println("</body>");
	         out.println("</html>");
	         return;
	      }
	  
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	   
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxMemSize);
	   
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("d:\\Chat\\Temp"));

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	   
	      // maximum file size to be uploaded.
	      upload.setSizeMax( maxFileSize );

	      try { 
	         // Parse the request to get file items.
	         List fileItems = upload.parseRequest(request);
		
	         // Process the uploaded file items
	         Iterator i = fileItems.iterator();
	         while ( i.hasNext () ) {
	            fi = (FileItem)i.next();
	            if ( !fi.isFormField () ) {
	               // Get the uploaded file parameters
	               String fieldName = fi.getFieldName();
	               String fileName = fi.getName();
	               String contentType = fi.getContentType();
	               boolean isInMemory = fi.isInMemory();
	               long sizeInBytes = fi.getSize();
	            
	               // Write the file
	          /*     if( fileName.lastIndexOf("\\") >= 0 ) {
	                  file = new File( "d:\\Chat\\" + fileName.substring( fileName.lastIndexOf("\\"))) ;
	               } else {
	            	   String str="d:\\Chat\\" + fileName.substring(fileName.lastIndexOf("\\")+1);
	            	   System.out.println(str);
	                  file = new File( str) ;
	               }*/
	              // fi.write( file ) ;
	              
	               System.out.println("Uplading image by "+ UserName);
	               
	            }
	            else{
	            	
	            	if ("UserName".equals(fi.getFieldName())){
	            		UserName = fi.getString();
	            		UserNm=new Cookie("UserNm",UserName);
	            		//Pwd=new Cookie("pwd",Password);
	            	System.out.println(UserName);
	            	}
	            	if ("Password".equals(fi.getFieldName())){
	            		Password = fi.getString();
	            	//	Pwd=new Cookie("pwd",Password);
	            	System.out.println(Password);
	            	}
	            }
	         
	        }
	         RandomNum=PwdSessionId();
	         Pwd=new Cookie("pwd",RandomNum);
	         Insimg.insertImage(UserName,Password,RandomNum,fi);
	     /*    if(jedis.hexists("Users_Authorise",UserName)){
	        	 jedis.hdel("Users_Authorise",UserName);
	        	 jedis.hset("Users_Authorise",UserName,RandomNum); 
	        	 jedis.save();
	         }
	         else{
	        	 jedis.hset("Users_Authorise",UserName,RandomNum);
	        	 jedis.save();
	         }*/
	         
	         jedis.hset("USERS",UserName , UserName+"_FL");
	         jedis.save();
	         
	         response.addCookie( UserNm );
	         response.addCookie( Pwd );
	         response.sendRedirect("./SignUpResp/main.html");
	         } catch(Exception ex) {
	            System.out.println(ex);
	         }
	      }
	      
	      public void doGet(HttpServletRequest request, HttpServletResponse response)
	         throws ServletException, java.io.IOException {

	         throw new ServletException("GET method used with " +
	            getClass( ).getName( )+": POST method required.");
	      }
	   }
	