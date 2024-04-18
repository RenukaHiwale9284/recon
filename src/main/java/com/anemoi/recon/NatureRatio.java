package com.anemoi.recon;

public class NatureRatio 	
	{
	int idx ;
	
	String incomeType ;
	
	String nature ;
	
	double ratio ;

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public int hashCode()
		{
		return idx * incomeType.hashCode() ;
		}	
	}
