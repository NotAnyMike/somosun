package com.somosun.plan.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.Deferred;
import com.somosun.plan.server.SiaProxy;
import com.somosun.plan.server.SomosUNServerUtils;
import com.somosun.plan.server.dummy.SemesterDummy;
import com.somosun.plan.server.dummy.SubjectDummy;
import com.somosun.plan.server.serviceImpl.LoginServiceImpl;
import com.somosun.plan.server.serviceImpl.SUNServiceImpl;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.values.TypologyCodes;

public class PlanDao implements Dao<Plan>{
	
	private static final Logger log = Logger.getLogger("PlanDao");
	
	private final String ECONOMIA = "{'sede':'bog','name':'Economía','code':'2522','lvl':'pre','semesters':[{'number':1,'courses':[{'code':'1000004','normal':true,'type':'l','oblig':true},{'code':'2016007','normal':true,'type':'l','oblig':true},{'code':'2016008','normal':true,'type':'l','oblig':true},{'code':'2015270','normal':true,'type':'l','oblig':true},{'code':'2016015','normal':true,'type':'l','oblig':true},{'code':'1000044','normal':true,'type':'n','oblig':true}]},{'number':2,'courses':[{'code':'2016021','normal':true,'type':'l','oblig':true},{'code':'2016011','normal':true,'type':'c','oblig':true},{'code':'2016017','normal':true,'type':'c','oblig':true},{'code':'2016012','normal':true,'type':'c','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000045','normal':true,'type':'n','oblig':true}]},{'number':3,'courses':[{'code':'2016020','normal':true,'type':'c','oblig':true},{'code':'2016009','normal':true,'type':'c','oblig':true},{'code':'2016018','normal':true,'type':'c','oblig':true},{'code':'2016013','normal':true,'type':'c','oblig':true},{'code':'optativa contenido cualitativo','normal':false,'credits':3,'type':'l','oblig':false},{'code':'1000046','normal':true,'type':'n','oblig':true}]},{'number':4,'courses':[{'code':'2016002','normal':true,'type':'c','oblig':true},{'code':'2016005','normal':true,'type':'c','oblig':true},{'code':'2016014','normal':true,'type':'c','oblig':true},{'code':'2016019','normal':true,'type':'c','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000047','normal':true,'type':'n','oblig':true},]},{'number':5,'courses':[{'code':'2016022','normal':true,'type':'c','oblig':true},{'code':'2016023','normal':true,'type':'c','oblig':true},{'code':'2016003','normal':true,'type':'c','oblig':true},{'code':'2016006','normal':true,'type':'c','oblig':true},{'code':'2016001','normal':true,'type':'c','oblig':true},]},{'number':6,'courses':[{'code':'2016004','normal':true,'type':'c','oblig':true},{'code':'2016010','normal':true,'type':'c','oblig':true},{'code':'2016024','normal':true,'type':'c','oblig':true},{'code':'2016016','normal':true,'type':'l','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false}]},{'number':7,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':8,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':9,'courses':[{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':10,'courses':[{'code':'2015300','normal':true,'type':'c','oblig':true}]}]}]}";      
	private final String CAREERS_DEFAULTS[] = {ECONOMIA};
	
	static{
		ObjectifyService.register(Plan.class);
	}
	
	public Plan createPlanFromDefaultString(String careerCode){
		
		CareerDao careerDao = new CareerDao();
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
		
		Plan plan = new Plan();
		Career career = null;
		String sede = "";
		String name = "";
		List<Semester> semesters = new ArrayList<Semester>();
		Semester semester = null;
		List<SubjectValue> subjects = null;
		Subject subject = null;
		ComplementaryValue complementaryValue = null;
		SubjectValue subjectValues = null;
		HashMap<SubjectValue, Subject> subjectMap = new HashMap<SubjectValue, Subject>();;
	
		JSONObject jsonPlan = new JSONObject(CAREERS_DEFAULTS[getCareerIndex(careerCode)]);
		JSONArray jsonSemesters = jsonPlan.getJSONArray("semesters");
		JSONArray jsonSubjects = null;
		JSONObject jsonSubject= null;
		
		//TODO construct the plan from zero
		
		career = careerDao.getByCode(jsonPlan.getString("code"));		
		sede = jsonPlan.getString("sede"); //sede is included in the code of the career
		name = jsonPlan.getString("name");
		
		for(int i = 0; i < jsonSemesters.length(); i++){
			semester = new Semester(/*Integer.toString(i)*/);
			jsonSubjects = jsonSemesters.getJSONObject(i).getJSONArray("courses");
			subjects = new ArrayList<SubjectValue>();
			
			for(int j = 0; j < jsonSubjects.length(); j++){
				jsonSubject = jsonSubjects.getJSONObject(j);
				if(jsonSubject.getBoolean("normal") == true){
					subject = subjectDao.getByCode(jsonSubject.getString("code"));
				}else{
					subject = new Subject(jsonSubject.getInt("credits"), jsonSubject.getString("code"), jsonSubject.getString("code"), jsonSubject.getString("code"), sede);
				}
				subjectValues = new SubjectValue();
				//getting the complementary values
				if(subject!= null){
					complementaryValue = complementaryValueDao.get(career, subject);
					if(complementaryValue != null) {subjectValues.setComplementaryValue(complementaryValue);}
					else 
					{
						complementaryValue = new ComplementaryValue(career, subject);
						subjectValues.getComplementaryValue().setMandatory(jsonSubject.getBoolean("oblig"));
						subjectValues.getComplementaryValue().setTypology(jsonSubject.getString("type"));
						if(jsonSubject.getBoolean("normal") == true && subject != null) complementaryValueDao.save(complementaryValue);
					}
				}				
				
				if(subject != null && subjectValues != null) {
					subjects.add(subjectValues);
					subjectMap.put(subjectValues, subject);
				}
			}
			semester.setSubjects(subjects);
			semesters.add(semester);
		}
		//plan.setValuesAndSubjectMap(subjectMap);
		plan.setSemesters(semesters);
		
		if(career != null)	plan.setCareer(career);
		return plan;
	}

	private int getCareerIndex(String careerCode) {
		int intToReturn = 0;
		switch(careerCode){
		case "2543":
			intToReturn = 0;
			break;
		}
		return intToReturn;
	}
	
	public Long save(Plan plan){
		
		Long id = null;
		
		if(plan != null){
			if(plan.getCareer() != null){
				//save everything inside
				
				SemesterDao semesterDao = new SemesterDao();
				ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
				SubjectValueDao subjectValueDao = new SubjectValueDao();
				SubjectDao subjectDao = new SubjectDao();
//				GroupDao groupDao = new GroupDao();
//				BlockDao blockDao = new BlockDao();
//				TeacherDao teacherDao = new TeacherDao();
//				SemesterValueDao  semesterValueDao = new SemesterValueDao();

				List<Semester> semesters = plan.getSemesters();
				List<SubjectValue> sVToRemoveList = new ArrayList<SubjectValue>();
				
				for(Semester s : semesters){
					//be careful because I can be saving more than once the same semester
					sVToRemoveList.clear();

					List<SubjectValue> subjectValuesList = s.getSubjects();
					
						if(subjectValuesList != null){
							if(subjectValuesList.size() != 0){
								for(SubjectValue sV : subjectValuesList){
									
										ComplementaryValue cV = sV.getComplementaryValue();
										if(cV != null){
											
												if(cV.getCareer() != null && cV.getSubject() != null){

													Subject s2 = cV.getSubject();
													if(s2 != null){
														if(s2.getId() == null){
															Subject sT = null;
															if(s2.getCode().isEmpty() == false) sT = subjectDao.getByCode(s2.getCode());
															else sT = subjectDao.getByName(s2.getName());
															
															if(sT.getId() == null){
																s2.setId(subjectDao.generateId());
																subjectDao.save(s2);																
															}
														}
													}
													
													List<Subject> requisitesList = cV.getListCorequisites();
													requisitesList.addAll(cV.getListCorequisitesOf());
													requisitesList.addAll(cV.getListPrerequisites());
													requisitesList.addAll(cV.getListPrerequisitesOf());
													
													for(Subject s3 : requisitesList){
														if(s3 != null && s3.getId() == null){
															
															Subject sT = null;
															if(s3.getCode().isEmpty() == false) sT = subjectDao.getByCode(s3.getCode());
															else sT = subjectDao.getByName(s3.getName());
															
															//OLD if(sR == null){
															if(s3.getId() == null){
																s3.setId(subjectDao.generateId());
																subjectDao.save(s3);																
															}
															
														}
													}
													
													if(cV.getId() == null){														
														cV.setId(complementaryValueDao.generateId());
													}
													complementaryValueDao.save(cV);
												}else{
													sV.setComplementaryValue(null);
												}
											
											if(sV.getId() == null){												
												sV.setId(subjectValueDao.generateId()); 
											}
											subjectValueDao.save(sV);
										}else{
											sVToRemoveList.add(sV);
										}
									
								}
							}
						}
						
						if(s.getId() == null){
							s.setId(semesterDao.generateId());
						}
						semesterDao.save(s);
					
					
						for(SubjectValue sVT : sVToRemoveList){						
							s.getSubjects().remove(sVT);
						}
					
				}
				
				if(plan.getId() == null){
					plan.setId(generateId());
				}
				//TODO not sure of the functionality of Defer
				ofy().defer().save().entity(plan);
				id = plan.getId();
			}
		}
		
		return id;
		
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Plan> key = f.allocateId(Plan.class);
		return key.getId();
	}

	public Plan getPlanDefault(String careerCode) {
		Plan p = (Plan) ofy().load().type(Plan.class).filter("isDefault", true).filter("career.code", careerCode).first().now();
		if(p != null){			
			p.setUser(null);
			p.setDefault(false);
			p.setId(null);
			if(p != null){
				for(Semester s : p.getSemesters()){
					s.setId(null);
					for(SubjectValue sV : s.getSubjects()){
						sV.setId(null);
					}
				}
			}
		}
		return p;
	}
	
	/**
	 * This method will only delete the plan entity, will not delete the semesters nor the subjectValues in it.
	 * 
	 * @param id
	 */
	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<Plan> key = Key.create(Plan.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}

	public List<Plan> getPlanByUser(Student s) {
		
		List<Plan> plans = null;
		
		if(s != null && s.getIdSun() != null){
			plans = ofy().load().type(Plan.class).filter("user.idG", s.getIdG()).list();
		}
		return plans;
	}

	public Plan getById(Long planId) {
		Plan pToReturn = null;
		if(planId != null){	
			Key<Plan> key = Key.create(Plan.class, planId);
			pToReturn = ofy().load().key(key).now();
			//pToReturn = ofy().load().type(Plan.class).id(planId).now();
		}
		return pToReturn;
	}

	public void delete(Plan plan) {
		if(plan != null){
			SubjectDao subjectDao = new SubjectDao();
			ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
			SemesterDao semesterDao = new SemesterDao();
			SubjectValueDao subjectValueDao = new SubjectValueDao();
			List<Semester> semesters = plan.getSemesters();
			if(semesters != null){
				for(Semester semester : semesters){
					List<SubjectValue> subjectValues = semester.getSubjects();
					for(SubjectValue subjectValue : subjectValues){
						ComplementaryValue complementaryValue = subjectValue.getComplementaryValue();
						
						if(complementaryValue != null){						
							Subject subject = complementaryValue.getSubject();
							if(subject != null && subject.isDefault() == true){
								
								//delete all the Default Subjects
								subjectDao.delete(subject.getId());
								if(subject.getId() == null) {
									log.severe("Subject id:" + subject.getCode() + " name: " + subject.getName() + " has no id");
								}
								//delete all the complementaryValues
								complementaryValueDao.delete(complementaryValue.getId());
							}
							//Delete subjectValue
							subjectValueDao.delete(subjectValue.getId());
						}
					}
					//delete all the semesters
					semesterDao.delete(semester.getId());
				}
			}
			//Delete the plan
			delete(plan.getId());
			
//			if(plan.getSemesters() != null && plan.getSemesters().size() > 0){
//				
//				SemesterDao semesterDao = new SemesterDao();
//				SubjectValueDao subjectValueDao = new SubjectValueDao();
//				
//				for(Semester s : plan.getSemesters()){
//					if(s.getSubjects() != null && s.getSubjects().size() > 0){
//						for(SubjectValue sV : s.getSubjects()){
//							if(sV.getId() != null && sV.getId().equals("") == false){
//								subjectValueDao.deleteSubjectValue(sV.getId());
//							}
//						}
//					}
//					if(s.getId() != null && s.getId().equals("") == false){					
//						semesterDao.deleteSemester(s.getId());
//					}
//				}
//			}			
//			if(plan.getId() != null && plan.getId().equals("") == false){				
//				deletePlan(plan.getId());
//			}
		}
	}

	public Plan generatePlanFromAcademicHistory(String academicHistory) {
		
		Plan planToReturn = null;

		if(academicHistory != null){
			
			String siaString = SomosUNUtils.removeAccents(academicHistory);
			if(siaString.contains("mi historia academica") == true){
				siaString = siaString.substring(siaString.lastIndexOf("mi historia academica"));
			}
			if(siaString.contains("promedio academico")== true){
				siaString = siaString.substring(0, siaString.indexOf("promedio academico"));
			}
			
			Pattern newLine = Pattern.compile("\n");
			String lines[] = newLine.split(siaString);
			
			// Defining patterns
			
			String careerLinePattern = "\\d+ . .*";
			String semesterLinePattern = "  \\d{2}\tperiodo academico . \\d+-i+.+";
			//String approvedLinePattern = "(no )*aprobado";
			//String subjectLinePattern = "(\\d+)-(\\d+)\\t(.+)\\t(\\d+)\\t(\\d+)\\t(\\d+)\\t([a-z]+)\\t(\\d+)\\t(\\d+)\\t{2}\\d+\\p{Punct}\\d+.*";
			//String subjectLinePattern = "(\\d+)-(\\d+)\\t(.+)\\t(\\d+)\\t(\\d+)\\t(\\d+)\\t([a-z]+)\\t(\\d+)\\t(\\d+)\\t(\\t\\d+\\p{Punct}\\d+)|(.+\\t((ap)|(na))).*";
			String subjectLinePattern = "(\\d+)-(\\d+)(.){0,3}\\t(.+)\\t(\\d+)\\t(\\d+)\\t(\\d+)\\t([a-z]+)\\t(\\d+)\\t(\\d+)\\t(\\t\\d+\\p{Punct}\\d+)|(.+\\t((ap)|(na))).*"; // the (.){0,3} in the case of groups as 33c in the case of felipe
			
			String careerCode = null;
			SemesterDummy semesterD = null;
			List<SemesterDummy> semesters = new ArrayList<SemesterDummy>();
			
			for(String s : lines){
				
				SubjectDummy subjectD = null;
				
				boolean careerBoolean = Pattern.matches(careerLinePattern,s);
				boolean semesterBoolean = Pattern.matches(semesterLinePattern,s);
				boolean subjectBoolean = Pattern.matches(subjectLinePattern,s);
				
				//Save the career
				if(careerBoolean == true && careerCode == null){
					careerCode = s.substring(0, s.indexOf("|")).trim();
				}
	
				//Save the semester
				if(semesterBoolean == true){
					semesterD = generateSemesterDummy(s);
					semesters.add(semesterD);
				}
				
				//detect subject
				if(subjectBoolean == true && semesterD != null){
					assert subjectD != null;
					
					subjectD = generateSubjectDummy(s);
					semesterD.addSubjectDummy(subjectD);
					
				}
				
			}
			
			planToReturn = generatePlanFromDummies(careerCode, semesters);
		}
		
		return planToReturn;
	}
	
	private Plan generatePlanFromDummies(String careerCode, List<SemesterDummy> semestersD) {
		
		long totalStartTime = System.nanoTime();
		
		Plan planToReturn = null;
		
		//getCareer
		CareerDao careerDao = new CareerDao();
		Career career = careerDao.getByCode(careerCode);
		
		if(career != null){
//			if(career.hasAnalysis() == false){
//				SiaProxy.getRequisitesForACareer(career.getCode());
//			}
			
			//getUser
			LoginServiceImpl login = new LoginServiceImpl();
			Student student = login.login("").getStudent();
			
			//name
			String name = "Historia académica - " + career.getName() + " " + new Date().toString();
			
			//isDefault
			boolean isDefault = false;
			
			//semesters list of subjectValues
			SubjectDao subjectDao = new SubjectDao();
			SubjectValueDao subjectValueDao = new SubjectValueDao();
			GroupDao groupDao = new GroupDao();
			SemesterDao semesterDao = new SemesterDao();
			SemesterValueDao semesterValueDao = new SemesterValueDao();
			ComplementaryValueDao complementaryValueDao = new ComplementaryValueDao();
			
			List<Semester> semesters = new ArrayList<Semester>();
			List<SubjectDummy> problematicSubjects = new ArrayList<SubjectDummy>();
			List<Subject> subjectsToUpdate = new ArrayList<Subject>();
			for(SemesterDummy semesterD : semestersD){
				Semester semester = new Semester();
				semester.setId(semesterDao.generateId());
				semesters.add(semester);
				
				//Add the info to the semester
//				semester.setSemesterValue(/*semesterD.getYear() + "-" + semesterD.getSemester()*/);
				
				//for subjects
				List<SubjectValue> subjects = new ArrayList<SubjectValue>();
				for(SubjectDummy subjectD : semesterD.getSubjects()){
					
					//Get subject
					Subject subject = subjectDao.getByCode(subjectD.getCode());
					//Create the subject
					assert subject != null;
					
					if(subject != null){
						SubjectValue subjectV = new SubjectValue();
						subjectV.setId(subjectValueDao.generateId());
						
						//  Get semesterValue
						SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(semesterD.getYear(), semesterD.getSemester());

						//Get group
						Group group = groupDao.getOrCreateGroup(subject, semesterValue, subjectD.getGroup());
						//  Add career to careers list
						if(group.containsCareer(career.getCode()) == false){
							group.addCareer(career);
						}						
						
						//Get complementaryValue
						ComplementaryValue complementaryValue = complementaryValueDao.get(careerCode, subject.getCode());
						if(complementaryValue != null){
							
							subjectV.setComplementaryValue(complementaryValue);
							subjectV.setGrade(subjectD.getGrade());
							if(subjectD.getApproved()){								
								subject.setApprovenType(true);
								subjectsToUpdate.add(subject);
							}
							subjectV.setTaken(true);
							subjectV.setGroup(group);
							
							subjects.add(subjectV);
							
						}else{
							//complementaryValuesNotFound.add(subjectD);
							problematicSubjects.add(subjectD);
						}
					}else{
						//subjectsNotFound.add(subjectD);
						problematicSubjects.add(subjectD);
					}
					
				}
				
				semester.setSubjects(subjects);
				
			}
			
			long problematicStartTime = System.nanoTime();
			long siaSearchStartTime = 0;
			long siaSearchEndTime = 0;
			
			List<SubjectDummy> subjectToBeDummy = new ArrayList<SubjectDummy>();
			if(problematicSubjects.size() > 0){
				
				List<ComplementaryValue> complementaryValuesProblematics = null;
				List<String> subjectCodes = new ArrayList<String>();
				List<String> careerCodes = new ArrayList<String>(); //in order to be send to the function in the siaProxy
				for(SubjectDummy subjectDT : problematicSubjects){
					subjectCodes.add(subjectDT.getCode());
					careerCodes.add(career.getCode());
				}

				//Get the list from the sia search
				//complementaryValues not found
				//Save the cV from the sia search result
				
				//subjectsNotFound
				//Save the subject from the siaSearch result
				//Save the cV from the sia Search resutl

				//getComplementaryValues
				

					siaSearchStartTime = System.nanoTime();
				SUNServiceImpl service = new SUNServiceImpl();
				complementaryValuesProblematics = service.getComplementaryValues(subjectCodes, careerCodes, careerCode);
					siaSearchEndTime = System.nanoTime();
				
				//Add the cV to the subjectValue 
				//add it to the subject and add it to a semester in the plan
				
				for(ComplementaryValue complementaryValuesT : complementaryValuesProblematics){
					
					SubjectDummy subjectDummyT = getSubjectDummy(problematicSubjects, complementaryValuesT.getSubject().getCode());
					assert subjectDummyT != null;
					
					if(subjectDummyT != null){
						SemesterDummy semesterDummyT = getSemesterDummy(semestersD, subjectDummyT.getCode());
						assert semesterDummyT != null;
						
						if(semesterDummyT != null){
							
							SubjectValue subjectValuesT = new SubjectValue();
							subjectValuesT.setId(subjectValueDao.generateId());
							
							subjectValuesT.setComplementaryValue(complementaryValuesT);
							subjectValuesT.setGrade(subjectDummyT.getGrade());
							if(subjectDummyT.getApproved() == true){
								complementaryValuesT.getSubject().setApprovenType(true);
								subjectsToUpdate.add(complementaryValuesT.getSubject());
							}
							
							SemesterValue semesterValueT = semesterValueDao.getOrCreateSemester(semesterDummyT.getYear(), semesterDummyT.getSemester());
							Group groupT = groupDao.getOrCreateGroup(complementaryValuesT.getSubject(), semesterValueT, subjectDummyT.getGroup());
							subjectValuesT.setGroup(groupT);
							
							subjectValuesT.setTaken(true);
							
							Semester semester = semesters.get(semesterDummyT.getPosition()-1);
							semester.addSubject(subjectValuesT);
							
						}
					}
					
				}
				
				//if there is any subjectDummy left, e.g. when people takes twice the same class (failed one) and was a "nonFoundSubject"
				if(problematicSubjects.size() > 0){
					for(SubjectDummy subjectDummyT : problematicSubjects){
						
						boolean isProblematic = false;
						
						//new subjectValue
						SubjectValue subjectValuesT = new SubjectValue();
						subjectValuesT.setId(subjectValueDao.generateId());
						
						//get complementaryValue
						ComplementaryValue complementaryValuesT = complementaryValueDao.get(career.getCode(), subjectDummyT.getCode());
						
						if(complementaryValuesT != null){

							//get semesterDummy
							SemesterDummy semesterDummyT = getSemesterDummy(semestersD, subjectDummyT.getCode());
							
							if(semesterDummyT != null){
								SemesterValue semesterValueT = semesterValueDao.getOrCreateSemester(semesterDummyT.getYear(), semesterDummyT.getSemester());
								Group groupT = groupDao.getOrCreateGroup(complementaryValuesT.getSubject(), semesterValueT, subjectDummyT.getGroup());
								
								subjectValuesT.setComplementaryValue(complementaryValuesT);
								subjectValuesT.setGroup(groupT);
								subjectValuesT.setGrade(subjectDummyT.getGrade());
								if(subjectDummyT.getApproved() == true){
									complementaryValuesT.getSubject().setApprovenType(true);
									subjectsToUpdate.add(complementaryValuesT.getSubject());
								}
								subjectValuesT.setTaken(true);
								
								Semester semester = semesters.get(semesterDummyT.getPosition()-1);
								
								if(semester != null){
									semester.addSubject(subjectValuesT);
								}
							}else{
								isProblematic = true;
							}
						}else{
							isProblematic = true;
						}
						if(isProblematic){							
							subjectToBeDummy.add(subjectDummyT);
						}
					}
				}
			}
			
			if(subjectToBeDummy.size() > 0){
				/************************ taking care of the old subjects *************************/
				SubjectGroupDao subjectGroupDao = new SubjectGroupDao();
				
				for(SubjectDummy subjectD : subjectToBeDummy){
					
					SemesterDummy semesterDummyT = getSemesterDummy(semestersD, subjectD.getCode());
					assert semesterDummyT != null;
					
					if(semesterDummyT != null){
						
						int credits = subjectD.getCredits();
						String code = subjectD.getCode();
						String siaCode = null;
						String nameSubject = subjectD.getName();
						String location ="bog";
						Subject subject = new Subject(credits, code, siaCode, nameSubject, location);
						subject.setDummy(true);
						subject.setId(subjectDao.generateId());
						
						boolean mandatory = false;
						String typology = TypologyCodes.getTypology(subjectD.getTypology());
						SubjectGroup subjectGroup = subjectGroupDao.getSubjectGroupFromTypology(career, typology);
						ComplementaryValue complementaryValue = new ComplementaryValue(career, subject, typology, mandatory, subjectGroup);
						complementaryValue.setId(complementaryValueDao.generateId());
						
						SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(semesterDummyT.getYear(), semesterDummyT.getSemester());
						Group group = groupDao.getOrCreateGroup(subject, semesterValue, subjectD.getGroup());
						
						SubjectValue subjectValuesT = new SubjectValue();
						subjectValuesT.setId(subjectValueDao.generateId());
						subjectValuesT.setComplementaryValue(complementaryValue);
						subjectValuesT.setGrade(subjectD.getGrade());
						if(subjectD.getApproved() == true){
							subject.setApprovenType(true);
						}
						subjectValuesT.setGroup(group);
						subjectValuesT.setTaken(true);
						
						subjectDao.save(subject);
						complementaryValueDao.save(complementaryValue);
						subjectValueDao.save(subjectValuesT);
						
						Semester semester = semesters.get(semesterDummyT.getPosition()-1);

						if(semester != null){
							semester.addSubject(subjectValuesT);
						}
						
					}
				}
				
				/**********************************************************************************/				
			}
			
			//update subjects to update
			for(Subject subject : subjectsToUpdate){
				subjectDao.save(subject);
			}
			
			//Create a task and add it to the cron job
			for(Semester semesterTemporary : semesters){
				for(SubjectValue subjectValue : semesterTemporary.getSubjects()){
					SomosUNServerUtils.createGradeUpdaterTask(subjectValue.getGroup().getTeacher(), subjectValue.getGroup().getSemesterValue(), null, subjectValue.getGrade(), subjectValue.getComplementaryValue().getSubject());					
				}
			}
			
			PlanDao planDao = new PlanDao();
			
			planToReturn = new Plan();
			planToReturn.setId(planDao.generateId());
			
			planToReturn.setName(name);
			planToReturn.setCareer(career);
			planToReturn.setUser(student);
			planToReturn.setDefault(isDefault);
			planToReturn.setSemesters(semesters);
			
			planDao.save(planToReturn);
			
			long totalEndTime = System.nanoTime();
			
			long totalDuration = (totalEndTime - totalStartTime)/1000000;
			long normalDuration = (problematicStartTime - totalStartTime)/1000000;
			long problematicDuration = (totalEndTime - problematicStartTime)/1000000;
			long siaTotalSearchDuration = (siaSearchEndTime - siaSearchStartTime)/1000000;
			
			Double normalPercentage = ((double) normalDuration / totalDuration)*100;
			Double problematicPercentage = ((double) problematicDuration/totalDuration)*100;
			Double siaSearchPercentage = ((double) siaTotalSearchDuration / problematicDuration)*100;
			
			log.info("GeneratePlanFromDummies - problematic took "+ problematicDuration + "ms (" + problematicPercentage + "%)");
			log.info("Searching the sia total took " + siaTotalSearchDuration + "ms (" + siaSearchPercentage  +"% of problematic)");
			log.info("The other methods took " + normalDuration + "ms (" + normalPercentage  + "%)");
			log.info("total time was " + totalDuration + "ms (100%)" );
			
		}
		
		return planToReturn;
	}

	private SemesterDummy getSemesterDummy(List<SemesterDummy> semestersD, String code) {
		
		SemesterDummy semesterDummy = null;
		
		for(SemesterDummy semesterDummyT : semestersD){
			SubjectDummy subjectDummyT = getSubjectDummy(semesterDummyT.getSubjects(), code);
			if(subjectDummyT != null){
				semesterDummy = semesterDummyT;
				break;
			}
		}
		
		return semesterDummy;
	}

	/**
	 * This will remove the subjectDummy selected from the list, to avoid sending the same subjectDummy if the user repeated it
	 * 
	 * @param listToSearch
	 * @param codeToFind
	 * @return
	 */
	private SubjectDummy getSubjectDummy(List<SubjectDummy> listToSearch, String codeToFind) {
		SubjectDummy subjectDummy = null;
		
		for(SubjectDummy subjectDummyT : listToSearch){
			if(subjectDummyT.getCode().equals(codeToFind) == true){
				subjectDummy = subjectDummyT;
				listToSearch.remove(subjectDummyT);
				break;
			}
		}
			
		return subjectDummy;
	}

	private SemesterDummy generateSemesterDummy(String s) {
		SemesterDummy semesterD = null;
		
		String positionString = s.substring(0, s.indexOf("\t")).trim();
		String yearString = s.substring(s.indexOf("|")+1, s.indexOf("-")).trim();
		String semesterString = s.substring(s.indexOf("-")+1, s.length()).trim();
		
		int semesterInt = 0;
		if(semesterString.equals("i")) semesterInt = 1;
		else if(semesterString.equals("ii")) semesterInt = 2;
		else if(semesterString.equals("iii")) semesterInt = 3;
		
		semesterD = new SemesterDummy(
				Integer.valueOf(positionString),
				Integer.valueOf(yearString),
				semesterInt);
		
		
		return semesterD;
	}

	private SubjectDummy generateSubjectDummy(String s) {
		SubjectDummy subjectD = null;
		
		String codeString = s.substring(0, s.indexOf("-")).trim();
		String groupString = s.substring(s.indexOf("-")+1, s.indexOf("\t")).trim();
		s = s.substring(s.indexOf("\t") +1);
	
		String nameString = s.substring(0, s.indexOf("\t")).trim();
		
		s = s.substring(s.indexOf("\t") +2 );
		s = s.substring(s.indexOf("\t")+1);
		s = s.substring(s.indexOf("\t")+1);
		s = s.substring(s.indexOf("\t")+1);
		
		String typoString = s.substring(0,s.indexOf("\t")).trim();
		s = s.substring(s.indexOf("\t")+1);
		String creditsString = s.substring(0,s.indexOf("\t")).trim();
		String gradeString = s.substring(s.lastIndexOf("\t")+1).trim();
		
		subjectD = new SubjectDummy();
		subjectD.setCode(codeString);
		int groupInt = 0;
		if(groupString.matches("(\\d+)")==true){
			groupInt = Integer.valueOf(groupString);
		}
		subjectD.setGroup(groupInt);
		subjectD.setName(nameString);
		subjectD.setTypology(typoString);
		subjectD.setCredits(Integer.valueOf(creditsString));
		if(gradeString.equals("ap")){
			subjectD.setApproved(true);
			subjectD.setGrade(3.0);
		}else if(gradeString.equals("na")){
			subjectD.setApproved(false);
			subjectD.setGrade(0.0);
		}else{
			subjectD.setApproved(false);
			subjectD.setGrade(Double.valueOf(gradeString));
		}
		
		
		return subjectD;
	}
	
	public void deleteAllDefaultPlans() {
		ofy().transact(new VoidWork(){
			public void vrun() {
				List<Plan> plans = getAllDefaultPlans();
				for(Plan plan : plans){
					delete(plan);
					CareerDao careerDao = new CareerDao();
					Career career = plan.getCareer();
					career.setHasDefault(false);
					careerDao.save(career);
				}
				log.warning("All default plans were delted");
			}
		});
	}
	
	private List<Plan> getAllDefaultPlans() {
		List<Plan> plans = ofy().load().type(Plan.class).filter("isDefault", true).list();
		return plans;
	}

	public void deleteDefaultPlan(final String careerCode) {
		final Plan plan = getPlanDefault(careerCode);
		if(plan != null){
			delete(plan);
			CareerDao careerDao = new CareerDao();
			Career career = careerDao.getByCode(careerCode);
			career.setHasDefault(false);
			careerDao.save(career);
		}
	}

	public void deleteAllPlans() {
		final List<Plan> plans = getAllPlans();
		for(Plan plan : plans){
			delete(plan);
		}
		log.warning("All plans where deleted");
	}

	private List<Plan> getAllPlans() {
		return ofy().load().type(Plan.class).list();
	}

	/**
	 * if @param username == null or isEmpty() then will search for plans with a null user
	 * @param username
	 * @return
	 */
	public List<Plan> getPlansByUsername(String username) {
		List<Plan> toReturn = null;
		if(username == null || username.isEmpty()){
			toReturn = ofy().load().type(Plan.class).filter("user", null).list();
		}else{			
			toReturn = ofy().load().type(Plan.class).filter("user.username", username).list();
		}
		return toReturn;
	}
}
