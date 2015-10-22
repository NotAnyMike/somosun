package com.somosun.plan.client.index.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlanViewImpl extends Composite implements PlanView {
	
	private Presenter presenter;
	
	@UiField HTMLPanel loadingSubjectsHTMLPanel;
	@UiField HTMLPanel changeNameHTMLPanel;
	@UiField HTMLPanel curtainHTMLPanel;
	@UiField HTMLPanel generalConfirmationHTMLPanel;
	
	@UiField Button cancelChangeNameButton;
	@UiField Button changeNameButton;
	@UiField Button cancelGeneralButton;
	@UiField Button deleteGeneralButton;
	
	@UiField TextBox textBoxChangeNamePlan;
	
	@UiField Label generalLabel;

	private static PlanViewUiBinder uiBinder = GWT
			.create(PlanViewUiBinder.class);

	@UiTemplate("PlanView.ui.xml")
	interface PlanViewUiBinder extends UiBinder<Widget, PlanViewImpl> {
	}

	public PlanViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancelChangeNameButton.addStyleName("margin");
		changeNameButton.addStyleName("margin");
		loadingSubjectsHTMLPanel.addStyleName("mainContainerLoadPlan");
		
		generalLabel.setStyleName("margin");
		
		hidePopups();
		
	}

	public void hidePopups() {
		hideCurtain();
		hideChangeName();
		hideGeneralPopup();
	}
	
	private void hideGeneralPopup() {
		generalConfirmationHTMLPanel.asWidget().setVisible(false);
	}
	
	public void showGeneralPopup(){
		if(deleteGeneralButton.getElement().getAttribute("plan").isEmpty() == false){
			generalLabel.setText("Seguro que quieres eliminar tu plan de estudios '" + deleteGeneralButton.getElement().getAttribute("plan") + "'");
		}else{
			generalLabel.setText("Seguro que quieres eliminar el semestre " + deleteGeneralButton.getElement().getAttribute("semester"));
		}
		generalConfirmationHTMLPanel.asWidget().setVisible(true);
		deleteGeneralButton.setFocus(true);
		textBoxChangeNamePlan.setFocus(false);
		showCurtain();
		
	}
	
	public void setSemester(String s){
		deleteGeneralButton.getElement().setAttribute("semester", s);
		deleteGeneralButton.getElement().setAttribute("plan", "");
	}
	
	/**
	 * Sets the name of the plan as a attribute in the deleteGeneralButton 
	 * @param name
	 */
	public void setPlan(String name){
		deleteGeneralButton.getElement().setAttribute("semester", "");
		deleteGeneralButton.getElement().setAttribute("plan", name);
	}

	public void showChangeName(){
		changeNameHTMLPanel.asWidget().setVisible(true);
		textBoxChangeNamePlan.setFocus(true);
		deleteGeneralButton.setFocus(false);
	}
	
	public void hideChangeName(){
		changeNameHTMLPanel.asWidget().setVisible(false);
	}
	
	public void showCurtain(){
		curtainHTMLPanel.asWidget().setVisible(true);
	}
	
	public void hideCurtain(){
		curtainHTMLPanel.asWidget().setVisible(false);
	}
	
	public void setSugestionText(String s){
		textBoxChangeNamePlan.setValue(s);
	}
	
	public void doSavePlanButton(){
		presenter.planNameChanged(textBoxChangeNamePlan.getValue());
	}
	
	public void showLoadingSubjects() {
		//loadingSubjectsHTMLPanel.setVisible(true);
		loadingSubjectsHTMLPanel.addStyleName("active");
	}
	
	public void hideLoadingSubjects() {
		loadingSubjectsHTMLPanel.removeStyleName("active");
		//loadingSubjectsHTMLPanel.setVisible(false);
	}
	
	/********************** Handlers ***************************/

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("cancelChangeNameButton")
	public void cancelChangeNameButtonClicked(ClickEvent e){
		hidePopups();
	}

	@UiHandler("changeNameButton")
	public void savePlanButton(ClickEvent e){
		doSavePlanButton();
	}
	
	@UiHandler("cancelGeneralButton")
	public void cancelGeneralButton(ClickEvent e){
		hidePopups();
	}
	
	@UiHandler("deleteGeneralButton")
	public void onDeleteGeneralButtonClick(ClickEvent e){
		if(deleteGeneralButton.getElement().getAttribute("plan").isEmpty() == false){
			presenter.deletePlanConfirmed();
		}else{
			presenter.deleteSemesterConfirmed(Integer.valueOf(deleteGeneralButton.getElement().getAttribute("semester")));
		}
	}
	
	@UiHandler("textBoxChangeNamePlan")
	public void onEnterInTextBoxChangeNamePlan(KeyPressEvent e){
		if(e.getUnicodeCharCode() == KeyCodes.KEY_ENTER){
			doSavePlanButton();
		}
		e.stopPropagation();
	}
	
}
