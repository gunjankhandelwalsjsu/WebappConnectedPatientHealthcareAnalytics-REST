package com.ami.webservice;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBDoctorDAO;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.model.AddDoctorToPatientProfileModel;
import com.ami.model.Doctor;
import com.ami.model.Patient;
import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created on 11/8/2015, 8:15 PM
 * AddDoctorToPatientProfileResource.java
 *
 * @author akhandelwal
 */


@Path("/addDoctorToPatientProfile")
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
@Produces(MediaType.APPLICATION_JSON)
public class AddDoctorToPatientProfileResource {

    private static Logger LOGGER = LoggerFactory.getLogger(PatientResource.class);

    @POST
    public Response attachDoctorToPatient(AddDoctorToPatientProfileModel addDoctorToPatientProfileModel) {
        Response response;
        String patientEmail = addDoctorToPatientProfileModel.getPatientEmail();
        String doctorEmail = addDoctorToPatientProfileModel.getDoctorEmail();
        LOGGER.info("incoming patient id is :: " + patientEmail);
        LOGGER.debug("incoming doctor id to attach is :: " + doctorEmail);
        MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
        MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);
        MongoDBDoctorDAO doctorDAO = new MongoDBDoctorDAO(mongo);
        Doctor doctor = new Doctor();
        doctor.setEmail(doctorEmail);
        doctor = doctorDAO.readDoctorfromEmail(doctor);
        Patient patient = new Patient();
        patient.setEmail(patientEmail);
        patient = patientDAO.readPatientfromEmail(patient);
        System.out.println(doctor.getFirstName());
        patient.setDoctorName(doctor.getName());
        patient.setDoctorMailId(doctor.getEmail());
        patient.setPhone(doctor.getPhone());
        patientDAO.updatePatientWithDoctor(patient);
        patient = patientDAO.readPatient(patient);
        return Response.status(Response.Status.OK).entity(patient).build();

    }
    
    @GET
    @Path("/list")

    public Response getListOfDoctors() {
        Response response;
        MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
   
        MongoDBDoctorDAO doctorDAO = new MongoDBDoctorDAO(mongo);
        List<Doctor> doctors = doctorDAO.readAllDoctor();           
        return Response.status(Response.Status.OK).entity(doctors).build();
    
    
    }
    

}
