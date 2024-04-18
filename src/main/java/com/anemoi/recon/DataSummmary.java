package com.anemoi.recon;

public class DataSummmary 	
	{
	long srNo ;
	String tag ;
	long totalName ;
	long totalTrans ;
	long totalProj  ;
	long totalRefund ;
	double totalAmount ;
	double totalTax ;
	double totalRefundAmount ;
	
	public void show(String stamp)
	{
		System.out.print("\n" + stamp + "[" + srNo + "] : file=" + tag + ",names=" + totalName  + ",ref records=" + totalRefund + ",tran records=" 
					+ totalTrans + ",record proj=" 
						+ totalProj + ",amount=" + totalAmount +  ",total refund=" + totalRefundAmount );

	}
	
	public DataSummmary()
	{
		
	}
	
	public DataSummmary(long no, String tag, long tn, long tt, long tp, long tr ,double ta, double trefund)
		{
		this.srNo = no ;
		this.tag = tag ;
		this.totalName = tn ;
		this.totalRefund = tr ;
		this.totalTrans = tt ;
		this.totalProj = tp ;
		this.totalAmount = ta ;
		this.totalRefundAmount = trefund ;
		}

	public long getSrNo() {
		return srNo;
	}

	public void setSrNo(long srNo) {
		this.srNo = srNo;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getTotalName() {
		return totalName;
	}

	public void setTotalName(long totalName) {
		this.totalName = totalName;
	}

	public long getTotalTrans() {
		return totalTrans;
	}

	public void setTotalTrans(long totalTrans) {
		this.totalTrans = totalTrans;
	}

	public long getTotalProj() {
		return totalProj;
	}

	public void setTotalProj(long totalProj) {
		this.totalProj = totalProj;
	}

	public long getTotalRefund() {
		return totalRefund;
	}

	public void setTotalRefund(long totalRefund) {
		this.totalRefund = totalRefund;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}
	
	
	
	
	}
