package com.uibinder.index.client.view;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.shared.control.Career;

public class SubjectAccordionViewImpl extends Composite implements SubjectAccordionView {
	
	PlanPresenter presenter;
	int counter = 0;

	@UiField PanelHeader header;
	@UiField PanelCollapse accordionBody;
	@UiField Panel accordionContainer;
	@UiField Button addSpecificSubject;
	@UiField Label codeFieldSearchSubject;
	@UiField Label nameFieldSearchSubject;
	@UiField HorizontalPanel infoHolderSearchSubject;
	@UiField Label creditsFieldSearchSubject;
	@UiField Label typeFieldSearchSubject;
	
	@UiField ListBox listBoxCareers;
	@UiField Label labelSubjectGroup;
	@UiField VerticalPanel preRequisitesPanel;
	@UiField VerticalPanel coRequisitesPanel;
	@UiField FlowPanel antiPreRequisite;
	@UiField FlowPanel antiCoRequisite;
	
	@UiField Grid groupTableTitles;
	@UiField Grid spacesTable;
	@UiField Grid titleSpacesTable;
	@UiField VerticalPanel groupTableContainer;
	
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
		
		//clases necesarias
		listBoxCareers.addStyleName("form-control");
		preRequisitesPanel.addStyleName("table table-hover text-center");
		coRequisitesPanel.addStyleName("table table-hover text-center");
		preRequisitesPanel.getElement().setAttribute("style", "margin-bottom:0px");
		coRequisitesPanel.getElement().setAttribute("style", "margin-bottom:0px");
		spacesTable.getElement().setAttribute("style", "width:100%");
		titleSpacesTable.getElement().setAttribute("style", "width:100%");
		
		groupTableContainer.getElement().setAttribute("style", "width:100%");
		groupTableTitles.getElement().setAttribute("style", "width:100%; margin:0px");
		groupTableTitles.getColumnFormatter().setWidth(0, "45px");
		groupTableTitles.getColumnFormatter().setWidth(2, "50px");
		groupTableTitles.getColumnFormatter().setWidth(3, "63px");
		groupTableTitles.getCellFormatter().getElement(0, 4).setAttribute("style", "padding-left:0px; padding-right:0px");
		groupTableTitles.getColumnFormatter().setWidth(4, "60px");
		groupTableTitles.getColumnFormatter().setWidth(5, "36px");
		groupTableTitles.getColumnFormatter().setWidth(6, "36px");
		groupTableTitles.getColumnFormatter().setWidth(7, "36px");
		groupTableTitles.getColumnFormatter().setWidth(8, "36px");
		groupTableTitles.getColumnFormatter().setWidth(9, "36px");
		groupTableTitles.getColumnFormatter().setWidth(10, "36px");
		groupTableTitles.getColumnFormatter().setWidth(11, "36px");
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 4, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 5, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 6, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 7, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 8, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 9, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 10, HasVerticalAlignment.ALIGN_BOTTOM);
		groupTableTitles.getCellFormatter().setVerticalAlignment(0, 11, HasVerticalAlignment.ALIGN_BOTTOM);
		
		addSpecificSubject.getElement().setAttribute("onClick", "function(event){event.stopPropagation();}");
		
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
		typeFieldSearchSubject.setTitle("Libre Elección");
		typeFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		typeFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		creditsFieldSearchSubject.setText("c:" + credits);
		creditsFieldSearchSubject.setTitle("Créditos: " + credits);
		creditsFieldSearchSubject.getElement().setAttribute("data-toggle", "tooltip");
		creditsFieldSearchSubject.getElement().setAttribute("data-placement", "top");
		addSpecificSubject.getElement().setAttribute("code", code);
		addSpecificSubject.getElement().setAttribute("career", careerCode);
		addSpecificSubject.getElement().setAttribute("name", name);
		addSpecificSubject.getElement().setAttribute("state", "add");
	}

	@Override
	public void setCareerList(List<Career> careers) {
		for(Career career : careers){
			listBoxCareers.addItem(career.getName(), career.getCode());
		}
	}

	@Override
	public void setSubjectGroupName(String s) {
		labelSubjectGroup.setText("Agrupación: " + s);
	}
	
	@Override
	public void addRequisite(String type, String name, String code) {
		Anchor a = new Anchor(name);
		a.getElement().setAttribute("code", code);
		if(type == "pre"){
			if(preRequisitesPanel.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") preRequisitesPanel.remove(0);
			
			preRequisitesPanel.add(a);
			preRequisitesPanel.setCellHorizontalAlignment(a, HasHorizontalAlignment.ALIGN_CENTER);
		}
		else {
			if(coRequisitesPanel.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") coRequisitesPanel.remove(0);
			
			coRequisitesPanel.add(a);
			coRequisitesPanel.setCellHorizontalAlignment(a, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}
	
	@Override
	public void addAntiRequisite(String type, String name, String code) {
		
		
		Button b = new Button(name);
		b.getElement().setAttribute("code", code);
		b.addStyleName("btn btn-default btn-xs");
		
		if(type=="pre"){			
			if(antiPreRequisite.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") antiPreRequisite.remove(0);
			
			antiPreRequisite.add(b);	
		}
		else {
			if(antiCoRequisite.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") antiCoRequisite.remove(0);
			
			antiCoRequisite.add(b);
		}
	}
	
	@Override
	public void addGroup(String group, String professor, String professorGrade,
			String groupGrade, String averageGrade, String freeSpaces,
			String totalSpaces, String L, String M, String C, String J,
			String V, String S, String D) {
		
		if(groupTableContainer.getWidget(1).getElement().getClassName()=="fa fa-refresh fa-spin") groupTableContainer.remove(1);
		
		Grid groupTable = new Grid(1,13);
		groupTable.addStyleName("table table-hover");
		
		groupTable.setText(0, 0, group);
		groupTable.setText(0, 1, professor);
		groupTable.setText(0, 2, professorGrade);
		groupTable.setText(0, 3, averageGrade);
		groupTable.setText(0, 4, freeSpaces);
		groupTable.setText(0, 5, totalSpaces);
		groupTable.setText(0, 6, L);
		groupTable.setText(0, 7, M);
		groupTable.setText(0, 8, C);
		groupTable.setText(0, 9, J);
		groupTable.setText(0, 10, V);
		groupTable.setText(0, 11, S);
		groupTable.setText(0, 12, D);
		groupTable.getColumnFormatter().setWidth(0, "45px");
		groupTable.getColumnFormatter().setWidth(2, "57px");
		groupTable.getColumnFormatter().setWidth(3, "63px");
		groupTable.getColumnFormatter().setWidth(4, "30px");
		groupTable.getColumnFormatter().setWidth(5, "30px");
		groupTable.getColumnFormatter().setWidth(6, "36px");
		groupTable.getColumnFormatter().setWidth(7, "36px");
		groupTable.getColumnFormatter().setWidth(8, "36px");
		groupTable.getColumnFormatter().setWidth(9, "36px");
		groupTable.getColumnFormatter().setWidth(10, "36px");
		groupTable.getColumnFormatter().setWidth(11, "36px");
		groupTable.getColumnFormatter().setWidth(12, "36px");
		groupTable.getCellFormatter().getElement(0, 0).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 1).setClassName("text-left");
		groupTable.getCellFormatter().getElement(0, 2).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 3).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 4).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 5).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 6).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 7).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 8).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 9).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 10).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 11).setClassName("text-center");
		groupTable.getCellFormatter().getElement(0, 12).setClassName("text-center");
		
		groupTable.getElement().setAttribute("style", "margin:0px; vertical-aling:bottom");
		
		groupTableContainer.add(groupTable);
		
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

}
