 package com.anemoi.itr.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.anemoi.data.HibernateUtil;
import com.anemoi.itr.CellTemplate;
import com.anemoi.itr.ITR6DataSet;
import com.anemoi.itr.ListTemplate;
import com.anemoi.itr.SheetTemplate;
import com.anemoi.itr.TableConfiguration;
import com.anemoi.itr.TaxElement;
import com.anemoi.itr.TaxListElement;
import com.anemoi.itr.TemplateConf;
import com.anemoi.itr.ref.RefData;
import com.anemoi.itr.validation.Validator;
import com.anemoi.util.Util;

public class HibernateStore implements InfoStore 
	{
	File itr6File;

	int projectId;

	double itrversion ;
	
	public HibernateStore() 
		{
		
		}

	public HibernateStore(int projid, double version) throws Exception {
		this.projectId = projid;
		this.itrversion = version ;
		
		String projectParentDirStr = Util.getProp("projectData");
		
		if(projectParentDirStr.contains("windows") || projectParentDirStr.contains("system") || projectParentDirStr.contains("root"))
			throw new Exception("ERR:482123 Project parent directory is not allowed for exectuion " + projectParentDirStr);
		File projectParentDir = new File(projectParentDirStr);

		if (!projectParentDir.exists())
			throw new Exception("ERR:492343 Project parent directory doesn't exit " + projectParentDir);

		File clientDataDirectory = new File(projectParentDir, projectId + "");
		itr6File = new File(clientDataDirectory, "ITR6_Template.xlsm");

	}

	public void loadProject(String projId) {

	}

	public static void main(String[] argv) {

	}

	public File getConfigFile() {
		return null;
	}

	public File getITR6File() {
		return itr6File;
	}

	public void loadTemplateConf(TemplateConf templateConf) throws Exception {		
		List results = this.getHibernateList("FROM TableConfiguration E WHERE E.projectId =  " + this.projectId);
		templateConf.load(results);		
		}
	
	
	public void loadData(ITR6DataSet dataset) throws Exception {
		List results = this.getHibernateList("FROM TaxElement E WHERE E.projectId =  " + this.projectId);
		dataset.load(results);						
		}

	
	public void loadStructure(SheetTemplate sheetTemplate) throws Exception {

		List cellresults  = this.getHibernateList("FROM CellTemplate E WHERE E.version =  " + this.itrversion );
		System.out.print("\nFetched : CellTemplates " + cellresults.size());
		sheetTemplate.loadCellTemplates(cellresults);						
		
		String hql = "FROM ListTemplate E WHERE E.version =  " + this.itrversion ;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		
		List listResults = query.list();
		System.out.print("\nFetched : List Templates " + listResults.size());
				
		sheetTemplate.loadListTemplates(listResults); 
		session.close();
		
		}
	
	public List<Validator> fetchValidators()
		{
		List<Validator> results = this.getHibernateList("FROM Validator v ");
		return results;		
		}
	
	private List getHibernateList(String hql)
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
        	
        	System.out.print("\nSaving element :" + entityName);
        	session.saveOrUpdate(entityName, o);
        	}
        session.getTransaction().commit();
        session.close();		
		}
		
	
	public void store(TemplateConf conf) throws org.hibernate.exception.ConstraintViolationException
		{		

		List<TableConfiguration> values = conf.getTableOffset() ;
		System.out.print("\nStoring Template Configuration :: " + values.size() );
		for(int i = 0 ; i < values.size() ; i++)
			{
			TableConfiguration tc = values.get(i);
			tc.setProjectId(this.projectId);
			((TableConfiguration) values.get(i)).show( "Store:" + i );
			}
		
		store(values, "TableConfiguration");		
		}
	
	

	public void store(ITR6DataSet data2) {
		data2.setProjectId(this.projectId);
		List values = data2.getValues();
		store(values, "TaxElement");		
		}
	

	public void storeTables(ITR6DataSet data2 , TemplateConf tc) throws Exception 
		{
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
        session.beginTransaction();        

		ArrayList<TableConfiguration> tlist = tc.getTableOffset();
		
		for ( int i = 0 ; i < tlist.size() ; i++)
			{
			TableConfiguration conf = tlist.get(i) ;
			System.out.print("\nStoring :: " + conf.getTableName());	
			
			GenDataTable gcc = data2.extractTable( this.projectId, conf.getTableName(), conf);	
			gcc.show(); 
			
			GenTableDefinition def = gcc.getGenTableDefinition() ;
			session.saveOrUpdate(def) ;
						
			List rows = gcc.getGenRowData();
						
			for( int idx = 0 ; idx < rows.size() ; idx++)
				{
				session.saveOrUpdate("GenRowData", rows.get(idx));			
				}
			
			conf.setData_length(rows.size());
			conf.recalOffset();
			session.saveOrUpdate("TableConfiguration", conf);
			}
		session.getTransaction().commit();
		session.close();
		}
	

	public void storeTable(TaxListElement listelement, TableConfiguration tc ) 
		{
		GenDataTable gcc = listelement.buildGenDataTable();
		gcc.setTable(tc.getTableName());
		
		deleteGenData(tc.getTableName());
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
	    session.beginTransaction();        

	    gcc.show(); 		
		GenTableDefinition def = gcc.getGenTableDefinition() ;
		def.setProjectId( this.projectId );
		session.save(def) ;
		
		List<GenRowData> rows = gcc.getGenRowData();
					
		for( int idx = 0 ; idx < rows.size() ; idx++)
			{
			GenRowData gd = rows.get(idx);
			gd.setProjectId(this.projectId);
			session.save("GenRowData", gd);			
			}

		tc.setProjectId(this.projectId);
		
		session.saveOrUpdate("TableConfiguration", tc);
		session.getTransaction().commit();
		session.close();
		}
	
	
	
	
	
	public void store(SheetTemplate structure) throws org.hibernate.exception.ConstraintViolationException
		{
		Session session = HibernateUtil.getSessionFactory().openSession();		
        session.beginTransaction();        
		
		
		List listtmps = structure.getListRefernces(); 		
		
		System.out.print("\nTotal of ListTemplates :: " + listtmps.size() );

		for(int i = 0 ; i < listtmps.size() ; i++)
			{			
			ListTemplate lst = (ListTemplate) listtmps.get(i);
			
			
			lst.buildFlyWeight();
			System.out.print("\nStoring the ListTemplate :: " + lst.getTableVar() );
			lst.show();			
			
			// Delete the existing records for the ITR6 template
			
			String hql = "FROM ListTemplate E WHERE E.version =  " + lst.getVersion() + " AND E.tableVar = '" + lst.getTableVar() + "'"  ;
			Query query = session.createQuery(hql);			
			List<ListTemplate> existingLists = query.list();
			for( ListTemplate o :  existingLists )
				{
				session.delete(o);
				}			
			
			session.saveOrUpdate("ListTemplate", lst);
			}
		
		System.out.print("\n ::::::::::: Storing Cell Template ------------------- ");		
		List<CellTemplate> tmps = structure.getStaticRefrences();
		for(int i = 0 ; i < tmps.size() ; i++)
			{			
			CellTemplate ct = tmps.get(i);
			ct.setVersion(this.itrversion);
			session.saveOrUpdate("CellTemplate", ct);
			ct.show();
			}	
		session.getTransaction().commit();		
		session.close();

		}

	@Override
	public GenDataTable fetchDataTable(String tablename, TableConfiguration tc) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
        session.beginTransaction();        
				
        GenDataTable table = new GenDataTable ();
        
        
		List definition  = this.getHibernateList("FROM GenTableDefinition E WHERE E.projectId =  " + this.projectId + " AND E.tableName = '" + tablename + "'");
		
		GenTableDefinition gtd = (GenTableDefinition)definition.get(0) ;
		ArrayList<String> headers = gtd.getList() ;
		
		System.out.print("\nHeaders :: " + headers);
		
		
		table.setHeaders(headers);
		List<GenRowData> data  = this.getHibernateList("FROM GenRowData E WHERE E.projectId =  " + this.projectId + " AND E.tableName = '" + tablename + "' order by RowNum ASC");

		
		for (int i = 0 ; i < data.size() ; i++)
			{
			GenRowData gtable = data.get(i);
			ArrayList<String> r = gtable.getList();
			
			table.pushRow(r);		
			}
		
		
		table.setProjectId(this.projectId);
		table.setTable(tablename);		
		table.setLenght(data.size());
		
		session.close();
		return table ;
		}



	public List<RefData> readRefData() {
		List<RefData> data  = this.getHibernateList("FROM RefData E WHERE E.itrVersion =  " + this.itrversion );       
		System.out.print("\nReading the RefData records ");
		return data ;
		}	

	
	public void storeRefData(ArrayList<RefData> data) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
        session.beginTransaction();        
        for(int i = 0 ; i < data.size() ;i++)
        	{
        	RefData o = data.get(i);
        	o.setItrVersion(this.itrversion);
        	System.out.print("\nSaving RefData :" );
        	session.saveOrUpdate("RefData", o);
        	}
        session.getTransaction().commit();
        session.close();		
		}

	public void deleteTableConfig() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
        session.beginTransaction();        
		
		String hql = "FROM TableConfiguration E WHERE E.projectId = " + this.projectId ;
		Query query = session.createQuery(hql);			
		List<TableConfiguration> existingLists = query.list();
		for( TableConfiguration o :  existingLists )
			{
			session.delete(o);
			}				
		
		session.getTransaction().commit();
        session.close();	
		}
	

	public void deleteData(String list) 
		{	
		System.out.print("\nDeleting the data " + list);
	
		Session session = HibernateUtil.getSessionFactory().openSession();		
	    session.beginTransaction();
	    
	        String hql = "DELETE FROM " + list + " E WHERE E.projectId =  " + this.projectId ;
	        session.createQuery(hql).executeUpdate();
	        session.flush();
	    
		session.getTransaction().commit();
	    session.close();		
		}


	public void deleteGenData(String genTableName) 
		{	
		System.out.print("\nDeleting the data " + genTableName);
	
		Session session = HibernateUtil.getSessionFactory().openSession();		
	    session.beginTransaction();
	    
        String hql = "DELETE FROM GenRowData E WHERE E.tableName ='" + genTableName + "' AND E.projectId =  " + this.projectId ;
        session.createQuery(hql).executeUpdate();
	        
        String hql2 = "DELETE FROM GenTableDefinition E WHERE E.tableName ='" + genTableName + "' AND E.projectId =  " + this.projectId ;
        session.createQuery(hql2).executeUpdate();

        session.flush();
		session.getTransaction().commit();
	    session.close();		
		}
	
	
	public void deleteProjectData()
		{
		System.out.print("\nDeleting all genrows and taxelemenets" );
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
	    session.beginTransaction();
	    
        String hql = "DELETE FROM GenRowData E WHERE E.projectId =  " + this.projectId ;
        session.createQuery(hql).executeUpdate();
	        
        String hql2 = "DELETE FROM GenTableDefinition E WHERE E.projectId =  " + this.projectId ;
        session.createQuery(hql2).executeUpdate();

        String hql3 = "DELETE FROM TaxElement E WHERE E.projectId =  " + this.projectId ;
        session.createQuery(hql3).executeUpdate();

        session.flush();
		session.getTransaction().commit();
	    session.close();		
			
		}

	public void storeRawTable(List<GenRowData> rowlist, String table) throws Exception {

		System.out.print("\nStoring Raw Table" );
		
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
	    session.beginTransaction();
	    
        String hql = "FROM TableConfiguration E WHERE E.projectId =  " + this.projectId + " AND E.tableName = '" + table + "'";
		Query<TableConfiguration> query = session.createQuery(hql);
		List<TableConfiguration> results = query.list();
		
		if(results.size() != 1) throw new Exception ("Table configuration not exist for " + table );
        
		TableConfiguration tconf = results.get(0);
		
		
		tconf.setData_length( rowlist.size() );
		tconf.reset();       
		
		session.saveOrUpdate( "TableConfiguration", tconf);
		

		for( int idx = 0 ; idx < rowlist.size() ; idx++)
			{
			GenRowData gd = rowlist.get(idx);
			gd.setProjectId(this.projectId);
			session.save("GenRowData", gd);			
			}
		
				
        session.flush();
		session.getTransaction().commit();
	    session.close();		
		
		}

	public void storeTaxElements(ArrayList<TaxElement> divientRows) 
		{	
		Session session = HibernateUtil.getSessionFactory().openSession();
		
        session.beginTransaction();        
        for(int i = 0 ; i < divientRows.size() ;i++)
        	{
        	TaxElement o = divientRows.get(i);
        	o.setProjectId(this.projectId);
        	System.out.print("\ncheck993 :" );
        	o.show();
        	session.saveOrUpdate("TaxElement", o);
        	}
        session.getTransaction().commit();
        session.close();
		}
	
	
	
	}




























