package com.somosun.plan.client.index.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface SiaSummaryView {
	
	interface Presenter{
		void deleteAdminButtons();
		void onSavePlanAsDefaultClicked();
		void onAddMandatorySubjectsButton();
		void showSavePlanPopup();
		void onDeletePlanButtonClicked();
		void onSelecteCurrentSemester(int semesterNumber);
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	
	void setFoundationCredits(String approved, String necessary, String percentage);
	void setDisciplinaryCredits(String approved, String necessary, String percentage);
	void setFreeElectionCredits(String approved, String necessary, String percentage);
	void setLevelingCredits(String approved, String necessary, String percentage);
	void setFoundationCredits(String approved, String percentage);
	void setDisciplinaryCredits(String approved, String percentage);
	void setFreeElectionCredits(String approved, String percentage);
	void setLevelingCredits(String approved, String percentage);
	void setGPA(String x);
	void setAvance(String x);
	void setApprovedCredits(String x);
	void setAdditionalyCredits(String x);
	void setTotalApproved(String x);
	void setTotalNecessary(String x);
	void setTotalPerCent(String x);
	void setPercentageDisciplinaryCredits(String percentage);
	void setPercentageFreeElectionCredits(String percentage);
	void setPercentageFoundationCredits(String percentage);
	void setPercentageLevelingCredits(String percentage);
	void setDefaultFreeElectionCredits(String defaultCredits);
	void setDefaultLevelingCredits(String defaultCredits);
	void setDefaultFoundationCredits(String defaultCredits);
	void setDefaultDisciplinaryCredits(String defaultCredits);
	void setApprovedFreeElectionCredits(String defaultCredits);
	void setApprovedLevelingCredits(String defaultCredits);
	void setApprovedFoundationCredits(String defaultCredits);
	void setApprovedDisciplinaryCredits(String defaultCredits);
	
	void setMaxSemesterInForm(int x);
	void removeMaxSemesterFromForm();
	
	void deleteAdminButtons();
	void showWarning();
	void hideWarning();
	void onAddMandatorySubjectsButton(ClickEvent e);
	void onSavePlanClick(ClickEvent e);
	void onDeletePlanButtonClicked(ClickEvent e);

}
