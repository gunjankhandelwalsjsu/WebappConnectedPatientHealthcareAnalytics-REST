package com.ami.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.dao.MongoDBSugarDAO;
import com.ami.model.Food;
import com.ami.model.Patient;
import com.ami.model.ProductFood;
import com.ami.model.SugarConsumed;
import com.mongodb.MongoClient;
import com.twilio.sdk.TwilioRestException;

import utility.SmsSender;

@Path("/nutritionixApi")
@Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
@Produces("application/json")
public class NutritionixApi {
	@POST
	@Path("/{email}")	
	public Response food(@PathParam("email") String email, ProductFood food) throws IOException {
		Response response;
		Food f = new Food();
		String sugarResult = "";
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new
		// DefaultHttpMethodRetryHandler(3, false));
		MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
		MongoDBPatientDAO patientDAO = new MongoDBPatientDAO(mongo);
		Patient p = new Patient();
		System.out.println(email);
		p = patientDAO.getPatient(email);
		System.out.println(p);
		List<String> Allergy = new ArrayList<String>();
		Allergy = p.getAllergy();
		String sugars = " ";
		List<String> Disease = new ArrayList<String>();
		Disease = p.getDisease();

		/*********************************************************************************************************************************/
		/*********************************
		 * If Product is found********************************************v
		 *********************/
		f.setPatientAllergy(Allergy);
		f.setPatientDisease(Disease);

		String product_name = food.getItem_name();
		f.setProductName(product_name);
		String brands = food.getBrand_name();
		f.setBrand(brands);

		/*********************************************/
		/*****************
		 * Checking nutrition facts and sugars
		 *****************/

		sugars = food.getNf_sugars();

		/*********************************************/
		/***************** Checking sugars *****************/
		if (!sugars.equals("null")) {
			MongoDBSugarDAO sugarDAO = new MongoDBSugarDAO(mongo);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			/***************** First Time sugars *****************/

			if (!sugarDAO.hasSugar(email)) {
				SugarConsumed tempObj = new SugarConsumed();
				List<String> sugarVal = new ArrayList<String>();
				System.out.println(sugars);
				sugarVal.add(sugars);
				tempObj.setSugar(sugarVal);
				System.out.println("print" + tempObj.getSugar().toString());
				tempObj.setEmail(email);
				String t = String.valueOf(dateFormat.format(cal.getTime()));
				System.out.println(t);
				String dateString = t.substring(0, t.indexOf(' '));
				String timeString = t.substring(t.indexOf(' ') + 1);
				List<String> timestampVal = new ArrayList<String>();
				timestampVal.add(t);
				tempObj.setTime(timestampVal);
				SugarConsumed temp = sugarDAO.createSugar(tempObj);

				// return
				// Response.status(Response.Status.CREATED).entity(temp).build();
			}
			/***************** Continuing sugars *****************/

			else {
				// System.out.println(
				// temperatureDAO.hasTemperature(email));
				SugarConsumed temp = sugarDAO.getSugar(email);
				temp.getSugar().add(sugars);
				String t = String.valueOf(dateFormat.format(cal.getTime()));

				String dateString = t.substring(0, t.indexOf(' '));
				String timeString = t.substring(t.indexOf(' ') + 1);
				temp.getTime().add(String.valueOf(dateFormat.format(cal.getTime())));
				sugarDAO.updateSugar(temp);

				float sugarTotal = 0;

				for (int i1 = 0; i1 < temp.getTime().size(); i1++) {
					String time = temp.getTime().get(i1).toString();
					if (time.contains(dateString)) {
						if (!temp.getSugar().get(i1).equals(" "))
							sugarTotal += Float.parseFloat(temp.getSugar().get(i1));
					}
				}
				int year = Integer.parseInt(p.getBirthDate().substring(0, 3));
				if ((p.getGender().equals("F") && (sugarTotal > 22) && (year < 1997))
						|| (p.getGender().equals("M") && (sugarTotal > 36) && (year < 1997))
						|| ((sugarTotal > 12) && (year > 1997))) {

					sugarResult = "Exceeded the daily intake." + "\n" + " If you eat this " + product_name
							+ " your sugar intake will reach to " + sugarTotal + "g";

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
					params.add(new BasicNameValuePair("Body", "This is SMS NOTIFICATION TO intimate that PATIENT "
							+ pName + " and email " + pEmail + " is consuming high sugar"));
					try {
						SmsSender.sendSMS(params);
					} catch (TwilioRestException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// return
				// Response.status(Response.Status.CREATED).entity(temp).build();

			}
		}

		f.setSugarResult(sugarResult);

		/*********************************************/
		/*****************
		 * Checking Ingredients and generating allergy Result
		 *****************/

		String ingredientString = food.getNf_ingredient_statement();

		if (Allergy != null && Allergy.size() != 0) {
			for (String a : Allergy) {

				if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(ingredientString, a)) {
					f.setAllergyResult("Don't consume you are allergic to " + a);
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
					params.add(new BasicNameValuePair("Body",
							"This is SMS NOTIFICATION TO intimate allergic food take for PATIENT " + pName
									+ " and email " + pEmail));
					try {
						SmsSender.sendSMS(params);
					} catch (TwilioRestException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else
					f.setAllergyResult("Safe to consume");

			}

		}
        return Response.status(Response.Status.CREATED).entity(f).build();

	}

	

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
