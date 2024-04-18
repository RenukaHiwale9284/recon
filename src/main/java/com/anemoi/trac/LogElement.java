package com.anemoi.trac;

import java.util.Date;

public class LogElement 
	{
	int projectId ;
	Date timestamp ;
	String usr ;
	String usrActivity ;	
	String elementClass ;
	String elementId ;	
	String msg ;
		
	
	public LogElement(Date timestamp, String usr, String usrActivity, String elementClass, String elementId, String msg) {
		super();
		this.timestamp = timestamp;
		this.usr = usr;
		this.usrActivity = usrActivity;
		this.elementClass = elementClass;
		this.elementId = elementId;
		this.msg = msg;
		}
	}
