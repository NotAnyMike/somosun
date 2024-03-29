package com.somosun.plan.client.admin.view;

import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.admin.presenter.PlanAdminPresenter;

public interface PlanAdminView {

	interface Presenter{
		void showPlansByUser(String username);
	}
	
	void setPresenter(PlanAdminPresenter presenter);
	Widget asWidget();
	void init();
	
}
