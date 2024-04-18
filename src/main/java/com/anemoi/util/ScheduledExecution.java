package com.anemoi.util;

import java.util.ArrayList;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.anemoi.data.HibernateWriter;
import com.anemoi.data.Trail;

@Component
public class ScheduledExecution {
	
    @Scheduled(fixedDelay =5000 , initialDelay = 10000)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
 
//    	System.out.println( "Logger Task async - " + System.currentTimeMillis() / 1000);
        
        ArrayList<Trail> logs = Util.pullLogs() ;
        HibernateWriter hw = new HibernateWriter(0);
        
        hw.store(logs, "Trail");
        
        Thread.sleep(1000);
    }

}