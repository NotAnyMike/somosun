package com.somosun.plan.client.index.view;

import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.ui.PanelHeader;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;

public interface SubjectAccordionView {
	
	interface Presenter{	
		void onSpecificSubjectSelected(String subjectName, String subjectCode, String careerCode);
		void onSpecificSubjectUnselected(String subjectName, String subjectCode, String careerCode);
		void onAccordionClicked(String subjectCode, SubjectAccordionViewImpl accordion, ComplementaryValueViewImpl view);
	}

	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void init();
	void setHeader(String code, String name, String type, String credtis, String careerCode);
	void onAddSpecificSubjectClick(ClickEvent event);
	void onAddSpecificRequisiteClick(ClickEvent event);
	String getCode();
	void showError();
	void addComplementaryView(ComplementaryValueViewImpl view);
	PanelHeader getHeader();
	
}
