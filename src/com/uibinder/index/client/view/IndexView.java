package com.uibinder.index.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface IndexView {
	
	public interface Presenter {
	}
	  
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
