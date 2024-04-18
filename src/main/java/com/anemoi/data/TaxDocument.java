package com.anemoi.data;

import java.util.Date;

public class TaxDocument 
	{
	int taxDocumentId ;
	int projectId ;
	
	String docType ;
	
	String filePath ;
	
	Date createdAt ;
			
	String encrKey ;

	public TaxDocument(int did, int pid, String dtype, String fpath, Date cre, String key )
		{
		this.taxDocumentId = did ;
		this.projectId = pid ;
		this.docType = dtype ;
		this.filePath = fpath ;
		this.createdAt = cre ;
		this.encrKey = key ;
		}
	
	public int getTaxDocumentId() {
		return taxDocumentId;
	}

	public void setTaxDocumentId(int taxDocumentId) {
		this.taxDocumentId = taxDocumentId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getEncrKey() {
		return encrKey;
	}

	public void setEncrKey(String encrKey) {
		this.encrKey = encrKey;
	}

	
	}
