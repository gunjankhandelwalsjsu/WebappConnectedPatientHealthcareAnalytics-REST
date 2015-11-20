package com.ami.model;

import java.sql.Timestamp;
import java.util.List;

public class SugarConsumed {

	String email;
	public List<Timestamp> getTime() {
		return time;
	}
	public void setTime(List<Timestamp> time) {
		this.time = time;
	}
	List<Timestamp> time;
	public void setSugar(List<String> sugar) {
		this.sugar = sugar;
	}
	List<String> sugar;

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
	public List<String> getSugar() {
		// TODO Auto-generated method stub
		return sugar;
	}
	
	
	
}
