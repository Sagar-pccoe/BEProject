package manager;


public class DataRow {

	int countryId,productId;
	
	String date,country,region,state,city,productName,category,subCategory,brand;
	
	int min,max,quantity;
	
	double basePrice,mrp,sp,discount,profit;
	
	String classLabel;
	
	String month;
	
	int year;
	
	public DataRow(){}
	
	public DataRow(String date,int cid,String c,String r,String s,String city,int pid,String product,String category,String sc,String b,double bp,double mrp,double sp,double discount, int d,double p,int min,int max)
	{
		
		this.date = date;
		countryId = cid;
		country = clean(c);
		region = clean( r);
		state = clean(s);
		this.city = clean(city);
		
		productId = pid;
		productName = clean(product);
		this.category = clean(category);
		subCategory = clean(sc);
		brand = clean(b);
		
		this.discount = discount;
		
		this.min = min;
		this.max = max;
		this.quantity = d;
		
		basePrice = bp;
	    this.mrp = mrp;
	    this.sp = sp;
	    profit = p;
	    
	}
	
	
	String clean(String s)
	{
		int i = 0;
		
		byte chars[] = s.getBytes();
		
		char dest[]=new char[chars.length];
		
		char ch;
		
		while(i < chars.length)
		{
			ch = (char) chars[i];
			
			if(ch=='.' || ch=='\'' || ch==',' || ch=='!' )
				dest[i]='_';
			else
				dest[i] = ch;
			i++;
		}
		
		return new String(dest);
	}
	
	void setClassLabel(String label)
	{
		classLabel=label;
	}
	
	public String toString()
	{
		return date+" "+country+" "+region+" "+state+" "+city+" "+productName+" "+category+" "+subCategory+" "+brand+" "+basePrice+" "+mrp+" "+sp+" "+quantity+" "+min+" "+max+" "+classLabel;
	}
	
	
}
