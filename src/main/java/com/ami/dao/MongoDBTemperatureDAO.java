package com.ami.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ami.converter.DoctorConverter;
import com.ami.converter.PatientConverter;
import com.ami.converter.TemperatureConverter;
import com.ami.model.Doctor;
import com.ami.model.Patient;
import com.ami.model.Temperature;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBTemperatureDAO {
	  private DBCollection col;

	    public MongoDBTemperatureDAO(MongoClient mongo) {
	        System.out.println("M I being called");

	        this.col = mongo.getDB("journaldev").getCollection("Temperatures");
	        System.out.println("crweated");
	    }

	    public Temperature createTemperature(Temperature t) {
	        DBObject doc = TemperatureConverter.toDBObject(t);
	        this.col.insert(doc);
	        ObjectId id = (ObjectId) doc.get("_id");
	        t.setId(id.toString());
	        return t;
	    }

	   

	    public List<Temperature> readAllTemperature() {
	        List<Temperature> data = new ArrayList<Temperature>();
	        DBCursor cursor = col.find();
	        while (cursor.hasNext()) {
	            DBObject doc = cursor.next();
	            Temperature t = TemperatureConverter.toTemperature(doc);
	            data.add(t);
	        }
	        return data;
	    }

	    public void deleteTemperature(Temperature t) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(t.getId())).get();
	        this.col.remove(query);
	    }

	    public Boolean hasTemperature(String email) {

	        BasicDBObject query = new BasicDBObject();
	        query.put("email", email);
	        DBObject data = this.col.findOne(query);
	        System.out.println(data+email);
	        if (data == null)
	            return false;
	        else
	            return true;

	    }

	    public Temperature getTemperature(String email) {

	        DBObject query = BasicDBObjectBuilder.start()
	                .append("email", email).get();

	        DBObject data = this.col.findOne(query);
	        if (data == null) {
	            return null;
	        } else {

	            return TemperatureConverter.toTemperature(data);
	        }
	    }

	    public Temperature updateTemperature(Temperature p) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(p.getId())).get();
	        this.col.update(query, TemperatureConverter.toDBObject(p));
			return p;
	    }

	    public Temperature readTemperature(Temperature p) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(p.getId())).get();
	        DBObject data = this.col.findOne(query);
	        return TemperatureConverter.toTemperature(data);
	    }

	    

	   
	}
