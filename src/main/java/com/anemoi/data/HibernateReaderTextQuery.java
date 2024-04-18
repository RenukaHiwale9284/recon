package com.anemoi.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.query.Query;

import com.anemoi.recon.DataSummmary;
import com.anemoi.recon.IncomeAmount;
import com.anemoi.recon.IncomeDocument;
import com.anemoi.recon.ValueMapDocument;
import com.anemoi.tqm.TaxPosition;

import org.hibernate.Criteria;
import org.hibernate.Session;


public class HibernateReaderTextQuery 
	{
	int projectId;
	
	public HibernateReaderTextQuery(int proj) 
		{
		this.projectId = proj ;
		}

		
	public List getHibernateList(String hql)
		{
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		List results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}

	public void store(List data, String entityName)  throws org.hibernate.exception.ConstraintViolationException
		{				
		Session session = HibernateUtil.getSessionFactory().openSession();
				
        session.beginTransaction();        
        for(int i = 0 ; i < data.size() ;i++)
        	{
        	Object o = data.get(i);
        	
        	System.out.print("\n");
        	session.saveOrUpdate(entityName, o);
        	}
        session.getTransaction().commit();
        session.close();		
		}

		
	
	
	public IncomeDocument readSRAggr()
		{
		HashMap<String,Double> result = new HashMap<String,Double>();
		ArrayList<String> keylist = new ArrayList<String>();
	
		Session session = HibernateUtil.getSessionFactory().openSession();
//		String hql = "select bk.writer, max(bk.price) from Book as bk group by bk.writer having avg(bk.price) > 100";
		
		String hql = "select S.partyName, sum(S.invoiceAmount) from SalesRecord as S where S.projectId = " + this.projectId + " group by S.partyName ";
		List<?> list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			
			System.out.println(row[0]+ ","+ row[1] + "," + row[0].getClass() + "," + row[1].getClass());			
			String key = (String)row[0];
			double value1 = (Double)row[1];
			
			result.put(key,value1);
			keylist.add(key);

			}
		session.close();
		
		ValueMapDocument vmap = new ValueMapDocument(result, keylist);

		return vmap ;
		}
	
	public IncomeDocument readRPTAggr()
		{
		HashMap<String,Double> result = new HashMap<String,Double>();
		ArrayList<String> keylist = new ArrayList<String>();

		Session session = HibernateUtil.getSessionFactory().openSession();
	//	String hql = "select bk.writer, max(bk.price) from Book as bk group by bk.writer having avg(bk.price) > 100";
		
		String hql = "select S.partyName, sum(S.amount) from RPTRecord as S where S.projectId = " + this.projectId + " group by S.partyName ";
		List<?> list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			
			System.out.println(row[0]+ ","+ row[1] + "," + row[0].getClass() + "," + row[1].getClass());			
			String key = (String)row[0];
			double value1 = (Double)row[1];
			
			result.put(key,value1);
			keylist.add(key);
			}
		session.close();
		ValueMapDocument vmap = new ValueMapDocument(result, keylist);

		return vmap ;
		}


	public IncomeDocument read26ASAggr()
		{
		HashMap<String,Double> result1 = new HashMap<String,Double>();
		HashMap<String,Double> result2 = new HashMap<String,Double>();
		ArrayList<String> keylist = new ArrayList<String>();
		Session session = HibernateUtil.getSessionFactory().openSession();
	//	String hql = "select bk.writer, max(bk.price) from Book as bk group by bk.writer having avg(bk.price) > 100";
		
		String hql = "select S.partyName, sum(S.tac), sum(S.ttd) from PartyRecord as S where S.projectId = " 
										+ this.projectId + " group by S.partyName ";
		List<?> list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			
			System.out.println(row[0]+ ","+ row[1] + "," + row[0].getClass() + "," + row[1].getClass());			
			String key = (String)row[0];
			double value1 = (Double)row[1];
			double value2 = (Double)row[2];
			
			result1.put(key,value1);
			result2.put(key,value2);
			keylist.add(key);
		}
		session.close();
		ValueMapDocument vmap = new ValueMapDocument(result1, keylist, result2);
		
		return vmap ;
		}
	

	public HashMap<String,Double> readTDSAggr()
		{
		HashMap<String,Double> result = new HashMap<String,Double>();
		Session session = HibernateUtil.getSessionFactory().openSession();
	//	String hql = "select bk.writer, max(bk.price) from Book as bk group by bk.writer having avg(bk.price) > 100";
		
		String hql = "select S.partyName, sum(S.ttd) from PartyRecord as S where S.projectId = " 
										+ this.projectId + " group by S.partyName ";
		List<?> list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			
			System.out.println(row[0]+ ","+ row[1] + "," + row[0].getClass() + "," + row[1].getClass());			
			result.put((String)row[0], (Double)row[1]);
			}
		session.close();
		
		return result ;
		}	
	
	
	public List<DataSummmary> readSummary()
		{
		ArrayList<DataSummmary> summary = new ArrayList<DataSummmary>();

		
		Session session = HibernateUtil.getSessionFactory().openSession();
	//	String hql = "select bk.writer, max(bk.price) from Book as bk group by bk.writer having avg(bk.price) > 100";
		
		DataSummmary as26, sr, rpt = null;
		as26 = new DataSummmary();
		sr = new DataSummmary();
		rpt = new DataSummmary();
		
		String hql = "select count(distinct S.partyName) from PartyRecord as S where S.projectId = " + this.projectId  ;
		List<?> list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) 
			{
			Object[] row26as = (Object[]) list.get(i);						

			as26.setSrNo(1);
			as26.setTag("26AS") ;
			as26.setTotalName( (Long) row26as[0]) ;
//			as26.setTotalAmount((Double) row26as[1] );
//			as26.setTotalTax((Double) row26as[2] );
			
			}
		
		hql = "select count(*), sum(amountOfRefund) from Refund as S where S.projectId = " + this.projectId  ;
		list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] refund = (Object[]) list.get(i);						
			as26.setTotalRefund( (Long) refund[0] );
			as26.setTotalRefundAmount( (Double) refund[1]);
			}

		
		hql = "select count(distinct S.partyName), count(*), sum(invoiceAmount)  from SalesRecord as S where S.projectId = " + this.projectId  ;
		list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] rowsr = (Object[]) list.get(i);						
			sr = new DataSummmary() ;
			sr.setSrNo(2);
			sr.setTag("Sales-Register");
			sr.setTotalName((Long)rowsr[0]);
			sr.setTotalTrans( (Long)rowsr[1]);
			sr.setTotalAmount((Double)rowsr[2] );
			
			}
		
		hql = "select count(distinct  S.partyName), sum(amount)  from RPTRecord as S where S.projectId = " + this.projectId  ;
		list = session.createQuery(hql).list();
		for(int i=0; i<list.size(); i++) {
			Object[] rowtpt = (Object[]) list.get(i);						
			rpt = new DataSummmary() ;
			rpt.setSrNo(3);
			rpt.setTag("RPT");
			rpt.setTotalName((Long)rowtpt[0]);
			rpt.setTotalAmount((Double)rowtpt[1] );
			}
		session.close();
		
		summary.add(as26);
		summary.add(sr);
		summary.add(rpt);

		return summary ;
		}
	
	
	public static void main(String[] argv)
		{
		HibernateReaderTextQuery br = new HibernateReaderTextQuery(65);
		
//		br.readSRAggr();

//		br.readRPTAggr();
		
//		br.read26ASAggr();
		
//		br.readTDSAggr();
		
//		br.readTDS();
	
//		br.readRPT();
		
//		br.readSR();
		
//		br.read26ASParty();
		
//		br.readRefund();

//		br.readIncomeAmount() ;
		
//		List l = br.getHibernateList("FROM ItaaTaxRate");
//		for(ItaaTaxRate o : br.readItaa())	 o.show();

		List<DataSummmary> l = br.readSummary();
			for(DataSummmary o : l)	 o.show("Document");

		}

	
	public  List<String> readCountries() {
		String hql = "from DtaaTaxRate as S "  ;
		System.out.print("\nHibernate : get List : " + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();

		Query<DtaaTaxRate> query = session.createQuery(hql);
		List<DtaaTaxRate> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		
		ArrayList<String> clist = new ArrayList<String>();
		for(int i = 0 ; i < results.size() ; i++)
			{
			String country = results.get(i).getCountry();
			
			if(!clist.contains(country)) clist.add(country) ;
			}
		return clist ;		
	}
	
	

	public List<IncomeAmount> readIncomeAmount() {
		String hql = "from IncomeAmount as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<IncomeAmount> query = session.createQuery(hql);
		List<IncomeAmount> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;		
	}


	public List<TDS> readTDS() {		
		String hql = "from TDS as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<TDS> query = session.createQuery(hql);
		List<TDS> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}

	public List<RPTRecord> readRPT() {		
		String hql = "from RPTRecord as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<RPTRecord> query = session.createQuery(hql);
		List<RPTRecord> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}
	
	public List<SalesRecord> readSR() {		
		String hql = "from SalesRecord as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<SalesRecord> query = session.createQuery(hql);
		List<SalesRecord> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}

	public List<PartyRecord> read26ASParty() {		
		String hql = "from PartyRecord as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<PartyRecord> query = session.createQuery(hql);
		List<PartyRecord> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}
	
	public List<Refund> readRefund() {		
		String hql = "from Refund as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<Refund> query = session.createQuery(hql);
		List<Refund> results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results ;
		}
	

	public List<DtaaTaxRate> readDtaa(String con) {		
		String hql = "from DtaaTaxRate as S where S.country = '" + con  + "'" ;
		
		if(con == null) hql = "from DtaaTaxRate as S " ;
			
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Query<DtaaTaxRate> query = session.createQuery(hql);
		
		List<DtaaTaxRate> results = query.list();
		session.close();
		System.out.print("Hibernate List size Dtaa:: " + results.size());
		return results ;
		}
	

	public List<ItaaTaxRate> readItaa() {		
		String hql = "from ItaaTaxRate "  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<ItaaTaxRate> query = session.createQuery(hql);
		List<ItaaTaxRate> results = query.list();
		session.close();
		System.out.print("Hibernate List size Dtaa:: " + results.size());
		return results ;
		}


	public List<Proj> projectData() {
		String hql = "from Proj as S " ;
		System.out.print("\nHibernate : get Project List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<Proj> query = session.createQuery(hql);
		List<Proj> results = query.list();
		
		for(Proj pp : results)
			{
			pp.show("Project : ");
			}
		
		session.close();
		System.out.print("Hibernate Project List size :: " + results.size());
		return results ;
	}
	

	public Proj specificProject(int pid) {
		String hql = "from Proj as S where projectId = " +  pid ;
		System.out.print("\nHibernate : get Project List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<Proj> query = session.createQuery(hql);
		List<Proj> results = query.list();
		
		for(Proj pp : results)
			{
			pp.show("Project : ");
			}
		
		session.close();
		System.out.print("Hibernate Project List size :: " + results.size());
		
		if(results.size() == 0) return null ;
		
		return results.get(0) ;
	}


	public NameData readNames() {
		String hql = "from NameData as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<NameData> query = session.createQuery(hql);
		List<NameData> results = query.list();
		
		if(results.size() == 0)
			{
			System.out.print("Hibernate No names are available for processing ");			
			return null ;
			}
		NameData data = results.get(0) ;
		session.close();
		System.out.print("Hibernate Name list size :: " + results.size());
		return data ;
	}


	public List<TaxPosition> readTaxPositions() {
		String hql = "from TaxPosition as S where S.projectId = " + this.projectId  ;
		System.out.print("\nHibernate Tax Position query :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query<TaxPosition> query = session.createQuery(hql);
		List<TaxPosition> results = query.list();
		session.close();
		System.out.print("Hibernate Tax Position List size :: " + results.size());
		return results ;
	}

	
	
	
	}




























