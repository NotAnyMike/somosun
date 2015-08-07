package com.uibinder.client.admin.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.IndexPresenter;

public interface IndexView {

	interface Presenter{
		void onDeleteAllDefaultPlans();
		void onDeleteCertainDefaultPlanButton(String careerCode);
		void onResetAllHasAnalysisButton();
		void onResetCertainHasAnalysisButton(String careerCode);
		void onResetUpdateCareersButton();
		void onDeleteAllPlansButton();
		void onDeleteAllComplementaryValuesButton();
		void onDeleteCertainComplementaryValuesButton(String careerCode);
		void onDeleteAllSubjectsButton();
		void onDeleteAllSubjectGroupsButton();
		void onResetAllHasDefaultButton();
		void onResetCertainHasDefaultButton(String careerCode);
		void onDeleteAllSemestersButton();
		void onDeleteAllSubjectValueButton();
		void onMakeUserAdminButton(String userName);
		void onBlockUnblockUser(String userName);
		void onAnalyseAllCareers(boolean analyseAll);
		void onDeleteAllMessages();
		void onDeleteSuggestionMessages();
		void onDeleteErrorMessages();
		void onDeleteOtherMessages();
	}
	
	Widget asWidget();
	void setPresenter(IndexPresenter presenter);
	void onDeleteAllDefaultPlans(ClickEvent e);
	void onDeleteCertainDefaultPlanButton(ClickEvent e);
	void onResetAllHasAnalysisButton(ClickEvent e);
	void onResetCertainHasAnalysisButton(ClickEvent e);
	void onResetUpdateCareersButton(ClickEvent e);
	void onDeleteAllPlansButton(ClickEvent e);
	void onDeleteAllComplementaryValuesButton(ClickEvent e);
	void onDeleteCertainComplementaryValuesButton(ClickEvent e);
	void onDeleteAllSubjectsButton(ClickEvent e);
	void onDeleteAllSubjectGroupsButton(ClickEvent e);
	void onResetAllHasDefaultButton(ClickEvent e);
	void onResetCertainHasDefaultButton(ClickEvent e);
	void onDeleteAllSemestersButton(ClickEvent e);
	void onDeleteAllSubjectValueButton(ClickEvent e);
	void onMakeUserAdminButton(ClickEvent e);
	void onBlockUnblockUser(ClickEvent e);
	void onAnalyseAllCareers(ClickEvent e);
	void onAnalyseCareersNotAnalyzed(ClickEvent e);
	void onShowCertainMessage(ClickEvent e);
	void onShowUserMessages(ClickEvent e);
	void onDeleteAllMessages(ClickEvent e);
	void onDeleteSuggestionMessages(ClickEvent e);
	void onDeleteErrorMessages(ClickEvent e);
	void onDeleteOtherMessages(ClickEvent e);
}
