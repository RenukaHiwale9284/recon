package com.anemoi.itr;

import java.io.Serializable;

public class TableConfiguration implements Serializable{

	int projectId;
	String tab;

	String tableName;
	int length;
	int offset;
	int data_length;
	String enable = "true";

	public TableConfiguration()
	{
		
	}
	
	public TableConfiguration(String tab1, String table_Name1, int length1, int offset1, int data_len, String enable1) {
		tab = tab1;
		tableName = table_Name1;
		length = length1;
		this.setOffset(offset1);
		data_length = data_len;
		enable = enable1;
	}

	public void show(String tag) {

		System.out.print("\nTableOffset (" + tag + ") :: " + tab + "," + tableName + "," + length + "," + offset + "," 
				+ data_length + "," + enable + ", pid=" + this.projectId);
	}

	public void recalOffset()
		{	
		offset = data_length - length ;
		
		if(offset <= 0)
			{
			offset = 0 ;
			}
		}
	
	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int of) {
		this.offset = of;
		
		if(offset <= 0)
			{
			offset = 0 ;
			}
		 
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int pid) {
		this.projectId = pid;
	}

	public int getData_length() {
		return data_length;
	}

	public void setData_length(int data_length) {
		this.data_length = data_length;
		
	}

	public String isEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public void reset() {
		this.offset = this.data_length-this.length ;
		
		if(offset <= 0 )
			offset = 0 ;		
	}

}
