package com.somosun.plan.client.index.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.ContactUsPresenter;
import com.somosun.plan.client.index.presenter.PlanPresenter;

public interface ContactUsView {

	interface Presenter{
		void setType(String type);
		void sendMessage(String name, String subject, String type, String message);
	}
	
	void setPresenter(ContactUsPresenter presenter);
	Widget asWidget();
	void init();
	void selectSuggestion();
	void selectError();
	void selectOther();
	void onCleanButtonClick(ClickEvent e);
	void onSendButtonClick(ClickEvent e);
	void showThanks();
	void hideThanks();
	void onYourWelcome(ClickEvent e);
	
}
