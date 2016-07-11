package com.BigData.dataanalysis;

/*
 * This class is used to display Aggregation or Analysis framework list to user
 */

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;

public class SelectOperation extends Activity {

    ListView lv;
    Context context;

    ArrayList prgmName;
    public static int [] prgmImages={R.drawable.anaicon,R.drawable.aggicon};
    public static String [] prgmNameList={"Statistic and Aggregation Framework","Analysis Framework"};	//Show services available
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_operation);
        
        Intent i=getIntent();
        
        context=this;

       lv=(ListView) findViewById(R.id.listView);
       lv.setAdapter(new OperationAdapter(this,prgmNameList,prgmImages));	//Send the response selected by user 

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.operation_adapter, menu);
        return true;
    }

}
