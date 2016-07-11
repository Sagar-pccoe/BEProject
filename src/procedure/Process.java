package procedure;

/***********************************************************************************
*
*File		 : Process.java
*
*Description : The Code in the file is written to perform various
*			   aggregate functions such as(sum,average etc.) on 
*			   Collections of MongoDb. 
*			
*Author      : Sagar Rathod
*
*Version     : 1.0
*
*Date 		 : 3/March/2015
***********************************************************************************/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.BSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import procedure.tools.Average;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Process
{
	
	public MongoClient mc;
	public DB db;
	
	
/*************************************************************************
*
*Method		  	  : connect
*
*Description	  : This method connects to the MongoDB database test on localhost
*				    and on default port no:27017 
*			  		 
*Input Parameters : 0
*
*Output		  	  : connection to the database.
*
*Returns		  : void. 
*************************************************************************/
	
	void connect(String dbName) throws Exception
	{
	     mc=new MongoClient("localhost",27017);
		 db=mc.getDB(dbName);
		
	}
	
	/***********************************************************************************
	 * Method 				:	connect
	 * 
	 * Description			:	This method is used to connect to MongoDB database specified
	 * 							by "dbname" on host machine specified by "host".
	 * 	
	 * Input Parameters		: 	2
	 * host					-  	The host machine name or IP address.
	 * dbname				-	The database name to connect.
	 * 
	 * Returns				: 	void
	 * 
	 ************************************************************************************/   
   
     public void connect(String host,String dbname) throws Exception
      {
    	  mc=new MongoClient(host,27017);
    	  db=mc.getDB(dbname);
      }
	
     public void close() throws Exception
     {
    	
    	 if(mc!=null)
    	   mc.close();
    	 
     }
     
	public static void main(String args[])
	{
		try
		{
			
			Process p=new Process();
			
			p.connect("testDB");
			
			//JSONArray arr = p.groupByAverageJSON("Sales_Fact", "Time_id", "Amount_Sales");
			
			//System.out.println(arr);
			
			
			//JSONArray arr = p.getValues("Product_Dimension", "Product_Name");
			
			//System.out.println(arr);
			
	//		long i = p.getValue("Summary", "Pisa_Good");
	//		System.out.println(i);
			
			//System.out.println(p.sum("Sales_Fact", "Amount_Sales","Product_id",1));
			
			
			//DBCursor c=p.findIn("Sales_Fact", "Product_id", list,"");
			
			//System.out.println(p.sum(c, "Amount_Sales"));
			
		   List<Integer> list = new ArrayList<Integer>();
		
		   
		   //1. find product ids from Product_Dimension table
		   list=p.getID("Product_Dimension","Product_Name","Xperia Z2");
		
		   System.out.println("Product Ids:"+list);
		  
		 //2. find all documents that matches above product ids from Sales_Fact table
		   DBCursor cur = p.findIn("Sales_Fact","Product_id", list);
			   

		   //3. Find country IDs from Region dimension table 
		    list=p.getID("Region_Dimension","Country","India");
			
			System.out.println("Country ids:"+list);
			
			//4. Find all product documents from cursor that matches above region id's
			List<DBObject> set =  p.findIn(cur,"Country_id", list);
			
			//5. find date id's from Time_Dimension
			List<String> date = p.getTimeID("Time_Dimension","Day", "Tuesday");
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
				  
			System.out.println(p.sum(cur, "Quantity_Sales"));
				
				//cur = p.findIn("Sales_Fact","Time_id", i);
			
			/*while(cur.hasNext())
			{
				System.out.println(cur.next());
			}
			*/
			 
			//System.out.println(p.average(cur, "Amount_Sales"));
			
			
			
			
		//	System.out.println(arr.toString());
			
	// 1.	System.out.println("Total Sum:"+p.sum("Order_Details","UnitPrice"));
	
			
	// 2.   Average avg=p.average("Order_Details", "UnitPrice");	
	 //2.  System.out.println(avg);
			
	//  3.	p.groupBySum("Order_Details", "OrderID","UnitPrice");
	
	//4.	p.groupByAverage("Order_Details", "OrderID","UnitPrice");
	
    //5.    p.orderBy("Order_Details","UnitPrice", 1);
			
	//6.  p.join("Customer_Dimension", "Order_Dimension", "Customer_name" ,"CustomerName");
	   
			p.close();
					}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public List<Integer> getID(String collectionName,String key,String value) throws Exception
	{
		DBCollection coll=db.getCollection(collectionName);
		
		BasicDBObject o=new BasicDBObject();
		
		o.put(key,value);
		
		List<Integer> list = new ArrayList<Integer>();
		
		DBCursor cursor=coll.find(o);
		
		DBObject obj;
		
		while(cursor.hasNext())
		{
			obj = cursor.next();
			list.add((Integer)obj.get("_id"));
		}
		
		return list;
	}
	
	public List<String> getTimeID(String collectionName,String key,String value) throws Exception
	{
		DBCollection coll=db.getCollection(collectionName);
		
		BasicDBObject o=new BasicDBObject();
		
		o.put(key,value);
		
		List<String> list = new ArrayList<String>();
		
		DBCursor cursor=coll.find(o);
		
		DBObject obj;
		
		while(cursor.hasNext())
		{
			obj = cursor.next();
			list.add((String)obj.get("_id"));
		}
		
		return list;
	}

	
	
public double sum(String collectionName,String aggregationkey,String keyCondition,Object value)
	{
		
		DBCollection c = db.getCollection(collectionName);
		
		BasicDBObject o=new BasicDBObject();
		o.put(keyCondition, value);
		
		//o.put(key,new BasicDBObject("$in",list));
		
		DBCursor cursor = c.find(o);
			
		DBObject doc; 
			
			double sum = 0;
			
			while(cursor.hasNext())
			{
				doc=cursor.next();
				
			//	if(doc.get(keyCondition)==value)
				sum = sum + Double.parseDouble(doc.get(aggregationkey).toString());
				//System.out.println(doc.toString());
			}
			
		return sum;
	}

	
/*************************************************************************
*@overloaded
*Method		  	  : sum
*
*Description	  : This method performs the on aggregate function sum on collection
*					specified by collectionName based on key. 
*
*Input Parameters : 2
*1.collectionName- Name of collection to perform aggregation.
*2.aggregationKey- The key in the collection on which to perform aggregate function.
*
*Returns		  : Total sum. 
*************************************************************************/

public double sum(String collectionName,String aggregationkey)
{

	
	DBCollection c = db.getCollection(collectionName);
		
	DBCursor cursor = c.find();
		
		DBObject doc; 
		
		double sum = 0;
		
		while(cursor.hasNext())
		{
			doc=cursor.next();	
				
			sum = sum + Double.parseDouble(doc.get(aggregationkey).toString());
			
		}
		
	return sum;
}


public double sum(List<DBObject> c,String aggregationkey)
{
		
		DBObject doc; 
		
		double sum=0;
		
		Iterator<DBObject> cursor = c.iterator();
		
		while(cursor.hasNext())
		{
			doc=cursor.next();	
			
			sum = sum + Double.parseDouble(doc.get(aggregationkey).toString());
			
		}
		
	return sum;
}

/*************************************************************************
*@overloaded
*Method		  	  : sum
*
*Description	  : This method performs the on aggregate function sum on collection
*					already retrieved in DBCursor based on key. 
*
*Input Parameters : 2
*1.cursor		 - The DBCursror containing the collection.
*2.aggregationKey- The key in the collection on which to perform aggregate function.
*
*Returns		  : Total sum. 
*************************************************************************/

public double sum(DBCursor cursor,String aggregationkey)
{
		
		DBObject doc; 
		
		double sum=0;
		
		while(cursor.hasNext())
		{
			doc=cursor.next();	
		
			
			sum = sum + Double.parseDouble(doc.get(aggregationkey).toString());
			
		}
		
	return sum;
}

/*************************************************************************
*@overloaded
*Method		 	  : average
*
*Description	  : This method performs the aggregate function average on single collection 
*					specified by collectionName based on aggregationKey.
*
*Input Parameters : 2
*1.collectionName - Name of collection to perform aggregate operation.
*2.aggregationKey - The key in the collection on which to perform aggregate function.
*
*Returns		  : Average Object that contains[Average:avg Sum:sum Observation:observations] 
*************************************************************************/

public Average average(String collectionName,String aggregationkey)
{
	
	DBCollection c = db.getCollection(collectionName);
		
		DBCursor cursor = c.find();
		
		DBObject doc; 
		
		Average avg = new Average();
		
		while(cursor.hasNext())
		{
			doc=cursor.next();	
				
			avg.sum = avg.sum + Double.parseDouble(doc.get(aggregationkey).toString());
			
			avg.observation++;
		}
		
		avg.average = (double) avg.sum / avg.observation;
		
	return avg;
}

/*************************************************************************
*@overloaded
*Method		  	  : average
*
*Description	  : This method performs the aggregate function average on single collection 
*					already retrieved in DBCursor based on key.
*
*Input Parameters : 2
*1.cursor		  - The DBCursror containing the collection.
*2.aggregationKey - The key in the collection on which to perform aggregate function.
*
*Returns		  : Average Object that contains[Average:avg Sum:sum Observation:observations] 
*************************************************************************/


public Average average(DBCursor cursor,String aggregationkey)
{
		
		DBObject doc; 
		
		Average avg = new Average();
		
		
		
		while(cursor.hasNext())
		{
			doc = cursor.next();	
				
			avg.sum = avg.sum + Double.parseDouble(doc.get(aggregationkey).toString());
		
			avg.observation++;
		}
		
		avg.average = (double) avg.sum / avg.observation;
		
	return avg;
}


public Average average(List<DBObject> list,String aggregationkey)
{
		
		DBObject doc; 
		
		Average avg = new Average();
		
		Iterator<DBObject> cursor = list.iterator();
		
		while(cursor.hasNext())
		{
			doc = cursor.next();	
				
			avg.sum = avg.sum + Double.parseDouble(doc.get(aggregationkey).toString());
		
			avg.observation++;
		}
		
		avg.average = (double) avg.sum / avg.observation;
		
	return avg;
}


/*************************************************************************
*
*Method		  	  : groupBySum
*
*Description	  : This method performs the group by operation on single collection based 
*			  		on aggregate function sum. 
*
*Input Parameters :
*1.collectionName - Name of collection to perform group by.
*2.groupByKey	  - The key in the collection on which to perform group by.
*3.aggregationKey - The key in the collection on which to perform aggregate function.
*
*Output		  	  : Displays the result on console.
*
*Returns		  : void. 
*************************************************************************/


public void groupBySum(String collectionName,String groupByKey,String aggregationKey)
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o;
	
	List<?> distinct = coll.distinct(groupByKey);
	
	
	Iterator<?> cursor = distinct.iterator();
	
	DBCursor groupCursor;
	
	Object value;
	
	System.out.println(groupByKey +" "+"Sum");
	
	while(cursor.hasNext()){
	
		value=cursor.next();
		
		o=new BasicDBObject();
		
		o.put(groupByKey,value);
		
		groupCursor=coll.find(o);
		
	  	System.out.println(value +" "+ sum(groupCursor,aggregationKey));	
	}
	
}




/*************************************************************************
*
*Method		  	  : groupBySumJSON
*
*Description	  : This method performs the group by operation on single collection based 
*			  		on aggregate function sum. 
*
*Input Parameters :
*1.collectionName - Name of collection to perform group by.
*2.groupByKey	  - The key in the collection on which to perform group by.
*3.aggregationKey - The key in the collection on which to perform aggregate function.
*
*
*Returns		  : JSONArray, it contains the final result 
 * @throws JSONException 
*************************************************************************/

public JSONArray groupBySumJSON(String collectionName,String groupByKey,String aggregationKey) throws JSONException
{
	//get collection from database
	DBCollection coll = db.getCollection(collectionName);
	
	//to store final result in json array
	JSONArray arr=new JSONArray();
	
	
	JSONObject jObj;
	
	BasicDBObject o;
	
	//first get the distinct documents from collection
	
	List<?> list = coll.distinct(groupByKey);
	
	Iterator<?> cursor= list.iterator();
	
	DBCursor groupCursor;
	
	Object value;
	
	//System.out.println(groupByKey +" "+"Sum");
	
	while(cursor.hasNext()){
	
		//get the document
		value = cursor.next();
		
		o=new BasicDBObject();
		
		o.put(groupByKey,value);
		
		// find the documents in a same collection that matches the groupBykey value
		groupCursor=coll.find(o);
		
		//System.out.println(value +" "+ sum(groupCursor,aggregationKey));
		
		jObj=new JSONObject();
		
		// put the group by key value and find the sum and put the final sum in json object
		jObj.put(groupByKey,value);
		
		jObj.put("Sum", sum(groupCursor,aggregationKey));
		
		//add JSON object in json array
		
		arr.put(jObj);
	}
	
	return arr;
	
}



/*************************************************************************
*
*Method		      : groupByAverage
*
*Description	  : This method performs the group by operation on single collection based 
*			  		on aggregate function average. 
*
*Input Parameters :
*1.collectionName - Name of collection to perform group by.
*2.groupByKey	  - The key in the collection on which to perform group by.
*3.aggregationKey - The key in the collection on which to perform aggregate function.
*
*Output		      : Displays the result on console.
*
*Returns		  : void. 
*************************************************************************/

public void groupByAverage(String collectionName,String groupByKey,String aggregationKey)
{	
	//get collection from database
	DBCollection coll=db.getCollection(collectionName);
	
	
	BasicDBObject o;
	
	List<?> list=coll.distinct(groupByKey);
	
	Iterator<?> cursor=list.iterator();
	
	DBCursor groupCursor;
	
	Object value;
	
	System.out.println(groupByKey +" "+"Average");
	
	while(cursor.hasNext()){
	
		value=cursor.next();
		
		o=new BasicDBObject();
		
		o.put(groupByKey,value);
		
		groupCursor=coll.find(o);
		
	  System.out.println(value +" "+ average(groupCursor,aggregationKey));
	
	}
	
}

/*************************************************************************
*
*Method		      : groupByAverageJSON
*
*Description	  : This method performs the group by operation on single collection based 
*			  		on aggregate function average. 
*
*Input Parameters :
*1.collectionName - Name of collection to perform group by.
*2.groupByKey	  - The key in the collection on which to perform group by.
*3.aggregationKey - The key in the collection on which to perform aggregate function.
*
*
*Returns		  : JOSNArray, it contains the final result 
 * @throws JSONException 
*************************************************************************/

public JSONArray groupByAverageJSON(String collectionName,String groupByKey,String aggregationKey) throws JSONException
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o;
	
	//to store final result in json array
	JSONArray arr=new JSONArray();
			
	JSONObject jObj;
	
	List<?> list=coll.distinct(groupByKey);
	
	Iterator<?> cursor=list.iterator();
	
	DBCursor groupCursor;
	
	Object value;
	
	//System.out.println(groupByKey +" "+"Average");
	
	while(cursor.hasNext()){
	
		value=cursor.next();
		
		o=new BasicDBObject();
		
		o.put(groupByKey,value);
		
		groupCursor=coll.find(o);
		
	  	//System.out.println(value +" "+ average(groupCursor,aggregationKey));
		
		
		jObj=new JSONObject();
		
		// put the group by key value and find the sum and put the final sum in json object
		jObj.put(groupByKey,value);
		jObj.put("Average",average(groupCursor,aggregationKey));
		//add JSON object in json array
		arr.put(jObj);
	
	}
	return arr;
}

/*************************************************************************
*
*Method		  	  : orderBy
*
*Description	  : This method performs the order by operation on key in the single collection based 
*			  		on the sort order. 
*
*Input Parameters : 3
*1.collectionName - Name of collection to perform order by.
*2.key	  		  - The key in the collection on which to perform order by.
*3.sortOrder	  - sortOrder value can be either '1' for ascending order or
*					'-1' for descending order.
*
*Output		  	  : Displays the result on console.
*
*Returns		  : void. 
*************************************************************************/


public  void orderBy(String collectionName,String key,int sortOrder)
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o=new BasicDBObject(key,sortOrder);
	
	DBCursor cursor=coll.find().sort(o);
	
	while(cursor.hasNext())
	{
		System.out.println(cursor.next());
	}
	
}


/*************************************************************************
*
*Method		  	  : orderByJSON
*
*Description	  : This method performs the order by operation on key in the single collection based 
*			  		on the sort order. 
*
*Input Parameters : 3
*1.collectionName - Name of collection to perform order by.
*2.key	  		  - The key in the collection on which to perform order by.
*3.sortOrder	  - sortOrder value can be either '1' for ascending order or
*					'-1' for descending order.
*
*Returns		  : JOSNArray, it contains the final result 
*************************************************************************/


public  JSONArray orderByJSON(String collectionName,String key,int sortOrder)
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o=new BasicDBObject(key,sortOrder);
	
	//to store final result in json array
	JSONArray arr=new JSONArray();
				
	JSONObject jObj;
	
	DBCursor cursor=coll.find().sort(o);
	
	while(cursor.hasNext())
	{
		//System.out.println(cursor.next());
		jObj=new JSONObject();
		((BSONObject) jObj).putAll(cursor.next().toMap());
		arr.put(jObj);
	}
	
	return arr;
}

/*************************************************************************
*
*Method		  		: join
*
*Description        : This method performs the inner join operation on two collections based 
*			  		  on the key specified by onKey 
*
*Input Parameters   : 3
*1.firstCollection	- Name of parent collection name
*2.secondCollection - Name of child collection name
*3.firstCollKey		- The key in the first collections on which to perform join operation.
*4.secondCollKey	- The key in the second collections on which to perform join operation.
*
*Output		  		: Displays the result on console.
*
*Returns		    : void.
*
*Example		    :{join("Student","Marks","RollNo","RollNo");}
*************************************************************************/

public void join(String firstCollection,String secondCollection,String firstCollKey,String secondCollKey) throws Exception
{
	DBCollection fc=db.getCollection(firstCollection);
	DBCollection sc=db.getCollection(secondCollection);
	
	DBCursor c1=fc.find();
	DBCursor c2=sc.find();
	
    List<DBObject> list=c2.toArray();
       
    Object obj[]=list.toArray();
       
	DBObject o1,o2;
		
	Object value1,value2;
	
       int i;
       
	while(c1.hasNext())
	{
		o1=c1.next();
		value1=o1.get(firstCollKey);
		
		i=0;
		while(i< obj.length)
		{
			o2=(DBObject)obj[i];
			value2=o2.get(secondCollKey);
			
			if(value1.equals(value2))
			 System.out.println(o1 +" "+o2);
           i++;  
		}
	}
}

/*************************************************************************
*
*Method		  		: joinJSON
*
*Description        : This method performs the inner join operation on two collections based 
*			  		  on the key specified by onKey 
*
*Input Parameters   : 3
*1.firstCollection	- Name of parent collection name
*2.secondCollection - Name of child collection name
3.firstCollKey		- The key in the first collections on which to perform join operation.
*4.secondCollKey	- The key in the second collections on which to perform join operation.
*
*
*Returns		    : JSONArray which contains the result
*
*Example		    :{join("Student","Marks","RollNo","RollNo");}
*************************************************************************/

public JSONArray joinJSON(String firstCollection,String secondCollection,String firstCollKey,String secondCollKey) throws Exception
{
	DBCollection fc=db.getCollection(firstCollection);
	DBCollection sc=db.getCollection(secondCollection);
	
	DBCursor c1=fc.find();
	
	DBCursor c2=sc.find();
	
	//store the second collection documents in a list
    List<DBObject> list=c2.toArray();
       
    Object obj[]=list.toArray();
       
	DBObject o1,o2;
	
	//to store final result in json array
	JSONArray arr=new JSONArray();
					
	JSONObject jObj;
	
	Object value1,value2;
	
       int i;
       
     // for each document in a first collection iterate over second collection  
	while(c1.hasNext())
	{
		// get document from first collection
		o1=c1.next();
		
		//get value from the document on which to perform join operation
		value1=o1.get(firstCollKey);
		
		i=0;
		//iterate for each document in second collection
		while(i < obj.length)
		{
			o2=(DBObject)obj[i];
			value2=o2.get(secondCollKey);
			
			if(value1.equals(value2))
			{ 
				//System.out.println(o1 +" "+o2);
				
				//merge the result in jObj
				o1.putAll(o2.toMap());

				jObj=new JSONObject(o1.toMap());
				
				//add json Object in JSON array
				arr.put(jObj);
			}
			i++;
		}
	}
	
	return arr;
}

/*************************************************************************
*
*Method		  	  : findIn($in)
*
*Description	  : This method searches the documents in single collection based on key
*					that satisfies the set of values specified in the list.
*			  		
*
*Input Parameters : 3
*1.collectionName	- Name of the collection
*2.key				- the key on which to perform "$in" operation
*3.list			    - Set of values.
* 
*Output		  	  : Displays the result on console.
*
*Returns		  : void. 
*
*Example		  :{ List<Integer> list=new ArrayList<Integer>();
					list.add(1);
					list.add(2);
					list.add(10);
					findIn("temp1","i",list); }
*************************************************************************/


public void findInConsole(String collectionName,String key,List<?> list) throws Exception
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o=new BasicDBObject();
	o.put(key,new BasicDBObject("$in",list));
	
	DBCursor cursor=coll.find(o);
	
	while(cursor.hasNext())
	{
		System.out.println(cursor.next());
	}
	
}


public DBCursor findIn(String collectionName,String key,List<?> list) throws Exception
{
	DBCollection coll=db.getCollection(collectionName);
	
	BasicDBObject o=new BasicDBObject();
	o.put(key,new BasicDBObject("$in",list));
	
	DBCursor cursor=coll.find(o);
	
	return cursor;
	
}



//most useful
public List<DBObject> findIn(DBCursor cursor,String key,List<?> value)
{
	
	List<DBObject> list = new ArrayList<DBObject>();
	
	DBObject obj;
	
	Iterator<?> i;
	
	Object v1,v2;
	
	while(cursor.hasNext())
	{
		obj = cursor.next();
		i = value.iterator();
		v1 = obj.get(key);
		
		
		while(i.hasNext())
		{
			v2 = i.next();
			
			
			if( v1 instanceof String && v2 instanceof String)
			{
				String value1 = (String) v1;
				String value2 = (String) v2;
				
			 if(value1.compareTo(value2)==0)
				list.add(obj);
			}
			else if(v1 instanceof Integer && v2 instanceof Integer)
			{
				Integer value1 = (Integer)  v1;
				Integer value2= (Integer) v2;
				
				if(value1.equals(value2))
				list.add(obj);
				
			}
			else if(v1 instanceof Long && v2 instanceof Long)
			{
				Long value1 = (Long)  v1;
				Long value2= (Long) v2;
				
				if(value1.equals(value2))
				list.add(obj);
				
			}
			else if(v1 instanceof Double && v2 instanceof Double)
			{
				
				Double value1 = (Double) v1;
				Double value2 = (Double) v2;
				
				if(value1.equals(value2))
					list.add(obj);
			}
		
		
		}
	}
	
	return list;
}

//most useful
public List<DBObject> findInList(List<DBObject> c,String key,List<?> value)
{
	
	List<DBObject> list = new ArrayList<DBObject>();
	
	DBObject obj;
	
	Iterator<DBObject> cursor = c.iterator();
	Iterator<?> i;
	
	Object v1,v2;
	
	while(cursor.hasNext())
	{
		obj = cursor.next();
		i = value.iterator();
		
		v1 = obj.get(key);
		
		while(i.hasNext())
		{
			v2= i.next();
			
			
			if( v1 instanceof String && v2 instanceof String)
			{
				String value1 = (String) v1;
				String value2 = (String) v2;
				
			 if(value1.compareTo(value2)==0)
			 {
				list.add(obj);
			 }
			}
			else if(v1 instanceof Integer && v2 instanceof Integer)
			{
				Integer value1 = (Integer)  v1;
				Integer value2= (Integer) v2;
				
				if(value1.equals(value2))
				list.add(obj);
				
			}
			else if(v1 instanceof Long && v2 instanceof Long)
			{
				Long value1 = (Long)  v1;
				Long value2= (Long) v2;
				
				if(value1.equals(value2))
				list.add(obj);
				
			}
			else if(v1 instanceof Double && v2 instanceof Double)
			{
				
				Double value1 = (Double) v1;
				Double value2 = (Double) v2;
				
				if(value1.equals(value2))
					list.add(obj);
			}
			
		}
	}
	
	return list;
}



/*************************************************************************
*@overloaded
*Method		  	  : search
*
*Description	  : This method searches the documents in a collection which satisfy the condition criteria
*				 
*Input Parameters : 2
*1.collectionName - Name of collection to perform aggregation.
*2.condition	  - The search condition criteria
*
*Output			  : Prints the output on console
*
*Returns		  : void.
*
*For example	  : To represent the query "db.CollName.find({"keyName":{"$gt":40,"$lt":50}})"
*
*					will be : search(collName,new BasicDBObject("keyName",new BasicDBObject("$gt",40).append("$lt",50)))
*
*************************************************************************/

public void search(String collectionName,DBObject condition)
{
	DBCollection coll=db.getCollection(collectionName);
	DBCursor cursor=coll.find(condition);
	
	while(cursor.hasNext())
	{
		System.out.println(cursor.next());
	}
	
}


/*************************************************************************
*@throws JSONException 
 * @overloaded
*Method		  	  : searchJSON
*
*Description	  : This method searches the documents in a collection which satisfy the condition criteria
*				 
*Input Parameters : 2
*1.collectionName - Name of collection to perform aggregation.
*2.condition	  - The search condition criteria
*
*
*Returns		  : JSONArray which will contain a final result
*
*For example	  : To represent the query "db.CollName.find({"keyName":{"$gt":40,"$lt":50}})"
*
*					will be : search(collName,new BasicDBObject("keyName",new BasicDBObject("$gt",40).append("$lt",50)))
*
*************************************************************************/

public JSONArray searchJSON(String collectionName,DBObject condition) throws JSONException
{
	DBCollection coll=db.getCollection(collectionName);
	DBCursor cursor=coll.find(condition);
	
	JSONArray arr=new JSONArray();
	
	JSONObject jObj;
	DBObject o;
	
	while(cursor.hasNext())
	{
		
		o = cursor.next();
		jObj=new JSONObject(o.toMap());
		arr.put(jObj);
	}
	
	return arr;
}


public JSONArray getValues(String collectionName,String key) throws JSONException
{
	JSONArray arr=new JSONArray();
	
	JSONObject jObj;
	
	DBCollection coll=db.getCollection(collectionName);
	
	List<?> list=coll.distinct(key);
	
	
	Iterator<?> cursor= list.iterator();
	
	while(cursor.hasNext())
	{
		
		jObj=new JSONObject();
		jObj.put(key,cursor.next());
		arr.put(jObj);
	}
	
	return arr;
}




public long getValue(String collectionName, String key)
{
	long result = 0;
	
	DBCollection coll = db.getCollection(collectionName);
	
	BasicDBObject cond = new BasicDBObject();
	
	BasicDBObject keyObj = new BasicDBObject(); 
	
	keyObj.put(key,1);
	
	DBCursor cursor = coll.find(cond,keyObj);

	DBObject valueObj = (DBObject) cursor.next();
	
	int first = key.indexOf('.');
	
	DBObject temp = (DBObject) valueObj.get(key.substring(0,first));
	
	Object r = temp.get(key.substring(first+1,key.length()));
	
	if(r!=null)
		result = (long) r;
	else
		result = 0;
	
	return result;
}


}

/****************************************************************************
*End of Process.java
***************************************************************************/
