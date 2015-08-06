package com.uibinder.client.admin.view;

import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.MessagesPresenter;

public interface MessagesView {
	
	interface Presenter{
		void addMessage(String fullMessage);
		void showAllMessage();
	}
	
	Widget asWidget();
	void setPresenter(MessagesPresenter presenter);
	void init();
	void addMessage(String fullMessage);
	
}
