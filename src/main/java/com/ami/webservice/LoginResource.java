package com.ami.webservice;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBDoctorDAO;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.model.Doctor;
import com.ami.model.Login;
import com.ami.model.Patient;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created on 11/8/2015, 3:53 PM
 * LoginResource.java
 *
 * @author akhandelwal
 */

@Path("/login")
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    @POST
    public Response doPatientLogin(Login login) {
        Response response;
        String password = login.getPassword();
        String email = login.getEmail();
        if ((email == null || email.equals("")) || (password == null || password.equals(""))) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Mandatory params are missing").build();
        }
        MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
        // patient-login (default value is for patient , pass this value as false if it is for doctor)
        if (login.isPatient()) {
            MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);
            System.out.println(email);
            Patient patient = patientDAO.getPatient(email);
            if (patient == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("please check your username/passwd combination , it is incorrect").build();
            } else {
                return Response.status(Response.Status.OK).entity(patient).build();
            }

        } else // doctor-login
        {
            MongoDBDoctorDAO DoctorDAO = new MongoDBDoctorDAO(mongo);

            Doctor doctor = DoctorDAO.getDoctor(email);
            if (doctor == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("please check your username/passwd combination , it is incorrect").build();
            } else {
                return Response.status(Response.Status.OK).entity(doctor).build();
            }
        }
        }
        @GET
        @Path("/profile/{email}")
        public Patient doPatientProfile(@PathParam("email")String email) {
            System.out.println(email);
            MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
            MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);
            Patient patient = patientDAO.getPatient(email);
			return patient;

    }
        
        @POST
        @Path("/editProfile")
        public Response editPatientProfile(Patient patient) {
        	
        	        Response response;
        	        LOGGER.info("incoming patient info is :: " + patient);
        	        String password = patient.getPassword();
        	        String email = patient.getEmail();
        	        String firstName = patient.getFirstName();
        	        if ((email == null || email.equals("")) || (password == null || password.equals("")) || (firstName == null || firstName.equals(""))) {
        	            return Response.status(Response.Status.BAD_REQUEST).entity("Mandatory params are missing").build();
        	        }
        	        MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
        	        MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);

        	       
        	            patient = patientDAO.updatePatient(patient);
        	            return Response.status(Response.Status.CREATED).entity(patient).build();
        	        }
        	    

        	}



