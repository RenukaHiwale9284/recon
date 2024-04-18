package com.anemoi.tqm;

import java.util.ArrayList;
import java.util.Date;

import com.anemoi.recon.ReferenceDataUtiity;
import com.anemoi.recon.TaxMaster;
import com.anemoi.trac.LogElement;

public class TaxComp
	{
	int projectId ;
	
	String nature ;
	double income ;
	double tdsDeposited ;
	String category ;
	String act ;
	String country ;
	String section ;
	double taxRate ;
	double taxAmount ;
	
	ArrayList<LogElement> changeLog ;
	
	public void modifyCatetory(String cat, String act1, String sect1)
		{
		this.category = cat ;
		this.act = act1 ;
		this.section = sect1 ;
		}
	
	public void modifyRate(double rr, String reason)
		{
		LogElement le = new LogElement(new Date(), "" , "Modification of Tax Rate", "TaxComp", nature , "Modified from" + this.taxRate + " to " + rr); 
		this.changeLog.add(le);
		this.taxRate = rr ;
		}
	
	
	public TaxComp(String n, double inc, double tds, String cat, String act1, String section1, double taxr, double taxamt1, String cntr)
		{
		nature = n ;
		income = inc ;
		tdsDeposited = tds ;
		category = cat ;
		act = act1 ; 
		section = section1;
		taxRate = taxr ;
		taxAmount = taxamt1;
		country = cntr ;
		}

	public TaxComp() {
		// TODO Auto-generated constructor stub
	}

	public double compute() {		
		
		this.taxAmount  = 0 ;
		if( TaxMaster.isTaxableCategory(this.category))
			{			
			this.taxAmount = ( this.income * this.taxRate ) - this.tdsDeposited ;
			}
		this.show("Tax Computation");
		return this.taxAmount ;
		}

	
	public void show(String tag) {
		
		System.out.print("\nTaxComp[" + tag + "] = " + nature + "," + income + "," + tdsDeposited + "," + category + "," + act + "," + section + "," + taxRate+ "," + country  ) ;
		
	}	
	
	
	public String validate() {
		
		String msg = "" ;
		
		String refid = "taxable-nature-of-amount" ;
		if( ReferenceDataUtiity.isEnum(refid, nature))
			{
			msg = msg + nature + " is not " + refid  + ",";
			}
		
		refid = "amount-category" ;		
		if( ReferenceDataUtiity.isEnum(refid, category))
			{
			msg = msg + category + " is not " + refid  + ",";
			}

		 refid = "income-tax-act" ;
		if( ReferenceDataUtiity.isEnum(refid, act))
			{
			msg = msg + act + " is not " + refid  + ",";
			}

		 refid = "income-tax-section" ;
		if( ReferenceDataUtiity.isEnum(refid, section))
			{
			msg = msg + section + " is not " + refid  + ",";
			}

		return act;
		
		
		}


	public int getProjectId() {
		return projectId;
	}


	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}


	public String getNature() {
		return nature;
	}


	public void setNature(String nature) {
		this.nature = nature;
	}


	public double getIncome() {
		return income;
	}


	public void setIncome(double income) {
		this.income = income;
	}


	public double getTdsDeposited() {
		return tdsDeposited;
	}


	public void setTdsDeposited(double tdsDeposited) {
		this.tdsDeposited = tdsDeposited;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getAct() {
		return act;
	}


	public void setAct(String act) {
		this.act = act;
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


	public double getTaxAmount() {
		return taxAmount;
	}


	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}





	
	
	
	
	}
