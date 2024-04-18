/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anemoi.tax.reconciliation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.anemoi.util.Util;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = { "com.anemoi" })
@EnableScheduling
@EnableAsync
public class Main {

	public static void main(String[] args) throws Exception {
		Util.load();
		SpringApplication.run(Main.class, args);
	}

	@Bean
	ServletRegistrationBean jsfServletRegistration(ServletContext servletContext) {
		
		// spring boot only works if this is set
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

		// servletContext.setInitParameter("primefaces.THEME", "bluesky");
		// servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
		// servletContext.setInitParameter("com.sun.faces.expressionFactory","com.sun.el.ExpressionFactoryImpl");
		servletContext.setInitParameter("primefaces.UPLOADER", "commons");

		// registration
		ServletRegistrationBean srb = new ServletRegistrationBean();
		srb.setServlet(new FacesServlet());
		srb.setUrlMappings(Arrays.asList("*.xhtml"));
		srb.setLoadOnStartup(1);
		return srb;
		}

	
	
	@Bean
	public FilterRegistrationBean FileUploadFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();		
		registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
		registration.setName("PrimeFaces FileUpload Filter");				
		return registration;
	}
	
	

	@Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
	
	
	
/*
	@Bean
	public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(){
	    
		FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean  = new FilterRegistrationBean<>();	        
	    registrationBean.setFilter(new RequestResponseLoggingFilter());
	    registrationBean.addUrlPatterns("/*");
	    registrationBean.setOrder(2);	        
	    return registrationBean;    
	}
*/	
	
	
	
}