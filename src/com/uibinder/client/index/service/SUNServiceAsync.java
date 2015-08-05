package com.uibinder.client.index.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

	void getComplementaryValueFromMisPlanes(String career, String code,	AsyncCallback<ComplementaryValue> callback);

	void getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student, AsyncCallback<SiaResultSubjects> callback);

	void getComplementaryValuesFromMisPlanes(String careerCode,	AsyncCallback<List<ComplementaryValue>> asyncGetComplementaryValuesByCareer);

	void savePlanAsDefault(Student student, Plan plan,	AsyncCallback<Void> callback);

	void getPlanDefaultFromString(String careerCode, AsyncCallback<Plan> callback);

	void getComplementaryValues(List<String> subjectCodeStrings, List<String> subjectCareerStrings, AsyncCallback<List<ComplementaryValue>> callback);

	void getSubjectFromSia(String nameOrCode, String typology, String career,String scheduleCP, int page, int ammount, String sede,	Student student, AsyncCallback<SiaResultSubjects> callback);

	void createDefaultSubject(String subjectGroupName, String credits,String careerCode, Student student,AsyncCallback<ComplementaryValue> callback);

	void getSubjectGroups(String careerCode, AsyncCallback<List<SubjectGroup>> callback);

	void getCareerToUse(String careerCode, AsyncCallback<Career> callback);

	void getMandatoryComplementaryValues(String careerCode,	AsyncCallback<List<ComplementaryValue>> callback);

	void savePlan(Student student, Plan plan, AsyncCallback<Long> callback);

	void getPlansByUserLoggedIn(AsyncCallback<List<Plan>> callback);

	void getPlanValuesByUserLoggedIn(
			AsyncCallback<List<PlanValuesResult>> callback);

	void deletePlanFromUser(String planId, AsyncCallback<Void> callback);

	void getPlanByUser(String planId, AsyncCallback<Plan> callback);

	void generatePlanFromAcademicHistory(String academicHistory, AsyncCallback<Plan> callback);

	void getComplementaryValueFromDb(String careerCode, String subjectCode, AsyncCallback<ComplementaryValue> callback);

}
