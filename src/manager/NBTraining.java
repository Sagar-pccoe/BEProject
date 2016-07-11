package manager;

/**
 * @(#)NBTraining.java
 *
 *
 * @author Sagar Rathod
 * @version 1.00 2014/12/26
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

public class NBTraining {
    
    
    MongoClient mc;
    DB db;
    DBCollection coll;
    
    HashMap<String,HashMap<Object,Long>> map;
    
    
    public NBTraining(String hostName,String dbName,String coll) throws Exception
    	 {
    	
    	mc=new MongoClient(hostName,27017);
    	db=mc.getDB(dbName);
    	this.coll=db.getCollection(coll);
    	map=new HashMap<String,HashMap<Object,Long>>();
    	
       
    	 }

// Use this method when to perform group by operation on sinlge coloumn
    
    public HashMap<Object,Long> groupByCount(String groupByKey)
    {
	
	BasicDBObject o;
	
	List<?> distinct = coll.distinct(groupByKey);
	
	List<?> list = distinct;
	
	Iterator<?> cursor=list.iterator();
	
	Object value;
	
	//System.out.println(groupByKey +" "+"Count");
	
	HashMap<Object,Long> counts=new HashMap<Object,Long>();
	
	Long count;
	
	while(cursor.hasNext()){
	
		value=cursor.next();
		
		o=new BasicDBObject();
		
		o.put(groupByKey,value);
		
		count=new Long(coll.count(o));
		
		counts.put(value, count);
		
	  	//System.out.println(value +" "+ count);	
	}
	
	return counts;
}

// Use this method when to perform group by operation on multiple coloumns


	void train(String attr[])
	{
		int i=0;
		
		HashMap<Object,Long> attrValues;
        
		//perform groupBy on each attribute
		while(i<attr.length)
		{	
			attrValues=groupByCount(attr[i]);
			map.put(attr[i],attrValues);
			i++;
		}
		
		//obtain class values
		HashMap<Object,Long> classValues= map.get("class");
		i=0;
		
		Set<Object> classValueSet= classValues.keySet();
		Object classArray[]=classValueSet.toArray();
		Set<Object> attrValueSet;
		
		BasicDBObject cond;
		
		Long count;
		Object attrArray[];
		
		
		int j,k;
		
		//perform group by with each combination of class value and attribute value
		
		while(i<attr.length-1)
		{
			attrValues=map.get(attr[i]);
			attrValueSet=attrValues.keySet();
			attrArray=attrValueSet.toArray();
	      
			//System.out.println(attrValues);
			j=0;
			
			while(j<attrArray.length)
			{    
				
				cond=new BasicDBObject();
			    cond.put(attr[i],attrArray[j]);
			    
			    k=0;
			    while(k < classArray.length)
			    {
			    	
			    	cond.put("class",classArray[k]);
			    	
			    	//perform count()
			    	
			    	count=new Long(coll.count(cond));
			    	
			    	map.get(attr[i]).put(attrArray[j].toString()+"_"+classArray[k].toString(), count);
			    	
			    	k++;
			    }
		     j++;
			}
			i++;
		}
	    
		
	}


       public static void main(String[] args) throws Exception
       	{
          long start=System.currentTimeMillis();
    	  String attr[]={"age","income","student","credit","class"};
          NBTraining db=new NBTraining("localhost","NBTest","sid");
          db.train(attr);
          long end=System.currentTimeMillis();
          System.out.println("Completed in "+(end-start)+" msec");
         
          
          System.out.println(db.map);
          
          DBCollection coll = db.db.getCollection("Summary");
          
          DBObject obj=new BasicDBObject();
          obj.putAll(db.map);
         
          coll.insert(obj);
          
        System.out.println(obj);
       }
}
