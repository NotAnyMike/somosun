package com.uibinder.index.client.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
 * This is the Asynchronous interface, the one that is inherit from the bitches form the server side,
 * this one has the same methods than SUNService because it is the connection between the server an 
 * the client
 * 
 * 
 */
public interface SUNServiceAsync {

	void getSubjectByCode(int code, AsyncCallback<Subject> callback);

	void getSubjectByCode(int code, String career, AsyncCallback<Subject> callback);

	void getSubjectByName(String name, AsyncCallback<Subject> callback);

	void getSubjectByName(String name, String career, AsyncCallback<Subject> callback);

	void getRandomPhrase(AsyncCallback<List<RandomPhrase>> callback);

	void saveRandomPhrase(String phrase, String author,
			AsyncCallback<Void> callback);

	void getGroupsFromSia(Subject subject, String sede,
			AsyncCallback<SiaResultGroups> callback);
	
	void getGroupsFromSia(String subjectSiaCode, String sede,
			AsyncCallback<SiaResultGroups> callback);

	void getSubjectFromSia(String nameOrCode, String typology, String career,
			String scheduleCP, int page, int ammount, String sede,
			Student student, List<String> subjectCodeList,
			AsyncCallback<SiaResultSubjects> callback);

	void getCareers(String sede, AsyncCallback<List<Career>> asyncGetCareers);

	void getPlanDefault(String careerCode, AsyncCallback<Plan> callback);

	void toTest(AsyncCallback<Void> callback);

	void getComplementaryValuesFromMisPlanes(String career, String code,	AsyncCallback<ComplementaryValues> callback);

	void getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student, AsyncCallback<SiaResultSubjects> callback);

	void getComplementaryValuesFromMisPlanes(String careerCode,	AsyncCallback<List<ComplementaryValues>> asyncGetComplementaryValuesByCareer);

	void savePlanAsDefault(Student student, Plan plan,	AsyncCallback<Void> callback);

	void getPlanDefaultFromString(String careerCode, AsyncCallback<Plan> callback);

	void analyzeCareer(String careerCode, AsyncCallback<Void> callback);

	void getComplementaryValues(List<String> selectedSubjectCodeStrings, List<String> selectedSubjectCareerStrings, AsyncCallback<List<ComplementaryValues>> callback);

	void getSubjectFromSia(String nameOrCode, String typology, String career,String scheduleCP, int page, int ammount, String sede,	Student student, AsyncCallback<SiaResultSubjects> callback);

	void createDefaultSubject(String subjectGroupName, String credits,String careerCode, Student student,AsyncCallback<ComplementaryValues> callback);

	void getSubjectGroups(String careerCode, AsyncCallback<List<SubjectGroup>> callback);

	void getCareerToUse(String careerCode, AsyncCallback<Career> callback);

	void getMandatoryComplementaryValues(String careerCode,	AsyncCallback<List<ComplementaryValues>> callback);

	void savePlan(Student student, Plan plan, AsyncCallback<Void> callback);

	void getPlansByUserLoggedIn(AsyncCallback<List<Plan>> callback);

	void getPlanValuesByUserLoggedIn(
			AsyncCallback<List<PlanValuesResult>> callback);

}
