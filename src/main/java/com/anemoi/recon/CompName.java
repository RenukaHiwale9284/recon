package com.anemoi.recon;

public class CompName 
	{
	int nameid ;
	
	String name26as = "-";	
	String namesr = "-";	
	String namertp = "-";

	CompName(int nid)
		{
		nameid = nid;
		}
		
	
	public int getNameid() {
		return nameid;
	}

	public void setNameid(int nameid) {
		this.nameid = nameid;
	}

	public String getName26as() {
		return name26as;
	}

	public void setName26as(String name26as) {
		this.name26as = name26as;
	}

	public String getNamesr() {
		return namesr;
	}

	public void setNamesr(String namesr) {
		this.namesr = namesr;
	}

	public String getNamertp() {
		return namertp;
	}

	public void setNamertp(String namertp) {
		this.namertp = namertp;
	}


	public boolean isMapped() {
		
		if(this.name26as.equals("-")) return false ;
		if(this.namertp.equals("-")) return false ;
		if(this.namesr.equals("-")) return false ;
			
		return true;
		}
	
	
	}
