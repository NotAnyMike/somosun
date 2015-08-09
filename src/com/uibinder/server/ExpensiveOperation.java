package com.uibinder.server;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.uibinder.server.serviceImpl.AdminServiceImpl;
import com.uibinder.shared.SiaResultSubjects;

public class ExpensiveOperation implements DeferredTask {

	final private static Logger log = Logger.getLogger("ExpensiveOperation");
	
	@Override
	public void run() {
		analyseCareer("2520");
	}

	public void analyseCareer(String careerCode) {
		String sede = "bog";
		
		log.info("<------------- STARTING TO ANALYSE THE CAREER ------------->");
		log.info("Getting all results from the sia");
		
		SiaResultSubjects allSiaSubjects = SiaProxy.getSubjects("", "", "", "", 1, 10000, sede, null);
		
		
			
		log.info("Starting to analyse the career " + careerCode);
							
		boolean error = false;
		try{
			SiaProxy.getRequisitesForACareer(careerCode, allSiaSubjects);
		}catch (Exception e){
			error = true;
		}
		
		if(error){
			log.info("<------------- ERROR with " + careerCode + " --------------->");
		}	

		log.info("<------------- ANALYSE ENDED ------------->");
	}
	
}
