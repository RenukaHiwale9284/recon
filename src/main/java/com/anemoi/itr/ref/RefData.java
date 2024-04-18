package com.anemoi.itr.ref;

import java.io.Serializable;

public class RefData implements Serializable
	{
	double itrVersion ;
	String tag ;
	String key ;
	String value ;
	
	public RefData()
	{
		
	}
	
	public RefData(String tag2, String key2, String value2) {
		tag = tag2 ;
		key = key2 ;
		value = value2 ;
		}

	public double getItrVersion() {
		return itrVersion;
	}
	public void setItrVersion(double itrVersion) {
		this.itrVersion = itrVersion;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	}
