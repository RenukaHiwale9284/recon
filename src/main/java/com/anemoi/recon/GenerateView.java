package com.anemoi.recon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultStreamedContent.Builder;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.SerializableSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.HibernateReader;
import com.anemoi.data.PartyRecord;
import com.anemoi.data.TaxDocument;
import com.anemoi.itr.ITR6;
import com.anemoi.itr.TableConfiguration;
import com.anemoi.itr.TaxElement;
import com.anemoi.itr.TemplateConf;
import com.anemoi.itr.ref.RefData;
import com.anemoi.itr.ref.RefDataUtiity;
import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.itr.storage.GenRowData;
import com.anemoi.itr.storage.HibernateStore;
import com.anemoi.itr.storage.InfoStore;
import com.anemoi.tqm.TaxPosition;
import com.anemoi.util.Util;

@Component("gview")
@ManagedBean
@SessionScope

public class GenerateView {

	double itrVersion = 6.35;

	@Autowired
	private LoginView father;

	List<TableConfiguration> tableConfig = new ArrayList<TableConfiguration>();

	List<TaxDocument> genITRFiles = new ArrayList<TaxDocument>();

	TaxDocument selectedTaxDocument;

	StreamedContent itr6TemplateStreamedFile;
	FileInputStream itr6TemplateStream;

	StreamedContent itr6GeneratedStreamedFile;
	FileInputStream itr6GeneratedStream;

	
	boolean adminView = false;

	int projectId;

	public StreamedContent getItr6GeneratedStreamedFile() {

		try {
			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);

			File f2 = ds.getDocument("GENITR6XLS");
			if (f2.exists()) {
				itr6GeneratedStream = new FileInputStream(f2);
				itr6GeneratedStreamedFile = DefaultStreamedContent.builder().name("FinalITR6.xlsm")
						.contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
						.stream(() -> itr6GeneratedStream).build();

				System.out.print("\nDownloaded ITR6 Generated File ::" + this.itr6GeneratedStreamedFile.getName());
			}
			else
				this.showMessage("ERR43033 : No Generated File exists, Kindly generate the ITR6 excel file" );

				
		} catch (Exception ex) {
			ex.printStackTrace();
			this.showMessage(
					"ERR43033 : Error in generatig ITR6 stream, kindly contact adminstrator" + ex.getMessage());

		}
		return itr6GeneratedStreamedFile;
	}

	public StreamedContent getItr6TemplateStreamedFile() throws Exception {

		try {
			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);

			itr6TemplateStream = new FileInputStream(ds.getItr6EmptyTemplate());
			itr6TemplateStreamedFile = DefaultStreamedContent.builder().name("ITR6Template.xlsm")
					.contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
					.stream(() -> itr6TemplateStream).build();

			System.out.print("\nDownloaded ITR6 Empty File ::" + this.itr6TemplateStreamedFile.getName());

		} catch (Exception ex) {
			ex.printStackTrace();
			this.showMessage("ERR43033 : Error in generatig ITR6 Template stream, kindly contact adminstrator"
					+ ex.getMessage());

		}

		return itr6TemplateStreamedFile;
	}

	public void refreshStreamedFiles() throws Exception {

		/*
		 * DirectoryStore ds = new DirectoryStore(projectId, itrVersion);
		 * 
		 * itr6TemplateStream = new FileInputStream(ds.getItr6EmptyTemplate());
		 * itr6TemplateStreamedFile =
		 * DefaultStreamedContent.builder().name("ITR6Template.xlsm") .contentType(
		 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
		 * .stream(() -> itr6TemplateStream).build();
		 * 
		 * System.out.print("\nDownloaded ITR6 Empty File ::" +
		 * this.itr6TemplateStreamedFile.getName()); // itr6TemplateStream.close();
		 * 
		 * File f2 = ds.getDocument("GENITR6XLS"); if(f2.exists()) { itr6GeneratedStream
		 * = new FileInputStream(f2); itr6GeneratedStreamedFile =
		 * DefaultStreamedContent.builder().name("FinalITR6.xlsm") .contentType(
		 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
		 * .stream(() -> itr6GeneratedStream).build();
		 * 
		 * System.out.print("\nDownloaded ITR6 Generated File ::" +
		 * this.itr6GeneratedStreamedFile.getName()); // itr6GeneratedStream.close(); }
		 * 
		 */

	}

	public void printStream(StreamedContent stream) throws IOException {
		InputStream is = stream.getStream();

		if (is == null)
			System.out.print("\nNULL  Stream content :: " + stream.getName());

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}

		// Java 1.1
		System.out.print(result.toString(StandardCharsets.UTF_8.name()));

	}

//	@PostConstruct
	public void init() {
		projectId = 217;

		this.reset();

		genITRFiles.add(new TaxDocument(0, this.projectId, "ITR6XLS", "230234123", new Date(), "AbhimanyaVishram"));
		genITRFiles.add(new TaxDocument(0, this.projectId, "ITR6XLS", "3331023", new Date(), "AbhimanyaVishram"));
		genITRFiles.add(new TaxDocument(0, this.projectId, "ITR6XLS", "32340032", new Date(), "AbhimanyaVishram"));
		genITRFiles.add(new TaxDocument(0, this.projectId, "ITR6XLS", "4203423", new Date(), "AbhimanyaVishram"));

	}

	public void loadData() {
		this.initialize(this.projectId);
	}

	void initialize(int projectId) {
	}

	public void reset() {
		HibernateStore hs;
		try {

			if (this.itr6GeneratedStream != null)
				this.itr6GeneratedStream.close();
			if (this.itr6TemplateStream != null)
				this.itr6TemplateStream.close();

			hs = new HibernateStore(projectId, itrVersion);
			TemplateConf conf = new TemplateConf();
			conf.load(hs);
			this.tableConfig = conf.getTableOffset();
			this.showMessage("ITR Generation loaded successfully");

			refreshStreamedFiles();
		} catch (Exception e) {
			this.showMessage("ERROR:539230 Issue in initializing the ITR generation view" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void loadTds() throws Exception {
		HibernateReader hr = new HibernateReader(this.projectId);
		List<PartyRecord> parties = hr.read26ASParty();
		ArrayList<GenRowData> tds = new ArrayList<GenRowData>();

		for (int i = 0; i < parties.size(); i++) {
			PartyRecord tp = parties.get(i);

			GenRowData gd = new GenRowData();
			int rowNum = i + 1;

			gd.setRowNum(rowNum);
			gd.setTableName("Schedule_TDS1");

			gd.setV1(rowNum + "");
			gd.setV2("Self");
			gd.setV5(tp.getTan());
			gd.setV8(tp.getTtd() + "");
			gd.setV16(tp.getTac() + "");

			tds.add(gd);
			continue;
		}
		HibernateStore hs = new HibernateStore(this.projectId, this.itrVersion);

		hs.storeRawTable(tds, "Schedule_TDS1");
	}

	public void loadTaxComputation() throws Exception {
		HibernateReader hr = new HibernateReader(this.projectId);
		List<TaxPosition> positions = hr.readTaxPositions();

		ArrayList<GenRowData> taxComputRows = new ArrayList<GenRowData>();
		ArrayList<GenRowData> nontaxComputRows = new ArrayList<GenRowData>();
		ArrayList<TaxElement> divientRows = new ArrayList<TaxElement>();

		for (int i = 0; i < positions.size(); i++) {
			TaxPosition tp = positions.get(i);

			if (tp.getCategory().equals("NON-TAXABLE")) {
				GenRowData gd = new GenRowData();
				int rowNum = nontaxComputRows.size() + 1;

				gd.setRowNum(rowNum);
				gd.setTableName("Exempt_Income_DTAA");

				gd.setV1(rowNum + "");
				gd.setV2(tp.getAmount() + "");
				gd.setV3(tp.getName());

				nontaxComputRows.add(gd);
				continue;
			}

			if (tp.getName().startsWith("Dividend")) {
				TaxElement te = new TaxElement();

				te.setSheet("OS");

				if (tp.getCategory().equals("DTAA")) {
					te.setVar("DIVIDEND_INCOME_DTAA");
					te.setDesc("Dividend income as per DTAA");

				} else {
					te.setVar("DIVIDEND_INCOME");
					te.setDesc("Dividend income as per ITAA or BOTH");

				}

				te.setValueStr(tp.getAmount() + "");
				te.setValueType("Number");
				System.out.print("\ncheck992");
				te.show();

				divientRows.add(te);
				continue;
			}

			if (tp.getName().startsWith("Interest") || tp.getName().startsWith("Royalty")
					|| tp.getName().startsWith("Technical")) {
				GenRowData gd = new GenRowData();

				int rowNum = taxComputRows.size() + 1;
				String nat = RefDataUtiity.one().keyMapping("NatureMap", tp.getName());

				gd.setRowNum(rowNum);
				gd.setTableName("Income_Chargeable_At_SpecialR");

				gd.setV1(rowNum + "");
				gd.setV2(nat);
				gd.setV3(tp.getAmount() + "");
				taxComputRows.add(gd);
				continue;
			}
		}

		HibernateStore hs = new HibernateStore(this.projectId, this.itrVersion);

		hs.storeRawTable(nontaxComputRows, "Exempt_Income_DTAA");
		hs.storeRawTable(taxComputRows, "Income_Chargeable_At_SpecialR");

		hs.storeTaxElements(divientRows);
	}

	public void archieve() {
		HibernateStore hs;
		try {

			
			if (this.itr6GeneratedStream != null )  this.itr6GeneratedStream.close();
					
			if (this.itr6TemplateStream != null)   this.itr6TemplateStream.close();

			hs = new HibernateStore(projectId, itrVersion);
			hs.deleteTableConfig();

			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);
			String fileName = ds.archieveProject();

			this.tableConfig.clear();

			this.showMessage("Data reset : All data is archieved in ");

			refreshStreamedFiles();
			
			} 
		catch (Exception e) {
			this.showMessage("ERROR:539230 Issue in initializing the ITR generation view" + e.getMessage());
			e.printStackTrace();
		}
		
	}

	public void delete() {
		HibernateStore hs;
		try {
			hs = new HibernateStore(projectId, itrVersion);
			TemplateConf conf = new TemplateConf();
			conf.load(hs);
			conf.getTableOffset();

			this.showMessage("ITR Generation is deleted successfully");
			refreshStreamedFiles();
		} catch (Exception e) {
			this.showMessage("ERROR:539230 Issue in initializing the ITR generation view" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void validate() {

	}

	public void approve() {

	}

	public String proceed() {
		this.showMessage("This is the final stage for in the workflow. Nothing to be proceeded further");
		return "";
	}

	public void handleFileUploadITR(FileUploadEvent event) {

		try {
			System.out
					.print("\nITR file is loaded for project " + this.projectId + "\n with Version:" + this.itrVersion);

			String uniqName = Util.uniqFileName("UploadITR6", ".xml");
			File xmlFile = new File(uniqName);
			Util.copyInputStreamToFile(event.getFile().getInputStream(), xmlFile);

			String msg = "ITR6 file upload" + xmlFile + " is uploaded.";
			System.out.print("\n" + msg);

			createdProject();

			loadItr6Xml(xmlFile);

			loadTaxComputation();

			loadTds();

			reset();
		} catch (Exception e) {
			e.printStackTrace();
			this.showMessage("Error in uploading the XML file : Kindly verify the file and upload again");
		}
	}

	public void createdProject() {
		try {
			DirectoryStore.createProject(projectId, itrVersion);
			} 
		catch (Exception ex) {
			System.out.print("\n================================== Project setting exists =======================");
			ex.printStackTrace();
			this.showMessage("ITR6 project settings for FS exists");
		}
		
		try {
			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);
			HibernateStore hs = new HibernateStore(projectId, itrVersion);

			ITR6 RET = new ITR6();

			RET.buildDefaultConfig(ds, hs);
			this.showMessage("ITR6 project settings created for first time ");			
			} 
		catch (Exception ex) {
			ex.printStackTrace();
			this.showMessage("ITR6 project settings already exists in DS");
			}
		}

	public void loadItr6Xml(File xmlfile) throws Exception {
		Util.load();

		DirectoryStore ds = new DirectoryStore(projectId, itrVersion);
		HibernateStore hs = new HibernateStore(projectId, itrVersion);
		ds.replaceDocumentFile(xmlfile, "ITR6XML");

		ITR6 itr = new ITR6();
		itr.readITR6xml(ds, hs);

		TemplateConf conf = new TemplateConf();
		conf.load(hs);

		this.tableConfig = conf.getTableOffset();

		refreshStreamedFiles();

		System.out.print("\nCompleted reading ITR6 file ::  " + xmlfile.getAbsolutePath());
	}

	public void handleFileUploadTemplate(FileUploadEvent event) {

		try {
			System.out
					.print("\nITR file is loaded for project " + this.projectId + "\n with Version:" + this.itrVersion);
			String uniqName = Util.uniqFileName("UploadITR6Template", ".xlsm");
			File templateFile = new File(uniqName);
			Util.copyInputStreamToFile(event.getFile().getInputStream(), templateFile);

			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);

			Util.copyFile(templateFile, ds.getITR6File());

			String msg = "ITR6 File template is uploaded successfully : ";

			this.showMessage(msg);

			System.out.print("\nITR6 File template is uploaded successfully : " + ds.getITR6File().getAbsolutePath());
			refreshStreamedFiles();

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("ITR6 Template File upload error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void generateITR6() {

		try {
			System.out
					.print("\nITR file is loaded for project " + this.projectId + "\n with Version:" + this.itrVersion);

			HibernateStore hs = new HibernateStore(projectId, itrVersion);
			DirectoryStore ds = new DirectoryStore(projectId, itrVersion);

			ITR6 RET = new ITR6();
			File genfile = RET.processData(hs);
			ds.replaceDocumentFile(genfile, "GENITR6XLS");
			refreshStreamedFiles();

			System.out.print("\nITR6 File generation is successful " + genfile.getAbsolutePath());
			this.showMessage("ITR6 File generation is successful");

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("ITR6 Generation error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public LoginView getFather() {
		return father;
	}

	public void setFather(LoginView father) {
		this.father = father;
	}

	public List<TableConfiguration> getTableConfig() {
		return tableConfig;
	}

	public void setTableConfig(List<TableConfiguration> tableConfig) {
		this.tableConfig = tableConfig;
	}

	public List<TaxDocument> getGenITRFiles() {
		return genITRFiles;
	}

	public void setGenITRFiles(List<TaxDocument> genITRFiles) {
		this.genITRFiles = genITRFiles;
	}

	public TaxDocument getSelectedTaxDocument() {
		return selectedTaxDocument;
	}

	public void setSelectedTaxDocument(TaxDocument selectedTaxDocument) {
		this.selectedTaxDocument = selectedTaxDocument;
	}

	public void setItr6TemplateStreamedFile(StreamedContent itr6TemplateStreamedFile) {
		this.itr6TemplateStreamedFile = itr6TemplateStreamedFile;
	}

	public boolean isAdminView() {
		return adminView;
	}

	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	private void showMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg , null));
	}

	public double getItrVersion() {
		return itrVersion;
	}

	public void setItrVersion(double itrVersion) {
		this.itrVersion = itrVersion;
	}

	public FileInputStream getItr6TemplateStream() {
		return itr6TemplateStream;
	}

	public void setItr6TemplateStream(FileInputStream itr6TemplateStream) {
		this.itr6TemplateStream = itr6TemplateStream;
	}

	public void setItr6GeneratedStreamedFile(StreamedContent itr6GeneratedStreamedFile) {
		this.itr6GeneratedStreamedFile = itr6GeneratedStreamedFile;
	}

	public FileInputStream getItr6GeneratedStream() {
		return itr6GeneratedStream;
	}

	public void setItr6GeneratedStream(FileInputStream itr6GeneratedStream) {
		this.itr6GeneratedStream = itr6GeneratedStream;
	}

}
