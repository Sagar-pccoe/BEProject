package app;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import app.tools.Client;
import tools.*;

public class QueryPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel label;
	JTextField text;
	
	JButton btnSubmit,btnClear;
	
	JComboBox<String> timeKey;
	JComboBox<Object> timeValue;
	
	JComboBox<String> productKey;
	JComboBox<Object> productValue;
	
	
	JComboBox<String> regionKey;
	JComboBox<Object> regionValue;
	
	JLabel salesKey;
	JComboBox<String> salesAttr;
	
	JPanel queryPanel,consolePanel;
	
	JScrollPane sc;
	JTextPane console;
	
	String dayFullName[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	Integer quarter[]={1,2,3,4};
	
	QueryPanel()
	{
		setLayout(new GridLayout(2,1));
		
		queryPanel=new JPanel(new GridLayout(6,2));
		consolePanel=new JPanel(new BorderLayout());
		
		timeKey=new JComboBox<String>();
		timeKey.addItem("<Select Time>");
		
		timeKey.addItem("Day_of_Month");
		timeKey.addItem("Day_of_Week");
		timeKey.addItem("Day");
		timeKey.addItem("Month");
		timeKey.addItem("Month_of_Year");
		timeKey.addItem("Quarter");
		timeKey.addItem("Year");
		
		queryPanel.add(timeKey);
		
		timeValue=new JComboBox<Object>();
		timeValue.addItem("None");
		queryPanel.add(timeValue);
		
		
		productKey=new JComboBox<String>();
		productKey.addItem("<Select Product>");
		
		productKey.addItem("Product_Name");
		productKey.addItem("Category");
		productKey.addItem("Sub_Category");
		productKey.addItem("Brand");
		
		
		queryPanel.add(productKey);
		
		productValue=new JComboBox<Object>();
		productValue.addItem("None");
		queryPanel.add(productValue);
		
		
		regionKey=new JComboBox<String>();
		regionKey.addItem("<Select Region>");
		
		regionKey.addItem("City");
		regionKey.addItem("State");
		regionKey.addItem("Region");
		regionKey.addItem("Country");
				
		queryPanel.add(regionKey);
		
		regionValue=new JComboBox<Object>();
		regionValue.addItem("None");	
		queryPanel.add(regionValue);
		
		salesKey=new JLabel("Select Measure:");
		salesAttr=new JComboBox<String>();
		salesAttr.addItem("None");
		
		salesAttr.addItem("Quantity_Sales");
		salesAttr.addItem("Amount_Sales");
		
		queryPanel.add(salesKey);
		queryPanel.add(salesAttr);
		
		
		btnSubmit=new JButton("Submit");
		queryPanel.add(btnSubmit);
		
		btnClear=new JButton("Clear");
		queryPanel.add(btnClear);
		
		add(queryPanel);
		
		console=new JTextPane();
		console.setFont(new Font("Calbri",Font.PLAIN,14));
		
		sc=new JScrollPane(console);
		

		add(sc);
		
		
		
		timeKey.addActionListener(new ActionListener()
			{
				  public void actionPerformed(ActionEvent ae)
				  {
					  try
					  {
					   String key=(String)timeKey.getSelectedItem();
					  
						  if(!key.equals("<Select Time>"))
						  {
							  timeValue.removeAllItems();
							  
							  if(key.equals("Day_full_Name"))
							  {
								  showValues(timeValue,dayFullName);
							  }
							  else
								  if(key.equals("Quarter"))
								  {
									  showValues(timeValue,quarter);
								  }
								  else
								  {
								  JSONArray arr=getValues("Time_Dimension",key);
								  println(arr.toString());
								  showValues(timeValue,arr,key); 
								  }
						  }
						  else
							  {
							    timeValue.removeAllItems();
							    timeValue.addItem("None");
							  }
					  
					  }catch(Exception e)
					  {
						  println(e.getMessage());
					  }
				  }
				
			}
		
		);
		
		
		productKey.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try
				{
					String key=(String)productKey.getSelectedItem();
					
					if(!key.equals("<Select Product>"))
					{
						productValue.removeAllItems();
						
						JSONArray arr=getValues("Product_Dimension",key);
						println(arr.toString());
						showValues(productValue,arr,key); 
					}
					else
						{
						   productValue.removeAllItems();
						   productValue.addItem("None");
						}
					
				}catch(Exception e)
				{
					println(e.getMessage());
				}
			}
		}
				
			);
		
		
		
		
		
		regionKey.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try
				{
					String key=(String)regionKey.getSelectedItem();
					
					if(!key.equals("<Select Region>"))
					{
						regionValue.removeAllItems();
						
						JSONArray arr=getValues("Region_Dimension",key);
						println(arr.toString());
						showValues(regionValue,arr,key); 
					}
					else
						{
						regionValue.removeAllItems();
						regionValue.addItem("None");
						}
					
				}catch(Exception e)
				{
					println(e.getMessage());
				}
			}
		}
				
			);
		
		btnSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				
				
				//All keys
				String tk=(String)timeKey.getSelectedItem();
				String pk=(String)productKey.getSelectedItem();
				String rk=(String)regionKey.getSelectedItem();
				
				
				
				//All Values
				
				Object tV=timeValue.getSelectedItem();
				Object pV=productValue.getSelectedItem();
				Object rV=regionValue.getSelectedItem();
				
				
				JSONArray arr=new JSONArray();
				
				JSONObject jObj=new JSONObject();
				
				try {
				jObj.put("Query", "Analysis");
				
				arr.put(jObj);
				
				jObj = new JSONObject();
				
				
				jObj.put(pk,pV);
				
				
				jObj.put(tk,tV);

				jObj.put(rk,rV);
				
				arr.put(jObj);
				
				println(arr.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				try
				{
				JSONArray result=solve(arr);
				println("Result:"+result.toString());
				
				}catch(Exception e)
				{
					println(e.getMessage());
				}
			}
			
		});
		
		btnClear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				timeKey.setSelectedIndex(0);
				timeValue.removeAllItems();
			    timeValue.addItem("None");
			    
			    productKey.setSelectedIndex(0);
				productValue.removeAllItems();
				productValue.addItem("None");
			    
				regionKey.setSelectedIndex(0);
				regionValue.removeAllItems();
				regionValue.addItem("None");

			    
			    salesAttr.setSelectedIndex(0);
			    
			   // console.setText("");
			}
			
		});
	}
	
	
		public JSONArray getValues(String dimension,String key) throws Exception
		{	
			  Client client=new Client("localhost",1234);
			  
			  println("Connected to QueryManager");
			  
			  JSONObject obj1=new JSONObject();
			  
			  obj1.put("Query", "Get");
			 
			  JSONObject obj2=new JSONObject();
			  
			  obj2.put("Dimension", dimension);
			  obj2.put("Key", key);
			  obj2.put("Value",-1);
							  
			  JSONArray arr=new JSONArray();
			  arr.put(obj1);
			  arr.put(obj2);
			  
			  Query q=new Query(arr);
			  
			  client.sendQuery(q);
			  
			  println("Query Sent:"+q.getQuery().toString());
			  
			  Query result=client.receive();
			
			 return result.getQuery();
		}
	
		
		public void showValues(JComboBox<Object> cbm,JSONArray arr,String key) throws JSONException
		{
			
			
			
			JSONObject jObj;
			Object value;
			int i=0;
			int len = arr.length();
			
			while(i < len)
			{
				jObj=(JSONObject) arr.getJSONObject(i);
				value=jObj.get(key);
				cbm.addItem(value);
				i++;
			}
		
		}
		
		public void showValues(JComboBox<Object> cbm,Object o[])
		{
			int i=0;
			while(i< o.length)
			{
				cbm.addItem(o[i]);
				i++;
			}
		}
		
		public JSONArray solve(JSONArray arr) throws Exception
		{
			Client client=new Client("localhost",1234);
			
			JSONArray a= new JSONArray();
			
			JSONObject obj = new JSONObject();
			obj.put("Query", "Login");
			
			a.put(obj);
			
			obj = new JSONObject();
			
			obj.put("Username", "admin");
			obj.put("Password", "admin");
			
			a.put(obj);
			
			Query login = new Query(a);
			
			client.sendQuery(login);
			
			Query q=new Query(arr);
			client.sendQuery(q);
			
			Query result=client.receive();
			
			return result.getQuery();
		}
	
		void println(String line)
		{
			console.setText(console.getText() +"\n"+ line );
		}
	
}
