package com.BigData.dataanalysis;

/*
 * This class deals with representation of List of operations and handles the response selected by the user
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
public class OperationAdapter extends BaseAdapter{   
    String [] result;
    Context context;
 int [] imageId;
      private static LayoutInflater inflater=null;
    public OperationAdapter(SelectOperation selectoperation, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=selectoperation;
        imageId=prgmImages;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    
    //This function is used to handle the response selected by the user
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
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
                
            	if(result[position].equals("Statistic and Aggregation Framework")){
            		
            		Intent intent=new Intent(context,AggregateActivity.class);	//Direct user to aggregation framework
            		context.startActivity(intent);
            		
            	}
            	if(result[position].equals("Analysis Framework")){
            		
            		Intent intent=new Intent(context,AnalysisActivity.class);	//Direct user to analysis framework
            		context.startActivity(intent);
            		
            	}
            }
        });   
        return rowView;
    }
}
