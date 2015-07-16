package com.uibinder.index.client.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class PlanViewImpl extends Composite implements PlanView {
	
	private Presenter presenter;
	
	@UiField HTMLPanel changeNameHTMLPanel;
	@UiField HTMLPanel curtainHTMLPanel;
	@UiField Button cancelChangeNameButton;
	@UiField Button changeNameButton;
	
	@UiField TextBox textBoxChangeNamePlan;

	private static PlanViewUiBinder uiBinder = GWT
			.create(PlanViewUiBinder.class);

	@UiTemplate("PlanView.ui.xml")
	interface PlanViewUiBinder extends UiBinder<Widget, PlanViewImpl> {
	}

	public PlanViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		cancelChangeNameButton.addStyleName("margin");
		changeNameButton.addStyleName("margin");
		
		hidePopups();
		
	}
	
	public void hidePopups() {
		hideCurtain();
		hideChangeName();
	}
	
	public void showChangeName(){
		changeNameHTMLPanel.asWidget().setVisible(true);
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
	public void changeNameButton(ClickEvent e){
		presenter.planNameChanged(textBoxChangeNamePlan.getValue());
	}
	
}
