package com.anemoi.itr;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.itr.storage.InfoStore;
import com.anemoi.itr.storage.TemplateComparator;
import com.anemoi.util.Util;

public class TemplateConf {

	int projectId ;
	
	SheetTemplate template ;
	
	private ArrayList<TableConfiguration> tableOffset = new ArrayList<TableConfiguration>();

	private HashMap<String, TableConfiguration> tableOffsetMap = new HashMap<String, TableConfiguration>();

	
	public int getProjectId() {
		return projectId;
	}

	public static void main(String[] argv) throws Exception {
		Util.load();
		File ff = new File("input/data1/ITR6_config.xlsx");
		TemplateConf st = new TemplateConf();
		st.load(ff);
		}

	public TemplateConf() 
		{		
		
		}
	
	
	public ArrayList<TableConfiguration> getTableOffset() {
		return tableOffset;
	}

	public void setTableOffset(ArrayList<TableConfiguration> tableOffset) {
		this.tableOffset = tableOffset;
	}

	public HashMap<String, TableConfiguration> getTableOffsetMap() {
		return tableOffsetMap;
	}

	public void setTableOffsetMap(HashMap<String, TableConfiguration> tableOffsetMap) {
		this.tableOffsetMap = tableOffsetMap;
	}

	public SheetTemplate getTemplate() {
		return template;
	}

	public boolean load(List<TableConfiguration> lst) throws Exception
		{		
		this.tableOffset.clear();
		this.tableOffsetMap.clear();
		
		for (int i = 0; i < lst.size(); i++) {
						
			TableConfiguration ct = lst.get(i);
			tableOffset.add(ct);
			
			String key = ct.tab + "_" + ct.tableName;
			tableOffsetMap.put(key, ct);
			}
		
		this.showTableOffset();
		
		return false;		
		}
	
	
	public boolean load(File offset) throws Exception {

		if (!offset.exists()) {

			throw new Exception("ITR60003 : ITR6 Offset file doesn't exist in path," + offset.getAbsolutePath());
		}

		FileInputStream file = new FileInputStream(offset);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		// 2. Or you can use a for-each loop
		System.out.println("Retrieving Sheets using for-each loop");
		XSSFSheet sheet = workbook.getSheet("Tables");

		if (sheet == null) {
			throw new Exception("ITR60007 : Excel ITR6 offset sheet is NULL," );
			}

		//load sheet and add ArrayList<TableConfig>  or loading table configuration data from a given Excel sheet
		loadTableOffset(sheet);

		showTableOffset();
		file.close();
		workbook.close();
		return false;
	}
	
	
	private void loadTableOffset(Sheet ss) {

		// tab
		// table_Name
		// length
		// offset

		tableOffset.clear();

		int rowlomit = Util.Limits.get("Template_Values_Row_Limit");

		//retrieved from class Util.Limits.
		for (int i = 1; i < rowlomit; i++) {

			Row rr = ss.getRow(i);
			System.out.print("\nRow " + i);
			if(rr == null) break ;
			//extract data from cell of current row
			String tab = rr.getCell(0).getStringCellValue();
			String table_Name = rr.getCell(1).getStringCellValue();
			int length = (int) rr.getCell(2).getNumericCellValue();
			int offset = (int) rr.getCell(3).getNumericCellValue();
			int data_len = (int) rr.getCell(4).getNumericCellValue();
			Boolean enableFlag = (Boolean) rr.getCell(5).getBooleanCellValue();
			String enable = "false" ;
			
			if(enableFlag) enable = "true" ;
			
			if (tab == null || tab.equalsIgnoreCase(""))	break ;
			
			TableConfiguration ct = new TableConfiguration(tab, table_Name, length, offset, data_len, enable);
			
			System.out.print("\n Loading TableConfig - projectId - " + this.projectId );
			ct.setProjectId(this.projectId);
			
			// Put the key-value pair into the config map
			tableOffset.add(ct);
			String key = tab + "_" + table_Name;
			tableOffsetMap.put(key, ct);
			
			
		}
	}

	public void setProjectId( int projectId )
		{
		for (int i = 0; i < tableOffset.size(); i++) {		
			TableConfiguration ct = tableOffset.get(i);
			ct.setProjectId(projectId);
			}
		}
	
	public void refreshKeys()
		{
		for (int i = 1; i < tableOffset.size(); i++) {
			
			TableConfiguration ct = tableOffset.get(i);
			tableOffset.add(ct);
			String key = ct.tab + "_" + ct.tableName;
			tableOffsetMap.put(key, ct);
			}		
		}	

	//default table configurations based on a list of list references (ListTemplate) stored in the class
	public void buildDefaultConfigurations() throws Exception
		{
		// Sort all the list refrecnes 
		ArrayList<ListTemplate> listRefernces = this.template.getListRefernces() ;
		this.tableOffset.clear();						
		System.out.print("\nStart of building table configuration ");
		for( int i = 0 ; i <  listRefernces.size() ; i++)
			{
			ListTemplate lt = listRefernces.get(i);			
			TableConfiguration ct = new TableConfiguration(lt.tab, lt.tableVar, lt.viewLen, 0, 0, "true");
			this.tableOffset.add(ct);		
			}
		System.out.print("\n\n ::::::::::::: Default Table configuraiton created :::::::::::::: " );
		this.showTableOffset();
		}
	
	
	
	public void buildTableConfigurations(XSSFWorkbook book) throws Exception
		{
		ArrayList<ListTemplate> list = this.template.getListRefernces() ;
		
		// Sort all the list refrecnes 
		ArrayList<ListTemplate> listRefernces = new ArrayList<ListTemplate>();
		for(int i = 0 ; i < list.size() ;i++)
			{
			listRefernces.add(list.get(i));
			}
		
		TemplateComparator tc  = new TemplateComparator();
		listRefernces.sort(tc);
				
		System.out.print("\nStart of building table configuration ");
		for( int i = 0 ; i <  listRefernces.size() ; i++)
			{
			ListTemplate lt = listRefernces.get(i);
			TableConfiguration config =  getTableConfigurator(lt);
			
			System.out.print("\nTable::" + lt.getTab() + "," + lt.getTableVar() + "," + lt.getRowIdx());


			//calculate the row offset based on the row index and table name using the calculateTableOffset method.

			int rowOffset = 0 ;
			
			rowOffset = calculateTableOffset( lt.getRowIdx() , lt.tab) ;
			
			System.out.print("\n --------------------------------------------------- Calculated Offset ::: " + rowOffset);
			
//			int rowNew = lt.getRowIdx() + rowOffset ;
	
			CellTemplate srNo = lt.getCellList().get(0);
						
			XSSFSheet sheet = book.getSheet(lt.getTab());		
	
			int lastRowNum = srNo.lastRow(sheet, rowOffset);
			
			System.out.print("\n Final Row Num " + lastRowNum);
			
			config.setData_length(lastRowNum);
			config.reset();
			
			config.show("CHECK-321");
			
			}
		}

	
	public void buildTableConfigurations(ITR6File documennt) throws Exception
		{
		ArrayList<ListTemplate> list = this.template.getListRefernces() ;
		
		// Sort all the list refrecnes 
		ArrayList<ListTemplate> listRefernces = new ArrayList<ListTemplate>();
		for(int i = 0 ; i < list.size() ;i++)
			{
			listRefernces.add(list.get(i));
			}
		
		TemplateComparator tc  = new TemplateComparator();
		listRefernces.sort(tc);
				
		System.out.print("\nStart of building table configuration ");
		for( int i = 0 ; i <  listRefernces.size() ; i++)
			{
			ListTemplate lt = listRefernces.get(i);
			TableConfiguration config =  getTableConfigurator(lt);
			
			System.out.print("\nTable::" + lt.getTab() + "," + lt.getTableVar() + "," + lt.getRowIdx());
			
			if(documennt.getType().equals("XML"))
				{
				XMLITR6File book = (XMLITR6File)documennt ;
								
				int lastRowNum = book.countRows( lt.getTableTag() );
				
				System.out.print("\n Final Row Num " + lastRowNum);			
				config.setData_length(lastRowNum);
				config.reset();
				}
			config.show("CHECK-321");
			
			}
		}

	
	
	
	
	
	
	
	
	
	public void showTableOffset() {
		for (int i = 0; i < this.tableOffset.size(); i++) {
			TableConfiguration to = this.tableOffset.get(i);
			to.show(i + "");
		}
	}

	
	public int calculateTableOffset(int rowIdx , String sheetName ) 
		{
//		System.out.print("\n-------- Calculation of offset for Row : " + rowIdx + " in sheet " + sheetName);
		int tableOffset = 0 ;		
		
		for(int i = 0 ; i < template.listRefernces.size() ; i++)
			{
//			System.out.print("\n-- Template List reference ::: " + i );
			
			ListTemplate topcell = template.listRefernces.get(i);
			
			if(!topcell.tab.equalsIgnoreCase(sheetName)) continue ;
			
//			topcell.show(); 
			
			if (  rowIdx  >  topcell.getRowIdx() ) 
				{
				String key = topcell.getTab() + "_" + topcell.getTableVar() ;
				
				TableConfiguration o = this.tableOffsetMap.get(key) ;
				if(o == null) continue ;
				
				tableOffset += o.getOffset() ;
				}
			
//			System.out.print("\n-- TableOffset = "  + tableOffset);			
			
			}
		
		return tableOffset;
		}
	
		
	
	public void setTemplate(SheetTemplate sheetTemplate) {
		
		this.template = sheetTemplate ;
		
		}

	public boolean enabled(ListTemplate ct) {
		
		String key = ct.tab + "_" + ct.tableVar ;
		
		System.out.print("\nTable enablement :: " + key);
		
		TableConfiguration c = this.tableOffsetMap.get(key) ;	
		
		if(c.enable.equalsIgnoreCase("true")) return true ;
		
		return false;
		}
	
	

	public TableConfiguration getTableConfigurator(ListTemplate ct) 
		{
		String key = ct.tab + "_" + ct.tableVar ;		
		TableConfiguration c = this.tableOffsetMap.get(key) ;
		return c ;
		}

	
	public void load(DirectoryStore dstore) throws Exception 
		{
		dstore.loadTemplateConf(this);
		}

	public void load(InfoStore dstore) throws Exception {
		dstore.loadTemplateConf(this);
		
	}

}
