package com.somosun.plan.client.index.view;

import java.util.Iterator;
import java.util.List;

import org.gwtbootstrap3.client.shared.event.HideEvent;
import org.gwtbootstrap3.client.shared.event.HideHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.control.Career;

public class SubjectAccordionViewImpl extends Composite implements SubjectAccordionView {
	
	PlanPresenter presenter;
	private int counter = 0;

	@UiField FlowPanel mainContainer;
	@UiField PanelHeader header;
	@UiField PanelCollapse accordionBody;
	@UiField HorizontalPanel infoHolderSearchSubject;
	@UiField Panel accordionContainer;
	@UiField Button addSpecificSubject;
	@UiField Button goTo1Button;
	@UiField Button goLeftButton;
	@UiField Button goRightButton;
	@UiField Button addSpecificRequisite;
	@UiField ButtonGroup navContainer;
	@UiField Label codeFieldSearchSubject;
	@UiField Label nameFieldSearchSubject;
	@UiField Label creditsFieldSearchSubject;
	@UiField Label typeFieldSearchSubject;
	@UiField HTML h1Name;
	@UiField HTML h1Code;
	@UiField HTMLPanel complementaryValueCurtain;
	
	HandlerRegistration onShowRegister;
	
	private static SubjectAccordionViewUiBinder uiBinder = GWT
			.create(SubjectAccordionViewUiBinder.class);

	@UiTemplate("SubjectAccordionView.ui.xml")
	interface SubjectAccordionViewUiBinder extends
			UiBinder<Widget, SubjectAccordionViewImpl> {
	}

	public SubjectAccordionViewImpl(int counter) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setCounter(counter);
		
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
		
		hideCurtain();
	}

	@Override
	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
		init();
	}

	@Override
	public void init() {
		this.getElement().setAttribute("position", "0");
		this.getElement().setAttribute("ammount", "0");
		
		h1Name.getElement().setAttribute("style", "display:inline-block");
		h1Code.getElement().setAttribute("style", "display:inline-block");
		
		typeFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		typeFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		
		creditsFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		creditsFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		
		goTo1Button.getElement().setAttribute("style", "outline:none");
		goLeftButton.getElement().setAttribute("style", "outline:none");
		goRightButton.getElement().setAttribute("style", "outline:none");
		
		complementaryValueCurtain.setStyleName("complementaryValue-curtainContainer isActive");
		
		accordionBody.addHideHandler(new HideHandler(){

			@Override
			public void onHide(HideEvent hideEvent) {
				GWT.log(hideEvent.toDebugString());
				goTo1();
			}
			
		});
	}
	
	private void setSubheader(String name, String code){
		h1Name.setText(name);
		h1Code.setText(code);
		
	}

	@Override
	public void setHeader(String code, String name, String type, String credits, String careerCode) {
		codeFieldSearchSubject.setText(code);
		nameFieldSearchSubject.setText(name);
		typeFieldSearchSubject.setText(type);
		
		setSubheader(name, code);
		
		typeFieldSearchSubject.setTitle("Libre Elección");
		
		creditsFieldSearchSubject.setText("c:" + credits);
		creditsFieldSearchSubject.setTitle("Créditos: " + credits);
		
		
		addSpecificSubject.getElement().setAttribute("code", code);
		addSpecificSubject.getElement().setAttribute("career", careerCode);
		addSpecificSubject.getElement().setAttribute("name", name);
		addSpecificSubject.getElement().setAttribute("state", "add");
		
		addSpecificRequisite.getElement().setAttribute("state", "add");
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
	

	public void changeSubheaderState() {
		String state = addSpecificRequisite.getElement().getAttribute("state");
		if(state.equals("add")){
			addSpecificRequisite.setType(ButtonType.DANGER);
			addSpecificRequisite.setIcon(IconType.MINUS);
			addSpecificRequisite.getElement().setAttribute("state", "remove");
		}else{
			addSpecificRequisite.setType(ButtonType.SUCCESS);
			addSpecificRequisite.setIcon(IconType.PLUS);
			addSpecificRequisite.getElement().setAttribute("state", "add");
		}
	}
	
	public String getCodeFromSubheader(){
		return h1Code.getText();
	}
	
	@Override
	public String getCode() {
		return addSpecificSubject.getElement().getAttribute("code");
	}
	
	
	@Override
	public void showError() {
		Alert error = new Alert("Ocurrió un error mientras buscabamos la información de esta asignatura, intentalo de nuevo :(");
		error.setType(AlertType.DANGER);
		error.getElement().setAttribute("style", "margin:0px 20px 20px 20px");
		
		accordionBody.add(error);
		
	}

	
	@Override
	public void addComplementaryView(final ComplementaryValueViewImpl view) {
		final SubjectAccordionViewImpl accordion = this;
		
		if(this.getElement().getAttribute("ammount").matches("()|(0)")){			
			onShowRegister = accordionBody.addShowHandler(new ShowHandler(){
				
				@Override
				public void onShow(ShowEvent showEvent) {
					presenter.onAccordionClicked(codeFieldSearchSubject.getText(), accordion, view);
					onShowRegister.removeHandler();
				}
				
			});
		}
		
		addComplementaryViewWidget(view.asWidget());
		
		
	}
	
	private void addComplementaryViewWidget(Widget w){
		int position = Integer.valueOf(this.getElement().getAttribute("position"));
		int ammount = Integer.valueOf(this.getElement().getAttribute("ammount"));
		
		validateNavigation();
		
		if(position + 2 == mainContainer.getWidgetCount() || ammount == 0){
			mainContainer.add(w);
			this.getElement().setAttribute("ammount", "" + (ammount+1));
		}else{
			mainContainer.insert(w,position+2);
			this.getElement().setAttribute("ammount", "" + (position+2));
			for(int x = position + 3; x <= ammount+1; x++){
				mainContainer.remove(position + 3);
			}
		}
	}
	
	private void validateNavigation() {
		
		int position = Integer.valueOf(this.getElement().getAttribute("position"));
		int ammount = Integer.valueOf(this.getElement().getAttribute("ammount"));
		
		if(ammount == 1 || ammount == 0){
			navContainer.setVisible(false);
		}else{
			navContainer.setVisible(true);
		
			if(position+1 == ammount) goRightButton.setEnabled(false);
			else goRightButton.setEnabled(true);
			
			if(position == 0) goLeftButton.setEnabled(false);
			else goLeftButton.setEnabled(true);
			
			if(position > 1) goTo1Button.setEnabled(true);
			else goTo1Button.setEnabled(false);
		}
		
		boolean isSelected = presenter.isSubjectSelected(h1Code.getText());
		if(isSelected){
			if(addSpecificRequisite.getElement().getAttribute("state").equals("add")){				
				this.changeSubheaderState();
			}
		}else{
			if(addSpecificRequisite.getElement().getAttribute("state").equals("add") == false){				
				this.changeSubheaderState();
			}
		}
		
	}
	
	public void goLeft(){
		int position = Integer.valueOf(this.getElement().getAttribute("position"));
		if(position - 1 >= 0) {

			Element currentElement = getNthElementInMainContainer(position+1);
			currentElement.getPreviousSiblingElement().removeClassName("isLeft");
			currentElement.addClassName("isRight");

			String name = currentElement.getPreviousSiblingElement().getAttribute("name");
			String code = currentElement.getPreviousSiblingElement().getAttribute("code");
			
			this.getElement().setAttribute("position", "" + (position - 1));

			setSubheader(name, code);

			validateNavigation();
			
		}
	}
	
	public void goRight(){
		int position = Integer.valueOf(this.getElement().getAttribute("position"));
		int ammount = Integer.valueOf(this.getElement().getAttribute("ammount"));
		if(position + 1 < ammount) {

			Element currentElement = getNthElementInMainContainer(position+1);
			currentElement.getNextSiblingElement().removeClassName("isRight");
			currentElement.addClassName("isLeft");
			
			String name = currentElement.getNextSiblingElement().getAttribute("name");
			String code = currentElement.getNextSiblingElement().getAttribute("code");
			
			this.getElement().setAttribute("position", "" + (position +1));

			setSubheader(name, code);
			
			validateNavigation();
			
		}
	}

	public void goTo1(){
		int position = Integer.valueOf(this.getElement().getAttribute("position"));
		
		GWT.debugger();
		
		if(position != 0) {

			Element firstElement = getNthElementInMainContainer(1);
			firstElement.removeClassName("isLeft");
			
			String name = firstElement.getAttribute("name");
			String code = firstElement.getAttribute("code");

			int currentPosition = 1;
			while(currentPosition != position){
				firstElement = firstElement.getNextSiblingElement();
				firstElement.setAttribute("style", "visibility:hidden");
				firstElement.removeClassName("isLeft");
				firstElement.addClassName("isRight");
				firstElement.removeAttribute("style");
				currentPosition++;
			}
			
			Element currentElement = getNthElementInMainContainer(position+1);
			currentElement.addClassName("isRight");
			
			this.getElement().setAttribute("position", "0");
			
			setSubheader(name, code);
			
			validateNavigation();
			
		}
	}
		
	private Element getNthElementInMainContainer(int i) {
		Element toReturn = mainContainer.getElement().getFirstChildElement();
		
		for(int x = 1; x <= i; x++){
			toReturn = toReturn.getNextSiblingElement();
		}
		
		return toReturn;
	}

	public void showCurtain(){
		complementaryValueCurtain.removeStyleName("isActive");
		//complementaryValueCurtain.setVisible(true); //FIXME: NOT WORKING - has a horrible effect
		complementaryValueCurtain.addStyleName("isActive");
	}
	
	public void hideCurtain(){
		complementaryValueCurtain.setVisible(false);
	}
	

	public int getAmmountOfCVViews() {
		return Integer.valueOf(this.getElement().getAttribute("ammount"));
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
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
	
	@UiHandler("addSpecificRequisite")
	public void onAddSpecificRequisiteClick(ClickEvent event){
		
		String name  = h1Name.getText();
		String code = h1Code.getText();
		String state = addSpecificRequisite.getElement().getAttribute("state");
		String career = addSpecificSubject.getElement().getAttribute("career");
		
		if(state.equals("add")){					
			presenter.onSpecificSubjectSelected(name, code, career);
		}else{
			presenter.onSpecificSubjectUnselected(name, code, career);
		}
		
	}

	@UiHandler("goTo1Button")
	public void goTo1(ClickEvent event){
		goTo1();
	}

	@UiHandler("goLeftButton")
	public void goLeft(ClickEvent event){
		goLeft();
	}
	
	@UiHandler("goRightButton")
	public void goRight(ClickEvent event){
		goRight();
	}
	
}
