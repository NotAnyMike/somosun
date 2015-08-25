package com.somosun.plan.client.index.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;

public interface SelectedSubjectView {
	
	interface Presenter{
		void onSpecificSubjectSelected(String subjectName, String subjectCode, String careerCode);
		void onSpecificSubjectUnselected(String subjectName, String subjectCode, String careerCode);
	}
	
	void setTexts(String name, String code, String careerCode);
	Widget asWidget();
	void setPresenter(Presenter presenter);
	void onToRemoveButtonClicked(ClickEvent event);
	void remove();
	String getCode();
	String getCareer();
	
}
