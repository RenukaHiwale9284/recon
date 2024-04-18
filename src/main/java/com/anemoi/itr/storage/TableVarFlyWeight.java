package com.anemoi.itr.storage;

import java.io.Serializable;

import com.anemoi.itr.ListTemplate;

public class TableVarFlyWeight implements Serializable
	{
	int list_itemid ;
	String var_name ;
	String tag ;
	String description ;
	String data_type ; 
	String col_index ;
	String validations ; 
		
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getList_itemid() {
		return list_itemid;
	}

	public void setList_itemid(int list_itemid) {
		this.list_itemid = list_itemid;
	}

	ListTemplate list_data ;
	
	public TableVarFlyWeight() {}

	public String getVar_name() {
		return var_name;
	}

	public void setVar_name(String var_name) {
		this.var_name = var_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ListTemplate getList_data() {
		return list_data;
	}

	public void setList_data(ListTemplate list_data) {
		this.list_data = list_data;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getCol_index() {
		return col_index;
	}

	public void setCol_index(String col_indeX) {
		this.col_index = col_indeX;
	}

	public String getValidations() {
		return validations;
	}

	public void setValidations(String validations) {
		this.validations = validations;
	} 
	
	public void show()
		{
		System.out.print("\nFlyWeight :: " + list_itemid + "," + var_name + "," + description + "," + data_type + "," + col_index + "," + validations  );
		}
	
	}
