package com.uibinder.server.expensiveOperation;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.uibinder.server.SiaProxy;
import com.uibinder.shared.SiaResultSubjects;

public class AnalyseCareerExpensiveOperation implements DeferredTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("AnalyseCareer");
	private final String careerCode;
	
	public AnalyseCareerExpensiveOperation(String careerCode){
		this.careerCode = careerCode;
	}
	
	public void run() {
		if(careerCode != null || careerCode.isEmpty() == false){
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

}
