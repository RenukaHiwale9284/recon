package com.anemoi.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proj implements Serializable {

	int srNo;
	int projectId;
	String name;
	String pan;
	String groupCompany;
	String precisionId;
	String activity;
	int stage = 0;
	String year;
	String clientField1;
	String clientField2;
	String secret;
	String encrSecret ;
	String users ;
	Timestamp lockedTill ;
	String lockedBy ;
	int attempts = 0 ;

	public Proj()
		{
		lockedTill = new Timestamp(System.currentTimeMillis());
		}
	
	public void show(String tag) {
		System.out.print("\nProject[" + projectId + "]=" + name + ",pan=" + pan + ",group=" + ",precisionId="
				+ precisionId + ",activity=" + activity + ",year=" + year + "," + secret);
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getGroupCompany() {
		return groupCompany;
	}

	public void setGroupCompany(String groupCompany) {
		this.groupCompany = groupCompany;
	}

	public String getPrecisionId() {
		return precisionId;
	}

	public void setPrecisionId(String precisionId) {
		this.precisionId = precisionId;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getClientField1() {
		return clientField1;
	}

	public void setClientField1(String clientField1) {
		this.clientField1 = clientField1;
	}

	public String getClientField2() {
		return clientField2;
	}

	public void setClientField2(String clientField2) {
		this.clientField2 = clientField2;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public String getEncrSecret() {
		return encrSecret;
	}

	public void setEncrSecret(String encrSecret) {
		this.encrSecret = encrSecret;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public Timestamp getLockedTill() {
		return lockedTill;
	}

	public void setLockedTill(Timestamp lockedTill) {
		this.lockedTill = lockedTill;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	
	
}
