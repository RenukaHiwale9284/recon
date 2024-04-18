package com.anemoi.recon;

import java.util.ArrayList;

public interface IncomeDocument {

	public ArrayList<String> listAllParties() 	;

	public double getIncome(String pName)		;

	public double getTds(String pName) 			;
}
