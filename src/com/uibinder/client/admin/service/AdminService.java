package com.uibinder.client.admin.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("adminService")
public interface AdminService extends RemoteService{

	void resetCareer();
	void deleteAllDefaultPlans();
	void deleteDefaultPlan(String careerCode);
	void resetAllHasAnalysis();
	void resetCertainHasAnalysis(String careerCode);
	void deleteAllPlans();
	void deleteAllComplementaryValues();
	void deleteCertainComplementaryValues(String careerCode);
	void deleteAllSubjects();
	void deleteAllSubjectGroup();
	void resetAllHasDefaultField();
	void resetCertainHasDefaultField(String careerCode);
	void deleteAllSemesters();
	void deleteAllSubjectValue();
	void makeUserAdmin(String userName);
	void blockUnblockUser(String userName);
}
