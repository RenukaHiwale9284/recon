package com.anemoi.document;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.anemoi.util.Util;

public class DirectoryStore {

	File clientDataDirectory;

	File configFile;
	File itr6File;
	File data;
	File itr6Structure;
	File itr6EmptyTemplate;
	int projectId = 0;
	double itrVersion = 6.0 ;
	
	HashMap<String, File> documents = new HashMap<String, File>();

	public DirectoryStore(File clientDir) {
		clientDataDirectory = clientDir;
	}

	public void replaceDataFile(File f) throws IOException {
		Util.copyFile(f, this.data);
	}

	public void replaceITRStructureFile(File f) throws IOException {
		System.out.print("\nF1=" + this.itr6Structure.getAbsolutePath() + "\tF2=" + f.getAbsolutePath());
		Util.copyFile(f, this.itr6Structure);
	}

	public void replaceDocumentFile(File doc, String tag) throws Exception {
		if (!this.documents.containsKey(tag)) {
			throw new Exception("Document tag " + tag + " is not allowed");
		}

		File f = this.documents.get(tag);
		Util.copyFile(doc, f);
	}

	public File getDocument(String tag) throws Exception {

		if (!this.documents.containsKey(tag)) {
			throw new Exception("Document tag " + tag + " is not allowed");
		}
		File f = this.documents.get(tag);
		return f;
	}

	public DirectoryStore(int pid, double version) throws Exception {

		this.projectId = pid;
		this.itrVersion = version ;

		File projectParentDir = new File(Util.getProperties().get("projectData"));

		File itr6VersionDirectory = new File(projectParentDir, "versions");

		if (!projectParentDir.exists())
			throw new Exception("ERR:492343 Project parent directory doesn't exit " + projectParentDir);

		File clientDataDirectory = new File(projectParentDir, projectId + "");

		if (!projectParentDir.exists())
			throw new Exception("ERR:459204 client data directory doesn't exit " + clientDataDirectory);

		configFile = new File(clientDataDirectory, "ITR6_Config.xlsx");
		itr6File = new File(clientDataDirectory, "ITR6_Template.xlsm");
		data = new File(clientDataDirectory, "ITR6_Data.xlsx");

		
		itr6Structure = new File(itr6VersionDirectory, "ITR6_structure " + version + ".xlsx");
		itr6EmptyTemplate = new File(itr6VersionDirectory, "ITR6_Empty_Template " + version + ".xlsm");
		
		
		File f = new File(clientDataDirectory, "26AS.txt");
		documents.put("26ASTXT", f);
		
		f = new File(clientDataDirectory, "26AS.pdf");
		documents.put("26ASPDF", f);

		f = new File(clientDataDirectory, "SalesRegister.xlsx");
		documents.put("SR", f);

		f = new File(clientDataDirectory, "RPT.xlsx");
		documents.put("RPTXLS", f);
				
		f = new File(clientDataDirectory, "itr6_data.xml");
		documents.put("ITR6XML", f);

		f = new File(clientDataDirectory, "itr6.xlsx");
		documents.put("ITR6XLS", f);

		f = new File(clientDataDirectory, "itr6Final.xlsm");
		documents.put("GENITR6XLS", f);
		
		
	}

	public static String createProject(int pid, double version) throws Exception {

		File projectParentDir = new File(Util.getProperties().get("projectData"));
		File itr6VersionDirectory = new File(projectParentDir, "versions");

		if (!projectParentDir.exists())
			throw new Exception("ERR:492343 Project parent directory doesn't exit " + projectParentDir);

		File projDir = new File(projectParentDir, pid + "");

		if (projDir.isDirectory())
			throw new Exception("ERR:430234 Project is already present, Kindy provide another project id");

		boolean flag = projDir.mkdir();

		if (!flag)
			throw new Exception("ERR:352342 Project directory can't be created. Technical error");

		
		File itrFile = new File(projDir, "ITR6_Template.xlsm");
		File srcTempFileFile = new File(itr6VersionDirectory, "ITR6_Empty_Template " + version + ".xlsm");
		
		
		Util.copyFile(srcTempFileFile, itrFile);

		
//		File configFile = new File(cd, "ITR6_Config.xlsx");
//		File srcConfigFile = new File(itr6VersionDirectory, "ITR6_Config.xlsx");
//		Util.copyFile(srcConfigFile , configFile);

		String msg = "Success : Project is created on directory system";

		return msg;
	}

	public void loadProject(int projId) {
		this.projectId = projId;
	}

	public static void main(String[] argv) {
		String clientdir = "/home/devzone/MyCenter/data";

	}

	public File getConfigFile() {
		return null;
	}

	public File getITR6File() {
		return itr6File;
	}

	public File getClientDataDirectory() {
		return clientDataDirectory;
	}

	public void setClientDataDirectory(File clientDataDirectory) {
		this.clientDataDirectory = clientDataDirectory;
	}

	public File getItr6File() {
		return itr6File;
	}

	public void setItr6File(File itr6File) {
		this.itr6File = itr6File;
	}

	public File getData() {
		return data;
	}

	public void setData(File data) {
		this.data = data;
	}

	public File getItr6Structure() {
		return itr6Structure;
	}

	public void setItr6Structure(File itr6Structure) {
		this.itr6Structure = itr6Structure;
	}

	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public HashMap<String, File> getDocuments() {
		return documents;
	}

	public void setDocuments(HashMap<String, File> documents) {
		this.documents = documents;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void replaceITREmptyTemplateFile(File f) throws Exception {
		System.out.print("\nF1=" + this.itr6Structure.getAbsolutePath() + "\tF2=" + f.getAbsolutePath());
		Util.copyFile(f, this.itr6EmptyTemplate);
	}

	public File getItr6EmptyTemplate() {
		return itr6EmptyTemplate;
	}

	public void setItr6EmptyTemplate(File itr6EmptyTemplate) {
		this.itr6EmptyTemplate = itr6EmptyTemplate;
	}

	public double getItrVersion() {
		return itrVersion;
	}

	public void setItrVersion(double itrVersion) {
		this.itrVersion = itrVersion;
	}

}
