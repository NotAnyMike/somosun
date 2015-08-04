package com.uibinder.client.index.view;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.presenter.PlanPresenter;
import com.uibinder.shared.control.Career;

public interface ComplementaryValueView {

	interface Presenter{
		void onGroupsAccordionShows(String subjectCode, final ComplementaryValueViewImpl view);
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void init();
	void hasNoPrerequisites();
	void hasNoCorequisites();
	void setSubjectGroupName(String s);
	void addRequisite(String type, String name, String code);
	void addAntiRequisite(String type, String name, String code);
	void setCareerList(List<Career> c, String careerCode);
	void isNoCorequisite();
	void isNoPrerequisite();
	void showGroupError();
	void hasNoGroups();

}