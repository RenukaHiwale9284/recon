package com.anemoi.itr.storage;

import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.TaxElement;
import com.anemoi.util.Util;

public class GenDataTable {
	
	int projectId ;
	String table ;
	int lenght ;
	
	ArrayList<String> headers ;
	ArrayList<ArrayList<String>> data ;
	
	
	public void show()
		{	
		System.out.print("\nGenDataTable :: " + projectId + "," + table + "," + lenght );
		System.out.print("\nHeaders in GenDataTable :: [" + headers.size() + " ] = " + headers);
		
		System.out.print("\nDATA :::::::");
		for(int idx = 0 ; idx < data.size() ; idx++)
			{
			System.out.print("\nRow-" + idx + " == " + data.get(idx));
			}		
		}	
	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public GenTableDefinition getGenTableDefinition()
		{
		GenTableDefinition def = new GenTableDefinition(projectId, table);
		def.setHeaders(headers);
		return def ;
		}
	
	
	public ArrayList<GenRowData> getGenRowData()
		{
		System.out.println("Generating row records ::::  ");
		ArrayList<GenRowData> dd = new ArrayList<GenRowData> ();
				
		for( int i = 0 ; i < data.size() ; i++ )
			{
			GenRowData  row = new GenRowData(projectId, table, i+1) ;
			row.setRow( data.get(i) );	
			
			dd.add(row);
			}		
		return dd ;
		}
	
	
	public GenDataTable () 
		{
		headers = new ArrayList<String>();
		data = new ArrayList<ArrayList<String>>();		
		}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}

	public ArrayList<String> getHeaders() {
		return headers;
	}
	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}
	public ArrayList<ArrayList<String>> getData() {
		return data;
	}
	public void setData(ArrayList<ArrayList<String>> data) {
		this.data = data;
		
	}

	public void pushRow(ArrayList<String> r) {
		this.data.add(r);		
	}


	
	public GenDataTable(XSSFWorkbook book, String table) throws Exception
		{		
		XSSFSheet ss = book.getSheet(table);
		System.out.print("\nLoading the table data :: " + table);
		if (ss == null) throw new Exception("CL 0012 Exact table is not available") ;
	
		int rowlimit = Util.Limits.get("Template_Values_Row_Limit");
	
		Row rr = ss.getRow(0) ;		
	
		System.out.print("\nSize of Header row :: " + rr.getLastCellNum());
		for ( int i = 0 ; i < rr.getLastCellNum() ; i++)
			{			
			rr.getCell(i);
			String t = rr.getCell(i).getCellType().name() ;
			
			if (t == null || t.equalsIgnoreCase("EMPTY")) break ;
			
			String h =   rr.getCell(i).getStringCellValue();
			headers.add(h);			
			}
		
		
		for (int i = 1; i <= rowlimit; i++) {
	
			rr = ss.getRow(i);
			if( Util.isEmptyRow(rr) ) break ;
			if (rr == null) break ;
			System.out.print("\nRow [" + i + "] = ");
	
			ArrayList<String> rowData = new ArrayList<String>();
			for ( int j = 0 ; j < headers.size() ; j++)
				{
				String d = this.getStringValue(rr.getCell(j));
				if(d.equalsIgnoreCase("eof") && j == 0 ) break ;
				rowData.add(d);
				System.out.print("\nValue : " +  d);
				}
			
			this.data.add(rowData);		
			}	
		}
	
	
	public String getStringValue(Cell cell) 
		{		
	
		String objType = cell.getClass().toString() ;
		String valueType = null , valueStr = null ;
		Object value ;
		
		switch (cell.getCellType()) {
	
			case BOOLEAN:			
				valueType = "Boolean" ;					
				value = cell.getBooleanCellValue()	;
				valueStr = value.toString() ;			
				// System.out.print(cell.getBooleanCellValue() + " --> Type ::" + valueType);			
				break;
		
			case STRING:
				value = cell.getRichStringCellValue().getString() ;
				valueType = "String" ;
				valueStr = value.toString() ;
				System.out.print(cell.getRichStringCellValue().getString() + " --> Type ::" + valueType  + " >> " + valueStr);
				break;
		
			case NUMERIC:
		
				if (DateUtil.isCellDateFormatted(cell)) 
					{
					Date dd = cell.getDateCellValue();
		
					value = cell.getDateCellValue() ;
					valueType = "Date" ;
					valueStr = Util.formatter.format(dd) ;
					System.out.print("\n" + dd + " --> Type ::" + valueType + "  Name ::" + value  +  " >> " + valueStr);
					} 
				else {
					float dd = (float)cell.getNumericCellValue();
		
					cell.setCellType(CellType.STRING);
					//	value = cell.getNumericCellValue();
					value = cell.getStringCellValue() ;
					valueStr = value.toString() ;
					valueType = "Double" ;		 						
					System.out.println("\n" +  dd + " --> Type ::" + valueType +  " >> " + valueStr);
					}
				break;
		
			case FORMULA:
				// System.out.print(cell.getCellFormula());
				break;
		
			case BLANK:
				System.out.print("");
				break;
			default:
				System.out.print("");
			}
	
		return valueStr ;

		
	}

	
	
	

}
