package backend;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;


public class AccountManager extends SQLManager {
    //private String user;
	private int current_user_id = -1;
    
    public boolean login(String username, String password){
//    	System.out.print("Enter your username: ");
//    	String username = obj.nextLine().toLowerCase();
//
//    	System.out.print("Enter your password: ");
//    	String password = passHash(obj.nextLine());
		password = passHash(password);
    	if(!searchForMatch("users","username",username) ||
		   !searchForMatch("users","password",password)) {
			System.out.println("Username or Password is incorrect. ");
    		return false;
    	}else {
    		System.out.println("Login Successful! ");
    	}
    	current_user_id = getUID(username);
		
       return true;
    }

    public String signup(String username, String password, String conPassword){

    	String data = null;

   		if(!password.equals(conPassword)) {
   			System.out.println("Password did not match. ");
   			return "pnomatch";
   		}
   		int uid = maxVal("users", "user_id")+1;
   		
   		data = uid + " ,'" + username + "' , '" + passHash(password) + "' ";
   		
   		addColumn("users","user_id, username, password",data);
   		
   		System.out.println("Success");

		return "success";
	}

	private int getUID(String username){
		Connection connection = connectDatabase();

		try {
			Statement stmt = connection.createStatement();

			String q1 = "SELECT user_id FROM users WHERE users.username = '"+username+"';";
			ResultSet rs = stmt.executeQuery(q1);

			if (rs.next()){
				return rs.getInt(1);
			}

		} catch (Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return -1;
	}

	public int verifyUser(String username, String password){
		password = passHash(password);

		if(!searchForMatch("users","username",username)) {

			System.out.println("Username does not exist.");
			return -1;
		}

		String userId = getUserId(username)+ "";

		if(!(Objects.equals(checkPassword(userId), password))) {

			System.out.println("Password does not match Username. ");
			return -1;
		}

		current_user_id = getUID(username);

		return current_user_id;
	}

	public boolean updateUser(int uid, String username, String password){
		password = passHash(password);
		if (uid != -1){
			updateColumn("users", "username",  username, "user_id", uid+"");
			updateColumn("users", "password",  password, "user_id", uid+"");
			System.out.println("Username and Password have been successfully updated!");
			return true;
		} else{
			System.out.println("ERROR: CURRENT UID NULL");
			return false;
		}

	}

	public int getCurrentUserID(){
		return current_user_id;
	}
	private static String passHash(String input) {
	  try {
		  MessageDigest digest = MessageDigest.getInstance("SHA256");
		  digest.update(input.getBytes());
		  byte[] result = digest.digest();
		  StringBuilder sb = new StringBuilder();

		  for(byte b : result) {
			  sb.append(String.format("%02x", b));
		  }
		  return sb.toString().substring(0,20);

	  }catch(Exception e) {
		  e.printStackTrace();
	  }finally{}

	  return null;
	}
}
