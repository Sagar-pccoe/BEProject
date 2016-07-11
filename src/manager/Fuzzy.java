package manager;
import fuzzy.mf.TriangularMembershipFunction;

public class Fuzzy {

	 final int noOfMemberFunction=3;
	 
	 int fuzzyify(int min,int max,double value)
	 {
		int increment=(max-min+1) / (noOfMemberFunction-1);
		
		if(value > max)
		 return noOfMemberFunction-1;
		
		if(value < min)
			return 0;
		
		double a,b,c;
	
		double member[]=new double[noOfMemberFunction];
		
		TriangularMembershipFunction mf;
		int j=0,maxMembership;
		
		//first level
		a = min - 1;
		b = min;
		c = min + increment;
		
		mf=new TriangularMembershipFunction(a, b, c);
		
		member[j] = mf.evaluate(value);
		
		
		//middle levels
		a = min;
		b = a + increment;
		c = b + increment;
		
		maxMembership=j;
		
		mf = new TriangularMembershipFunction(a,b,c);
		
		for(j=1;j < noOfMemberFunction-1;j++)
		{
			
			member[j] = mf.evaluate(value);
			
			if(member[j] > member[maxMembership])
			{
				maxMembership=j;
			}
		
		}
		
		//last level
		c = max + 1;
		b=max;
		a=max-increment;
		
		mf=new TriangularMembershipFunction(a, b, c);
		
		member[j]=mf.evaluate(value);
		
		if(member[maxMembership] < member[j])
		{
			maxMembership = j;
		}
		
	//	System.out.println("Min Max Increment");
	//	System.out.println(min+" "+max+ " "+increment );
	
		//display(member);
		
		return maxMembership;
	 }
	 
	 void display(double a[])
	 {
		 System.out.println("Poor Average Good");
		 for(int i=0;i< a.length;i++)
		 {
			 System.out.print(" "+a[i]);
			 
		 }
		 System.out.println("");
	 }
	 
}
