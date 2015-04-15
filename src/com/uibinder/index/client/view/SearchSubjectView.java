package com.uibinder.index.client.view;

import org.gwtbootstrap3.extras.select.client.ui.Select;

import com.google.gwt.uibinder.client.UiField;
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
	void addAntiRequisite(String type, String name, String code);
	void addRequisite(String type, String name, String code);
	void setSubjectGroupName(String s);

}
