package com.ami.model;

/**
 * Created on 11/8/2015, 4:05 PM
 * Login.java
 *
 * @author akhandelwal
 */
public class Login {

    String email;
    String password;
    boolean isPatient=true;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setIsPatient(boolean isPatient) {
        this.isPatient = isPatient;
    }
}
