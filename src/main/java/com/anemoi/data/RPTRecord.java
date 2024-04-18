package com.anemoi.data;

import java.io.Serializable;
import java.util.Date;

public class RPTRecord implements Serializable
	{
	int projectId ;	
	
	int srNo ;	
	String partyName ;	
	double amount ;

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void show(String tag) {

		System.out.print("\nRTP[" + tag + "]=" + this.projectId +"," + srNo + "," + this.partyName + "," + this.amount);
		}
	
	
	}
