package com.uibinder.client.admin.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.ListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
	
	@UiField ListBox careerDefaultListBox;
	@UiField ListBox careerAnalysisListBox;
	@UiField ListBox complementaryValuesListBox;
	@UiField ListBox planListBox;
	@UiField ListBox subjectGroupListBox;
	
	@UiField Button resetUpdateCareersButton;
	@UiField Button deleteAllDefaultPlansButton;
	@UiField Button resetAllHasAnalysisButton;
	@UiField Button deleteAllPlansButton;
	@UiField Button deleteAllComplementaryValuesButton;
	@UiField Button deleteAllSubjectsButton;
	@UiField Button deleteAllSubjectGroupsButton;
	@UiField Button resetAllHasDefaultButton;
	@UiField Button deleteAllSemestersButton;
	@UiField Button deleteAllSubjectValueButton;
	@UiField Button deleteCertainDefaultPlanButton;
	@UiField Button resetCertainHasAnalysisButton;
	@UiField Button deleteCertainComplementaryValuesButton;
	@UiField Button resetCertainHasDefaultButton;

	private static IndexViewUiBinder uiBinder = GWT.create(IndexViewUiBinder.class);

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
		subjectGroupListBox.addItem(name, value);
	}
	
	public void cleanLists(){
		careerDefaultListBox.clear();
		careerAnalysisListBox.clear();
		complementaryValuesListBox.clear();
		planListBox.clear();
		subjectGroupListBox.clear();
	}
	
	private String getTimes(Element b){
		String timesString  = b.getAttribute("times");
		if(timesString == null || timesString.isEmpty()) timesString = "0";
		return timesString;
	}
	
	private void setTimes(Element b){
		if(getTimes(b).equals("1") == true){
			b.setAttribute("times","0");
		}else{
			b.setAttribute("times","1");
		}
	}
	
	/********************** HANDLERS **************************/
	
	@UiHandler("deleteAllDefaultPlansButton")
	public void onDeleteAllDefaultPlans(ClickEvent e){
		if(getTimes(deleteAllDefaultPlansButton.getElement()).equals("1")){
			presenter.onDeleteAllDefaultPlans();
		}
		setTimes(deleteAllDefaultPlansButton.getElement());
	}
	
	@UiHandler("deleteCertainDefaultPlanButton")
	public void onDeleteCertainDefaultPlanButton(ClickEvent e){
		if(getTimes(deleteCertainDefaultPlanButton.getElement()).equals("1")){
			String careerCode = planListBox.getSelectedValue();
			presenter.onDeleteCertainDefaultPlanButton(careerCode);
		}
		setTimes(deleteCertainDefaultPlanButton.getElement());
	}
	
	@UiHandler("resetAllHasAnalysisButton")
	public void onResetAllHasAnalysisButton(ClickEvent e){
		if(getTimes(resetAllHasAnalysisButton.getElement()).equals("1")){
			presenter.onResetAllHasAnalysisButton();
		}
		setTimes(resetAllHasAnalysisButton.getElement());
	}
	
	@UiHandler("resetCertainHasAnalysisButton")
	public void onResetCertainHasAnalysisButton(ClickEvent e){
		if(getTimes(resetCertainHasAnalysisButton.getElement()).equals("1")){
			String careerCode = careerAnalysisListBox.getSelectedValue();
			presenter.onResetCertainHasAnalysisButton(careerCode);
		}
		setTimes(resetCertainHasAnalysisButton.getElement());
	}
	
	@UiHandler("resetUpdateCareersButton")
	public void onResetUpdateCareersButton(ClickEvent e){
		if(getTimes(resetUpdateCareersButton.getElement()).equals("1")){
			presenter.onResetUpdateCareersButton();
		}
		setTimes(resetUpdateCareersButton.getElement());
	}
	
	@UiHandler("deleteAllPlansButton")
	public void onDeleteAllPlansButton(ClickEvent e){
		if(getTimes(deleteAllPlansButton.getElement()).equals("1")){
			presenter.onDeleteAllPlansButton();
		}
		setTimes(deleteAllPlansButton.getElement());
	}
	
	@UiHandler("deleteAllComplementaryValuesButton")
	public void onDeleteAllComplementaryValuesButton(ClickEvent e){
		if(getTimes(deleteAllComplementaryValuesButton.getElement()).equals("1")){
			presenter.onDeleteAllComplementaryValuesButton();
		}
		setTimes(deleteAllComplementaryValuesButton.getElement());
	}
	
	@UiHandler("deleteCertainComplementaryValuesButton")
	public void onDeleteCertainComplementaryValuesButton(ClickEvent e){
		if(getTimes(deleteCertainComplementaryValuesButton.getElement()).equals("1")){
			String careerCode = complementaryValuesListBox.getSelectedValue();
			presenter.onDeleteCertainComplementaryValuesButton(careerCode);
		}
		setTimes(deleteCertainComplementaryValuesButton.getElement());
	}
	
	@UiHandler("deleteAllSubjectsButton")
	public void onDeleteAllSubjectsButton(ClickEvent e){
		if(getTimes(deleteAllSubjectsButton.getElement()).equals("1")){
			presenter.onDeleteAllSubjectsButton();
		}
		setTimes(deleteAllSubjectsButton.getElement());
	}
	
	@UiHandler("deleteAllSubjectGroupsButton")
	public void onDeleteAllSubjectGroupsButton(ClickEvent e){
		if(getTimes(deleteAllSubjectGroupsButton.getElement()).equals("1")){
			presenter.onDeleteAllSubjectGroupsButton();
		}
		setTimes(deleteAllSubjectGroupsButton.getElement());
	}
	
	@UiHandler("resetAllHasDefaultButton")
	public void onResetAllHasDefaultButton(ClickEvent e){
		if(getTimes(resetAllHasDefaultButton.getElement()).equals("1")){
			presenter.onResetAllHasDefaultButton();
		}
		setTimes(resetAllHasDefaultButton.getElement());
	}
	
	@UiHandler("resetCertainHasDefaultButton")
	public void onResetCertainHasDefaultButton(ClickEvent e){
		if(getTimes(resetCertainHasDefaultButton.getElement()).equals("1")){
			String careerCode = careerDefaultListBox.getSelectedValue();
			presenter.onResetCertainHasDefaultButton(careerCode);
		}
		setTimes(resetCertainHasDefaultButton.getElement());
	}
	
	@UiHandler("deleteAllSemestersButton")
	public void onDeleteAllSemestersButton(ClickEvent e){
		if(getTimes(deleteAllSemestersButton.getElement()).equals("1")){
			presenter.onDeleteAllSemestersButton();
		}
		setTimes(deleteAllSemestersButton.getElement());
	}
	
	@UiHandler("deleteAllSubjectValueButton")
	public void onDeleteAllSubjectValueButton(ClickEvent e){
		if(getTimes(deleteAllSubjectValueButton.getElement()).equals("1")){
			presenter.onDeleteAllSubjectValueButton();
		}
		setTimes(deleteAllSubjectValueButton.getElement());
	}

}
