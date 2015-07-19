package com.uibinder.index.server.serviceImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.log.AppLogLine;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.server.SiaProxy;
import com.uibinder.index.server.dao.CareerDao;
import com.uibinder.index.server.dao.ComplementaryValuesDao;
import com.uibinder.index.server.dao.PlanDao;
import com.uibinder.index.server.dao.StudentDao;
import com.uibinder.index.server.dao.SubjectDao;
import com.uibinder.index.server.dao.SubjectGroupDao;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.PlanValuesResult;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.SomosUNUtils;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectGroup;

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
	private static final Logger log = Logger.getLogger("testing-sunServiceImpl");


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
		//SiaProxy.updateCareersFromSia("bog");
	}

	@Override
	public ComplementaryValues getComplementaryValuesFromMisPlanes(String career, String code) {
		return (ComplementaryValues) SiaProxy.getRequisitesFromSia(code, career);
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
	public List<ComplementaryValues> getComplementaryValuesFromMisPlanes(String careerCode) {
		ComplementaryValuesDao cVDao = new ComplementaryValuesDao();
		List<ComplementaryValues> cVList = cVDao.getComplementaryValues(careerCode);
		List<ComplementaryValues> cVListToReturn = new ArrayList<ComplementaryValues>();
		for(ComplementaryValues cV : cVList){
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
				cDao.updateCareer(c);
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

	@Override
	public void analyzeCareer(String careerCode) {
		SiaProxy.getRequisitesForACareer(careerCode);
	}

	/**
	 * Will the return the full list of subjects complementaryValues, if it is necessary it will create the complementaryValue and search for its values
	 * <br></br>The only case where it will not return a complementaryValue for a subject is when the subject does not exist in the Sia results
	 */
	public List<ComplementaryValues> getComplementaryValues(List<String> selectedSubjectCodeStrings, List<String> selectedSubjectCareerStrings) {
		
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
		CareerDao careerDao = new CareerDao();
		SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
		
		List<ComplementaryValues> cVList = new ArrayList<ComplementaryValues>();
		List<String> codeStringsProblematic = new ArrayList<String>();
		List<String> careerCodeStringsProblematic = new ArrayList<String>();
		Career career = careerDao.getCareerByCode(selectedSubjectCareerStrings.get(0));
		
		for(String subjectCode : selectedSubjectCodeStrings){
			
			String careerCode = selectedSubjectCareerStrings.get(selectedSubjectCodeStrings.indexOf(subjectCode));
			
			if((career == null) ? true : career.getCode().equals(careerCode) == false){
				career = careerDao.getCareerByCode(careerCode);					
				if(career.hasAnalysis()  == false){
					analyzeCareer(career.getCode());
					career = careerDao.getCareerByCode(selectedSubjectCareerStrings.get(0));
				}
			}
			
			if(career != null){
				Subject subject = subjectDao.getSubjectByCode(subjectCode);
				if(subject != null){
					ComplementaryValues complementaryValues = complementaryValuesDao.getComplementaryValues(career.getCode(), subject.getCode());
					if(complementaryValues != null){
						cVList.add(complementaryValues);
					}
				}				
			}
			
		}
		
		//check if every String in selectedSubjectCodeStrings has a subjectValue, if not then search, create and save the ones that are not there
		int difference = selectedSubjectCodeStrings.size() - cVList.size();
		if(difference != 0){
			while(difference > 0){
				for(String subjectCodeT : selectedSubjectCodeStrings){
					boolean hasCV = false;
					for(ComplementaryValues cVT : cVList){
						if(cVT.getSubject().getCode().equals(subjectCodeT) == true){
							hasCV = true;
							break;
						}
					}
					if(hasCV == false){
						codeStringsProblematic.add(subjectCodeT);
						careerCodeStringsProblematic.add(selectedSubjectCareerStrings.get(selectedSubjectCodeStrings.indexOf(subjectCodeT)));
						difference --;
					}
				}
			}
			
			//do something with those subjects
			career = careerDao.getCareerByCode(careerCodeStringsProblematic.get(0));
			
			SiaResultSubjects levelingResult = getSubjectFromSia("", "p", career.getCode(), "", 1, 1000, "bog", null);
			SiaResultSubjects freeElectionResult = getSubjectFromSia("", "l", career.getCode(), "", 1, 1000, "bog", null, codeStringsProblematic);
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
					if(career.hasAnalysis()  == false){
						analyzeCareer(career.getCode());
						career = careerDao.getCareerByCode(selectedSubjectCareerStrings.get(0));
					}
					if(subjectCodeT.equals(SomosUNUtils.LIBRE_CODE) == false || subjectCodeT.equals(SomosUNUtils.OPTATIVA_CODE) == false){						
						levelingResult = getSubjectFromSia("", "p", career.getCode(), "", 1, 1000, "bog", null);
						levelingList = levelingResult.getSubjectList();
						freeElectionResult = getSubjectFromSia("", "l", career.getCode(), "", 1, 1000, "bog", null);
						freeElectionList = freeElectionResult.getSubjectList();
					}
					
				}
				
				if(career != null){
					if(subjectCodeT.equals(SomosUNUtils.LIBRE_CODE)){
						//means there is no cV for Libre
//						subjectT = subjectDao.getSubjectByCode(subjectCodeT);
//						
//						if(subjectT == null){
//							subjectDao.createSubjectLibre();
//							subjectT = subjectDao.getSubjectByCode(subjectCodeT);
//						}						
//						
//						complementaryValuesDao.createComplementaryValuesForLibre(career.getCode());
//						ComplementaryValues cVT = complementaryValuesDao.getComplementaryValues(career.getCode(), SomosUNUtils.LIBRE_CODE);
//						
//						if(cVT != null){
//							cVList.add(cVT);
//							oldCareer = careerCodeT;
//						}
					}else if(subjectCodeT.equals(SomosUNUtils.OPTATIVA_CODE)){
						//can not uncomment this because I need the subjecGroup id to do it, therefore cannot add this subject
//						//means there is no cV for Optativa
//						subjectT = subjectDao.getSubjectByCode(subjectCodeT);
//						
//						if(subjectT == null){
//							subjectDao.createSubjectOptative();;
//							subjectT = subjectDao.getSubjectByCode(subjectCodeT);
//						}						
//						
//						complementaryValuesDao.createComplementaryValuesForOptativa(career.getCode(), subjectGroupId);(career.getCode());
//						ComplementaryValues cVT = complementaryValuesDao.getComplementaryValues(career.getCode(), SomosUNUtils.OPTATIVA_CODE);
//						
//						if(cVT != null){
//							cVList.add(cVT);
//						}
					}else{
						//The subject is not the dummy subjects "opativa" nor "libre"
						/**
						 * 1. Get the libreEleccion subjects and the Nivelación from SiaBuscador
						 * 2. Find out if the problematic subjects are part of any of those
						 * 3. Get/create the subject and create its cV and add to the list
						 */
						
						//for nivelación
						Subject subject = null;
						ComplementaryValues cV = null;
						SubjectGroup subjectGroup = null;
						for(Subject sT : levelingList){
							if(subjectCodeT.equals(sT.getCode()) == true){
								subject = subjectDao.getSubjectByCode(sT.getCode());

								cV = complementaryValuesDao.getComplementaryValues(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.getSubjectGroup(SomosUNUtils.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValues(career, subject, "b", false, subjectGroup);
										cV.setId(complementaryValuesDao.generateId());
										complementaryValuesDao.saveComplementaryValues(cV);
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

								cV = complementaryValuesDao.getComplementaryValues(careerCodeT, subjectCodeT);
								
								if(cV == null){
									//Create the cV
									subjectGroup = subjectGroupDao.getSubjectGroup(SomosUNUtils.NIVELACION_NAME, careerCodeT);
									
									if(subjectGroup != null){										
										cV = new ComplementaryValues(career, subject, "l", false, subjectGroup);
										cV.setId(complementaryValuesDao.generateId());
										complementaryValuesDao.saveComplementaryValues(cV);
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
								fundamentalList = getSubjectFromSia("", "b", careerCodeT, "", 1, 1000, "bog", null, codeStringsProblematic).getSubjectList();
							}
							if(fundamentalList != null){
								if(fundamentalList.size() > 0){
									for(Subject sT : fundamentalList){
										if(subjectCodeT.equals(sT.getCode()) == true){
											subject = subjectDao.getSubjectByCode(sT.getCode());

											cV = complementaryValuesDao.getComplementaryValues(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, true);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValues(career, subject, SomosUNUtils.getTypology("f"), false, subjectGroup);
													cV.setId(complementaryValuesDao.generateId());
													complementaryValuesDao.saveComplementaryValues(cV);
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
								professionalList = getSubjectFromSia("", "d", careerCodeT, "", 1, 1000, "bog", null, codeStringsProblematic).getSubjectList();
							}
							if(professionalList != null){
								if(professionalList.size() > 0){
									for(Subject sT : professionalList){
										if(subjectCodeT.equals(sT.getCode()) == true){
											subject = subjectDao.getSubjectByCode(sT.getCode());

											cV = complementaryValuesDao.getComplementaryValues(careerCodeT, subjectCodeT);
											
											if(cV == null){
												//Create the cV
												
												//TODO get the subjectGroup from comparing the subjects which have a CV that is fundamental and from the same career
												//else add it to the unknown isFundamental subjectGroup
												
												subjectGroup = subjectGroupDao.getUnkownSubjectGroup(careerCodeT, false);
												
												if(subjectGroup != null){										
													cV = new ComplementaryValues(career, subject, SomosUNUtils.getTypology("d"), false, subjectGroup);
													cV.setId(complementaryValuesDao.generateId());
													complementaryValuesDao.saveComplementaryValues(cV);
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
	public ComplementaryValues createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student) {
		
		ComplementaryValues complementaryValues = null;
		
		if(subjectGroupName.isEmpty() == false && credits.isEmpty() == false && careerCode.isEmpty() == false && student != null){
			StudentDao studentDao = new StudentDao();
			student = studentDao.getStudentByIdSun(student.getIdSun());
			if(student != null){
				if(student.isAdmin() == true){
					
					CareerDao careerDao = new CareerDao();
					SubjectDao subjectDao = new SubjectDao();
					ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
					SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
					
					Career career = careerDao.getCareerByCode(careerCode);

					if(career!= null){
						
						Subject subjectDefault = null;
						SubjectGroup subjectGroup = null;
						
						if(subjectGroupName.equals(SomosUNUtils.LIBRE_CODE) == true){
							subjectDefault = new Subject(Integer.valueOf(credits), SomosUNUtils.LIBRE_CODE, SomosUNUtils.LIBRE_CODE, SomosUNUtils.LIBRE_NAME, "bog");
							subjectGroup = subjectGroupDao.getSubjectGroup(SomosUNUtils.LIBRE_AGRUPACION_NAME, careerCode);
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
							
							complementaryValues = new ComplementaryValues(career, subjectDefault, t, false, subjectGroup);
							complementaryValues.setId(complementaryValuesDao.generateId());
							complementaryValuesDao.saveComplementaryValues(complementaryValues);
							
						}
						
						
						
						
						
					}
					
				}
			}
		}
		
		return complementaryValues;
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

		log.info("hola ... SUNServiceImpl");
		
		Career c = null;
		CareerDao careerDao = new CareerDao();
		
		if(careerCode != null){
			c = careerDao.getCareerByCode(careerCode);
		}
		
		if(c != null && c.hasAnalysis() == false){
			analyzeCareer(c.getCode());
			
			c = careerDao.getCareerByCode(careerCode);
			
		}
		
		return c; 
	}

	@Override
	public List<ComplementaryValues> getMandatoryComplementaryValues(String careerCode) {
		
		List<ComplementaryValues> listToReturn = null;
		
		if(careerCode != null){
			ComplementaryValuesDao cVDao = new ComplementaryValuesDao();
			List<ComplementaryValues> cVListT = cVDao.getMandatoryComplementaryValues(careerCode);
			
			if(cVListT != null && cVListT.size() > 0){
				listToReturn = new ArrayList<ComplementaryValues>();
				for(ComplementaryValues cVT : cVListT){
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

	
}
