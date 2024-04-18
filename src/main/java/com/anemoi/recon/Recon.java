package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.stereotype.Component;

import com.anemoi.tqm.TaxPosition;


@Component
@ManagedBean
@ApplicationScoped
public class Recon 
	{	
	double precision = 0.1 ;

	ArrayList<IncomeAmount> original = new ArrayList<IncomeAmount>() ;
	
	ArrayList<IncomeAmount> carryForward = new ArrayList<IncomeAmount>();
	
	UniqueNameMap umap = new UniqueNameMap() ;	
	
	public ArrayList<IncomeAmount> getOriginal() {
		return original;
		}


	public void setOriginal(ArrayList<IncomeAmount> original) {
		this.original = original;
	}


	public ArrayList<IncomeAmount> getCarryForward() {
		return carryForward;
	}


	public void setCarryForward(ArrayList<IncomeAmount> carryForward) {
		this.carryForward = carryForward;
	}


	public UniqueNameMap getUmap() {
		return umap;
	}

	

	public void setUmap(UniqueNameMap umap) {
		this.umap = umap;
	}

	
	public String reconclied()
		{
		StringBuffer sbf = new 	StringBuffer();
		boolean valid = true ;
		for(IncomeAmount aa : original)
			{
			ArrayList<Double> validValues = new ArrayList<Double>() ;

			validValues.add(aa.source1Amount) ;
			validValues.add(aa.source2Amount) ;
			validValues.add(aa.source3Amount) ;

			
			double curVal = 0 ;
			for(Double incometemp : validValues)
				{
				if( ReconMath.numEqual(incometemp, 0 , precision ) ) continue ;
				
				if ( curVal == 0 ) 
					{
					curVal = incometemp ; 
					continue ;
					}

				if( !ReconMath.numEqual(incometemp, curVal , precision ) ) 
					{
					valid = false ;
					sbf.append("Recon Failed :" + aa.getPartyName() + "\n") ;
					continue ;
					}
				}
			}
			
			return sbf.toString() ;
		}	
	
	
	

	public boolean reconcliedComplete()
		{
		double precision = 0.1 ;
				
		for(IncomeAmount aa : original)
			{
			
			double v1 = aa.source1Amount ;						
			
			if( ReconMath.numEqual(v1, 0 , precision ) ) 
				{
				System.out.print("\nRecon comp : 26as is zero " + v1);
				return false ;
				}
			
			
			double v2 = aa.source2Amount ;
			
			if( !ReconMath.numEqual(v2, 0 , precision )  &&  !ReconMath.numEqual(v2, v1, precision ) ) 
				{
				System.out.print("\nRecon comp : SR is not equal to 26as " + v1 + "," + v2);
				return false ;
				}

									
			double v3 = aa.source3Amount ;
			if( !ReconMath.numEqual(v3, 0 , precision ) && !ReconMath.numEqual(v3, v1, precision ) )  
				{
				System.out.print("\nRecon comp : RPT is not equal to 26as " + v1 + "," + v3);
				return false ;
				}
			
			}
		
		return true ;
		}	
	
	
	public HashMap<String, TaxPosition> retriveTaxation()
		{		
		HashMap<String, TaxPosition> taxation = new HashMap<String, TaxPosition>();
		
		int idx = 1 ;
		for ( IncomeAmount amt : original)
			{
			if( !taxation.containsKey(amt.nature))
				{
				TaxPosition tt = new TaxPosition();

				// TaxComp t1 = new TaxComp("Royalty-non-taxable", 4120032.42, 19321.21, "Income" , "Non taxable", "section231" , 0.12, 332.31, "IND" );
				
				tt.setId(idx++);
				tt.setName(amt.nature);			
				tt.setCode(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9));
				
				taxation.put(amt.nature, tt);		
				tt.show();
				}
			
			
			TaxPosition tt = taxation.get(amt.nature);			
			tt.setAmount( amt.finalIncome + tt.getAmount() ) ;
			tt.setTds( amt.finalTax + tt.getTds() ) ;
			}
				
		return taxation;
		}
	
	
	public void init(int projectId)
		{
		System.out.print("\n Initializing the Recon :: " + projectId );
		}
	
	
	
	public static void main(String[] argv) throws Exception
		{
		Recon recon = new Recon() ;
		
		IncomeDocument as26 = null , salesRegister = null , rtp = null;
				
		// Attach the primary source as 26AS.
		recon.attachPrimarySource(as26) ;	
		
		// Verify the party names from the Sales register

		HashMap<String, String> namePairs = new HashMap<String, String>() ;
		recon.umap.addNamePairs(namePairs) ;

		ArrayList<String> pendingNames = new ArrayList<String>() ;
		recon.umap.newNames(pendingNames) ;

		
		// Attach the source as SalesRegister		
		recon.attachSource(salesRegister, 2);
				
		// Verify the party names from the Sales register
//		ArrayList<String> unknowRTP = recon.unIdentifiedPartyNames(rtp) ;
		
		HashMap<String, String> namePairs2 = new HashMap<String, String>() ;
		recon.umap.addNamePairs(namePairs2) ;

		ArrayList<String>    pendingNames2 = new ArrayList<String>() ;
		recon.umap.newNames( pendingNames2) ;
		recon.attachSource(  rtp, 3);
		
		int partyId = 2 	;
		int transCount = 3 	;
		String priNature = "global" ;
		String[] natures = {"Royalty-non-taxable","Royalty-taxable","Technical-taxable"} ;
		double[] ratio = {0.2, 0.3, 0.4, 0.1} ;
		recon.splitAmount(partyId, priNature , natures ,ratio ) ;
		}
	

	
	public IncomeAmount getIncomeAmount(int partyId, String nature)
		{
		for( IncomeAmount amt : this.original)
			{
			if(amt.nature.equals(nature) && amt.partyId ==  partyId)
				{
				return amt ;
				}
			}
		return null ;
		}	
	
	
	public ArrayList<IncomeAmount> retrieve(int partyId) 
		{
		System.out.print("\nIncome retrieve :: " + partyId);
		ArrayList<IncomeAmount> list = new ArrayList<IncomeAmount>() ;

		for(int idx = 0 ; idx < this.original.size() ; idx++)
			{
			IncomeAmount amt = original.get(idx);
			
			if(amt.partyId == partyId)
				list.add(amt);
			}
		return list ;		
		}
	
	public IncomeAmount retrieveSingle(int partyId, String incomeType) 
		{
		System.out.print("\nIncome retrieve :: " + partyId + "," + incomeType);
		
		for(int idx = 0 ; idx < this.original.size() ; idx++)
			{
			IncomeAmount amt = original.get(idx);			
			if(amt.partyId == partyId && amt.incomeType.equals(incomeType))
				return amt;
			}
		return null ;		
		}
	
	public IncomeAmount retrieveSingleByNature(int partyId, String nature) 
		{
		System.out.print("\nIncome retrieve :: " + partyId + "," + nature);
		
		for(int idx = 0 ; idx < this.original.size() ; idx++)
			{
			IncomeAmount amt = original.get(idx);			
			if(amt.partyId == partyId && amt.nature.equals(nature))
				return amt;
			}
		return null ;		
		}
	

	
	public IncomeAmount retrieveCarryFw(int partyId) 
		{
		System.out.print("CF retrieve :: " + partyId);
	
		for(IncomeAmount amt : carryForward)
			{
			if(amt.partyId == partyId)
				return amt ;
			}
		return null ;		
		}
	
	
	
	
	public String splitAmount(int partyId, String priNature, String[] natures, double[] value) throws Exception 
		{		

		
		double tempsum = 0 ;
		for(int i = 0 ; i < value.length ; i++)
			tempsum += value[i];
		
		double[] ratio = new double[value.length];
		for(int i = 0 ; i < value.length ; i++)
			ratio[i] = value[i] / tempsum;
			
				
		if(natures.length != ratio.length) 
			return "ERR:32412 Number of natures and numbers of ratios are differnt in count N=" + natures.length + ",R=" + ratio.length ;
		
		if( !ReconMath.numEqual( ReconMath.sum(ratio), 1.0, 0.0001 ))
			return "ERR:32431 Total sum of the ratio during split is not equal to 1 ";	
		
		IncomeAmount amt = this.getIncomeAmount(partyId, priNature);
		
		ArrayList<IncomeAmount> children = amt.split(natures, ratio);
		
		int idx = original.indexOf(amt) ;
		this.original.remove(amt);

		this.original.addAll(idx,children);	
		
		return "SUCCESS in splitting amount" ;
		}


	private HashMap<String,Double> distributionByNature( int partyId, int reconPriority )
		{
		HashMap<String,Double> dist = new HashMap<String,Double>() ;

		double sm = sum( partyId, reconPriority);
			
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		for(IncomeAmount amt : list)
			{
			double ratio = 0.0 ;
			if(sm != 0)
				{
				ratio = amt.getAmount(reconPriority) / sm ;
				}
			System.out.print("\n DISTR :: " + amt.nature + "," + ratio);
			dist.put(amt.nature, ratio) ;			
			}
		return dist ;
		}


	private HashMap<String,Double> distributionByIncomeType( int partyId, int reconPriority )
		{
		HashMap<String,Double> dist = new HashMap<String,Double>() ;
	
		double sm = sum( partyId, reconPriority);
			
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		for(IncomeAmount amt : list)
			{
			double ratio = 0.0 ;
			if(sm != 0)
				{
				ratio = amt.getAmount(reconPriority) / sm ;
				}
			System.out.print("\n DISTR :: " + amt.incomeType + "," + ratio);
			dist.put(amt.incomeType, ratio) ;			
			}
		return dist ;
		}
	
	
	
	public double sum(int partyId, int reconPriority)
		{
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		
		double sum = 0.0 ;
		for(IncomeAmount amt : list)
			{
			sum += amt.getAmount(reconPriority);			
			}		
		return sum ;		
		}
	
	

/*	
	public String carryForwardNautreWise( int partyId, double amount, String reason, int reconPriority)
		{		
		HashMap<String,Double> distribution = this.distributionByNature(partyId, reconPriority) ;
		
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		
		for(IncomeAmount amt : list)
			{
			double value =  amt.getAmount(reconPriority);			
			double ratio = distribution.get(amt.nature) ;
						
//			System.out.print("\n on Value :: " + value + ", ratio" + ratio);
			amt.show("Carrying forward :: " + ratio );
			double carryAmount = amount * ratio ;
			
			IncomeAmount amt2 =  new IncomeAmount(amt.partyId, amt.incomeType, amt.partyName) ;
			
			amt2.nature = amt.nature ;

			amt2.reason = reason ;
			
			if(value < carryAmount) return " ERR:59232 Carryforward amount is greater than actual value" ;
			
			amt2.setAmount(reconPriority, carryAmount);
			amt.setAmount(reconPriority, value - carryAmount);
			amt2.tag = "C" ;
			this.carryForward.add(amt2);			
			}
		
		return "Success" ;		
		}
*/	

	
/*	
	public String removeCarryForward(IncomeAmount carryamt)
		{
		int reconPriority = carryamt.getPrioritySource() ;
		HashMap<String,Double> distribution = this.distributionByIncomeType(carryamt.getPartyId(), reconPriority) ;		
		ArrayList<IncomeAmount> list = this.retrieve(carryamt.getPartyId()) ;

		double amount = carryamt.getAmount(reconPriority);
		
		for(IncomeAmount amt : list)
			{			
	//		System.out.print("\n on Value :: " + value + ", ratio" + ratio);
			
			double value =  amt.getAmount(reconPriority);			
			double ratio =  distribution.get(amt.incomeType) ;
			double partcarry =  amount * ratio ;			
			
			amt.setAmount(reconPriority, value + partcarry);						
			amt.show("Carrying forward :: " + ratio +  "," + partcarry);			
			}
		this.carryForward.remove(carryamt);
		
		return "SUCCESS" ;		
		}
*/	
	
	public String removeCarryForwardSingle(IncomeAmount carryamt)
		{
		int reconPriority = carryamt.getPrioritySource() ;
		
		IncomeAmount mainamt =  this.retrieveSingle(carryamt.partyId, carryamt.incomeType);
	
		mainamt.increase(reconPriority, carryamt.getAmount(reconPriority));			
		mainamt.increase(0, carryamt.getTdsDeposited());
		
		mainamt.show("Carrying Backward :: " + carryamt.getAmount(reconPriority) );			
						
		this.carryForward.remove(carryamt);		
		return "SUCCESS" ;		
		}
	
	

/*
	public String carryForward( int partyId, double amount, String reason, int reconPriority)
		{
		this.show();
		System.out.print("\n ------------------------------ Carryforward ------------------ ");
		
		HashMap<String,Double> distribution = this.distributionByIncomeType(partyId, reconPriority) ;		
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		
		String partyName = list.get(0).getPartyName();
				
		IncomeAmount carryamt =  this.retrieveCarryFw(partyId);
		
		if(carryamt == null)
			{
			carryamt =  new IncomeAmount(partyId, "CFW", partyName) ;
			carryamt.tag = "C" ;
			this.carryForward.add(carryamt);
			}
		
		carryamt.setReason(reason);
		carryamt.setAmount(reconPriority, amount);
		carryamt.show("CARRY OBJECT");
		
		for(IncomeAmount amt : list)
			{						
			double value =  amt.getAmount(reconPriority);			
			double ratio = distribution.get(amt.incomeType) ;
			double carryAmount = amount * ratio ;			
			amt.setAmount(reconPriority, value - carryAmount);						
			amt.show("Carrying forward :: " + ratio );
			System.out.print("\nCFW : on value=" + value + ", ratio=" + ratio + ",carryAmount=" + carryAmount );			
			}		
		return "Success" ;		
		}
*/
	
	
	
/*	
	public String carryBackword( int partyId, double amount, String reason, int reconPriority)
		{		
		HashMap<String,Double> distribution = this.distributionByIncomeType(partyId, reconPriority) ;		
		ArrayList<IncomeAmount> list = this.retrieve(partyId) ;
		
		String partyName = list.get(0).getPartyName();
				
		IncomeAmount carryamt =  this.retrieveCarryFw(partyId);
		
		if(carryamt == null)
			{
			System.out.print("\nCarry forward doesn't exist ::: " );
			return "Carry forward doesn't exist ::: for " + partyId + "," + amount;
			}
		
		carryamt.setReason( carryamt.getReason() + " " +reason);
		double existingcarry = carryamt.getAmount(reconPriority);
		carryamt.setAmount(reconPriority, existingcarry - amount);
		
		for(IncomeAmount amt : list)
			{			
	//		System.out.print("\n on Value :: " + value + ", ratio" + ratio);
			
			double value =  amt.getAmount(reconPriority);			
			double ratio = distribution.get(amt.incomeType) ;
			double carry = amount * ratio ;			
			
			amt.setAmount(reconPriority, value + carry);						
			amt.show("Carrying forward :: " + ratio +  "," + carry);			
			}
		
		
		
		return "Success" ;		
		}
*/	
	
	
	
	
	
	public String attachPrimarySource(IncomeDocument incomeDoc)
		{
		ArrayList<String> parties = incomeDoc.listAllParties() ;
		
		original = new ArrayList<IncomeAmount>();
		
		for ( int pid = 0 ; pid < parties.size() ; pid++ )
			{
			System.out.print("Attaching Primary Source ::: " + pid );
			
			String pName = parties.get(pid);
			
			double value = incomeDoc.getIncome(pName) 	;
			int partyId = pid + 1 	;
			IncomeAmount amt = new IncomeAmount(partyId, "SingleIncomeType", pName, value , 1 ) ;
			
			original.add(amt);							
			}
		
		umap.setPrimaryPartyName(parties);	
		
		return null ;
		
		}


	public String attachTdsSource(IncomeDocument incomeDoc)
		{
		ArrayList<String> parties = incomeDoc.listAllParties() ;		

		for ( String pp : parties )
			{
			int partyId = this.umap.nameId(pp);
			ArrayList<IncomeAmount> income = this.retrieve(partyId);
			if(income.size() >1 ) return ("ERROR : 506312 : Intigrity issue : TDS amount not be split  " + pp);
			
			double value = incomeDoc.getTds(pp);
			IncomeAmount amt = income.get(0);
			amt.setTdsDeposited(value);
			}
		return "SUCCESS : TDS source is attached ";
		}
	
	
	
	public ArrayList<String> unIdentifiedPartyNames(IncomeDocument incomeDoc)
		{
		ArrayList<String> parties = incomeDoc.listAllParties() ;
		ArrayList<String> unidentified = new ArrayList<String>() ;
				
		for(String p : parties)
			{
			int nid = this.umap.nameId(p);
			if(nid == -1)
				{
				unidentified.add(p);
				}
			}
		
		return unidentified ;
		}
	
	

	public ArrayList<String> primaryNames()
		{
		ArrayList<String> ppp = this.umap.getPrimaryNames();
		return ppp ;		
		}
	
		
	
	public String attachSource(IncomeDocument incomeDoc, int reconPriority)
		{
		ArrayList<String> parties = incomeDoc.listAllParties() ;
		
		
		for(String pp : parties)
			{
			int pid = this.umap.nameSearch.get(pp); 
			double value = incomeDoc.getIncome(pp);
			IncomeAmount amt = getIncomeAmount(pid,"global"); 
			//IncomeAmount amt = getIncomeAmountByIncomeType(pid,"SingleIncome");
			
			if(amt!= null)
				{
				System.out.print("\nFound Income Amount ::: Party:: " + pp + "," + pid );
				amt.show(pp);
				}
			
			if(amt == null)
				{
				amt = new IncomeAmount(pid , "SingleIncomeType" ,pp, value , reconPriority ) ;
				this.original.add(amt);
				}
			
				amt.setAmount(reconPriority, value);			
			}
		
		return "SUCCESS : Source is attached successfully :::: " ;
				
		}


	public void show() 
		{

		System.out.print("\n-------------------------------------------- Start of Recon data" );
		for(int i = 0 ; i < this.original.size() ; i++)
			this.original.get(i).show(i + "");
		
		System.out.print("\nCarryForward Amounts ::::::::::::::::::::::::::::: " );
		for(int i = 0 ; i < this.carryForward.size() ; i++)
			this.carryForward.get(i).show(i + "");
		
		System.out.print("\n-------------------------------------------- End of Recon data" );
		
		System.out.print("\n------------ RCON successful :: " + this.reconclied());
		
		}


	public String carryForwardSingleValue(IncomeAmount amt, double carryAmount, String reason, int reconPriority) {
		
		this.show();
		System.out.print("\n ------------------------------ Carryforward ------------------ ");
		
//		IncomeAmount carryamt =  this.retrieveCarryFw(amt.partyId, amt.incomeType);
		
		IncomeAmount carryamt =  new IncomeAmount(amt.partyId, amt.incomeType , amt.partyName) ;
		carryamt.setNature(amt.getNature() + "-CFW");
		carryamt.tag = "C" ;	
		carryamt.setReason(reason);		
				
		if(reconPriority == 1)
			{			
			double carryTax = ( carryAmount / amt.getAmount(1))  * amt.getTdsDeposited() ; 
			carryamt.increase(0, carryTax);
			amt.reduce(0, carryTax);
			
			System.out.print("\nCFW : Carry-Tax=" + carryTax );			
			}

		carryamt.increase(reconPriority, carryAmount);
		amt.reduce(reconPriority, carryAmount);						
				
		carryamt.show("CARRY OBJECT");
		amt.show("Carrying forward :: " + carryAmount );

		this.carryForward.add(carryamt);

		System.out.print("\nCFW : carryAmount=" + carryAmount );			

		return "Success" ;		
		}


	
	private IncomeAmount retrieveCarryFw(int partyId, String incomeType) {
		
		System.out.print("\nCFW Income retrieve :: " + partyId + "," + incomeType);
		
		for(int idx = 0 ; idx < this.carryForward.size() ; idx++)
			{
			IncomeAmount amt = carryForward.get(idx);			
			if(amt.partyId == partyId && amt.incomeType.equals(incomeType))
				{
				return amt;
				}
			}
		return null ;		
		}
	
		
	}	
