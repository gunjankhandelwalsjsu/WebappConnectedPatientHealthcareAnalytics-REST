package com.ami.webservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ami.context.WebAppContext;
import com.ami.dao.MongoDBNutritionixDAO;
import com.ami.dao.MongoDBPatientDAO;
import com.ami.dao.MongoDBSugarDAO;
import com.ami.model.Food;
import com.ami.model.Patient;
import com.ami.model.SugarConsumed;
import com.mongodb.MongoClient;
import com.twilio.sdk.TwilioRestException;

import utility.SmsSender;

public class GetFoodDetailNutritionix {
	
	String barcode;
	Food f=new Food();
	String email;
	List<String> Allergy = new ArrayList<String>();
	
	List<String> Disease = new ArrayList<String>();
	Patient p;

	public GetFoodDetailNutritionix(String barcode, List<String> allergy, List<String> disease,String email,Patient p) {
		this.barcode=barcode;
		this.Allergy=allergy;
		this.Disease=disease;
		this.email=email;
		this.p=p;
	}
      Food foodFunction(){
    	  String sugars = " ";
  		 String sugarResult = "";
   	      String ingredientTrace = " ";
    	  f.setPatientAllergy(Allergy);
			f.setPatientDisease(Disease);
    	  MongoClient mongo = (MongoClient) WebAppContext.WEBAPP_CONTEXT.getAttribute("MONGO_CLIENT");
          MongoDBNutritionixDAO nutritionDAO = new MongoDBNutritionixDAO(mongo);
          if(!nutritionDAO.hasFood(barcode)){
			  f.setProductName("Product not found");
        	  return f;
          }
    	   
            f=nutritionDAO.getFood(barcode);
            System.out.println(f.getBrand());
           
			
			

			
          /*********************************************/
			/*****************
			 * Checking nutrition facts and sugars
			                                                    *****************/

			
					sugars = f.getNutriments();
				
					System.out.println("sugars"+sugars);
				

			

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
							if(!temp.getSugar().get(i1).equals(" "))
							sugarTotal += Float.parseFloat(temp.getSugar().get(i1));
						}
					}
					int year = Integer.parseInt(p.getBirthDate().substring(0, 4));
					if ((p.getGender().equals("Female") && (sugarTotal > 22) && (year < 1997))
							|| (p.getGender().equals("Male") && (sugarTotal > 36) && (year < 1997))
							|| ((sugarTotal > 12) && (year > 1997))) {

						sugarResult = "Exceeded the daily intake." + "\n" + " If you eat this " + f.getProductName()
								+ " your sugar intake will reach to " + sugarTotal + "g";
System.out.println("inside twillio");
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

			
				/*****************
				 * Checking Ingredients and generating allergy Result
				 *****************/
                    Boolean flag=false;
					String ingredientString = f.getIngredients();
                    if(!ingredientString.equals(null)){
                    	if (Allergy != null && Allergy.size() != 0) {
    						for (String a : Allergy) {

    							if ((org.apache.commons.lang3.StringUtils.containsIgnoreCase(ingredientString, a))
    									|| (org.apache.commons.lang3.StringUtils.containsIgnoreCase(ingredientTrace, a))) {
    								
    								flag=true;//break out of if statement
    								break;
    								
    							}
    						
    								else{
    									System.out.println("No Plese");
    									f.setAllergyResult("Safe to consume");
    								}
    						}
    							if (flag==true){
    								f.setAllergyResult("Don't consume you are allergic to it");
    						
    								System.out.println("I hv reached in this block");	/********************
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
    				else{
    					f.setAllergyResult("No information available");
    					
    				}
    			

    			System.out.println(f.toString());

    		
    		return f;

    	}

}
