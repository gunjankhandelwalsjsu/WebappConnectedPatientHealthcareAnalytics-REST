package com.ami.converter;

import com.ami.model.Patient;
import com.ami.model.SugarConsumed;
import com.ami.model.Temperature;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SugarConverter {

	// convert Person Object to MongoDB DBObject
	// take special note of converting id String to ObjectId
	public static DBObject toDBObject(SugarConsumed t) {

		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("email", t.getEmail())
				.append("time", t.getTime())
				.append("sugar", t.getSugar());
				
		if (t.getId() != null)
			builder = builder.append("_id", new ObjectId(t.getId()));
		return builder.get();
	}

	// convert DBObject Object to Patient
	// take special note of converting ObjectId to String
	public static SugarConsumed toSugar(DBObject doc) {
		SugarConsumed t = new SugarConsumed();
		t.setEmail((String) doc.get("email"));
		BasicDBList sugar = (BasicDBList) doc.get("sugar");
		List<String> temp=new ArrayList<String>();
		if(sugar!=null && sugar.size()!=0){
			for(int i = 0 ; i < sugar.size(); i++) {
				temp.add((String) sugar.get(i));
			}
		}
		t.setSugar(temp);
		
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
