package app.tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONException;

import tools.Query;

public class Client {
	
	private Socket socket;
	
	private ObjectInputStream objin;
	private ObjectOutputStream objout;
	public Query q;
	
	public Client(String hostName,int port) throws Exception
	{
		socket=new Socket(hostName,port);
		objout=new ObjectOutputStream(socket.getOutputStream());
		objin=new ObjectInputStream(socket.getInputStream());
		q = new Query();
	}
	
	public void sendQuery(Query q) throws IOException
	{
		this.q = q;
		
		this.q.writeObject(objout);
		
	}
	
	public Query receive() throws ClassNotFoundException, IOException, JSONException
	{
		this.q.readObject(objin);
		return q;
		
	}
	
	public void close() throws Exception
	{
		if(socket!=null)
		{
			objin.close();
			objout.close();
			socket.close();
			
			objin=null;
			objout=null;
			socket=null;
			System.gc();
			
		}
	}
	
}
