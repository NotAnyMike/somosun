package com.somosun.plan.client.index.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.somosun.plan.shared.CompletePlanInfo;
import com.somosun.plan.shared.PlanValuesResult;
import com.somosun.plan.shared.RandomPhrase;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;

/**
 * 
 * @author Mike
 * 
 * The RemoteServiceRelativePath note is to reference it dynamically on the web.xml to allow the use of the servlet
 * This is the class that has the methods that can be used to communicate with the server.
 * 
 * 
 */
@RemoteServiceRelativePath("sunService")
public interface SUNService extends RemoteService{

	Subject getSubjectByCode(int code);
	Subject getSubjectByCode(int code, String career);
	Subject getSubjectByName(String name);
	Subject getSubjectByName(String name, String career);
	
	List<RandomPhrase> getRandomPhrase();
	void saveRandomPhrase(String phrase, String author);
	
	public SiaResultGroups getGroupsFromSia(String subjectSiaCode, String sede);
	public SiaResultGroups getGroupsFromSia(Subject subject, String sede);
	
	public SiaResultSubjects getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student);
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student);
	public SiaResultSubjects getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, List<String> subjectCodeList);
	
	public List<Career> getCareers(String sede);
	public Career getCareerToUse(String careerCode);
	
	public Plan getPlanDefaultFromString(String careerCode);
	public Plan getPlanByUser(String planId);
	public List<Plan> getPlansByUserLoggedIn();
	public List<PlanValuesResult> getPlanValuesByUserLoggedIn();
	public void deletePlanFromUser(String planId);
	public void savePlanAsDefault(Student student, Plan plan);
	public Long savePlan(Student student, Plan plan);
	public Long savePlanAndGrade(Student student, Plan plan, Group group, Double oldGrade, Double newGrade);
	public Plan getPlanDefault(String careerCode);
	public Plan generatePlanFromAcademicHistory(String academicHistory);
	
	public void toTest();
	
	public List<ComplementaryValue> getComplementaryValueFromMisPlanes(String career, List<String> codes);
	public ComplementaryValue getComplementaryValueFromDb(String careerCode, String subjectCode);
	public List<ComplementaryValue> getComplementaryValuesFromMisPlanes(String careerCode);
	public List<ComplementaryValue> getMandatoryComplementaryValues(String careerCode);
	public ComplementaryValue createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student);
	
	List<ComplementaryValue> getComplementaryValues(List<String> selectedSubjectCodeStrings, List<String> selectedSubjectCareerStrings, String mainCareerCode);
	
	public List<SubjectGroup> getSubjectGroups(String careerCode);
	public void saveMessage(String name, String subject, String type, String message);
	
	public SemesterValue getCurrentSemesterValue();
	
	public CompletePlanInfo getCompletePlanInfo(String careerCode);
	
}
