package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.stereotype.Component;

import com.anemoi.data.DtaaTaxRate;
import com.anemoi.data.HibernateReader;
import com.anemoi.data.ItaaTaxRate;
import com.anemoi.tqm.TaxPosition;

@Component("ref")
@ManagedBean
@ApplicationScoped
public class ReferenceDataUtiity {

	
	HashMap<String, ArrayList<String>> refMap = new HashMap<String, ArrayList<String>>();
		
	List<String> defReasons ;		
	
	List<String> defSources ; 
	
	List<String> defNatures ;
	
	List<ItaaTaxRate> itaa ;
	
	List<DtaaTaxRate> dtaa ;
	
	HashMap<String,ItaaTaxRate> itaaRates = new HashMap<String,ItaaTaxRate>();

	List<String> countries ; 
	
	public List<String> getList(String tag) 
		{
		// TODO Auto-generated method stub
		return null;
		}
	
	public List<DtaaTaxRate> fetchDtaa(String country)
		{
		HibernateReader hr = new HibernateReader(0);
		return hr.readDtaa(country);
		}
		
	
    @PostConstruct
	public void load()
		{
		ArrayList<String> slist = new ArrayList<String>();
		slist.add("26AS");
		slist.add("SalesRegister");
		slist.add("RPT");
		refMap.put("sources", slist); 
		defSources = slist;
			
		
		ArrayList<String> nlist = new ArrayList<String>();
		nlist.add("Royalty-non-taxable");
		nlist.add("Royalty-taxable");
		nlist.add("Technical-taxable");		
		refMap.put("nature", nlist);
		this.defNatures = nlist;
		
		
		ArrayList<String> rlist = new ArrayList<String>();		
		rlist.add("Accural");
		rlist.add("Incorrect TDS filed");
		rlist.add("Currency exchange differences");
		refMap.put("reason", rlist);
		defReasons = rlist;		

		// Load the Itaa rates 
		HibernateReader hr = new  HibernateReader(0);
		this.itaa  = hr.readItaa();		
    	this.itaaRates.clear(); 
    	for(ItaaTaxRate kk : this.itaa)
    		{
    		this.itaaRates.put( kk.getNature(), kk);
    		}		

		// Load the Dtaa rates 
		this.dtaa  = hr.readDtaa(null);		

		
    	// Load the Nature from Itaa		
		ArrayList<String> nlist2 = new ArrayList<String>();
		for(int i = 0 ; i < this.itaa .size() ; i++)
			{
			nlist2.add(this.itaa .get(i).getNature());
			}
		this.defNatures = nlist2 ;		

		this.countries = hr.readCountries() ;
		
		}
    
    
    
    
  
    
    public List<String> getEnum(String tag)
    	{
    	return refMap.get(tag);
    	}
    
	
    public String getDefSource(int srcId)
    	{
    	return this.defSources.get(srcId-1) ;
    	}
    
    
    
	public HashMap<String, ArrayList<String>> getRefMap() {
		return refMap;
	}


	public void setRefMap(HashMap<String, ArrayList<String>> refMap) {
		this.refMap = refMap;
	}


	public List<String> getDefReasons() {
		return defReasons;
	}


	public void setDefReasons(List<String> defReasons) {
		this.defReasons = defReasons;
	}


	public static boolean isEnum(String string, String nature) {
		// TODO Auto-generated method stub
		return true;
	}


	public List<String> getDefSources() {
		return defSources;
	}


	public void setDefSources(List<String> defSources) {
		this.defSources = defSources;
	}


	public List<String> getDefNatures() {
		return defNatures;
	}


	public void setDefNatures(List<String> defNatures) {
		this.defNatures = defNatures;
	}



	public String find(String mapType, String key) {
		// TODO Auto-generated method stub
		return null;
	}



	public List<ItaaTaxRate> getItaa() {
		return itaa;
	}



	public void setItaa(List<ItaaTaxRate> itaa) {
		this.itaa = itaa;
	}



	public HashMap<String, ItaaTaxRate> getItaaRates() {
		return itaaRates;
	}



	public void setItaaRates(HashMap<String, ItaaTaxRate> itaaRates) {
		this.itaaRates = itaaRates;
	}

	public List<TaxPosition> getDtaaRates() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public List<String> fetchCountry(String query) {
		
		String qq = query.toUpperCase() ;
        return this.countries.stream().filter(t -> t.toUpperCase().startsWith(qq)).collect(Collectors.toList());
		}

	public List<DtaaTaxRate> getDtaa() {
		return dtaa;
	}

	public void setDtaa(List<DtaaTaxRate> dtaa) {
		this.dtaa = dtaa;
	}



	
}
