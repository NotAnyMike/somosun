package com.uibinder.index.server.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Semester;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

public class PlanDao {
	
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
	
	public void savePlan(Plan plan){
		if(plan != null){
			if(plan.getCareer() != null){
				//save everything inside
				
				SemesterDao semesterDao = new SemesterDao();
				ComplementaryValuesDao complementaryValueDao = new ComplementaryValuesDao();
				SubjectValuesDao subjectValuesDao = new SubjectValuesDao();
				SubjectDao subjectDao = new SubjectDao();
				GroupDao groupDao = new GroupDao();
				BlockDao blockDao = new BlockDao();
				TeacherDao teacherDao = new TeacherDao();
				SemesterValueDao  semesterValueDao = new SemesterValueDao();

				List<Semester> semesters = plan.getSemesters();
				List<SubjectValues> sVToRemoveList = new ArrayList<SubjectValues>();
				
				for(Semester s : semesters){
					//be careful because I can be saving more than once the same semester
					sVToRemoveList.clear();

					List<SubjectValues> subjectValuesList = s.getSubjects();
					if(s.getId() == null || true){
						if(subjectValuesList != null){
							if(subjectValuesList.size() != 0){
								for(SubjectValues sV : subjectValuesList){
									if(sV.getId() == null || true){
										
										ComplementaryValues cV = sV.getComplementaryValues();
										if(cV != null){
											if(cV.getId() == null || true){
												if(cV.getCareer() != null && cV.getSubject() != null){
													//TODO
													Subject s2 = cV.getSubject();
													if(s2 != null){
														if(s2.getId() == null){
															Subject sT = null;
															if(s2.getCode().isEmpty() == false) sT = subjectDao.getSubjectByCode(s2.getCode());
															else sT = subjectDao.getSubjectByName(s2.getName());
															
															if(sT == null){
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
															
															if(sT == null){
																s3.setId(subjectDao.generateId());
																subjectDao.saveSubject(s3);																
															}
															
														}
													}
													
													cV.setId(complementaryValueDao.generateId());
													complementaryValueDao.saveComplementaryValues(cV);
												}else{
													sV.setComplementaryValues(null);
												}
											}
											sV.setId(subjectValuesDao.generateId()); 
											subjectValuesDao.saveSubjectValue(sV);
										}else{
											sVToRemoveList.add(sV);
										}
									}
								}
							}
						}
						s.setId(semesterDao.generateId(s));
						semesterDao.saveSemester(s);
					}
					
					for(SubjectValues sVT : sVToRemoveList){						
						s.getSubjects().remove(sVT);
					}
					
				}
				
				plan.setId(generateId());
				ofy().save().entity(plan).now();
			}
		}
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

	public void updatePlan(Plan plan) {
		if(plan.getId() == null){
			savePlan(plan);
		}else{
			updatePlanTransaction(plan);
		}
	}
	
	public void deletePlan(Long id) {
		Key<Plan> key = Key.create(Plan.class, id);
		ofy().delete().key(key).now();
		
	}

	private void updatePlanTransaction(final Plan plan) {
		ofy().transact(new VoidWork (){
			public void vrun() {
				deletePlan(plan.getId());
				savePlan(plan);
			}
			
		});
	}

	public List<Plan> getPlanByUser(Student s) {
		
		List<Plan> plans = null;
		
		if(s != null && s.getIdSun() != null){
			plans = ofy().load().type(Plan.class).filter("user.idG", s.getIdG()).list();
		}
		return plans;
	}

}
