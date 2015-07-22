package com.uibinder.server.dao;

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
import com.uibinder.server.SiaProxy;
import com.uibinder.server.dummy.SemesterDummy;
import com.uibinder.server.dummy.SubjectDummy;
import com.uibinder.server.serviceImpl.LoginServiceImpl;
import com.uibinder.server.serviceImpl.SUNServiceImpl;
import com.uibinder.shared.SomosUNUtils;
import com.uibinder.shared.control.Career;
import com.uibinder.shared.control.ComplementaryValues;
import com.uibinder.shared.control.Group;
import com.uibinder.shared.control.Plan;
import com.uibinder.shared.control.Semester;
import com.uibinder.shared.control.SemesterValue;
import com.uibinder.shared.control.Student;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectGroup;
import com.uibinder.shared.control.SubjectValues;
import com.uibinder.shared.values.TypologyCodes;

public class PlanDao {
	
	private static final Logger log = Logger.getLogger("PlanDao");
	
	private final String ECONOMIA = "{'sede':'bog','name':'Economía','code':'2522','lvl':'pre','semesters':[{'number':1,'courses':[{'code':'1000004','normal':true,'type':'l','oblig':true},{'code':'2016007','normal':true,'type':'l','oblig':true},{'code':'2016008','normal':true,'type':'l','oblig':true},{'code':'2015270','normal':true,'type':'l','oblig':true},{'code':'2016015','normal':true,'type':'l','oblig':true},{'code':'1000044','normal':true,'type':'n','oblig':true}]},{'number':2,'courses':[{'code':'2016021','normal':true,'type':'l','oblig':true},{'code':'2016011','normal':true,'type':'c','oblig':true},{'code':'2016017','normal':true,'type':'c','oblig':true},{'code':'2016012','normal':true,'type':'c','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000045','normal':true,'type':'n','oblig':true}]},{'number':3,'courses':[{'code':'2016020','normal':true,'type':'c','oblig':true},{'code':'2016009','normal':true,'type':'c','oblig':true},{'code':'2016018','normal':true,'type':'c','oblig':true},{'code':'2016013','normal':true,'type':'c','oblig':true},{'code':'optativa contenido cualitativo','normal':false,'credits':3,'type':'l','oblig':false},{'code':'1000046','normal':true,'type':'n','oblig':true}]},{'number':4,'courses':[{'code':'2016002','normal':true,'type':'c','oblig':true},{'code':'2016005','normal':true,'type':'c','oblig':true},{'code':'2016014','normal':true,'type':'c','oblig':true},{'code':'2016019','normal':true,'type':'c','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000047','normal':true,'type':'n','oblig':true},]},{'number':5,'courses':[{'code':'2016022','normal':true,'type':'c','oblig':true},{'code':'2016023','normal':true,'type':'c','oblig':true},{'code':'2016003','normal':true,'type':'c','oblig':true},{'code':'2016006','normal':true,'type':'c','oblig':true},{'code':'2016001','normal':true,'type':'c','oblig':true},]},{'number':6,'courses':[{'code':'2016004','normal':true,'type':'c','oblig':true},{'code':'2016010','normal':true,'type':'c','oblig':true},{'code':'2016024','normal':true,'type':'c','oblig':true},{'code':'2016016','normal':true,'type':'l','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false}]},{'number':7,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':8,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':9,'courses':[{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':10,'courses':[{'code':'2015300','normal':true,'type':'c','oblig':true}]}]}]}";      
	private final String CAREERS_DEFAULTS[] = {ECONOMIA};
	
	static{
		ObjectifyService.register(Plan.class);
	}
	
	public Plan createPlanFromDefaultString(String careerCode){
		
		CareerDao careerDao = new CareerDao();
		SubjectDao subjectDao = new SubjectDao();
		ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
		
		Plan plan = new Plan();
		Career career = null;
		String sede = "";
		String name = "";
		List<Semester> semesters = new ArrayList<Semester>();
		Semester semester = null;
		List<SubjectValues> subjects = null;
		Subject subject = null;
		ComplementaryValues complementaryValues = null;
		SubjectValues subjectValues = null;
		HashMap<SubjectValues, Subject> subjectMap = new HashMap<SubjectValues, Subject>();;
	
		JSONObject jsonPlan = new JSONObject(CAREERS_DEFAULTS[getCareerIndex(careerCode)]);
		JSONArray jsonSemesters = jsonPlan.getJSONArray("semesters");
		JSONArray jsonSubjects = null;
		JSONObject jsonSubject= null;
		
		//TODO construct the plan from zero
		
		career = careerDao.getCareerByCode(jsonPlan.getString("code"));		
		sede = jsonPlan.getString("sede"); //sede is included in the code of the career
		name = jsonPlan.getString("name");
		
		for(int i = 0; i < jsonSemesters.length(); i++){
			semester = new Semester(Integer.toString(i));
			jsonSubjects = jsonSemesters.getJSONObject(i).getJSONArray("courses");
			subjects = new ArrayList<SubjectValues>();
			
			for(int j = 0; j < jsonSubjects.length(); j++){
				jsonSubject = jsonSubjects.getJSONObject(j);
				if(jsonSubject.getBoolean("normal") == true){
					subject = subjectDao.getSubjectByCode(jsonSubject.getString("code"));
				}else{
					subject = new Subject(jsonSubject.getInt("credits"), jsonSubject.getString("code"), jsonSubject.getString("code"), jsonSubject.getString("code"), sede);
				}
				subjectValues = new SubjectValues();
				//getting the complementary values
				if(subject!= null){
					complementaryValues = complementaryValuesDao.getComplementaryValues(career, subject);
					if(complementaryValues != null) {subjectValues.setComplementaryValues(complementaryValues);}
					else 
					{
						complementaryValues = new ComplementaryValues(career, subject);
						subjectValues.getComplementaryValues().setMandatory(jsonSubject.getBoolean("oblig"));
						subjectValues.getComplementaryValues().setTypology(jsonSubject.getString("type"));
						if(jsonSubject.getBoolean("normal") == true && subject != null) complementaryValuesDao.saveComplementaryValues(complementaryValues);
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
	
	public Long savePlan(Plan plan){
		
		Long id = null;
		
		if(plan != null){
			if(plan.getCareer() != null){
				//save everything inside
				
				SemesterDao semesterDao = new SemesterDao();
				ComplementaryValuesDao complementaryValueDao = new ComplementaryValuesDao();
				SubjectValuesDao subjectValuesDao = new SubjectValuesDao();
				SubjectDao subjectDao = new SubjectDao();
//				GroupDao groupDao = new GroupDao();
//				BlockDao blockDao = new BlockDao();
//				TeacherDao teacherDao = new TeacherDao();
//				SemesterValueDao  semesterValueDao = new SemesterValueDao();

				List<Semester> semesters = plan.getSemesters();
				List<SubjectValues> sVToRemoveList = new ArrayList<SubjectValues>();
				
				for(Semester s : semesters){
					//be careful because I can be saving more than once the same semester
					sVToRemoveList.clear();

					List<SubjectValues> subjectValuesList = s.getSubjects();
					
						if(subjectValuesList != null){
							if(subjectValuesList.size() != 0){
								for(SubjectValues sV : subjectValuesList){
									
										ComplementaryValues cV = sV.getComplementaryValues();
										if(cV != null){
											
												if(cV.getCareer() != null && cV.getSubject() != null){

													Subject s2 = cV.getSubject();
													if(s2 != null){
														if(s2.getId() == null){
															Subject sT = null;
															if(s2.getCode().isEmpty() == false) sT = subjectDao.getSubjectByCode(s2.getCode());
															else sT = subjectDao.getSubjectByName(s2.getName());
															
															if(sT.getId() == null){
																s2.setId(subjectDao.generateId());
																subjectDao.saveSubject(s2);																
															}
														}
													}
													
													List<Subject> requisitesList = cV.getListCorequisites();
													requisitesList.addAll(cV.getListCorequisitesOf());
													requisitesList.addAll(cV.getListPrerequisites());
													requisitesList.addAll(cV.getListPrerequisitesOf());
													
													for(Subject s3 : requisitesList){
														if(s3.getId() == null){
															
															Subject sT = null;
															if(s3.getCode().isEmpty() == false) sT = subjectDao.getSubjectByCode(s3.getCode());
															else sT = subjectDao.getSubjectByName(s3.getName());
															
															//OLD if(sR == null){
															if(s3.getId() == null){
																s3.setId(subjectDao.generateId());
																subjectDao.saveSubject(s3);																
															}
															
														}
													}
													
													if(cV.getId() == null){														
														cV.setId(complementaryValueDao.generateId());
													}
													complementaryValueDao.saveComplementaryValues(cV);
												}else{
													sV.setComplementaryValues(null);
												}
											
											if(sV.getId() == null){												
												sV.setId(subjectValuesDao.generateId()); 
											}
											subjectValuesDao.saveSubjectValue(sV);
										}else{
											sVToRemoveList.add(sV);
										}
									
								}
							}
						}
						if(s.getId() == null){
							s.setId(semesterDao.generateId());
						}
						semesterDao.saveSemester(s);
					
					
					for(SubjectValues sVT : sVToRemoveList){						
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

	private Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Plan> key = f.allocateId(Plan.class);
		return key.getId();
	}

	public Plan getPlanDefault(String careerCode) {
		Plan p = (Plan) ofy().load().type(Plan.class).filter("isDefault", true).filter("career.code", careerCode).first().now();
		p.setUser(null);
		p.setDefault(false);
		if(p != null){
			for(Semester s : p.getSemesters()){
				s.setId(null);
				for(SubjectValues sV : s.getSubjects()){
					sV.setId(null);
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
	private void deletePlan(Long id) {
		if(id != null){			
			Key<Plan> key = Key.create(Plan.class, id);
			ofy().delete().key(key).now();
		}
	}

	public List<Plan> getPlanByUser(Student s) {
		
		List<Plan> plans = null;
		
		if(s != null && s.getIdSun() != null){
			plans = ofy().load().type(Plan.class).filter("user.idG", s.getIdG()).list();
		}
		return plans;
	}

	public Plan getPlanById(Long planId) {
		Plan pToReturn = null;
		if(planId != null){			
			pToReturn = ofy().load().type(Plan.class).id(planId).now();
		}
		return pToReturn;
	}

	public void deletePlan(Plan plan) {
		if(plan != null){
			if(plan.getSemesters() != null && plan.getSemesters().size() > 0){
				
				SemesterDao semesterDao = new SemesterDao();
				SubjectValuesDao subjectValuesDao = new SubjectValuesDao();
				
				for(Semester s : plan.getSemesters()){
					if(s.getSubjects() != null && s.getSubjects().size() > 0){
						for(SubjectValues sV : s.getSubjects()){
							if(sV.getId() != null && sV.getId().equals("") == false){
								subjectValuesDao.deleteSubjectValues(sV.getId());
							}
						}
					}
					if(s.getId() != null && s.getId().equals("") == false){					
						semesterDao.deleteSemester(s.getId());
					}
				}
			}			
			if(plan.getId() != null && plan.getId().equals("") == false){				
				deletePlan(plan.getId());
			}
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
			String subjectLinePattern = "(\\d+)-(\\d+)\\t(.+)\\t(\\d+)\\t(\\d+)\\t(\\d+)\\t([a-z]+)\\t(\\d+)\\t(\\d+)\\t(\\t\\d+\\p{Punct}\\d+)|(.+\\t((ap)|(na))).*";
			
			String careerCode = null;
			SemesterDummy semesterD = null;
			List<SemesterDummy> semesters = new ArrayList<SemesterDummy>();
			
			for(String s : lines){
				
				SubjectDummy subjectD = null;
				
				boolean careerBoolean = Pattern.matches(careerLinePattern,s);
				boolean semesterBoolean = Pattern.matches(semesterLinePattern,s);
				boolean subjectBoolean = Pattern.matches(subjectLinePattern,s);
				

				@SuppressWarnings("unused")
				int x = 0;
				
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
		Career career = careerDao.getCareerByCode(careerCode);
		
		if(career != null){
			if(career.hasAnalysis() == false){
				SiaProxy.getRequisitesForACareer(career.getCode());
			}
			
			//getUser
			LoginServiceImpl login = new LoginServiceImpl();
			Student student = login.login("").getStudent();
			
			//name
			String name = "Historia académica - " + career.getName() + " " + new Date().toString();
			
			//isDefault
			boolean isDefault = false;
			
			//semesters list of subjectValues
			SubjectDao subjectDao = new SubjectDao();
			SubjectValuesDao subjectValuesDao = new SubjectValuesDao();
			GroupDao groupDao = new GroupDao();
			SemesterDao semesterDao = new SemesterDao();
			SemesterValueDao semesterValueDao = new SemesterValueDao();
			ComplementaryValuesDao complementaryValuesDao = new ComplementaryValuesDao();
			
			List<Semester> semesters = new ArrayList<Semester>();
			List<SubjectDummy> problematicSubjects = new ArrayList<SubjectDummy>();
			List<Subject> subjectsToUpdate = new ArrayList<Subject>();
			for(SemesterDummy semesterD : semestersD){
				Semester semester = new Semester();
				semester.setId(semesterDao.generateId());
				semesters.add(semester);
				
				//Add the info to the semester
				semester.setDate(semesterD.getYear() + "-" + semesterD.getSemester());
				
				//for subjects
				List<SubjectValues> subjects = new ArrayList<SubjectValues>();
				for(SubjectDummy subjectD : semesterD.getSubjects()){
					
					//Get subject
					Subject subject = subjectDao.getSubjectByCode(subjectD.getCode());
					assert subject != null;
					
					if(subject != null){
						SubjectValues subjectV = new SubjectValues();
						subjectV.setId(subjectValuesDao.generateId());
						
						//  Get semesterValue
						SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(semesterD.getYear(), semesterD.getSemester());

						//Get group
						Group group = groupDao.getOrCreateGroup(subject, semesterValue, subjectD.getGroup());
						//  Add career to careers list
						if(group.containsCareer(career.getCode()) == false){
							group.addCareer(career);
						}
						
						
						//Get complementaryValue
						ComplementaryValues complementaryValues = complementaryValuesDao.getComplementaryValues(careerCode, subject.getCode());
						if(complementaryValues != null){
							
							subjectV.setComplementaryValues(complementaryValues);
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
				
				List<ComplementaryValues> complementaryValuesProblematics = null;
				List<String> subjectCodes = new ArrayList<String>();
				List<String> careerCodes = new ArrayList<String>(); //in order to be send to te function in the siaProxy
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
				complementaryValuesProblematics = service.getComplementaryValues(subjectCodes, careerCodes);
					siaSearchEndTime = System.nanoTime();
				
				//Add the cV to the subjectValue 
				//add it to the subject and add it to a semester in the plan
				
				for(ComplementaryValues complementaryValuesT : complementaryValuesProblematics){
					
					SubjectDummy subjectDummyT = getSubjectDummy(problematicSubjects, complementaryValuesT.getSubject().getCode());
					assert subjectDummyT != null;
					
					if(subjectDummyT != null){
						SemesterDummy semesterDummyT = getSemesterDummy(semestersD, subjectDummyT.getCode());
						assert semesterDummyT != null;
						
						if(semesterDummyT != null){
							
							SubjectValues subjectValuesT = new SubjectValues();
							subjectValuesT.setId(subjectValuesDao.generateId());
							
							subjectValuesT.setComplementaryValues(complementaryValuesT);
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
						SubjectValues subjectValuesT = new SubjectValues();
						subjectValuesT.setId(subjectValuesDao.generateId());
						
						//get complementaryValue
						ComplementaryValues complementaryValuesT = complementaryValuesDao.getComplementaryValues(career.getCode(), subjectDummyT.getCode());
						
						if(complementaryValuesT != null){

							//get semesterDummy
							SemesterDummy semesterDummyT = getSemesterDummy(semestersD, subjectDummyT.getCode());
							
							if(semesterDummyT != null){
								SemesterValue semesterValueT = semesterValueDao.getOrCreateSemester(semesterDummyT.getYear(), semesterDummyT.getSemester());
								Group groupT = groupDao.getOrCreateGroup(complementaryValuesT.getSubject(), semesterValueT, subjectDummyT.getGroup());
								
								subjectValuesT.setComplementaryValues(complementaryValuesT);
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
						ComplementaryValues complementaryValues = new ComplementaryValues(career, subject, typology, mandatory, subjectGroup);
						complementaryValues.setId(complementaryValuesDao.generateId());
						
						SemesterValue semesterValue = semesterValueDao.getOrCreateSemester(semesterDummyT.getYear(), semesterDummyT.getSemester());
						Group group = groupDao.getOrCreateGroup(subject, semesterValue, subjectD.getGroup());
						
						SubjectValues subjectValuesT = new SubjectValues();
						subjectValuesT.setId(subjectValuesDao.generateId());
						subjectValuesT.setComplementaryValues(complementaryValues);
						subjectValuesT.setGrade(subjectD.getGrade());
						if(subjectD.getApproved() == true){
							subject.setApprovenType(true);
						}
						subjectValuesT.setGroup(group);
						subjectValuesT.setTaken(true);
						
						subjectDao.saveSubject(subject);
						complementaryValuesDao.saveComplementaryValues(complementaryValues);
						subjectValuesDao.saveSubjectValue(subjectValuesT);
						
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
				subjectDao.saveSubject(subject);
			}
			
			PlanDao planDao = new PlanDao();
			
			planToReturn = new Plan();
			planToReturn.setId(planDao.generateId());
			
			planToReturn.setName(name);
			planToReturn.setCareer(career);
			planToReturn.setUser(student);
			planToReturn.setDefault(isDefault);
			planToReturn.setSemesters(semesters);
			
			planDao.savePlan(planToReturn);
			
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
		subjectD.setGroup(Integer.valueOf(groupString));
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

}
