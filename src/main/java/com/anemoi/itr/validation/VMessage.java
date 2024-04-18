package com.anemoi.itr.validation;

public class VMessage 
	{
	
	String category ;
	String variable ;
	int idx ;
	
	String value ;	
	boolean valid ;
	int severity ;
	String message ;
	String action ;
	
	public VMessage() { }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void show() {

		System.out.print(this.category + "," + this.variable + "," + this.value + "," + this.message + "," + this.action );
		
		} 
	
	
	
	}
