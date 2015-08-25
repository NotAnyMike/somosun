package com.somosun.plan.server.expensiveOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.control.Career;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * This method cannot be use from the default module, this is intended to be used only in the admin module 
 * @author MW
 *
 */
public class AnalyseAllCareersExpensiveOperation{

	private final static Logger log = Logger.getLogger("AnalyseAllCareersExpensiveOperation");
	
	public static void run(boolean includeAnalyzed) {
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
					log.warning("<------------- ERROR with " + career.getCode() + " " + career.getName() + " --------------->");
					careersNotAnalysed.add(career.getCode());
				}
				careersAnalysed.add(career.getCode());
				log.info("Analysis for " + career.getCode() + " " + career.getName() + " ended");
			}else{
				log.info("Analysis for " + career.getCode() + " " + career.getName() + " canceled because it has been analysed already");
			}
			
			ofy().clear();
			
		}
		
		log.warning("Careers not analyzed: " + careersNotAnalysed.toString());
		log.info("<------------- ANALYSE ALL CAREERS ENDED ------------->");
	}

}
