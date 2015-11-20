package com.ami.converter;

import com.ami.model.Patient;
import com.ami.model.Temperature;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemperatureConverter {

	// convert Person Object to MongoDB DBObject
	// take special note of converting id String to ObjectId
	public static DBObject toDBObject(Temperature t) {

		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("email", t.getEmail())
				.append("time", t.getTime())
				.append("temperature", t.getTemp());
				
		if (t.getId() != null)
			builder = builder.append("_id", new ObjectId(t.getId()));
		return builder.get();
	}

	// convert DBObject Object to Patient
	// take special note of converting ObjectId to String
	public static Temperature toTemperature(DBObject doc) {
		Temperature t = new Temperature();
		t.setEmail((String) doc.get("email"));
		BasicDBList temperature = (BasicDBList) doc.get("temperature");
		List<Double> temp=new ArrayList<Double>();
		if(temperature!=null && temperature.size()!=0){
			for(int i = 0 ; i < temperature.size(); i++) {
				temp.add((Double) temperature.get(i));
			}
		}
		t.setTemp(temp);
		
		BasicDBList time = (BasicDBList) doc.get("time");
		List<Timestamp> ti=new ArrayList<Timestamp>();
		if(time!=null && time.size()!=0){
			for(int i = 0 ; i < time.size(); i++) {
				Date tstamp=(Date) time.get(i);
			    Timestamp timestamp = new java.sql.Timestamp(tstamp.getTime());
				ti.add(timestamp);
				System.out.println("adding time"+ti.toString());
			}
		}
		t.setTime(ti);
		
		ObjectId id = (ObjectId) doc.get("_id");
		t.setId(id.toString());
		return t;
	}
	
}
