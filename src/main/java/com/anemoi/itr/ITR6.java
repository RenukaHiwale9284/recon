package com.anemoi.itr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.anemoi.itr.ref.RefDataUtiity;
import com.anemoi.itr.storage.ActionITR6Reading;
import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.itr.storage.HibernateStore;
import com.anemoi.itr.storage.InfoStore;
import com.anemoi.itr.validation.VMessage;
import com.anemoi.itr.validation.ValidationUtil;
import com.anemoi.util.Util;

public class ITR6 
{	
	File itr6File ;
	
	HashMap<String,Integer> Limits = new HashMap<String,Integer>();
	
	String[] grossReturnSheets = {"PART A - GENERAL", "GENERAL2", "NATURE OF BUSINESS", "OS", "SI", "EI", "FD", "PARTB - TI - TTI", "IT", " Verification"};
		
	public ITR6(File ff)
		{
		itr6File = ff ;
		}

	
	public ITR6() 
		{

		}
	
	
	public void processData(File template, File conffile, File dataFile) throws Exception
		{	
		ITR6DataSet data = new ITR6DataSet() ;
		
		
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		
		conf.load(conffile) ;
		data.load(dataFile) ;
		
		structure.load(template);
		
		
		FileInputStream file = new FileInputStream(itr6File);
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
        String command = "write" ;
        structure.applyListTemplates(workbook, data ,conf, command);
        
        structure.applyCellTemplates(workbook, data, conf, command);
        
		String fName = Util.uniqFileName("ITR6","xlsm");//unique file name by date
		String folder = Util.getProp("output_folder");			
		
		if(folder.contains("windows") || !folder.contains("system") || !folder.contains("root") || folder.startsWith("..\\"))
			return ;

		if(fName.contains("windows") || !fName.contains("system") || !fName.contains("root") || fName.startsWith("..\\"))
			return ;
		
		
		try 
			{	
				File ofile = new File (folder, fName) ;
				
				System.out.print("\n Output file :: " + ofile.getAbsolutePath() );
				FileOutputStream outputStream = new FileOutputStream(ofile) ;
	            workbook.write(outputStream);
	            outputStream.close();
	            file.close();
	            data.close();
	            structure.close();
	            
	        }
		catch(Exception ex)
			{
			ex.printStackTrace();
			}
		}
	
	
	public boolean validate() throws Exception
		{
		if (!itr6File.exists()) 
			throw new Exception("ITR60001 : ITR6 file doesn't exist in paht," + itr6File.getAbsolutePath());
		
		FileInputStream file = new FileInputStream(itr6File);
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        // 2. Or you can use a for-each loop
        System.out.println("Retrieving Sheets using for-each loop");
        for(Sheet sheet: workbook) {
            System.out.println("=> " + sheet.getSheetName());
            Util.readSheet(sheet);
        	}
        
        workbook.close();
        return false ;
		} 

		
	
	
	public static void main(String[] argv) throws Exception
		{
		Util.load();//output_folder=client and proj Data=/home/devzone/MyCenter/data/itr6files prop file
		System.out.println();
		System.out.println("rgjjijsdfjks");		
		String inputFolder = "input/" +  "data4" ;
		
		int arglen = argv.length; 
	
		if ( arglen > 0  )  inputFolder = "input/" +  argv[0] ;

		if(inputFolder.contains("windows") || !inputFolder.contains("system") || !inputFolder.contains("root") || inputFolder.startsWith("..\\"))
			return ;
//		System.out.println();
//		System.out.println(".....  rgjjijsdfjks");
		
		File configFile = new File(inputFolder, "ITR6_Config.xlsx");
		File itr6File = new File(inputFolder,"ITR6_template.xlsm") ; 
		File structure = new File("ITR6_structure.xlsx") ;
		File data = new File(inputFolder,"ITR6_data.xlsx") ;
		
		
		ITR6 RET = new ITR6(itr6File) ;
		
		RET.processData(structure , configFile, data );
		
//		RET.validate();
		}

	

	public void executeCommand(DirectoryStore dstore, HibernateStore hstore, String commands ) throws Exception 
		{
		
		if (commands.equalsIgnoreCase("all"))
			{
			commands = "FDL,FCL,FSL,HTS,HCS,HDS,HSS" ;
			}		
		
		ITR6DataSet data = new ITR6DataSet() ;		
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		String[] instructions = commands.split(",");
		
		for(int i = 0 ; i < instructions.length ; i++)
			{
			String C = instructions[i];
			
			switch(C.toUpperCase())
				{
				
				case "FDL" : 
					System.out.print("\nFile Data Load");
				
					data.load(dstore) 		;		
					break;
					
				case "FCL" :
					System.out.print("\nFile Configuration Load");
					conf.load(dstore) 		;
					break ;
				
				case "FSL" :					
					System.out.print("\nFile Structure Load");
					structure.load(dstore)	;
					
					structure.showCellRef();
					
					structure.showListTemplates();					
					break ;
				
				case "HDL" : 
					System.out.print("\nDB Data Load");
					//load db data InfoStore 
					data.load(hstore) 		;		
					break;
					
				case "HCL" :
					System.out.print("\nDB Configuration Load");
					conf.load(hstore) 		;
					break ;
				
				case "HSL" :					
					System.out.print("\nDB Structure Load");
					structure.load(hstore)	;
					break ;
					
					
				case "HTS" :					
					System.out.print("\nDB Table Store");
					hstore.storeTables(data, conf);
					break ;
				
				case "HCS" :
					System.out.print("\nDB Configuration Store");
					//TemplateConfig
					hstore.store(conf);
					break ;
				
				case "HDS" :
					System.out.print("\nDB Data Store");
					//ITR6DataSet
					hstore.store(data);		
					break ;
					
				case "HSS" :					
					System.out.print("\nDB Structure Store");
					hstore.store(structure);	
					break ;				
				}
			}		
		}
		
	
	public void readITR6xls(DirectoryStore dstore, HibernateStore hstore) throws Exception
		{
		ITR6DataSet data = new ITR6DataSet() ;
				
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		conf.load(hstore) ;
		structure.load(hstore);				
		conf.setTemplate(structure);
		
		//retrieves the TableConfiguration object at index
		conf.showTableOffset();
		//retrieves the CellTemplate object at index i from the list  nd assign ct
		structure.showCellRef();
		//retrieves the ListTemplate object at index i from the list  nd assign lt
		structure.showListTemplates();
		data.setTableStore(hstore);
		
		//check key "ITR6XLS" return file obj
		File itr6FileInputFile 	= dstore.getDocument("ITR6XLS") ;
		FileInputStream file 	= new FileInputStream(itr6FileInputFile);
        XSSFWorkbook workbook 	= new XSSFWorkbook(file);
		 
        //Create Workbook instance holding reference to .xlsx file
        
        conf.buildTableConfigurations(workbook);
        
        
        String command = "read" ;

      //calculates row and column offsets 
        structure.applyCellTemplates(workbook, data, conf, command);      
        conf.showTableOffset();
        
        structure.applyListTemplates(workbook, data ,conf, command);
        
        System.out.print("\n\n :::::::::::::::::::::::: All the tax elements :::: ");     
        
        data.showValues();
        hstore.store(data);                
		}
	
	
	public File validateData(InfoStore dstore) throws Exception 
		{
		ITR6DataSet data = new ITR6DataSet() ;
				
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		conf.load(dstore) ;
		data.load(dstore) ;
		structure.load(dstore);				
		data.setTableStore(dstore);
		
		structure.showCellRef();
		structure.showListTemplates();
		
		ValidationUtil V = new ValidationUtil();
		ArrayList<VMessage> messages = structure.validateDataset( V, data, conf);
		
		showValidations(messages);
		
		File f = generateValidationFile(messages);
		return f ;
		}
	
		
	private void showValidations(ArrayList<VMessage> messages) {
		System.out.print("\nFinal Validation messages :::::::::::::::::::::::::");
		for(VMessage v : messages)
			{
			v.show();
			}
		
	}

//configuration file and then we process that data to perform validation checks 
	public File generateValidationFile(ArrayList<VMessage> vmsg)
		{
		String fName = Util.uniqFileName("ITR6Validation","csv");
		String folder = Util.getProp("output_folder");			
		File ofile = new File (folder, fName) ;
		
		try 
			{	
			System.out.print("\n Output file :: " + ofile.getAbsolutePath() );
			//write data to the output file
			FileWriter writer = new FileWriter(ofile) ;
			
			for(int i = 0 ; i < vmsg.size() ;i++)
				{
				VMessage v = vmsg.get(i);
				//read lines from the cong file
				StringBuffer vline = new StringBuffer();
				vline.append(i + ",");
				vline.append(v.getCategory() + ",");
				vline.append(v.getVariable() + ",");
				vline.append(v.getValue() + ",");
				vline.append(v.getMessage() + ",");
				vline.append(v.getSeverity() + ",");
				vline.append(v.getAction() + "");
				vline.append("\n");
				
				writer.write(vline.toString());

				}
			writer.close();
	        }
		catch(Exception ex)
			{
			ex.printStackTrace();
			}
		return ofile ;
		}
	
	public File processData(InfoStore dstore) throws Exception 
		{
		ITR6DataSet data = new ITR6DataSet() ;
		
		
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		conf.load(dstore) ;
		data.load(dstore) ;
		structure.load(dstore);		
		data.setTableStore(dstore);
				
		itr6File = dstore.getITR6File();
				
		FileInputStream file = new FileInputStream(itr6File);
		 
        //Create Workbook instance holding reference to .xlsx file
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        
        
        String command = "write" ;
        structure.applyListTemplates(workbook, data ,conf, command);

        structure.applyCellTemplates(workbook, data, conf, command);
        
               
		String fName = Util.uniqFileName("ITR6","xlsm");
		String folder = Util.getProp("output_folder");			
		File ofile = new File (folder, fName) ;
				
		try 
			{					
			System.out.print("\n Output file :: " + ofile.getAbsolutePath() );
			FileOutputStream outputStream = new FileOutputStream(ofile) ;
            workbook.write(outputStream);
            outputStream.close();
            file.close();
            data.close();
            structure.close();
	            
	        }
		catch(Exception ex)
			{
			ex.printStackTrace();
			}
		
		return ofile ;
		}


	public void buildDefaultConfig(DirectoryStore dstore, HibernateStore hstore) throws Exception {
		
		ITR6 itr = new ITR6();
		
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		
		structure.load(hstore);
		conf.setTemplate(structure);
		
		structure.showCellRef();
		structure.showListTemplates();
				
		conf.buildDefaultConfigurations(); 		
		
		hstore.store(conf);
		
		}


	
	public void readITR6xml(DirectoryStore dstore, HibernateStore hstore) throws Exception {
		
		RefDataUtiity.one().load(hstore);
		
		ITR6DataSet data = new ITR6DataSet() ;
		
		
		TemplateConf conf = new TemplateConf();				
		SheetTemplate structure = new SheetTemplate() ;
		
		conf.load(hstore) ;
		structure.load(hstore);				
		conf.setTemplate(structure);
				
		conf.showTableOffset();
		structure.showCellRef();
		structure.showListTemplates();
		
		data.setTableStore(hstore);
				
		// Deleting ITR6 related project data.	
		hstore.deleteProjectData();		
		
		File itr6FileInputFile = dstore.getDocument("ITR6XML") ;
		XMLITR6File xfile = new XMLITR6File();
		xfile.init(itr6FileInputFile, "read");
        
		
		
        conf.buildTableConfigurations(xfile);
		
		String command = "read" ;
		
        structure.applyCellTemplates(xfile, data, conf, command);      
        conf.showTableOffset();
                
        structure.applyListTemplates(xfile, data ,conf, command);
        
        System.out.print("\n\n :::::::::::::::::::::::: All the tax elements :::: ");     
        
        data.showValues();
        hstore.store(data);                
		}
	}
