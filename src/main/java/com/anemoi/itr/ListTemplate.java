package com.anemoi.itr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.TableVarFlyWeight;
import com.anemoi.itr.validation.VMessage;
import com.anemoi.itr.validation.ValidationUtil;

public class ListTemplate {
	
	List<TableVarFlyWeight> fieldList = new ArrayList<TableVarFlyWeight>();
	
	ArrayList<CellTemplate> cellList = new ArrayList<CellTemplate>();
	HashMap<String,CellTemplate> cellMap = new HashMap<String,CellTemplate>();
	
	double version = 6.0;
	String tab 	;
	String tableTag ;
	String tableVar ;
	int len ;	
	int viewLen = 0; 
	int rowIdx 	;
	int listid ;
		
	public String getTableTag() {
		return tableTag;
	}



	public void setTableTag(String tableTag) {
		this.tableTag = tableTag;
	}



	public int getViewLen() {
		return viewLen;
	}



	public void setViewLen(int viewLen) {
		this.viewLen = viewLen;
	}



	public ListTemplate()
		{
		
		}


	
	public List<TableVarFlyWeight> getFieldList() {
		return fieldList;
	}


	public void setFieldList(List<TableVarFlyWeight> fieldList) {
		this.fieldList = fieldList;
	}
	
	public ListTemplate(Row rr) throws Exception 
		{
		
		int colMetCount = 4 ;
		int colDefOffset = 6 ;
		
		try {
			tab 	= rr.getCell(0).getStringCellValue();		
			tableTag = rr.getCell(1).getStringCellValue();
			tableVar = rr.getCell(2).getStringCellValue();
			len = (int) rr.getCell(3).getNumericCellValue();
			viewLen = (int) rr.getCell(4).getNumericCellValue();
			
			// xml tag 		
			rowIdx 	= (int) rr.getCell(5).getNumericCellValue();
						
			for(int i = 0 ; i < len  ; i++)
				{
				String var = rr.getCell(colDefOffset + i * colMetCount ).getStringCellValue();			
				String data_type = rr.getCell(colDefOffset + i * colMetCount + 1).getStringCellValue();
				String colcellindex = rr.getCell(colDefOffset + i * colMetCount + 2).getStringCellValue();
				String colTag = rr.getCell(colDefOffset + i * colMetCount + 3).getStringCellValue();
	
				// xml tag
				
				CellTemplate ct = new CellTemplate(tableVar, colTag, var, data_type, var, rowIdx, colcellindex + "", "");
				
				cellList.add(ct);
				cellMap.put(var, ct);
				}		
			}
		catch(Throwable ex)
			{
			ex.printStackTrace();
			Exception e = new Exception("Structure file isssue on current Row can't build the ITR6 struture ");
			throw e ;
			}
		}

	
	public double getVersion() {
		return version;
	}


	public void setVersion(double version) {
		this.version = version;
	}


	public int getListid() {
		return listid;
	}


	public void setListid(int listid) {
		this.listid = listid;
		}


	public ArrayList<TableVarFlyWeight> getFlyWeight()
		{
		ArrayList<TableVarFlyWeight> data = new  ArrayList<TableVarFlyWeight>() ;
		
		for ( int i= 0 ; i <this.cellList.size() ;i++)
			{
			CellTemplate cc =  this.cellList.get(i);
			TableVarFlyWeight tt = new TableVarFlyWeight();
			
		
			tt.setList_data(this);			
			tt.setData_type(cc.datatype); 
			tt.setCol_index(cc.col);
			tt.setVar_name(cc.getVar());
			tt.setValidations(cc.validations);
			tt.setDescription(cc.getDesc());
			tt.setTag(cc.getTag());
			
			System.out.print("\nFlyWeight :::: ");
			tt.show();
			data.add(tt);
			}
		
		return data;		
		}

	
	public void loadFlyWeight(List<TableVarFlyWeight> data)
		{
		
		this.cellList.clear();
		this.cellMap.clear();
		
		for ( int i= 0 ; i < data.size() ;i++)
			{
			TableVarFlyWeight tw = data.get(i);
			
			String tag = tw.getTag() ;
			String var = tw.getVar_name();			
			String data_type = tw.getData_type();
			String colcellindex = tw.getCol_index();
			String validations = tw.getValidations();
			
			CellTemplate ct = new CellTemplate(tableVar, tag, var, data_type, var, rowIdx, colcellindex + "", "");
			ct.setValidations(validations);
			
			cellList.add(ct);
			cellMap.put(var, ct);
			
			}
		}

	
	public void loadFlyWeight()
		{
		
		this.cellList.clear();
		this.cellMap.clear();
		
		for ( int i= 0 ; i < this.fieldList.size() ;i++)
			{
			TableVarFlyWeight tw = fieldList.get(i);
					
			
			String var = tw.getVar_name();			
			String tag = tw.getTag();
			String data_type = tw.getData_type();
			String colcellindex = tw.getCol_index();
			String validations = tw.getValidations() ;
			
			//	if(validations != null ) { System.out.print("\nVFound=" + validations);}
			
			CellTemplate ct = new CellTemplate(tableVar, tag, var, data_type, var, rowIdx, colcellindex + "", "");
			ct.setValidations(validations);
			
			cellList.add(ct);
			cellMap.put(var, ct);			
			}
		}

	
	
	public ArrayList<CellTemplate> getCellList() {
		return cellList;
	}


	public void setCellList(ArrayList<CellTemplate> cellList) {
		this.cellList = cellList;
	}


	public HashMap<String, CellTemplate> getCellMap() {
		return cellMap;
	}


	public void setCellMap(HashMap<String, CellTemplate> cellMap) {
		this.cellMap = cellMap;
	}


	public String getTab() {
		return tab;
	}


	public void setTab(String tab) {
		this.tab = tab;
	}


	public String getTableVar() {
		return tableVar;
	}


	public void setTableVar(String tableVar) {
		this.tableVar = tableVar;
	}


	public int getLen() {
		return len;
	}


	public void setLen(int len) {
		this.len = len;
	}


	public int getRowIdx() {
		return rowIdx;
	}


	public void setRowIdx(int rowIdx) {
		this.rowIdx = rowIdx;
	}


	public void applyTemplate(XSSFWorkbook book, TaxListElement data, int rowOffset, int colOffset, String command) throws Exception 
		{		
		XSSFSheet ss = book.getSheet(this.tab);
		
		if(ss == null) throw new Exception ("DL 120331 : Actual sheet is not available : " + this.tab) ;
		
		for(int dataidx = 0 ; dataidx < data.length() ; dataidx++)
			{
			for(int i = 0 ; i < this.cellList.size() ; i++)
				{
				CellTemplate ct = this.cellList.get(i);
				
				ct.applyTemplate(ss, data.getRowValues(dataidx) , rowOffset + dataidx, colOffset, command);
				// ct.show();				
				}					
			}
		}


	public void show() {
		System.out.print("\nList ::Tab=" + tab + ",Var=" + tableVar + ",Len=" + len + ",Row=" + rowIdx + ",Ver=" + this.version + ",dbId="+ this.listid + ",ver=" + this.version + ",VL=" + this.viewLen) 	;
		System.out.print("\nTag ::" + this.tableTag );
		
		for( int i = 0 ; i < this.cellList.size() ; i++)
			{
			CellTemplate ct = this.cellList.get(i);
			ct.show();
			}
		System.out.print("");		
		}


	public void buildFlyWeight() {
		this.fieldList = this.getFlyWeight() ;		
	}

	
	public ArrayList<VMessage> validate(ValidationUtil V, TaxListElement data) throws Exception 
		{
		this.show();
		ArrayList<VMessage> messages = new ArrayList<VMessage>();
		for(int dataidx = 0 ; dataidx < data.length() ; dataidx++)
			{
			for(int i = 0 ; i < this.cellList.size() ; i++)
				{
				CellTemplate ct = this.cellList.get(i);	
				if(ct.validations == null || ct.validations.trim().length() ==0) continue;
				
				System.out.print("Table Validation Found :: " + ct.validations );
				
				VMessage msg = ct.validate(V, data.getRowValues(dataidx) );
				msg.setCategory(tab);
				msg.setVariable(this.tableVar + ":" + ct.var);
				msg.setIdx(dataidx + 1 );
				
				if(msg != null)
					{
					messages.add(msg);	
					}				
				}					
			}
		return messages ;		
		}



	public ArrayList<String> headers() 
		{
		ArrayList<String> colNames = new ArrayList<String>();
		for( int i = 0 ; i < this.cellList.size() ;i++)
			{
			colNames.add(this.cellList.get(i).var);
			}
		
		return colNames ;
		
		}



	public void applyTemplate(ITR6File itrfiledoc, TaxListElement data, String command) throws Exception {
				
		for(int dataidx = 0 ; dataidx < data.length() ; dataidx++)
			{
			for(int i = 0 ; i < this.cellList.size() ; i++)
				{				
				CellTemplate ct = this.cellList.get(i);
				
				if(ct.getTag() != null && ct.getTag().trim().length() > 0)
					{
					String tempTag = ct.getTag() ;				
					ct.setTag( this.tableTag + "[" +  (dataidx+1) + "]" + ct.getTag() );
					
					ct.applyTemplate(itrfiledoc, data.getRowValues(dataidx), command);
	
					
					
					ct.setTag(tempTag);
					}
				}					
			}
	}
	
	
	
	}
