package com.uibinder.client.index.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.presenter.PlanPresenter;
import com.uibinder.client.index.view.SiaSummaryView.Presenter;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectValues;

public interface WarningDeleteSubjectView {
	
	interface Presenter{
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void hideIt();
	void showIt();
	void setSubject(SubjectValues sV, Subject s);

}
