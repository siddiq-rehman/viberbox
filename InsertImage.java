import java.sql.*;

import org.apache.commons.fileupload.FileItem;

import java.io.*;  
public class InsertImage {  
//public static void main(String[] args) {
	void insertImage(String Name,String Password,String RandomNum, FileItem f){
try{  
Class.forName("oracle.jdbc.driver.OracleDriver");  
Connection con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","system","123");   
              
PreparedStatement ps=con.prepareStatement("insert into Data values(?,?,?,?)");  
ps.setString(1,Name);  
  
//FileInputStream fin=new FileInputStream("d:\\Chat\\FullSizeRender.jpg");  
FileInputStream fin1=(FileInputStream) f.getInputStream();
ps.setBinaryStream(2,fin1,fin1.available());  
ps.setString(3, Password);
ps.setString(4, RandomNum);
int i=ps.executeUpdate();  
System.out.println(i+" records affected");  
          
con.close();  
}catch (Exception e) {e.printStackTrace();}  
	}
} 

