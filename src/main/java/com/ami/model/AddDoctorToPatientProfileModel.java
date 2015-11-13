package com.ami.model;

/**
 * Created on 11/8/2015, 8:16 PM
 * AddDoctorToPatientProfileModel.java
 *
 * @author akhandelwal
 */
public class AddDoctorToPatientProfileModel {

    String patienId;
    String doctorId;

    public String getPatienId() {
        return patienId;
    }

    public void setPatienId(String patienId) {
        this.patienId = patienId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
