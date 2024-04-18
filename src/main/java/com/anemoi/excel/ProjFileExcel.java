package com.anemoi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.data.HibernateWriter;
import com.anemoi.data.Proj;
import com.anemoi.security.CryptAlgo;
import com.anemoi.security.DecryptAlgo;
import com.anemoi.security.HashCore;
import com.anemoi.security.Infosec;
import com.anemoi.security.StringCrypto;
import com.anemoi.util.Util;

public class ProjFileExcel {
	
	List<Proj> data = new ArrayList<Proj>();
	
	public void load(File file) throws Exception {

		FileInputStream stream = new FileInputStream(file);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(stream);

		// 2. Or you can use a for-each loop
		System.out.println("Retrieving Sheets using for-each loop");
		Sheet ss = workbook.getSheetAt(0);

		int rowlomit = 1000000 ;
		
		for (int i = 1; i < rowlomit; i++) {

			Row rr = ss.getRow(i);
			if (rr == null) break ;
			
			System.out.print("\nRow " + i);
			Proj sr = new Proj();
			
			//	S.No.	Party Name	Invoice Number	Invoice Date 	Invoice Amount	Amount of Taxes withheld	Billing Currency			
			
			if( rr.getCell(0) == null || rr.getCell(0).getCellType() == CellType.BLANK )
				{
				break ;
				}

			int num = (int) rr.getCell(0).getNumericCellValue();
			sr.setSrNo(num);

			String str = rr.getCell(1).getStringCellValue();
			sr.setName(str.toUpperCase());
			
			String secret = Util.generateRandomPassword(8) ;
						
			str = rr.getCell(2).getStringCellValue();
			sr.setPan(str.toUpperCase());

			str = rr.getCell(3).getStringCellValue();
			sr.setGroupCompany(str.toUpperCase());

			str = rr.getCell(4).getStringCellValue();
			sr.setPrecisionId(str.toUpperCase());

			str = rr.getCell(5).getStringCellValue();			
			sr.setActivity(str);
						
			str = rr.getCell(6).getStringCellValue();			
			sr.setYear(str);
			
			str = rr.getCell(7).getStringCellValue();						
			if(!this.validatesEmails(str))
				{
				System.out.print("\nInvalid Email address during projecct");
				throw new Exception("ERR43023: Project creation failes : Email address for the project accounts are not valid");				
				}
			sr.setUsers(str);

			
			str = " " ; 
			if( !(rr.getCell(8) == null || rr.getCell(8).getCellType() == CellType.BLANK) )
				{
				str = rr.getCell(8).getStringCellValue();			
				}
			sr.setClientField1(str);

			str = " " ; 
			
			if( !(rr.getCell(9) == null || rr.getCell(9).getCellType() == CellType.BLANK) )
				{
				str = rr.getCell(9).getStringCellValue();			
				}
			sr.setClientField1(str);

			sr.setSecret(secret);

			HashCore hasher = new HashCore();
			
			sr.setEncrSecret(hasher.hash1(secret));
			
			data.add(sr);
			}
		workbook.close();
	}

	
	public boolean validatesEmails(String str)
		{
		String regexPattern = Infosec.VUEP ;
		
	    String[] emailAddress = str.split(",") ;
	    
	    for(int i = 0 ; i < emailAddress.length ; i++)
	    	{
	    	emailAddress[i] = emailAddress[i].trim();
	    	boolean check =  Pattern.compile(regexPattern).matcher(emailAddress[i]).matches();
	    	
	    	if(!check) 
	    		{
	    		System.out.println("ERR430594: Invalid Emai:" + emailAddress[i] );
	    		return false ;
	    		}
	    	}
	    
	    return true ;	    
		}
	
	public void show()
		{
		System.out.print("\nProject Data ------------------------------- ");
		
		for(int i = 0 ; i < this.data.size() ; i++)
			this.data.get(i).show(i + "");
		}
	

	public static void main(String[] argv) throws Exception
		{
		ProjFileExcel sr = new ProjFileExcel();
		
		File f = new File("/home/devzone/MyCenter/data/testData/projects2021.xlsx");		
		sr.load(f);
		sr.show();		
		
		HibernateWriter hw = new HibernateWriter(0);
		hw.store(sr.getData(), "Proj");
		
		}

	public List<Proj> getData() 
		{
		return data;
		}

	public void setData(List<Proj> data) {
		this.data = data;
	}


}
