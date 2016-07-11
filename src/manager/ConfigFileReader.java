package manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;


public class ConfigFileReader {

	public HashMap<String,String> readValues(String file)
	{
		HashMap<String,String> map;
		
		try {
			
				File f=new File(System.getProperty("user.dir") + "/conf/" + file);
				
				BufferedReader bf = new BufferedReader(new FileReader(f));
			
				String line = bf.readLine();
				String arr[];
				
				 map = new HashMap<String,String>();
				
				while(line!=null)
				{
					arr = line.split("=");
					
					map.put(arr[0], arr[1]);
					
					line = bf.readLine();
				}
				
				bf.close();
				
				
		} catch (Exception e) {
			
			map = null;
			
		}
		
		return map;
	}
	
	
 public	void writeValues(String file, HashMap<String,String> map)
	{
		try {
			
			
			PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/conf/" + file);
		
			
			Iterator<String> k = map.keySet().iterator();
			Iterator<String> v = map.values().iterator();
			
			
			
			while(k.hasNext() && v.hasNext())
			{
							
				pw.println(k.next() + "=" + v.next());
				pw.flush();
			
			} 

			pw.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
 
 public static void main(String args[]) throws IOException, URISyntaxException {
	

	
	 ConfigFileReader cfg =new ConfigFileReader();
	
	// File f=new File(ConfigFileReader.class.getResource("/manager/conf/mongod.confg").toURI());
	 //System.out.println(f.getAbsolutePath());
	 
	System.out.println(cfg.readValues("mongod.confg"));
	  
	// System.out.println(ConfigFileReader.class.getResource("/manager/conf/mongod.confg").getPath());
 
 }


}
