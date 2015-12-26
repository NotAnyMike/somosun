package com.somosun.plan.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.somosun.plan.client.admin.service.AdminService;
import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.control.MessageServer;
import com.somosun.plan.server.control.PlanServer;
import com.somosun.plan.server.dao.BlockDao;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.server.dao.GroupDao;
import com.somosun.plan.server.dao.MessageDao;
import com.somosun.plan.server.dao.PlanDao;
import com.somosun.plan.server.dao.SemesterDao;
import com.somosun.plan.server.dao.StudentDao;
import com.somosun.plan.server.dao.SubjectGroupDao;
import com.somosun.plan.server.expensiveOperation.AnalyseAllCareersExpensiveOperation;
import com.somosun.plan.server.expensiveOperation.AnalyseCareerExpensiveOperation;
import com.somosun.plan.server.expensiveOperation.ComplementaryValueExpensiveOperations;
import com.somosun.plan.server.expensiveOperation.SubjectExpensiveOperations;
import com.somosun.plan.server.expensiveOperation.SubjectValueExpensiveOperations;
import com.somosun.plan.server.expensiveOperation.Codes.ComplementaryValueExpensiveOperationsCodes;
import com.somosun.plan.server.expensiveOperation.Codes.SubjectExpensiveOperationsCodes;
import com.somosun.plan.server.expensiveOperation.Codes.SubjectValueExpensiveOperationsCodes;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Student;

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
			String hostName = ModulesServiceFactory.getModulesService().getVersionHostname("admin-module", "1");
			q.add(TaskOptions.Builder.withPayload(new ComplementaryValueExpensiveOperations(ComplementaryValueExpensiveOperationsCodes.DELETE_ALL)).header("Host",hostName));
		}
	}

	/**
	 * Admin method
	 */
	public void deleteCertainComplementaryValues(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("adminQueue");
			String hostName = ModulesServiceFactory.getModulesService().getVersionHostname("admin-module", "1");
			q.add(TaskOptions.Builder.withPayload(new ComplementaryValueExpensiveOperations(ComplementaryValueExpensiveOperationsCodes.DELETE_FOR_CAREER, careerCode)).header("Host",hostName));
		}
	}

	/**
	 * Admin method
	 */
	public void deleteAllSubjects() {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("adminQueue");
			String hostName = ModulesServiceFactory.getModulesService().getVersionHostname("admin-module", "1");
			q.add(TaskOptions.Builder.withPayload(new SubjectExpensiveOperations(SubjectExpensiveOperationsCodes.DELETE_ALL)).header("Host",hostName));
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
	public void certainHasDefaultField(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			CareerDao careerDao = new CareerDao();
			Career c = careerDao.getByCode(careerCode);
			c.setHasDefault(true);
			careerDao.save(c);
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
				studentDao.save(s);
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
				studentDao.save(s);
			}
		}
	}

	/**
	 * Admin method
	 */
	public void analyseAllCareers(boolean analyseAll) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("analyseAllCareers");
			String hostName = ModulesServiceFactory.getModulesService().getVersionHostname("admin-module", "1");
			q.add(TaskOptions.Builder.withUrl("/admin/analyse-all-careers").param("allCareers", (analyseAll ? "true" : "false")).header("Host",hostName));
			log.info("Task to analyse all careers was added to the queue, analysing all: " + analyseAll);
		}
		
	}
	
	@Override
	public void analyseCareer(String careerCode) {
		if(getUserLogged().isAdmin() == true){
			Queue q = QueueFactory.getQueue("analyseCareer");
			q.add(TaskOptions.Builder.withPayload(new AnalyseCareerExpensiveOperation(careerCode)));
			/************ uncomment this to test locally if there is any error with the time out, this task queue has a 10min limit  ************/
			/******* if the server has the same error (10 min limit reached) then it has to apply the same solution as AnalayseAllCareers *******/
//			if(careerCode != null || careerCode.isEmpty() == false){
//				String sede = "bog";
//				
//				log.info("<------------- STARTING TO ANALYSE THE CAREER WITH NO DEFERRED WORK ------------->");
//				log.info("Getting all results from the sia");
//				
//				SiaResultSubjects allSiaSubjects = SiaProxy.getSubjects("", "", "", "", 1, 10000, sede, null);
//				
//				
//					
//				log.info("Starting to analyse the career " + careerCode);
//									
//				boolean error = false;
//				try{
//					SiaProxy.getRequisitesForACareer(careerCode, allSiaSubjects);
//				}catch (Exception e){
//					log.info("<------------- ERROR with " + careerCode + " --------------->");
//					log.warning("Message: " + e.getMessage());
//					log.warning("Cause: " + e.getCause());
//					e.printStackTrace();
//				}
//					
//
//				log.info("<------------- ANALYSE ENDED ------------->");
//			}
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
			for(MessageServer m : messageDao.getAllMessages()){
				toReturn.add(m.getShared());
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
			for(MessageServer m : messageDao.getAllErrorMessages()){
				toReturn.add(m.getShared());
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
			for(MessageServer m : messageDao.getAllSuggestionMessages()){
				toReturn.add(m.getShared());
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
			for(MessageServer m : messageDao.getAllOtherMessages()){
				toReturn.add(m.getShared());
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
			for(MessageServer m : messageDao.getUserMessages(username)){
				toReturn.add(m.getShared());
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
			toReturn = messageDao.getById(id).getShared();
		}
		return toReturn;
	}

	/**
	 * Admin method
	 */
	public void deleteAllMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<MessageServer> list = messageDao.getAllMessages();
			for(MessageServer m : list){
				messageDao.delete(m.getId());
			}
		}
	}	

	/**
	 * Admin method
	 */
	public void deleteSuggestionMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<MessageServer> list = messageDao.getAllSuggestionMessages();
			for(MessageServer m : list){
				messageDao.delete(m.getId());
			}
		}
	}
	
	/**
	 * Admin method
	 */
	public void deleteErrorMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<MessageServer> list = messageDao.getAllErrorMessages();
			for(MessageServer m : list){
				messageDao.delete(m.getId());
			}
		}
	}
	
	/**
	 * Admin method
	 */
	public void deleteOtherMessages() {
		if(getUserLogged().isAdmin() == true){
			MessageDao messageDao = new MessageDao();
			List<MessageServer> list = messageDao.getAllOtherMessages();
			for(MessageServer m : list){
				messageDao.delete(m.getId());
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
			for(PlanServer p : planDao.getPlansByUsername(username)){
				toReturn.add(p.getClientInstance());
			}
		}
		return toReturn;
	}

	@Override
	public Plan getPlanById(Long id) {
		Plan plan = null;
		if(getUserLogged().isAdmin() == true){			
			PlanDao planDao = new PlanDao();
			PlanServer planServer = planDao.getById(Long.valueOf(id));
			if(planServer != null) plan = planServer.getClientInstance(); 
		}
		return plan;
	}

	@Override
	public void deleteAllBlocks() {
		if(getUserLogged().isAdmin() == true){			
			BlockDao blockDao = new BlockDao();
			blockDao.deleteAll();
		}
	}

	@Override
	public void deleteAllGroups() {
		log.warning("Starting to delete all groups");
		if(getUserLogged().isAdmin() == true){			
			GroupDao groupDao = new GroupDao();
			groupDao.deleteAll();
		}
		log.warning("All groups were deleted");
	}
	
}
