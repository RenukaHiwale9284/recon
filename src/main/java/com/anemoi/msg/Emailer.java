package com.anemoi.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.*;
import javax.mail.internet.* ;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.anemoi.data.Proj;

@Component("email_service")
@ManagedBean
@ApplicationScoped
public class Emailer {

	String username = "ctoavi@gmail.com";
	String apppassword = "blrdtivhczzcxzlx";
	
	Properties prop = new Properties();

	public void setup() {
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
	}

	public void testSetup(String host, String port) {
		prop = new Properties();
		
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port );
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS
	}
	
	
	public void setSender(String usr, String pass) {
		this.username = usr;
		this.apppassword = pass;
		}

	public void send(String fromAddr, String tolist, String subject, String body) 
		{		
		Session session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, apppassword);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress());
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(tolist));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

			System.out.println("Done");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		otpTest();
	}
	
	public static void otpTest()
	{

		Emailer agent = new Emailer();

		agent.testSetup("smtp.gmail.com", "587");		
		agent.setSender("ctoavi@gmail.com", "blrdtivhczzcxzlx");

		ArrayList<String> emailIds = new ArrayList<String>();		
		emailIds.add("abhinav.shrivastav@anemoitechnologies.com");
		emailIds.add("adhisha.gupta@anemoitechnologies.com");
		emailIds.add("anudit.bhatt@anemoitechnologies.com");
		emailIds.add("pragya.maurya@anemoitechnologies.com");
		emailIds.add("sachin.madhukar.khaire.tpr@pwc.com");
		
		agent.setDefaultConfig();
		
		for(String toList : emailIds)
			{
			String otp = "3123" ;
			
			agent.sendOtp(toList, otp);
			}
	}
	
	public void emailTest() throws Exception
		{

		Emailer agent = new Emailer();

		agent.testSetup("smtp.gmail.com", "587");		
		agent.setSender("ctoavi@gmail.com", "blrdtivhczzcxzlx");

		ArrayList<String> emailIds = new ArrayList<String>();		
		emailIds.add("abhinav.shrivastav@anemoitechnologies.com");
		emailIds.add("adhisha.gupta@anemoitechnologies.com");
		emailIds.add("anudit.bhatt@anemoitechnologies.com");
		emailIds.add("pragya.maurya@anemoitechnologies.com");
		emailIds.add("sachin.madhukar.khaire.tpr@pwc.com");
		
		
		agent.setDefaultConfig();
		
		for(String toList : emailIds)
			{
			String fromAddr = "ctoavi@gmail.com";
			String subject = "Authentication message : OTP";
			String body = "Dear " + toList + " \n Kindly enter the PwC-" + (System.currentTimeMillis()/1000)  ;
						
			Thread.sleep(7);
			
			agent.send(fromAddr, toList, subject, body);
			}
		}	
	
	@Async("threadPoolTaskExecutor")
	public void sendOtp(String toaddress, String otp)
		{		
		this.setDefaultConfig(); 
		
        System.out.print("\nSMTP config in SENDOTP " + prop.toString());
		
		
		String fromAddr = prop.getProperty("mail.auth.sender") ;
		String subject = prop.getProperty("mail.auth.subject") ;
		String body = prop.getProperty("mail.auth.body") + "OTP=" + otp ;		
		
		send(fromAddr, toaddress, subject, body);		
		}

	
	public void setDefaultConfig()  {
		
		InputStream input = null ;
	  try {
		   input = getClass().getClassLoader().getResourceAsStream("smtpconfig.properties"); 
            prop = new Properties();
            prop.load(input);

            System.out.print("\nSMTP config : " + prop.toString());
            
            setSender(prop.getProperty("mail.smtp.account"), prop.getProperty("mail.smtp.secret")); 
            
            input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	 
	}


	@Async("threadPoolTaskExecutor")
	public void sendPasswords(ArrayList<Proj> newProjects) 
		{
		this.setDefaultConfig();

		for(Proj pp : newProjects)
			{
			String[] addr = pp.getUsers().split(",");
				
			for(String usr : addr)		
				{
				System.out.print("\nSending the msg " + usr);
				
				String fromAddr = prop.getProperty("mail.auth.sender") ;
				String subject = prop.getProperty("mail.newpass.subject") ;
				String body = prop.getProperty("mail.newpass.body")  + "\tProjectId=" 
													+ pp.getProjectId() + "\tPASS=" + pp.getSecret();		
				
				send(fromAddr, usr, subject, body);		
				
				}
			}
		
		}
}
