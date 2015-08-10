package com.uibinder.server.expensiveOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.uibinder.server.SiaProxy;
import com.uibinder.server.dao.CareerDao;
import com.uibinder.shared.SiaResultSubjects;
import com.uibinder.shared.control.Career;

public class AnalyseAllCareersExpensiveOperation implements DeferredTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean includeAnalyzed;
	private final static Logger log = Logger.getLogger("AnalyseAllCareersExpensiveOperation");

	public AnalyseAllCareersExpensiveOperation(boolean includeAnalyzed){
		this.includeAnalyzed = includeAnalyzed;
	}
	
	public void run() {
		CareerDao careerDao = new CareerDao();
		List<Career> careers = careerDao.getCareersBySede("bog");
		List<String> careersAnalysed = new ArrayList<String>();
		List<String> careersNotAnalysed = new ArrayList<String>();
		String sede = "bog";
		
		log.info("<------------- STARTING TO ANALYSE ALL CAREERS ------------->");
		log.info("Getting all results from the sia");
		
		SiaResultSubjects allSiaSubjects = SiaProxy.getSubjects("", "", "", "", 1, 10000, sede, null);;
		
		for(Career career : careers){
			
			log.info("Starting to analyse the career " + career.getCode() + " " + career.getName());
			if(!career.hasAnalysis() || includeAnalyzed){					
				boolean error = false;
				try{
					SiaProxy.getRequisitesForACareer(career.getCode(), allSiaSubjects);
				}catch (Exception e){
					error = true;
				}
				
				if(error){
					log.info("<------------- ERROR with " + career.getCode() + " " + career.getName() + " --------------->");
					careersNotAnalysed.add(career.getCode());
				}
				careersAnalysed.add(career.getCode());
				log.info("Analysis for " + career.getCode() + " " + career.getName() + " ended");
			}else{
				log.info("Analysis for " + career.getCode() + " " + career.getName() + " canceled because it has been analysed already");
			}
			
		}
		
		log.info("Careers not analyzed: " + careersNotAnalysed.toString());
		log.info("<------------- ANALYSE ALL CAREERS ENDED ------------->");
	}

}
