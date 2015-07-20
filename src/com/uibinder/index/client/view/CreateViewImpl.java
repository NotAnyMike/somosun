package com.uibinder.index.client.view;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.shared.PlanValuesResult;

public class CreateViewImpl extends Composite implements CreateView {
	
	private Presenter presenter;
	
	@UiField Button continueButton;
	@UiField Button modelAnalyzedPlanButton;
	@UiField Button newAnalyzedPlanButton;
	@UiField Button resetButton;
	@UiField Button deletePlanButton;
	@UiField Button selectPlanButton;
	
	@UiField TextArea textBoxCreate;
	
	@UiField ListBox listBoxCreate;
	@UiField ListBox listBoxSelectPlanSaved;
	
	@UiField HTMLPanel htmlPanelWarning;

	private static CreateViewUiBinder uiBinder = GWT
			.create(CreateViewUiBinder.class);

	@UiTemplate("CreateView.ui.xml")
	interface CreateViewUiBinder extends UiBinder<Widget, CreateViewImpl> {
	}

	public CreateViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void addCareerToListBox(String career, String value) {
		listBoxCreate.addItem(career, value);
	}

	@Override
	public void clearListBox() {
		listBoxCreate.clear();
	}
	
	public void clearTextBoxCreate(){
		textBoxCreate.setValue("");
	}

	@Override
	public Button getContinueDefaultButton() {
		return modelAnalyzedPlanButton;
	}
	
	@Override
	public String getCurrentDefaultCareerValue() {
		return listBoxCreate.getSelectedValue();
	}
	
	@Override
	public void setNewAnalyzedPlanButtonEnable(boolean b) {
		newAnalyzedPlanButton.setEnabled(b);
	}
	
	@Override
	public void setModelAnalyzedPlanButtonEnable(boolean b) {
		modelAnalyzedPlanButton.setEnabled(b);
	}

	@Override
	public void showWarning() {
		htmlPanelWarning.setVisible(true);
		htmlPanelWarning.getElement().setAttribute("style", "display: visible");
	}
	
	@Override
	public void hideWarning() {
		htmlPanelWarning.setVisible(false);
		htmlPanelWarning.getElement().setAttribute("style", "display: none");
	}

	public void addPlans(List<PlanValuesResult> planValuesList) {
		if(planValuesList != null && planValuesList.size() > 0){
			listBoxSelectPlanSaved.clear();
			for(PlanValuesResult values : planValuesList){
				listBoxSelectPlanSaved.addItem(values.getName(), values.getValue());
			}
		}
		
	}
	
	@Override
	public void onDeletePlanButtonClicked(ClickEvent e) {
		presenter.onDeletePlanButtonClicked(listBoxSelectPlanSaved.getSelectedValue());		
	}

	
	/********************* Firing Events *********************/
	
	@UiHandler("resetButton")
	void onResetButtonClicked(ClickEvent event){
		if(presenter != null){			
			presenter.onResetButtonClicked();
		}
	}
	
	@UiHandler("listBoxCreate")
	void onListBoxCreateChange(ChangeEvent event){
		setNewAnalyzedPlanButtonEnable(true);
		setModelAnalyzedPlanButtonEnable(true);
		if(presenter != null){			
			presenter.onListBoxCreateChange(listBoxCreate.getSelectedValue());
		}
	}
	
	@UiHandler("continueButton")
	void onContinueButtonClicked(ClickEvent event){
		String academicHistory = textBoxCreate.getText();
		if(presenter != null && academicHistory != null && academicHistory != ""){
			presenter.onContinueButtonClicked(academicHistory);
		}
	}
	
	@UiHandler("modelAnalyzedPlanButton")
	void onContinueDefaultButton(ClickEvent event){
		setNewAnalyzedPlanButtonEnable(true);
		setModelAnalyzedPlanButtonEnable(true);
		if(presenter!=null){			
			presenter.onContinueDefaultButtonClick(listBoxCreate.getSelectedValue());
		}
	}
	
	@UiHandler("newAnalyzedPlanButton")
	public void onNewAnalyzedPlanButtonClicked(ClickEvent event){
		presenter.onNewAnalyzedPlanButtonClicked(listBoxCreate.getSelectedValue());
	}
	
	@UiHandler("selectPlanButton")
	public void onSelectPlanButtonClicked(ClickEvent e){
		presenter.onSelectPlanButtonClicked(listBoxSelectPlanSaved.getSelectedValue());
	}
	
	@UiHandler("deletePlanButton")
	public void onDeltePlanButtonClicked(ClickEvent e){
		presenter.onDeletePlanButtonClicked(listBoxSelectPlanSaved.getSelectedValue());
	}

	
}
