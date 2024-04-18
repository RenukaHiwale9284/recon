package com.anemoi.data;

import java.sql.Timestamp;

public class Trail 
	{
	long trailId ;
	Timestamp timestamp ;
	int projectId ;
	int projectStage ;
	String user ;
	String msg ;
	String msgType ;
	String details ;
	long sessionId ;
	long threadId ;
	String systemState ;
	
	public Trail()
		{
		;
		}
	
	
	
	public long getTrailId() {
		return trailId;
	}



	public void setTrailId(long trailId) {
		this.trailId = trailId;
	}



	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp2) {
		this.timestamp = timestamp2;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getProjectStage() {
		return projectStage;
	}

	public void setProjectStage(int projectStage) {
		this.projectStage = projectStage;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSystemState() {
		return systemState;
	}

	public void setSystemState(String systemState) {
		this.systemState = systemState;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}


	
	
	
	
	}
