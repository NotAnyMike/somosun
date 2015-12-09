package com.somosun.plan.server.expensiveOperation;

import java.util.logging.Logger;

import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.control.Subject;

public class GetAllGroupsExpensiveOperation {

	private final static Logger log = Logger.getLogger("GetAllGroupsExpensiveOperation");
	
	public static void run(){
		
		/**
		 * 1. Get all the subjects for all careers
		 * 2. For each subject get its groups
		 */
		
		log.info("<------ Starting to get all the groups ------>");
		log.info("Getting all subjects");
		SiaResultSubjects subjects = SiaProxy.getSubjects("", "", "", "", 1, 1000, "bog");
		log.info("All subjects recived, total: " + subjects.getTotalAsignaturas());
		
		int subjectNumber = 0;
		int groupNumber = 0;
		int error = 0;
		int subjectsSkiped = 0;
		for(int position = 0; position < subjects.getSubjectList().size(); position ++){
			Subject s = subjects.getSubjectList().get(position);
			try{
				log.info("Getting groups for subject " + ++subjectNumber + " " + s.getName() + " (" + s.getCode() + ")");
				SiaResultGroups groups = SiaProxy.getGroupsFromSubject(s, "bog");
				groupNumber += groups.getGroups().size();
				log.info("Subject has " + groups.getGroups().size() + "groups, total groups gathered " + groupNumber);
			}catch (Exception e){
				log.warning("ERROR, 30s delay");
				try {
					error++;
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(error > 5){
					subjectsSkiped ++;	
					error = 0;
					log.warning("Skipping subject");
				}else{
					position --;
					subjectNumber --;
				}
			}
			
		}
	
	}
	
}
