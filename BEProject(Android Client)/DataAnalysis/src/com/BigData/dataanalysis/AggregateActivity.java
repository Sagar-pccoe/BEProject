package com.BigData.dataanalysis;

/*
 * This class is used to perform Aggregation operation 
 */

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;


public class AggregateActivity extends Activity implements OnItemSelectedListener, View.OnClickListener{
	
	Spinner spinner_time;	
	Spinner spinner_region;
	Spinner spinner_product;
	
	Spinner spinner_text_time;
	Spinner spinner_text_region;
	Spinner spinner_text_product;
	Spinner spinner_sales;
	
	RadioGroup type;
	RadioButton radio_sa;	 
	

	
	JSONArray result;
	Query query;
	String oldKey;
	TextView textView;
	
	Reader reader;
	ResultReader resultReader;
	
	Button btnSubmit;
	
	String radioOp="";
	
	 ArrayAdapter<String> adapter1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aggregate);
		
		Intent i=getIntent();
		
		
		query = new Query();
		
		Vector<String>str = new Vector<String>();
		Vector<String>str1 = new Vector<String>();
		Vector<String>str2 = new Vector<String>();
		Vector<String>str3=new Vector<String>();
		Vector<String>str4=new Vector<String>();
		BufferedReader in,in1,in2,in3;
		
		spinner_text_time = (Spinner) findViewById(R.id.spinner_text_time);
		spinner_text_product = (Spinner) findViewById(R.id.spinner_text_product);
	    spinner_text_region = (Spinner) findViewById(R.id.spinner_text_region);
	    btnSubmit=(Button)findViewById(R.id.btnSubmit);
	    btnSubmit.setOnClickListener(this);
		
/***
* Spinner_time - the file time.txt is written in the spinner of the time dimension
* 			    
*/
		
		try
		{
		in = new BufferedReader(new InputStreamReader(getAssets().open("ATime")));

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
	   adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_time.setAdapter(adapter1);
	    
	    adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_region.setAdapter(adapter1);
	    
	    adapter1= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str3);
	    spinner_text_product.setAdapter(adapter1);
	    
	    
 /***
 * Spinner_text_Region - the file Region in assets is written in the spinner of the order dimension
 * 			    
 *  */		    
		    try
			{
			in1 = new BufferedReader(new InputStreamReader(getAssets().open("ARegion")));

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
 * Spinner_text_product - the file is written in the spinner of the product dimension
 * 			    
 */
			    try
				{
				in2 = new BufferedReader(new InputStreamReader(getAssets().open("AProduct")));

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
 /***
 * Aggregation - the file is written in Sales
 * 			    
*/
				    try
					{
					in3 = new BufferedReader(new InputStreamReader(getAssets().open("Sales")));

					    String line = in3.readLine();
					    while (line != null) {

					        str4.add(line);
					        line = in3.readLine();
					    }
					}catch(IOException e)
					{
						e.printStackTrace();
					}
					spinner_sales = (Spinner) findViewById(R.id.spinner_sales);
					spinner_sales.setOnItemSelectedListener((OnItemSelectedListener) this);
					ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, str4);
					spinner_sales.setAdapter(adapter12);					    
					    
					
				    reader = new Reader();
				    
				    
				    resultReader = new ResultReader();
				    
				    type=(RadioGroup)findViewById(R.id.type);
				    
				    			    
				    type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(RadioGroup arg0, int arg1) {
							int selectedID=type.getCheckedRadioButtonId();
							
							radio_sa = (RadioButton)findViewById(selectedID);	//Radio button for choosing between sum and average operation
							
							radioOp=(String)radio_sa.getText();
							
						}
					});
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aggregate, menu);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
			
		int id=parent.getId();
		switch(id){
				case R.id.spinner_time:
					try{
						String key=(String)spinner_time.getSelectedItem();
						
						if(key.equals("Select Time Dimension")){
							spinner_text_time.setAdapter(adapter1);
						}
						
							
						if((!(key.equals("Select Time Dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
							myobject.put("Query", "Get");
						
							jsonarray.put(myobject);
							
							myobject=new JSONObject();
							myobject.put("Dimension", "Time_Dimension");
							myobject.put("Key",key);
							myobject.put("Value", -1);
					    
							jsonarray.put(myobject);
							
							query = new Query(jsonarray);
							
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
						
						if(key.equals("Select Region Dimension")){
							spinner_text_region.setAdapter(adapter1);
						}
						
						if((!(key.equals("Select Region Dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
					
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
						
						if(key.equals("Select product Dimension")){
							spinner_text_product.setAdapter(adapter1);
						}
						if((!(key.equals("Select product dimension"))) && (!(key.equals(oldKey))))
						{
							JSONArray jsonarray = new JSONArray();
							JSONObject myobject=new JSONObject();
					
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
	
	private class Download extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			return null;
		}
		
		protected void onPostExecute(Void args){
			
			 Collections.sort(reader.result);
			
			ArrayAdapter<String> a = new ArrayAdapter<String>(AggregateActivity.this,android.R.layout.simple_dropdown_item_1line,reader.result);
			
			Spinner s = reader.currentSpinner;
			
			s.setAdapter(a);

		}
		
	}
		@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Get value from server and display in text view	
	 */
	public class GetResult extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute(){
			
			
			Toast.makeText(getApplicationContext(), "Analyzing Query.....", 
					   Toast.LENGTH_SHORT).show();
   
		}
		
		@Override
		protected Void doInBackground(Void... arg) {
			try
			{
				query = new Query();
			
				query.readObject(LoginActivity.client.objin);	

				result = query.getQuery();	//get result sent by server
			
				
				
			
			 }catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Void args){
			
			textView = (TextView) findViewById(R.id.textView1);	
						
				
				textView.setText(result.toString());	//Display the value in TextView
			
			
			
		
			
		}
		
	}
	
	
	/*
	 * Used to send the values on which aggregation operation is to be performed
	 */
	
	public void onClick(View arg0) {
		
		
		try {
			
			JSONArray arr = new JSONArray();
			
			JSONObject o1 = new JSONObject();
			o1.put("Query", "Aggregation");
			
			 
			String pdt_key=(String)spinner_product.getSelectedItem();
			
			String region_key=(String)spinner_region.getSelectedItem();
			
			String time_key=(String)spinner_time.getSelectedItem();
			
			if((pdt_key.equals("Select product dimension")) && (region_key.equals("Select Region Dimension")) && (time_key.equals("Select Time Dimension"))){
				Toast.makeText(getApplicationContext(),"Please select at least one value", Toast.LENGTH_SHORT).show();
				return;
			}
			
			//Send all three fields to perform aggregation
			if((!pdt_key.equals("Select product dimension")) && (!region_key.equals("Select Region Dimension")) && (!time_key.equals("Select Time Dimension"))){
				o1.put("Type","Triple");
			}
			
			//Send all two fields to perform aggregation
			else if((!pdt_key.equals("Select product dimension")) && (!region_key.equals("Select Region Dimension")) || (!pdt_key.equals("Select product dimension")) && (!time_key.equals("Select Time Dimension")) || (!region_key.equals("Select Region dimension")) && (!time_key.equals("Select Time Dimension"))){
				o1.put("Type","Double");
			}
			
			//Send all one field to perform aggregation
			else{
				o1.put("Type","Single");
			}
			
			arr.put(0,o1);
			
			int i=1;
			
			String pdt_text_key=(String)spinner_text_product.getSelectedItem();
			
			//Framing of query begins
			if((!pdt_key.equals("Select product dimension")) && (!pdt_text_key.equals("None"))){
			
				o1 = new JSONObject();
				o1.put("Dimension", "Product_Dimension");
				o1.put("Key", pdt_key);
				o1.put("Value", pdt_text_key);
				arr.put(i,o1);
				i++;
			
			}
			
			String region_text_key=(String)spinner_text_region.getSelectedItem();
			
			
			if((!region_key.equals("Select Region Dimension")) && (!region_text_key.equals("None"))){
			
				o1 = new JSONObject();
				o1.put("Dimension", "Region_Dimension");
				o1.put("Key", region_key);
				o1.put("Value", region_text_key);
				arr.put(i,o1);
				i++;
			
			}
			
			String time_text_key=(String)spinner_text_time.getSelectedItem();
			
			if((!time_key.equals("Select Time Dimension")) && (!time_text_key.equals("None"))){
				o1 = new JSONObject();
				o1.put("Dimension", "Time_Dimension");
				o1.put("Key", time_key);
				o1.put("Value", time_text_key);
				
				arr.put(i,o1);
				i++;
			
			}
			
			
			o1 = new JSONObject();
			
			o1.put("Attribute_Measure",(String)spinner_sales.getSelectedItem());
			
			arr.put(i,o1);
			i++;
			
			//Select type of aggregation operation to be performed
			if(radioOp.equals(""))
			{
				Toast.makeText(getApplicationContext(),"Please select Aggregate operation to perform.", Toast.LENGTH_SHORT).show();
				return;
			}
			else
			{
				
			o1=new JSONObject();
			o1.put("Operation",(String)radio_sa.getText());
			
			arr.put(i,o1);
			i++;
			}
		
			//End of framing query
			query = new Query(arr);
			
			//Send query
			query.writeObject(LoginActivity.client.objout);
			
			
		    new GetResult().execute();	//Initiate Async task to display the result obtained from server
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
