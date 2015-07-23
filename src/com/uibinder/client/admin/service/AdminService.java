package com.uibinder.client.admin.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminService")
public interface AdminService extends RemoteService{

	void resetCareer();
	void deleteAllDefaultPlans();
	void deleteDefaultPlan(String careerCode);
}
