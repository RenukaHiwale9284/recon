package com.anemoi.data;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.anemoi.recon.DataSummmary;
import com.anemoi.recon.IncomeAmount;
import com.anemoi.tqm.TaxPosition;

public class HibernateWriter {
	int projectId;

	public HibernateWriter(int pid) {
		this.projectId = pid;
	}

	private List getHibernateList(String hql) {
		System.out.print("\nHibernate : get List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		List results = query.list();
		session.close();
		System.out.print("Hibernate List size :: " + results.size());
		return results;
	}

	public void store(List data, String entityName) throws org.hibernate.exception.ConstraintViolationException {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		for (int i = 0; i < data.size(); i++) {
			Object o = data.get(i);

			System.out.print("\n");
			session.saveOrUpdate(entityName, o);
		}
		session.getTransaction().commit();
		session.close();
	}

	public ArrayList<Proj> storeNewProj(List<Proj> records) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		ArrayList<Proj> newProjects = new ArrayList<Proj>();

		System.out.print("\nStoring the Project Record file  ");

		for (Proj o : records) {

			if (session.contains(o)) {
				System.out.print("\nWARN: Store New Project : Project Id already exists " + o.getProjectId());
				continue;
			}
			newProjects.add(o);

			System.out.print("\nSaving in DB ");
			o.show("Project");
			session.saveOrUpdate("Proj", o);
		}

		session.getTransaction().commit();
		session.close();

		return newProjects;
	}

	public void storeIncomeAmount(List<IncomeAmount> incomes, List<IncomeAmount> carryforwards) {

		deleteData("IncomeAmount");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		System.out.print("\nStoring the Income Amount records  ");
		for (IncomeAmount o : incomes) {
			o.setProjectId(this.projectId);
			System.out.print("\nSaving Income in DB ");
			o.show("Income");
			session.save("IncomeAmount", o);
		}

		System.out.print("\nStoring the Income Amount records  ");
		for (IncomeAmount o : carryforwards) {
			o.setProjectId(this.projectId);
			System.out.print("\nSaving Carryforward in DB ");
			o.show("CFW");
			session.save("IncomeAmount", o);
		}

		session.getTransaction().commit();
		session.close();
	}

	public void store26ASRecords(List<PartyRecord> parties) {

		this.deleteData("PartyRecord");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the 26AS file  ");

		for (PartyRecord o : parties) {
			o.setProjectId(this.projectId);

			System.out.print("\nSaving in DB ");
			o.show("R");
			session.save("PartyRecord", o);
		}

		session.getTransaction().commit();
		session.close();
	}

	public List<PartyRecord> read26ASRecords() {

		List<PartyRecord> data = this.getHibernateList(
				"FROM PartyRecord E WHERE E.projectId =  " + this.projectId + " order by E.sr_no ASC");
		System.out.print("\nReading the 26AS file  ");
		return data;
	}

	public void storeRefunds(List<Refund> refunds) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the 26AS file  ");

		for (Refund o : refunds) {
			o.setProjectId(this.projectId);

			System.out.print("\nSaving in DB ");
			o.show("Refund");
			session.saveOrUpdate("Refund", o);
		}

		session.getTransaction().commit();
		session.close();
	}

	public List<Refund> readRefunds() {
		List<Refund> data = this
				.getHibernateList("FROM Refund E WHERE E.projectId =  " + this.projectId + " order by E.srNo ASC");
		System.out.print("\nReading the Refund records ");
		return data;
	}

	public void storeSalesRecord(List<SalesRecord> records) {

		HashMap<String, Double> exchange = readExchangeRates();

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the Sales Register file  ");

		for (SalesRecord o : records) {
			o.setProjectId(this.projectId);

			if (!(o.currency == null || o.currency.equalsIgnoreCase("") || o.currency.equalsIgnoreCase("INR"))) {
				o.currency = o.currency.toUpperCase();
				double exchangeRate = exchange.get(o.currency);

				o.orgInvoiceAmount = o.invoiceAmount;

				if (o.orgInvoiceAmount != 0 && exchangeRate != 0) {
					o.invoiceAmount = o.orgInvoiceAmount / exchangeRate;
				}
			}

			System.out.print("\nSaving in DB ");
			o.show("SR");
			session.saveOrUpdate("SalesRecord", o);
		}

		session.getTransaction().commit();
		session.close();
	}

	public HashMap<String, Double> readExchangeRates() {
		List<CurrCode> data = this.getHibernateList("FROM CurrCode ");

		HashMap<String, Double> allRates = new HashMap<String, Double>();
		for (CurrCode rate : data) {
			allRates.put(rate.getCode(), rate.getRate());
		}

		System.out.print("\nReading the Exchange rates ");
		return allRates;
	}

	public List<SalesRecord> readSR() {
		List<SalesRecord> data = this
				.getHibernateList("FROM SalesRecord E WHERE E.projectId =  " + this.projectId + " order by E.srNo ASC");
		System.out.print("\nReading the Refund records ");
		return data;
	}

	public void storeRPT(List<RPTRecord> records) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the RPT Record file  ");

		for (RPTRecord o : records) {
			o.setProjectId(this.projectId);

			System.out.print("\nSaving in DB ");
			o.show("RPTRecord");
			session.saveOrUpdate("RPTRecord", o);
		}

		session.getTransaction().commit();
		session.close();

	}

	public List<RPTRecord> readRPT() {
		List<RPTRecord> data = this.getHibernateList("FROM RPTRecord E WHERE E.projectId =  " + this.projectId);
		System.out.print("\nReading the Refund records ");
		return data;
	}

	public static void main(String[] argv) {
		HibernateWriter hw = new HibernateWriter(0);

		// hw.cteateDtaa();
		// hw.createAtaa();
		hw.createCurr();
	}

	public void cteateDtaa() {
		ArrayList<DtaaTaxRate> dtaa = new ArrayList<DtaaTaxRate>();
		dtaa.add(new DtaaTaxRate(880, "BANGLADESH", "Dividend", 10, 15));
		dtaa.add(new DtaaTaxRate(880, "BANGLADESH", "Interest", 10, 0));
		dtaa.add(new DtaaTaxRate(880, "BANGLADESH", "Royalty", 10, 0));
		dtaa.add(new DtaaTaxRate(880, "BANGLADESH", "Technical", 0, 0));

		dtaa.add(new DtaaTaxRate(1, "CANADA", "Dividend", 15, 25));
		dtaa.add(new DtaaTaxRate(1, "CANADA", "Interest", 15, 0));
		dtaa.add(new DtaaTaxRate(1, "CANADA", "Royalty", 10, 15));
		dtaa.add(new DtaaTaxRate(1, "CANADA", "Technical", 10, 15));

		store(dtaa, "DtaaTaxRate");
	}

	public void createAtaa() {
		ArrayList<ItaaTaxRate> itaa = new ArrayList<ItaaTaxRate>();

		itaa.add(new ItaaTaxRate(1, "Intrest on other foriegn currency loans", "115A(1)(a)(ii)", 20));
		itaa.add(new ItaaTaxRate(2, "Interest received from infrastructure debt fund referred to in Section 10(47)",
				"115A(1)(a)(iia)", 5));
		itaa.add(new ItaaTaxRate(3, "Interest referred to under Section 194LC (other than interest paid by IFSC)",
				"115A(1)(a)(iiaa)", 5));
		itaa.add(new ItaaTaxRate(4, "Interest referred to under Section 194LC (paid by IFSC)",
				"115A(1)(a)(iiaa) (IFSC)", 4));
		itaa.add(new ItaaTaxRate(5, "Interest referred to under Section 194LD", "115A(1)(a)(iiab)", 5));
		itaa.add(new ItaaTaxRate(6, "Interest referred to under Section 194LBA(2)", "115A(1)(a)(iiac)", 5));
		itaa.add(new ItaaTaxRate(7, "Royalty", "115A(1)(b)(A)", 10));
		itaa.add(new ItaaTaxRate(8, "Technical fees", "115A(1)(b)(B)", 10));
		itaa.add(new ItaaTaxRate(9, "Dividend income", "115A(1)(a)(i)", 20));
		itaa.add(new ItaaTaxRate(10, "Others income", "Open field", 40));
		itaa.add(new ItaaTaxRate(11, "Others", "NA", 0));
		store(itaa, "ItaaTaxRate");
	}

	public void createCurr() {
		ArrayList<CurrCode> codes = new ArrayList<CurrCode>();

		codes.add(new CurrCode(1, "AED", "UAE Dirham", 1.340286));
		store(codes, "CurrCode");
	}

	public void deleteData(String list) {
		System.out.print("\nDeleting the data " + list);

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String hql = "DELETE FROM " + list + " E WHERE E.projectId =  " + this.projectId;
		session.createQuery(hql).executeUpdate();
		session.flush();

		session.getTransaction().commit();
		session.close();
	}

	public void storeNames(NameData data) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the Name records ");

		data.setProjectId(this.projectId);
		session.saveOrUpdate("NameData", data);

		session.getTransaction().commit();
		session.close();

	}

	public void storeProject(Proj myProject) {
		System.out.print("\nStoring Project status  " + myProject.getProjectId());

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		session.update("Proj", myProject);

		session.getTransaction().commit();
		session.close();
	}

	public void storeTaxComp(List<TaxPosition> positions) {

		this.deleteData("TaxPosition");

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.print("\nStoring the Tax position records :: ");

		for (TaxPosition o : positions) {
			o.setProjectId(this.projectId);
			System.out.print("\nSaving in DB Tax Position");
			session.save("TaxPosition", o);
		}

		session.getTransaction().commit();
		session.close();
	}

	
	public void lockProject(int pid, Timestamp locktime) {

		String hql = "from Proj as S where projectId = :projId ";
		System.out.print("\nHibernate : get Project List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		session.beginTransaction();
		Query<Proj> query = session.createQuery(hql);
		query.setParameter("projId", pid);

		List<Proj> results = query.list();
		for (Proj pp : results) {			
			System.out.print("\nProject lock set for pid " + pid + "\t time=" + locktime);

			pp.show("Locking the Project");
			pp.setLockedTill(locktime);
			pp.setAttempts(0);
			session.update("Proj", pp);
		}
		session.getTransaction().commit();

		session.close();
		System.out.print("Hibernate Project List size :: " + results.size());
	}

	
	public int increamentProjectAttempts() {

		String hql = "from Proj as S where projectId = :projId ";
		System.out.print("\nHibernate : get Project List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		int attempt = 0 ;
		session.beginTransaction();
		Query<Proj> query = session.createQuery(hql);
		query.setParameter("projId", this.projectId);

		List<Proj> results = query.list();
		for (Proj pp : results) {			
			System.out.print("\nProject Attempts for pid " + this.projectId + "\t time=" + pp.getAttempts());

			pp.show("Increament Attempt");
			pp.setAttempts( pp.getAttempts() + 1 );
			attempt = pp.getAttempts() ;
			session.update("Proj", pp);
		}
		session.getTransaction().commit();

		session.close();
		System.out.print("Hibernate Project List size :: " + results.size());
		return attempt ;
	}

	public void userLoggedIn() 
		{

		String hql = "from Proj as S where projectId = :projId ";
		System.out.print("\nHibernate : get Project List :" + hql);
		Session session = HibernateUtil.getSessionFactory().openSession();
		int attempt = 0 ;
		session.beginTransaction();
		Query<Proj> query = session.createQuery(hql);
		query.setParameter("projId", this.projectId);

		List<Proj> results = query.list();
		for (Proj pp : results) {			
			System.out.print("\nProject Attempts for pid " + this.projectId + "\t time=" + pp.getAttempts());

			pp.show("Increament Attempt");
			pp.setAttempts(0);
			session.update("Proj", pp);
		}
		session.getTransaction().commit();

		session.close();
		System.out.print("Hibernate Project List size :: " + results.size());
		
		}
	
	
	
	
	
	
}
