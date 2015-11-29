package com.ami.webservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.dao.MongoDBTemperatureDAO;
import com.ami.model.Patient;
import com.ami.model.Temperature;
import com.mongodb.MongoClient;
import com.twilio.sdk.TwilioRestException;

import utility.SmsSender;

@Path("/temperatureAnalytics/")

public class ThresholdTemperature {

	@GET
	@Path("/{email}/{temperature}")
	public void temperature(@PathParam("email") String email, @PathParam("temperature") Double temperature) {

		MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
		MongoDBTemperatureDAO temperatureDAO = new MongoDBTemperatureDAO(mongo);
		MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);

		Patient p = new Patient();
		System.out.println(email);

		p = patientDAO.getPatient(email);
		java.util.Date date = new java.util.Date();

		if (temperature > 100.00) {
			/********************
			 * SMS Notification
			 ***********************************/
			String pName = p.getFirstName();
			String pEmail = p.getEmail();

			String dEmail = p.getDoctorMailId();

			String dPhone = p.getdPhone();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("To", "+1" + dPhone));
			params.add(new BasicNameValuePair("From", "+16509341358"));
			params.add(new BasicNameValuePair("Body", "This is SMS NOTIFICATION TO intimate high temperature of value  "
					+ temperature + " for PATIENT " + pName + " and email " + pEmail));
			try {
				SmsSender.sendSMS(params);
			} catch (TwilioRestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if (!temperatureDAO.hasTemperature(email)) {
			Temperature tempObj = new Temperature();
			List<Double> tempVal = new ArrayList<Double>();
			tempVal.add(temperature);
			tempObj.setTemp(tempVal);
			tempObj.setEmail(email);
			List<Timestamp> timestampVal = new ArrayList<Timestamp>();
			timestampVal.add(new Timestamp(date.getTime()));
			tempObj.setTime(timestampVal);
			Temperature temp = temperatureDAO.createTemperature(tempObj);

		} else {
			Temperature temp = temperatureDAO.getTemperature(email);
			temp.getTemp().add(temperature);
			temp.getTime().add(new Timestamp(date.getTime()));
			temperatureDAO.updateTemperature(temp);

		}

	}
}
