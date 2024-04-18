package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;

public class DummyRPT1 implements IncomeDocument {

	ArrayList<String> parties = new ArrayList<String>();  
	HashMap<String,Double> data = new HashMap<String,Double>();
			
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
	
	public DummyRPT1()
		{		
		parties.add("TATA tele Ltd");
		data.put("TATA tele Ltd", 130.0);
		
		parties.add("TITAN");
		data.put("TITAN", 160.0);		

		parties.add("Anemoi Technologies");
		data.put("Anemoi Technologies", 170.0);		
		}

	@Override
	public double getTds(String pName) {
		return 0;
	}
}
