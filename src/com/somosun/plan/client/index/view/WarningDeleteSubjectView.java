package com.somosun.plan.client.index.view;

import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.client.index.view.SiaSummaryView.Presenter;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectValue;

public interface WarningDeleteSubjectView {
	
	interface Presenter{
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void hideIt();
	void showIt();
	void setSubject(SubjectValue sV, Subject s);

}
