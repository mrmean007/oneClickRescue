package com.example.sqlandroid;

public class ValidateRecords {

	public boolean validateName(String name){
		
		if( name.length() <= 0 )
			return false;
		else
			return true;
		
	}
	
	public boolean validatePassword(String password){
		
		if( password.length() <= 0)
			return false;
		else
		return true;
	
	}
	
	
	public boolean validatePhone(String phone){
		if( phone.length() <= 0)
			return false;
		else
		return true;
	}
	
	public boolean validateAddress(String add){
		if( add.length() <= 0)
			return false;
		else
		return true;
	}
	
	public boolean validateEmail(String mail){
		if( mail.length() <= 0)
			return false;
		else
		return true;
	}
	
	
}
