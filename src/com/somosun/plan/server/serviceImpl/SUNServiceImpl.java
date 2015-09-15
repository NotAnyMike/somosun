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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.dao.CareerDao;
import com.somosun.plan.server.dao.ComplementaryValueDao;
import com.somosun.plan.server.dao.MessageDao;
import com.somosun.plan.server.dao.PlanDao;
import com.somosun.plan.server.dao.StudentDao;
import com.somosun.plan.server.dao.SubjectDao;
import com.somosun.plan.server.dao.SubjectGroupDao;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.PlanValuesResult;
import com.somosun.plan.shared.RandomPhrase;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
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
		List<Career> listFromDb = careerDao.getCareersBySede(sede);
		List<Career> listToReturn = new ArrayList<Career>();
		for(Career c : listFromDb){
			listToReturn.add(c);
		}
		return listToReturn;
	}

	@Override
	public Plan getPlanDefaultFromString(String careerCode) {
		PlanDao planDao = new PlanDao();
		return (Plan) planDao.createPlanFromDefaultString(careerCode);
	}

	@Override
	public void toTest() {
		//SiaProxy.getRequisitesForACareer("2522");
	}

	@Override
	public ComplementaryValue getComplementaryValueFromMisPlanes(String career, String code) {
		return (ComplementaryValue) SiaProxy.getRequisitesFromSia(code, career);
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
		List<ComplementaryValue> cVList = cVDao.getComplementaryValues(careerCode);
		List<ComplementaryValue> cVListToReturn = new ArrayList<ComplementaryValue>();
		for(ComplementaryValue cV : cVList){
			cVListToReturn.add(cV);
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
				plan.setUser(null);
				plan.setDefault(true);
				c = cDao.getCareerByCode(plan.getCareerCode());
				c.setHasDefault(true);
				cDao.saveCareer(c);
				pDao.savePlan(plan);
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
					
					id = pDao.savePlan(plan);
					
				}
			}
			
			
		}
		
		return id;
	}
	
	public Plan getPlanDefault(String careerCode){
		Plan p = null;
		if(careerCode.equals("") == false){
			PlanDao pDao = new PlanDao();
			p = pDao.getPlanDefault(careerCode);
		}
		return p;
	}

//	@Override
//	public void analyzeCareer(String careerCode) {
//		SiaProxy.getRequisitesForACareer(careerCode);
//	}

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
			mainCareer = careerDao.getCareerByCode(mainCareerCode);
		}
		
		/***** Arrange the subjectsCodes to be group together by the same career to minimize the search to the sia *****/
		Map<String,String> subjectCareerCodeStringMap = arrangeLists(subjectCodeStrings, subjectCareerStrings);
		subjectCodeStrings = new ArrayList<String>(subjectCareerCodeStringMap.keySet());
		subjectCareerStrings = new ArrayList<String>(subjectCareerCodeStringMap.values());
		/***************************************************************************************************************/
		
		for(String subjectCode : subjectCodeStrings){
			
			String careerCode = subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCode));
			
			if((career == null) ? true : career.getCode().equals(careerCode) == false){
				career = careerDao.getCareerByCode(careerCode);					
			}
			
			boolean isProblematic = false;
			if(career != null || careerCode.equals("") == true){ //it will be almost all of the times true
				Subject subject = subjectDao.getSubjectByCode(subjectCode);
				if(subject != null){
					ComplementaryValue complementaryValue = complementaryValueDao.getComplementaryValues(mainCareer.getCode(), subject.getCode());
					if(complementaryValue != null){
						cVListToReturn.add(complementaryValue);
					}else{
						String typology = TypologyCodes.LIBRE_ELECCION;
						boolean mandatory = false;
						SubjectGroup subjectGroup = subjectGroupDao.getSubjectGroupFromTypology(mainCareer, typology); 
						if(subjectGroup != null && subjectGroup.getName() != null && subjectGroup.getName().isEmpty() == false){
							//Look create an complementaryVALUE as a free election to mainCareer
							complementaryValue = new ComplementaryValue(mainCareer, subject,  typology, mandatory, subjectGroup);
							cVListToReturn.add(complementaryValue);
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
					career = careerDao.getCareerByCode(currentCareerCode);					

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
						
						
						ComplementaryValue complementaryValue = complementaryValueDao.getComplementaryValues(currentCareerCode, subjectCodeString);
						
						if(complementaryValue == null){
							//create the complementaryValues
							Subject subjectFromDb = subjectDao.getDummySubjectByCode(subjectCodeString);
							if(subjectFromDb == null) subjectFromDb = subjectT;
							
							complementaryValue = new ComplementaryValue(career, subjectFromDb);
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
							
							complementaryValueDao.saveComplementaryValues(complementaryValue);
							
						}
						
						cVListToReturn.add(complementaryValue);
						
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

		for(String careerCode : subjectCareerStrings){
			if(careerCode.equals(careerCodeTemporary) == false){
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
			subjectCareerStrings.set(index, "");
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
		Career career = careerDao.getCareerByCode(subjectCareerStrings.get(0));
		
		for(String subjectCode : subjectCodeStrings){
			
			String careerCode = subjectCareerStrings.get(subjectCodeStrings.indexOf(subjectCode));
			
			if((career == null) ? true : career.getCode().equals(careerCode) == false){
				career = careerDao.getCareerByCode(careerCode);					
//				if(career.hasAnalysis()  == false){
//					analyzeCareer(career.getCode());
//					career = careerDao.getCareerByCode(subjectCareerStrings.get(0));
//				}
			}
			
			if(career != null){
				Subject subject = subjectDao.getSubjectByCode(subjectCode);
				if(subject != null){
					ComplementaryValue complementaryValue = complementaryValueDao.getComplementaryValues(career.getCode(), subject.getCode());
					if(complementaryValue != null){
						cVList.add(complementaryValue);
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
			career = careerDao.getCareerByCode(careerCodeStringsProblematic.get(0));
			
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
					career = careerDao.getCareerByCode(careerCodeT);					
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
						ComplementaryValue cV = null;
						SubjectGroup subjectGroup = null;
						for(Subject sT : levelingList){
							if(subjectCodeT.equals(sT.getCode()) == true){
								subject = subjectDao.getSubjectByCode(sT.getCode());

								cV = complementaryValueDao.getComplementaryValues(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValue(career, subject, "b", false, subjectGroup);
										cV.setId(complementaryValueDao.generateId());
										complementaryValueDao.saveComplementaryValues(cV);
									}else{
										//Se econtró pero no existe la agrupación así que no se hace nada TODO
									}
									
								}
								break;
							}
						}
						if(cV != null){
							cVList.add(cV);
							oldCareer = careerCodeT;
							continue;
						}
						
						//for freeElection
						for(Subject sT : freeElectionList){
							if(subjectCodeT.equals(sT.getCode()) == true){
								subject = subjectDao.getSubjectByCode(sT.getCode());

								cV = complementaryValueDao.getComplementaryValues(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValue(career, subject, "l", false, subjectGroup);
										cV.setId(complementaryValueDao.generateId());
										complementaryValueDao.saveComplementaryValues(cV);
									}else{
										//Se econtró pero no existe la agrupación así que no se hace nada TODO
									}
									
								}
								break;
							}
						}
						
						if(cV != null){							
							cVList.add(cV);
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
											subject = subjectDao.getSubjectByCode(sT.getCode());

											cV = complementaryValueDao.getComplementaryValues(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, true);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValue(career, subject, SomosUNUtils.getTypology("f"), false, subjectGroup);
													cV.setId(complementaryValueDao.generateId());
													complementaryValueDao.saveComplementaryValues(cV);
												}
												
											}
											break;
										}
									}
									
									if(cV != null){							
										cVList.add(cV);
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
											subject = subjectDao.getSubjectByCode(sT.getCode());

											cV = complementaryValueDao.getComplementaryValues(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, false);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValue(career, subject, SomosUNUtils.getTypology("d"), false, subjectGroup);
													cV.setId(complementaryValueDao.generateId());
													complementaryValueDao.saveComplementaryValues(cV);
												}
												
											}
											break;
										}
									}
									
									if(cV != null){							
										cVList.add(cV);
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

	@Override
	public ComplementaryValue createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student) {
		
		ComplementaryValue complementaryValue = null;
		
		if(subjectGroupName.isEmpty() == false && credits.isEmpty() == false && careerCode.isEmpty() == false && student != null){
			StudentDao studentDao = new StudentDao();
			student = studentDao.getStudentByIdSun(student.getIdSun());
			if(student != null){
				if(student.isAdmin() == true){
					
					CareerDao careerDao = new CareerDao();
					SubjectDao subjectDao = new SubjectDao();
					ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
					SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
					
					Career career = careerDao.getCareerByCode(careerCode);

					if(career!= null){
						
						Subject subjectDefault = null;
						SubjectGroup subjectGroup = null;
						
						if(subjectGroupName.equals(SomosUNUtils.LIBRE_CODE) == true){
							subjectDefault = new Subject(Integer.valueOf(credits), SomosUNUtils.LIBRE_CODE, SomosUNUtils.LIBRE_CODE, SomosUNUtils.LIBRE_NAME, "bog");
							subjectGroup = subjectGroupDao.getSubjectGroup(SubjectGroupCodes.LIBRE_NAME, careerCode);
						}else{						
							subjectDefault = new Subject(Integer.valueOf(credits), SomosUNUtils.OPTATIVA_CODE, SomosUNUtils.OPTATIVA_CODE, SomosUNUtils.OPTATIVA_NAME, "bog");
							subjectGroup = subjectGroupDao.getSubjectGroup(subjectGroupName, careerCode);
						}
						
						if(subjectGroup != null){
							
							subjectDefault.setDefault(true);
							subjectDefault.setId(subjectDao.generateId());
							//prehasp this is duplicating the subjects in the db, if not then delete this comment later on
							subjectDao.saveSubject(subjectDefault);
							
							String t = null;
							if(subjectGroupName.equals(SomosUNUtils.LIBRE_CODE) == true) t = SomosUNUtils.getTypology("l");
							else t = (subjectGroup.isFundamental()== true ? SomosUNUtils.getTypology("f") : SomosUNUtils.getTypology("c"));
							
							complementaryValue = new ComplementaryValue(career, subjectDefault, t, false, subjectGroup);
							complementaryValue.setId(complementaryValueDao.generateId());
							complementaryValueDao.saveComplementaryValues(complementaryValue);
							
						}
						
						
						
						
						
					}
					
				}
			}
		}
		
		return complementaryValue;
	}

	public List<SubjectGroup> getSubjectGroups(String careerCode) {
		
		List<SubjectGroup> subjectGroupsListToReturn = new ArrayList<SubjectGroup>();
		
		if(careerCode != null){
			SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
			List<SubjectGroup> subjectGroupsList = subjectGroupDao.getSubjectGroups(careerCode);
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
			c = careerDao.getCareerByCode(careerCode);
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
			List<ComplementaryValue> cVListT = cVDao.getMandatoryComplementaryValues(careerCode);
			
			if(cVListT != null && cVListT.size() > 0){
				listToReturn = new ArrayList<ComplementaryValue>();
				for(ComplementaryValue cVT : cVListT){
					listToReturn.add(cVT);
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
			List<Plan> plans = planDao.getPlanByUser(s);
			
			for(Plan p : plans){
				plansToReturn.add(p);
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
		Plan plan = planDao.getPlanById(Long.valueOf(planId));
		
		if(plan != null && plan.getUser().getIdSun().equals(s.getIdSun()) == true){
			planDao.deletePlan(plan);
		}
	}

	@Override
	public Plan getPlanByUser(String planId) {
		Plan planToReturn = null;
		
		if(planId != null && planId.isEmpty() == false){
			
			PlanDao planDao = new PlanDao();
			planToReturn = planDao.getPlanById(Long.valueOf(planId));
			
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
			plan = planDao.generatePlanFromAcademicHistory(academicHistory);
		}
		return plan;
	}

	@Override
	public ComplementaryValue getComplementaryValueFromDb(String careerCode, String subjectCode) {
		// TODO Auto-generated method stub
		ComplementaryValueDao cVDao = new ComplementaryValueDao();
		ComplementaryValue toReturn = cVDao.getComplementaryValues(careerCode, subjectCode);
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
		messageDao.saveMessage(message);
	}

	
}
