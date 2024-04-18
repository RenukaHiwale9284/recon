package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;

public class ValueMapDocument implements  IncomeDocument{

	
	ArrayList<String> parties = new ArrayList<String>();  
	HashMap<String,Double> data = new HashMap<String,Double>();
	HashMap<String,Double> tds = new HashMap<String,Double>();

	
	@Override
	public ArrayList<String> listAllParties() {
		return parties; 		
		}

	@Override
	public double getIncome(String pName) {
		if(!data.containsKey(pName))
			return 0 ;
		
		return data.get(pName);
	}
	
	public ValueMapDocument(HashMap<String,Double> records, ArrayList<String> dataids)
		{		
		this.data = records   ;
		this.parties = dataids ;
		}

	public ValueMapDocument(HashMap<String,Double> records1, ArrayList<String> dataids, HashMap<String,Double> records2)
		{		
		this.data = records1   ;
		this.tds =  records2 ;
		this.parties = dataids ;
		}
	
	@Override
	public double getTds(String pName) {		
		return tds.get(pName);		
	}

}
