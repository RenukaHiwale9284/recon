package com.anemoi.itr.validation;

import java.util.Date;

public abstract class  GenValidator 
	{		
	Validator data ;
	
	
	abstract boolean validate(String value) ;
	abstract boolean validate(double value) ;
	abstract boolean validate(Date dd) ;
	abstract boolean validate(int number);
	public Validator getData() {
		return data;
	}
	public void setData(Validator data) {
		this.data = data;
	}	
	
	
	}
