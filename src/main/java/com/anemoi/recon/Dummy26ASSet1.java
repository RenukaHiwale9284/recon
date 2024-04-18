package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;

public class Dummy26ASSet1 implements IncomeDocument {

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
	
	public Dummy26ASSet1()
		{		
		parties.add("TATA Tele");
		data.put("TATA Tele", 120.0);
		tds.put("TATA Tele", 20.0);

		
		parties.add("TITAN Industries");

		data.put("TITAN Industries", 150.0);				
		tds.put("TITAN Industries", 30.0);		

		
		parties.add("Anemoi Robotics");

		data.put("Anemoi Robotics", 170.0);				
		tds.put("Anemoi Robotics", 30.0);		
		
		
		}

	@Override
	public double getTds(String pName) {		
		return tds.get(pName);		
	}
}
