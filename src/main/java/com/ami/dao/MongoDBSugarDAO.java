package com.ami.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ami.converter.DoctorConverter;
import com.ami.converter.PatientConverter;
import com.ami.converter.SugarConverter;
import com.ami.model.Doctor;
import com.ami.model.Patient;
import com.ami.model.SugarConsumed;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBSugarDAO {
	  private DBCollection col;

	    public MongoDBSugarDAO(MongoClient mongo) {

	        this.col = mongo.getDB("journaldev").getCollection("Sugars");
	    }

	    public SugarConsumed createSugar(SugarConsumed t) {
	        DBObject doc = SugarConverter.toDBObject(t);
	        this.col.insert(doc);
	        ObjectId id = (ObjectId) doc.get("_id");
	        t.setId(id.toString());
	        return t;
	    }

	   

	    public List<SugarConsumed> readAllSugar() {
	        List<SugarConsumed> data = new ArrayList<SugarConsumed>();
	        DBCursor cursor = col.find();
	        while (cursor.hasNext()) {
	            DBObject doc = cursor.next();
	            SugarConsumed t = SugarConverter.toSugar(doc);
	            data.add(t);
	        }
	        return data;
	    }

	    public void deleteSugar(SugarConsumed t) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(t.getId())).get();
	        this.col.remove(query);
	    }

	    public Boolean hasSugar(String email) {

	        BasicDBObject query = new BasicDBObject();
	        query.put("email", email);
	        DBObject data = this.col.findOne(query);
	        if (data == null)
	            return false;
	        else
	            return true;

	    }

	    public SugarConsumed getSugar(String email) {

	        DBObject query = BasicDBObjectBuilder.start()
	                .append("email", email).get();

	        DBObject data = this.col.findOne(query);
	        if (data == null) {
	            return null;
	        } else {

	            return SugarConverter.toSugar(data);
	        }
	    }

	    public SugarConsumed updateSugar(SugarConsumed p) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(p.getId())).get();
	        this.col.update(query, SugarConverter.toDBObject(p));
			return p;
	    }

	    public SugarConsumed readSugar(SugarConsumed p) {
	        DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", new ObjectId(p.getId())).get();
	        DBObject data = this.col.findOne(query);
	        return SugarConverter.toSugar(data);
	    }

	    

	   
	}
