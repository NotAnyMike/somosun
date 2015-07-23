package com.uibinder.client.admin.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminServiceAsync {

	void resetCareer(AsyncCallback<Void> callback);

	void deleteAllDefaultPlans(AsyncCallback<Void> callback);

	void deleteDefaultPlan(String careerCode, AsyncCallback<Void> callback);
	
}
