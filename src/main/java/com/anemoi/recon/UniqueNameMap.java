package com.anemoi.recon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.anemoi.data.NameData;

public class UniqueNameMap 
	{
	
	HashMap<String,Integer> nameSearch = new HashMap<String,Integer>(); 		
	ArrayList<String> primaryNames = new ArrayList<String>() ;
	
	public void show()
		{
		System.out.print("\n NAME SEARCH ::::::::::::: ");
		for(String nn : nameSearch.keySet())
			{
//			String nn = primaryNames.get(i);
			int pid = nameSearch.get(nn);
			System.out.print("\nNS :: (" + nn + "," + pid + ")");
			}
		System.out.print("\n Name List ::::: " + this.primaryNames);
				
		}
	
	public NameData getNameData()
		{
		NameData data = new NameData();
		
		StringBuffer sbf = new StringBuffer();
		for(String nn : this.primaryNames)
			{
			sbf.append(nn) ;
			sbf.append("&&&") ;
			}
		if(sbf.length() > 3) sbf.delete(sbf.length()-3, sbf.length()) ;
		//	10 ...                                 7            9
		data.setPrimaryName(sbf.toString());
		
		sbf = new StringBuffer();
		for(String nn : this.nameSearch.keySet())
			{
			String str = nn + "::" + this.nameSearch.get(nn) + "&&&" ;
			sbf.append(str) ;
			System.out.print("\nName Generation :: " + str );
			
			}
		
		if(sbf.length() > 3) sbf.delete(sbf.length()-3, sbf.length()) ;
		
		data.setNamePairs(sbf.toString());
		return data ;
		}
	
	
	public void loadNameData(NameData data) throws Exception
		{
		try {
		String[] pnames = data.getPrimaryName().split("&&&") ;
		primaryNames.clear();
		nameSearch.clear(); 
		
		primaryNames.addAll(Arrays.asList(pnames));
		
		String[] all = data.getNamePairs().split("&&&") ;
		for(String pair : all)
			{
			String[] dd = pair.split("::");
			nameSearch.put(dd[0],Integer.parseInt(dd[1]) );
			}
		}
		catch(Throwable ex)
			{
			ex.printStackTrace();
			throw new Exception("ERR:432094  Issue in loading the name data " + ex.getMessage());
			}
		
		}
	
	
	void newNames(ArrayList<String> names) 
		{		
		for(String s : names)
			{
			primaryNames.add(s);			
			nameSearch.put(s, primaryNames.size());
			}				
		}


	void addNamePairs(HashMap<String, String> namePairs) {
		
		for(String source : namePairs.keySet())
			{
			String dest = namePairs.get(source);
			
			System.out.print("\nName Pair :: (" + source + "," + dest + ")");

			int pid = nameSearch.get(dest) ;
			nameSearch.put(source, pid);
			}		
		}


	
	
	public ArrayList<String> getPrimaryNames()
		{
		return primaryNames ;
		}
	
	
	public void setPrimaryPartyName(List<String> pp) 
		{						
		
		for(int idx = 0 ; idx < pp.size() ;idx++ )
			{
			int pid = idx + 1 ;
			String name = pp.get(idx);			
			System.out.print("\nAdding Primary Name :: " + name + "," + pid);
			nameSearch.put(name , pid);				
			}
		
		primaryNames.addAll(nameSearch.keySet());
		}
	
	
	public ArrayList<String> topMatches(String name, int numMatches)
		{
		ArrayList<String> topMatches = new ArrayList<String> () ;
		
		for(int ll = 0 ; ll < numMatches ; ll++ )			
			{
			System.out.print("\n idx " + ll) ;
			
			String selectedName = null ;
			double leastDist = 99999 ;
			for(String sn : this.nameSearch.keySet() )
				{
				if(topMatches.contains(sn)) continue ;

				System.out.print("\n Dist start " + sn );
				String nameNorm = name.replace(" ", "").toLowerCase();
				String snNorm = sn.replace(" ", "").toLowerCase();
				
				double dist =  TaxtUtil.compute_Levenshtein_distance(nameNorm, snNorm);
				System.out.print("\n Dist = " + dist );
				
//				double dist = new FuzzyScore().fuzzyScore(name, sn);
				
				if(dist < leastDist)
					{		
					System.out.print("\n Least distance :: " + dist + "," +leastDist + "," + sn);
					leastDist = dist  ;
					selectedName = sn ;
					}
				}
			
			if(selectedName != null)
				topMatches.add(selectedName);
			}
			
			return topMatches;		
		}
	
	
	public int nameId(String name)
		{
		if(name == null)	return -1 ;

		Integer o = this.nameSearch.get(name);
		
		if(o == null)	return -1 ;
		
		return o ;
		}


	public void clear() {
		this.primaryNames.clear();
		this.nameSearch.clear();
		
	}
	
	

	}
