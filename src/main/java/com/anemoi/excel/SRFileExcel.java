package com.anemoi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.data.SalesRecord;

public class SRFileExcel {
	
	List<SalesRecord> data = new ArrayList<SalesRecord>();
	
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
			SalesRecord sr = new SalesRecord();
			
			//	S.No.	Party Name	Invoice Number	Invoice Date 	Invoice Amount	Amount of Taxes withheld	Billing Currency

			
			if( rr.getCell(0) == null || rr.getCell(0).getCellType() == CellType.BLANK )
				{
				break ;
				}
			int num = (int) rr.getCell(0).getNumericCellValue();
			sr.setSrNo(num);			
			
			
			String str = rr.getCell(1).getStringCellValue();
			sr.setPartyName(str.toUpperCase());
			
			rr.getCell(2).setCellType(CellType.STRING);
			str = rr.getCell(2).getStringCellValue();
			sr.setInvoiceNumber(str);
			
			Date date = rr.getCell(3).getDateCellValue();
			sr.setInvoiceDate(date);
			
			double value = rr.getCell(4).getNumericCellValue();
			sr.setInvoiceAmount(value);
			
			rr.getCell(5).setCellType(CellType.STRING);
			str = rr.getCell(5).getStringCellValue();
			sr.setTax(str);
			
			str = rr.getCell(6).getStringCellValue();
			sr.setCurrency(str);
			
			data.add(sr);
			}

	// 	Util.readSheet(sheet);
		workbook.close();
	}

	public void show()
		{
		System.out.print("\nSales Register ------------------------------- ");
		
		for(int i = 0 ; i < this.data.size() ; i++)
			this.data.get(i).show(i + "");
		}
	

	public static void main(String[] argv) throws Exception
		{
		SRFileExcel sr = new SRFileExcel();
		
		File f = new File("/home/devzone/MyCenter/data/testData/SR_set2.xlsx");
		sr.load(f);
		sr.show();
		
		}

	public List<SalesRecord> getData() 
		{
		return data;
		}

	public void setData(List<SalesRecord> data) {
		this.data = data;
	}
	
}
