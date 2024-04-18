package com.anemoi.itr;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import com.anemoi.util.Util;

public class TaxElement implements Serializable{

	int projectId ;
	String sheet ; 	
	String var	 ;
	String desc  ;	
	Object value ;
	String valueType ;
	String valueStr ;		
	
	public TaxElement() {
		
		}
	
	public String getValueStr() {		
		return valueStr;
		}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
		this.value = valueStr ;
		this.valueType = "String" ;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getSheet() {
		return sheet;
	}

	public void setSheet(String sheet) {
		this.sheet = sheet;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public TaxElement(String tab, String var1, String desc1) 
		{
		sheet = tab ;
		var = var1 ;
		desc = desc1 ;
		}
	
	public void setValue(Cell cell) 
		{		

		String objType = cell.getClass().toString() ;
		System.out.print("\nCell Type =" + cell.getCellType() );
		
		switch (cell.getCellType()) {
		
		
		case BOOLEAN:			
			valueType = "Boolean" ;					
			value = cell.getBooleanCellValue()	;
			valueStr = value.toString() ;			
			System.out.print("\nTaxElement::" + cell.getBooleanCellValue() + " --> Type ::" + valueType + "-->" + valueStr);			
			break;

		case STRING:
			value = cell.getRichStringCellValue().getString() ;
			valueType = "String" ;
			valueStr = value.toString() ;
			System.out.print("\nTaxElement::" + cell.getRichStringCellValue().getString() + " --> Type ::" + valueType  + "-->" + valueStr);
			break;

		case NUMERIC:

			if (DateUtil.isCellDateFormatted(cell)) 
				{
				Date dd = cell.getDateCellValue();

				value = cell.getDateCellValue() ;
				valueType = "Date" ;			
				valueStr = Util.formatter.format(dd);
				System.out.print("\nTaxElement::" + dd + " --> Type ::" + valueType + "  Name ::" + this.var +   "-->" + valueStr);
				} 
			else {
				double dd = cell.getNumericCellValue();
					
				DecimalFormat decimalFormat = new DecimalFormat("##############");
				if(dd%10 != 0)
					{
					decimalFormat = new DecimalFormat("##############.##");
					}
				
				cell.setCellType(CellType.STRING);
				//	value = cell.getNumericCellValue();
				value = cell.getStringCellValue() ;
				
				valueStr = decimalFormat.format(dd);
				valueType = "Double" ;								
				System.out.println("\nTaxElement::" +  dd + " --> Type ::" + valueType + "  Name ::" + this.var  + "-->" + valueStr);
				}
			break;

		case FORMULA:
				switch( cell.getCachedFormulaResultType() )
						{
						case NUMERIC :
							if (DateUtil.isCellDateFormatted(cell)) 
								{
								Date dd = cell.getDateCellValue();
	
								value = cell.getDateCellValue() ;
								valueType = "Date" ;			
								valueStr = Util.formatter.format(dd);
								System.out.print("\nTaxElement::" + dd + " --> Type ::" + valueType + "  Name ::" + this.var +   "-->" + valueStr);
								} 
							else {
								double dd = cell.getNumericCellValue();
									
								DecimalFormat decimalFormat = new DecimalFormat("##############");
								if(dd%10 != 0)
									{
									decimalFormat = new DecimalFormat("##############.##");
									}
								
								cell.setCellType(CellType.STRING);
								//	value = cell.getNumericCellValue();
								value = cell.getStringCellValue() ;
								
								valueStr = decimalFormat.format(dd);
								valueType = "Double" ;								
								System.out.println("\nTaxElement::" +  dd + " --> Type ::" + valueType + "  Name ::" + this.var  + "-->" + valueStr);
								}
							break;
	
							
						case STRING :
							value = cell.getRichStringCellValue().getString() ;
							valueType = "String" ;
							valueStr = value.toString() ;
							System.out.print("\nTaxElement::" + cell.getRichStringCellValue().getString() + " --> Type ::" + valueType  + "-->" + valueStr);
							break;
						}
			     break;

		case BLANK:
			System.out.print("");
			break;
		default:
			System.out.print("");
		}

		System.out.print("\t");
	}
	

	
	public void setValue(String vv ) 
		{
		value = vv ;
		valueStr = vv;
		valueType = "String" ;
		}

		
	
	public void show() 
		{
		if(value == null )			
			System.out.print("\nData :" + sheet + "," + var + "," + desc + ",NULL" + ",PID=" + this.projectId);	
		else
			System.out.print("\nData :" + sheet + "," + var + "," + desc + "," + value.toString() + ",PID=" + this.projectId);
		}
	
	

}
