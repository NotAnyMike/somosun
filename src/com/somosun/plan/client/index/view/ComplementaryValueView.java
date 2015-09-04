package com.somosun.plan.client.index.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.control.Career;

public interface ComplementaryValueView {

	interface Presenter{
		void onGroupsAccordionShows(String subjectCode, final ComplementaryValueViewImpl view);
		void addComplementaryValueView(SubjectAccordionViewImpl accordion, String name, String code, String careerCode);
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void init();
	void hasNoPrerequisites();
	void hasNoCorequisites();
	void setSubjectGroupName(String s);
	void addRequisite(String type, String name, String code, SubjectAccordionViewImpl accordion, boolean makeStatic, boolean makeUnavailable);
	void addAntiRequisite(String type, String name, String code, SubjectAccordionViewImpl accordion,  boolean makeStatic);
	void setCareerList(List<Career> c, String careerCode);
	void isNoCorequisite();
	void isNoPrerequisite();
	void showGroupError();
	void hasNoGroups();
	void showUnavailableWarning();
	void hideUnavailableWarning();
	void showErrorWarning();
	void hideErrorWarning();

}
