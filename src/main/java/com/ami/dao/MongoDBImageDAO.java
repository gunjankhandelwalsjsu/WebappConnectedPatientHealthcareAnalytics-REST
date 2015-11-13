package com.ami.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.ami.converter.DoctorConverter;
import com.ami.model.Doctor;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDBImageDAO {
	
	private DBCollection col;
	private DB db;

	public MongoDBImageDAO(MongoClient mongo) {
		this.db = mongo.getDB("journaldev");
	}
	public void saveImage(String email,String address,String fileName) throws IOException {

	File imageFile = new File(address);
	GridFS gfsPhoto = new GridFS(this.db, email);
	GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
	gfsFile.setFilename(fileName);
	gfsFile.save();
	
	}
	public GridFSDBFile getImage(String email,String fileName) throws IOException {

	GridFS gfsPhoto = new GridFS(this.db, email);
	GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
	System.out.println(imageForOutput);
	return imageForOutput;
	}
	public void deleteImage(String email,String fileName) throws IOException{
	GridFS gfsPhoto = new GridFS(this.db, email);
	gfsPhoto.remove(gfsPhoto.findOne(fileName));
	}
}
	