package com.anemoi.data;

import java.util.Date;

public class TDS {
	
	// This record file is useful for the trascations 
	 int srNo;
	 String section;
	 Date dateOfTran;
	 Date dateOfBooking;
	 String remarks;
	 double amountPaid;
	 double taxDeducted;
	 double taxDeposited;
	 String bookingStatus;

	 
	
	public void show(String tag) {
		System.out.print("\nTDS_RECORD[" + tag  + "] = " + srNo + "," + section + "," + dateOfTran + "," + dateOfBooking + "," + remarks + ","
				+ amountPaid + "," + taxDeducted + "," + taxDeposited + "," + bookingStatus);

	}
	
	

	public int getSrNo() {
		return srNo;
	}

	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}



	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Date getDateOfTran() {
		return dateOfTran;
	}

	public void setDateOfTran(Date dateOfTran) {
		this.dateOfTran = dateOfTran;
	}

	public Date getDateOfBooking() {
		return dateOfBooking;
	}

	public void setDateOfBooking(Date dateOfBooking) {
		this.dateOfBooking = dateOfBooking;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getTaxDeducted() {
		return taxDeducted;
	}

	public void setTaxDeducted(double taxDeducted) {
		this.taxDeducted = taxDeducted;
	}

	public double getTaxDeposited() {
		return taxDeposited;
	}

	public void setTaxDeposited(double taxDeposited) {
		this.taxDeposited = taxDeposited;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
