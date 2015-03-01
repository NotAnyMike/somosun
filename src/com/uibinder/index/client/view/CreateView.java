package com.uibinder.index.client.view;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public interface CreateView {

	public interface Presenter { 
		void onResetButtonClicked();
		void onListBoxCreateChange(String career);
		void onContinueButtonClicked(String academicHistory);
		void getCareers(String sede);
		void onContinueDefaultButtonClick(String careerCode);
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	public abstract void addCareerToListBox(String career, String value);
	public abstract void clearListBox();
	public Button getContinueDefaultButton();
	public String getCurrentDefaultCareerValue();
}
