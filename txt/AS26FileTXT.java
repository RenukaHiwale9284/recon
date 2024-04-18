package com.anemoi.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;

import com.anemoi.pdf.PartyRecord;
import com.anemoi.pdf.TDS;

public class AS26FileTXT {
	
	PartA partA = new PartA();
	PartD partD = new PartD();
	
	enum Part {None, partA, partD } ;
	String partAtag = "^PART A - Details of Tax Deducted at Source^" ;
	String partDtag = "^PART D - Details of Paid Refund^" ;
	
	public void load(File f) throws FileNotFoundException, IOException {
		
		int lcnt = 0 ;
		Part currentTag = Part.None;
		try(BufferedReader br = new BufferedReader(new FileReader(f))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        
		    	if(line.startsWith("^PART")) 		currentTag = Part.None;
		    		
		    	if(line.equalsIgnoreCase(partAtag)) currentTag = Part.partA ;

		    	if(line.equalsIgnoreCase(partDtag)) currentTag = Part.partD ;
		    	
		    			    	
		    	switch(currentTag)
		    		{
		    	case partA : partA.parse(line); break ;
		    	
		    	case partD : partD.parse(line); break ;

		    	
				default:break;	
		    		}
		    	
		    		
		    	
		    	
//		    	System.out.print("\nL[" + (++lcnt) + "]=" + line);
		    }
		    // line is not visible here.
		}
		
	}
	
	


	
	public PartA getPartA() {
		return partA;
	}





	public void setPartA(PartA partA) {
		this.partA = partA;
	}





	public PartD getPartD() {
		return partD;
	}





	public void setPartD(PartD partD) {
		this.partD = partD;
	}





	public void parse() {
		// TODO Auto-generated method stub
		
	}

	public List<PartyRecord> readRecords(String string) {
		
		
		return null;
	}

	public static void main(String[] argv) throws Exception
		{
		AS26FileTXT as26 = new AS26FileTXT();
		
		File f = new File("/home/devzone/MyCenter/data/InputData/26AS/26ASset2.txt");
		as26.load(f);
		
		}
	
}
