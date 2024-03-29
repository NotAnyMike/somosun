package com.somosun.plan.client.admin.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.somosun.plan.shared.control.Message;
import com.somosun.plan.shared.control.Plan;

public interface AdminServiceAsync {

	void resetCareer(AsyncCallback<Void> callback);

	void deleteAllDefaultPlans(AsyncCallback<Void> callback);

	void deleteDefaultPlan(String careerCode, AsyncCallback<Void> callback);

	void resetAllHasAnalysis(AsyncCallback asyncCallback);

	void resetCertainHasAnalysis(String careerCode, AsyncCallback asyncCallback);

	void deleteAllPlans(AsyncCallback asyncCallback);

	void deleteAllComplementaryValues(AsyncCallback asyncCallback);

	void deleteCertainComplementaryValues(String careerCode, AsyncCallback asyncCallback);

	void deleteAllSubjects(AsyncCallback asyncCallback);

	void deleteAllSubjectGroup(AsyncCallback asyncCallback);

	void resetAllHasDefaultField(AsyncCallback asyncCallback);

	void resetCertainHasDefaultField(String careerCode, AsyncCallback asyncCallback);

	void deleteAllSemesters(AsyncCallback asyncCallback);

	void deleteAllSubjectValue(AsyncCallback<Void> callback);

	void makeUserAdmin(String userName, AsyncCallback<Void> callback);

	void blockUnblockUser(String userName, AsyncCallback asyncCallback);

	void analyseAllCareers(boolean analyseAll, AsyncCallback<Void> asyncCallback);

	void getAllMessages(AsyncCallback<List<Message>> asyncCallback);

	void getAllErrorMessages(AsyncCallback<List<Message>> asyncCallback);

	void getAllSuggestionMessages(AsyncCallback<List<Message>> asyncCallback);

	void getAllOtherMessages(AsyncCallback<List<Message>> asyncCallback);

	void getMessageById(Long id, AsyncCallback<Message> asyncCallback);

	void getUserMessages(String username, AsyncCallback<List<Message>> asyncCallback);

	void deleteAllMessages(AsyncCallback asyncCallback);

	void deleteSuggestionMessages(AsyncCallback asyncCallback);

	void deleteErrorMessages(AsyncCallback asyncCallback);

	void deleteOtherMessages(AsyncCallback asyncCallback);

	void getPlansByUser(String username, AsyncCallback<List<Plan>> asyncCallback);

	void getPlanById(Long id, AsyncCallback<Plan> asyncCallback);

	void analyseCareer(String careerCode, AsyncCallback asyncCallback);

	void deleteAllBlocks(AsyncCallback<Void> callback);

	void deleteAllGroups(AsyncCallback<Void> callback);

	void certainHasDefaultField(String careerCode, AsyncCallback<Void> callback);
	
}
