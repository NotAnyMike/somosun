package com.uibinder.server.expensiveOperation;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.uibinder.server.dao.ComplementaryValueDao;
import com.uibinder.server.expensiveOperation.Codes.ComplementaryValueExpensiveOperationsCodes;

public class ComplementaryValueExpensiveOperations implements DeferredTask {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger log = Logger.getLogger("ComplementaryValueExpensiveOperations");
	private final String action;
	private final String careerCode;
	
	public ComplementaryValueExpensiveOperations(String action){
		this.action = ComplementaryValueExpensiveOperationsCodes.getValidValue(action);
		careerCode = null;
	}
	
	public ComplementaryValueExpensiveOperations(String action, String careerCode){
		this.action = ComplementaryValueExpensiveOperationsCodes.getValidValue(action);
		this.careerCode = careerCode;
	} 

	public void run() {
		if(action != ComplementaryValueExpensiveOperationsCodes.DELETE_ALL){
			
			log.info("Start to delete all ComplementaryValues");
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			cVDao.deleteAllComplementeryValues();
			log.info("All the ComplementaryValues were deleted");
			
		}else if(action != ComplementaryValueExpensiveOperationsCodes.DELETE_FOR_CAREER && careerCode != null){
			
			log.info("Start to delete all ComplementaryValues for " + careerCode);
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			cVDao.deleteCertainComplementeryValues(careerCode);
			log.info("All the ComplementaryValues of career " + careerCode + " were deleted");
		
		}
	}

}
