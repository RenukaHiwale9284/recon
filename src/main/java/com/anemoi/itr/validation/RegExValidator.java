package com.anemoi.itr.validation;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExValidator extends GenValidator {

	Pattern P 	;
		
	public RegExValidator(Validator v) {
		data = v ;
	    P = Pattern.compile(v.getFormula());
		}

	@Override
	public boolean validate(String value) {		
        Matcher m = P.matcher(value);   	 
        return m.matches();
		}

	@Override
	public boolean validate(double value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(Date dd) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(int number) {
		// TODO Auto-generated method stub
		return false;
	}

}
