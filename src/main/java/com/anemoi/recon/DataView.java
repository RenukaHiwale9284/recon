package com.anemoi.recon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.Session;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.*;
import com.anemoi.tax.data.DataService;
import com.anemoi.util.Util;
import com.anemoi.document.DocumentUtility;


@Component("dview")
@ManagedBean
@SessionScope

public class DataView {

	private Recon recon;

	@Autowired
	private LoginView father;

	@Autowired
	private DataService dService;

	@Autowired
	private ReconService reconService;

	@Autowired
	private ReferenceDataUtiity ref;

	@Autowired
	private NameView nview;

	List<Refund> refunddata;

	List<RPTRecord> rptdata;

	List<SalesRecord> saledata;

	List<PartyRecord> partydata;

	List<Proj> projectdata;

	List<DataSummmary> summary = new ArrayList<DataSummmary>();

	DataSummmary selectedSummary;

	boolean adminView = false;

	int projectId;

	private String uploadType;

	public void loadData() {
		this.initialize(this.projectId);
	}

	void initialize(int projectId) {
		HibernateReader reader = new HibernateReader(projectId);

		refunddata = reader.readRefund();
		rptdata = reader.readRPT();
		saledata = reader.readSR();
		partydata = reader.read26ASParty();

		readLoadedSummary();
	}

	public void readLoadedSummary() {
		DataSummmary tempas26, tempsr, temprpt;

		tempas26 = new DataSummmary();
		tempas26.setSrNo(1);
		tempas26.setTag("26AS");
		tempas26.setTotalName(this.partydata.size());
		tempas26.setTotalTrans(this.partydata.size());
		tempas26.setTotalRefund(this.refunddata.size());

		tempsr = new DataSummmary();
		tempsr.setSrNo(2);
		tempsr.setTag("Sales-Register");
		tempsr.setTotalName(0);
		tempsr.setTotalTrans(this.saledata.size());

		temprpt = new DataSummmary();
		temprpt.setSrNo(3);
		temprpt.setTag("RPT");
		temprpt.setTotalName(0);
		temprpt.setTotalTrans(this.rptdata.size());

		this.summary.clear();
		this.summary.add(tempas26);
		this.summary.add(tempsr);
		this.summary.add(temprpt);
	}

	
	public void reset() {

		HibernateReader reader = new HibernateReader(projectId);
		refunddata = reader.readRefund();
		rptdata = reader.readRPT();
		saledata = reader.readSR();
		partydata = reader.read26ASParty();
		readLoadedSummary();

	}

	public void validate() {

	}

	public void approve() {

	}

	public void deldoc(String doc) {
		System.out.print("\nDeleted document :" + doc);
		HibernateWriter writer = new HibernateWriter(projectId);

		if (doc.equals("26AS")) {
			System.out.print("\n Delete the 26AS data :");
			writer.deleteData("PartyRecord");
			writer.deleteData("Refund");
			this.partydata.clear();
			this.refunddata.clear();
		}

		if (doc.equals("Sales-Register")) {
			System.out.print("\n Delete the 26AS data :");
			writer.deleteData("SalesRecord");
			this.saledata.clear();
		}

		if (doc.equals("RPT")) {
			writer.deleteData("RPTRecord");
			this.rptdata.clear();
		}
		readLoadedSummary();
	}

	public String proceed() {
		
		if (this.rptdata.size() == 0 && this.saledata.size() == 0 && this.partydata.size() == 0 ) {
			FacesMessage message = new FacesMessage("Validation",
					"At least Sales register & RPT records should be present");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "";
		}

		nview.setProjectId(this.projectId);
		nview.init();
		
		this.father.getMyProject().setStage(1);
		this.father.saveStatus();
		return this.father.getPage(1);
	}

	
	public void handleFileUploadITR6(FileUploadEvent event) {
		String msg = "ITR6 File Upload" + event.getFile().getFileName() + " is uploaded.";
		System.out.print("\n" + msg);

		FacesMessage message = new FacesMessage();
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void handleFileUpload26AS(FileUploadEvent event) {
		
		String msg = "26AS File Upload" + event.getFile().getFileName() + " is uploaded.";

		if (this.partydata.size() > 0) {
			FacesMessage message = new FacesMessage("File upload warning", "Data already exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		
		System.out.print("\n" + msg);
		try {
			String uniqName = Util.uniqFileName("AS26", ".txt");
			File f = new File(uniqName);

			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateWriter hwriter = new HibernateWriter(this.projectId);
			DocumentUtility.loadDocument(f, hwriter, "26ASTXT");
			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateReader reader = new HibernateReader(projectId);
			partydata = reader.read26ASParty();
			refunddata = reader.readRefund();

			readLoadedSummary();
			FacesMessage message = new FacesMessage("26AS File Upload",
					event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("File upload error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	
	public List<Proj> getProjectdata() {
		return projectdata;
	}

	
	public void setProjectdata(List<Proj> projectdata) {
		this.projectdata = projectdata;
		}

	
	public void handleFileUploadProjects(FileUploadEvent event) {
		String msg = "Project File Upload" + event.getFile().getFileName() + " is uploaded.";
		System.out.print("\n" + msg);

		try {
			String uniqName = Util.uniqFileName("PROJECT", ".xlsx");
			File f = new File(uniqName);

			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateWriter hwriter = new HibernateWriter(this.projectId);

			DocumentUtility.loadDocument(f, hwriter, "PROJXLS");
			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
						
			HibernateReader reader = new HibernateReader(projectId);
			this.projectdata = reader.projectData();
			
			readLoadedSummary();
			
			FacesMessage message = new FacesMessage("Project File Upload",
					event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage("Project file upload error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void handleFileUploadRPT(FileUploadEvent event) {
		String msg = "RPT File Upload" + event.getFile().getFileName() + " is uploaded.";
		System.out.print("\n" + msg);

		if (this.rptdata.size() > 0) {
			FacesMessage message = new FacesMessage("File upload warning", "Data already exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		try {
			String uniqName = Util.uniqFileName("RPTXLS", ".xlsx");
			File f = new File(uniqName);

			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateWriter hwriter = new HibernateWriter(this.projectId);
			DocumentUtility.loadDocument(f, hwriter, "RPTXLS");

			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateReader reader = new HibernateReader(projectId);

			rptdata = reader.readRPT();

			readLoadedSummary();

			FacesMessage message = new FacesMessage("RPT File Upload", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();

			FacesMessage message = new FacesMessage("File upload error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void handleFileUploadSR(FileUploadEvent event) {
		String msg = "Sales-Register File Upload" + event.getFile().getFileName() + " is uploaded.";
		System.out.print("\n" + msg);

		if (this.saledata.size() > 0) {
			FacesMessage message = new FacesMessage("File upload warning", "Data already exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		try {
			String uniqName = Util.uniqFileName("SRXLS", ".xlsx");
			File f = new File(uniqName);

			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);

			HibernateWriter hwriter = new HibernateWriter(this.projectId);
			DocumentUtility.loadDocument(f, hwriter, "SRXLS");
			Util.copyInputStreamToFile(event.getFile().getInputStream(), f);
			HibernateReader reader = new HibernateReader(projectId);
			this.saledata = reader.readSR();

			readLoadedSummary();

			FacesMessage message = new FacesMessage("Sales Rgister File Upload",
					event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();

			FacesMessage message = new FacesMessage("File upload error:", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	// Change of the Sales Register
	public void onRowEditRefund(RowEditEvent<Refund> event) {
		FacesMessage msg = new FacesMessage("Invoice Edited", String.valueOf(event.getObject().getAmountOfRefund()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelRefund(RowEditEvent<Refund> event) {
		FacesMessage msg = new FacesMessage("Invoice Cancelled", String.valueOf(event.getObject().getAmountOfRefund()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEditRPT(RowEditEvent<RPTRecord> event) {
		FacesMessage msg = new FacesMessage("Invoice Edited", String.valueOf(event.getObject().getPartyName()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelRPT(RowEditEvent<RPTRecord> event) {
		FacesMessage msg = new FacesMessage("Invoice Cancelled", String.valueOf(event.getObject().getPartyName()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowEditSR(RowEditEvent<SalesRecord> event) {
		FacesMessage msg = new FacesMessage("Invoice Edited", String.valueOf(event.getObject().getInvoiceNumber()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelSR(RowEditEvent<SalesRecord> event) {
		FacesMessage msg = new FacesMessage("Invoice Cancelled", String.valueOf(event.getObject().getInvoiceNumber()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	// Change of the Party Record
	public void onRowEditPartyData(RowEditEvent<PartyRecord> event) {
		FacesMessage msg = new FacesMessage("Party Edited", String.valueOf(event.getObject().getTan()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancelPartyData(RowEditEvent<PartyRecord> event) {
		FacesMessage msg = new FacesMessage("Party Cancelled", String.valueOf(event.getObject().getTan()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	// View functions
	public void onTabChange(TabChangeEvent event) {
		FacesMessage msg = new FacesMessage("Tab Changed", "Active Tab: " + event.getTab().getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onTabClose(TabCloseEvent event) {
		FacesMessage msg = new FacesMessage("Tab Closed", "Closed tab: " + event.getTab().getTitle());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void clearMultiViewState() {
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();
		PrimeFaces.current().multiViewState().clearAll(viewId, true, this::showMessage);
	}

	
	private void showMessage(String clientId) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
	}

	// Getters & Setters

	public Recon getRecon() {
		return recon;
	}

	public void setRecon(Recon recon) {
		this.recon = recon;
	}

	public DataService getdService() {
		return dService;
	}

	public void setdService(DataService dService) {
		this.dService = dService;
	}

	public ReconService getReconService() {
		return reconService;
	}

	public void setReconService(ReconService reconService) {
		this.reconService = reconService;
	}

	public ReferenceDataUtiity getRef() {
		return ref;
	}

	public void setRef(ReferenceDataUtiity ref) {
		this.ref = ref;
	}

	public List<Refund> getRefunddata() {
		return refunddata;
	}

	public void setRefunddata(List<Refund> refunddata) {
		this.refunddata = refunddata;
	}

	public List<RPTRecord> getRptdata() {
		return rptdata;
	}

	public void setRptdata(List<RPTRecord> rptdata) {
		this.rptdata = rptdata;
	}

	public List<SalesRecord> getSaledata() {
		
		return saledata;
	}

	public void setSaledata(List<SalesRecord> saledata) {
		this.saledata = saledata;
	}

	public List<PartyRecord> getPartydata() {
		return partydata;
	}

	public void setPartydata(List<PartyRecord> partydata) {
		this.partydata = partydata;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public NameView getNview() {
		return nview;
	}

	public void setNview(NameView nview) {
		this.nview = nview;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public boolean isAdminView() {
		return adminView;
	}

	public void setAdminView(boolean adminView) {
		this.adminView = adminView;
	}

	public List<DataSummmary> getSummary() {
		return summary;
	}

	public void setSummary(List<DataSummmary> summary) {
		this.summary = summary;
	}

	public DataSummmary getSelectedSummary() {
		return selectedSummary;
	}

	public void setSelectedSummary(DataSummmary selectedSummary) {
		this.selectedSummary = selectedSummary;
	}

}
