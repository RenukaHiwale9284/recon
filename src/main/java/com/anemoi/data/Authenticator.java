package com.anemoi.data;

import java.sql.Timestamp;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anemoi.security.HashCore;
import com.anemoi.security.Infosec;



@Component("auth")
@ManagedBean
@ApplicationScoped
public class Authenticator {
	
	@Autowired Infosec infosec ;
	
	public boolean authenticate(int pid, String pass, String token, long nonce)
		{
		System.out.print("\nAuthenticating record : " + pid ); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		String hql = " From Proj P where  P.projectId =  " + pid  ;
		Query<Proj> query = session.createQuery(hql);
		
		List<Proj> results = query.list();		
		session.close();

		if(results.size() == 0) 
			{
			System.out.print("\nNot authenticated :::: Project Id is wrong " + pid + "-->" +pass + "\nHQL :" + hql);
			return false ;
			}
		
		boolean rightlogin = false ;
		for(Proj pp : results)
			{
			pp.show("Authenticating ------- ");
			HashCore hasher = new HashCore() ;
				
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			if(pp.getEncrSecret().equals(hasher.hash1(pass)) &&  pp.getLockedTill().before(now) )
				{					
				System.out.print("\nAuthenticated ------" + pid);
				return true ;
				}			
			
			}

		System.out.print("\nNot authenticated :::: Wrong credentials " + pid + "-->" +pass + "\nHQL :" + hql);
		return false ;
		}	

	public static void main(String[] argv)
		{	
		Authenticator auth = new Authenticator();
		auth.authenticate(2, "ZBCA", "", 1);
		}

	
	public boolean isLocked(int pid) {
		
		System.out.print("\nLock check : Authenticating record : " + pid ); 
		Session session = HibernateUtil.getSessionFactory().openSession();
		String hql = " From Proj P where  P.projectId =  " + pid  ;
		Query<Proj> query = session.createQuery(hql);
		
		List<Proj> results = query.list();		
		session.close();

		if(results.size() == 0) 
			{
			System.out.print("\nNot locked :::: project Id is wrong " + pid);
			return false ;
			}
		
		boolean rightlogin = false ;
		for(Proj pp : results)
			{
			pp.show("Lock check ------- ");
		
			Timestamp now = new Timestamp(System.currentTimeMillis());
			
			if(pp.getLockedTill().after(now) )
				{					
				System.out.print("\nlocked ------" + pid);
				return true ;
				}			
			
			}

		System.out.print("\nNot locked " + pid + "-->" );
		return false ;
		}
	
	
	}
