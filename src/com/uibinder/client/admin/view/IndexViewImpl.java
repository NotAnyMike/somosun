package com.uibinder.client.admin.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.admin.presenter.IndexPresenter;

public class IndexViewImpl extends Composite implements IndexView {

	private IndexPresenter presenter;
	
	private static IndexViewUiBinder uiBinder = GWT.create(IndexViewUiBinder.class);
	
	@UiField ListBox careerDefaultListBox;
	@UiField ListBox careerAnalysisListBox;
	@UiField ListBox complementaryValuesListBox;
	@UiField ListBox planListBox;
	@UiField ListBox subjectListBox;
	@UiField ListBox subjectGroupListBox;
	
	@UiField Button deleteAllDefaultPlansButton;
	@UiField Button deleteCertainDefaultPlanButton;

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
		careerDefaultListBox.addItem(name, value);
		careerAnalysisListBox.addItem(name, value);
		complementaryValuesListBox.addItem(name, value);
		planListBox.addItem(name, value);
		subjectListBox.addItem(name, value);
		subjectGroupListBox.addItem(name, value);
	}
	
	public void cleanLists(){
		careerDefaultListBox.clear();
		careerAnalysisListBox.clear();
		complementaryValuesListBox.clear();
		planListBox.clear();
		subjectListBox.clear();
		subjectGroupListBox.clear();
	}
	
	private int getTimes(Button b){
		String timesString  = b.getElement().getAttribute("times");
		int times = Integer.valueOf(timesString);
		return times;
	}
	
	private void setTimes(Button b){
		if(getTimes(b) == 1){
			b.getElement().setAttribute("times","0");
		}else{
			b.getElement().setAttribute("times","1");
		}
	}
	
	/********************** HANDLERS **************************/
	
	@UiHandler("deleteAllDefaultPlansButton")
	public void onDeleteAllDefaultPlans(ClickEvent e){
		if(getTimes(deleteAllDefaultPlansButton) == 1){
			presenter.onDeleteAllDefaultPlans();
		}
		setTimes(deleteAllDefaultPlansButton);
	}
	
	@UiHandler("deleteCertainDefaultPlanButton")
	public void onDeleteCertainDefaultPlanButton(ClickEvent e){
		if(getTimes(deleteCertainDefaultPlanButton) == 1){
			String careerCode = planListBox.getSelectedValue();
			presenter.onDeleteCertainDefaultPlanButton(careerCode);
		}
		setTimes(deleteCertainDefaultPlanButton);
	}

}
