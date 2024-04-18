package com.anemoi.recon;

import java.sql.Timestamp;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.anemoi.data.Authenticator;
import com.anemoi.data.HibernateReader;
import com.anemoi.data.HibernateWriter;
import com.anemoi.data.Proj;
import com.anemoi.msg.Emailer;
import com.anemoi.security.Infosec;
import com.anemoi.tqm.TaxView;

@Component("father")
@ManagedBean
@SessionScope
public class LoginView {

	private int 	pid;
	private String 	pass;
	private String 	email;
	private boolean logged;

	private long 	otpGenAt = 0;	
	private Proj 	myProject;
	private String 	storedOtp;
	private String 	otp;



	@Autowired
	private ApplicationContext context;

	@Autowired
	DataView dview;
	@Autowired
	NameView nview;
	@Autowired
	ReconView rview;
	@Autowired
	Preview preview;
	@Autowired
	TaxView taxView;
	@Autowired
	GenerateView gView;
	@Autowired
	Infosec infosec;
	@Autowired
	Authenticator auth;

	@Autowired
	Emailer email_service;

	int attempts = 0;
	boolean admin = false;

	public void saveStatus() {
		HibernateWriter hw = new HibernateWriter(0);
		hw.storeProject(this.myProject);
	}

	public void destroySession() {
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext) context;
		SingletonBeanRegistry beanRegistry = configContext.getBeanFactory();
		BeanDefinitionRegistry factory = (BeanDefinitionRegistry) context.getAutowireCapableBeanFactory();

//		factory.removeBeanDefinition("dview");
	}

	public void logout() {
		pid = 0;
		pass = "";
		admin = false;
		logged = false;
		myProject = null;

		dview.reset();
		nview.resest();
		rview.reset();
		preview.reset();
		taxView.reset();
		gView.reset();

		destroySession();
		}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public DataView getDview() {
		return dview;
	}

	public void setDview(DataView dview) {
		this.dview = dview;
	}

	public String sendotp() {
		
		if (!this.infosec.factor2Active()) {
			return "otp.xhtml";
			}

		if (!validatesEmails(this.email)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("ERR423523: Authentication failed because of wrong email id."));
			return "login.xhtml";
		}

		String otp = infosec.generateOTP();
		this.otpGenAt = System.currentTimeMillis();

		this.storedOtp = otp;
		asyncSendPassword(this.email, otp);

		return "otp.xhtml";
	}

	public void asyncSendPassword(String ee, String oo) {
		email_service.sendOtp(ee, oo);
	}

	public String save() {

		HibernateReader hr = new HibernateReader(this.pid);
		HibernateWriter hw = new HibernateWriter(this.pid);

		
		if (this.infosec.factor2Active()) {
			if (!validatesEmails(this.email)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("ERR423523: Authentication failed because of wrong email id."));
				return "login.xhtml";
			}

			if (!this.otp.equals(this.storedOtp)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						"ERR593203: Authentication failed, Identity detection has failed. - Wrong OTP"));
				return "login.xhtml";
			}

			if (!infosec.isValidOtpTimings(this.otpGenAt)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("ERR593203: OTP timeout. Kindly login again"));
				return "login.xhtml";
			}
		}

		if (this.accountLocked(this.pid)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("ERR593203: Account is locked Kindly login after some time"));
			return "login.xhtml";
		}

		attempts = hw.increamentProjectAttempts();

		if (this.attempts > infosec.maxLoginAttempts()) {
			Timestamp tt = infosec.getProjectLockTime();

			hw.lockProject(this.pid, tt);
			this.attempts = 0;
			System.out.print("\nProject lock set for pid " + this.pid + "\t time=" + tt);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					"Account will be locked till" + tt.toString() + ", try login actvity after " + tt.toString()));
			return "login.xhtml";
		}

		System.out.print("\nLogin object :: " + pass);

		if (this.pid == 0) {

			if (!infosec.validateMasterEmail(this.email)) {
				System.out.print("\nMaster authentication is failed for Admin: wrong email-" + this.email);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						"Master authentication is failed for Admin:wrong email Attempt:" + this.attempts));
				return "login.xhtml";
			}

			if (!infosec.validateMaster(pass)) {
				System.out.print("\nMaster authentication is failed for Admin: wrong password-" + pass);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						"Master authentication is failed for Admin:wrong pass  Attempt:" + this.attempts));
				return "login.xhtml";
			}

			this.admin = true;
			
			
			dview.setProjectId(0);
			dview.setAdminView(true);
			dview.loadData();
			logged = true;
			
			hw.userLoggedIn();
			myProject = hr.specificProject(0);

			dview.reset();
			nview.reset();
			rview.reset();
			preview.reset();
			taxView.reset();
			gView.reset();

			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome Admin"));
			return "rdata.xhtml";
		}

		if (!authenticatePid(this.pid, this.pass)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Not valid credentials :: " + pid));
			return "login.xhtml";
		}
		
		hw.userLoggedIn();
		myProject = hr.specificProject(pid);
		
		
		
		dview.setProjectId(this.pid);
		dview.loadData();

		logged = true;

		dview.setProjectId(this.pid);
		;
		nview.setProjectId(this.pid);
		rview.setProjectId(this.pid);
		preview.setProjectId(this.pid);
		taxView.setProjectId(this.pid);
		gView.setProjectId(this.pid);

		dview.reset();
		nview.reset();
		rview.reset();
		preview.reset();
		taxView.reset();
		gView.reset();

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome " + pid));

		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot viewRoot = facesContext.getViewRoot();
		viewRoot.setLocale(new Locale("en", "IN"));
//		viewRoot.setLocale(Locale.US);		
		return getPage(myProject.getStage());
	}

	public String getPage(int stage) {
		if (stage == 0)
			return "rdata.xhtml";
		if (stage == 1)
			return "rnames.xhtml";
		if (stage == 2)
			return "rboard6.xhtml";
		if (stage == 3)
			return "preview.xhtml";
		if (stage == 4)
			return "tboard3.xhtml";
		if (stage == 5)
			return "gboard2.xhtml";

		return "login.xhtml";
	}

	public boolean authenticatePid(int pid, String pass) {
		return auth.authenticate(pid, pass, " ", 1);
	}

	public boolean accountLocked(int pid) {
		return auth.isLocked(pid);
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public Authenticator getAuth() {
		return auth;
	}

	public void setAuth(Authenticator auth) {
		this.auth = auth;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public Proj getMyProject() {
		return myProject;
	}

	public void setMyProject(Proj myProject) {
		this.myProject = myProject;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean validatesEmails(String str) {

		String regexPattern = Infosec.VUEP;

		str = str.trim();
		boolean check = Pattern.compile(regexPattern).matcher(str).matches();

		if (!check) {
			System.out.println("ERR430594: Invalid Emai:" + str);
			return false;
		}
		return true;
	}

	public String getStoredOtp() {
		return storedOtp;
	}

	public void setStoredOtp(String storedOtp) {
		this.storedOtp = storedOtp;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
