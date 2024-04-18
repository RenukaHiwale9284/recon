package com.anemoi.recon;

public class TaxMaster {
	
	public static final String[] taxableCategories = { "Income" , "Extra-Income"} ;
	
	public static boolean isTaxableCategory(String sc)
		{
		for( int idx = 0 ; idx < taxableCategories.length ; idx++ )
			{
			if (taxableCategories[idx].equals(sc)) return true ;
			}
		return false ;
		}
}
