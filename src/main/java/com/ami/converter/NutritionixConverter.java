package com.ami.converter;

import com.ami.model.Doctor;
import com.ami.model.Food;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class NutritionixConverter {

	// convert Doctor Object to MongoDB DBObject
	// take special note of converting id String to ObjectId
	public static DBObject toDBObject(Food p) {

		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("item_name", p.getProductName())
				.append("brands", p.getBrand())
				.append("nf_sugars", p.getNutriments())
				.append("nf_ingredient_statement", p.getIngredients());
				
		return builder.get();
	}

	// convert DBObject Object to Doctor
	// take special note of converting ObjectId to String
	public static Food toFood(DBObject doc) {
		Food p = new Food();
		p.setBrand((String) doc.get("brand_name"));
		p.setIngredients((String) doc.get("nf_ingredient_statement"));
		String s=(String) doc.get("nf_sugars");
           if(s.equals("null"))		
           {
   			System.out.println("reached");

	          p.setNutriments((String) doc.get("nf_sugars"));
		}
		else{
			
   			System.out.println("in else statement");

			p.setNutriments("No information available");

		}

	
		p.setProductName((String) doc.get("item_name"));
		
		return p;

	}
	
	
	
}
