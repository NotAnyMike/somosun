package com.somosun.plan.client.index.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.shared.PlanValuesResult;

public interface PlanView {

	public interface Presenter {
		void planNameChanged(String s);
		void deletePlanConfirmed();
		void deleteSemesterConfirmed(int semester);
	}
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
	void showCurtain();
	void hideCurtain();
	void showChangeName();
	void hideChangeName();
	void showLoadingSubjects();
	void hideLoadingSubjects();
	void setSugestionText(String s);
	void cancelChangeNameButtonClicked(ClickEvent e);
	void savePlanButton(ClickEvent e);
	void cancelGeneralButton(ClickEvent e);
	void onDeleteGeneralButtonClick(ClickEvent e);
	
}
