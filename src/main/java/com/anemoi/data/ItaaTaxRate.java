package com.anemoi.data;

import java.io.Serializable;
import java.util.Date;

public class ItaaTaxRate implements Serializable
	{
	int srNo ;	
	String nature ;
	String section ;
	double taxRate ;
	
	public void show() {
		System.out.print("\nItaa(" + srNo + "( " + nature + "," + section + "," +taxRate) ;
// TODO Auto-generated method stub
		
	}
	
	public ItaaTaxRate(int srno, String nat, String sec, double rate)
	{
		this.srNo = srno ;
		this.nature = nat ;
		this.section = sec ;
		this.taxRate = rate ;
	}
	
	
	public ItaaTaxRate()
	{
		
	}
	
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public double getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}


	
	
	
	
	}
