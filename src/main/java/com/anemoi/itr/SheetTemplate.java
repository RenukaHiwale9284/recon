package com.anemoi.itr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.ActionITR6Reading;
import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.itr.storage.InfoStore;
import com.anemoi.itr.storage.TemplateComparator;
import com.anemoi.itr.validation.VMessage;
import com.anemoi.itr.validation.ValidationUtil;
import com.anemoi.util.Util;

import org.apache.poi.xssf.usermodel.XSSFSheet;


public class SheetTemplate {

	// Template file
	File sheetTemplateFile;

	ArrayList<CellTemplate> staticRefrences = new ArrayList<CellTemplate>();

	ArrayList<ListTemplate> listRefernces = new ArrayList<ListTemplate>();
			
	
	public ArrayList<CellTemplate> getStaticRefrences() {
		return staticRefrences;
	}

	public void setStaticRefrences(ArrayList<CellTemplate> staticRefrences) {
		this.staticRefrences = staticRefrences;
	}

	public ArrayList<ListTemplate> getListRefernces() {		
		return listRefernces;
	}

	public void setListRefernces(ArrayList<ListTemplate> listRefernces) {
		this.listRefernces = listRefernces;
	}

	public static void main(String[] argv) throws Exception {

		Util.load(); 
		File strctFile = new File("ITR6_structure6.1.xlsx");
		File offsetFile = new File("ITR6_Config_Template.xlsx");
		
		SheetTemplate st = new SheetTemplate();
		st.load(strctFile);

		TemplateConf config = new TemplateConf();
		config.load(offsetFile);
		config.setTemplate(st);
		
		System.out.print("\nReading the ITR6 file ::: " );
		File itr6File = new File("/home/devzone/MyCenter/workspace/ITR6/client/ITR6_041121-114755.xlsm");
		
		FileInputStream file = new FileInputStream(offsetFile);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		config.buildTableConfigurations(workbook);
		
		workbook.close();
//		st.load(ff);		
		}
	
	public SheetTemplate() 
		{
		
		}

	public boolean load(File templateFile) throws Exception {
		
		sheetTemplateFile = templateFile ;
		
		if (!sheetTemplateFile.exists()) 
			{
			
			throw new Exception("ITR60002 : ITR6 Excel Template file doesn't exist in paht," + templateFile.getAbsolutePath());
			}

		FileInputStream file = new FileInputStream(templateFile);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		// 2. Or you can use a for-each loop
		System.out.println("Retrieving Sheets using for-each loop");
		Sheet sheet = workbook.getSheet("CELL_TEMPLATE");
		this.loadCellTemplates(sheet);
		
		
		System.out.println("=> " + sheet.getSheetName());

		// Util.readSheet(sheet);
		
		Sheet sheet2 = workbook.getSheet("LIST_TEMPLATE");		
		loadListTemplates(sheet2);
		
		showCellRef(); 
		showListTemplates();
		
		file.close();
		workbook.close();
		return false;
	}
	

	public void loadCellTemplates(Sheet ss) {

		// Tab
		// Variable
		// Desc
		// Row
		// Column
		// Select Option

		staticRefrences.clear();

		int rowlomit = Util.Limits.get("Template_Values_Row_Limit");

		for (int i = 1; i < rowlomit; i++) {

			Row rr = ss.getRow(i);
			System.out.print("\nRow " + i );
			
			String tab = rr.getCell(0).getStringCellValue();
			String var = rr.getCell(1).getStringCellValue();
			String data_type = rr.getCell(2).getStringCellValue();
			String desc = rr.getCell(3).getStringCellValue();
			
			int row = (int) rr.getCell(4).getNumericCellValue();
			
			String col = rr.getCell(5).getStringCellValue();
			String selOpt = rr.getCell(6).getStringCellValue();
			String tag = rr.getCell(7).getStringCellValue();

			
			if(row == 0) break ;

			
			CellTemplate ct = new CellTemplate(tab, tag, var, data_type, desc, row, col, selOpt);

			staticRefrences.add(ct);			
		}
	}
		
	
	public void loadListTemplates(Sheet ss) throws Exception {

		// Tab
		// Variable
		// Desc
		// Row
		// Column
		// Select Option

		listRefernces.clear();

		int rowlomit = Util.Limits.get("Template_Values_Row_Limit");

		for (int i = 1; i < rowlomit; i++) {

			Row rr = ss.getRow(i);
			
			if ( rr == null || rr.getCell(0) == null || rr.getCell(0).getStringCellValue().equalsIgnoreCase("")) break ;
			
			
			System.out.print("\nLoading the Cell template Row " + i );
			
			ListTemplate ll = new ListTemplate(rr);
			this.listRefernces.add(ll);		
			ll.show();
		}
	}

	
	
	public void showListTemplates()
		{
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{
			ListTemplate lt = this.listRefernces.get(i);
			lt.show();
			}			
		}
	
	public void showCellRef()
		{
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{
			CellTemplate ct = this.staticRefrences.get(i);
			ct.show();
			}
		}
	
	

	
	
		
	public void applyListTemplates(XSSFWorkbook book, ITR6DataSet dataset , TemplateConf configMaster, String command ) throws Exception
		{		
		TaxListElement data = null ;
		configMaster.setTemplate(this);
		
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{
			ListTemplate ct = this.listRefernces.get(i);
			System.out.print("\n\n\n--------------- Applying the List :: " + ct.tableVar);
			
			if(! configMaster.enabled(ct) )
				{
				System.out.print("\n\n--------------- List is not enabled  :: " + ct.tableVar);
				continue ;
				}
			
			TableConfiguration tc = configMaster.getTableConfigurator(ct) ;
			
			String key = ct.getTableVar() ;
						
			if(command.equalsIgnoreCase("write"))
				{
				data = dataset.extract(key, tc);
				System.out.print("\n\n--------------- Data loaded :: " + ct.tableVar);
				data.show();
				}
			
			if(command.equalsIgnoreCase("read"))
				{
				data = new TaxListElement(ct,tc);
				System.out.print("\n\n--------------- Empty data Loaded :: " + ct.tableVar);
				data.show();
				}
			
			int rowOffset = 0 , colOffset = 0 ;
			String sheet = ct.tab ;
			rowOffset = configMaster.calculateTableOffset(ct.getRowIdx() , ct.tab ) ;
			System.out.print("\n\n --------- Table offset " + rowOffset);
			
			ct.applyTemplate(book, data, rowOffset, colOffset, command);
			
			System.out.print("\nFinished : List template Application  :: " + ct.tableVar + "\n\n\n");
	
			if(command.equalsIgnoreCase("read"))
				{
				dataset.store(data, tc);
				}
			
			data.show();	
			
			}		
		}
	
	
	public void applyCellTemplates(XSSFWorkbook workbook, ITR6DataSet data, TemplateConf conf, String command) {
		
		conf.setTemplate(this);
			
		HashMap<String, TaxElement> values = data.getValueMap();
		 
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{			
			CellTemplate ct = this.staticRefrences.get(i);
			
			int rowoffset = conf.calculateTableOffset(ct.row, ct.tab);
			int coloffset = 0 ;
			
			ct.applyTemplate(workbook, values, rowoffset, coloffset, command);
			ct.show();			
			}		
		data.getValues().clear();
		
		
		data.getValues().addAll(values.values());		
		}


	public void applyCellTemplates(ITR6File itrfiledoc, ITR6DataSet data, TemplateConf conf, String command) throws Exception {
		
		conf.setTemplate(this);
			
		HashMap<String, TaxElement> values = data.getValueMap();
		 
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{			
			CellTemplate ct = this.staticRefrences.get(i);
			
			ct.applyTemplate(itrfiledoc, values, command);
			ct.show();			
			}		
		
		data.getValues().clear();
		data.getValues().addAll(values.values());		
		}
	

	
	public void applyListTemplates(ITR6File itrfiledoc, ITR6DataSet dataset, TemplateConf configMaster, String command ) throws Exception
		{		
		TaxListElement data = null ;
		configMaster.setTemplate(this);
		
		
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{
			ListTemplate ct = this.listRefernces.get(i);
			System.out.print("\n\n\n--------------- Applying the List :: " + ct.tableVar);
			
			if(! configMaster.enabled(ct) )
				{
				System.out.print("\n\n--------------- List is not enabled  :: " + ct.tableVar);
				continue ;
				}
			
			TableConfiguration tc = configMaster.getTableConfigurator(ct) ;
			tc.show("Table Configuration ::" ) ;
			String key = ct.getTableVar() ;
									
			if(command.equalsIgnoreCase("read"))
				{
				data = new TaxListElement(ct,tc);
				System.out.print("\n\n--------------- Empty data Loaded Len=" + tc.getData_length() + " :: " + ct.tableVar 
						+ " ::TAG=" + ct.getTableTag()  );
				data.show();
				}
									
			ct.applyTemplate(itrfiledoc, data, command);

			System.out.print("\nFinished : List template Application  :: " + ct.tableVar + "\n\n\n");
	
			if(command.equalsIgnoreCase("read"))
				{
				dataset.store(data, tc);
				}
			
			data.show();	
			
			}		
		}
	
	
	
	
	
	
	
	
	public void load(DirectoryStore dstore) throws Exception {
		dstore.loadStructure(this);		
		}

	public void loadCellTemplates(List results) 
		{				
		staticRefrences.clear();
		for (int i = 0; i < results.size() ; i++) 
			{
			CellTemplate ct = (CellTemplate) results.get(i);
			staticRefrences.add(ct);			
			}
		}

	public void load(InfoStore dstore) throws Exception {
		dstore.loadStructure(this);		
		}

	public void loadListTemplates(List listResults) {
		this.listRefernces.clear();
		for (int i = 0; i < listResults.size() ; i++) 
			{
			ListTemplate ct = (ListTemplate) listResults.get(i);
			ct.loadFlyWeight();
			
			listRefernces.add(ct);			
			}
		
		}
	
	
	public ArrayList<VMessage> validateValues(ValidationUtil V, ITR6DataSet data) {		
		ArrayList<VMessage>  messages = new ArrayList<VMessage>() ;		
		HashMap<String, TaxElement> values = data.getValueMap();		 
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{			
			CellTemplate ct = this.staticRefrences.get(i);		
			VMessage msg = ct.validate(V, values);			
			if(msg != null)
				{
				messages.add(msg);
				}
			}				
		return messages ;
		}
		
	
	public ArrayList<VMessage> validateTable(ValidationUtil V, ITR6DataSet dataset, String table, TemplateConf configMaster) throws Exception 
		{		
		ListTemplate ct = null ;
		TableConfiguration tc = configMaster.getTableConfigurator(ct) ;
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{
			if(this.listRefernces.get(i).getTableVar().equals(table) )
					{
					ct = this.listRefernces.get(i);
					System.out.print("\nListTemplate found for validation of Table :: " + table);
					break ;
					}						
			}
		
		TaxListElement data = dataset.extract(table, tc);						
		ArrayList<VMessage> messages  = ct.validate(V, data);		
		System.out.print("\nFinished : Validation  :: " + ct.tableVar + "\n\n\n");
		return messages ;
		}
	

	public ArrayList<VMessage> validateAllTables(ValidationUtil V, ITR6DataSet dataset, TemplateConf configMaster) throws Exception 
		{		

		ArrayList<VMessage> allValidations = new ArrayList<VMessage>();
		ListTemplate lt = null ;		
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{
			lt = this.listRefernces.get(i);		

			System.out.print("\nStart of Validation  :: " + lt.tableVar + "\n\n\n");
			TableConfiguration tc = configMaster.getTableConfigurator(lt) ;
			String table = lt.tableVar ;
			TaxListElement data = dataset.extract(table, tc);			
			
			ArrayList<VMessage> msg = lt.validate(V, data);
			
			allValidations.addAll(msg);
			System.out.print("\nFinished : Validation  :: " + lt.tableVar + "\n\n\n");
			}		
				
		return allValidations ;
		}
	
	
	public ArrayList<VMessage>  validateDataset(ValidationUtil V, ITR6DataSet data , TemplateConf cm) throws Exception {

		ArrayList<VMessage> all = new ArrayList<VMessage>();
		
		ArrayList<VMessage> msg1 = validateValues(V,data);
		all.addAll(msg1);
		
		ArrayList<VMessage> msg2 = validateAllTables(V,data, cm);		
		all.addAll(msg2);
		
		return all;
		}


	public void close() 
		{
			
		}
	
	public void orgnizeOffset()
		{
		
			
		}

	public void visitCell(ActionITR6Reading visitor ) {
		 
		
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{			
			CellTemplate ct = this.staticRefrences.get(i);			
			visitor.visit(ct);			
			}				
		
		
		for(int i = 0 ; i < this.listRefernces.size() ; i++)
			{			
			ListTemplate lt = this.listRefernces.get(i);
			TableConfiguration tc = visitor.loadConfig(lt) ;
			visitor.setData(tc);
			
			ArrayList<CellTemplate> clist = lt.getCellList() ;			
			for(int j = 0 ; j < tc.getData_length() ; j++ )
				{				
				CellTemplate ct = clist.get(j);
				
				visitor.visitListCell(ct);							
				}
			}				
		

		
	}

	public void setITRVersion(double itrVersion) {
		
		for(int i = 0 ; i < this.staticRefrences.size() ; i++)
			{			
			CellTemplate ct = this.staticRefrences.get(i);			
			ct.setVersion(itrVersion);
			}				
	
	
	for(int i = 0 ; i < this.listRefernces.size() ; i++)
		{			
		ListTemplate lt = this.listRefernces.get(i);
		lt.setVersion(itrVersion);
		}						
	}


	
	
}
