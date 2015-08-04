package com.uibinder.client.index.view;

import java.util.List;

import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.presenter.PlanPresenter;
import com.uibinder.shared.control.Career;

public class SubjectAccordionViewImpl extends Composite implements SubjectAccordionView {
	
	PlanPresenter presenter;
	int counter = 0;

	@UiField HTMLPanel mainContainer;
	@UiField PanelHeader header;
	@UiField PanelCollapse accordionBody;
	@UiField HorizontalPanel infoHolderSearchSubject;
	@UiField Panel accordionContainer;
	@UiField Button addSpecificSubject;
	@UiField Button goTo1Button;
	@UiField Button goLeftButton;
	@UiField Button goRightButton;
	@UiField Label codeFieldSearchSubject;
	@UiField Label nameFieldSearchSubject;
	@UiField Label creditsFieldSearchSubject;
	@UiField Label typeFieldSearchSubject;
	@UiField HTML h1Name;
	@UiField HTML h1Code;
	
	HandlerRegistration onShowRegister;
	
	private static SubjectAccordionViewUiBinder uiBinder = GWT
			.create(SubjectAccordionViewUiBinder.class);

	@UiTemplate("SubjectAccordionView.ui.xml")
	interface SubjectAccordionViewUiBinder extends
			UiBinder<Widget, SubjectAccordionViewImpl> {
	}

	public SubjectAccordionViewImpl(int counter) {
		initWidget(uiBinder.createAndBindUi(this));
		this.counter = counter;
		
		accordionContainer.getElement().setAttribute("style", "margin-bottom:5px");
		header.getElement().setAttribute("data-target", "#collapse" + counter);
		accordionBody.getElement().setAttribute("id", "collapse" + counter);
		
		codeFieldSearchSubject.addStyleName("codeFieldSearchSubject");
		nameFieldSearchSubject.addStyleName("nameFieldSearchSubject");
		infoHolderSearchSubject.addStyleName("infoHolderSearchSubject");
		creditsFieldSearchSubject.addStyleName("creditsFieldSearchSubject");
		typeFieldSearchSubject.addStyleName("typeFieldSearchSubject");
		
		addSpecificSubject.getElement().setAttribute("onClick", "function(event){event.stopPropagation();}");
		
		mainContainer.setStyleName("complementaryValue-mainContainer");
	}

	@Override
	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
		init();
	}

	@Override
	public void setHeader(String code, String name, String type, String credits, String careerCode) {
		codeFieldSearchSubject.setText(code);
		nameFieldSearchSubject.setText(name);
		typeFieldSearchSubject.setText(type);
		
		h1Name.setText(name);
		h1Code.setText("(" + code + ")");
		
		typeFieldSearchSubject.setTitle("Libre Elección");
		
		creditsFieldSearchSubject.setText("c:" + credits);
		creditsFieldSearchSubject.setTitle("Créditos: " + credits);
		
		
		addSpecificSubject.getElement().setAttribute("code", code);
		addSpecificSubject.getElement().setAttribute("career", careerCode);
		addSpecificSubject.getElement().setAttribute("name", name);
		addSpecificSubject.getElement().setAttribute("state", "add");
	}
	
	public void changeState() {
		String state = addSpecificSubject.getElement().getAttribute("state");
		if(state.equals("add")){
			addSpecificSubject.setType(ButtonType.DANGER);
			addSpecificSubject.setIcon(IconType.MINUS);
			addSpecificSubject.getElement().setAttribute("state", "remove");
		}else{
			addSpecificSubject.setType(ButtonType.SUCCESS);
			addSpecificSubject.setIcon(IconType.PLUS);
			addSpecificSubject.getElement().setAttribute("state", "add");
		}
	}

	@Override
	public void init() {
		this.getElement().setAttribute("position", "1");
		this.getElement().setAttribute("ammount", "1");
		
		h1Name.getElement().setAttribute("style", "display:inline-block");
		h1Code.getElement().setAttribute("style", "display:inline-block; padding-left:10px");
		
		typeFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		typeFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		
		creditsFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		creditsFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		
		goTo1Button.getElement().setAttribute("style", "outline:none");
		goLeftButton.getElement().setAttribute("style", "outline:none");
		goRightButton.getElement().setAttribute("style", "outline:none");
	}
	
	@Override
	public String getCode() {
		return addSpecificSubject.getElement().getAttribute("code");
	}
	
	/*************************** EVENTS ***************************/
	@UiHandler("addSpecificSubject")
	public void onAddSpecificSubjectClick(ClickEvent event){
		
		String name  = addSpecificSubject.getElement().getAttribute("name");
		String code = addSpecificSubject.getElement().getAttribute("code");
		String career = addSpecificSubject.getElement().getAttribute("career");
		String state = addSpecificSubject.getElement().getAttribute("state");
		
		if(state.equals("add")){					
			presenter.onSpecificSubjectSelected(name, code, career);
		}else{
			presenter.onSpecificSubjectUnselected(name, code, career);
		}

	}

	@Override
	public void showError() {
		Alert error = new Alert("Ocurrió un error mientras buscabamos la información de esta asignatura, intentalo de nuevo :(");
		error.setType(AlertType.DANGER);
		error.getElement().setAttribute("style", "margin:0px 20px 20px 20px");
		
		accordionBody.add(error);
		
	}

	
	@Override
	public void addMainComplementaryView(final ComplementaryValueViewImpl view) {
		final SubjectAccordionViewImpl accordion = this;
		
		onShowRegister = accordionBody.addShowHandler(new ShowHandler(){

			@Override
			public void onShow(ShowEvent showEvent) {
				presenter.onAccordionClicked(codeFieldSearchSubject.getText(), accordion, view);
				onShowRegister.removeHandler();
			}
			
		});
		
		mainContainer.add(view.asWidget());
		
	}

}
