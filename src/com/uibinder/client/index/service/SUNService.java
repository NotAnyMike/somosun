package com.uibinder.client.index.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.shared.PlanValuesResult;
import com.uibinder.shared.RandomPhrase;
import com.uibinder.shared.SiaResultGroups;
import com.uibinder.shared.SiaResultSubjects;
import com.uibinder.shared.control.Career;
import com.uibinder.shared.control.ComplementaryValue;
import com.uibinder.shared.control.Plan;
import com.uibinder.shared.control.Student;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectGroup;

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
	public Plan getPlanDefault(String careerCode);
	public Plan generatePlanFromAcademicHistory(String academicHistory);
	
	public void toTest();
	
	public ComplementaryValue getComplementaryValuesFromMisPlanes(String career, String code);
	public List<ComplementaryValue> getComplementaryValuesFromMisPlanes(String careerCode);
	public List<ComplementaryValue> getMandatoryComplementaryValues(String careerCode);
	public ComplementaryValue createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student);
	
	List<ComplementaryValue> getComplementaryValues(List<String> selectedSubjectCodeStrings, List<String> selectedSubjectCareerStrings);
	
	public void analyzeCareer(String careerCode);
	
	public List<SubjectGroup> getSubjectGroups(String careerCode);
	
}
