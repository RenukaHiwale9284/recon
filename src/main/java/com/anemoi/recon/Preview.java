package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.HibernateReader;
import com.anemoi.data.HibernateWriter;
import com.anemoi.data.PartyRecord;
import com.anemoi.data.RPTRecord;
import com.anemoi.data.Refund;
import com.anemoi.data.SalesRecord;
import com.anemoi.tax.data.DataService;
import com.anemoi.tqm.TaxPosition;
import com.anemoi.tqm.TaxView;

@Component("preview")
@ManagedBean
@SessionScope
public class Preview {

	Recon recon ;
	
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
    
	List<Refund>  refunddata ; 
	
	List<IncomeAmount> income  ;
	
	
	List<IncomeAmount> nonsplits ;
	List<IncomeAmount> splits ;	
	List<IncomeAmount> carryforward ;
	
	int projectId ;
	
	private String uploadType ;
	
	 @Autowired
	 private TaxView taxView ;

	public void loadData()
		{		
    	this.projectId = 65 ;
		this.reset();
		}

	public void initialize(int projectId, Recon recon)
		{
		HibernateReader reader = new HibernateReader(projectId) ;
		this.recon = recon ;		
		refunddata = reader.readRefund();

		
		income = new ArrayList<IncomeAmount>();
		income.addAll(recon.getOriginal());	
		
		nonsplits = new ArrayList<IncomeAmount>();
		splits = new ArrayList<IncomeAmount>();		
		
		carryforward = recon.getCarryForward();
		
		for(int i = 0 ; i < this.income.size() ; i++ )
			{
			IncomeAmount amt = this.income.get(i);
			amt.calculateTax();
			
			System.out.print("\nIncome tag:"+ amt.getIncomeType());
			if(amt.getIncomeType().equals("SingleIncomeType") )
				{
				nonsplits.add(amt);
				}
			
			if(amt.getIncomeType().startsWith("IncomeType") )
				{
				splits.add(amt);
				}
			}
		System.out.print("\nIncome" + income.size() +",Splits=" + splits.size() + ",Non=" + nonsplits.size() + ",carry=" + carryforward.size());		
		}	

    
	public void reset()
		{
		HibernateReader reader = new HibernateReader(projectId) ;
				
		refunddata = reader.readRefund();
		
		income = reader.readIncomeAmount();
		
		nonsplits = new ArrayList<IncomeAmount>();
		
		splits = new ArrayList<IncomeAmount>();
		
		carryforward = new ArrayList<IncomeAmount>();
		
		for(int i = 0 ; i < this.income.size() ; i++ )
			{
			IncomeAmount amt = this.income.get(i);
			amt.calculateTax();
			System.out.print("\nIncome tag:"+ amt.getIncomeType());

			if( amt.getNature().endsWith("CFW") )
				{
				carryforward.add(amt);
				continue ;
				}			
			
			if(amt.getIncomeType().equals("SingleIncomeType") )
				{
				nonsplits.add(amt);
				continue;
				}
			
			if(amt.getIncomeType().startsWith("IncomeType") )
				{
				splits.add(amt);
				continue;
				}
			
			}
		System.out.print("\nIncome" + income.size() +",Splits=" + splits.size() + ",Non=" + nonsplits.size() + ",carry=" + carryforward.size());
		
		}	

	
	public void validate() {
		
		}
	
	public void approve() {
		// Saving all the contents ;
		System.out.print("\nStoing the View :::::::::::: ");
		HibernateWriter hw = new HibernateWriter(this.projectId);
		
		hw.storeIncomeAmount(this.recon.getOriginal(), this.recon.getCarryForward());
		
		String summary = "Recon saved : Recon-trans" + this.recon.getOriginal().size()
					+ ",Carry forward :" + this.recon.getCarryForward().size() ;
		this.addMessage(summary);
		
		
		}
	
	
	public String proceed() 
		{	
		if(this.recon == null)
			{
			this.recon = new Recon();
			this.recon.getOriginal().addAll(this.income);
			this.recon.getCarryForward().addAll(this.carryforward);
			}
		
		approve();

		
		HashMap<String, TaxPosition> taxPositions = this.recon.retriveTaxation();		
		ArrayList<TaxPosition> allPositions = new ArrayList<TaxPosition>(); 
		
		allPositions.addAll(taxPositions.values());
		
		taxView.setPositions(allPositions);
		
		this.father.getMyProject().setStage(4);
		this.father.saveStatus();

		return "tboard3.xhtml";
		}
	
    public void handleFileUploadITR6(FileUploadEvent event) 
    	{
    	String msg = "ITR6 File Upload" + event.getFile().getFileName() + " is uploaded." ;
    	System.out.print("\n" + msg);
    	
    	
        FacesMessage message = new FacesMessage();
        FacesContext.getCurrentInstance().addMessage(null, message);
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
        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
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


	public List<IncomeAmount> getIncome() {
		return income;
	}


	public void setIncome(List<IncomeAmount> income) {
		this.income = income;
	}


	public List<IncomeAmount> getNonsplits() {
		return nonsplits;
	}


	public void setNonsplits(List<IncomeAmount> nonsplits) {
		this.nonsplits = nonsplits;
	}


	public List<IncomeAmount> getSplits() {
		return splits;
	}


	public void setSplits(List<IncomeAmount> splits) {
		this.splits = splits;
	}


	public List<IncomeAmount> getCarryforward() {
		return carryforward;
	}


	public void setCarryforward(List<IncomeAmount> carryforward) {
		this.carryforward = carryforward;
	}
	
	
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        List<FacesMessage> msgList = FacesContext.getCurrentInstance().getMessageList();
        for(FacesMessage m :msgList )
        	{
        	System.out.print("\n MSG : " + m.getDetail() + "," + m.isRendered() + "," + m.getSummary());
        	}
    
    }

    public void addWaring(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        List<FacesMessage> msgList = FacesContext.getCurrentInstance().getMessageList();
        for(FacesMessage m :msgList )
        	{
        	System.out.print("\n MSG : " + m.getDetail() + "," + m.isRendered() + "," + m.getSummary());
        	}
    
    }

    
	}
