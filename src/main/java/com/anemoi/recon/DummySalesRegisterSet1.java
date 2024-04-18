package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;

public class DummySalesRegisterSet1 implements IncomeDocument {

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
	
	
	public DummySalesRegisterSet1()
		{		
		parties.add("TATA Tele Ltd");
		data.put("TATA Tele Ltd", 128.0);
		
		parties.add("TITAN Industries");
		data.put("TITAN Industries", 150.0);		

		parties.add("Anemoi Robotics");
		data.put("Anemoi Robotics", 40.0);				
		}

	@Override
	public double getTds(String pName) {
		return 0;
	}

}
