package com.uibinder.client.index.view;

import com.google.gwt.user.client.ui.Widget;

public interface AnnouncementView {

	public interface Presenter { 
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
