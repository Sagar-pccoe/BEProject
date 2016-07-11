package com.BigData.dataanalysis;
/* 
 * This class is used to login into the application
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONArray;

import org.json.JSONObject;

import tools.Query;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

   private EditText  username=null;		//takes the username from edit text
   private EditText  password=null;	    //takes the password from edit text	
   private EditText hostServer=null;
   private TextView attempts;			//maintains and shows number of login attempts available
   private Button login;				//used to initiate login process
   int counter = 3;						//counter for number of login attempts

  static ClientThread client;			//instance of ClientThread
  Query query;							//instance of Query class
  boolean status;						//maintains the boolean value when login details are checked 
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);
      username = (EditText)findViewById(R.id.editText1);
      password = (EditText)findViewById(R.id.editText2);
      hostServer = (EditText) findViewById(R.id.ipAddress);
      
      attempts = (TextView)findViewById(R.id.textView5);
      attempts.setText(Integer.toString(counter));
      login = (Button)findViewById(R.id.button1);
      
      query = new Query();
     
  }

   /*
    * Code to check credentials of user
    */
   
   public void login(View view){
	 
	   	   String un;	//maintains the username in string format
		   String pw;	//maintains the password in string format
		   String host;
		   
		   host = hostServer.getText().toString();
		   
	    client = new ClientThread();			
		client.setServer(host);
	    
	    Thread t = new Thread(client);			//connects application to server
	    t.start();
	    
	    try
	    {
	    	//wait till clients connect to server
	    	t.join();
	    	
	    	if(client.isConnected)
	 	   {
	 		   	 		    		   
	 		   //get username and password 
	 		   un = username.getText().toString();		
	 		   pw = password.getText().toString();
	 		   
	 		   
	 		   JSONArray arr = new JSONArray();
	 		   
	 		   JSONObject obj = new JSONObject();
	 		   
	 		   try {
	 			   
	 				   		obj.put("Query", "Login");
	 				   		arr.put(obj);
	 		 
	 				   		
	 					   if(un != null && pw != null)		//Check if username and password fields are null
	 					   {
	 						   
	 						   obj = new JSONObject();
	 						   
	 						   obj.put("Username", un);		//put username in JSONObject
	 						   obj.put("Password",pw);		//put password in JSONObject
	 						   
	 						   arr.put(obj);				//insert object in JSONArray
	 						
	 						   query = new Query(arr);
	 						   query.writeObject(client.objout);	//Send JSONArray to Server
	 					
	 						   new Recieve().execute();			//wait for server to send response and initiate necessary changes
	 						   
	 						}	
	 		   
	 		   }catch(Exception e)
	 		   {
	 			   e.printStackTrace();
	 		   }
	       
	    }else
	    {
	  	  Toast.makeText(getApplicationContext(), "Connection to Server failed.", Toast.LENGTH_SHORT).show();
	    }

	    	
	    }catch(Exception e)
	    {
	    	
	    }
	    
	      
}
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.login, menu);
      return true;
   }
   
   /*
    * Code to connect client application to server
    */
   
   class ClientThread implements Runnable {

	   String server;
		Socket socket;
		ObjectInputStream objin;
		ObjectOutputStream objout;
        boolean isConnected = false;
		
        void setServer(String server)
        {	
        	this.server = server;
        	
        }
        
		@Override
		public void run() {

			try {

				socket = new Socket(server,1234);		//Connect to given IPAddress and port number
				
				
				objout = new ObjectOutputStream(socket.getOutputStream());	//Initialize object outputstream
				objin = new ObjectInputStream(socket.getInputStream());		//Initialize object inputstream
				
				
				isConnected = true;		//is set if connection is successful
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		
   }
   /*
    * Code to verify user details and direct valid user to the next page
    */
   
	private class Recieve extends AsyncTask<Void,Void,Void>{
		
		
		//Que user to wait while details are being verified
		protected void onPreExecute(){
			Toast.makeText(getApplicationContext(), "Verifying credentials please wait...", 
					   Toast.LENGTH_SHORT).show();
   
		}
		
		//Checks the status sent by server
		@Override
		protected Void doInBackground(Void... arg0) {
			try
			{
				
				query = new Query();
					
				 query.readObject(client.objin);	//get response from server
		
				 JSONArray arr = query.getQuery();
				 
				 JSONObject obj = (JSONObject) arr.get(0);	//obtain JSONObject from JSONArray
				 
				 status = Boolean.parseBoolean((String)obj.get("Status"));	//status is set to true if details are right else false
				 
		 
				 
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}	
		

		protected void onPostExecute(Void args){	
			
			if(status){		//if status is true
				
				Intent i=new Intent(LoginActivity.this,SelectOperation.class);	//Direct user to new interface
				startActivity(i);
			  
			}
			else
			{
					Toast.makeText(getApplicationContext(), "Wrong Credentials",	//else inform about wrong credentials
						   Toast.LENGTH_SHORT).show();
				   attempts.setBackgroundColor(Color.RED);	
				   counter--;
				   attempts.setText(Integer.toString(counter));
				   if(counter==0){
					   login.setEnabled(false);
				   }
			}
		}
		
	}
   
}

