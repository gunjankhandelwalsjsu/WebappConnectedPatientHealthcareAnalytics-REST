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
        String patientId = addDoctorToPatientProfileModel.getPatienId();
        String doctorId = addDoctorToPatientProfileModel.getDoctorId();
        LOGGER.info("incoming patient id is :: " + patientId);
        LOGGER.debug("incoming doctor id to attach is :: " + doctorId);
        MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
        MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);
        MongoDBDoctorDAO doctorDAO = new MongoDBDoctorDAO(mongo);
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor = doctorDAO.readDoctor(doctor);
        Patient patient = new Patient();
        patient.setId(patientId);
        patient = patientDAO.readPatient(patient);
        //patient.setDoctor(doctor);
        patient.setDoctorName(doctor.getName());
        patient.setDoctorMailId(doctor.getEmail());
        patient.setdId(doctor.getId());
        patientDAO.updatePatientWithDoctor(patient);
        patient = patientDAO.readPatient(patient);
        return Response.status(Response.Status.OK).entity(patient).build();

    }
    
    
    
    

}
