package com.BigData.dataanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import tools.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/*
 * This class is used to frame and send dynamic queries to server
 * 
 */
public class AnalysisActivity extends Activity implements OnItemSelectedListener, View.OnClickListener{
	
	Spinner spinner_time;		//Shows the fields of time dimension
	Spinner spinner_region;		//Shows the fields of region dimension
	Spinner spinner_product;	//Shows the fields of product dimension
	
	Spinner spinner_text_time;		//Shows the values of time dimension
	Spinner spinner_text_region;	//Shows the values of region dimension
	Spinner spinner_text_product;	//Shows the values of product dimension
	
	//ClientThread client;
	Query query;
	String oldKey;
	TextView textView;
	
	Reader reader;
	ResultReader resultReader;
	
	Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis);
		
		Intent i=getIntent();
		
		
		query = new Query();
		
		Vector<String>str = new Vector<String>();
		Vector<String>str1 = new Vector<String>();
		Vector<String>str2 = new Vector<String>();
		Vector<String>str3=new Vector<String>();
		
		BufferedReader in,in1,in2,in3;
		
		spinner_text_time = (Spinner) findViewById(R.id.spinner_text_time);
		spinner_text_product = (Spinner) findViewById(R.id.spinner_text_product);
	    spinner_text_region = (Spinner) findViewById(R.id.spinner_text_region);
		
	     btnSubmit = (Button) findViewById(R.id.btnSubmit);
	    
	     btnSubmit.setOnClickListener(this);
	     
		
		
		
/***
* Spinner_time - the file time.txt is written in the spinner of the time dimension
* 			    
*/
		
		try
		{
		in = new BufferedReader(new InputStreamReader(getAssets().open("time.txt")));

		    String line = in.readLine();
		    while (line != null) {

		        str.add(line);
		        line = in.readLine();
		    }
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		    spinner_time = (Spinner) findViewById(R.id.spinner_time);
		    spinner_time.setOnItemSelectedListener((OnItemSelectedListener) this);
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str);
		    spinner_time.setAdapter(adapter);
		try
		{
			in3 = new BufferedReader(new InputStreamReader(getAssets().open("None")));

			String line = in3.readLine();
			while (line != null){

			   str3.add(line);
			   line = in3.readLine();
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		spinner_text_time = (Spinner) findViewById(R.id.spinner_text_time);
	    spinner_text_time.setOnItemSelectedListener((OnItemSelectedListener) this);
	    ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_time.setAdapter(adapter1);
	    
	    adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_region.setAdapter(adapter1);
	    
	    adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_product.setAdapter(adapter1);
	    
	    
 /***
 * Spinner_order - the file Region in assets is written in the spinner of the order dimension
 * 			    
 *  */		    
		    try
			{
			in1 = new BufferedReader(new InputStreamReader(getAssets().open("Region")));

			    String line = in1.readLine();
			    while (line != null) {

			        str1.add(line);
			        line = in1.readLine();
			    }
			}catch(IOException e)
			{
				e.printStackTrace();
			}
			    spinner_region = (Spinner) findViewById(R.id.spinner_region);
			    spinner_region.setOnItemSelectedListener((OnItemSelectedListener) this);
			    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str1);
			    spinner_region.setAdapter(adapter2);
			    
/***
 * Spinner_product - the file product.txt is written in the spinner of the product dimension
 * 			    
 */
			    try
				{
				in2 = new BufferedReader(new InputStreamReader(getAssets().open("product.txt")));

				    String line = in2.readLine();
				    while (line != null) {

				        str2.add(line);
				        line = in2.readLine();
				    }
				}catch(IOException e)
				{
					e.printStackTrace();
				}
				    spinner_product = (Spinner) findViewById(R.id.spinner_product);
				    spinner_product.setOnItemSelectedListener((OnItemSelectedListener) this);
				    ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str2);
				    spinner_product.setAdapter(adapter11);
				
					
		
				    reader = new Reader();
				    
				    resultReader = new ResultReader();
				    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.analysis, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * This function sends query to the server when values field spinner is selected and corressponding values are populated 
	 * in the value spinner
	 */
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
			
		int id=parent.getId();
		//Obtain value of spinner selected and send the query to server
		switch(id){
				case R.id.spinner_time:
					try{
						String key=(String)spinner_time.getSelectedItem();
						
						if((!(key.equals("Select Time Dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
							//Framing the query that is to be sent to the server
							myobject.put("Query", "Get");
						
							jsonarray.put(myobject);
							
							myobject=new JSONObject();
							myobject.put("Dimension", "Time_Dimension");
							myobject.put("Key",key);
							myobject.put("Value", -1);
					    
							jsonarray.put(myobject);
							
							query = new Query(jsonarray);
							
							//Send query to server
							query.writeObject(LoginActivity.client.objout);
						    reader.setCurrentKey(key);		
						    reader.setCurrentSpinner(spinner_text_time);
						    
						    new Thread(reader).start();
							
						    oldKey=key;
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
					
				case R.id.spinner_region:
					try{
						String key=(String)spinner_region.getSelectedItem();
					
						if((!(key.equals("Select Region Dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
							
							//Framing the query that is to be sent to the server
							myobject.put("Query", "Get");
						
							jsonarray.put(myobject);
							
							myobject=new JSONObject();
							myobject.put("Dimension", "Region_Dimension");
							
							if(key.equals("Region_ID"))
								key = "_id";
							else
							myobject.put("Key",key );
							
							myobject.put("Value", -1);
					    
							jsonarray.put(myobject);
							
							query = new Query(jsonarray);
							
							//Send query to server
							query.writeObject(LoginActivity.client.objout);
							
						    reader.setCurrentKey(key);		
						    reader.setCurrentSpinner(spinner_text_region);
						    
						    new Thread(reader).start();
							
						    oldKey=key;
							
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
				case R.id.spinner_product:
					try{
						String key=(String)spinner_product.getSelectedItem();
						if((!(key.equals("Select product dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
							
							//Framing the query that is to be sent to the server
							myobject.put("Query", "Get");
						
							jsonarray.put(myobject);
							
							myobject=new JSONObject();
							myobject.put("Dimension", "Product_Dimension");
							
							if(key.equals("Product_ID"))
								key = "_id";
							else
							myobject.put("Key",key );
							
							myobject.put("Key",key );
							myobject.put("Value", -1);
					    
							jsonarray.put(myobject);
							
							query = new Query(jsonarray);
							
							//Send query to server
							query.writeObject(LoginActivity.client.objout);
						    reader.setCurrentKey(key);		

						    reader.setCurrentSpinner(spinner_text_product);
						    
						    new Thread(reader).start();
							
						    oldKey=key;
							
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
		}
					
	}
	
	
	//This function initializes the array that will contain the values sent by server
	class Reader implements Runnable
	{
	  
	  String key;
	  ArrayList<String> result;
	  Spinner currentSpinner;
	  
	  void setCurrentKey(String key)
	  {
		  this.key = key;
	  }
	  
	  void setCurrentSpinner(Spinner s)
	  {
		  currentSpinner = s;
	  }
	  
		public void run()
		{
			try
			{
				
				//read the values from server
				
				query = new Query();
				
				query.readObject(LoginActivity.client.objin);

				
				JSONArray arr = query.getQuery();
				
				result=new ArrayList<String>();
				
				int len = arr.length();
				
				JSONObject obj=null;
				
				for(int i=0; i < len; i++ )
				{
					obj = arr.getJSONObject(i);
					
					result.add(obj.getString(key));
				}
				
					new Download().execute();
		
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	//Sets value of the corresponding spinner as per query sent by user
	private class Download extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			return null;
		}
		
		protected void onPostExecute(Void args){
			
			 Collections.sort(reader.result);
			
			ArrayAdapter<String> a = new ArrayAdapter<String>(AnalysisActivity.this,android.R.layout.simple_dropdown_item_1line,reader.result);
			
			Spinner s = reader.currentSpinner;
			
			s.setAdapter(a);

		}
		
	}
		@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
		
	/*
	 * This class is activated when submit button is pressed 
	 * it sends the complete query for analysis 
	 * recieves the values and initializes the result class (Singleton pattern)	
	 */
	public class GetResult extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute(){
			Toast.makeText(getApplicationContext(), "Analyzing Query.....", 
					   Toast.LENGTH_SHORT).show();
   
		}
		
		
		/*
		 * Obtains Good, Average and Poor result from Server and initializes the result object
		 */
		@Override
		protected Void doInBackground(Void... arg) {	 
			try
			{
			query = new Query();
			
			query.readObject(LoginActivity.client.objin);	//Get Response from server

			JSONArray arr = query.getQuery();
			
			try {
				
				Result result = Result.getInstance();	//get instance of result class
				
				/* Set Good, Average and Poor Value*/
				
				JSONObject obj = arr.getJSONObject(0);
				result.setGood(obj.getDouble("Good"));
				
				
				obj = arr.getJSONObject(1);
				result.setPoor(obj.getDouble("Poor"));
				
				obj = arr.getJSONObject(2);
				result.setAvg(obj.getDouble("Average"));
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 }catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
		
		protected void onPostExecute(Void args){
			
			/*
			 *Direct user to next interface for drawing graph 
			 */
			Intent i=new Intent(AnalysisActivity.this,SelectGraphActivity.class);
		    startActivity(i);
			
			
			
		}
		
	}
	
	/*
	 * This function is activated when user presses submit button
	 */
	public void onClick(View arg0) {
		
		
		try {
			
			JSONArray arr = new JSONArray();
			
			JSONObject o1 = new JSONObject();
			
			//Frame Query to be sent to user
			o1.put("Query", "Analysis");
			
			arr.put(o1);
			
			o1 = new JSONObject();
			o1.put((String)spinner_time.getSelectedItem(),(String)spinner_text_time.getSelectedItem());
			
			
			o1.put((String)spinner_product.getSelectedItem(),(String)spinner_text_product.getSelectedItem());
			
		
			o1.put((String)spinner_region.getSelectedItem(),(String)spinner_text_region.getSelectedItem());
			
			arr.put(o1);
			//Query frame completed
			
			query = new Query(arr);
			
			//Send query to server
			query.writeObject(LoginActivity.client.objout);
			
			//Initiate Async Task
		    new GetResult().execute();
		}
			
			 catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
}

	class ResultReader implements Runnable
	{
		
		public void run()
		{
			try
			{
				//read the values from server
				
				query = new Query();
				
				query.readObject(LoginActivity.client.objin);
				
				textView = (TextView) findViewById(R.id.textView1);
				
				textView.setText(query.toString());
			
			}catch(Exception e)
			{
				
			}
		}
		
	}

}
