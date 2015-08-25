package com.somosun.plan.client.index.view;

import java.util.List;

import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.control.Career;

public interface SearchSubjectView {
	
	interface Presenter {
		void onSearchButtonClicked(String text, String career, String checkBoxValue, int x);
		void onFinalizarButtonClick();
		void hideSearchBox();
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
	void setCareerList(List<Career> c, Career selectedCareer);
	void addSelectedSubject(Widget w);
	void onSearchButtonClick(ClickEvent event);
	void onFinalizarButtonClick(ClickEvent event);
	void clear();
	void setSemester(String semester);
	
}
