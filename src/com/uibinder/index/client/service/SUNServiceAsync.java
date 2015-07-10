package com.uibinder.index.client.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.SiaResultGroups;
import com.uibinder.index.shared.SiaResultSubjects;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Student;
import com.uibinder.index.shared.control.Subject;

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

	void getSubjectFromSia(String nameOrCode, String typology, String career, String scheduleCP, int page, int ammount, String sede, Student student, AsyncCallback<SiaResultSubjects> callback);

	void getCareers(String sede, AsyncCallback<List<Career>> asyncGetCareers);

	void getPlanDefault(String careerCode, AsyncCallback<Plan> callback);

	void toTest(AsyncCallback<Void> callback);

	void getComplementaryValues(String career, String code,	AsyncCallback<ComplementaryValues> callback);

	void getSubjectsFromSia(String nameOrCode, String typology, String career, String sede, int page, Student student, AsyncCallback<SiaResultSubjects> callback);

	void getComplementaryValues(String careerCode,	AsyncCallback<List<ComplementaryValues>> asyncGetComplementaryValuesByCareer);

	void savePlanAsDefault(Student student, Plan plan,	AsyncCallback<Void> callback);

	void getPlanDefaultFromString(String careerCode, AsyncCallback<Plan> callback);

}
