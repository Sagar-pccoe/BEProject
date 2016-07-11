package tools;

/*
 *This class provides methods for sending and receiving JSON array
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;


public class Query extends JSONArray implements Serializable
{

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
	
	/* Send JSON Array to server*/
	public void writeObject(ObjectOutputStream oos) throws IOException
	{
		oos.writeObject(arr.toString());
	}
	
	/* Recieve JSON Array from server*/
	public void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, JSONException
	{
		
		arr = new JSONArray((String) ois.readObject());
	}
	
	public JSONArray getQuery(){
		return arr;
	}
	public String toString()
	{
		return arr.toString();
	}
	
}