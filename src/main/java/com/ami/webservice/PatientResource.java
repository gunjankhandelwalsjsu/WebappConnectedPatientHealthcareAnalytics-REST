package com.ami.webservice;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBDoctorDAO;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.model.Doctor;
import com.ami.model.Patient;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created on 11/8/2015, 4:34 PM
 * PatientResource.java
 *
 * @author akhandelwal
 */

@Path("/patient")
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    private static Logger LOGGER = LoggerFactory.getLogger(PatientResource.class);

    @POST
    public Response createPatient(Patient patient) {
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

        if (!patientDAO.hasPatient(email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email already exist").build();
        } else {

            patient = patientDAO.createPatient(patient);
            return Response.status(Response.Status.CREATED).entity(patient).build();
        }
    }

}
