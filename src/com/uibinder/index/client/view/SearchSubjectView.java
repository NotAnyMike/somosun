package com.uibinder.index.client.view;

import java.util.List;

import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.shared.control.Career;

public interface SearchSubjectView {
	
	interface Presenter {
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void fill();
	void hideIt();
	void showIt();
	void addSubject(SubjectAccordionView view);
	//void addAntiRequisite(String type, String name, String code);
	//void addRequisite(String type, String name, String code);
	//void setSubjectGroupName(String s);
	//void addGroup(String group, String professor, String professorGrade, String groupGrade, String averageGrade, String freeSpaces, String totalSpaces, String L, String M, String C, String J, String V, String S, String D);
	int getSubjectsAmmount();
	void setCareerList(List<Career> c);
	
}
