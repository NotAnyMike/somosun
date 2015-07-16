package com.uibinder.index.client.view;


import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.shared.PlanValuesResult;

public interface CreateView {

	public interface Presenter { 
		void onResetButtonClicked();
		void onListBoxCreateChange(String career);
		void onContinueButtonClicked(String academicHistory);
		void getCareers(String sede);
		void onContinueDefaultButtonClick(String careerCode);
		void showWarning();
		void hideWarning();
		void onNewAnalyzedPlanButtonClicked(String careerCode);
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	public void addCareerToListBox(String career, String value);
	public void clearListBox();
	public Button getContinueDefaultButton();
	public String getCurrentDefaultCareerValue();
	public void setNewAnalyzedPlanButtonEnable(boolean b);
	public void setModelAnalyzedPlanButtonEnable(boolean b);
	void showWarning();
	void hideWarning();
	void onNewAnalyzedPlanButtonClicked(ClickEvent e);
	void addPlans(List<PlanValuesResult> planValue);
	
}
