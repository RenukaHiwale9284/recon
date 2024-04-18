package com.anemoi.itr.ref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.storage.HibernateStore;
import com.anemoi.itr.validation.GenValidator;
import com.anemoi.util.Util;

public class RefDataUtiity {

	private double itrVersion ;
	
	private HashMap<String, HashMap<String, String>> dataMaps = new HashMap<String, HashMap<String, String>>();

	private static RefDataUtiity singletone;

	public static RefDataUtiity one() {

		if (singletone == null)
			singletone = new RefDataUtiity();
		return singletone;
	}

	public void load(File excel) throws Exception {
		
		System.out.print("\nRefrence data file loaded :" + excel.getAbsolutePath());

		FileInputStream file = new FileInputStream(excel);

		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		Iterator<Sheet> iterator = workbook.sheetIterator();
		dataMaps.clear(); 
		
		while (iterator.hasNext()) {			
			Sheet ss = iterator.next();
			
			String tag = ss.getSheetName() ; 
			
			HashMap<String,String> keymap = new HashMap<String,String>();
			dataMaps.put(tag, keymap);
			
			int rowlomit = Util.Limits.get("Template_Values_Row_Limit");

			for (int i = 1; i < rowlomit; i++) {

				Row rr = ss.getRow(i);
				
				if(rr == null) break ;
				if(rr.getCell(0).getCellType().equals(CellType.BLANK)) break ;
				rr.getCell(0).setCellType(CellType.STRING);
				String key = rr.getCell(0).getStringCellValue();

				rr.getCell(1).setCellType(CellType.STRING);
				String value = rr.getCell(1).getStringCellValue();

				System.out.print("\nRow(" + i + ")=" + key + "," + value);
				
				keymap.put(key, value);
				}
			
			System.out.println("\nTAG-" + tag);
			}
		}
		
	public void store(HibernateStore hs)
		{
		ArrayList<RefData> lst = new ArrayList<RefData>();
		
		for(String tag : this.dataMaps.keySet())
			{
			HashMap<String,String> keymap = this.dataMaps.get(tag);
			for(String key : keymap.keySet())
				{				
				RefData rdata = new RefData();
				
				rdata.setKey(key);
				rdata.setTag(tag);
				rdata.setValue(keymap.get(key));
				lst.add(rdata);
				}
			}
		
		hs.storeRefData(lst);		
		}

	
	public void load(HibernateStore hs)
		{		
		List<RefData> lst = hs.readRefData();
		
		for(RefData data : lst)
			{
						
			if(!dataMaps.containsKey( data.getTag()))
				{				
				this.dataMaps.put(data.getTag(), new HashMap<String,String>());
				}
			
			HashMap<String,String> keymap = this.dataMaps.get(data.getTag() );
			
			keymap.put( data.getKey(), data.getValue() );
			}
		}
		
	

	public static void main(String[] argv) throws Exception {
		Util.load();

		File f = new File("/home/devzone/MyCenter/release/SET1/ITR6_refdata.xlsx");
		RefDataUtiity.one().load(f);

		HibernateStore hs = new HibernateStore(0,6.35) ;
		RefDataUtiity.one().store(hs);
		
		}

	public static boolean isEnum(String string, String nature) {
		// TODO Auto-generated method stub
		return true;
	}

	
	public String keyMapping(String key, String data)
		{
		System.out.print("\nKeyMapping[" + key + ","+data+ "]" );
		HashMap<String, String> pairs = dataMaps.get(key);
		
		if (pairs == null)
			{
			System.out.print("\nNo Mapping : ");
			return data;
			}
		
		System.out.print("\nMapping=" + pairs.get(data));

		if(pairs.get(data) == null) 
			{
			return data ;
			}
		
		return (pairs.get(data));		
		}
	
	public String transform(String commands, String data) {

		String transkey = getTransformation(commands);
		if (transkey == null)
			return data;

		HashMap<String, String> pairs = dataMaps.get(transkey);
		if (pairs == null)
			return data;

		return (pairs.get(data));
	}

	String getTransformation(String commands) {
		System.out.print("\nRef Check :: " + commands + " on ");

		String[] vlist = commands.split(":");
		boolean flag = true;
		GenValidator g = null;

		for (String vname : vlist) {
			vname = vname.trim();

			if (vname.startsWith("TRANS")) {
				String[] dd = commands.split("-");
				if (dd.length != 2)
					return null;

				return dd[1];
			}
		}
		return null;
	}
}