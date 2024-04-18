package com.anemoi.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyRecord  implements Serializable {

	int projectId ;
	int sr_no ;
	String partyName = "";
	String tan ;
	double tac ; // total amount credited
	double ttd ; // total tax deducted 	
	double tdsdeposited ;// total tds deposited;
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getSr_no() {
		return sr_no;
	}
	public void setSr_no(int sr_no) {
		this.sr_no = sr_no;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public String getTan() {
		return tan;
	}
	public void setTan(String tan) {
		this.tan = tan;
	}
	public double getTac() {
		return tac;
	}
	public void setTac(double tac) {
		this.tac = tac;
	}
	public double getTtd() {
		return ttd;
	}
	public void setTtd(double ttd) {
		this.ttd = ttd;
	}
	public double getTdsdeposited() {
		return tdsdeposited;
	}
	public void setTdsdeposited(double tdsdeposited) {
		this.tdsdeposited = tdsdeposited;
	}
	
	public void addTrans(TDS tt) {
		// TODO Auto-generated method stub
		
	}
	public void show(String tag) {
		// TODO Auto-generated method stub
		
	}
	

}
