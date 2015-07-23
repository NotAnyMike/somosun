package com.uibinder.client.admin.view;

import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.IndexPresenter;

public class IndexViewImpl extends Composite implements IndexView {

	private IndexPresenter presenter;
	
	private static IndexViewUiBinder uiBinder = GWT.create(IndexViewUiBinder.class);
	
	@UiField ListBox careerListBox;
	@UiField ListBox complementaryValuesListBox;
	@UiField ListBox planListBox;
	@UiField ListBox subjectListBox;
	@UiField ListBox subjectGroupListBox;

	@UiTemplate("IndexView.ui.xml")
	interface IndexViewUiBinder extends UiBinder<Widget, IndexViewImpl> {
	}

	public IndexViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(IndexPresenter presenter) {
		this.presenter = presenter;
	}

	public void addCareerToListBox(String name, String value) {
		careerListBox.addItem(name, value);
		complementaryValuesListBox.addItem(name, value);
		planListBox.addItem(name, value);
		subjectListBox.addItem(name, value);
		subjectGroupListBox.addItem(name, value);
	}
	
	public void cleanLists(){
		careerListBox.clear();
		complementaryValuesListBox.clear();
		planListBox.clear();
		subjectListBox.clear();
		subjectGroupListBox.clear();
	}

}
