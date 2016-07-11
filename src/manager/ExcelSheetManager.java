
package manager;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;


import javax.swing.JTextArea;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSheetManager implements Runnable{
 
    Connection con;
    MongoClient mc;
    DB db;
    DBCollection prodDim,timeDim,regDim,salesFact;
    BasicDBObject doc;
    
    Statement stmt;
    ResultSet rs;
    
    
    String days[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    String months[] = {
    		"January", "February", "March", "April",
    		"May", "June", "July", "August",
    		"September", "Octomber", "November", "December"};
    
    Calendar cal;
    
    SimpleDateFormat format;
    
    Thread t;
    
    JProgressBar bar;
    JTextArea area;

    File file =  null;
    FileInputStream fin = null;
    XSSFWorkbook workbook = null;
    
    int loadType;
    
    
    public ExcelSheetManager(String sourceDriver,String url,String destHost,String dbName) throws Exception
    {
    	Class.forName(sourceDriver);
    	con=DriverManager.getConnection(url); 
    	mc=new MongoClient(destHost,27017);
    	db=mc.getDB(dbName);
    	
    	prodDim=db.getCollection("Product_Dimension");
    	timeDim=db.getCollection("Time_Dimension");
    	regDim=db.getCollection("Region_Dimension");
    	salesFact=db.getCollection("Sales_Fact");
    	
    	cal=Calendar.getInstance();
    	format=new SimpleDateFormat("d-M-y");
    	
    	loadType = 0; //using jdbc-odbc bridge
    	
    	t = new Thread(this);
    }
    
    public ExcelSheetManager(String file,String destHost,String dbName) throws Exception
    {
    	this.file = new File(file);
    	mc=new MongoClient(destHost,27017);
    	db=mc.getDB(dbName);
    	
    	prodDim=db.getCollection("Product_Dimension");
    	timeDim=db.getCollection("Time_Dimension");
    	regDim=db.getCollection("Region_Dimension");
    	salesFact=db.getCollection("Sales_Fact");
    	
    	cal=Calendar.getInstance();
    	format=new SimpleDateFormat("d-M-y");
    	
    	loadType = 1; //using apache poi framework
    	
    	t = new Thread(this);
    }
    
    public void run() {

    	if(loadType == 1)
    		process2();
    	else
    		process1();
	}
    
    
    void process1()
    {
    	try
    	{
    	
    	Statement stmt=con.createStatement();
        
        ResultSet rs = stmt.executeQuery("select count(*) from [Sheet1$]");
    	rs.next();
       
    	int size = rs.getInt(1);
        
    	area.append("\nTotal Records:"+size);
        
        bar.setMaximum(size);
        bar.setMinimum(0);
        
       stmt = con.createStatement();
       
       rs = stmt.executeQuery("select * from [Sheet1$]");
       
       area.append("\nExtracting Data into memory. Please wait...");
        
        int count=0;
        String classLabels[]={"Poor","Average","Good"};
        DataRow row;
        
        Fuzzy f=new Fuzzy();
        
        LiveNBTraining lbt=new LiveNBTraining();
        
        // Date	Country_id	Country	Region	State	City	Product_id	Product_Name	Category	Sub_Category	Brand
        // Base_Price	MRP	SP	Discount	Quantity_Sold	Profit	Min	Max

        
        area.append("\nExtraction Successful.\nCleaning and Loading the data. Please wait...");
        
        while(rs.next() && count < size)
        {
        	
        	row=new DataRow(rs.getString(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)
        			
        			, rs.getDouble(12),rs.getDouble(13),rs.getDouble(14),rs.getDouble(15),rs.getInt(16),rs.getDouble(17),rs.getInt(18),rs.getInt(19));
        			
        
        	row.setClassLabel(classLabels[f.fuzzyify(row.min, row.max, row.quantity)]);
        	
        
        	//System.out.println(row);
        	
        	transformAndLoad(row);
        	
        	lbt.train(row);
        
        	
        	count++;
        
        	bar.setValue(count);
        	
        	row=null;     
            
        }
        
        
        //System.out.println(lbt.map);
        
        area.append("\nThe Data has been successfully loaded. \nLoading Summary information. Please wait...");
        
        // Store Naive Bayesian values in Summary dimension table 
       
        DBCollection coll=db.getCollection("Summary");
        
        BasicDBObject doc=new BasicDBObject();
        doc.putAll(lbt.map);
        
        coll.insert(doc);
        area.append("\nSummary information has been loaded. \nGarbage Collecting the memory. Please wait...");
        
        rs.close();
        
        stmt.close();
        
        System.gc();
        area.append("\nAll Process Completed..!");
        
        close();
         	
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    
    void process2()
    {
    	try
    	{
    	
        
    	FileInputStream fin = new FileInputStream(file);
		
    	area.append("\nExtracting Data into memory. Please wait...");
    	
		XSSFWorkbook workbook = new XSSFWorkbook(fin); 
	
		XSSFSheet spreadsheet = workbook.getSheetAt(0); 
		
		area.append("\nExtraction Successful.\nCleaning and Loading the data. Please wait...");
		
		Iterator < Row > rowIterator = spreadsheet.iterator();
		
		XSSFRow row;
	
    	int size = spreadsheet.getLastRowNum();
    	
    	area.append("\nTotal Records:"+size);
    	
        bar.setMaximum(size);
        bar.setMinimum(0);
            
        int count=0;
        String classLabels[]={"Poor","Average","Good"};
        DataRow data;
        
        Fuzzy f=new Fuzzy();
        
        LiveNBTraining lbt=new LiveNBTraining();
       
              
        //skip column header
        rowIterator.next();
       
        
     // Date	Country_id	Country	Region	State	City	Product_id	Product_Name	Category	Sub_Category	Brand
        // Base_Price	MRP	SP	Discount	Quantity_Sold	Profit	Min	Max
        
        while (rowIterator.hasNext()) { 
			
			row = (XSSFRow) rowIterator.next(); 
		    
	
			data=new DataRow(row.getCell(0).getStringCellValue(),
			(int)row.getCell(1).getNumericCellValue(),
			row.getCell(2).getStringCellValue(),
			row.getCell(3).getStringCellValue(),
			row.getCell(4).getStringCellValue(),
			row.getCell(5).getStringCellValue(),
			(int)row.getCell(6).getNumericCellValue(),
			row.getCell(7).getStringCellValue(),
			row.getCell(8).getStringCellValue(),
			row.getCell(9).getStringCellValue(),
			row.getCell(10).getStringCellValue(),
			row.getCell(11).getNumericCellValue(),
			row.getCell(12).getNumericCellValue(),
			row.getCell(13).getNumericCellValue(),
			row.getCell(14).getNumericCellValue(),
			(int)row.getCell(15).getNumericCellValue(),
			row.getCell(16).getNumericCellValue(),
			(int)row.getCell(17).getNumericCellValue(),
			(int)row.getCell(18).getNumericCellValue());
        			
        
			//determine class label using fuzzy logic
			
			data.setClassLabel(classLabels[f.fuzzyify(data.min, data.max, data.quantity)]);
        
        
        	//System.out.println(row);
        	
        	transformAndLoad(data);
        	
        	lbt.train(data);
       
        	count++;
        
        	bar.setValue(count);
        	
        	row=null;
			data = null;
			
			count++;
			}
			
		
        
        //System.out.println(lbt.map);
        
        area.append("\nThe Data has been successfully loaded. \nLoading Summary information. Please wait...");
        
        // Store Naive Bayesian values in Summary dimension table 
       
        DBCollection coll=db.getCollection("Summary");
        
        BasicDBObject doc=new BasicDBObject();
        doc.putAll(lbt.map);
        
        coll.insert(doc);
        area.append("\nSummary information has been loaded. \nGarbage Collecting the memory. Please wait...");
        
        workbook.close();
        
        System.gc();
        area.append("\nAll Process Completed..!");
        
        mc.close();
        
         	
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null,e.getMessage(),"Mismatch Error. Cannot load data.",JOptionPane.ERROR_MESSAGE);
    	}
    }

    void close()
    {
    	try
    	{
    		mc.close();
    		con.close();
    		
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally{
    		mc=null;
    		con=null;
    		
    	}
    }
    
    
    public void extract(JProgressBar bar,JTextArea area) throws Exception
    {	
    	this.bar = bar;
    	this.area = area;
    	t.start();
    }
    
    void extract() throws Exception
    {
        Statement stmt=con.createStatement();
        ResultSet rs= stmt.executeQuery("select * from [Sheet1$]");
        
        String classLabels[]={"Poor","Average","Good"};
        DataRow row;
        
        Fuzzy f=new Fuzzy();
        
        LiveNBTraining lbt=new LiveNBTraining();
        
        // Date	Country_id	Country	Region	State	City	Product_id	Product_Name	Category	Sub_Category	Brand
        // Base_Price	MRP	SP	Discount	Quantity_Sold	Profit	Min	Max

        
        while(rs.next())
        {
        	
        	row=new DataRow(rs.getString(1),rs.getInt(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getInt(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11)
        			
        			, rs.getDouble(12),rs.getDouble(13),rs.getDouble(14),rs.getDouble(15),rs.getInt(16),rs.getDouble(17),rs.getInt(18),rs.getInt(19));
        			
        
        	row.setClassLabel(classLabels[f.fuzzyify(row.min, row.max, row.quantity)]);
        	
        
        	//System.out.println(row);
        	
        	transformAndLoad(row);
        	
        	lbt.train(row);
        	
        	row=null;
         
        }
        
       // System.out.println(lbt.map);
        
        
        // Store Naive Bayesian training values in Summary dimension table 
       
        DBCollection coll=db.getCollection("Summary");
        
        BasicDBObject doc=new BasicDBObject();
        doc.putAll(lbt.map);
        
        coll.insert(doc);
        
        rs.close();
        stmt.close();
        
        close();
         
    }
    
    void transformAndLoad(DataRow row) throws ParseException
    {
    	// To insert a document in Product Dimension
    	
    	BasicDBObject doc=new BasicDBObject();
    	doc.put("_id",row.productId);
    	doc.put("Product_Name",row.productName);
    	doc.put("Category",row.category);
    	doc.put("Sub_Category", row.subCategory);
    	doc.put("Brand",row.brand);
    	doc.put("Base_Price", row.basePrice);
    	doc.put("MRP", row.mrp);
    	doc.put("Selling_Price",row.sp);
    	doc.put("Discount",row.discount);
    	prodDim.save(doc);
    	
    	// To insert a document in Region Dimension
    	doc=new BasicDBObject();
    	doc.put("_id",row.countryId);
    	doc.put("City", row.city);
    	doc.put("State",row.state);
    	doc.put("Region", row.region);
    	doc.put("Country",row.country);
    	
    	regDim.save(doc);
    	
    	//To insert a document in Time Dimension
    	Date d;
    	String dateTemp;
    	
    	try {
    		
    		int temp;
    		
			 d=format.parse(row.date);
		
	    	cal.setTime(d);
			
	    	doc=new BasicDBObject();
	    	
	    	//doc.put("_id",""+ d);
	    	
	    	doc.put("Day_of_Month",cal.get(Calendar.DAY_OF_MONTH));
	    	
	    	doc.put("Day_of_Week",cal.get( Calendar.DAY_OF_WEEK));
	    	
	        doc.put("Day",days[cal.get(Calendar.DAY_OF_WEEK)-1]);
	        
	        temp=cal.get(Calendar.MONTH);
	        
	        row.month=months[temp];
	        
	        row.year=cal.get(Calendar.YEAR);
	        
	    	doc.put("Month", row.month);
	    	
	    	//int monthNo=temp+1;
	    	
	    	doc.put("Month_of_Year",++temp);
	    	doc.put("Quarter",findQuarter(temp));
	    	doc.put("Year",row.year);
	    	
	    	 dateTemp = cal.get(Calendar.DAY_OF_MONTH) +"/"+ temp + "/" + row.year;
	    			
	    	doc.put("_id", dateTemp);
	    	doc.put("Date", dateTemp);
	    	
	    	timeDim.save(doc);
	    	
		} catch (ParseException e) {
			
			throw e;
		}
    	
    	// to insert document into SalesFact
    	
    	doc=new BasicDBObject();
    	doc.put("Product_id",row.productId);
    	doc.put("Country_id",row.countryId);
    	doc.put("Time_id",dateTemp);
    	doc.put("Quantity_Sales",row.quantity);
    	doc.put("Amount_Sales", row.quantity * row.sp);
    	doc.put("Sales_Performance",row.classLabel);
    	
    	salesFact.insert(doc);
    }
    
    int findQuarter(int Month_No){
        if(Month_No==1 || Month_No==2 || Month_No==3){
            return 1;
        }
        if(Month_No==4 || Month_No==5 || Month_No==6){
            return 2;
        }
        if(Month_No==7 || Month_No==8 || Month_No==9){
            return 3;
        }
        if(Month_No==10 || Month_No==11 || Month_No==12){
            return 4;
        }
        return -1;
    }
    
	public static void main(String[] args) throws Exception{
		
		ExcelSheetManager e=new ExcelSheetManager("sun.jdbc.odbc.JdbcOdbc","jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ=F:/BE Project/Data Set/Excel Files Data Set/temp.xlsx;","localhost","SalesDataWarehouse");
		e.extract();
	}


	
	

}

//java.sql.DriverManager.getConnection( "jdbc:odbc:Driver={Microsoft Excel Driver(*.xls)};DBQ=F:/BE Project/Data Set/Excel Files Data Set/Jan2013.xls");
