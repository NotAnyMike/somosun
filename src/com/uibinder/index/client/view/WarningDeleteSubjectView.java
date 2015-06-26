package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.view.SiaSummaryView.Presenter;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

public interface WarningDeleteSubjectView {
	
	interface Presenter{
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void hideIt();
	void showIt();
	void setSubject(SubjectValues sV, Subject s);

}
