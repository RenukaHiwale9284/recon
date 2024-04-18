package com.anemoi.data;

import java.io.Serializable;
import java.util.Date;

public class SalesRecord implements Serializable
	{
	// 	S.No.	Party Name	Invoice Number	Invoice Date 	
	//	Invoice Amount	Amount of Taxes withheld	Billing Currency

	int projectId ;	

	int srNo ;	
	String partyName ;	
	String invoiceNumber ;	
	Date invoiceDate ;	
	double invoiceAmount ;	
	String tax ;	
	String currency ;
	
	double orgInvoiceAmount ;
	
	public SalesRecord()
	{
		
	}
	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public void show(String tag)
		{
		System.out.print("\nSR[" + tag+"]= " + srNo +"," + partyName + "," + invoiceNumber 
			+ "," + invoiceDate + "," + invoiceAmount + "," + tax + "," + currency);	
		}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}


	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getOrgInvoiceAmount() {
		return orgInvoiceAmount;
	}

	public void setOrgInvoiceAmount(double orgInvoiceAmount) {
		this.orgInvoiceAmount = orgInvoiceAmount;
	}
	
	
	
	
	}
