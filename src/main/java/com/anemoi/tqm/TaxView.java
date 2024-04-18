package com.anemoi.tqm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.DtaaTaxRate;
import com.anemoi.data.HibernateReader;
import com.anemoi.data.HibernateWriter;
import com.anemoi.data.ItaaTaxRate;
import com.anemoi.itr.storage.DirectoryStore;
import com.anemoi.recon.LoginView;
import com.anemoi.recon.ReferenceDataUtiity;
import com.anemoi.util.Util;

@Component("taxView")
@ManagedBean
@SessionScope
public class TaxView implements Serializable {

	@Autowired
	private transient LoginView father;

	private List<TaxPosition> positions = new ArrayList<TaxPosition>();

	private TaxPosition selectedPosition;

	private List<TaxPosition> selectedPositions = new ArrayList<TaxPosition>();

	@Autowired
	private transient CountryService countryService;

	@Autowired
	private transient ProductService productService;

	@Autowired
	private transient ReferenceDataUtiity ref;

	private int projectId;

//	StreamedContent exportContentStream;

	FileInputStream exportFileStream;

	@PostConstruct
	public void init() {
		this.positions = this.productService.getClonedProducts(5);

	}

	public void initByProceed() {

	}

	public boolean isCountryEnabled() {
		if (selectedPosition.getCategory().contentEquals("DTAA")
				|| selectedPosition.getCategory().contentEquals("BOTH"))
			return true;

		return false;
	}

	public void reset() {
		System.out.print("\n Initializing the TaxView ");

		HibernateReader hr = new HibernateReader(this.projectId);

		selectedPositions.clear();
		selectedPosition = null;
		this.positions = hr.readTaxPositions();

		this.addMessage("Tax View Reset : Total " + this.positions.size() + " incomes transactions are loaded ");
	}

	public StreamedContent getExportFile() 
		{
		System.out.print("\nstarted export ::" );
		StreamedContent exportContentStream = null ;
		try {

			File f2 = export();

			if (f2.exists()) {
				exportFileStream = new FileInputStream(f2);
				exportContentStream = DefaultStreamedContent.builder().name("taxposition.csv")
						.contentType("text/csv")
						.stream(() -> exportFileStream).build();

				System.out.print("\nDownloaded ITR6 Generated File ::" + exportContentStream.getName());
			} else
				this.addMessage("ERR749334 : No export File exists for tax positions");

		} catch (Exception ex) {
			ex.printStackTrace();
			this.addMessage("ERR642394 : Error in generatig tax positions file" + ex.getMessage());

		}

		return exportContentStream;
	}
	

	public File export() {
		System.out.print("\n Initializing the TaxView Export function");
		
		HibernateReader hr = new HibernateReader(this.projectId);
		List<TaxPosition> allpos =  hr.readTaxPositions();
		
		String exportFileName = Util.uniqFileName("taxpos", ".csv");
		File ff = new File(exportFileName);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ff)));

			for (int i = 0; i < allpos.size(); i++) {
				
				if (i == 0)  allpos.get(0).printRow(pw);

				allpos.get(i).print(pw);
			}

			pw.close();

		} catch (IOException e) {
			this.addMessage("ERR6942934 Tax View Export : Total " + this.positions.size() + " are exported ");
			e.printStackTrace();
		}

		return ff;
	}

	public void validate() {

	}

	public void storeGenTable() {

	}

	public void approve() {
		// Saving all the contents ;
		System.out.print("\nStoing the View :::::::::::: ");
		HibernateWriter hw = new HibernateWriter(this.projectId);

		hw.storeTaxComp(this.positions);

		String summary = "Tax Positions saved : " + this.positions.size();
		this.addMessage(summary);
	}

	public String proceed() {

		approve();

		this.father.getMyProject().setStage(5);
		this.father.saveStatus();
		return father.getPage(5);
	}

	public void cancelTaxEditing() {

	}

	// Listeners
	public void countrySelected(ValueChangeEvent event) {
		if (event == null || event.getNewValue() == null) {
			System.out.print("\nDummy Value change Event : Country changed ");
			return;
		}

		System.out.print("\nValue change Event : Country :");
		String con = event.getNewValue().toString();
		System.out.print("\nNew Value - Country :" + con);

		String summary = "Country selected :" + this.selectedPosition.getCountry();

		ItaaTaxRate indiaRate = ref.getItaaRates().get(this.selectedPosition.getName());
		indiaRate.show();

		List<DtaaTaxRate> abroadRates = ref.fetchDtaa(con);
		for (DtaaTaxRate frate : abroadRates) {
			System.out.print("\nNature Match ::-" + indiaRate.getNature() + "-,-" + frate.getNature() + "-");
			if (indiaRate.getNature().startsWith(frate.getNature())) {
				if (this.selectedPosition.getCategory().equalsIgnoreCase("BOTH")) {
					System.out.print("\nBOTH selected :: " + this.selectedPosition.getName());

					this.selectedPosition.setSection(indiaRate.getSection());

					double tr = Math.min(frate.getTaxRate1(), indiaRate.getTaxRate());

					this.selectedPosition.setRating(tr);
					this.selectedPosition.setOrgRating(tr);

					this.selectedPosition.setTaxAmount(tr * this.selectedPosition.getPrice() * 0.01);

					summary = "BOTH : Tax rate, Amount filled automatically";
				}

				if (this.selectedPosition.getCategory().equalsIgnoreCase("DTAA")) {
					System.out.print("\nDTAA selected :: " + this.selectedPosition.getName() + ","
							+ this.selectedPosition.getCountry());

					this.selectedPosition.setSection("NA");
					this.selectedPosition.setRating(frate.getTaxRate1());
					this.selectedPosition.setOrgRating(frate.getTaxRate1());
					this.selectedPosition.setTaxAmount(frate.getTaxRate1() * this.selectedPosition.getPrice() * 0.01);

					summary = "DTAA : Tax rate, Amount filled automatically";
				}
			}
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}

	public void categorySelected(ValueChangeEvent event) {
		if (event == null || event.getNewValue() == null) {
			System.out.print("\nDummy Value change Event : Act changed ");
			return;
		}

		System.out.print("\nValue change Even : Category : Old " + this.selectedPosition.getCategory()
				+ event.getNewValue().toString());

		String summary = "Category selected :" + event.getNewValue().toString();

		/*
		 * if(ref.getItaaRates().get( this.selectedPosition.getName()) == null) {
		 * System.out.print("\nData integrity issue : Act is invalid") ; return ; }
		 */
		String cat = event.getNewValue().toString();

		if (cat.equalsIgnoreCase("ITAA")) {
			// find section
			System.out.print("\nITAA selected :: " + this.selectedPosition.getName());
			ItaaTaxRate rate = ref.getItaaRates().get(this.selectedPosition.getName());
			rate.show();

			this.selectedPosition.setSection(rate.getSection());

			this.selectedPosition.setRating(rate.getTaxRate());
			this.selectedPosition.setOrgRating(rate.getTaxRate());

			this.selectedPosition.setTaxAmount(rate.getTaxRate() * this.selectedPosition.getPrice() * 0.01);

			this.selectedPosition.setCountry("India");
			this.selectedPosition.setReason("NA");

			summary = "ITAA : Section, Tax rate, Amount filled automatically";
		}

		if (cat.equalsIgnoreCase("DTAA")) {
			System.out.print("\nITAA selected :: " + this.selectedPosition.getName());
			ItaaTaxRate rate = ref.getItaaRates().get(this.selectedPosition.getName());
			rate.show();

			this.selectedPosition.setSection("NA");
			this.selectedPosition.setCountry("Please select");
			this.selectedPosition.setRating(0.0);
			this.selectedPosition.setOrgRating(0.0);

			this.selectedPosition.setTaxAmount(0.0);
			this.selectedPosition.setReason("NA");
			summary = "ITAA : Section, Tax rate, Amount filled automatically";
		}

		if (cat.equalsIgnoreCase("BOTH")) {
			System.out.print("\nITAA selected :: " + this.selectedPosition.getName());
			ItaaTaxRate rate = ref.getItaaRates().get(this.selectedPosition.getName());
			rate.show();

			this.selectedPosition.setSection(rate.getSection());
			this.selectedPosition.setCountry("Please Select");
			this.selectedPosition.setRating(0.0);
			this.selectedPosition.setOrgRating(0.0);

			this.selectedPosition.setTaxAmount(0.0);
			this.selectedPosition.setReason("NA");
			summary = "DTAA / BOTH : Select country for tax calculation";
		}

		if (cat.equalsIgnoreCase("NON-TAXABLE")) {
			this.selectedPosition.setSection("NA");
			this.selectedPosition.setRating(0.0);
			this.selectedPosition.setOrgRating(0.0);

			this.selectedPosition.setTaxAmount(0.0);
			this.selectedPosition.setCountry("NA");
			this.selectedPosition.setReason("NA");
			summary = "NON-Taxable : Section, Tax rate, Amount are not applicable";
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}

	public void reasonSelected(ValueChangeEvent event) {
		if (event == null || event.getNewValue() == null) {
			System.out.print("\nDummy Value change Event : Reason  Changed ");
			return;
		}

		String val = event.getNewValue().toString();

		if (val.contentEquals("Other")) {
			this.selectedPosition.setDescription("");
		} else {
			this.selectedPosition.setDescription("Not Required");
		}

	}

	public void incomeSelected(ValueChangeEvent event) {
		if (event == null || event.getNewValue() == null) {
			System.out.print("\nDummy Value change Event : Income Type  Changed ");
			return;
		}

		String val = event.getNewValue().toString();
		System.out.print("\nValue change : Income type : " + val);
		String summary = this.selectedPosition.nonIncome ? "Checked" : "Unchecked";

		if (val.equalsIgnoreCase("true")) {
			this.selectedPosition.setCategory("NON-TAXABLE");
			this.selectedPosition.setSection("NA");

			this.selectedPosition.setRating(0.0);
			this.selectedPosition.setOrgRating(0.0);

			this.selectedPosition.setTaxAmount(0.0);
			this.selectedPosition.setCountry("NA");

		}

		if (val.equalsIgnoreCase("false")) {
			this.selectedPosition.setCategory("NON-TAXABLE");
			this.selectedPosition.setSection("NA");
			this.selectedPosition.setRating(0.0);
			this.selectedPosition.setOrgRating(0.0);
			this.selectedPosition.setTaxAmount(0.0);
			this.selectedPosition.setCountry("NA");
		}

// 		System.out.print("\nFlag :::" + this.selectedProduct.nonIncome );
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}

	public void rateChanged(ValueChangeEvent event) {
		System.out.print("\nRage Changed(event)");

		if (event != null && event.getNewValue() != null) {
			System.out.print("\nDummy Value change Event : Rate Changed " + event.getNewValue());

			return;
		}

		double rorg = this.selectedPosition.getRating();
		double rnew = this.selectedPosition.getOrgRating();

		if (rorg == rnew) {
			this.selectedPosition.setReason("");
		}

		this.selectedPosition.setTaxAmount(rnew * this.selectedPosition.getPrice() * 0.01);

	}

	public void rateChanged() {
		System.out.print("\nRage Changed()");
		double rnew = this.selectedPosition.getRating();
		double rorg = this.selectedPosition.getOrgRating();

		System.out.print("\nRates :: " + rorg + "," + rnew);
		if (rorg == rnew) {
			this.selectedPosition.setReason("");
		}

		if (rorg != rnew && this.selectedPosition.getReason().equals("NA")) {
			this.selectedPosition.setReason("MFN-Clause");
		}

		this.selectedPosition.setTaxAmount(rnew * this.selectedPosition.getPrice() * 0.01);
	}

	public List<String> completeCountryViaService(String query) {

		String queryLowerCase = query.toLowerCase();
		List<String> countryList = new ArrayList<>();
		List<Country> countries = countryService.getCountries();

		for (Country country : countries) {
			countryList.add(country.getName());
		}

		return countryList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase))
				.collect(Collectors.toList());
	}

	public List<String> completeCountry(String query) {
		List<String> countryList = ref.fetchCountry(query);
		return countryList;
	}

	// Add Messages
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

	public void actionTester(String action) {
		System.out.print("\n-------- Action is Tested -----------");
		this.addMessage("Test action is invoked :: " + action);
	}

	public void setSelectedProduct(TaxPosition selectedProduct) {
		this.selectedPosition = selectedProduct;
	}

	public List<TaxPosition> getSelectedProducts() {
		return selectedPositions;
	}

	public void setSelectedProducts(List<TaxPosition> selectedProducts) {
		this.selectedPositions = selectedProducts;
	}

	public void openNew() {
		this.selectedPosition = new TaxPosition();
	}

	public void saveProduct() {
		if (this.selectedPosition.getCode() == null) {
			this.selectedPosition.setCode(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9));
			this.positions.add(this.selectedPosition);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));
		}

		PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
	}

	public void deleteProduct() {
		this.positions.remove(this.selectedPosition);
		this.selectedPosition = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedProducts()) {
			int size = this.selectedPositions.size();
			return size > 1 ? size + " products selected" : "1 product selected";
		}

		return "Delete";
	}

	public boolean hasSelectedProducts() {
		return this.selectedPositions != null && !this.selectedPositions.isEmpty();
	}

	public void deleteSelectedProducts() {
		this.positions.removeAll(this.selectedPositions);
		this.selectedPositions = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Products Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
		PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
	}

	public List<TaxPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<TaxPosition> positions) {
		this.positions = positions;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ReferenceDataUtiity getRef() {
		return ref;
	}

	public void setRef(ReferenceDataUtiity ref) {
		this.ref = ref;
	}

	public TaxPosition getSelectedProduct() {
		return selectedPosition;
	}

	public TaxPosition getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(TaxPosition selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public List<TaxPosition> getSelectedPositions() {
		return selectedPositions;
	}

	public void setSelectedPositions(List<TaxPosition> selectedPositions) {
		this.selectedPositions = selectedPositions;
	}

	public LoginView getFather() {
		return father;
	}

	public void setFather(LoginView father) {
		this.father = father;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

}