package com.somosun.plan.server.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.SomosUNServerUtils;
import com.somosun.plan.server.control.ComplementaryValueServer;
import com.somosun.plan.server.control.MessageServer;
import com.somosun.plan.server.control.PlanServer;
import com.somosun.plan.server.cronJob.GradeUpdaterCronJob;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.server.dao.ComplementaryValueDao;
import com.somosun.plan.server.dao.MessageDao;
import com.somosun.plan.server.dao.PlanDao;
import com.somosun.plan.server.dao.ScoreDao;
import com.somosun.plan.server.dao.SemesterValueDao;
import com.somosun.plan.server.dao.StudentDao;
import com.somosun.plan.server.dao.SubjectDao;
import com.somosun.plan.server.dao.SubjectGroupDao;
import com.somosun.plan.shared.CompletePlanInfo;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.PlanValuesResult;
import com.somosun.plan.shared.RandomPhrase;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Score;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.SingleScore;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.control.UserSun;
import com.somosun.plan.shared.values.SubjectGroupCodes;
import com.somosun.plan.shared.values.TypologyCodes;

/**
 * 
 * @author Mike
 *
 * This class takes care of implementing the client's RPC services, 
 * Feel free to modify the methods implementation
 * 
 *  
 */
public class SUNServiceImpl extends RemoteServiceServlet implements SUNService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger("SunServiceImpl");


	@Override
	public Subject getSubjectByCode(int code) {
		//Subject subject = new Subject(3,"1231231","Just bullshit");
		return null;
	}

	@Override
	public Subject getSubjectByCode(int code, String career) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubjectByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subject getSubjectByName(String name, String career) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 
	 * It allows the admins to retrieve and store data in & out of the database infrastructure. 
	 * 
	 */
	@Override
	public List<RandomPhrase> getRandomPhrase() {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("RandomPhrase");
		List<RandomPhrase> lista = new LinkedList<RandomPhrase>();
		for(Entity n: datastore.prepare(q).asIterable()){
			String random = (String) n.getProperty("random");
			String author = (String) n.getProperty("author");
			lista.add(new RandomPhrase(random,author));
		}
		return lista;
	}

	@Override
	public void saveRandomPhrase(String phrase, String author) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction tx = datastore.beginTransaction();
		try{
			Entity daPhrase = new Entity("RandomPhrase");
			daPhrase.setProperty("random", phrase);
			daPhrase.setProperty("author", author);
			datastore.put(daPhrase);
			tx.commit();
		}finally{
			if(tx.isActive()){
				tx.rollback();
			}
		}
	}
	
	/**
	 * For more information goto SiaProxy.class
	 * @param subject
	 * @param sede
	 * @return
	 */
	@Override
	public SiaResultGroups getGroupsFromSia(Subject subject, String sede){
		return SiaProxy.getGroupsFromSubject(subject, sede);
	}

	/**
	 * For more information goto SiaProxy.class
	 * @param subjectSiaCode
	 * @param sede
	 * @return
	 */
	@Override
	public SiaResultGroups getGroupsFromSia(String subjectSiaCode, String sede) {
		return SiaProxy.getGroupsFromSubject(subjectSiaCode, sede);
	}

	/**
	 * For more information goto SiaProxy.class
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param student 
	 * @param subjectCodeListToFilter 
	 * @return
	 */
	@Override
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, List<String> subjectCodeListToFilter) {
		return SiaProxy.getSubjects(nameOrCode, typology, career, scheduleCP, page, ammount, sede, student, subjectCodeListToFilter);
	}
	
	/**
	 * For more information goto SiaProxy.class
	 * @param nameOrCode
	 * @param typology
	 * @param career
	 * @param scheduleCP
	 * @param page
	 * @param ammount
	 * @param sede
	 * @param student 
	 * @param subjectCodeList 
	 * @return
	 */
	@Override
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student) {
		return getSubjectFromSia(nameOrCode, typology, career, scheduleCP, page, ammount, sede, student, null);
	}

	@Override
	public List<Career> getCareers(String sede) {
		CareerDao careerDao = new CareerDao();
		List<Career> listFromDb = careerDao.getBySede(sede);
		List<Career> listToReturn = new ArrayList<Career>();
		for(Career c : listFromDb){
			listToReturn.add(c);
		}
		return listToReturn;
	}

	@Override
	public Plan getPlanDefaultFromString(String careerCode) {
		PlanDao planDao = new PlanDao();
		return (Plan) planDao.createPlanFromDefaultString(careerCode).getClientInstance();
	}

	@Override
	public void toTest() {
		//SiaProxy.getRequisitesForACareer("2522");
	}

	@Override
	public List<ComplementaryValue> getComplementaryValueFromMisPlanes(String career, List<String> codes) {
		List<ComplementaryValue> toReturn = new ArrayList<ComplementaryValue>();
		List<ComplementaryValueServer> list = (List<ComplementaryValueServer>) SiaProxy.getRequisitesFromMisPlanes(codes, career);
		for(ComplementaryValueServer cV : list) toReturn.add(cV.getClientInstance());
		return toReturn;
	}
	
	/**
	 * @param nameOrCode the name or the code as a String
	 * @para typology:
	 * 	For all = ""
	 * 	For under-graduate: Nivelación: "P" Fundamentación: "B" Disciplinar: "C" Libre elección: "L"
	 * 	For graduate: Obligatorio: "O" Elegible: "T"
	 * @param career: the String of the code of the career
	 * @param sede: "ama", "bog", "car", "man", "med", "ori", "pal" or  "tum" if nothing it will be taken as bog
	 */
	@Override
	public SiaResultSubjects getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student) {
		return getSubjectFromSia(nameOrCode, typology, career, "", page, 10, sede, student);
	}
	
	@Override
	public List<ComplementaryValue> getComplementaryValuesFromMisPlanes(String careerCode) {
		ComplementaryValueDao cVDao = new ComplementaryValueDao();
		List<ComplementaryValueServer> cVList = cVDao.getComplementaryValues(careerCode);
		List<ComplementaryValue> cVListToReturn = new ArrayList<ComplementaryValue>();
		for(ComplementaryValueServer cV : cVList){
			cVListToReturn.add(cV.getClientInstance());
		}
		return cVListToReturn;
	}

	@Override
	public void savePlanAsDefault(Student student, Plan plan) {
		PlanDao pDao = new PlanDao();
		StudentDao sDao = new StudentDao();
		//Student s = sDao.getStudentByIdSun(student.getIdSun());
		Student s = sDao.getStudentByIdG(student.getIdG());
		Career c = null;
		CareerDao cDao = new CareerDao();
		if(s != null){
			if(s.isAdmin() == true){
				plan.setId(null);
				plan.setUser(null);
				plan.setDefault(true);
				c = cDao.getByCode(plan.getCareerCode());
				c.setHasDefault(true);
				cDao.save(c);
				pDao.save(plan);
			}
		}
	}

	@Override
	public Long savePlan(Student student, Plan plan) {
		
		Long id = null;
		
		if(plan != null){
			PlanDao pDao = new PlanDao();
			
			LoginServiceImpl login = new LoginServiceImpl();
			LoginInfo loginInfo = login.login("");
			
			if(loginInfo.getStudent().getIdSun().equals(student.getIdSun()) == true){
				student = loginInfo.getStudent();
				if(student != null){
					
					plan.setUser(student);
					plan.setDefault(false);
					
					id = pDao.save(plan);
					
				}
			}
			
			
		}
		
		return id;
	}
	
	public Plan getPlanDefault(String careerCode){
		Plan p = null;
		if(careerCode.equals("") == false){
			PlanDao pDao = new PlanDao();
			p = pDao.getPlanDefault(careerCode).getClientInstance();
		}
		return p;
	}

	public List<ComplementaryValue> getComplementaryValues(List<String> subjectCodeStrings, List<String> subjectCareerStrings, String mainCareerCode){
		
		CareerDao careerDao = new CareerDao();
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		List<ComplementaryValue> cVListToReturn = new ArrayList<ComplementaryValue>();
		List<String> problematicSubjects = new ArrayList<String>();
		Career career = null;
		Career mainCareer = null;
		
		if(mainCareerCode != null && mainCareerCode.equals("") == false){			
			mainCareer = careerDao.getByCode(mainCareerCode);
		}
		
		/***** Arrange the subjectsCodes to be group together by the same career to minimize the search to the sia *****/
		Map<String,String> subjectCareerCodeStringMap = arrangeLists(subjectCodeStrings, subjectCareerStrings);
		subjectCodeStrings = new ArrayList<String>(subjectCareerCodeStringMap.keySet());
		subjectCareerStrings = new ArrayList<String>(subjectCareerCodeStringMap.values());
		/***************************************************************************************************************/
		
		for(String subjectCode : subjectCodeStrings){
			
			String careerCode = subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCode));
			
			if((career == null) ? true : career.getCode().equals(careerCode) == false){
				career = careerDao.getByCode(careerCode);					
			}
			
			boolean isProblematic = false;
			if(career != null || careerCode.equals("") == true){ //it will be almost all of the times true
				Subject subject = subjectDao.getByCode(subjectCode);
				if(subject != null){
					ComplementaryValueServer complementaryValue = complementaryValueDao.get(mainCareer.getCode(), subject.getCode());
					if(complementaryValue != null){
						cVListToReturn.add(complementaryValue.getClientInstance());
					}else{
						String typology = TypologyCodes.LIBRE_ELECCION;
						boolean mandatory = false;
						SubjectGroup subjectGroup = subjectGroupDao.getSubjectGroupFromTypology(mainCareer, typology); 
						if(subjectGroup != null && subjectGroup.getName() != null && subjectGroup.getName().isEmpty() == false){
							//Look create an complementaryVALUE as a free election to mainCareer
							complementaryValue = new ComplementaryValueServer(mainCareer, subject,  typology, mandatory, subjectGroup);
							cVListToReturn.add(complementaryValue.getClientInstance());
						}else{
							//if there is no code or name or credits then it is problematic
							isProblematic = true;							
						}
					}
				}else{
					isProblematic = true;
				}
			}
			if(isProblematic){
				problematicSubjects.add(subjectCode);
			}	
		}
		
		if(problematicSubjects.size() > 0){
			/***************************** Take care of the subjectsProblematic *****************************/
			career = null;
			
				long allResults0 = System.nanoTime();
			SiaResultSubjects allResults = getSubjectFromSia("", "", "", "", 1, 10000, "bog",null, problematicSubjects);
			List<Subject> allResultsList = allResults.getSubjectList();
				long allResults1 = System.nanoTime();
				long allResultsT = (allResults0 - allResults1)/1000000;
				log.info("AllResults: " + allResultsT +"ms");
				
			SiaResultSubjects careerResults = null;
			List<Subject> careerSubjectList = null;
			
			List<String> subjectProblematicToDelete = new ArrayList<String>();
			for(String subjectCodeString : problematicSubjects){
				String currentCareerCode =  subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCodeString));
				
				if((career == null) ? true : career.getCode().equals(currentCareerCode) == false){
					career = careerDao.getByCode(currentCareerCode);					

						long careerResults0 = System.nanoTime();
					careerResults = getSubjectFromSia("", "", currentCareerCode, "", 1, 10000, "bog", null, problematicSubjects);			
					careerSubjectList = careerResults.getSubjectList();
						long careerResults1 = System.nanoTime();
						long careerResultsT = (careerResults0 - careerResults1)/1000000;
						log.info("CareerResults: " + careerResultsT +"ms");
				}
				
				//search the subject
				Subject subjectFound  = null;
				for(Subject subjectT : allResultsList){
					assert subjectT.getId() != null;
					
					if(subjectCodeString.equals(subjectT.getCode())){
						
						
						ComplementaryValueServer complementaryValue = complementaryValueDao.get(currentCareerCode, subjectCodeString);
						
						if(complementaryValue == null){
							//create the complementaryValues
							Subject subjectFromDb = subjectDao.getDummySubjectByCode(subjectCodeString);
							if(subjectFromDb == null) subjectFromDb = subjectT;
							
							complementaryValue = new ComplementaryValueServer(career, subjectFromDb);
							complementaryValue.setId(complementaryValueDao.generateId());
							complementaryValue.setMandatory(false);
														
							String typology = careerResults.getTypologyForASubject(subjectT.getCode());
							if(typology == null || typology.isEmpty()){
								typology = "l";
							}
							complementaryValue.setTypology(SomosUNUtils.getTypology(typology));
							
							SubjectGroup subjectGroup = subjectGroupDao.getSubjectGroupFromTypology(career, typology);
							//if the subject is libre or niv get the sujectgroup libre or niv, and if it is the other to add it to unkown subjectgroup
							
							complementaryValue.setSubjectGroup(subjectGroup);
							
							complementaryValueDao.save(complementaryValue);
							
						}
						
						cVListToReturn.add(complementaryValue.getClientInstance());
						
						subjectProblematicToDelete.add(subjectCodeString);
						subjectFound = subjectT;
						break;
					}
				}
				//send subject To the end of the list
				if(subjectFound != null){
					allResultsList.remove(subjectFound);
					allResultsList.add(subjectFound);
				}
				
			}
			
			for(String subjectDummyT : subjectProblematicToDelete){
				problematicSubjects.remove(subjectDummyT);
			}
			
			/************************************************************************************************/
		}
		
		return cVListToReturn;
		
	}

	/**
	 * This method is to be use by the new getComplementaryValuesV2 method, this will add in order the two list, as if it was a map, 
	 * but this method will arrange together the subjects from the same career
	 * 
	 * @param subjectCodeStrings
	 * @param subjectCareerStrings
	 * @return
	 */
	private Map<String, String> arrangeLists(List<String> subjectCodeStrings, List<String> subjectCareerStrings) {
		Map<String, String> mapToReturn = new HashMap<String,String>();
		
		String careerCodeTemporary = "";
		
		List<String> temporaryList = null;
		List<String> fullListCareer = new ArrayList<String>();
		Map<String,List<String>> listMap = new HashMap<String, List<String>>();
		
//		temporaryList = new ArrayList<String>();
		for(String careerCode : subjectCareerStrings){
			if(careerCode.equals(careerCodeTemporary) == false || temporaryList == null){
				//the new code is different, so
				//1. see it there is already a list with that code, if not then create it and add the code to that list
				
				temporaryList = null;
				temporaryList = getListWithCode(listMap, careerCode);

				if(temporaryList == null){					
					String nameOfTheNewList = new String(careerCode);
					temporaryList = new ArrayList<String>();
					listMap.put(nameOfTheNewList, temporaryList);
				}				
			}
			
			temporaryList.add(careerCode);
		}
		for(List<String> list : listMap.values()){
			fullListCareer.addAll(list);
		}
		
		for(String careerCode : fullListCareer){
			int index = subjectCareerStrings.indexOf(careerCode);
			mapToReturn.put(subjectCodeStrings.get(index), careerCode);
			subjectCareerStrings.set(index, null);
		}
		
		return mapToReturn;
	}

	private List<String> getListWithCode(Map<String, List<String>> listMap, String careerCode) {
		List<String> listToReturn = null;
		
		for(String key : listMap.keySet()){
			if(key.equals(careerCode)){
				listToReturn = listMap.get(key);
				break;
			}
		}
		
		return listToReturn;
	}
	
	/**
	 * Will the return the full list of subjects complementaryValues, if it is necessary it will create the complementaryValue and search for its values
	 * <br></br>The only case where it will not return a complementaryValue for a subject is when the subject does not exist in the Sia results
	 */
	public List<ComplementaryValue> getComplementaryValues_OLD(List<String> subjectCodeStrings, List<String> subjectCareerStrings) {
		
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
		CareerDao careerDao = new CareerDao();
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		List<ComplementaryValue> cVList = new ArrayList<ComplementaryValue>();
		List<String> codeStringsProblematic = new ArrayList<String>();
		List<String> careerCodeStringsProblematic = new ArrayList<String>();
		Career career = careerDao.getByCode(subjectCareerStrings.get(0));
		
		for(String subjectCode : subjectCodeStrings){
			
			String careerCode = subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCode));
			
			if((career == null) ? true : career.getCode().equals(careerCode) == false){
				career = careerDao.getByCode(careerCode);					
//				if(career.hasAnalysis()  == false){
//					analyzeCareer(career.getCode());
//					career = careerDao.getCareerByCode(subjectCareerStrings.get(0));
//				}
			}
			
			if(career != null){
				Subject subject = subjectDao.getByCode(subjectCode);
				if(subject != null){
					ComplementaryValueServer complementaryValue = complementaryValueDao.get(career.getCode(), subject.getCode());
					if(complementaryValue != null){
						cVList.add(complementaryValue.getClientInstance());
					}
				}				
			}
			
		}
		
		//check if every String in selectedSubjectCodeStrings has a subjectValue, if not then search, create and save the ones that are not there
		int difference = subjectCodeStrings.size() - cVList.size();
		if(difference != 0){
			while(difference > 0){
				for(String subjectCodeT : subjectCodeStrings){
					boolean hasCV = false;
					for(ComplementaryValue cVT : cVList){
						if(cVT.getSubject().getCode().equals(subjectCodeT) == true){
							hasCV = true;
							break;
						}
					}
					if(hasCV == false){
						codeStringsProblematic.add(subjectCodeT);
						careerCodeStringsProblematic.add(subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCodeT)));
						difference --;
					}
				}
			}
			
			//do something with those subjects
			career = careerDao.getByCode(careerCodeStringsProblematic.get(0));
			
			SiaResultSubjects levelingResult = getSubjectFromSia("", "p", "", "", 1, 10000, "bog", null);
			SiaResultSubjects freeElectionResult = getSubjectFromSia("", "l", ""/*career.getCode()*/, "", 1, 10000, "bog", null, codeStringsProblematic);
			//freeElectionResult = getSubjectFromSia("", "l", career.getCode(), "", 1, freeElectionResult.getTotalAsignaturas(), "bog", null, codeStringsProblematic);
			
			List<Subject> levelingList = levelingResult.getSubjectList();
			List<Subject> freeElectionList = freeElectionResult.getSubjectList();
			List<Subject> fundamentalList = null;
			List<Subject> professionalList = null;
			
			String oldCareer = "";
			
			for(String subjectCodeT : codeStringsProblematic){
				
				Subject subjectT = null;
				String careerCodeT = careerCodeStringsProblematic.get(codeStringsProblematic.indexOf(subjectCodeT));
				
				if(oldCareer.equals(careerCodeT) == false){
					career = careerDao.getByCode(careerCodeT);					
//					if(career.hasAnalysis()  == false){
//						analyzeCareer(career.getCode());
//						career = careerDao.getCareerByCode(subjectCareerStrings.get(0));
//					}
					if(subjectCodeT.equals(SomosUNUtils.LIBRE_CODE) == false || subjectCodeT.equals(SomosUNUtils.OPTATIVA_CODE) == false){						
						levelingResult = getSubjectFromSia("", "p", ""/*career.getCode()*/, "", 1, 10000, "bog", null);
						levelingList = levelingResult.getSubjectList();
						freeElectionResult = getSubjectFromSia("", "l", ""/*career.getCode()*/, "", 1, 10000, "bog", null);
						freeElectionList = freeElectionResult.getSubjectList();
					}
					
				}
				
				if(career != null){
					if(subjectCodeT.equals(SomosUNUtils.LIBRE_CODE)){
					}else if(subjectCodeT.equals(SomosUNUtils.OPTATIVA_CODE)){
					}else{
						//The subject is not the dummy subjects "opativa" nor "libre"
						/**
						 * 1. Get the libreEleccion subjects and the Nivelación from SiaBuscador
						 * 2. Find out if the problematic subjects are part of any of those
						 * 3. Get/create the subject and create its cV and add to the list
						 */
						
						//for nivelación
						Subject subject = null;
						ComplementaryValueServer cV = null;
						SubjectGroup subjectGroup = null;
						for(Subject sT : levelingList){
							if(subjectCodeT.equals(sT.getCode()) == true){
								subject = subjectDao.getByCode(sT.getCode());

								cV = complementaryValueDao.get(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.get(SubjectGroupCodes.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValueServer(career, subject, "b", false, subjectGroup);
										cV.setId(complementaryValueDao.generateId());
										complementaryValueDao.save(cV);
									}else{
										//Se econtró pero no existe la agrupación así que no se hace nada TODO
									}
									
								}
								break;
							}
						}
						if(cV != null){
							cVList.add(cV.getClientInstance());
							oldCareer = careerCodeT;
							continue;
						}
						
						//for freeElection
						for(Subject sT : freeElectionList){
							if(subjectCodeT.equals(sT.getCode()) == true){
								subject = subjectDao.getByCode(sT.getCode());

								cV = complementaryValueDao.get(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.get(SubjectGroupCodes.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValueServer(career, subject, "l", false, subjectGroup);
										cV.setId(complementaryValueDao.generateId());
										complementaryValueDao.save(cV);
									}else{
										//Se econtró pero no existe la agrupación así que no se hace nada TODO
									}
									
								}
								break;
							}
						}
						
						if(cV != null){							
							cVList.add(cV.getClientInstance());
							oldCareer = careerCodeT;
							continue;
						}
						
						if(cV == null){
							//Look for the subject in the fundamentación y profesional lists
							if(fundamentalList == null || careerCodeT.equals(oldCareer) == false){
								fundamentalList = getSubjectFromSia("", "b", ""/*careerCodeT*/, "", 1, 10000, "bog", null, codeStringsProblematic).getSubjectList();
							}
							if(fundamentalList != null){
								if(fundamentalList.size() > 0){
									for(Subject sT : fundamentalList){
										if(subjectCodeT.equals(sT.getCode()) == true){
											subject = subjectDao.getByCode(sT.getCode());

											cV = complementaryValueDao.get(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, true);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValueServer(career, subject, SomosUNUtils.getTypology("f"), false, subjectGroup);
													cV.setId(complementaryValueDao.generateId());
													complementaryValueDao.save(cV);
												}
												
											}
											break;
										}
									}
									
									if(cV != null){							
										cVList.add(cV.getClientInstance());
										oldCareer = careerCodeT;
										continue;
									}
								}
							}
							
							//looking inside the professional subjects
							if(professionalList == null || careerCodeT.equals(oldCareer) == false){
								professionalList = getSubjectFromSia("", "d", ""/*careerCodeT*/, "", 1, 10000, "bog", null, codeStringsProblematic).getSubjectList();
							}
							if(professionalList != null){
								if(professionalList.size() > 0){
									for(Subject sT : professionalList){
										if(subjectCodeT.equals(sT.getCode()) == true){
											subject = subjectDao.getByCode(sT.getCode());

											cV = complementaryValueDao.get(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, false);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValueServer(career, subject, SomosUNUtils.getTypology("d"), false, subjectGroup);
													cV.setId(complementaryValueDao.generateId());
													complementaryValueDao.save(cV);
												}
												
											}
											break;
										}
									}
									
									if(cV != null){							
										cVList.add(cV.getClientInstance());
										oldCareer = careerCodeT;
										continue;
									}
								}
							}
							
						}
						
					}
					
				}
			}
		}
		
		return cVList;
	}

	/**
	 * Be really careful because here to send the code it is used the SomosUNUtils.LIBRE_CODE and the SubjetGroupCodes.LIBRE_NAME 
	 */
	public ComplementaryValue createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student) {
		
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao(); 
		
		SubjectGroup subjectGroup = null;
		if(subjectGroupName.equals(SomosUNUtils.LIBRE_CODE) == true || subjectGroupName.equals(SubjectGroupCodes.LIBRE_NAME) == true){
			subjectGroup = subjectGroupDao.get(SubjectGroupCodes.LIBRE_NAME, careerCode);
		}else{						
			subjectGroup = subjectGroupDao.get(subjectGroupName, careerCode);
		}
		
		return createDefaultSubject(subjectGroup, credits, careerCode, student);
		
	}
	
	/**
	 * This method will create acomplementaryValue (the old version it was just if you're logged as an admin, cuz the option 
	 * to add default subjects were only for admins when they're creating the default plans) [The old code is commented]
	 * 
	 * @param subjectGroupName -> please use the values from SomosUNUtils
	 * @param student -> can be null (for retro-compatibility)
	 */
	public ComplementaryValue createDefaultSubject(SubjectGroup subjectGroup, String credits, String careerCode, Student student) {
		
		ComplementaryValue complementaryValue = null;
		
		if(subjectGroup != null && credits.isEmpty() == false && careerCode.isEmpty() == false /*&& student != null*/){
			/*StudentDao studentDao = new StudentDao();
			student = studentDao.getStudentByIdSun(student.getIdSun());
			if(student != null){
				if(student.isAdmin() == true){*/
			
			CareerDao careerDao = new CareerDao();
			SubjectDao subjectDao = new SubjectDao();
			ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			
			Career career = careerDao.getByCode(careerCode);

			if(career!= null){
				
				Subject subjectDefault = null;
				
				//if(subjectGroup.getName().equals(SomosUNUtils.LIBRE_CODE) == true){
				if(subjectGroup.getName().equals(SubjectGroupCodes.LIBRE_NAME) == true){
					subjectDefault = new Subject(Integer.valueOf(credits), SomosUNUtils.LIBRE_CODE, SomosUNUtils.LIBRE_CODE, SubjectGroupCodes.LIBRE_NAME, "bog");
					subjectGroup = subjectGroupDao.get(SubjectGroupCodes.LIBRE_NAME, careerCode);
				}else{						
					subjectDefault = new Subject(Integer.valueOf(credits), SomosUNUtils.OPTATIVA_CODE, SomosUNUtils.OPTATIVA_CODE, SomosUNUtils.OPTATIVA_NAME, "bog");
					subjectGroup = subjectGroupDao.get(subjectGroup.getName(), careerCode);
				}
				
				if(subjectGroup != null){
					
					subjectDefault.setDefault(true);
					subjectDefault.setId(subjectDao.generateId());
					//prehasp this is duplicating the subjects in the db, if not then delete this comment later on
					subjectDao.save(subjectDefault);
					
					String t = null;
					if(subjectGroup.getName().equals(SubjectGroupCodes.LIBRE_NAME) == true) t = SomosUNUtils.getTypology("l");
					else t = (subjectGroup.isFundamental()== true ? SomosUNUtils.getTypology("f") : SomosUNUtils.getTypology("c"));
					
					complementaryValue = new ComplementaryValue(career, subjectDefault, t, false, subjectGroup);
					complementaryValue.setId(complementaryValueDao.generateId());
					complementaryValueDao.save(complementaryValue);
					
				}
				
			}
			
					/*}
				}*/
		}
		
		return complementaryValue;
	}

	public List<SubjectGroup> getSubjectGroups(String careerCode) {
		
		List<SubjectGroup> subjectGroupsListToReturn = new ArrayList<SubjectGroup>();
		
		if(careerCode != null){
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			List<SubjectGroup> subjectGroupsList = subjectGroupDao.getList(careerCode);
			for(SubjectGroup sG : subjectGroupsList){				
				subjectGroupsListToReturn.add(sG);
			}
		}
		
		return subjectGroupsListToReturn;
	}

	/**
	 * if the career has no analysis, this method will run it before returning the career
	 */
	@Override
	public Career getCareerToUse(String careerCode) {
		
		Career c = null;
		CareerDao careerDao = new CareerDao();
		
		if(careerCode != null){
			c = careerDao.getByCode(careerCode);
		}
		
//		if(c != null && c.hasAnalysis() == false){
//			analyzeCareer(c.getCode());
//			
//			c = careerDao.getCareerByCode(careerCode);
//			
//		}
		
		return c; 
	}

	@Override
	public List<ComplementaryValue> getMandatoryComplementaryValues(String careerCode) {
		
		List<ComplementaryValue> listToReturn = null;
		
		if(careerCode != null){
			ComplementaryValueDao cVDao = new ComplementaryValueDao();
			List<ComplementaryValueServer> cVListT = cVDao.getMandatoryComplementaryValues(careerCode);
			
			if(cVListT != null && cVListT.size() > 0){
				listToReturn = new ArrayList<ComplementaryValue>();
				for(ComplementaryValueServer cVT : cVListT){
					listToReturn.add(cVT.getClientInstance());
				}
			}
		}
		
		return listToReturn;
	}
	
	public List<Plan> getPlansByUserLoggedIn(){
		LoginServiceImpl loginService = new LoginServiceImpl();
		Student s = loginService.login("").getStudent();
		
		List<Plan> plansToReturn = null;
		
		if(s != null){
			plansToReturn = new ArrayList<Plan>();
			PlanDao planDao = new PlanDao();
			List<PlanServer> plans = planDao.getPlanByUser(s);
			
			for(PlanServer p : plans){
				plansToReturn.add(p.getClientInstance());
			}
		}
		return plansToReturn;
	}

	@Override
	public List<PlanValuesResult> getPlanValuesByUserLoggedIn() {
		List<Plan> plans = getPlansByUserLoggedIn();
		List<PlanValuesResult> planValuesToReturn = null;
		
		if(plans != null && plans.size() > 0){
			planValuesToReturn = new ArrayList<PlanValuesResult>();
			for(Plan p : plans){
				PlanValuesResult value = new PlanValuesResult(p.getName(), Long.toString(p.getId()));
				planValuesToReturn.add(value);
			}
		}
		
		return planValuesToReturn;
	}

	@Override
	public void deletePlanFromUser(String planId) {
		LoginServiceImpl loginService = new LoginServiceImpl();
		Student s = loginService.login("").getStudent();
		
		PlanDao planDao = new PlanDao();
		PlanServer plan = planDao.getById(Long.valueOf(planId));
		
		if(plan != null && plan.getUserRef().get().getIdSun().equals(s.getIdSun()) == true){
			planDao.delete(plan);
		}
	}

	@Override
	public Plan getPlanByUser(String planId) {
		Plan planToReturn = null;
		
		if(planId != null && planId.isEmpty() == false){
			
			PlanDao planDao = new PlanDao();
			planToReturn = planDao.getById(Long.valueOf(planId)).getClientInstance();
			
			if(planToReturn != null){
				LoginServiceImpl loginService = new LoginServiceImpl();
				Student s = loginService.login("").getStudent();
				
				if(planToReturn.getUser().getIdSun().equals(s.getIdSun()) == false){
					planToReturn = null;
				}
			}
			
		}
		return planToReturn;
	}

	@Override
	public Plan generatePlanFromAcademicHistory(String academicHistory) {
		
		Plan plan = null;
		
		if(academicHistory != null){
			PlanDao planDao = new PlanDao();
			plan = planDao.generatePlanFromAcademicHistory(academicHistory).getClientInstance();
		}
		return plan;
	}

	@Override
	public ComplementaryValue getComplementaryValueFromDb(String careerCode, String subjectCode) {
		// TODO Auto-generated method stub
		ComplementaryValueDao cVDao = new ComplementaryValueDao();
		ComplementaryValueServer cVS = cVDao.get(careerCode, subjectCode);
		ComplementaryValue toReturn = null;
		if(cVS != null) toReturn = cVS.getClientInstance();
		return toReturn;
	}

	@Override
	public void saveMessage(String name, String subject, String type, String messageString) {
		
		LoginServiceImpl loginService = new LoginServiceImpl();
		Student student = loginService.login("").getStudent();
		
		name = Jsoup.clean(name, Whitelist.simpleText());
		subject = Jsoup.clean(subject, Whitelist.simpleText());
		type = SomosUNUtils.setCorrectType(type);
		messageString = Jsoup.clean(messageString, Whitelist.simpleText());
		
		Message message = new Message(name, subject, type, messageString, student);
		
		MessageDao messageDao = new MessageDao();
		messageDao.save(new MessageServer(message));
	}

	@Override
	public SemesterValue getCurrentSemesterValue() {
	
		SemesterValueDao semesterValueDao = new SemesterValueDao();
		return (SemesterValue) semesterValueDao.getCurrentSemester();
	}

	@Override
	public CompletePlanInfo getCompletePlanInfo(String careerCode) {
		CareerDao careerDao = new CareerDao();
		Career career = careerDao.getByCode(careerCode);
		CompletePlanInfo toReturn = null;
		
		if(career != null){
			
			
			if(career.hasAnalysis() == true){
				toReturn  = new CompletePlanInfo();
				toReturn.setCareer(career);

				SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
				List<SubjectGroup> subjectGroups = subjectGroupDao.getList(career.getCode());
				List<SubjectGroup> subjectGroupsToReturn = new ArrayList<SubjectGroup>();
				
				for(SubjectGroup sG : subjectGroups){
					subjectGroupsToReturn.add(sG);
				}
				
				toReturn.setSubjectGroups(subjectGroupsToReturn);
						
				ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
				List<ComplementaryValueServer> mandatoryComplementaryValues = complementaryValueDao.getMandatoryComplementaryValues(career.getCode());
				List<ComplementaryValue> mandatoryComplementaryValuesToReturn = new ArrayList<ComplementaryValue>();
				
				for(ComplementaryValueServer cV : mandatoryComplementaryValues){
					mandatoryComplementaryValuesToReturn.add(cV.getClientInstance());
				}
				
				toReturn.setMandatoryComplementaryValues(mandatoryComplementaryValuesToReturn);
				
				if(career.hasDefault() == true){
					
					PlanServer defaultPlan = null;
					PlanDao planDao = new PlanDao();
					defaultPlan = planDao.getPlanDefault(career.getCode());
					
					toReturn.setPlanDefautl(defaultPlan.getClientInstance());
					
				}
			
				/****** Copy all the subject from defaultPlan to mandatory subjects ******/
				if(toReturn.getPlanDefautl() != null){
					for(Semester semester : toReturn.getPlanDefautl().getSemesters()){
						for(SubjectValue subjectValue : semester.getSubjects()){
							//Check if it is in the list
							if(SomosUNUtils.getSubjectFromListByCode(subjectValue.getComplementaryValue().getSubject().getCode(), subjectValue.getComplementaryValue().getSubject().getId(), subjectValue.getComplementaryValue().getSubject().getName(), toReturn.getMandatoryComplementaryValues()) == null){
								//add it
								toReturn.getMandatoryComplementaryValues().add(subjectValue.getComplementaryValue());
							}						
						}
					}
				}
				/*************************************************************************/

				/****** <Add the requisites not added in the plan *******/
				List<ComplementaryValue> subjectsIncomplete = new ArrayList<ComplementaryValue>();
				List<String> subjectsToSearch = new ArrayList<String>();
				for(ComplementaryValue cV: toReturn.getMandatoryComplementaryValues()){
					//Check for requisites, add the ones that are not here into a list
					
					//merge the two lists
					List<List<Subject>> listOfListsToSearch = new ArrayList<List<Subject>>();
					listOfListsToSearch.addAll(cV.getPrerequisitesLists());
					listOfListsToSearch.addAll(cV.getCorequisitesLists());
					
					for(List<Subject> list : listOfListsToSearch){
						//This is a OR list, if there is any subject in the plan then not add all of them, select just one
						boolean notToAdd = false;
						List<String> codesToAdd = new ArrayList<String>();
						for(Subject s : list){
							if(SomosUNUtils.getSubjectByCodeInList(s.getCode(), toReturn.getMandatoryComplementaryValues()) == null){
								if(subjectsToSearch.contains(s.getCode()) == false){									
									codesToAdd.add(s.getCode());
								}
							}else{
								notToAdd = true;
							}
						}
						if(notToAdd == false && codesToAdd.isEmpty() == false){
							subjectsToSearch.addAll(codesToAdd);
							subjectsIncomplete.add(cV);
						}
					}
				}
				//Getting the cV for those subjectsToSearch
				if(subjectsToSearch.isEmpty() == false){					
					List<ComplementaryValueServer> complementaryValuesServerLeftout = SiaProxy.getRequisitesFromMisPlanes(subjectsToSearch, careerCode);
					List<ComplementaryValue> complementaryValuesLeftout = new ArrayList<ComplementaryValue>();
					if(complementaryValuesServerLeftout != null){
						for(ComplementaryValueServer cVS : complementaryValuesServerLeftout){
							complementaryValuesLeftout.add(cVS.getClientInstance());
						}
					}
					
					for(ComplementaryValue cV : subjectsIncomplete){
						List<List<Subject>> listOfListsToSearch = new ArrayList<List<Subject>>();
						listOfListsToSearch.addAll(cV.getPrerequisitesLists());
						listOfListsToSearch.addAll(cV.getCorequisitesLists());
						
						for(List<Subject> list : listOfListsToSearch){
							for(Subject s : list){
								if(s.getCode().trim().isEmpty() == false){									
									ComplementaryValue cVToAdd = SomosUNUtils.getSubjectByCodeInList(s.getCode(), complementaryValuesLeftout);
									if(cVToAdd != null){										
										toReturn.getMandatoryComplementaryValues().add(cVToAdd);
										break;
									}
								}
								
							}
						}
					}
					
					//Check which cV from complementaryValuesLeftout should be added 
				}
				/****** </Add the requisites not added in the plan ******/
				
				/******** <taking care of the subjects groups> *********/
				// Count the number of the no-mandatory subjects for a given subjectGroup and find the credits number left to complete that subjectGroup
				// For every subject, if not mandatory find the subjectGroup in the map and add the number of credits
				Map<SubjectGroup, Integer> amountOfCredits = new HashMap<SubjectGroup, Integer>();
				for(ComplementaryValue cVToSG : toReturn.getMandatoryComplementaryValues()){
					if(cVToSG.isMandatory() == false){
						SubjectGroup sG_temporary = SomosUNUtils.getSubjectGroupInSetByName(cVToSG.getSubjectGroup().getName(), amountOfCredits.keySet());
						int x = -1;
						
						if(sG_temporary != null) {
							x = amountOfCredits.get(sG_temporary);
						} else {
							x = 0;
							sG_temporary = cVToSG.getSubjectGroup();
						}
						
						x += cVToSG.getSubject().getCredits();
						
						amountOfCredits.put(sG_temporary, x);
						
					}
				}
				//Add to the mandatorySubjectList an optative subject with that amount of credits left
				for(SubjectGroup sG : toReturn.getSubjectGroups()){
					
					int creditsToAdd = 0;
					SubjectGroup sG_temporary = SomosUNUtils.getSubjectGroupInSetByName(sG.getName(), amountOfCredits.keySet());
					
					if(sG_temporary == null){					
						creditsToAdd = sG.getOptativeCredits();
					}else{
						int creditsAdded = amountOfCredits.get(sG_temporary);
						creditsToAdd = sG.getOptativeCredits() - creditsAdded;
					}
					
					if(creditsToAdd > 0){
						
						String credits = "" + creditsToAdd;
						
						//Create a default subject (cV) for sG with creditsToAdd credits and add it to the mandatorySubjectList
						ComplementaryValue cVDefault = null;
						if(sG.getName().equals(SubjectGroupCodes.LIBRE_NAME)){
							cVDefault = createDefaultSubject(SubjectGroupCodes.LIBRE_NAME, credits, careerCode, null);
							log.info("There is one libre elección");
						}else{							
							cVDefault =	createDefaultSubject(sG, credits, careerCode, null);
						}
						toReturn.getMandatoryComplementaryValues().add(cVDefault);
					}
				}
				/******** </taking care of the subjects groups> ********/
					
			}
			
			
		}
		
		return toReturn;
	}

	@Override
	public Long savePlanAndGrade(Student student, Plan plan, Group group, Double oldGrade, Double newGrade) {
		
		if(group != null){
			//TODO updateGrade
			SomosUNServerUtils.createGradeUpdaterTask(group.getTeacher(), group.getSemesterValue(), oldGrade, newGrade, group.getSubject());
			
			//This is only for dev mode
			//GradeUpdaterCronJob.updateAllGrades();
		}else{
			log.warning("savePlanAndGrade - A subject which has no group, the plan's id is " + plan.getId());
		}
		
		return savePlan(student, plan);
	}

}
