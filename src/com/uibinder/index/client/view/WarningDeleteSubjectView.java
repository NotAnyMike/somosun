package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.view.SiaSummaryView.Presenter;

public interface WarningDeleteSubjectView {
	
	interface Presenter{
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void hideIt();
	void showIt();
	void setSubject(String code, String name, double grade, int credits, boolean oblg, int type);

}
