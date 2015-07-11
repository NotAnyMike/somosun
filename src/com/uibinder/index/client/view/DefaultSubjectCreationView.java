package com.uibinder.index.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.shared.control.SubjectGroup;

public interface DefaultSubjectCreationView {

	interface Presenter{
		void onCreateDefaultSubjectButtonClick(String subjectGroupName, String credits, String semester);
	}
	
	void setPresenter(Presenter p);
	Widget asWidget();
	void onCreateDefaultSubjectButtonClick(ClickEvent e);
	void addEntryToList(String name);
	void clear();
	void hideIt();
	void showIt();
	void setSemester(String s);
	void addEntriesToList(List<SubjectGroup> list);
}
