package com.ami.model;

import java.util.List;

public class Food {
String productName;
String sugarResult;
public String getSugarResult() {
	return sugarResult;
}
public void setSugarResult(String sugarResult) {
	this.sugarResult = sugarResult;
}
public String getProductName() {
	return productName;
}
public void setProductName(String productName) {
	this.productName = productName;
}
public String getBrand() {
	return brand;
}
public void setBrand(String brand) {
	this.brand = brand;
}
public List<String> getPatientAllergy() {
	return PatientAllergy;
}
public void setPatientAllergy(List<String> patientAllergy) {
	PatientAllergy = patientAllergy;
}
public List<String> getPatientDisease() {
	return PatientDisease;
}
public void setPatientDisease(List<String> patientDisease) {
	PatientDisease = patientDisease;
}
public String getNutriments() {
	return nutriments;
}
public void setNutriments(String nutriments) {
	this.nutriments = nutriments;
}
public String getAllergyResult() {
	return AllergyResult;
}
public void setAllergyResult(String allergyResult) {
	AllergyResult = allergyResult;
}
String brand;
List<String> PatientAllergy;
List<String> PatientDisease;
String nutriments;
String AllergyResult;
@Override
public String toString() {
	return "Food [productName=" + productName + ", brand=" + brand + ", PatientAllergy=" + PatientAllergy
			+ ", PatientDisease=" + PatientDisease + ", nutriments=" + nutriments + ", AllergyResult=" + AllergyResult
			+ "]";
}
}
