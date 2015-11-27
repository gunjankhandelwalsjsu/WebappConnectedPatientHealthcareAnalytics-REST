package com.ami.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ami.converter.DoctorConverter;
import com.ami.converter.NutritionixConverter;
import com.ami.converter.PatientConverter;
import com.ami.model.Doctor;
import com.ami.model.Food;
import com.ami.model.Patient;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MongoDBNutritionixDAO {
	private DBCollection col;

    public MongoDBNutritionixDAO(MongoClient mongo) {
        this.col = mongo.getDB("journaldev").getCollection("Nutritionix");
    }

    
    public Boolean hasFood(String barcode) {

        BasicDBObject query = new BasicDBObject();
        System.out.println(barcode);
        query.put("upc",Long.parseLong(barcode));
        DBObject data = this.col.findOne(query);
        if (data != null)
        {
            System.out.println("safe"+barcode);

            return true;
        }
        else{
        	
            System.out.println("null"+barcode);
            return false;
        }

    }

    public Food getFood(String barcode) {

        DBObject query = BasicDBObjectBuilder.start()
                .append("upc",Long.parseLong(barcode)).get();

        DBObject data = this.col.findOne(query);
        if (data == null) {
            return null;
        } else {
            System.out.println("gt here");
            return NutritionixConverter.toFood(data);
        }
    }

  
   
	

}
