package com.anemoi.itr.validation;

public class Validator 
	{
	String command 		;
	String datatype 	;
	String execution ;
	String formula ;
	String desc ;
	
	public Validator()
		{
		
		}
	
	

	public Validator(String c, String dt, String exe, String formula1, String descr)
		{
		this.command = c ;
		this.datatype = dt;
		this.execution = exe ;
		this.formula = formula1 ;
		this.desc = descr ;
		}

	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getExecution() {
		return execution;
	}

	public void setExecution(String execution) {
		this.execution = execution;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String des) {
		this.desc = desc;
	}
	
	
	
	
	}
