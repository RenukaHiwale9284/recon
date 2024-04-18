package com.anemoi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.anemoi.data.Trail;

public class Util {

	public static HashMap<String, Integer> Limits = new HashMap<String, Integer>();

	public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public static SimpleDateFormat xmlDataFormatter = new SimpleDateFormat("yyyy-MM-dd");

	private  static HashMap<String, String> properties = new HashMap<String, String>();
	
	private static HashMap<String, String> validproperties ;
	
	private static ArrayList<Trail> logs = new ArrayList<Trail> ();
		
	public static String getProp(String key)
		{
		return properties.get(key);		
		}
	
	
	public static boolean readSheet(Sheet ss) throws Exception {
		// Create a DataFormatter to format and get each cell's value as String

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
		Iterator<Row> rowIterator = ss.rowIterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				printCellValue(cell);
			}
			System.out.println();
		}
		return false;

	}
	
	

	public static void load() throws Exception {
		
		System.out.print("\nLoad the Util Configuration:");
		 // Initialize properties object
		Properties prop = new Properties();

		// Load properties from file
		prop.load(new FileInputStream("syspath.properties"));

		
		Limits.put("Template_Values_Row_Limit", 5000);

		 // You can access properties "output_folder"
		String attr = prop.getProperty("output_folder");

		if((new File(attr)).isDirectory() && !attr.contains("windows") && !attr.contains("system") && !attr.contains("root"))		
			{
			properties.put("output_folder", attr);
			}
			
		System.out.print("\nUTIL-Attr:" + attr);
		
		// You can access properties "projectData"
		attr = prop.getProperty("projectData");
		if((new File(attr)).isDirectory() && !attr.contains("windows") && !attr.contains("system") && !attr.contains("root"))		
			{			
			properties.put("projectData", attr);
			}

		System.out.print("\nUTIL-Attr:" + attr);
	}

	
	
	public static void loadOld() {

		Limits.put("Template_Values_Row_Limit", 500);

		properties.put("output_folder", "client");

		properties.put("projectData", "/home/devzone/MyCenter/data/itr6files");

	}

	public static HashMap<String, String> getProperties() {
		return properties;
	}

	public static void printCellValue(Cell cell) {

		System.out
				.print("CELL[" + cell.getRowIndex() + "," + cell.getColumnIndex() + "] =" + cell.getCellType().name());

		switch (cell.getCellType()) {

		case BOOLEAN:
			System.out.print(cell.getBooleanCellValue());
			break;
		case STRING:
			System.out.print(cell.getRichStringCellValue().getString());
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				System.out.print(cell.getDateCellValue());
			} else {
				System.out.print(cell.getNumericCellValue());
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

		System.out.print("\t");
	}

	public static String uniqFileName(String name, String ex) {
		Date dd = new Date();
		SimpleDateFormat uniqFname = new SimpleDateFormat("ddMMyy-hhmmss");
		return name + "_" + uniqFname.format(dd) + "." + ex;
	}

	public static boolean isEmptyRow(Row rr) {

		if (rr == null)
			return true;

		if (rr.getCell(0) == null)
			return true;

		if (rr.getCell(0).getCellType().name().equalsIgnoreCase("EMPTY"))
			return true;

		return false;
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
	
		if (destFile.exists()) {
			destFile.delete();
			}		
	
		if (!destFile.exists()) {
			destFile.createNewFile();
			}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new RandomAccessFile(sourceFile, "rw").getChannel();
			destination = new RandomAccessFile(destFile, "rw").getChannel();

			long position = 0;
			long count = source.size();

			source.transferTo(position, count, destination);
			} 
		finally {
			if (source != null) {
				source.close();
				}
			if (destination != null) {
				destination.close();
				}
			}
		}

	public static String generateRandomPassword(int len) {
		// ASCII range – alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDXYZ";
		final String nums = "56789";
		final String symbols = "@$#";
		final String smallChars = "pqrs";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		// each iteration of the loop randomly chooses a character from the given
		// ASCII range and appends it to the `StringBuilder` instance

		for (int i = 0; i < len/2; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		for (int i = 0; i < len/2; i++) {
			int randomIndex = random.nextInt(nums.length());
			sb.append(nums.charAt(randomIndex));
		}

		
		for (int i = 0; i < 1; i++) {
			int randomIndex = random.nextInt(symbols.length());
			sb.append(symbols.charAt(randomIndex));
		}
		
		for (int i = 0; i < len/4; i++) {
			int randomIndex = random.nextInt(smallChars.length());
			sb.append(smallChars.charAt(randomIndex));
			}
		
		return sb.toString();
	}

	public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

		// append = false
		try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
			int read;
			byte[] bytes = new byte[5000];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		}
	}
	
	
	public static ArrayList<Trail> pullLogs()
		{
		ArrayList<Trail> tempArrayList = new ArrayList<Trail>();
		tempArrayList.addAll(logs) ;
		logs.clear();
		return tempArrayList ;
		}
	
	
	public static void log(String event, String msg, String details) throws IOException 
		{
		Trail tt = new Trail();
		
		tt.setMsgType(event);
		tt.setMsg(msg);
		tt.setDetails(details);
		tt.setThreadId( Thread.currentThread().getId() ) ;
		tt.setTimestamp(new Timestamp(System.currentTimeMillis()));
		
		logs.add(tt);
		}
}
