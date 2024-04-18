package com.anemoi.recon;

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

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.*;
import com.anemoi.tax.data.DataService;

@Component("nview")
@ManagedBean
@SessionScope
public class NameView {

	@Autowired
	private LoginView father;
	
	@Autowired
	private ReconService reconService;

	@Autowired
	private ReconView reconView;

	int projectId;
	CompCatName selectedName;
	List<CompCatName> selectedNames;
	CompName selectedNameSet;
	UniqueNameMap umap = new UniqueNameMap();

	ArrayList<String> as26Names = new ArrayList<String>();
	ArrayList<String> srNames = new ArrayList<String>();
	ArrayList<String> rptNames = new ArrayList<String>();

	ArrayList<CompName> nameset = new ArrayList<CompName>();
	ArrayList<CompCatName> nameMap = new ArrayList<CompCatName>();

	IncomeDocument as26 = null;
	IncomeDocument salesRegister = null;;
	IncomeDocument rtp = null;

	
	public void reset() {
		try {
			as26Names.clear();
			srNames.clear();
			rptNames.clear();
			nameset.clear();
			umap.clear();
			fetchNames(false);
			build();
		} catch (Exception ex) {
			ex.printStackTrace();

			this.addWaring("Name mapping error during reset :" + ex.getMessage());
		}
	}

	public void validate() {

	}

	public void approve() {
		HibernateWriter hwriter = new HibernateWriter(projectId);
		NameData data = this.umap.getNameData();
		hwriter.storeNames(data);
		
		this.addMessage("Data is approved and saved in database ");
	}

	
//	@PostConstruct
	public void init() {
		System.out.print("\nName bean initialization ::::::::::::::::::::::");		
		try {
			this.fetchNames(true);
			this.build();
		} catch (Exception ex) {
			ex.printStackTrace();

			addWaring(ex.getMessage());
		}
	}

	public void fetchNames(boolean force) throws Exception {
		HibernateReader reader = new HibernateReader(projectId);

		as26 = reader.read26ASAggr();
		salesRegister = reader.readSRAggr();
		rtp = reader.readRPTAggr();

		as26Names = as26.listAllParties();
		srNames = salesRegister.listAllParties();
		rptNames = rtp.listAllParties();

		NameData data = reader.readNames();
		
		umap = new UniqueNameMap();

		if (data == null || force)
			umap.setPrimaryPartyName(as26Names);
		else
			umap.loadNameData(data);
		}

	

	public void createNewName() {
		ArrayList<String> nn = new ArrayList<String>();

		for (CompCatName cc : selectedNames) {
			nn.add(cc.getName());
			}
		umap.newNames(nn);
		build();
	}

	
	
	public void build() {

		nameMap.clear();
		nameset.clear();
		int idx = 0;

		for (int i = 0; i < umap.primaryNames.size(); i++) {
			nameset.add(new CompName(i + 1));
		}

		System.out.print("\n26AS Name mapping");
		for (int i = 0; i < this.as26Names.size(); i++) {
			String nn = this.as26Names.get(i);
//			System.out.print("\n" + nn) ;

			int nid = umap.nameId(nn);

			if (nid == -1) {
//				System.out.print("\nNOT AVAILABLE ------ " ) ;				
				CompCatName cc = new CompCatName();
				cc.id = idx++;
				cc.name = nn;
				cc.source = "26AS";
				cc.newName = "--";
				nameMap.add(cc);
			} else
				nameset.get(nid - 1).name26as = nn;

		}

		System.out.print("\nSales Register Name mapping");
		for (int i = 0; i < this.srNames.size(); i++) {
//			umap.show();

			String nn = this.srNames.get(i);

//			System.out.print("\n" + nn) ;

			int nid = umap.nameId(nn);

			if (nid == -1) {
//				System.out.print("\nNOT AVAILABLE ------ " ) ;				
				CompCatName cc = new CompCatName();
				cc.id = idx++;
				cc.name = nn;
				cc.source = "SR";
				cc.newName = "--";
				nameMap.add(cc);
			} else {
				nameset.get(nid - 1).namesr = nn;
			}

		}

		System.out.print("\nRPT Name mapping");
		for (int i = 0; i < this.rptNames.size(); i++) {
			String nn = this.rptNames.get(i);
//			System.out.print("\n" + nn) ;

			int nid = umap.nameId(nn);

			if (nid == -1) {
//				System.out.print("\nNOT AVAILABLE ------ " ) ;				
				CompCatName cc = new CompCatName();
				cc.id = idx++;
				cc.name = nn;
				cc.newName = "--";
				cc.source = "RPT";
				nameMap.add(cc);
			} else
				nameset.get(nid - 1).namertp = nn;

		}

		System.out.print("\nName MAP -------------------------------------");
		for (int i = 0; i < nameMap.size(); i++) {
			System.out.print("\nNMap[" + nameMap.get(i).id + "]=" + nameMap.get(i).name + "-" + nameMap.get(i).source);
		}

	}

	public List<String> getRightNames() {
		ArrayList<String> right = new ArrayList<String>();

		right.addAll(this.umap.getPrimaryNames());

		for (CompName cname : this.nameset) {
			if (cname.isMapped()) {
				right.remove(cname.name26as);
				right.remove(cname.namertp);
				right.remove(cname.namesr);
			}
		}
		return right;
	}

	public static void main(String[] argv) throws Exception {
		NameView nv = new NameView();
		nv.setProjectId(65);
		nv.fetchNames(false);
	}

	public Recon buildRecon() {
		Recon recon = new Recon();
		System.out.print("\n Initializing the Reconciliation ");
		recon.setUmap(this.umap);

		recon.attachPrimarySource(as26);
		recon.attachTdsSource(as26);
		recon.attachSource(salesRegister, 2);
		recon.attachSource(rtp, 3);

		recon.show();
		recon.umap.show();
		return recon;
	}

	public String proceed() {
		try {
			Recon newrecon = buildRecon();
			reconView.setRecon(newrecon);
			reconView.init(this.projectId);

			this.father.getMyProject().setStage(2);
			this.father.saveStatus();
			return this.father.getPage(2);

		} catch (Exception ex) {
			ex.printStackTrace();
			this.addWaring("Recon initialization failure :: Kindly check the name mapping");
		}

		return "";
	}

	// Change of the Sales Register
	public void onRowEdit(RowEditEvent<CompCatName> event) {
		HashMap<String, String> pairs = new HashMap<String, String>();
		System.out.print(
				"\n New Name mapping :: " + event.getObject().getName() + "-->" + event.getObject().getNewName());
		pairs.put(event.getObject().getName(), event.getObject().getNewName());
//    	this.umap.addNamePairs(pairs);
//    	this.build();

		FacesMessage msg = new FacesMessage("New name pair :", String.valueOf(event.getObject().getName()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
		PrimeFaces.current().ajax().update("pendingNames");
	}

	public void onRowCancel(RowEditEvent<CompCatName> event) {
		System.out.print(
				"\n Cancel Name mapping - " + event.getObject().getName() + "-->" + event.getObject().getNewName());
		ArrayList<String> names = new ArrayList<String>();
		names.add(event.getObject().getName());
		this.umap.newNames(names);
//    	this.build();
//    	nameMap.clear();
		FacesMessage msg = new FacesMessage("New name - ", String.valueOf(event.getObject().getName()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
		PrimeFaces.current().ajax().update("pendingNames");

	}

	public void refresh() {
		HashMap<String, String> pairs = new HashMap<String, String>();
		for (int i = 0; i < this.nameMap.size(); i++) {
			String nn = nameMap.get(i).getNewName();
			if (!nn.equals("--")) {
				pairs.put(nameMap.get(i).getName(), nn);
//				this.nameMap.remove(i);				
			}
		}

		this.umap.addNamePairs(pairs);
		build();
		System.out.print("\nName Map size :: " + this.nameMap.size());
	}

	public ReconService getReconService() {
		return reconService;
	}

	public void setReconService(ReconService reconService) {
		this.reconService = reconService;
	}

	public UniqueNameMap getUmap() {
		return umap;
	}

	public void setUmap(UniqueNameMap umap) {
		this.umap = umap;
	}

	public ArrayList<String> getAs26Names() {
		return as26Names;
	}

	public void setAs26Names(ArrayList<String> as26Names) {
		this.as26Names = as26Names;
	}

	public ArrayList<String> getSrNames() {
		return srNames;
	}

	public void setSrNames(ArrayList<String> srNames) {
		this.srNames = srNames;
	}

	public ArrayList<String> getRptNames() {
		return rptNames;
	}

	public void setRptNames(ArrayList<String> rptNames) {
		this.rptNames = rptNames;
	}

	public ArrayList<CompName> getNameset() {
		return nameset;
	}

	public void setNameset(ArrayList<CompName> nameset) {
		this.nameset = nameset;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public ArrayList<CompCatName> getNameMap() {
		return nameMap;
	}

	public void setNameMap(ArrayList<CompCatName> nameMap) {
		this.nameMap = nameMap;
	}

	public CompCatName getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(CompCatName selectedName) {
		this.selectedName = selectedName;
	}

	public CompName getSelectedNameSet() {
		return selectedNameSet;
	}

	public void setSelectedNameSet(CompName selectedNameSet) {
		this.selectedNameSet = selectedNameSet;
	}

	public List<CompCatName> getSelectedNames() {
		return selectedNames;
	}

	public void setSelectedNames(List<CompCatName> selectedNames) {
		this.selectedNames = selectedNames;
	}

	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);

		List<FacesMessage> msgList = FacesContext.getCurrentInstance().getMessageList();
		for (FacesMessage m : msgList) {
			System.out.print("\n MSG : " + m.getDetail() + "," + m.isRendered() + "," + m.getSummary());
		}

	}

	public void addWaring(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);

		List<FacesMessage> msgList = FacesContext.getCurrentInstance().getMessageList();
		for (FacesMessage m : msgList) {
			System.out.print("\n MSG : " + m.getDetail() + "," + m.isRendered() + "," + m.getSummary());
		}

	}

	public void resest() {
		// TODO Auto-generated method stub

	}

}
