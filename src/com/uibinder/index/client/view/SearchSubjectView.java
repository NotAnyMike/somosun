package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;

public interface SearchSubjectView {
	
	interface Presenter {
	}
	
	void setPresenter(PlanPresenter presenter);
	Widget asWidget();
	void fill();
	void hideIt();
	void showIt();

}
