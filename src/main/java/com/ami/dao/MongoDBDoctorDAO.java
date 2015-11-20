package com.ami.dao;


import com.ami.converter.DoctorConverter;
import com.ami.model.Doctor;
import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

//DAO class for different MongoDB CRUD operations
//take special note of "id" String to ObjectId conversion and vice versa
//also take note of "_id" key for primary key
public class MongoDBDoctorDAO {

	private DBCollection col;

	public MongoDBDoctorDAO(MongoClient mongo) {
		this.col = mongo.getDB("journaldev").getCollection("Doctors");
	}

	public Doctor createDoctor(Doctor p) {
		DBObject doc = DoctorConverter.toDBObject(p);
		this.col.insert(doc);
		ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id.toString());
		return p;
	}

	public void updateDoctor(Doctor p) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(p.getId())).get();
		this.col.update(query, DoctorConverter.toDBObject(p));
	}

	public List<Doctor> readAllDoctor() {
		List<Doctor> data = new ArrayList<Doctor>();
		DBCursor cursor = col.find();
		while (cursor.hasNext()) {
			DBObject doc = cursor.next();
			Doctor p = DoctorConverter.toDoctor(doc);
			data.add(p);
		}
		return data;
	}

	public void deleteDoctor(Doctor p) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(p.getId())).get();
		this.col.remove(query);
	}

	public Doctor readDoctor(Doctor p) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("_id", new ObjectId(p.getId())).get();
		DBObject data = this.col.findOne(query);
		return DoctorConverter.toDoctor(data);
	}
	
	public Doctor readDoctorfromEmail(Doctor p) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("email", p.getEmail()).get();
		DBObject data = this.col.findOne(query);
		return DoctorConverter.toDoctor(data);
	}
	
	
	public Boolean hasDoctor(String email) {
		DBObject query = BasicDBObjectBuilder.start()
				.append("email", email).get();
		DBObject data = this.col.findOne(query);
		if(data!=null)
			return false;
		else
			return true;
		
	}
	public Doctor getDoctor(String email) {	
		DBObject query = BasicDBObjectBuilder.start()
				.append("email", email).get();
		DBObject data = this.col.findOne(query);
		if (data==null){
			System.out.println("ret null");		
			return null;}
		else{			
		return DoctorConverter.toDoctor(data);
		}
		
	}
	
	public Doctor getDoctorWithId(String id) {	
		  DBObject query = BasicDBObjectBuilder.start()
	                .append("_id", id).get();
		DBObject data = this.col.findOne(query);
		if (data==null){
			System.out.println("u are pretty null");		
			return null;}
		else{			
		return DoctorConverter.toDoctor(data);
		}
		
	}

	public List<String> readAllPatient(Doctor d) {
		// TODO Auto-generated method stub
		List<String> Patientlist = new ArrayList<String>();	

		Doctor doc = 	readDoctor(d);
		System.out.println("printindao"+doc.getName());
		List<String> emailList=new ArrayList<String>();
		emailList=doc.getPatientEmail();
		if(emailList!=null && emailList.size()!=0){
		for(String e:emailList)
			{
			 if(!e.equals(null)){
			System.out.println("Printing emails"+e);
			Patientlist.add(e);}
			 
			}
		}
		else 
		    Patientlist.add("no patient yet");

		
		return Patientlist;	}

}
