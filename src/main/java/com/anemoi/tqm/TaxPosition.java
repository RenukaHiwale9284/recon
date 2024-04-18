package com.anemoi.tqm;

import java.io.Serializable;
import java.io.Writer;
import java.util.List;

public class TaxPosition implements Serializable, Cloneable {

	//Nature
	//Income
	//TDS
	//Flag nonIncome
	//Act
	//Country
	//Section
	//Taxrate
	//TaxAmount
	//Reason
	
	
	private int projectId ;
    private int id = 0;
    private String code; 		
    private String name;			// nature 
    private String description;			
    private String image;
    private double amount;			// Amount 
    private String category;
    private double tds;			// Tds
    public transient InventoryStatus inventoryStatus;
    private double rating;
    private double orgRating ;//
    private String reason;    
    public boolean nonIncome ;
    private String country ;
    private double taxAmount ;
    private String section ;
    

    public void printRow(Writer w)
		{
   	 	System.out.println("id,code,name,description,amount,category,tds,rating,section,nonIncome,country,taxAmount,reason");         
		}

    public void print(Writer w)
    	{
   	 System.out.println("\n" + id + "," + code +"," + name + "," +  description + "," + amount
			 	+ "," + category + "," +  tds + "," + rating + "," + section + 
			 		"," +  nonIncome + "," +  country + "," +  taxAmount + "" + reason);             	
    	}
    
    public void show()
    	{
    	 System.out.print("\nid = " + id + ",code =" + code +",name =" + name + ",description =" +  description + ",amount = " + amount
    			 	+ ",category =" + category + ",tds =" +  tds + ",rating =" + rating + "section =" + section + 
    			 		",nonIncome =" +  nonIncome + ",country =" +  country + ",taxAmount =" +  taxAmount + ",reason =" + reason);         
    	}
    
    
    public TaxPosition() {

    	this.id = 0;
        this.name = "NA";
        this.description = "";
        this.amount = 0.0;
        this.category = "NON-TAXABLE";
        this.tds = 0.0;
        this.rating = 0.0;
        this.section = "NA" ;
        this.nonIncome = false;
        this.country = "NA";
        this.taxAmount = 0.0;
        this.reason = "";
    	
    	}

    public TaxPosition(int id, String code, String name, String description, String image, double amount, String category, double tds,
            InventoryStatus inventoryStatus, double rating) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.image = image;
        this.amount = amount;
        this.category = category;
        this.tds = tds;
        this.inventoryStatus = inventoryStatus;
        this.rating = rating;        
    }
    
    
    
    public TaxPosition(int id, String code, String name, String description, String image, double amount, String category, double tds,
            InventoryStatus inventoryStatus, double rating, String section, boolean nonIncome, String country, double taxAmount,
            String reason) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.image = image;
        this.amount = amount;
        this.category = category;
        this.tds = tds;
        this.inventoryStatus = inventoryStatus;
        this.rating = rating;
        this.section = section ;
        this.nonIncome = nonIncome;
        this.country = country;
        this.taxAmount = taxAmount;
        this.reason = reason;
        
    }

    @Override
    public TaxPosition clone() {
        return new TaxPosition(getId(), getCode(), getName(), getDescription(), getImage(), getAmount(), getCategory(), getTds(),
                getInventoryStatus(), getRating(), getSection(), isNonIncome(), getCountry(), getTaxAmount(),getReason() );
    }

    public int getId() {
    	
    	System.out.print("\nREAD-ID" + id);
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
    	System.out.print("\nREAD-CODE" + code);
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return amount;
    }

    public void setPrice(double price) {
        this.amount = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setQuantity(int quantity) {
        this.tds = quantity;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TaxPosition other = (TaxPosition) obj;
        if (code == null) {
            return other.code == null;
        }
        else {
            return code.equals(other.code);
        }
    }

	public boolean isNonIncome() {
		return nonIncome;
	}

	public void setNonIncome(boolean nonIncome) {
		this.nonIncome = nonIncome;
	}

	public String getCountry() {

		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public double getTds() {
		return tds;
	}


	public void setTds(double tds) {
		this.tds = tds;
	}


	public double getOrgRating() {
		return orgRating;
	}


	public void setOrgRating(double orgRating) {
		this.orgRating = orgRating;
	}


	public int getProjectId() {
		return projectId;
	}


	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
    
    
    
    
    
    
    

}