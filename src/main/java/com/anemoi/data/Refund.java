package com.anemoi.data;

import java.io.Serializable;
import java.util.Date;

public class Refund implements Serializable{

	int projectId ;
	
	 int srNo;
	 // 1

	 String assessmentYear ;
	 //2018-19
	 
	 String mode ;	 
	 //ECS                 
	 
	 String issued ;
	 //-
	 
	 String nature ;
	 //PAN
	 
	 double amountOfRefund ;
	 //^64787.0^
	 
	 double interest ;
 	 //90950.0
	 
	 Date dateOfRefund ;
	 //04-Mar-2020
	 
	 String remarks ;
	 

			 
	 
	 
	 
	 
	 
	public int getProjectId() {
		return projectId;
	}








	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}








	public void show(String tag) {
		System.out.print("\nREFUND[" + tag  + "] = " + srNo + "," + assessmentYear + "," + mode + "," + issued + "," + nature + ","
				+ amountOfRefund + "," + interest + "," + dateOfRefund + "," + remarks);

	}








	public int getSrNo() {
		return srNo;
	}








	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}








	public String getAssessmentYear() {
		return assessmentYear;
	}








	public void setAssessmentYear(String assessmentYear) {
		this.assessmentYear = assessmentYear;
	}








	public String getMode() {
		return mode;
	}








	public void setMode(String mode) {
		this.mode = mode;
	}








	public String getIssued() {
		return issued;
	}








	public void setIssued(String issued) {
		this.issued = issued;
	}








	public String getNature() {
		return nature;
	}








	public void setNature(String nature) {
		this.nature = nature;
	}








	public double getAmountOfRefund() {
		return amountOfRefund;
	}








	public void setAmountOfRefund(double amountOfRefund) {
		this.amountOfRefund = amountOfRefund;
	}








	public double getInterest() {
		return interest;
	}








	public void setInterest(double interest) {
		this.interest = interest;
	}








	public Date getDateOfRefund() {
		return dateOfRefund;
	}








	public void setDateOfRefund(Date dateOfRefund) {
		this.dateOfRefund = dateOfRefund;
	}








	public String getRemarks() {
		return remarks;
	}








	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


}
