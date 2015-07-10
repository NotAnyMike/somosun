package com.uibinder.index.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.shared.control.Career;

public interface SubjectAccordionView {
	
	interface Presenter{	
		void onSpecificSubjectSelected(String subjectName, String subjectCode, String careerCode);
		void onSpecificSubjectUnselected(String subjectName, String subjectCode, String careerCode);
	}

	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void init();
	void setCareerList(List<Career> c);
	void setHeader(String code, String name, String type, String credtis, String careerCode);
	void setSubjectGroupName(String s);
	void addRequisite(String type, String name, String code);
	void addAntiRequisite(String type, String name, String code);
	void addGroup(String group, String professor, String professorGrade, String groupGrade, String averageGrade, String freeSpaces, String totalSpaces, String L, String M, String C, String J, String V, String S, String D);
	void onAddSpecificSubjectClick(ClickEvent event);
	String getCode();
	
}
