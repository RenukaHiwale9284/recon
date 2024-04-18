package com.anemoi.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.SplittableRandom;

import javax.faces.bean.ManagedBean;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component("infosec")
@ManagedBean
@SessionScope

public class Infosec 
	{
	
	public static String VUEP = "^[\\w.+\\-]+@[in\\.pwc]*[pwc]*[gmail]*[anemoitechnologies]*\\.com$";
	
	String mastercode = "chimbotingomingovenugopal" ;
	String mappingCode = "Lb��M���&@����JK�L��d[Z��G�ݪ�";
	String masterHash = "cdcb875c41efeb174c173a260508ff2381870e35" ;
	                
	String ecoding = "hash1" ;
	
//	String masterEmail="adhisha.gupta.tpr@in.pwc.com" ;
	String masterEmail="ctoavi@gmail.com" ;

	int otplength = 6 ;				
	long otptimeout = 180000L ;
	long lockTime = 180000L ;
	boolean factor2A = true ;
	boolean circulationPass = false ;              
	
	
	public boolean factor2Active()
		{
		return this.factor2A ;
		}
	
	public boolean validateMasterEmail(String email)
		{
		if(email == null || email.trim().length() == 0) return false ;
		
		if(email.equalsIgnoreCase(this.masterEmail)) return true ;
		
		return false ;
		}
	
	public boolean validateMaster(String pass) 
		{
		CryptAlgo crypto = new CryptAlgo() ; 		
		HashCore hc = new HashCore();
		if(pass.length() < 24) return false ; 
		
		if(this.circulationPass)
			{
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			
			hour-- ;
			
			char miss = pass.charAt(pass.length()-1) ;		
			String s1 = pass.substring(0, hour);
			String s2 = pass.substring(hour, pass.length()-1);
			System.out.print("\nModpass:" + pass.toString() + "\n" + s1 + "\n" + s2 + "\n" + miss + "\n" + hour + "\n");

			pass = s1 + miss + s2 ;			
			}
		
		String str ="" ;
		if(ecoding.equalsIgnoreCase("hash1"))
			{
			System.out.print("\nPass Verification :" + pass);
			str = hc.hash1(pass);
			System.out.print("\nHASH:::" + str + ":::");
			if(str.equalsIgnoreCase(this.masterHash))
				{
				System.out.print("\nHashMethod ;Master is Valid ");
				return true ;
				}
			}
		else
			{
			System.out.print("\nPass Verification : DES");
			byte[] encrtext = crypto.encrypt(pass.toString().getBytes(), mastercode.getBytes());				
			str = new String(encrtext) ;		
			if(str.equalsIgnoreCase(mappingCode))
				{
				System.out.print("\nMaster is Valid ");
				return true ;
				}
			}
		
			
		return false;
		}
	
	public String generateOTP()
		{
		StringBuilder generatedOTP = new StringBuilder();
		SplittableRandom splittableRandom = new SplittableRandom();

		for (int i = 0; i < otplength; i++) {				
				int randomNumber = splittableRandom.nextInt(0, 9);
				generatedOTP.append(randomNumber);
			}
		return generatedOTP.toString();
		}
	
	public static void main(String[] argv )
		{
		String data = "ThisTheToolFor@9819436007@BestPeople" ;
		
		Infosec inf = new Infosec();
		
		if( inf.validateMaster(data) )
			{
			System.out.print("\nValid Password");
			}
		
		String key = "chimbotingomingovenugopal" ;
		
		CryptAlgo crypto = new CryptAlgo() ; 
		DecryptAlgo decrypto = new DecryptAlgo() ; 
		HashCore hc = new HashCore();
		
		
		String hashcode = hc.hash1(data) ;
		
		byte[] encrtext = crypto.encrypt(data.getBytes(), key.getBytes());
		
		String cryptData = new String(encrtext);
		
		byte[] decrtext = decrypto.decrypt(encrtext, key.getBytes());

		
		System.out.println("\nCrypt Data ::"+cryptData +"::");
		System.out.println("\nHashcode ::"+hashcode +"::");
		System.out.println("\nDecrypt data ::" + new String(decrtext) +"::");

		}

	public boolean isValidOtpTimings(long otpGenAt) {
		
		long elapseTime = System.currentTimeMillis() - otpGenAt ;
		System.out.print("\bTime diff = " + elapseTime );
		
		if( elapseTime < this.otptimeout )			
			{
			System.out.print("\nElapsed Time is less than Timepout " + elapseTime + " < " + this.otptimeout);

			return true;
			}
		System.out.print("\nTimeout Elapsed Time is greater than Timepout " + elapseTime + " > " + this.otptimeout);
		
		return false ;
	}
	
	
	

	public int maxLoginAttempts() {		
		return 1;
		}

	public Timestamp getProjectLockTime() {
		long tt = System.currentTimeMillis() + this.lockTime ;
		return new Timestamp(tt);
	}

	
	}
