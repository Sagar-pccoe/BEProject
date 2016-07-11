
/***********************************************************************************
 * File 		:	QueryManager.java
 * 
 * Author		:	Sagar Rathod
 *
 * Description	:	Query manager is component that satisfies the user request.
 * 					It acts as a interface between data warehouse and end user
 * 					access tools. It has capability to accept multiple client 
 * 					requests. 
 * 
 * Version		: 	1.0
 * 
 * Date			: 	15th August 2014
 * 
 ************************************************************************************/
package manager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

import manager.tools.Client;

public class QueryManager implements Runnable {
	
	private int port;
	private ServerSocket serverSocket=null;
	private Socket clientSocket=null;
	private boolean flag=false;
	private Thread thread;
	private String host;
	private String db;
	private JTextArea area;
	
	
	public QueryManager(int port,String host, String db,JTextArea area) {
		
		this.port = port;
		
		thread = new Thread(this);
		
		this.host = host;
		this.db = db;
		this.area = area;
		thread.start();
	}
	
	public void run()
	{
		start();
	}
	
	/***********************************************************************************
	 * Method 				:	start
	 * 
	 * Description			:	This method is used to start the query manager component.
	 * 	
	 * Input Parameters		: 	0
	 * 
	 * Returns				: 	void
	 * 
	 ************************************************************************************/
	
	public void start()
	{
		try
		{
			area.append("\n=====["+new java.util.Date()+"]: Query Manager Started.=====\nServer Information:"+InetAddress.getLocalHost());
			
			flag = true;
			
			serverSocket=new ServerSocket(port);
					
			while(flag)
			{
				
				area.append("\nWaiting for Connection..");
				
				clientSocket=serverSocket.accept();
				
				area.append("\n["+ new java.util.Date() +"]: Connection request recieved from IP address:" + clientSocket.getInetAddress());
				
				new Client(clientSocket,clientSocket.getInputStream(),clientSocket.getOutputStream(),host,db,area);
			}
			
			
		}
		catch(InterruptedException e)
		{
			area.append("\n=====["+new java.util.Date()+"]: Query Manager Stopped.");
			thread = null;
		}catch(IOException io)
		{
		
			area.append("\n"+io.getMessage());
		}
		catch(Exception e)
		{
			area.append("\n"+e.getMessage());
		}
		finally{
		    
			stop();
		}
		
	}
	
	/***********************************************************************************
	 * Method 				:	stop
	 * 
	 * Description			:	This method is used to stop the query manager component.
	 * 	
	 * Input Parameters		: 	0
	 * 
	 * Returns				: 	void
	 * 
	 ************************************************************************************/
	
	public void stop()
	{
		try
		{
			
			flag = false;
			
			if(thread!=null)
				thread.interrupt();
			if(serverSocket!=null)
			serverSocket.close();
			clientSocket=null;
			System.gc();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		
			//new QueryManager(1234);
			
		//	qm.stop();
	}

	public boolean getFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
