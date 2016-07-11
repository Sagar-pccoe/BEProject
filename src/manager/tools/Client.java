
/***********************************************************************************
 * File 		:	Client.java
 * 
 * Author		:	Sagar Rathod
 *
 * Description	:	The object of this class is used by QueryManager component
 * 					to accept the connection. Each client has a separate thread
 * 					associated.
 * 
 * Version		: 	1.0
 * 
 * Date			: 	15th August 2014
 * 
 ************************************************************************************/

package manager.tools;


import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

import org.json.JSONArray;
import org.json.JSONObject;

import app.tools.User;
import tools.Query;
import manager.QuerySolver;

public class Client implements Runnable
{
	private Thread t;
	private Query query;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
    private boolean flag = true;
    private QuerySolver qs;
    private JTextArea area;
    
   public Client()
    {
    	 socket=null;
    	 query=null;
    	 socket=null;
    	 in=null;
    	 out=null;
    	 t=null;
    	 qs=null;
    }
   
   public Client(Socket sc,InputStream in,OutputStream out,String dbHost,String db,JTextArea area) throws Exception
   {
	   socket=sc;
	   this.in=new ObjectInputStream(in);
	   this.out=new ObjectOutputStream(out);
	   t=new Thread(this);
	   query = new Query();
	   qs = new QuerySolver(dbHost,db);
	   this.area = area;
	   t.start();
   }
   
   /***********************************************************************************
	 * Method 				:	run
	 * 
	 * Description			:	This method reads the request sent by client and to provide
	 * 							the response.
	 * 	
	 * Input Parameters		: 	0
	 * 
	 * Returns				: 	void
	 * 
	 ************************************************************************************/

   
 public void run()
 {
	
	 try
	 {
	
		 //System.out.println("Thread started");
		 
		 //query=(Query)in.readObject();
		 
		 JSONArray result;
		 Query response;
		 
		 // first authenticate user
		
		 String host = socket.getInetAddress().getHostAddress();
		 
		 query.readObject(in);
		 
		 //System.out.println(query);
		 
		 area.append("\nfrom host:[" + host +"]: " + query);
		 
		 JSONObject obj = query.getQuery().getJSONObject(1);
		 
		 String username = obj.getString("Username");
		 String pwd = obj.getString("Password");
		 
		 if(User.authenticate(username, pwd))
		 {
			  result = new JSONArray();
		 
			 result.put(new JSONObject().put("Status","true"));
		 
			  response=new Query(result);
			
			 response.writeObject(out);
		}
		 else
		 {
			 result = new JSONArray();
			 
			 result.put(new JSONObject().put("Status","false"));
		 
			  response=new Query(result);
			
			 response.writeObject(out);
			
			 return;
		 }
			 
		 
		 while(flag)
		 {
		 
			 query.readObject(in);
		 
			 //System.out.println(query);
		 
			 area.append("\nfrom host:[" + host +"]: " + query);
		 
			 //Object o= in.readObject();
		 
			 // System.out.println(o.toString());
		 
		     //System.out.println("Received Query:"+query); 
		 
			 result=qs.solve(query.getQuery());
		
			 response=new Query(result);
		
			 response.writeObject(out);
		 
			// System.out.println("Result Sent:"+result.toString());
		 
			 area.append("\nTo host:[" + host +"] Result Sent :" + result.toString());
			 
			 Thread.sleep(10);
	
		 }
	 }
	 catch(Exception e)
	 {
		 e.printStackTrace();
	 }
	 
	 finally
	 { 
		 //close();
	 }
 }
 
 /***********************************************************************************
	 * Method 				:	close
	 * 
	 * Description			:	This method closes the client socket.
	 * 	
	 * Input Parameters		: 	0
	 * 
	 * Returns				: 	void
	 * 
	 ************************************************************************************/
 
 public void close()
 {
	 try
	 {
		 if(socket!=null)
			 socket.close();
		 
	 }catch(Exception e)
	 {
		 e.printStackTrace();
	 }
 }
 
}
