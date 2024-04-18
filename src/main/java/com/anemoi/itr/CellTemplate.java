package com.anemoi.itr;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.anemoi.itr.ref.RefDataUtiity;
import com.anemoi.itr.validation.VMessage;
import com.anemoi.itr.validation.ValidationUtil;
import com.anemoi.util.Util;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.apache.poi.ss.usermodel.CellType;

public class CellTemplate implements Serializable {

	double version = 6.0;
	String tab;
	String tag;
	String var;
	String datatype;
	String desc;
	int row;
	String col;
	String selOpt;
	String validations = "" ;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public CellTemplate()
	{
		
	}
	public String getValidations() {
		return validations;
	}

	public void setValidations(String vv) {
		this.validations = vv;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}

	public String getSelOpt() {
		return selOpt;
	}

	public void setSelOpt(String selOpt) {
		this.selOpt = selOpt;
	}

	public void show() {
		System.out.print("\nCellTemplate = [ Ver=" + this.version + " Data=" + tab + "," + var + "," + datatype + "," + col + row + "," + desc + ",Val=" + validations + ",TAG=" + tag );
	}

	
	CellTemplate(String tab1, String tag1, String var1, String dataType1, String desc1, int row1, String col1, String selOpt1) {
		tab = tab1;
		tag = tag1 ; 
		var = var1;
		datatype = dataType1;
		desc = desc1;
		row = row1;
		col = col1;
		selOpt = selOpt1;
	}

	public void applyTemplate(XSSFWorkbook book, HashMap<String, TaxElement> data, String command) {		
		applyTemplate ( book,data, 0, 0, command) ;		
		}
	
	

	public void applyTemplate(XSSFSheet ss, HashMap<String, TaxElement> data, int rowOffset, int colOffset, String command) {

		ss.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999),IgnoredErrorType.NUMBER_STORED_AS_TEXT );
		
		
		if(command.equals("write"))
			{
			int rowNew= row + rowOffset ;
	//		String colNew =  (((char)col.charAt(0)) + colOffset) + "" ;
			
			CellReference ref = new CellReference(col + rowNew);
			Row r = ss.getRow(ref.getRow());
			
			System.out.print("\nInside CTA :: " + tab + "," + rowNew + "," + col);
			
			if (r != null ) {
				Cell c = r.getCell(ref.getCol());
	
				String destTypeName = c.getCellType().name();
				String key = tab + "_" + var;
				TaxElement value = data.get(key);
				
				if(value == null || value.value == null) 
					{					
					System.out.print("\nWARNING:4910232 : Application of Template is not possible for CT=[" + this.tag + "," + this.var +"]" + " due to NULL Value");
					return ;
					}
				
				System.out.print("\nProcess conversion :: NULL=" + value); 
				System.out.print("\nProcess conversion :: (source -> dest) via datatype " + value.value.getClass().getName()
						+ "->" + destTypeName + " via " + datatype +   " from :: " + this.tab + "," + this.var);
	
				if (value.value instanceof java.lang.String)
					processStringDataType(value, c);
	
				if (value.value instanceof java.lang.Double)
					processDoubleDataType(value, c);
	
				if (value.value instanceof java.util.Date)
					processDateDataType(value, c);
	
				}
			}

		if(command.equals("read"))
			{						
			int rowNew= row + rowOffset ;
			
			CellReference ref = new CellReference(col + rowNew);
			Row r = ss.getRow(ref.getRow());
			
			System.out.print("\n\n----------------------- Inside CTA :: " + tab + "," + rowNew + "," + col);
			
			if (r != null) {
				Cell value = r.getCell(ref.getCol());
				String key = tab + "_" + var ;							
				TaxElement dest = data.get(key);
				
				if(dest == null)
					{
					dest = new TaxElement(this.tab, this.var, this.desc);
					data.put(key, dest);	
					}

				System.out.print("\nReading values  ::  " + this.tab + "-" + this.var );
							
				dest.setValue(value);				
				}
			}
		}
	
	
	
	public void write(XSSFWorkbook book, HashMap<String, TaxElement> data, int rowOffset, int colOffset)
		{
		XSSFSheet ss = book.getSheet(tab);		
		ss.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999),IgnoredErrorType.NUMBER_STORED_AS_TEXT );

		int rowNew= row + rowOffset ;
//		String colNew =  (((char)col.charAt(0)) + colOffset) + "" ;
		
		CellReference ref = new CellReference(col + rowNew);
		Row r = ss.getRow(ref.getRow());
		
		System.out.print("\nInside Cell Template Application :: " + tab + "," + rowNew + "," + col);
		
		if (r != null) {
			Cell c = r.getCell(ref.getCol());

			String destTypeName = c.getCellType().name();
			String key = tab + "_" + var;
			TaxElement value = data.get(key);
			
			if( value.value == null )
				{
				System.out.print("\nProcess conversion :: (source -> dest) via datatype " + "NULL-VALUE"
						+ "->" + destTypeName + " via " + datatype);
				return ;				
				}
			System.out.print("\nProcess conversion :: (source -> dest) via datatype " + value.value.getClass().getName()
					+ "->" + destTypeName + " via " + datatype);

			if (value.value instanceof java.lang.String)
				processStringDataType(value, c);

			if (value.value instanceof java.lang.Double)
				processDoubleDataType(value, c);

			if (value.value instanceof java.util.Date)
				processDateDataType(value, c);

			}
		}
		
	
	
	public void read(XSSFWorkbook book, HashMap<String, TaxElement> data, int rowOffset, int colOffset)
		{
		XSSFSheet ss = book.getSheet(tab);		
		ss.addIgnoredErrors(new CellRangeAddress(0,9999,0,9999),IgnoredErrorType.NUMBER_STORED_AS_TEXT );

		int rowNew= row + rowOffset ;
		
		CellReference ref = new CellReference(col + rowNew);
		Row r = ss.getRow(ref.getRow());
		
		System.out.print("\n\nInside Cell Template Read :: " + tab + "," + rowNew + "," + col);
		
		if (r != null) 
			{
			Cell value = r.getCell(ref.getCol());
			String key = tab + "_" + var ;							
			TaxElement dest = data.get(key);
			
			if(dest == null)
				{
				dest = new TaxElement(this.tab, this.var, this.desc);
				data.put(key, dest);	
				}

			System.out.print("\nReading values  ::  " + this.tab + "-" + this.var );
						
			dest.setValue(value);			
			}				
		}
	
	
	public void applyTemplate(XSSFWorkbook book, HashMap<String, TaxElement> data, int rowOffset, int colOffset, String command) {
		
		if(command.equals("write"))
			{
			write( book, data, rowOffset, colOffset);
			}
		
		if(command.equals("read"))
			{
			read(book, data, rowOffset, colOffset);
			}
		}

	
	
	
	
	
	private void processStringDataType(TaxElement value, Cell c) {
		String V = (String) value.value;
		String destTypeName = c.getCellType().name();

		try {
			if (datatype.equalsIgnoreCase("text") || datatype.equalsIgnoreCase("string")
					|| datatype.equalsIgnoreCase("email")) {
				c.setCellValue(V);
			} else if (datatype.equalsIgnoreCase("integer")) {
				c.setCellValue((Integer) (Integer.parseInt(V)));
			} else if (datatype.equalsIgnoreCase("date")) {
				c.setCellValue(V);
//				Date d = Util.formatter.parse(V);
//				c.setCellValue(d);
			} else if (datatype.equalsIgnoreCase("number")) {
				c.setCellValue((Double.parseDouble(V)));
			}
			
			// c.setCellType(null);
			// c.setCellType(Cell.CELL_TYPE_STRING);
		} catch (Exception ex) {
			System.out.print("\nMisMatch for destination cellType :" + destTypeName);
			value.show();
//	 		ex.printStackTrace();
		}
	}

	private void processDoubleDataType(TaxElement value, Cell c) {
		Double V = (Double) value.value;
		String destTypeName = c.getCellType().name();

		try {
			if (datatype.equalsIgnoreCase("text") || datatype.equalsIgnoreCase("string")) {
				c.setCellValue(V.toString());
			}
			if (datatype.equalsIgnoreCase("email")) {
				throw new Exception("Double cant be converted to Email");
			} else if (datatype.equalsIgnoreCase("integer")) {
				c.setCellValue((Integer) (V.intValue()));
			} else if (datatype.equalsIgnoreCase("date")) {
				throw new Exception("Double cant be converted to Date");
			} else if (datatype.equalsIgnoreCase("number")) {
				c.setCellValue((V.intValue()));
			}
		} catch (Exception ex) {
			System.out.print("\nMisMatch for destination cellType :" + destTypeName);
			value.show();
			// ex.printStackTrace();
		}
	}

	private void processDateDataType(TaxElement value, Cell c) {
		Date V = (Date) value.value;
		String destTypeName = c.getCellType().name();

		try {
			if (datatype.equalsIgnoreCase("text") || datatype.equalsIgnoreCase("string")) {
				String data = Util.formatter.format(V);
				c.setCellValue(data);
			}
			if (datatype.equalsIgnoreCase("email")) {
				throw new Exception("Double cant be converted to Email");
			} else if (datatype.equalsIgnoreCase("integer")) {
				throw new Exception("Date cant be converted to Integer");
			} else if (datatype.equalsIgnoreCase("date")) {
				c.setCellValue(V);
			} else if (datatype.equalsIgnoreCase("number")) {
				throw new Exception("Date cant be converted to Double");
			}
		} catch (Exception ex) {
			System.out.print("\nMisMatch for destination cellType :" + destTypeName);
			value.show();
			// ex.printStackTrace();
		}
	}
	
	
	
	public VMessage validate(ValidationUtil V,  HashMap<String, TaxElement> values) 
		{
		if(this.validations == null || this.validations.trim().length() == 0) return null ;
		
		String key = tab + "_" + var;
		TaxElement value = values.get(key);
		
		this.show();
		
		System.out.print("\nValidating [" + validations  + "]");

		VMessage msg = V.validate(this.validations, value);
		msg.setCategory(this.tab);
		msg.setVariable(this.var);
		
		return msg ;
		}
	
	
	public int lastRow(XSSFSheet ss, int rowOffset ) {
		
		int lastRowNum = 0 ;
		
		for(  ; rowOffset < 100 ; rowOffset++)
			{
			int rowNew= row + rowOffset ;
				
			CellReference ref = new CellReference(col + rowNew);
			
			Row r = ss.getRow(ref.getRow());
			
			System.out.print("\nInside Cell Template Application :: " + tab + "," + rowNew + "," + col);
			
			if (r == null) break ;
			
			
			Cell c = r.getCell(ref.getCol());
			System.out.print("\nCell Type " + c.getCellType() );
			
			// If there is black cell found at the end.
			if ( c.getCellType() ==  CellType.BLANK ) 
				{
				break;
				}
			
			// If the cell not numeric or forumla so break.
			if ( c.getCellType() !=  CellType.FORMULA && c.getCellType() !=  CellType.NUMERIC )
				{
				break ; 
				}

			lastRowNum++ ;
						
			System.out.print("\n Row ID = :" + lastRowNum);
			}	
		return lastRowNum;
			}
	
	
	
	public void applyTemplate(ITR6File itrdoc, HashMap<String, TaxElement> data, String command) throws Exception 
		{		
		if(command.equals("write"))
			{
			write( itrdoc, data);
			}
	
		if(command.equals("read"))
			{
			read(itrdoc, data);
			}
		}

	
	private void read(ITR6File itrfile, HashMap<String, TaxElement> data) throws Exception 
		{
		System.out.print("\n\nInside Cell Template Read :: " + tab );
		
		String key = tab + "_" + var ;									
		String value = "NA" ;
		
		if( !itrfile.getType().equals("XML")) throw new Exception("Read function is not available for type " + itrfile.getType()) ;

		TaxElement dest = data.get(key);			
		if(dest == null)
			{
			dest = new TaxElement(this.tab, this.var, this.desc);
			data.put(key, dest);	
			}

		XMLITR6File doc = (XMLITR6File) itrfile ;		
		if (datatype.equalsIgnoreCase("date")) 
		 	{				 
			value = doc.readSingleValue(this.tag);
			
			Date d = Util.xmlDataFormatter.parse(value);			
			dest.setValueStr(Util.formatter.format(d));
			System.out.print("\nReading Date value  ::  " + this.tab + "-" + this.var + ":Val=" + value);

			return ;
		 	}

		value = doc.readSingleValue(this.tag);
		System.out.print("\nReading values  ::  " + this.tab + "-" + this.var + " = " + value);
		
		dest.setValue(value);			
		}				
	
	
	
	private void read2(ITR6File itrfile, HashMap<String, TaxElement> data) throws Exception 
		{
		System.out.print("\n\nInside Cell Template Read :: " + tab );
		
		String key = tab + "_" + var ;									
		String value = "NA" ;
		
		if(itrfile.getType().equals("XML"))
			{
			XMLITR6File doc = (XMLITR6File) itrfile ;
			
			value = doc.readSingleValue(this.tag);
			
//			System.out.print("\nValidations :: " + this.validations + ":::" + this.version);				
//			value = ReferenceDataUtiity.one().transform(this.validations, value);
			}
		
		TaxElement dest = data.get(key);
			
		if(dest == null)
			{
			dest = new TaxElement(this.tab, this.var, this.desc);
			data.put(key, dest);	
			}

		System.out.print("\nReading values  ::  " + this.tab + "-" + this.var );
		
		dest.setValue(value);		
		}				
	
	
	
	
	
	
	private void write(ITR6File xmlDoc, HashMap<String, TaxElement> data) {
		
	}

}
