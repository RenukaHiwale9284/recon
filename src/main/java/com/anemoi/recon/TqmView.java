package com.anemoi.recon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.HibernateWriter;

@Component("tview")
@ManagedBean
@SessionScope
public class TqmView {	
	int projectId;

	private Recon recon;

	@Autowired
	private ReconService reconService;

	@Autowired
	private ReferenceDataUtiity ref;


	public void init(int pid) {
		this.projectId = pid;
	}

//	@PostConstruct
	public void init() {

	}

	
//	Lifecycle Methods
	public void reset() {

	}

	public void validate() {

	}

	public void approve() {
		// Saving all the contents ;
		System.out.print("\nStoing the View :::::::::::: ");
		HibernateWriter hw = new HibernateWriter(this.projectId);

		hw.storeIncomeAmount(this.recon.getOriginal(), this.recon.getCarryForward());
	}

	public String proceed() {

		return "tview.xhtml";
	}


}
