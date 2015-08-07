package com.uibinder.client.admin.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.uibinder.shared.control.Message;

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
	void analyseAllCareers(boolean analyseAll);
	List<Message> getAllMessages();
	List<Message> getAllErrorMessages();
	List<Message> getAllSuggestionMessages();
	List<Message> getAllOtherMessages();
	Message getMessageById(Long id);
	List<Message> getUserMessages(String username);
	void deleteAllMessages();
	void deleteSuggestionMessages();
	void deleteErrorMessages();
	void deleteOtherMessages();
}
