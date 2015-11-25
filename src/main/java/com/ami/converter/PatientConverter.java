package com.ami.converter;

import com.ami.model.Patient;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class PatientConverter {

	// convert Person Object to MongoDB DBObject
	// take special note of converting id String to ObjectId
	public static DBObject toDBObject(Patient p) {

		BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
				.append("firstName", p.getName())
				.append("lastName", p.getLastName())
				.append("password", p.getPassword())
				.append("streetAddress",p.getStreetAddress() )
				.append("state", p.getState())
				.append("city", p.getCity())
				.append("zipcode", p.getZipCode())
				.append("phone", p.getPhone())
				.append("email", p.getEmail())
		        .append("doctorName",p.getDoctorName())
		        .append("doctorMailId",p.getDoctorMailId())
		        .append("birthDate",p.getBirthDate())
		        .append("dPhone",p.getdPhone())
				.append("gender", p.getGender())
		        .append("allergy", p.getAllergy())
                .append("disease", p.getDisease());


		if (p.getId() != null)
			builder = builder.append("_id", new ObjectId(p.getId()));
		return builder.get();
	}

	// convert DBObject Object to Patient
	// take special note of converting ObjectId to String
	public static Patient toPatient(DBObject doc) {
		Patient p = new Patient();
		p.setName((String) doc.get("firstName"));
		p.setLastName((String) doc.get("lastName"));
		p.setPassword((String) doc.get("password"));
		p.setStreetAddress((String) doc.get("streetAddress"));
		p.setState((String) doc.get("state"));
		p.setZipCode((String) doc.get("zipcode"));
		p.setPhone((String) doc.get("phone"));
		p.setEmail((String) doc.get("email"));
		p.setCity((String) doc.get("city"));
		p.setDoctorName((String) doc.get("doctorName"));
		p.setdPhone((String)doc.get("dPhone"));
		p.setBirthDate((String) doc.get("birthDate"));
		p.setGender((String) doc.get("gender"));
		
		BasicDBList allergy = (BasicDBList) doc.get("allergy");
		List<String> all=new ArrayList<String>();
		if(allergy!=null && allergy.size()!=0){
			for(int i = 0 ; i < allergy.size(); i++) {
				System.out.println("in converter"+allergy.get(i).toString());
				all.add(allergy.get(i).toString());
			}
		}
		p.setAllergy(all);
		
		
		BasicDBList disease = (BasicDBList) doc.get("disease");
		List<String> dis=new ArrayList<String>();
		if(disease!=null && disease.size()!=0){
			for(int i = 0 ; i < disease.size(); i++) {
				System.out.println("in converter"+disease.get(i).toString());
				dis.add(disease.get(i).toString());
			}
		}
		p.setDisease(dis);
		
		
		p.setDoctorMailId((String) doc.get("doctorMailId"));
		ObjectId id = (ObjectId) doc.get("_id");
		p.setId(id.toString());
		return p;
	}
	
}
