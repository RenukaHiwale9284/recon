package com.anemoi.data;

public class CurrCode 
	{
	int srNo ;	
	String code ;
	String name ;
	double rate ;
	
	public void show() {
		System.out.print("\nCurrency(" + srNo + "( " + code + "," + name + "," +rate) ;
// TODO Auto-generated method stub
		
	}
	
	
	public CurrCode()
	{
	}
	
	public CurrCode(int srno1, String code1, String name1, double rate1)
		{
		this.srNo = srno1 ;
		this.code= code1 ;
		this.name = name1 ;
		this.rate = rate1 ;
		}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	
	
	
	}
