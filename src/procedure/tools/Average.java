package procedure.tools;


public class Average {

	public double average;
	public double sum;
	public long observation;

    public Average() {
    	average=0;
    	sum=0;
    	observation=0;
    }
    
    public String toString()
    {
    	return "[ Average:" + average + " Sum:" + sum +" Observation:"+observation+ " ]";
    }
    
}