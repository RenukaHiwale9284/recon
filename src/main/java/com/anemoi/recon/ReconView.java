package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.HibernateReader;
import com.anemoi.data.HibernateWriter;
import com.anemoi.tqm.TaxPosition;
import com.anemoi.tqm.TaxView;

@Component("rview")
@ManagedBean
@SessionScope
public class ReconView {
	
	int projectId ;

	private Recon recon ; 

	@Autowired
	private LoginView father;
		
    @Autowired
    private ReconService reconService;

    @Autowired
    private ReferenceDataUtiity ref;

    @Autowired
    private TaxView taxView ;
    
    @Autowired
    private Preview preview ;
    
    private IncomeAmount selectedTrans ;
    
        
    int selectedPartyId ;    
    List<String> natureList ;    
    String selectedReasons ;    
    String carryfwreasondetail = " ";    
    int natureMatTotal ;    
    int natureIdCounter = 0 ;    
    double carryfwamount = 0.0 ;
    
    String selectedCfSource ;
    
    double amountToSplit = 0.0  ;
    double aggrSplitAmount = 0.0 ; 
    double splitRemainingAmount = 0.0 ; 
    
    public String getSelectedCfSource() {
		return selectedCfSource;
	}

    
    
    public TaxView getTaxView() {
		return taxView;
	}



	public void setTaxView(TaxView taxView) {
		this.taxView = taxView;
	}



	public Preview getPreview() {
		return preview;
	}



	public void setPreview(Preview preview) {
		this.preview = preview;
	}



	public IncomeAmount getSelectedTrans() {
		return selectedTrans;
	}



	public void setSelectedTrans(IncomeAmount selectedTrans) {
		System.out.print("\nTranscatio is set " );
		selectedTrans.show("Selected");
		this.selectedTrans = selectedTrans;
	}



	public boolean isRemainingZero()
    	{
    	double precision = 0.01;
    	if (ReconMath.numEqual(splitRemainingAmount,0,precision)) 
    		return true; 
    	
    	return false ;
    	}
    
	public void setSelectedCfSource(String selectedCfSource) {
		this.selectedCfSource = selectedCfSource;
	}
   
    private List<NatureRatio> natureMat = new ArrayList <NatureRatio>();
    
    public void init(int pid) 
    	{
    	this.projectId = pid ;
    	natureMatTotal = 0 ; 
    	natureList = ref.getDefNatures();
    	System.out.print("\n Initializing the Reconciliation View ");    	   	
		}
    
    @PostConstruct
    public void init()  {
    	
    	this.projectId = 65 ;
    	natureMatTotal = 0 ; 
    	natureList = ref.getDefNatures();
    	recon = new Recon() ;
    	System.out.print("\n Initializing the Reconciliation ");
    	   	
    	IncomeDocument as26 = null , salesRegister = null , rtp = null ;    	
		as26 = new Dummy26ASSet1();	
		salesRegister = new DummySalesRegisterSet1();	
		rtp = new DummyRPT1();	
		
		// ------------------------------------------------------------
		// Attach the primary source as 26AS.
		recon.attachPrimarySource(as26) ;	
		recon.attachTdsSource(as26);
		recon.show();
		recon.umap.show();

		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: SR  -------------------------- " );		
		// Verify the party names from the Sales register
		ArrayList<String> unknowSR = recon.unIdentifiedPartyNames(salesRegister) ; 
		System.out.print("\nUnknown Party Names from SalesRegister:: " + unknowSR);
				
		HashMap<String, String> namePairs = new HashMap<String, String>() ;
		namePairs.put("TATA Tele Ltd", "TATA Tele");		
		recon.umap.addNamePairs(namePairs) ;
	
		ArrayList<String> pendingNames = new ArrayList<String>() ;
		pendingNames.add("Anemoi Robotics");
		recon.umap.newNames(pendingNames) ;
			
		// Attach the source as SalesRegister		
		recon.attachSource(salesRegister, 2);
		recon.show();
		recon.umap.show();
		// ------------------------------------------------------------
		System.out.print("\n\nNew Income Document :: RTP  -------------------------- " );
		// Verify the party names from the Sales register
		ArrayList<String> unknowRTP = recon.unIdentifiedPartyNames(rtp) ;
		System.out.print("\nUnknown Party Names from RTP:: " + unknowRTP);

		HashMap<String, String> namePairs2 = new HashMap<String, String>() ;
		namePairs2.put("TATA tele Ltd", "TATA Tele");		
		namePairs2.put("TITAN", "TITAN Industries");	
		namePairs2.put("Anemoi Technologies", "Anemoi Robotics");	
		
		recon.umap.addNamePairs(namePairs2) ;
	
		ArrayList<String>    pendingNames2 = new ArrayList<String>() ;
		recon.umap.newNames( pendingNames2) ;
		recon.attachSource(  rtp, 3);
		
		recon.show();
		recon.umap.show();
		}

	//	Lifecycle Methods
	public void reset() 
		{
		
		natureMatTotal = 0 ; 
		natureList = ref.getDefNatures();
		System.out.print("\n Initializing the Reconciliation View ");  	

		HibernateReader hr = new HibernateReader(this.projectId);
		
		List<IncomeAmount> incomes = hr.readIncomeAmount() ;
		
		this.recon.getOriginal().clear();
		this.recon.getCarryForward().clear();
		for(IncomeAmount amt : incomes )
			{
			if(amt.nature.endsWith("CFW"))
				{
				this.recon.getCarryForward().add(amt);
				}
			else
				{
				this.recon.getOriginal().add(amt);
				}			
			}
		
		this.addMessage("Recon Reset : Total " + incomes.size() + " incomes transactions are loaded ");				
		}
	
	public void validate() 
		{
		String msg = recon.reconclied() ;
		if(msg.length() > 0) 
			{
			this.addWaring("Reconciliation problem :" + msg);
			}
		else
			{
			this.addMessage("Reconciliation successful : Kindly proceed for next stage");
			}
		}
	
	public void approve() 
		{	
		// Saving all the contents ;
		System.out.print("\nStoing the View :::::::::::: ");
		HibernateWriter hw = new HibernateWriter(this.projectId);
		
		hw.storeIncomeAmount(this.recon.getOriginal(), this.recon.getCarryForward());
		
		String summary = "Recon saved : Recon-trans" + this.recon.getOriginal().size()
					+ ",Carry forward :" + this.recon.getCarryForward().size() ;
		this.addMessage(summary);
		}
	

public String proceed() {	
	
	try {
		String msg = recon.reconclied() ;
		if(msg.length() > 0) 
			{
			this.addWaring("Reconciliation problem :" + msg);
			return "" ;
			}	
		
		approve();
		
		this.preview.initialize(this.projectId, this.recon);	
		this.father.getMyProject().setStage(3);
		this.father.saveStatus();

		return this.father.getPage(3);
		}
	catch(Exception ex){
		ex.printStackTrace();
		this.addWaring("Preview can't can be initialized :: Kindly check the reconciliation");
		}
	
	return "";
	}
    
    
    
    public String getSummary(String partyName)
    	{
    	return "totalTDS";
    	}

    
	public List<IncomeAmount> getTrans() {
		return this.recon.getOriginal();
	}

	public void setTrans(List<IncomeAmount> trans) {
		ArrayList<IncomeAmount> amttrans = new ArrayList<IncomeAmount>();
		amttrans.addAll(trans);
		this.recon.setOriginal(amttrans );
	}

	public List<IncomeAmount> getCfTrans() {
		return this.recon.getCarryForward();
	}

	public void setCfTrans(List<IncomeAmount> trans) {
		ArrayList<IncomeAmount> amttrans = new ArrayList<IncomeAmount>();
		amttrans.addAll(trans);
		this.recon.setCarryForward(amttrans );
	}

	
	
	
	public Recon getRecon() {
		return recon;
	}

	public void setRecon(Recon recon) {
		this.recon = recon;
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
	
    public String save() {
        addMessage("Data saved");
        return null;
    }

    public void update() {
        addMessage("Data updated");
    }

    public void delete() {
        addMessage("Data deleted");
    }

    public String getPartyName(int pid)
    	{
    	return this.recon.getUmap().primaryNames.get(pid);
    	}	
	
    public double getTDSAggr(int pid)
		{    	
		return this.recon.sum(pid, 10);
		}	
    
    
    public double get26ASAggr(int pid)
		{    	
		return this.recon.sum(pid, 1);
		}	

    public double getSRAggr(int pid)
		{    	
		return this.recon.sum(pid, 2);
		}	

    public double getRTPAggr(int pid)
		{    	
		return this.recon.sum(pid, 3);
		}	
    
    
    public void splitAmount()
    	{
        addMessage("Split Amount");
    	}
    
    public void resetNaturMat()
    	{
        addMessage("Reset Nature Matrix");
    	
    	}
    
    public void onNatureEdit()
    	{
    	System.out.print("\n ----------------- Check 2 ");
    	this.aggrSplitAmount = 0 ;
    	for(NatureRatio nn : this.natureMat)
    		aggrSplitAmount +=  nn.getRatio() ;
    	
    	this.splitRemainingAmount = this.amountToSplit - this.aggrSplitAmount;
    	    	
        addMessage("on Nature Edit");
    	}

    
    
    
	public ReconService getReconService() {
		return reconService;
	}

	public void setReconService(ReconService reconService) {
		this.reconService = reconService;
	}

	public int getSelectedPartyId() {
		return selectedPartyId;
	}

	public void setSelectedPartyId(int selectedPartyId) {
		System.out.print("\nSelected Party :: " + selectedPartyId);
		this.selectedPartyId = selectedPartyId;
		
	}

	public List<String> getNatureList() {
		return natureList;
	}

	public void setNatureList(List<String> natureList) {
		this.natureList = natureList;
	}

	public int getNatureMatTotal() {
		return natureMatTotal;
	}

	public void setNatureMatTotal(int natureMatTotal) {
		this.natureMatTotal = natureMatTotal;
	}

	public List<NatureRatio> getNatureMat() {
		return natureMat;
	}

	public void setNatureMat(List<NatureRatio> natureMat) {
		this.natureMat = natureMat;
	}
	
    
	public void onNatureCountChanged(ValueChangeEvent event)
		{
		if(event == null)
			{
			System.out.print("\n Spinnner change event is NULL " );
			return ;
			}
		System.out.print("\n Spinnner change event is captured " + event.getNewValue() );
		System.out.print("\nCurrent Party :: " + this.selectedPartyId);
		int totalNewNature = (Integer)event.getNewValue();
		
		if(totalNewNature < natureMatTotal)
			{
			this.addMessage("Kindly select split count more than existing split");			
			return ;
			}
		
		addNewRatio(totalNewNature);		
		System.out.print("\n Split Box size : " + natureMatTotal + "," + totalNewNature);
		}

	public void addNewRatio(int total)
		{
		System.out.print("\n Adding new Ratios :: " + total);
		if( this.natureMat == null ) this.natureMat = new ArrayList<NatureRatio>();
		
		if(total < natureMat.size()) 
			{
			System.out.print("\n No nature will be added :: ");
			return ;
			}
		
		int newRowsToAdd = total - natureMat.size() ;
		
		
		for(int idx = 0 ; idx < newRowsToAdd ; idx++)
			{
			NatureRatio nr = new NatureRatio();
			nr.setIdx(++this.natureIdCounter);
			nr.incomeType = "Income-Type" + (this.natureIdCounter ) ;
			nr.nature = this.natureList.get(0) ;
			nr.ratio = 0 ;
			this.natureMat.add(nr);
			}
		
		System.out.print("\nTotal Ratios :: " + this.natureMat.size());
		
		}
		
	
	public void actionTester(String action)
		{
		System.out.print("\n-------- Action is Tested -----------");
		this.addMessage("Test action is invoked :: " + action);		
		}
	
	public void deleteNature(NatureRatio nn)
		{
		System.out.print("\n-------- Deleting the nature  -----------" + nn.getIdx());
		this.natureMat.remove(nn);
		this.natureMatTotal = this.natureMat.size();

		this.onNatureEdit();
		
		this.addMessage("Split Amount :: Nature is removed from the list final size " + this.natureList.size());				
		}
	
	public void deleteAllRatio()
		{
		System.out.print("\n-------- Deleting all the ratios   -----------" );
		this.natureMat.clear();
		this.natureMatTotal = this.natureMat.size();
		
	    aggrSplitAmount = 0.0 ; 
	    splitRemainingAmount = amountToSplit ; 

		
		this.addMessage("Split Amount :: All the natures are cleared " );				
		}

	
	public void onIncomeTransEdit(CellEditEvent event) 
		{
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
        	
        	this.addMessage( "Cell Changed - Old: " + oldValue + ", New:" + newValue ) ;
        	
        	}
        }	
	
    public void onIncomeTransEditRow(RowEditEvent<IncomeAmount> event) {
    	this.addMessage("Edit Performed - " + String.valueOf(event.getObject().getPartyName()  )) ;
    }

    public void onIncomeTransCancelRow(RowEditEvent<IncomeAmount> event) {
    	this.addMessage("Edit Cancelled - " + String.valueOf(event.getObject().getPartyName()  )) ;
    	}
	
	
	public void splitGlobalAmount()
		{		
		int partyId = this.selectedPartyId ;
		recon.show();
		System.out.print("\nGlobal split : splitting for the first time " + partyId);
		ArrayList<IncomeAmount> amt =  recon.retrieve(partyId);
		
		for(IncomeAmount sample : amt )  sample.show("Split-" + partyId);
		
		if(amt.size() != 1) 
			{			
			this.addMessage("Can't perform Split Amount :: Party amounts are alreay split :: " + amt.size());
			return ;
			}
		
		IncomeAmount aa = amt.get(0);		
		String priNature = aa.getNature();
		int totalSplits = this.natureMat.size() ;
				
		String[] natures = new String[totalSplits];
		double[] ratio = new double[totalSplits];
		
		for(int i = 0 ; i < this.natureMat.size() ; i++)
			{
			natures[i] = this.natureMat.get(i).getNature();
			ratio[i] = this.natureMat.get(i).getRatio();			
			}
		
		
		double totalRatio = ReconMath.sum(ratio) ;
		System.out.print("\nTotal ratio ::" + totalRatio + "    ratios:" + ratio.length);

		
		/*		
		if(!ReconMath.numEqual(totalRatio, aa., 0.0001))
			{
			this.addMessage("Can't perform Split Amount :: Total ratio is not equal to 100%, to split ratios has to be 100% in totality ");
			return ;			
			}
		 */
		
		try {
			recon.splitAmount(partyId, priNature, natures, ratio);
		} catch (Exception e) {
			this.addMessage("Can't perform Split Amount :: Unknown error occured ");
			e.printStackTrace();
		}
		
		recon.show();
		this.addMessage("Split operation successful ::: ");				
		}
	
	

	public void splitGlobalAmountByRatio()
		{
		int partyId = this.selectedPartyId ;
		recon.show();
		System.out.print("\nGlobal split : splitting for the first time " + partyId);
		ArrayList<IncomeAmount> amt =  recon.retrieve(partyId);
		
		for(IncomeAmount sample : amt )  sample.show("Split-" + partyId);
		
		if(amt.size() != 1) 
			{			
			this.addMessage("Can't perform Split Amount :: Party amounts are alreay split :: " + amt.size());
			return ;
			}
		
		IncomeAmount aa = amt.get(0);
		String priNature = aa.getNature();
		int totalSplits = this.natureMat.size() ;
				
		String[] natures = new String[totalSplits];
		double[] ratio = new double[totalSplits];
		
		for(int i = 0 ; i < this.natureMat.size() ; i++)
			{
			natures[i] = this.natureMat.get(i).getNature();
			ratio[i] = this.natureMat.get(i).getRatio();
			}
		
		
		double totalRatio = ReconMath.sum(ratio) ;
		System.out.print("\nTotal ratio ::" + totalRatio + "    ratios:" + ratio.length);
		
		if(!ReconMath.numEqual(totalRatio, 1.0, 0.0001))
			{
			this.addMessage("Can't perform Split Amount :: Total ratio is not equal to 100%, to split ratios has to be 100% in totality ");
			return ;			
			}
		
		try {
			recon.splitAmount(partyId, priNature, natures, ratio);
		} catch (Exception e) {
			this.addMessage("Can't perform Split Amount :: Unknown error occured ");
			e.printStackTrace();
		}
		
		recon.show();
		this.addMessage("Split operation successful ::: ");				
		}
	
	public void validateRecon()
		{
		

		}
	
	public void resetSplitPanel()
		{
		System.out.print("\nReseting the split panel ");
		natureMat.clear();
		
		}
	
	
	
	public void carryFwSplitWise()
		{
		System.out.print("\nCarry Forward invoked ------------- " + this.selectedPartyId);
				
		double amount = this.carryfwamount ;
		String reason = this.selectedReasons + " " + this.carryfwreasondetail;		
		int reconPriority = ref.defSources.indexOf(this.selectedCfSource) + 1;

		
		String msg = recon.carryForwardSingleValue(this.selectedTrans, amount, reason, reconPriority) ;					
	
		
		this.addMessage("Carry-forward is finished " + msg );
		recon.show();
		}	
	
	
	
/*	
	public void carryFw()
		{
		System.out.print("\nCarry Forward invoked ------------- " + this.selectedPartyId);
		
		int partyId = this.selectedPartyId ; 
		double amount = this.carryfwamount ;
		String reason = this.selectedReasons + " " + this.carryfwreasondetail;
		int reconPriority = ref.defSources.indexOf(this.selectedCfSource) + 1;
		
				
		IncomeAmount amt = this.recon.retrieveCarryFw(partyId);
		if(amt != null)
			{
			this.addWaring("Can't perform carry forward : Please delete existing carry-forward entry ");
			return ;
			
			}		
		
		if(reconPriority == 1 && amount > this.get26ASAggr(partyId))
			{
			this.addMessage("Can't perform carry forward :: 26AS Amount[] " + this.get26ASAggr(partyId)
					+ "is lesser than total Carry forward amount ");
			return ;
			}
		
		if(reconPriority == 2 && amount > this.getSRAggr(partyId))
			{
			this.addMessage("Can't perform carry forward :: Sales register Amount is lesser than total Carry forward amount ");
			return ;
			}

		if(reconPriority == 3 && amount > this.getRTPAggr(partyId))
			{
			this.addMessage("Can't perform carry forward :: RPT Amount is lesser than total Carry forward amount ");
			return ;
			}
		
		if(reason.trim().length() == 0)
			{
			this.addMessage("Can't perform carry forward :: No reason is slected for the operation ");
			return ;			
			}
				
		
		String msg = recon.carryForward(partyId, amount, reason, reconPriority) ;					

		this.addMessage("Carry-forward is finished " + msg );
		recon.show();
		}	
*/	
	
// Carry backward	
	
	
	public void carryBw(IncomeAmount amt)
		{
		System.out.print("\nCarry Backward invoked ------------- ");	

		this.recon.removeCarryForwardSingle(amt);
		}
		
	
	
	public String concatnate(List<String> words)
		{
		String concatenatedString = ",";
		String delimiter = " ";
		
		for (String word : words) 
			{
		    concatenatedString += concatenatedString.equals("") ? word : delimiter + word;
			}
		
		System.out.println(concatenatedString);
		return concatenatedString ;		
		}
	
	
	public String getSelectedReasons() {
		return selectedReasons;
	}

	public void setSelectedReasons(String selectedReasons) {
		this.selectedReasons = selectedReasons;
	}

	public void resetCarryFw()
		{
		
		}

	public ReferenceDataUtiity getRef() {
		return ref;
	}

	public void setRef(ReferenceDataUtiity ref) {
		this.ref = ref;
	}


	public String getCarryfwreasondetail() {
		return carryfwreasondetail;
	}

	public void setCarryfwreasondetail(String carryfwreasondetail) {
		this.carryfwreasondetail = carryfwreasondetail;
	}

	public int getNatureIdCounter() {
		return natureIdCounter;
	}

	public void setNatureIdCounter(int natureIdCounter) {
		this.natureIdCounter = natureIdCounter;
	}

	public double getCarryfwamount() {
		return carryfwamount;
	}

	public void setCarryfwamount(double carryfwamount) {
		this.carryfwamount = carryfwamount;
	}

	public double getAmountToSplit() {
		return amountToSplit;
	}

	public void setAmountToSplit(double amountToSplit) {
		System.out.print("\nAmount to split:" + amountToSplit);
		this.amountToSplit = amountToSplit;
	}

	public double getAggrSplitAmount() {
		return aggrSplitAmount;
	}

	public void setAggrSplitAmount(double aggrSplitAmount) {
		this.aggrSplitAmount = aggrSplitAmount;
	}

	public double getSplitRemainingAmount() {
		return splitRemainingAmount;
	}

	public void setSplitRemainingAmount(double splitRemainingAmount) {
		this.splitRemainingAmount = splitRemainingAmount;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	
	
	
	}
