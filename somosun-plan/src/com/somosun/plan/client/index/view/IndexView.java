package com.somosun.plan.client.index.view;

import com.google.gwt.user.client.ui.Widget;

public interface IndexView {
	
	public interface Presenter {
	}
	  
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
