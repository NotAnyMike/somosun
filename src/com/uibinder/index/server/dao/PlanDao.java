package com.uibinder.index.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Semester;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class PlanDao {
	
	private final String ADMON = "{'sede':'bog','name':'Economía','code':'2522','lvl':'pre','semesters':[{'number':1,'courses':[{'code':'1000004','normal':true,'type':'f','oblig':true},{'code':'2016007','normal':true,'type':'f','oblig':true},{'code':'2016008','normal':true,'type':'f','oblig':true},{'code':'2015270','normal':true,'type':'f','oblig':true},{'code':'2016015','normal':true,'type':'f','oblig':true},{'code':'1000044','normal':true,'type':'n','oblig':true}]},{'number':2,'courses':[{'code':'2016021','normal':true,'type':'f','oblig':true},{'code':'2016011','normal':true,'type':'d','oblig':true},{'code':'2016017','normal':true,'type':'d','oblig':true},{'code':'2016012','normal':true,'type':'d','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000045','normal':true,'type':'n','oblig':true}]},{'number':3,'courses':[{'code':'2016020','normal':true,'type':'d','oblig':true},{'code':'2016009','normal':true,'type':'d','oblig':true},{'code':'2016018','normal':true,'type':'d','oblig':true},{'code':'2016013','normal':true,'type':'d','oblig':true},{'code':'optativa contenido cualitativo','normal':false,'credits':3,'type':'f','oblig':false},{'code':'1000046','normal':true,'type':'n','oblig':true}]},{'number':4,'courses':[{'code':'2016002','normal':true,'type':'d','oblig':true},{'code':'2016005','normal':true,'type':'d','oblig':true},{'code':'2016014','normal':true,'type':'d','oblig':true},{'code':'2016019','normal':true,'type':'d','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false},{'code':'1000047','normal':true,'type':'n','oblig':true},]},{'number':5,'courses':[{'code':'2016022','normal':true,'type':'d','oblig':true},{'code':'2016023','normal':true,'type':'d','oblig':true},{'code':'2016003','normal':true,'type':'d','oblig':true},{'code':'2016006','normal':true,'type':'d','oblig':true},{'code':'2016001','normal':true,'type':'d','oblig':true},]},{'number':6,'courses':[{'code':'2016004','normal':true,'type':'d','oblig':true},{'code':'2016010','normal':true,'type':'d','oblig':true},{'code':'2016024','normal':true,'type':'d','oblig':true},{'code':'2016016','normal':true,'type':'f','oblig':true},{'code':'libre elección','normal':false,'credits':2,'type':'l','oblig':false}]},{'number':7,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'f','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'f','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':8,'courses':[{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'f','oblig':false},{'code':'optativa interdsciplinar','normal':false,'credits':3,'type':'f','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección seminario profesional','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':9,'courses':[{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false},{'code':'libre elección','normal':false,'credits':3,'type':'l','oblig':false}]},{'number':10,'courses':[{'code':'2015300','normal':true,'type':'d','oblig':true}]}]}]}";  
	private final String CAREERS_DEFAULTS[] = {ADMON};
	
	static{
		ObjectifyService.register(Plan.class);
	}
	
	public Plan createPlanFromDefaultString(String careerCode){
		
		CareerDao careerDao = new CareerDao();
		SubjectDao subjectDao = new SubjectDao();
		
		Plan plan = new Plan();
		Career career = null;
		String sede = "";
		String name = "";
		List<Semester> semesters = new ArrayList<Semester>();
		Semester semester = null;
		List<SubjectValues> subjects = null;
		Subject subject = null;
		SubjectValues subjectValues = null;
		Map<SubjectValues, Subject> subjectMap = new HashMap<SubjectValues, Subject>();;
	
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
				subjectValues.setTypology(jsonSubject.getString("type"));
				subjectValues.setObligatoriness(jsonSubject.getBoolean("oblig"));
				
				if(subject != null && subjectValues != null) {
					subjects.add(subjectValues);
					subjectMap.put(subjectValues, subject);
				}
			}
			semester.setSubjects(subjects);
			semesters.add(semester);
		}
		plan.setSubjectMap(subjectMap);
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
	
	

}
