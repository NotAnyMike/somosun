package com.uibinder.server.serviceImpl;

import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.client.admin.service.AdminService;
import com.uibinder.server.SiaProxy;
import com.uibinder.server.dao.PlanDao;
import com.uibinder.shared.control.Student;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("AdminServiceImpl");
	
	@Override
	public void resetCareer() {
		SiaProxy.updateCareersFromSia("bog");
	}

	/**
	 * Admin method
	 */
	public void deleteAllDefaultPlans() {
		if(getUserLogged().isAdmin() == true){
			PlanDao planDao = new PlanDao();
			planDao.deleteAllDefaultPlans();
		}
	}
	
	private Student getUserLogged(){
		LoginServiceImpl login = new LoginServiceImpl();
		Student student = login.login(null).getStudent();
		return student;
	}

	/**
	 * Admin method
	 */
	public void deleteDefaultPlan(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			PlanDao planDao = new PlanDao();
			planDao.deleteDefaultPlan(careerCode);
		}
	}

}
