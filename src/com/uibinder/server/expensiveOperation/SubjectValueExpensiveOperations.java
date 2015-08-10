package com.uibinder.server.expensiveOperation;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.uibinder.server.dao.SubjectValueDao;
import com.uibinder.server.expensiveOperation.Codes.SubjectValueExpensiveOperationsCodes;

public class SubjectValueExpensiveOperations implements DeferredTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("SubjectValueExpensiveOperations");
	private final String action;
	
	public SubjectValueExpensiveOperations(String action){
		this.action = SubjectValueExpensiveOperationsCodes.getValidValue(action);
	}
	
	public void run() {
		if(action.equals(SubjectValueExpensiveOperationsCodes.DELETE_ALL)){
			
			log.info("Starting to delete al SubjectValues");
			SubjectValueDao sVDao = new SubjectValueDao();
			sVDao.deleteAllSubjectValues();
			log.info("All SubjectValues were deleted");
			
		}
	}

}
