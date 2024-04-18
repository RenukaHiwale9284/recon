package com.anemoi.itr.validation;

import java.util.Date;
import java.util.HashMap;

import com.anemoi.itr.TaxElement;

public class ValidationUtil {
		
	HashMap<String,GenValidator> vMap = new HashMap<String,GenValidator>();
	
	public ValidationUtil()
		{
		vMap = ValidationMaster.extract() ;
		}
	
	public VMessage validate(String validations, TaxElement value) {

		System.out.print("\nVUtil :: " + validations + " on " );
		value.show();
		
		VMessage v = new VMessage();
		
		String[] vlist = validations.split(":");
		boolean flag = true ;
		GenValidator g = null ;
		
		for( String vname :  vlist )
			{
			g = vMap.get(vname);
			
			String msg = "" ;
			if(value.getValueType().equalsIgnoreCase("String"))
				{
				flag = g.validate(value.getValueStr()) ;
				}
				else if(value.getValueType().equalsIgnoreCase("Double")) 
					{
					flag = g.validate((Double)value.getValue()) ;
					}
					else if(value.getValueType().equalsIgnoreCase("Date")) 
						{
						flag = g.validate((Date)value.getValue()) ;
						}
				
			if(!flag) break ;
			}
		
		v.setCategory(value.getSheet());
		v.setVariable(value.getVar());
		v.setValue(value.getValueStr());
		
		if(!flag)	
			{
			v.setSeverity(10);
			v.setValid(false);			
			v.setMessage(g.data.command + " validation failed ");
			v.setAction(" Correct the Value ");			
			}
		else 
			{
			v.setSeverity(0);
			v.setValid(true);			
			v.setMessage("Validation succesul");
			v.setAction("None");						
			}
		
		return v ;
	}

}
