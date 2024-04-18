package com.anemoi.data;

import java.io.Serializable;
import java.util.Date;

public class DtaaTaxRate implements Serializable
	{
	int countryCode ;	
	String country ;
	String nature ;
	
	double taxRate1 ;
	double taxRate2 ;

	public DtaaTaxRate(int cc, String cont, String nat, double rate1, double rate2)
		{
		this.countryCode = cc;
		this.country = cont ;
		this.nature = nat ; 
		this.taxRate1 = rate1 ;
		this.taxRate2 = rate2 ;
		}
	
	
	public DtaaTaxRate()
		{
		
		}
	
	public int getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public double getTaxRate1() {
		return taxRate1;
	}
	public void setTaxRate1(double taxRate1) {
		this.taxRate1 = taxRate1;
	}
	public double getTaxRate2() {
		return taxRate2;
	}
	public void setTaxRate2(double taxRate2) {
		this.taxRate2 = taxRate2;
	}

	
	
	}
