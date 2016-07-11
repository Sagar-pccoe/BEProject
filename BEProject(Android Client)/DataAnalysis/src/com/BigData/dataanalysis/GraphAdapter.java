package com.BigData.dataanalysis;

/*
 * This class deals with the response selected by user related to which graph needs to be constructed
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class GraphAdapter extends BaseAdapter{   
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    
    //Obtains the image and list value selected by user
    public GraphAdapter(SelectGraphActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        
        return position;
    }

    @Override
    public long getItemId(int position) {
        
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    
    /*
     * Handles the response selected by the user
     * 
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.program_list, null);
             holder.tv=(TextView) rowView.findViewById(R.id.textView1);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);       
         holder.tv.setText(result[position]);
         holder.img.setImageResource(imageId[position]);         
         rowView.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                //Direct user to Bar Graph interface
            	if(result[position].equals("Bar Graph")){
            		Toast.makeText(context,"Building Graph Please Wait", Toast.LENGTH_LONG).show();
            		Intent intent=new Intent(context,BarGraphActivity.class);
            		context.startActivity(intent);
            	}
            	
            	//Direct user to Pie Chart interface
            	if(result[position].equals("Pie Chart")){
            		Toast.makeText(context,"Building Graph Please Wait", Toast.LENGTH_LONG).show();
            		Intent intent=new Intent(context,PieChartActivity.class);
            		context.startActivity(intent);
            	}
            	
            	//Direct user to Line Chart interface
            	if(result[position].equals("Line Chart")){
            		Toast.makeText(context,"Building Graph Please Wait", Toast.LENGTH_LONG).show();
            		Intent intent=new Intent(context,LineChartActivity.class);
            		context.startActivity(intent);
            	}
            }
        });   
        return rowView;
    }

}