package manager;

/***********************************************************************************
*
*File		 : QuerySolver.java
*
*Description : The Code in the file is written to solve the ad-hoc queries
*			   
*			   upon android client request. 
*			
*Author      : Sagar Rathod
*
*Version     : 1.0
*
*Date 		 : 1/August/2014
***********************************************************************************/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import procedure.Process;
import procedure.tools.Average;

public class QuerySolver {

	String dbHost;
	String db;
	
	public QuerySolver(String dbHost,String db)
	{
        		
		this.dbHost = dbHost;
		this.db = db;
	}
	
	
	public static void main(String args[]) throws Exception
	  {
		  	JSONArray arr = new JSONArray();
		  	
		  	JSONObject obj = new JSONObject();
		  	
		  	obj.put("Query", "Analysis");
		  	
		  	arr.put(obj);
		  	
		  	obj = new JSONObject();
		  	obj.put("Year", 2013);
		  	obj.put("Product_Name","Xperia Z2");
		  	obj.put("Country", "Australia");
		  	
		  	arr.put(obj);
		  	
		  	System.out.println(arr);
		  	
		  	/*obj.put("Query", "Aggregation");
		  	
		  	obj.put("Type", "Triple");
		  	
		  	arr.put(obj);
		  	
		  	obj = new JSONObject();
		  	
		  	obj.put("Dimension", "Product_Dimension");
		  	
		  	obj.put("Key", "Category");
		  	
		  	obj.put("Value", "Mobile");

		  	arr.put(obj);
		  	
		  	obj = new JSONObject();
		  	
		  	obj.put("Dimension", "Region_Dimension");
		  	
		  	obj.put("Key", "Country");
		  	
		  	obj.put("Value", "Australia");

		  	arr.put(obj);
		  	
		  	obj = new JSONObject();
		  	
		  	obj.put("Dimension", "Time_Dimension");
		  	
		  	obj.put("Key", "Date");
		  	
		  	obj.put("Value", "27/1/2013");

		  	arr.put(obj);
		  	
		  	obj = new JSONObject();
		  	
		  	obj.put("Attribute_Measure", "Quantity_Sales");
		  	
		  	arr.put(obj);
		  	
		  	obj = new JSONObject();

		  	obj.put("Operation", "Sum");
		  	
		  	arr.put(obj);
		  	*/
		  	
		  	QuerySolver qs = new QuerySolver("localhost","testDB");
		  	
		  	System.out.println(qs.solve(arr));
	  }
	
  public JSONArray solve(JSONArray arr) throws Exception
	{
		Process p = new Process();
		p.connect(dbHost, db);
	  
		JSONObject jObj=(JSONObject)arr.get(0);
		
		String key=(String)jObj.get("Query");
		
		JSONArray result=new JSONArray();
		
		if(key.equals("Get"))
		{
		   jObj = (JSONObject)arr.get(1);
		   result = p.getValues((String)jObj.get("Dimension"),(String)jObj.get("Key"));
		   
		}
		else if(key.equals("Aggregation"))
		{
			
			//System.out.println("Sorry, Operation not defined.");
			
			String type = jObj.getString("Type");
			
			  // index 1 indicates dimension(s)
			
			if(type.equals("Single"))
			{
				jObj = arr.getJSONObject(1);
				
				 List<?> list = new ArrayList<Object>();
					
				   //1. find product ids from Product_Dimension table
				 String dimension = jObj.getString("Dimension");
				 String k = jObj.getString("Key");
				 String v= jObj.getString("Value");
				 String id="";
				 
				 if(dimension.equals("Time_Dimension"))
				 list= (List<String>)p.getTimeID(dimension,k,v);
				 else
					 list = (List<Integer>) p.getID(dimension, k, v);
				 
				 if(dimension.equals("Product_Dimension"))
					 id = "Product_id";
				 else if(dimension.equals("Region_Dimension"))
					 id = "Country_id";
				 else if(dimension.equals("Time_Dimension"))
					 id="Time_id";
				 
				 DBCursor cursor = p.findIn("Sales_Fact",id, list);
				 
				 jObj = arr.getJSONObject(2); // index 2 indicates aggregation key "Amount_Sales" or "Quantity_Sales" 
				 
				 String aggregationKey = jObj.getString("Attribute_Measure");
				 
				 jObj = arr.getJSONObject(3); //index 3 indicate aggregate operation "Average" or "Sum"
				 
				 String op = jObj.getString("Operation");
				 
				 if(op.equals("Average"))
				 {
					 Average res = p.average(cursor, aggregationKey);
					 
					 jObj = new JSONObject();
					 
					/* if(d.isInfinite()) 
						 + return 0 == d.compareTo(Double.POSITIVE_INFINITY) ? "23456789012E777" : "-23456789012E777"; 
						 + } */
					/* if(Double.isInfinite(res.average))
						 res.average = res.average == Double.POSITIVE_INFINITY ? 23456789012E777 : -23456789012E777;*/
					 Double d =new Double(res.average);
					 
					 jObj.put("Average",d.toString());
					 
					 result.put(jObj);
				 }
				 else if(op.equals("Sum"))
				 {
					 double res =  p.sum(cursor, aggregationKey);
					 
					 jObj = new JSONObject();
					 
					 //do comment
					 System.out.println(res);
					 
					 Double d = new Double(res);
					 jObj.put("Sum", d.toString());
					 
					 result.put(jObj);
				 }
				 
			}
			else if(type.equals("Double"))
				{
				
				jObj = arr.getJSONObject(1);
				
				 List<?> list = new ArrayList<Object>();
					
				   //1. find product ids from Product_Dimension table
				 String dimension1 = jObj.getString("Dimension");
				 String k1 = jObj.getString("Key");
				 String v1= jObj.getString("Value");
				 String id="";
				 
				 if(dimension1.equals("Time_Dimension"))
				 list= (List<String>)p.getTimeID(dimension1,k1,v1);
				 else
					 list = (List<Integer>) p.getID(dimension1, k1, v1);
				 
				 if(dimension1.equals("Product_Dimension"))
					 id = "Product_id";
				 else if(dimension1.equals("Region_Dimension"))
					 id = "Country_id";
				 else if(dimension1.equals("Time_Dimension"))
					 id="Time_id";
				 
				 DBCursor cursor = p.findIn("Sales_Fact",id, list);
				
				 jObj = arr.getJSONObject(2);
				 
				 String dimension2 = jObj.getString("Dimension");
				 String k2 = jObj.getString("Key");
				 String v2 = jObj.getString("Value");
				 String id2="";
	
				 //list=p.getID("Region_Dimension","Country","India");
				 
				 if(dimension2.equals("Time_Dimension"))
					 list= (List<String>) p.getTimeID(dimension2,k2,v2);
					 else
						 list = (List<Integer>) p.getID(dimension2, k2, v2);
				 
				 if(dimension2.equals("Product_Dimension"))
					 id2 = "Product_id";
				 else if(dimension2.equals("Region_Dimension"))
					 id2 = "Country_id";
				 else if(dimension2.equals("Time_Dimension"))
					 id2 ="Time_id";
			
				//4. Find all documents from cursor that matches above id's
					List<DBObject> set =  p.findIn(cursor,id2, list);
				
					 jObj = arr.getJSONObject(3); // index 2 indicates aggregation key "Amount_Sales" or "Quantity_Sales" 
					 
					 String aggregationKey = jObj.getString("Attribute_Measure");
					 
					 jObj = arr.getJSONObject(4); //index 3 indicate aggregate operation "Average" or "Sum"
					 
					 String op = jObj.getString("Operation");
					 
					 if(op.equals("Average"))
					 {
						 Average res = p.average(set, aggregationKey);
						 
						 jObj = new JSONObject();
						 
						/* if(d.isInfinite()) 
							 + return 0 == d.compareTo(Double.POSITIVE_INFINITY) ? "23456789012E777" : "-23456789012E777"; 
							 + } */
						/* if(Double.isInfinite(res.average))
							 res.average = res.average == Double.POSITIVE_INFINITY ? 23456789012E777 : -23456789012E777;*/
						 Double d =new Double(res.average);
						 
						 jObj.put("Average",d.toString());
						 
						 result.put(jObj);
					 }
					 else if(op.equals("Sum"))
					 {
						 double res =  p.sum(set, aggregationKey);
						 jObj = new JSONObject();
						 
						 Double d = new Double(res);
						 jObj.put("Sum", d.toString());
						 
						 result.put(jObj);
					 }
				}
			else if(type.equals("Triple"))
			{
		
				List<Integer> list = new ArrayList<Integer>();
				
				 jObj = arr.getJSONObject(1);
				   
				   //1. find product ids from Product_Dimension table
				   list=p.getID("Product_Dimension",(String)jObj.get("Key"),(String)jObj.get("Value"));
				
				   System.out.println("Product Ids:"+list);
				   
				 //2. find all documents that matches above product ids from Sales_Fact table
				   DBCursor cur = p.findIn("Sales_Fact","Product_id", list);
					
				  /* while(cur.hasNext())
					{
						System.out.println(cur.next());
					}*/
				   
				   //3. Find country IDs from Region dimension table 
				   jObj = arr.getJSONObject(2);
				    list=p.getID("Region_Dimension",(String)jObj.get("Key"),(String)jObj.get("Value"));
					
					System.out.println("Country ids:"+list);
					
					//4. Find all product documents from cursor that matches above region id's
					List<DBObject> set =  p.findIn(cur,"Country_id", list);
					
					//5. find date id's from Time_Dimension
					jObj = arr.getJSONObject(3);
					List<String> date = p.getTimeID("Time_Dimension",(String)jObj.get("Key"),(String)jObj.get("Value"));
					System.out.println("Time ids:"+date);
					
					//6. Find all product documents that matches above time id's
					
					System.out.println("Region && time matched product ids:");
					
					List<DBObject> o = p.findInList(set,"Time_id", date);
					
					/*Iterator<DBObject> i = o.iterator();
					
					while(i.hasNext())
					{
						System.out.println(i.next());
					}*/
					//7. Perform aggregation
					
					System.out.println(p.average(o, "Amount_Sales"));
					
					//cur = p.findIn("Sales_Fact","Time_id", i);					
					//cur = p.findIn("Sales_Fact","Time_id", i);
					
							 
					 jObj = arr.getJSONObject(4); // index 2 indicates aggregation key "Amount_Sales" or "Quantity_Sales" 
					 
					 String aggregationKey = jObj.getString("Attribute_Measure");
					 
					 jObj = arr.getJSONObject(5); //index 3 indicate aggregate operation "Average" or "Sum"
					 
					 String op = jObj.getString("Operation");
					 
					 if(op.equals("Average"))
					 {
						 Average res = p.average(o, aggregationKey);
						 
						 jObj = new JSONObject();
						 
						/* if(d.isInfinite()) 
							 + return 0 == d.compareTo(Double.POSITIVE_INFINITY) ? "23456789012E777" : "-23456789012E777"; 
							 + } */
						/* if(Double.isInfinite(res.average))
							 res.average = res.average == Double.POSITIVE_INFINITY ? 23456789012E777 : -23456789012E777;*/
						 Double d =new Double(res.average);
						 
						 jObj.put("Average",d.toString());
						 
						 System.out.println("avg:"+res);
						 
						 result.put(jObj);
					 }
					 else if(op.equals("Sum"))
					 {
						 double res =  p.sum(o, aggregationKey);
						 jObj = new JSONObject();
						 
						 Double d = new Double(res);
						 jObj.put("Sum", d.toString());
						 System.out.println("sum:"+res);
						 result.put(jObj);
					 }
			}
			
		    jObj = (JSONObject) arr.get(1);
		
			
		}else if(key.equals("Analysis"))
		{
			//[ { "Query":"Analysis" }, { "Month":"January", "Product_Name":"Xperia Z2", "Region" : "Oceania" } ]
			
			jObj = (JSONObject) arr.get(1);
		
			System.out.println("Analysis query:"+jObj);
			
			// Step 1. Calculate Prior probabilities
			
		    long good = p.getValue("Summary", "Class.Good");
			long poor = p.getValue("Summary", "Class.Poor");
			long avg = p.getValue("Summary", "Class.Average");
			
			
			long total = good + poor + avg;
			
			
			
			double prior_good = (double) good / total ;
			double prior_poor =  (double) poor / total ;
			double prior_avg = (double) avg / total ;
			
			
			System.out.println("\n[ Prior probalities \n(Counts):->{Good:"+good + " Poor:"+ poor + " Avg:"+ avg + " }\n( Total):-> {" + total + "}" );
			
			System.out.println("\n (probalities):-> {Good:"+prior_good +" Poor:"+prior_poor+" Avg:"+prior_avg +" } ] ");
			
			// Step 2. Calculate Posterior probabilities
			
			// get list of keys for eg:{"Month","Product_Name","Region"}
		    
			//Set<String> keys = (Set<String>)jObj.keys();
			
			Iterator<?> keyIterator= (Iterator<?>) jObj.keys();
		
			
			// Step 2.1 Get frequency counts
			
			String keyName;
			Object value = null;
	
			long value1_good=0, value1_poor=0, value1_avg=0;

			System.out.println("Attribute Frequency_Count");
			
			if(keyIterator.hasNext())
			{
				
				keyName = (String) keyIterator.next();
				value = jObj.get(keyName);
				
				 value1_good = p.getValue("Summary", keyName + "." + value + "_Good");
						
				 value1_poor = p.getValue("Summary", keyName + "." + value + "_Poor");
			
				 value1_avg = p.getValue("Summary", keyName + "." + value + "_Average");
			}
			
			System.out.println("Value:"+value);
			System.out.println("Good:"+value1_good + " Poor:"+ value1_poor + " Avg:"+ value1_avg);
			
			long value2_good=0, value2_poor=0, value2_avg=0;
			
			if(keyIterator.hasNext())
			{
				
				keyName = (String) keyIterator.next();
				value = jObj.get(keyName);
				
				 value2_good = p.getValue("Summary", keyName + "." + value + "_Good");
						
				 value2_poor = p.getValue("Summary", keyName + "." + value + "_Poor");
			
				 value2_avg = p.getValue("Summary", keyName + "." + value + "_Average");
			}
			
		
			System.out.println("Value:"+value);
			System.out.println("Good:"+value2_good + " Poor:"+ value2_poor + " Avg:"+ value2_avg);
			
			long value3_good = 0, value3_poor=0, value3_avg=0;
			
			if(keyIterator.hasNext())
			{
				
				keyName = (String) keyIterator.next();
				value = jObj.get(keyName);
				
				 value3_good = p.getValue("Summary", keyName + "." + value + "_Good");
						
				 value3_poor = p.getValue("Summary", keyName + "." + value + "_Poor");
			
				 value3_avg = p.getValue("Summary", keyName + "." + value + "_Average");
			}
			
			System.out.println("Value:"+value);
			System.out.println("Good:"+value3_good + " Poor:"+ value3_poor + " Avg:"+ value3_avg);
			
			// Step 2.2 Calculate posterior probabilities
			
			double posterior_good =  ((double) value1_good / good ) * ((double) value2_good / good) * ((double)value3_good / good);
			
			double posterior_poor =  ((double)value1_poor / poor ) * ((double)value2_poor / poor) * ((double)value3_poor / poor);
			
			double posterior_avg =  ((double)value1_avg / avg ) * ((double)value2_avg / avg) * ((double)value3_avg / avg);
			
			System.out.println("Posterior probabilities");
			System.out.println("Good:"+posterior_good +" Poor:"+posterior_poor+" Avg:"+ posterior_avg);
			
			
			// Step 3. Multiply Prior probabilities with posterior probabilities
			
			double g = prior_good * posterior_good ;
			double pr = prior_poor * posterior_poor ;
			double a = prior_avg * posterior_avg ;
			
			JSONObject o = new JSONObject();
			
			o.put("Good", g);
			result.put(o);
			
		    o = new JSONObject();
			o.put("Poor",pr);
			result.put(o);
			
			
			o = new JSONObject();
			
	    	o.put("Average",a);
			
			result.put(o);
			
		}
		
		p.close();
		
		
		return result;
	}
  
  
	
	
}
