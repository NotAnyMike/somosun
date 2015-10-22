package com.somosun.plan.server.expensiveOperation;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.somosun.plan.server.dao.SubjectDao;
import com.somosun.plan.server.expensiveOperation.Codes.SubjectExpensiveOperationsCodes;

public class SubjectExpensiveOperations implements DeferredTask {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("subjectExpensiveOperations");
	private final String action;

	public SubjectExpensiveOperations(String action){
		this.action = SubjectExpensiveOperationsCodes.getValidValue(action);
	}
	
	@Override
	public void run() {
		if(action != SubjectExpensiveOperationsCodes.DELETE_ALL){
			log.info("Delete all subjects method started");
			SubjectDao subjectDao = new SubjectDao();
			subjectDao.deleteAllSubjects();
			log.info("All subjects were deleted");
		}
	}

}
