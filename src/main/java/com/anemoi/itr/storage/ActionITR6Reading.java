package com.anemoi.itr.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.CellTemplate;
import com.anemoi.itr.ITR6DataSet;
import com.anemoi.itr.ListTemplate;
import com.anemoi.itr.TableConfiguration;
import com.anemoi.itr.TaxElement;

public class ActionITR6Reading extends ITR6Visitor
{
	
	XSSFWorkbook book = null ;
	ITR6DataSet dataset ;
	
	ActionITR6Reading(File xlsFile, ITR6DataSet dd) throws IOException
		{
		FileInputStream file = new FileInputStream(xlsFile);
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
        this.dataset = dd ;
		}
	
	
	public ActionITR6Reading() {
		// TODO Auto-generated constructor stub
	}


	public void visit(CellTemplate ct) {
		
		

		}

		
	

	public TableConfiguration loadConfig(ListTemplate lt) {

		System.out.print("\nVisted :: ");
		lt.show(); 	
		
		return null ;
		}

	
	public void setData(TableConfiguration tc) {
		System.out.print("\nSet Data :::::: ");
		
	}

	public void visitListCell(CellTemplate ct) {
		System.out.print("\nVisit List Cell ::::: ");
		ct.show();; 				
	}


}
