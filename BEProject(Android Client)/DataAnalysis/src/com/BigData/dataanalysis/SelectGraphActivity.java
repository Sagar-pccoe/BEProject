package com.BigData.dataanalysis;

/*
 * Provides a list of graphs from which user selects the appropriate graph
 */

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;

public class SelectGraphActivity extends Activity {

    ListView lv;
    Context context;

    ArrayList prgmName;
    public static int [] prgmImages={R.drawable.bar,R.drawable.pie,R.drawable.line};
    public static String [] prgmNameList={"Bar Graph","Pie Chart","Line Chart"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_graph);
        
        Intent i=getIntent();
        
        context=this;

       lv=(ListView) findViewById(R.id.listView);
       lv.setAdapter(new GraphAdapter(this, prgmNameList,prgmImages));	//Displays the list with name and image and sends the response selected by user

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_graph, menu);
        return true;
    }

}
