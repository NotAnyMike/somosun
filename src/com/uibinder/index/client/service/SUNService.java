package com.uibinder.index.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.index.shared.PlanValuesResult;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
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
	
	public void toTest();
	
	public ComplementaryValues getComplementaryValuesFromMisPlanes(String career, String code);
	public List<ComplementaryValues> getComplementaryValuesFromMisPlanes(String careerCode);
	public List<ComplementaryValues> getMandatoryComplementaryValues(String careerCode);
	public ComplementaryValues createDefaultSubject(String subjectGroupName, String credits, String careerCode, Student student);
	
	List<ComplementaryValues> getComplementaryValues(List<String> selectedSubjectCodeStrings, List<String> selectedSubjectCareerStrings);
	
	public void analyzeCareer(String careerCode);
	
	public void savePlanAsDefault(Student student, Plan plan);
	public void savePlan(Student student, Plan plan);
	public Plan getPlanDefault(String careerCode);
	
	public List<SubjectGroup> getSubjectGroups(String careerCode);
	
}
