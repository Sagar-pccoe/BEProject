package app.tools;


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;



public class User implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private long mobileNo;
	private String email;

	
	public User(String un,String pwd,long mn, String ed)
	{
		userName = un;
		password = pwd;
		mobileNo = mn;
		email = ed;
				
	}
	
	public boolean insert() throws FileNotFoundException, IOException, ClassNotFoundException, URISyntaxException
	{
		
		ArrayList<User> list = (ArrayList<User>) User.read();
		
		Iterator<User> it1 = list.iterator();
		
		User user = null;
		
		while(it1.hasNext())
		{
			
			user = it1.next();
			
			if(user.userName.equals(this.userName))
			 return false;
		}
		
		list.add(this);
		
		Iterator<User> it2 = list.iterator();
		
		File f=new File(System.getProperty("user.dir") + "/conf/user.list");
		
		ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(f));
		
		while(it2.hasNext())
		{
	     	 user = it2.next();
	     	 
	     	 objout.writeObject(user);
		}
		
		
		objout.close();
		
		return true;
	}
	
	public static ArrayList<User> read() throws FileNotFoundException, IOException, ClassNotFoundException, URISyntaxException 
	{
		ArrayList<User> list = new ArrayList<User>();
		
		ObjectInputStream objin = null;
		
		File file = new File(System.getProperty("user.dir") + "/conf/user.list");
		try
		{
			 objin = new ObjectInputStream(new FileInputStream(file));
		
		}
		catch(EOFException e)
		{

			return list;
			
		}
		
		User u=null;
		
		do
		{
		
			try
			{
			
			   u = (User) objin.readObject();
			   list.add(u);
			}
			catch(EOFException e)
			{
				u = null;
			}
			
		}while(u!=null);
		
		objin.close();
		
		return list;
	}
	
	public static boolean authenticate(String un,String pwd) throws FileNotFoundException, ClassNotFoundException, IOException, URISyntaxException
	{
		ArrayList<User> list = read();
		
		Iterator<User> u = list.iterator();
		
		User user =null;
		
		while(u.hasNext())
		{
			
			user = u.next();
			
			if(user.getUsername().equals(un) && user.getPassword().equals(pwd))
			{
				return true;
			}
			
		}
		return false;
	}
	
	public String getUsername() {
		return userName;
	}
	public void setUsername(String username) {
		this.userName = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public static void main(String args[])
	{
		try
		{
		/*File file = new File("");
				
		    System.out.println("Absolute path:"+file.getAbsolutePath());
			System.out.println("c path:"+ file.getCanonicalPath());
			System.out.println(file.getPath());
			
			//System.out.println(User.read());*/
			
		//BufferedReader bf = new BufferedReader(new InputStreamReader(User.class.getResourceAsStream("/conf/user.list")));
			
			
		System.out.println(User.class.getResource("/conf/user.list").toURI());
			
		System.out.println(User.read());
		}catch(Exception e)		
		{
			e.printStackTrace();
		}
	}
	
	public String toString()
	{
		return userName + " " + password + " " + mobileNo + " " + email;
	}
}
