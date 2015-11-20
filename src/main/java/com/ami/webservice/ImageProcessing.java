package com.ami.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ami.context.WebAppContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
 
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Path("/patientImage")
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class ImageProcessing {

	 static Logger logger = Logger.getLogger(ImageProcessing.class);
	  @POST
	  @Path("/upload")
	  @Consumes(MediaType.MULTIPART_FORM_DATA)
	  @Produces("text/html")
	  public Response uploadFile(
	      @FormDataParam("image") InputStream fileInputStream,
	      @FormDataParam("image") FormDataContentDisposition fileInputDetails)
	//      @FormDataParam("email") FormDataContentDisposition emailInputDetails,

	   //   @FormDataParam("email") String email)
	  {
		  System.out.println("inside upload");
		  
			MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
			DB mongoDB = mongo.getDB("journaldev");
		  //   System.out.println("imgProcessing"+email);
		    //Let's store the standard data in regular collection
		    DBCollection collection = mongoDB.getCollection("imageC");
		     
		    // Let's query to ensure ID does not already exist in Mongo
		    // if it does, we will alert the user 
		    String filename=fileInputDetails.getFileName();
		    BasicDBObject query = new BasicDBObject();
		    query.put("filename", filename);
		    DBCursor cursor = collection.find(query);
		     
		    if (!cursor.hasNext()) {
		      // Build our document and add all the fields
		      BasicDBObject document = new BasicDBObject();
		   //   document.append("email", email);
		      document.append("filename", fileInputDetails.getFileName());
		       		      
		      //insert the document into the collection 
		      collection.insert(document);
		       		      // Now let's store the binary file data using filestore GridFS  
		      GridFS fileStore = new GridFS(mongoDB, "imageCollection");
		      GridFSInputFile inputFile = fileStore.createFile(fileInputStream);
		    //  inputFile.setId(email);
		      inputFile.setFilename(fileInputDetails.getFileName());
		      inputFile.save();			
		      
		      String status = "Upload has been successful";
		     
		 //     logger.info("Email: " + email);
		      logger.info("fileInputDetails: " + fileInputDetails);
		       System.out.println("Inside img proc");
		      return Response.status(200).entity(status).build();
		    } else {
		      String status = "Unable to insert record with Email: " + filename +" as record already exists!!!";
		      logger.info(status);
		      return Response.status(200).entity(status).build();
		    }
		  }
	  @GET
	  @Path("/download/{email}")
	  @Produces(MediaType.APPLICATION_OCTET_STREAM)
	  public Response downloadFilebyEmail(@PathParam("email")  String email) throws IOException {
	     
	    Response response = null;

		MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
		DB mongoDB = mongo.getDB("journaldev");
	  //   System.out.println("imgProcessing"+email);
	    //Let's store the standard data in regular collection
	    DBCollection collection = mongoDB.getCollection("imageCollection");
	     
	    //Let's store the standard data in regular collection
	     
	    logger.info("Inside downloadFilebyID...");
	    logger.info("Email: " + email);
	 
	    BasicDBObject query = new BasicDBObject();
	    query.put("filename", email+".jpg");
	    DBObject doc = collection.findOne(query);
	    DBCursor cursor = collection.find(query);
	         
	    if (cursor.hasNext()) {
	      Set<String> allKeys = doc.keySet();
	      HashMap<String, String> fields = new HashMap<String,String>();
	      for (String key: allKeys) {
	        fields.put(key, doc.get(key).toString());
	      }
	           
	      
	      logger.info("filename: " + fields.get("filename"));
	          
	      GridFS fileStore = new GridFS(mongoDB, "imageCollection");
	      GridFSDBFile gridFile = fileStore.findOne(query);
	   
	      InputStream in = gridFile.getInputStream();
	           
	      ByteArrayOutputStream out = new ByteArrayOutputStream();
	      int data = in.read();
	      while (data >= 0) {
	        out.write((char) data);
	        data = in.read();
	      }
	      out.flush();
	   
	      ResponseBuilder builder = Response.ok(out.toByteArray());
	      builder.header("Content-Disposition", "attachment; filename=" + fields.get("filename"));
	      response = builder.build();
	    } else {
	      response = Response.status(404).
	    		  
	          entity(" Unable to get file with ID: " + email).
	          type("text/plain").
	          build();
	    }
	         
	    return response;
	  }

}