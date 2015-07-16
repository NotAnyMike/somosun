package com.uibinder.index.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.shared.PlanValuesResult;

public interface PlanView {

	public interface Presenter {
		void planNameChanged(String s);
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	void showCurtain();
	void hideCurtain();
	void showChangeName();
	void hideChangeName();
	void setSugestionText(String s);
	void cancelChangeNameButtonClicked(ClickEvent e);
	void changeNameButton(ClickEvent e);
}
