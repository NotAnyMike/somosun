package com.uibinder.client.admin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
	
}
