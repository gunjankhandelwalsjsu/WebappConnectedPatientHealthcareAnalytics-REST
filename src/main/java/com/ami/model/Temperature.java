package com.ami.model;

import java.sql.Timestamp;
import java.util.List;

public class Temperature {

	String email;
	public List<Timestamp> getTime() {
		return time;
	}
	public void setTime(List<Timestamp> time) {
		this.time = time;
	}
	List<Timestamp> time;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	String Id;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Double> getTemp() {
		return temp;
	}
	public void setTemp(List<Double> temp) {
		this.temp = temp;
	}
	List<Double> temp;
	
}
