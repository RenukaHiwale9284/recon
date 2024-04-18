package com.anemoi.recon;

public class ReconMath 
	{
	
	static public double sum(double[] num)
		{
		double total = 0;
		for ( int idx = 0 ; idx < num.length ; idx++)
			{
			total += num[idx];
			}
		return total ;
		}	
	
	
	static public boolean numEqual(double d1, double d2, double precision )
		{
		if ( Math.abs( d1 - d2 ) < precision  ) 
			return true ;
		else 
			return false ;
		}
	
	}
