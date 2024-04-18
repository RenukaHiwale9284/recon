package com.anemoi.txt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.anemoi.pdf.PartyRecord;
import com.anemoi.pdf.TDS;

public class PartA {

	int pc = 0;
	int tc = 0;
	ArrayList<PartyRecord> parties = new ArrayList<PartyRecord>();

	private void readPartyTrans(String tuple) {
//		System.out.print("\nAdding new Trans " + tuple);

		String[] w = tuple.split("\\^");
		int l = w.length;
		System.out.print("\n\n" + w[0] + "," + w[1] + "," + w[2] + "," + w[3] + "," + w[4] + "," + w[5]);

		TDS tt = new TDS();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

		tt.setSrNo(Integer.parseInt(w[1]));
		tt.setSection(w[2]);
		try {
			System.out.print("\nNUM" + w[3]);
			tt.setDateOfTran(sdf.parse(w[3]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tt.setBookingStatus(w[4]);
		try {
			System.out.print("\nNUM" + w[5]);
			tt.setDateOfBooking(sdf.parse(w[5]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tt.setRemarks(w[6]);
		tt.setAmountPaid(Double.parseDouble(w[7]));
		tt.setTaxDeducted(Double.parseDouble(w[8]));
		tt.setTaxDeposited(Double.parseDouble(w[9]));

		int last = this.parties.size() - 1;
		this.parties.get(last).addTrans(tt);
	}

	private void readParty(String tuple) {
		PartyRecord pp = new PartyRecord();

		String[] w = tuple.split("[\\^]+");

		int l = w.length;

		pp.setSr_no(Integer.parseInt(w[0]));
		pp.setPartyName(w[1].toUpperCase());
		pp.setTan(w[2]);
		pp.setTac(Double.parseDouble(w[3]));
		pp.setTtd(Double.parseDouble(w[4]));
		pp.setTdsdeposited(Double.parseDouble(w[5]));

		pp.show("");
		parties.add(pp);

		// System.out.print("\n" + tuple);
//		System.out.print("\n\n" + w[0] + "," + w[1] + "," + w[2] + "," + w[3] + "," + w[4] + "," + w[5]);
	}

	public void parse(String line) {

		if (line.startsWith((pc + 1) + "^")) {
			pc++;
			readParty(line);
			tc = 0;
		}

		if (line.startsWith("^" + (tc + 1))) {
			tc++;
			readPartyTrans(line);
		}
	}

	public ArrayList<PartyRecord> getParties() {
		return parties;
	}

	public void setParties(ArrayList<PartyRecord> parties) {
		this.parties = parties;
	}

	

}
