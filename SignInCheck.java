import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.math.BigInteger; 
import java.security.SecureRandom;
import java.math.BigInteger;
public class SignInCheck {
	private String dypwd;
	Jedis jedis = new Jedis("localhost");
	 public String PwdSessionId() {
		 SecureRandom random = new SecureRandom();
	        return new BigInteger(130, random).toString(32);
	    }
//public static void main(String[] args) {
	 HashMap<String,String> signInCheck(String UserName, String Passwword, String FriendName){
		 HashMap<String,String> hm=new HashMap<String,String>();  
try{  
Class.forName("oracle.jdbc.driver.OracleDriver");  
Connection con=DriverManager.getConnection(  "jdbc:oracle:thin:@localhost:1521:xe","system","123");  
Connection con1=DriverManager.getConnection(  "jdbc:oracle:thin:@localhost:1521:xe","system","123");  
      String Query="select * from Data where NAME='"+UserName +"' and PASSWORD='"+Passwword +"'";
PreparedStatement ps=con.prepareStatement(Query);  
ResultSet rs=ps.executeQuery();  
/*if(rs.next()){//now on 1st row  
String str=rs.getString(1);              
Blob b=rs.getBlob(2);//2 means 2nd column data  
barr=b.getBytes(1,(int)b.length());//1 means first image  
 /*String FileLoc=  "d:\\Chat\\"+str+"sonoo.jpg";      
FileOutputStream fout=new FileOutputStream(FileLoc);  
fout.write(barr);  
              
fout.close();*/
//return barr;
//}//end of if  */
if (!rs.isBeforeFirst() ) {    
    System.out.println("No data"); 
    hm.put("Authentication","Fail"); 
}
else{
	String data_pwd=null;
	while(rs.next())  
		data_pwd=rs.getString(4); 
	
	String Query1="select * from Data where NAME='"+ FriendName+"'";
	PreparedStatement ps2=con.prepareStatement(Query1);  
	ResultSet rs2=ps2.executeQuery(); 
	if (!rs2.isBeforeFirst() ) {    
	    System.out.println("No data"); 
	    hm.put("Authentication","NoFriend"); 
	}
	else
	{
	hm.put("Authentication","Success");
	dypwd=PwdSessionId();
//added now		
	
	String sql = "UPDATE Data SET DYPWD ='"+ dypwd+"'WHERE NAME='"+UserName +"' and Password='"+Passwword +"'";
	ps=con.prepareStatement(sql);   
	int i2=ps.executeUpdate();  
	if(i2>0){
	
	String sql3="Select * from Data_FL where UserName='"+UserName+"' and FriendName='"+FriendName+"'";
	PreparedStatement ps3=con.prepareStatement(sql3);  
	ResultSet rs3=ps3.executeQuery(); 
	if (!rs3.isBeforeFirst() ) {    
	   PreparedStatement ps4=con.prepareStatement("insert into Data_FL values(?,?,?)");  
			ps4.setString(1,UserName);   
			ps4.setString(2,FriendName);
			ps4.setString(3,dypwd);
			int i=ps4.executeUpdate(); 
		if(i>0){
		hm.put("Db","Success");
		hm.put("Password", dypwd);
		Create_redis(UserName,FriendName, dypwd);
		}
		else{
			hm.put("Db","Success");
		}
	}
	else{
	String sql1 = "UPDATE Data_FL SET DYPWD1 ='"+ dypwd+"'WHERE UserName='"+UserName +"' and FriendName='"+FriendName +"'";
	PreparedStatement ps4=con.prepareStatement(sql1);   
	int i4=ps4.executeUpdate();  
	if(i4>0){
			hm.put("Db","Success");
		hm.put("Password", dypwd);
		Create_redis(UserName,FriendName, dypwd);
	}
	else{
		hm.put("Db","Success");
	}
	}
	}
	else{
		hm.put("Db","Success");
	}
	//adding now
	
}
}
con.close();
System.out.println("ok");  
//con.close(); 
 
}catch (Exception e) {e.printStackTrace();  }
return hm;
	}
	 
	 void Create_redis(String UserName,String FriendName,String Password){
		 
		 if(jedis.hexists("Users_Authorise",UserName+"_FL_"+FriendName)){
        	 jedis.hdel("Users_Authorise",UserName+"_FL_"+FriendName);
        	 jedis.hset("Users_Authorise",UserName+"_FL_"+FriendName,dypwd); 
        	 jedis.save();
         }
         else{
        	 jedis.hset("Users_Authorise",UserName+"_FL_"+FriendName,dypwd);
        	 jedis.save();
         }
		 
		List<String> L1= jedis.lrange(UserName+"_FL", 0, 20);
		int check=0;
		for(String s: L1){
			
			System.out.println(s);
			if(UserName+"_FL_"+FriendName.equals(s) != null){
				check=1;
			}
		}
		if(check==0){
			jedis.lpush(UserName+"_FL", UserName+"_FL_"+FriendName);
			jedis.lpush(UserName+"_FL_"+FriendName, UserName+"_FL_"+FriendName+"_Msgs",UserName+"_FL_"+FriendName+"_tobeSent",UserName+"_FL_"+FriendName+"_Received",UserName+"_FL_"+FriendName+"_Sent");
			jedis.save();
			System.out.println("Creating redis structures");
		}
		 
	 }
}  
 