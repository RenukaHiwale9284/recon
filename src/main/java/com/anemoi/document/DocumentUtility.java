package com.anemoi.document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.anemoi.data.HibernateWriter;
import com.anemoi.data.PartyRecord;
import com.anemoi.data.Proj;
import com.anemoi.data.RPTRecord;
import com.anemoi.data.Refund;
import com.anemoi.data.SalesRecord;
import com.anemoi.excel.ProjFileExcel;
import com.anemoi.excel.RPTFileExcel;
import com.anemoi.excel.SRFileExcel;
import com.anemoi.msg.EmailUtility;
import com.anemoi.msg.Emailer;
import com.anemoi.txt.AS26FileTXT;

public class DocumentUtility {

	public static void loadDocument(File f, HibernateWriter hs, String docTag) throws Exception {

		
		if (docTag.contentEquals("26ASTXT")) {
			AS26FileTXT as26 = new AS26FileTXT();
			as26.load(f);
			as26.parse();

			List<PartyRecord> parties = as26.getPartA().getParties();							
			hs.store26ASRecords(parties);
			
			List<Refund> refunds = as26.getPartD().getRefunds();
			hs.storeRefunds(refunds);
			}
		
		
		if (docTag.contentEquals("SRXLS")) {

			SRFileExcel sr = new SRFileExcel();
			sr.load(f);
			
			
			
			List<SalesRecord> records = sr.getData();
			hs.storeSalesRecord(records);
			}

		
		if (docTag.contentEquals("RPTXLS")) {
			RPTFileExcel rptfile = new RPTFileExcel();
			rptfile.load(f);			
			List<RPTRecord> records = rptfile.getData();
			rptfile.show();
			
			hs.storeRPT(records);
			}

		if (docTag.contentEquals("PROJXLS")) {
			ProjFileExcel sr = new ProjFileExcel();
			sr.load(f);						
			List<Proj> records = sr.getData();
			ArrayList<Proj> newProjects = hs.storeNewProj(records);			
			
			Emailer sender = new Emailer();
			sender.setDefaultConfig();
			
			sender.sendPasswords(newProjects);			
			}
		
		}		

	}
