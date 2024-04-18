package com.anemoi.itr.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.anemoi.itr.storage.HibernateStore;

public class ValidationMaster
	{

	public static void main(String[] argv)
		{
		ValidationMaster vc = new ValidationMaster();
		ArrayList<Validator> all = vc.createValidators() ;
		
		HibernateStore store = new HibernateStore();		
		store.store(all, "Validator");				
		List<Validator> v = store.fetchValidators();
		
		
		HashMap<String, GenValidator> VV = extract();
		
		GenValidator gv = VV.get("pancard");
		
		System.out.print("Message ::" +gv.validate("APCPK5548J")) ;
		
		
		
		}
		
	
	public static ArrayList<Validator> createValidators()
		{
		ArrayList<Validator> allV = new ArrayList<Validator>() ;
		Validator gv1 = new Validator("pancard", "String", "RegEx", "[A-Z]{5}[0-9]{4}[A-Z]{1}", "Pancard information is invalid");
		Validator gv2 = new Validator("adharcard", "String", "RegEx", "^[2-9]{1}[0-9]{3}\\\\s[0-9]{4}\\\\s[0-9]{4}$", "Adhar card data is invalid");
		Validator gv3 = new Validator("pincode", "String", "RegEx", "^[1-9][0-9]{5}$", "Pincode information is invalid");
		Validator gv4 = new Validator("email", "String", "RegEx", "^(.+)@(\\\\S+)$", "Email is in inappropriate format");
		Validator gv5 = new Validator("mobileNum", "String", "RegEx", "^\\\\d{10}$", "Mobile information is not valid");		
		
		allV.add(gv1);
		allV.add(gv2);
		allV.add(gv3);
		allV.add(gv4);
		allV.add(gv5);
		
		return allV ;
		}


	public static HashMap<String, GenValidator> extract() {
		
		HashMap<String, GenValidator> vmap = new HashMap<String, GenValidator>();
		HibernateStore store = new HibernateStore();		
		List<Validator> vlist = store.fetchValidators();

		for(Validator v : vlist)
			{
			if(v.execution.equalsIgnoreCase("RegEx"))
				{
				GenValidator gv = new RegExValidator(v);
				vmap.put(v.command, gv);
				}
			}
		
		return vmap ;
		}

	
	}

	