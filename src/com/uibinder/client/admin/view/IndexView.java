package com.uibinder.client.admin.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.IndexPresenter;

public interface IndexView {

	interface Presenter{
		void onDeleteAllDefaultPlans();
		void onDeleteCertainDefaultPlanButton(String careerCode);
	}
	
	Widget asWidget();
	void setPresenter(IndexPresenter presenter);
	void onDeleteAllDefaultPlans(ClickEvent e);
	void onDeleteCertainDefaultPlanButton(ClickEvent e);
}
