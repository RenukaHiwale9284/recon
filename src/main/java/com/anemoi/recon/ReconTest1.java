package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class ReconTest1
	{	
	public Recon getRecon()
		{
		Recon recon = new Recon() ;
		
		IncomeDocument as26 = null , salesRegister = null , rtp = null;
		
		as26 = new Dummy26ASSet1();	
		salesRegister = new DummySalesRegisterSet1();	
		rtp = new DummyRPT1();	
		
		// ------------------------------------------------------------
		// Attach the primary source as 26AS.
		recon.attachPrimarySource(as26) ;	
		recon.attachTdsSource(as26);
		recon.show();
		recon.umap.show();

		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: SR  -------------------------- " );		
		// Verify the party names from the Sales register
		ArrayList<String> unknowSR = recon.unIdentifiedPartyNames(salesRegister) ; 
		System.out.print("\nUnknown Party Names from SalesRegister:: " + unknowSR);
				
		HashMap<String, String> namePairs = new HashMap<String, String>() ;
		namePairs.put("TATA Tele Ltd", "TATA Tele");		
		recon.umap.addNamePairs(namePairs) ;
	
		ArrayList<String> pendingNames = new ArrayList<String>() ;
		pendingNames.add("Anemoi Robotics");
		recon.umap.newNames(pendingNames) ;
			
		// Attach the source as SalesRegister		
		recon.attachSource(salesRegister, 2);
		recon.show();
		recon.umap.show();
		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: RTP  -------------------------- " );
		// Verify the party names from the Sales register
		ArrayList<String> unknowRTP = recon.unIdentifiedPartyNames(rtp) ;
		System.out.print("\nUnknown Party Names from RTP:: " + unknowRTP);

		HashMap<String, String> namePairs2 = new HashMap<String, String>() ;
		namePairs2.put("TATA tele Ltd", "TATA Tele");		
		namePairs2.put("TITAN", "TITAN Industries");	
		namePairs2.put("Anemoi Technologies", "Anemoi Robotics");	
		
		recon.umap.addNamePairs(namePairs2) ;
	
		ArrayList<String>    pendingNames2 = new ArrayList<String>() ;
		recon.umap.newNames( pendingNames2) ;
		recon.attachSource(  rtp, 3);
		
		recon.show();
		recon.umap.show();
		return recon ;
		}
	
	public static void main(String[] argv) throws Exception
		{
		Recon recon = new Recon() ;
		
		IncomeDocument as26 = null , salesRegister = null , rtp = null;
		
		as26 = new Dummy26ASSet1();	
		salesRegister = new DummySalesRegisterSet1();	
		rtp = new DummyRPT1();	
		
		// ------------------------------------------------------------
		// Attach the primary source as 26AS.
		recon.attachPrimarySource(as26) ;	
		recon.attachTdsSource(as26);
		recon.show();
		recon.umap.show();

		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: SR  -------------------------- " );		
		// Verify the party names from the Sales register
		ArrayList<String> unknowSR = recon.unIdentifiedPartyNames(salesRegister) ; 
		System.out.print("\nUnknown Party Names from SalesRegister:: " + unknowSR);
				
		HashMap<String, String> namePairs = new HashMap<String, String>() ;
		namePairs.put("TATA Tele Ltd", "TATA Tele");		
		recon.umap.addNamePairs(namePairs) ;
	
		ArrayList<String> pendingNames = new ArrayList<String>() ;
		pendingNames.add("Anemoi Robotics");
		recon.umap.newNames(pendingNames) ;
			
		// Attach the source as SalesRegister		
		recon.attachSource(salesRegister, 2);
		recon.show();
		recon.umap.show();
		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: RTP  -------------------------- " );
		// Verify the party names from the Sales register
		ArrayList<String> unknowRTP = recon.unIdentifiedPartyNames(rtp) ;
		System.out.print("\nUnknown Party Names from RTP:: " + unknowRTP);

		HashMap<String, String> namePairs2 = new HashMap<String, String>() ;
		namePairs2.put("TATA tele Ltd", "TATA Tele");		
		namePairs2.put("TITAN", "TITAN Industries");	
		namePairs2.put("Anemoi Technologies", "Anemoi Robotics");	
		
		recon.umap.addNamePairs(namePairs2) ;
	
		ArrayList<String>    pendingNames2 = new ArrayList<String>() ;
		recon.umap.newNames( pendingNames2) ;
		recon.attachSource(  rtp, 3);
		
		recon.show();
		recon.umap.show();

		// ------------------------------------------------------------
		System.out.print("\n\nRecon step 1 :: -------------------------- " );
		
		
		int partyId = 2 	;
		int transCount = 3 	;
		String priNature = "global" ;
		String[] natures = {"Royalty-non-taxable","Royalty-taxable","Technical-taxable"} ;
		double[] ratio = {0.3, 0.3, 0.4} ;
		String msg = recon.splitAmount(partyId, priNature , natures ,ratio ) ;
		System.out.print("\nMSG :: " + msg);
		recon.show();
		
		
		// ------------------------------------------------------------		

		int partyId2 = 1 	;
		int transCount2 = 2 	;
		String priNature2 = "global" ;
		String[] natures2 = {"Royalty-non-taxable","Royalty-taxable"} ;
		double[] ratio2 = {0.2, 0.8} ;
		String msg2 = recon.splitAmount(partyId2, priNature2 , natures2 ,ratio2 ) ;
		System.out.print("\nMSG :: " + msg2);
		recon.show();
		
		
		// ------------------------------------------------------------		

		int partyId3 = 1 ;
		double amount = 50 ;
		String reason = "something special" ;
		int reconPriority = 1 ;
//		recon.carryForward(partyId3, amount, reason, reconPriority);
		recon.show();		
		
		
		
		if(true) return ;
		
		
		}
	}
