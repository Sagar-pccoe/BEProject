package manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.JTextArea;

public class MongoDBStarter implements Runnable
{

	private HashMap<String,String> map;
	private Process process;
	private JTextArea area;
	private Thread thread;
	 boolean flag;
	
public MongoDBStarter(HashMap<String,String> map,JTextArea area)
	{
		this.map = map;
		this.area = area;
		thread = new Thread(this);
	}
	
	public void  start()
	{
		area.setText("");
		flag = true;
		 thread.start();
	}
	
	public void stop()
	{
		flag = false;
		process.destroy();
		area.setText("");
	}

	@Override
	public void run(){
		
		try
		{
	
		//ProcessBuilder pb = new ProcessBuilder("C:/Program Files/MongoDB 2.6 Standard/bin/mongod","--dbpath","C:/data/db","--logpath","f:/mongodb.log","--port","27017"); 
		
		 ProcessBuilder pb = new ProcessBuilder( map.get("Installation Path"),"--dbpath",map.get("DB Path"),"--port", map.get("Port")); 
			
		 process = pb.start();
		 
		 BufferedReader bf = new BufferedReader(new InputStreamReader(process.getInputStream()));
		 
		 String line;
		 while(flag)
		 {
			 line = bf.readLine();
			 if(line != null)
			 area.append("\n"+bf.readLine());
		     Thread.sleep(100);
		 }
		 
		 bf.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/*public static void main(String[] args) throws Exception {
		
		HashMap<String,String> map = new HashMap<String, String>();
		
		
			 map.put("Port","27017");
			 map.put("Log File Path","f:/mongodb.log");
			 map.put("DB Path","c:/data/db");
			 map.put("Hostname","localhost");
			map.put("Installation Path","C:/Program Files/MongoDB 2.6 Standard");
			
			MongoDBStarter m = new MongoDBStarter(map);
			
			

	}*/

}
