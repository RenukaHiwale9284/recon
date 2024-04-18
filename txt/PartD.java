package com.anemoi.txt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.anemoi.pdf.PartyRecord;
import com.anemoi.pdf.Refund;
import com.anemoi.pdf.TDS;

public class PartD {

	int pc = 0 ;
	int tc = 0 ;
	ArrayList<Refund> refunds = new ArrayList<Refund>() ;

	
	private void readPartyTrans(String tuple) 
		{		
		System.out.print("\nAdding new Trans " + tuple);
		
		String[] w = tuple.split("\\^");
		int l = w.length ;
		System.out.print("\n\n" + w[0] + "," + w[1] + "," + w[2] + "," + w[3] + "," + w[4] + "," + w[5] );
				
		Refund tt = new Refund() ;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				
		tt.setSrNo( Integer.parseInt(w[0]));
		tt.setAssessmentYear(w[1]);
		tt.setMode(w[2].trim());
		tt.setIssued(w[3]);
		tt.setNature(w[4]);
		try {
			tt.setAmountOfRefund(Double.parseDouble(w[5]) );
			}
		catch(Exception ex)
			{
			ex.printStackTrace();
			}

		try {
			tt.setAmountOfRefund(Double.parseDouble(w[6]) );
			}
		catch(Exception ex)
			{
			ex.printStackTrace();
			}

		
		tt.setRemarks(w[7]);
		tt.show("::");
		refunds.add(tt);
		}
	
	
public void parse(String line)
	{	
	if(line.startsWith( (tc+1) + "^"  )) 
		{
		tc++;
		readPartyTrans(line);
		}    	
	}


public ArrayList<Refund> getRefunds() {
	return refunds;
}


public void setRefunds(ArrayList<Refund> refunds) {
	this.refunds = refunds;
}

	
}
