package com.uibinder.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.client.admin.service.AdminService;
import com.uibinder.server.SiaProxy;
import com.uibinder.server.dao.CareerDao;
import com.uibinder.server.dao.MessageDao;
import com.uibinder.server.dao.PlanDao;
import com.uibinder.server.dao.SemesterDao;
import com.uibinder.server.dao.StudentDao;
import com.uibinder.server.dao.SubjectGroupDao;
import com.uibinder.server.expensiveOperation.AnalyseAllCareersExpensiveOperation;
import com.uibinder.server.expensiveOperation.AnalyseCareerExpensiveOperation;
import com.uibinder.server.expensiveOperation.ComplementaryValueExpensiveOperations;
import com.uibinder.server.expensiveOperation.SubjectExpensiveOperations;
import com.uibinder.server.expensiveOperation.SubjectValueExpensiveOperations;
import com.uibinder.server.expensiveOperation.Codes.ComplementaryValueExpensiveOperationsCodes;
import com.uibinder.server.expensiveOperation.Codes.SubjectExpensiveOperationsCodes;
import com.uibinder.server.expensiveOperation.Codes.SubjectValueExpensiveOperationsCodes;
import com.uibinder.shared.control.Message;
import com.uibinder.shared.control.Plan;
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
			Queue q = QueueFactory.getQueue("adminQueue");
			q.add(TaskOptions.Builder.withPayload(new ComplementaryValueExpensiveOperations(ComplementaryValueExpensiveOperationsCodes.DELETE_ALL)));
		}
	}

	/**
	 * Admin method
	 */
	public void deleteCertainComplementaryValues(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("adminQueue");
			q.add(TaskOptions.Builder.withPayload(new ComplementaryValueExpensiveOperations(ComplementaryValueExpensiveOperationsCodes.DELETE_FOR_CAREER, careerCode)));
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSubjects() {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("adminQueue");
			q.add(TaskOptions.Builder.withPayload(new SubjectExpensiveOperations(SubjectExpensiveOperationsCodes.DELETE_ALL)));
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
			Queue q = QueueFactory.getQueue("adminQueue");
			q.add(TaskOptions.Builder.withPayload(new SubjectValueExpensiveOperations(SubjectValueExpensiveOperationsCodes.DELETE_ALL)));
		}
	}

	/**
	 * Admin method
	 */
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

	/**
	 * Admin method
	 */
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

	/**
	 * Admin method
	 */
	public void analyseAllCareers(boolean analyseAll) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("analyseAllCareers");
			q.add(TaskOptions.Builder.withPayload(new AnalyseAllCareersExpensiveOperation(analyseAll)));
			//q.add(TaskOptions.Builder.withUrl("").param("", "").header("Host", ModulesServiceFactory.getModulesService().getInstanceHostname("admin", null, 1)));
		}
		
	}
	
	@Override
	public void analyseCareer(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("analyseCareer");
			q.add(TaskOptions.Builder.withPayload(new AnalyseCareerExpensiveOperation(careerCode)));
		}
	}

	/**
	 * Admin method
	 */
	public List<Message> getAllMessages() {
		List<Message> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){			
			toReturn = new ArrayList<Message>();
			MessageDao messageDao = new MessageDao();
			for(Message m : messageDao.getAllMessages()){
				toReturn.add(m);
			}
			
		}
		return toReturn;
	}

	/**
	 * Admin method
	 */
	public List<Message> getAllErrorMessages() {
		List<Message> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){			
			toReturn = new ArrayList<Message>();
			MessageDao messageDao = new MessageDao();
			for(Message m : messageDao.getAllErrorMessages()){
				toReturn.add(m);
			}
			
		}
		return toReturn;
	}

	/**
	 * Admin method
	 */
	public List<Message> getAllSuggestionMessages() {
		List<Message> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){			
			toReturn = new ArrayList<Message>();
			MessageDao messageDao = new MessageDao();
			for(Message m : messageDao.getAllSuggestionMessages()){
				toReturn.add(m);
			}
			
		}
		return toReturn;
	}	
	
	/**
	 * Admin method
	 */
	public List<Message> getAllOtherMessages() {
		List<Message> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){			
			toReturn = new ArrayList<Message>();
			MessageDao messageDao = new MessageDao();
			for(Message m : messageDao.getAllOtherMessages()){
				toReturn.add(m);
			}
			
		}
		return toReturn;
	}
	
	/**
	 * Admin method
	 */
	public List<Message> getUserMessages(String username) {
		List<Message> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){			
			toReturn = new ArrayList<Message>();
			MessageDao messageDao = new MessageDao();
			for(Message m : messageDao.getUserMessages(username)){
				toReturn.add(m);
			}
			
		}
		return toReturn;
	}

	/**
	 * Admin method
	 */
	public Message getMessageById(Long id) {
		Message toReturn = null;
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			toReturn = messageDao.getMessageById(id);
		}
		return toReturn;
	}

	/**
	 * Admin method
	 */
	public void deleteAllMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<Message> list = messageDao.getAllMessages();
			for(Message m : list){
				messageDao.deleteMessage(m.getId());
			}
		}
	}	

	/**
	 * Admin method
	 */
	public void deleteSuggestionMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<Message> list = messageDao.getAllSuggestionMessages();
			for(Message m : list){
				messageDao.deleteMessage(m.getId());
			}
		}
	}
	
	/**
	 * Admin method
	 */
	public void deleteErrorMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<Message> list = messageDao.getAllErrorMessages();
			for(Message m : list){
				messageDao.deleteMessage(m.getId());
			}
		}
	}
	
	/**
	 * Admin method
	 */
	public void deleteOtherMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<Message> list = messageDao.getAllOtherMessages();
			for(Message m : list){
				messageDao.deleteMessage(m.getId());
			}
		}
	}

	/**
	 * Admin method
	 */
	public List<Plan> getPlansByUser(String username) {
		List<Plan> toReturn = null;
		
		if(getUserLogged().isAdmin() == true){
			toReturn = new ArrayList<Plan>();
			PlanDao planDao = new PlanDao();
			for(Plan p : planDao.getPlansByUsername(username)){
				toReturn.add(p);
			}
		}
		return toReturn;
	}

	@Override
	public Plan getPlanById(Long id) {
		Plan plan = null;
		if(getUserLogged().isAdmin() == true){			
			PlanDao planDao = new PlanDao();
			plan = planDao.getPlanById(Long.valueOf(id));
		}
		return plan;
	}
	
}
