package com.anemoi.recon;

import java.io.Serializable;
import java.util.ArrayList;

import com.anemoi.trac.LogElement;

// This class can be loaded to the database 

public class IncomeAmount implements Cloneable, Serializable 
	{
	long id ;
	int projectId ;
	int srNo ;
	int partyId ;
	String partyName ;
	String incomeType ;	
	String tag = "I";
	String nature ;		
	double tdsDeposited ;	
	double  source1Amount ;	
	double  source2Amount ;
	double  source3Amount ;
	double  source4Amount ;
	double  source5Amount ;	
	double finalIncome ;
	double finalTax ;	
	String reason ;
	
	public void show(String tag)
		{
		System.out.print("\nIncomeAmt [" + tag + "] = Party(" + partyId + "," + partyName + ","
				+ "Type(" + incomeType + "," + nature  
				+ ")  Income (" + source1Amount +"," + source2Amount + "," + source3Amount + "," + source4Amount + "," + source5Amount + "," + finalIncome +")"
				+ " TDS( " + tdsDeposited + "," + finalTax  + ")  ")  ;
		}
	
	public void calculateTax()
		{
		this.finalIncome =  Math.max(this.source1Amount, this.source2Amount);
		this.finalIncome =  Math.max(this.finalIncome, this.source3Amount);

		this.finalTax = this.tdsDeposited ;
		}
	
	
	public double getPriorityAmount()
		{
		if(this.source1Amount > 0.0001) return source1Amount ;
		if(this.source2Amount > 0.0001) return source2Amount ;
		if(this.source3Amount > 0.0001) return source3Amount ;
		if(this.source4Amount > 0.0001) return source4Amount ;
		if(this.source5Amount > 0.0001) return source5Amount ;
		
		return  0;		
		}
	

	public int getPrioritySource()
		{
		if(this.source1Amount > 0) return 1 ;
		if(this.source2Amount > 0) return 2 ;
		if(this.source3Amount > 0) return 3 ;
		if(this.source4Amount > 0) return 4 ;
		if(this.source5Amount > 0) return 5 ;
		
		return  0;		
		}
	
	
	public void reduce(int priority, double deficit)
		{
		this.setAmount(priority, this.getAmount(priority) - deficit);
		}
	
	public void increase(int priority, double deficit)
		{
		this.setAmount(priority, this.getAmount(priority) + deficit);
		}

	
	
	public double getAmount(int priority)
		{
		if(priority == 0 ) return this.tdsDeposited   ;		
		if(priority == 1 ) return this.source1Amount  ;
		if(priority == 2 ) return this.source2Amount  ;
		if(priority == 3 ) return this.source3Amount  ;
		if(priority == 4 ) return this.source4Amount  ;
		if(priority == 5 ) return this.source5Amount  ;
		
		if(priority==10) return this.tdsDeposited ;
		
		return 0 ;
		}
	
	
	public IncomeAmount(int partyId2, String inctype, String pName, double value, int incomePriority) 
		{
		this.nature = "global";
		this.incomeType = inctype ;
		this.partyId = partyId2 ;
		this.partyName = pName ;
		setAmount(incomePriority, value);
		}	

	public IncomeAmount(int partyId2, String inctype, String pName) 
		{
		this.nature = "global";
		this.incomeType = inctype ;
		this.partyId = partyId2 ;
		this.partyName = pName ;
		}	
	
	
	public void setAmount(int priority, double value)
		{
		if(priority == 0 ) this.tdsDeposited = value ;
		if(priority == 1 ) this.source1Amount = value ;
		if(priority == 2 ) this.source2Amount = value ;
		if(priority == 3 ) this.source3Amount = value ;
		if(priority == 4 ) this.source4Amount = value ;
		if(priority == 5 ) this.source5Amount = value ;
		}
	
	
	
	ArrayList<IncomeAmount> split(String[] natures, double[] ratio) throws Exception
		{
		ArrayList<IncomeAmount> children = new ArrayList<IncomeAmount>();
		
		if( natures.length != ratio.length )
			throw new Exception("ERR 54923 : Recon intigriy is violated : Nature lengh is"  
						+ natures.length + ", Ratio lengh = " + ratio.length) ;
		
		for(int idx = 0 ; idx < natures.length ; idx++)
			{
			
			IncomeAmount amt = (IncomeAmount)this.clone();
			amt.nature = natures[idx];
			amt.incomeType = "IncomeType" + (idx+1) ;
			amt.source1Amount = this.source1Amount * ratio[idx];
			amt.source2Amount = this.source2Amount * ratio[idx];
			amt.source3Amount = this.source3Amount * ratio[idx];
			amt.source4Amount = this.source4Amount * ratio[idx];
			amt.source5Amount = this.source5Amount * ratio[idx];
			
			amt.tdsDeposited = this.tdsDeposited * ratio[idx];
			System.out.print("\nSplit Clild :::::::::::::::::::");
			amt.show(idx + "");
			children.add(amt);
			}
		
		return children ;
		 
		}
	
	public IncomeAmount clone()	
		{
		IncomeAmount amt = new IncomeAmount(this.partyId, this.incomeType, this.partyName) ;

		//amt.partyId = this.partyId ;
		//amt.partyName = this.partyName ;
		//amt.incomeType = this.incomeType ;

		
		amt.finalTax = this.finalTax ;		
		amt.nature = this.nature ;			
		amt.tdsDeposited = this.tdsDeposited ;	
		amt.source1Amount = this.source1Amount ;	
		amt.source2Amount = this.source2Amount ;
		amt.source3Amount = this.source3Amount ;
		amt.source4Amount = this.source4Amount ;
		amt.source5Amount = this.source5Amount ;
		
		amt.finalIncome = this.finalIncome ;
		amt.finalTax = this.finalTax ;	
		
		return amt;
		}
	
	public IncomeAmount()
		{
		this.tag = "I" ;
		}
	

	public int hashCode() 
		{
		return (partyName.hashCode()  +  this.incomeType.hashCode());
		}


	public void reduceAmount(int reconPriority, double carryAmount)  {		
		double a = this.getAmount(reconPriority) ;
		a = a - carryAmount ;
		this.setAmount(reconPriority, a);		
		}

	public int getPartyId() {
		return partyId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public double getTdsDeposited() {
		return tdsDeposited;
	}

	public void setTdsDeposited(double tdsDeposited) {
		this.tdsDeposited = tdsDeposited;
	}

	public double getFinalIncome() {
		return finalIncome;
	}

	public void setFinalIncome(double finalIncome) {
		this.finalIncome = finalIncome;
	}

	public double getFinalTax() {
		return finalTax;
	}

	public void setFinalTax(double finalTax) {
		this.finalTax = finalTax;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public double getSource1Amount() {
		return source1Amount;
	}

	public void setSource1Amount(double source1Amount) {
		this.source1Amount = source1Amount;
	}

	public double getSource2Amount() {
		return source2Amount;
	}

	public void setSource2Amount(double source2Amount) {
		this.source2Amount = source2Amount;
	}

	public double getSource3Amount() {
		return source3Amount;
	}

	public void setSource3Amount(double source3Amount) {
		this.source3Amount = source3Amount;
	}

	public double getSource4Amount() {
		return source4Amount;
	}

	public void setSource4Amount(double source4Amount) {
		this.source4Amount = source4Amount;
	}

	public double getSource5Amount() {
		return source5Amount;
	}

	public void setSource5Amount(double source5Amount) {
		this.source5Amount = source5Amount;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
	}
