package com.ami.model;

/**
 * Created on 11/8/2015, 8:16 PM
 * AddDoctorToPatientProfileModel.java
 *
 * @author akhandelwal
 */
public class AddDoctorToPatientProfileModel {

    String patientEmail;
    public String getPatientEmail() {
		return patientEmail;
	}
	public void setPatientEmail(String patienEmail) {
		this.patientEmail = patienEmail;
	}
	public String getDoctorEmail() {
		return doctorEmail;
	}
	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}
	String doctorEmail;

   
}
