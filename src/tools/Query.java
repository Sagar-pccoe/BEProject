package tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;


public class Query extends JSONArray implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient JSONArray arr;

	public Query()
	{
		arr = null;
	}
	
	public Query(JSONArray arr) throws JSONException
	{
		this.arr = arr;
	}
	
	
	public void writeObject(ObjectOutputStream oos) throws IOException
	{
		oos.writeObject(arr.toString());
	}
	
	public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, JSONException
	{
		
		arr = new JSONArray((String) ois.readObject());
	}
	
	public JSONArray getQuery()
	{
		return arr;
	}
	
	public String toString()
	{
		return arr.toString();
	}
	
}