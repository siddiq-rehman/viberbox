import java.sql.*;  
import java.io.*;  
public class RetrieveImage {  
//public static void main(String[] args) {
	byte[] retrieveImg(String UserName, String Passwword){
		byte barr[] = null;
try{  
Class.forName("oracle.jdbc.driver.OracleDriver");  
Connection con=DriverManager.getConnection(  
"jdbc:oracle:thin:@localhost:1521:xe","system","123");  
      String Query="select * from Data where NAME='"+UserName+"'";
PreparedStatement ps=con.prepareStatement(Query);  
ResultSet rs=ps.executeQuery();  
if(rs.next()){//now on 1st row  
String str=rs.getString(1);              
Blob b=rs.getBlob(2);//2 means 2nd column data  
barr=b.getBytes(1,(int)b.length());//1 means first image  
 /*String FileLoc=  "d:\\Chat\\"+str+"sonoo.jpg";      
FileOutputStream fout=new FileOutputStream(FileLoc);  
fout.write(barr);  
              
fout.close();*/
//return barr;
}//end of if  
System.out.println("ok");  
              
con.close();  
}catch (Exception e) {e.printStackTrace();  }
return barr;  
	}
}  
 