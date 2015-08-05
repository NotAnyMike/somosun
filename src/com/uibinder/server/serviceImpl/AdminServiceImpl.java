package com.uibinder.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.client.admin.service.AdminService;
import com.uibinder.server.SiaProxy;
import com.uibinder.server.dao.CareerDao;
import com.uibinder.server.dao.ComplementaryValueDao;
import com.uibinder.server.dao.PlanDao;
import com.uibinder.server.dao.SemesterDao;
import com.uibinder.server.dao.StudentDao;
import com.uibinder.server.dao.SubjectDao;
import com.uibinder.server.dao.SubjectGroupDao;
import com.uibinder.server.dao.SubjectValueDao;
import com.uibinder.shared.SiaResultSubjects;
import com.uibinder.shared.control.Career;
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
		Student student = login.login("").getStudent();
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

	/**
	 * Admin method
	 */
	public void resetAllHasAnalysis() {
		if(getUserLogged().isAdmin() == true){
			CareerDao careerDao = new CareerDao();
			careerDao.resetAllHasAnalysis();
		}
	}

	/**
	 * Admin method
	 */
	public void resetCertainHasAnalysis(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			CareerDao careerDao = new CareerDao();
			careerDao.resetCertainHasAnalysis(careerCode);
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllPlans() {
		if(getUserLogged().isAdmin() == true){
			PlanDao planDao = new PlanDao();
			planDao.deleteAllPlans();
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllComplementaryValues() {
		if(getUserLogged().isAdmin() == true){
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			cVDao.deleteAllComplementeryValues();
		}
	}

	/**
	 * Admin method
	 */
	public void deleteCertainComplementaryValues(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			cVDao.deleteCertainComplementeryValues(careerCode);
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSubjects() {
		if(getUserLogged().isAdmin() == true){
			SubjectDao subjectDao = new SubjectDao();
			subjectDao.deleteAllSubjects();
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSubjectGroup() {
		if(getUserLogged().isAdmin() == true){
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			subjectGroupDao.deleteAllSubjectGroups();
		}
	}

	/**
	 * Admin method
	 */
	public void resetAllHasDefaultField() {
		if(getUserLogged().isAdmin() == true){
			CareerDao careerDao = new CareerDao();
			careerDao.resetAllHasDefault();
		}
	}

	/**
	 * Admin method
	 */
	public void resetCertainHasDefaultField(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			CareerDao careerDao = new CareerDao();
			careerDao.resetHasDefaultForCareer(careerCode);
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSemesters() {
		if(getUserLogged().isAdmin() == true){
			SemesterDao semesterDao = new SemesterDao();
			semesterDao.deleteAllSemesters();
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSubjectValue() {
		if(getUserLogged().isAdmin() == true){
			SubjectValueDao sVDao = new SubjectValueDao();
			sVDao.deleteAllSubjectValues();
		}
	}

	@Override
	public void makeUserAdmin(String userName) {
		if(getUserLogged().isAdmin() == true){			
			StudentDao studentDao = new StudentDao();
			Student s = studentDao.getStudentByUserName(userName);
			if(s!= null){			
				s.setAdmin(true);
				studentDao.saveStudent(s);
			}
		}
	}

	@Override
	public void blockUnblockUser(String userName) {
		if(getUserLogged().isAdmin() == true){			
			StudentDao studentDao = new StudentDao();
			Student s = studentDao.getStudentByUserName(userName);
			if(s!= null){						
				s.setBlocked(!s.isBlocked());
				studentDao.saveStudent(s);
			}
		}
	}

	@Override
	public void analyseAllCareers(boolean analyseAll) {
		if(getUserLogged().isAdmin() == true){
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
				if(!career.hasAnalysis() || analyseAll){					
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

}
