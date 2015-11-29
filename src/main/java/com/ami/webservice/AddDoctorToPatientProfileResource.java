package com.ami.webservice;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBDoctorDAO;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.model.AddDoctorToPatientProfileModel;
import com.ami.model.Doctor;
import com.ami.model.Patient;
import com.mongodb.MongoClient;
import com.twilio.sdk.TwilioRestException;

import utility.SmsSender;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created on 11/8/2015, 8:15 PM AddDoctorToPatientProfileResource.java
 *
 * @author akhandelwal
 */

@Path("/addDoctorToPatientProfile")
@Consumes({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
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
		patient.setdPhone(doctor.getPhone());
		patientDAO.updatePatientWithDoctor(patient);
		patient = patientDAO.readPatient(patient);
		/********************
		 * SMS Notification
		 ***********************************/
		String pName = patient.getFirstName();
		String pEmail = patient.getEmail();

		String dEmail = patient.getDoctorMailId();

		String dPhone = patient.getdPhone();
		System.out.println(dPhone);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", "+1" + dPhone));
		params.add(new BasicNameValuePair("From", "+16509341358"));
		params.add(new BasicNameValuePair("Body", "This is SMS NOTIFICATION TO intimate that PATIENT " + pName
				+ " and email " + pEmail + "has added you as a doctor"));
		try {
			SmsSender.sendSMS(params);
		} catch (TwilioRestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/***************** Setting Patient for doctor ***************/
		List<String> PatientList = new ArrayList<String>();

		for (String pmail : doctor.getPatientEmail()) {
			if (!pmail.equals(null)&&!pmail.equals("no Patient yet") && (!PatientList.contains(pmail))) {
				PatientList.add(pmail);
			}
		}
		if(!PatientList.contains(pEmail))
		PatientList.add(pEmail);
		for (int i = 0; i < PatientList.size(); i++)
			System.out.println(PatientList.get(i));
		doctor.setPatientEmail(PatientList);
		doctorDAO.updateDoctor(doctor);
		/*********************************************************/
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
