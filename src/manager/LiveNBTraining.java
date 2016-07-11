package manager;

import java.util.HashMap;

public class LiveNBTraining {

	
	HashMap<String,HashMap<String,Long>> map;

	long value=1;
	
	LiveNBTraining()
	{
		map=new HashMap<String,HashMap<String,Long>>();
		
		map.put("Month",new HashMap<String,Long>());
		map.put("Year", new HashMap<String,Long>());
		map.put("Country",new HashMap<String,Long>());
		map.put("Region",new HashMap<String,Long>());
		map.put("State",new HashMap<String,Long>());
		map.put("City",new HashMap<String,Long>());
		map.put("Product_Name",new HashMap<String,Long>());
		map.put("Category",new HashMap<String,Long>());
		map.put("Sub_Category",new HashMap<String,Long>());
		map.put("Brand",new HashMap<String,Long>());
		map.put("Quantity_Sold",new HashMap<String,Long>());
		map.put("Class",new HashMap<String,Long>());
		
	}
	
	void train(DataRow row)
	{
		String temp;
		
		//for Class
		HashMap<String, Long> classMap=map.get("Class");
		
		if(classMap.get(row.classLabel)==null)
		{
			classMap.put(row.classLabel, value);
		}
		else
		{
			classMap.put(row.classLabel,classMap.get(row.classLabel)+value);
		}
		
		//for Month
		HashMap<String, Long> monthMap=map.get("Month");
			
			//for attribute + class_label
			temp = row.month + "_" + row.classLabel;
			
			if(monthMap.get(temp)==null)
			{
				monthMap.put(temp,value);
			}
			else
			{
			monthMap.put(temp,monthMap.get(temp)+value);
			}
		
			//for attribute
			if(monthMap.get(row.month)==null)
			{
				monthMap.put(row.month,value);
			}
			else
			{
				monthMap.put(row.month,monthMap.get(row.month)+value);
			}
	
		//for year
		HashMap<String, Long> yearMap=map.get("Year");
			
			//for attribute + class_label
			temp=row.year + "_" +row.classLabel;
			if(yearMap.get(temp)==null)
			{
				yearMap.put(temp,value);
			}
			else
			{
				yearMap.put(temp ,yearMap.get(temp)+value);
			}
			//for attribute
			temp=row.year +"";
			if(yearMap.get(temp)==null)
			{
				yearMap.put(temp,value);
			}
			else
			{
				yearMap.put(temp,yearMap.get(temp)+value);
			}
	
		//for country
		HashMap<String, Long> countryMap=map.get("Country");
			
			//for attribute + class_label
			temp=row.country +"_"+ row.classLabel;
			
			if(countryMap.get(temp)==null)
			{
				countryMap.put(temp, value);
			}
			else
			{
				countryMap.put(temp,countryMap.get(temp)+value);
			}
		
			//for attribute
			if(countryMap.get(row.country)==null)
			{
				countryMap.put(row.country, value);
			}
			else
			{
				countryMap.put(row.country,countryMap.get(row.country)+value);
			}
	
		//for region
		HashMap<String, Long> regionMap=map.get("Region");
		
			//for attribute + class_label
			temp=row.region + "_" + row.classLabel;
			
			if(countryMap.get(temp)==null)
			{
				regionMap.put(temp, value);
			}
			else
			{
				regionMap.put(temp,regionMap.get(temp)+value);
			}
		
			//for attribute
			if(countryMap.get(row.region)==null)
			{
				regionMap.put(row.region, value);
			}
			else
			{
				regionMap.put(row.region,regionMap.get(row.region)+value);
			}
		
		
		//for state
				HashMap<String, Long> stateMap=map.get("State");
				
				//for attribute + class_label
				temp=row.state + "_" + row.classLabel;
				
				if(stateMap.get(temp)==null)
				{
					stateMap.put(temp, value);
				}
				else
				{
					stateMap.put(temp,stateMap.get(temp)+value);
				}
				//for attribute
				if(stateMap.get(row.state)==null)
				{
					stateMap.put(row.state, value);
				}
				else
				{
					stateMap.put(row.state,stateMap.get(row.state)+value);
				}
			
				
			//for city
				HashMap<String, Long> cityMap=map.get("City");
				
				//for attribute + class_label
				temp= row.city + "_" + row.classLabel;
				if(cityMap.get(temp)==null)
				{
					cityMap.put(temp, value);
				}
				else
				{
					cityMap.put(temp,cityMap.get(temp)+value);
				}
				
				//for attribute
				if(cityMap.get(row.city)==null)
				{
					cityMap.put(row.city, value);
				}
				else
				{
					cityMap.put(row.city,cityMap.get(row.city)+value);
				}
				
				
				//for Product_Name
				HashMap<String, Long> productMap=map.get("Product_Name");
				
				//for attribute + class_label
				temp= row.productName + "_" + row.classLabel;
				if(productMap.get(temp)==null)
				{
					productMap.put(temp, value);
				}
				else
				{
					productMap.put(temp,productMap.get(temp)+value);
				}
				
				//for attribute
				if(productMap.get(row.productName)==null)
				{
					productMap.put(row.productName, value);
				}
				else
				{
					productMap.put(row.productName,productMap.get(row.productName)+value);
				}

			
				//for category
				HashMap<String, Long> categoryMap=map.get("Category");
				
				//for attribute + class_label
				temp= row.category + "_" + row.classLabel;
				if(categoryMap.get(temp)==null)
				{
					categoryMap.put(temp, value);
				}
				else
				{
					categoryMap.put(temp,categoryMap.get(temp)+value);
				}
				
				//for attribute
				if(categoryMap.get(row.category)==null)
				{
					categoryMap.put(row.category, value);
				}
				else
				{
					categoryMap.put(row.category,categoryMap.get(row.category)+value);
				}
				
			//for Sub_Category
				HashMap<String, Long> subCategoryMap=map.get("Sub_Category");
				
				//for attribute + class_label
				temp= row.subCategory + "_" + row.classLabel;
				
				if(subCategoryMap.get(temp)==null)
				{
					subCategoryMap.put(temp, value);
				}
				else
				{
					subCategoryMap.put(temp,subCategoryMap.get(temp)+value);
				}
			
				//for attribute
				
				if(subCategoryMap.get(row.subCategory)==null)
				{
					subCategoryMap.put(row.subCategory, value);
				}
				else
				{
					subCategoryMap.put(row.subCategory,subCategoryMap.get(row.subCategory)+value);
				}
			
			//for Brand
				HashMap<String, Long> brandMap=map.get("Brand");
				
				//for attribute + class_label
				temp= row.brand + "_" + row.classLabel;
				if(brandMap.get(temp)==null)
				{
					brandMap.put(temp, value);
				}
				else
				{
					brandMap.put(temp,brandMap.get(temp)+value);
				}
				//for attribute
				if(brandMap.get(row.brand)==null)
				{
					brandMap.put(row.brand, value);
				}
				else
				{
					brandMap.put(row.brand,brandMap.get(row.brand)+value);
				}
				
				//for Quantity_Sold
				HashMap<String, Long> quantityMap=map.get("Quantity_Sold");
				
				//for attribute + class_label
				temp= row.quantity + "_" + row.classLabel;
				
				if(quantityMap.get(temp)==null)
				{
					quantityMap.put(temp, value);
				}
				else
				{
					quantityMap.put(temp,quantityMap.get(temp)+value);
				}
				
				//for attribute
				temp = row.quantity + "";
				if(quantityMap.get(temp)==null)
				{
					quantityMap.put(temp, value);
				}
				else
				{
					quantityMap.put(temp,quantityMap.get(temp)+value);
				}
				
						
	}
	

}
