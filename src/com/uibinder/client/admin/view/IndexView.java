package com.uibinder.client.admin.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.IndexPresenter;

public interface IndexView {

	interface Presenter{
	}
	
	Widget asWidget();
	void setPresenter(IndexPresenter presenter);
}
