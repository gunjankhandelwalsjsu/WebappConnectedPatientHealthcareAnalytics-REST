package com.ami.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.ami.dao.MongoDBTemperatureDAO;
import com.ami.model.Food;
import com.ami.model.Patient;
import com.ami.model.SugarConsumed;
import com.ami.model.Temperature;
import com.mongodb.MongoClient;
import com.twilio.sdk.TwilioRestException;

import utility.SmsSender;

@Path("/food")
@Produces("application/json")
public class GetFoodDetail {
	@GET
	@Path("/{email}/{barcode}")
	public Food food(@PathParam("email") String email, @PathParam("barcode") String barcode) throws IOException {
		Response response ;
		System.out.println(barcode);
		String SERVER_URL = "http://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
		System.out.println(SERVER_URL);
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
			
		List<String> Disease = new ArrayList<String>();
		Disease = p.getDisease();
		
		JSONObject object = new JSONObject();
		

		// System.out.println(resp);

		InputStream inputStream = null;
		String result = "";

		// create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		HttpResponse httpResponse = httpclient.execute(new HttpGet(SERVER_URL));

		// receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// convert inputstream to string
		if (inputStream != null)
			result = convertInputStreamToString(inputStream);
		else
			result = "Did not work!";

		if (result.equals("Did not work")) {
			System.out.println("result doesnt exist");
			f.setProductName("result doesnt exist");
			f.setBrand("result doesnt exist");
			f.setNutriments("result doesnt exist");
			f.setAllergyResult("result doesnt exist");
			
			List<String> a = new ArrayList<String>();
			a.add("result doesnt exist");
			f.setPatientAllergy(a);
			
			List<String> b = new ArrayList<String>();
			b.add("result doesnt exist");
			f.setPatientDisease(b);
			
			return f;
		} else {

			JSONObject obj = new JSONObject(result);
			JSONObject product = obj.getJSONObject("product");
			String product_name = product.getString("product_name");
			JSONArray IngredientTraces = product.getJSONArray("traces_tags");
			String brands = product.getString("brands");
			JSONObject nutriments = product.getJSONObject("nutriments");
			
			String sugars=nutriments.getString("sugars");
			System.out.println("sugarsssssss"+sugars);
			if(!sugars.equals(null)){
				 MongoDBSugarDAO sugarDAO = new MongoDBSugarDAO(mongo);
				 java.util.Date date= new java.util.Date();
				 if(!sugarDAO.hasSugar(email)){
	      	 		   SugarConsumed tempObj=new SugarConsumed();
	      	 		   List<String> sugarVal=new ArrayList<String>();
	      	 		   System.out.println(sugars);
	      	 		   sugarVal.add(sugars);
	      	 		   tempObj.setSugar(sugarVal);
	      	 		   System.out.println("print"+tempObj.getSugar().toString());
	      	 		   tempObj.setEmail(email);
	      	 		   List<Timestamp> timestampVal=new ArrayList<Timestamp>();
	      	 		   String t=new Timestamp(date.getTime()).toString();
	      	 		   String delims = "[ISODate()]";
	      	 		   t.split(delims);
	      	 		String dateString=t.substring(0, t.indexOf(' '));
      	 	        String timeString=t. substring(t.indexOf(' ') + 1);
	      	 		   timestampVal.add(new Timestamp(date.getTime()));
	      	 		   tempObj.setTime(timestampVal);
	      	 		   SugarConsumed temp = sugarDAO.createSugar(tempObj);
	      	 		   
	      	 		   
	    	           //   return Response.status(Response.Status.CREATED).entity(temp).build();
	      	 	   }
	    	 		else
	                 {
	     	 		//System.out.println( temperatureDAO.hasTemperature(email));
	    	 			SugarConsumed temp = sugarDAO.getSugar(email);
	     	           temp.getSugar().add(sugars);
	     	           String t=new Timestamp(date.getTime()).toString();
	      	 		   String delims = "[ISODate()]";
	      	 		   t.split(delims);
	      	 		    String dateString=t.substring(0, t.indexOf(' '));
	      	 	        String timeString=t. substring(t.indexOf(' ') + 1);
	      	 		   System.out.println("I am hreeeeeee"+t);
	    	           temp.getTime().add(new Timestamp(date.getTime()));
	     	           sugarDAO.updateSugar(temp);	     	          

	    	           int sugarTotal=0;
	    	           
	    	           for(int i=0;i<temp.getTime().size();i++)
	    	           {
		     	           String time=temp.getTime().get(i).toString();
		     	           if(time.contains(dateString)){
		     	        	    sugarTotal+=Integer.parseInt(temp.getSugar().get(i));
		     	           }	    	        	   
	    	           }
	    	           int year=Integer.parseInt(p.getBirthDate().substring(0, 3));
	    	           if((p.getGender().equals("F")&&(sugarTotal>22)&& (year<1997))||(p.getGender().equals("M")&&(sugarTotal>36)&&(year<1997))||((sugarTotal>12)&&(year>1997))){
	    	        	   
	    	        	   sugarResult="Exceeded the daily intake."+"\n"+" If you eat this "+product_name+" your sugar intake will reach to "+sugarTotal+"g";
	    	        	   
	    	        	   System.out.println("greate"+p.getGender());
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
							params.add(new BasicNameValuePair("Body", "This is SMS NOTIFICATION TO intimate that PATIENT " + pName+" and email "+pEmail+ " is consuming high sugar"));
							try {
								SmsSender.sendSMS(params);
							} catch (TwilioRestException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							}
	    	           
		          //  return Response.status(Response.Status.CREATED).entity(temp).build();

	     	 	   }
			}
			
			
			
			f.setProductName(product_name);
			f.setBrand(brands);
			f.setNutriments(nutriments.toString());
			f.setSugarResult(sugarResult);
		/*	JSONArray states = product.getJSONArray("states_tags");
			List<String> state = new ArrayList<String>();
			for (int i = 0; i < states.length(); i++) {
				state.add(states.get(i).toString());
			}*/

			List<String> IngredientTracesList = new ArrayList<String>();
			String ingredientTrace=" ";
			for (int i = 0; i < IngredientTraces.length(); i++) {
				IngredientTracesList.add(IngredientTraces.get(i).toString());
				ingredientTrace+=IngredientTraces.get(i).toString()+"\t";
				
			}
			
			System.out.println(product.toString() + "product");
		/*	if (state.contains("en:ingredients-completed")) {
				System.out.println("true");*/
				String ingredientString = product.getString("ingredients_text");
				String[] ingredients = ingredientString.split(",");
				for (String i : ingredients) {
					System.out.println(i);
					System.out.println("Sending......................");
				
				f.setAllergyResult("Safe to consume");				
				if (Allergy != null && Allergy.size() != 0) {
					for (String a : Allergy) {

						if ((org.apache.commons.lang3.StringUtils.containsIgnoreCase(ingredientString, a))|| (org.apache.commons.lang3.StringUtils.containsIgnoreCase(ingredientTrace, a)))                          
						{	f.setAllergyResult("Don't consume you are allergic to "+a); 
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
						params.add(new BasicNameValuePair("Body", "This is SMS NOTIFICATION TO intimate allergic food take for PATIENT " + pName+" and email "+pEmail));
						try {
							SmsSender.sendSMS(params);
						} catch (TwilioRestException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						}
						
					

					}
				

				}
			}
			f.setPatientAllergy(Allergy);
			f.setPatientDisease(Disease);
			System.out.println(f.toString());
			

			
		}
		return f;

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