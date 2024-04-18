package com.anemoi.itr.storage;

import java.util.Comparator;

import com.anemoi.itr.ListTemplate;

public class TemplateComparator implements Comparator<ListTemplate>{

	@Override
	public int compare(ListTemplate l1, ListTemplate l2) {
		
		int dd = l1.getTab().compareTo( l2.getTab()) ;				
		if(dd != 0) return dd ;
		
		if(l1.getRowIdx() > l2.getRowIdx()) return 1  ; 
				
		if(l1.getRowIdx() < l2.getRowIdx()) return -1 ;
		
		return 0;
	}

	
}
