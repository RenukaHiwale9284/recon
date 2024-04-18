package com.anemoi.itr;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.GenDataTable;
import com.anemoi.util.Util;

public class TaxListElement {
		
	HashMap<String,TaxElement> values = new HashMap<String,TaxElement>();

	ArrayList<ArrayList<TaxElement>> tableData = new ArrayList<ArrayList<TaxElement>>();
	ArrayList<String> headers = new ArrayList<String>();
	
	
	public GenDataTable buildGenDataTable()
		{			
		ArrayList<ArrayList<String>> data = new  ArrayList<ArrayList<String>>();
		System.out.print("Building GenDataTable :: " );
		
		for ( int i = 0 ; i < tableData.size() ; i++)
			{
			ArrayList<TaxElement> dlist = tableData.get(i) ;
			ArrayList<String> N = new ArrayList<String>() ;
			System.out.print("ROW :: " + i );

			for( int idx = 0 ; idx < dlist.size() ; idx++ )
				{
				TaxElement tt = dlist.get(idx) ;
				String strValue =tt.getValueStr() ;
				
				System.out.print("," + strValue);
				
				N.add(strValue) ;				
				}
			
			data.add(N);			
			}		
		
		GenDataTable gt = new GenDataTable() ;
		
		gt.setHeaders(headers);
		gt.setData(data);
		gt.setLenght(data.size());
		
		return gt;	
		
		}
	
	public TaxListElement(XSSFWorkbook book, String table, int data_length) throws Exception
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
		
		
		if(data_length != -1) 
			{
			rowlimit = data_length;
			}
		
		
		for (int i = 1; i <= rowlimit; i++) {

			rr = ss.getRow(i);
			if( Util.isEmptyRow(rr) ) break ;
			if (rr == null) break ;
			System.out.print("\nRow [" + i + "] = ");

			ArrayList<TaxElement> rowData = new ArrayList<TaxElement>();
			for ( int j = 0 ; j < headers.size() ; j++)
				{				
				String var =   headers.get(j);

				TaxElement ct = new TaxElement(table, var, "");
				ct.setValue( rr.getCell(j) ) ;							
				rowData.add(ct);				

				String key = "Row" + i + "_" + var ;
				values.put(key, ct);
				}
			tableData.add(rowData);		
			}
		
		}
	
	
	public TaxListElement( ListTemplate lt, TableConfiguration tc ) throws Exception
		{
		System.out.print("\n\nTransformation from GenDataTable to TaxListElemet --------- ");
		//genTable.show();

		this.headers = lt.headers();
		
		for (int i = 0; i < tc.getData_length(); i++) 
			{
			ArrayList<TaxElement> rowData = new ArrayList<TaxElement>();

			for ( int j = 0 ; j < headers.size() ; j++)
				{
				
				String var =   headers.get(j);
				TaxElement ct = new TaxElement( lt.tableVar , var, "");
								
				rowData.add(ct);				
	
				String key = "Row" + i + "_" + var ;				
				values.put(key, ct);				
				}
			tableData.add(rowData);		
			}

		}
	
	public TaxListElement(GenDataTable genTable ) throws Exception
		{			
		System.out.print("\n\nTransformation from GenDataTable to TaxListElemet --------- ");
		//genTable.show();

		this.headers = genTable.getHeaders() ;
		
		for (int i = 0; i < genTable.getLenght(); i++) 
			{
			ArrayList<String> R = genTable.getData().get(i);
			System.out.print("\n ROW data from gennTable :: " + R);
			
			ArrayList<TaxElement> rowData = new ArrayList<TaxElement>();

			for ( int j = 0 ; j < headers.size() ; j++)
				{
				
				String var =   headers.get(j);
	
				TaxElement ct = new TaxElement( genTable.getTable(), var, "");
				
				ct.setValue(R.get(j)) ;							
				
				rowData.add(ct);				
	
				String key = "Row" + i + "_" + var ;
				
				values.put(key, ct);
				
				}
			tableData.add(rowData);		
			}
		}



	public int length() {
		// TODO Auto-generated method stub
		return tableData.size();
	}

	public HashMap<String, TaxElement> getValues() {		
		return values;
		}
	
	public HashMap<String,TaxElement> getRowValues(int rowid) throws Exception
		{
		if(rowid > tableData.size()) throw new Exception("CL 2231 Table data for row is not available") ; 
		
		HashMap<String,TaxElement> dmap = new HashMap<String,TaxElement>();
		
		for(TaxElement ct : this.tableData.get(rowid))
			{			
			//ct.show();
			dmap.put(ct.sheet + "_" + ct.var, ct);
			
			}
		
		return dmap ;			
		}

	public void show() 
		{
		System.out.print("\nHeaders in TaxListElement " + headers.size());
		for ( int i = 0 ; i < headers.size() ; i++)
			{
			System.out.print(headers.get(i) + ",");
			}

		System.out.print("\nData in TaxListElement " + tableData.size());
		for (int i = 0; i < this.tableData.size(); i++) {

			System.out.print("\nData[" + i + "]");
	
			ArrayList<TaxElement> rowData = tableData.get(i);
			for ( int j = 0 ; j < rowData.size() ; j++)
				{
				Object vv = rowData.get(j).value ;
				
				if(vv != null)
					System.out.print(rowData.get(j).value.toString() + ",");
				else
					System.out.print("EMPTY" + ",");
				}
			}
		}
		
	}

