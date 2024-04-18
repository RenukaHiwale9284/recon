package com.anemoi.itr;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.itr.storage.GenDataTable;
import com.anemoi.itr.storage.InfoStore;
import com.anemoi.util.Util;

public class ITR6DataSet {

	// Template file
	File dataFile;

	private ArrayList<TaxElement> values = new ArrayList<TaxElement>();
	private HashMap<String, TaxElement> valueMap = new HashMap<String, TaxElement>();

	InfoStore tableStore = null ;
	
	public static void main(String[] argv) throws Exception {
		Util.load();
		File ff = new File("ITR6-data.xlsx");
		ITR6DataSet dataset = new ITR6DataSet();
		dataset.load(ff);
	}

	public ITR6DataSet() {
	}
		
	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public InfoStore getTableStore() {
		return tableStore;
	}

	public void setTableStore(InfoStore tableStore) {
		this.tableStore = tableStore;
	}

	public void setValues(ArrayList<TaxElement> values) {
		this.values = values;
	}

	public void setValueMap(HashMap<String, TaxElement> valueMap) {
		this.valueMap = valueMap;
	}

	public boolean load(File ff) throws Exception {

		dataFile = ff;
		
		System.out.print("\n Data file path :" + ff.getAbsolutePath());
		if (!dataFile.exists()) {
			throw new Exception(
					"ITR60002 : ITR6 Excel Template file doesn't exist in paht," + dataFile.getAbsolutePath());
		}

		FileInputStream file = new FileInputStream(dataFile);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		// 2. Or you can use a for-each loop
		System.out.println("Retrieving Sheets using for-each loop");
		Sheet sheet = workbook.getSheet("Values");

		System.out.println("=> " + sheet.getSheetName());

		// Util.readSheet(sheet);
		workbook.close();
		loadValues(sheet);
		showValues();
		return false;
	}


	public GenDataTable extractTable(int projectId , String table, TableConfiguration tc) throws Exception 
		{		
		TaxListElement ttt = extract(table,tc) ;		
		GenDataTable ggg = ttt.buildGenDataTable() ;
		
		ggg.setProjectId(projectId);
		ggg.setTable(table);
		
		return ggg;		
		}
	

	public void store(TaxListElement listElement, TableConfiguration tc) throws Exception
		{
		System.out.print("\nStoring TaxElement check512");
		tc.show("check512");
		tableStore.storeTable(listElement, tc) ;
		}
	
	
	public TaxListElement extract(String table, TableConfiguration tc) throws Exception
		{
		if(tableStore != null)
			{
			GenDataTable tbl = this.tableStore.fetchDataTable(table, tc) ;
			tbl.show();
			TaxListElement te = new TaxListElement(tbl);
			return te ;
			}
		
		System.out.print("Extracting table data " +  table );
		
		if (!dataFile.exists()) {
			throw new Exception(
					"ITR60002 : ITR6 Excel Template file doesn't exist in paht," + dataFile.getAbsolutePath());
			}
		
		FileInputStream file = new FileInputStream(dataFile);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		TaxListElement data = new TaxListElement(workbook, table, tc.data_length);
		
		workbook.close();
		data.show();
		return data ;		
		}
		
	
	
	public void loadValues(Sheet ss) {

		System.out.print("Loading values for ::" + ss.getSheetName());

		// Sheet
		// Variable
		// Description
		// Value
		values.clear();

		int rowlomit = Util.Limits.get("Template_Values_Row_Limit");

		for (int i = 1; i < rowlomit; i++) {

			Row rr = ss.getRow(i);
			if (rr == null) break ;
			
			System.out.print("\nRow " + i);

			String tab = rr.getCell(0).getStringCellValue();
			if(tab.equalsIgnoreCase("EOF")) break ;
			
			String var = rr.getCell(1).getStringCellValue();
			String desc = rr.getCell(2).getStringCellValue();

			if (tab.trim().equalsIgnoreCase("")) break;
			TaxElement ct = new TaxElement(tab, var, desc);
			ct.setValue(rr.getCell(3)) ;
						
			values.add(ct);
			
			String key = tab + "_" + var ;
			valueMap.put(key, ct);
			
		}
	}


	public void load(List vv) {

		System.out.print("Loading values from List ::" + vv.size());

		for (int i = 0 ; i < vv.size(); i++) {

			TaxElement ct = (TaxElement) vv.get(i);
			ct.setValueType("String");				
			values.add(ct);			
			String key = ct.sheet + "_" + ct.var ;
			valueMap.put(key, ct);			
			}
		
		showValues();
		}
		
	
	public void showValues() {
		for (int i = 0; i < this.values.size(); i++) {
			TaxElement ct = this.values.get(i);
			System.out.print("\n Data : " + i ) ;
			ct.show();
			
			if(ct.valueStr == null) System.out.print("\n ------------------------------------------------ CHECK " );
			
		}
	}

	public void close() {

		}

	public HashMap<String, TaxElement> getValueMap() {
		return this.valueMap ;
	}

	public void load(DirectoryStore dstore) throws Exception {
		dstore.loadData(this);		
		}

	public List getValues() {
		return this.values ;
	}

	public void setProjectId(int pid) {
		
		for(int i = 0 ; i < values.size() ;i++)
			{
			TaxElement tc = this.values.get(i);
			tc.setProjectId(pid);
			}
		}

	public void load(InfoStore dd) throws Exception {
		dd.loadData(this);	
	}

	public TaxListElement extract(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	public void refreshListViaMap() {
		}

}
