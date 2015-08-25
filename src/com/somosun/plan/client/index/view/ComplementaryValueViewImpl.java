package com.somosun.plan.client.index.view;

import java.util.List;

import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.control.Career;

public class ComplementaryValueViewImpl extends Composite implements ComplementaryValueView {

	PlanPresenter presenter;
	
	@UiField ListBox listBoxCareers;
	@UiField Label labelSubjectGroup;
	@UiField VerticalPanel preRequisitesPanel;
	@UiField VerticalPanel coRequisitesPanel;
	@UiField FlowPanel antiPreRequisite;
	@UiField FlowPanel antiCoRequisite;
	
	@UiField PanelCollapse groupsTableContainer;
	@UiField PanelHeader panelHeaderGroups;
	
	@UiField Grid groupTableTitles;
	@UiField Grid spacesTable;
	@UiField Grid titleSpacesTable;
	@UiField VerticalPanel groupTableContainer;
	@UiField HTMLPanel mainHtmlPanel;
	
	HandlerRegistration onGroupsShows;
	
	private static ComplementaryValueViewUiBinder uiBinder = GWT.create(ComplementaryValueViewUiBinder.class);

	@UiTemplate("ComplementaryValueView.ui.xml")
	interface ComplementaryValueViewUiBinder extends UiBinder<Widget, ComplementaryValueViewImpl> {
	}

	public ComplementaryValueViewImpl(String name, String subjectCode, SubjectAccordionViewImpl accordion, int counterOfAccordions, int ammountOfThis) {
		initWidget(uiBinder.createAndBindUi(this));
		
		createAccordionFunctionality(counterOfAccordions, ammountOfThis);
		init();		
		bind(subjectCode, accordion);
		
		this.getElement().setAttribute("name", name);
		this.getElement().setAttribute("code", subjectCode);
		
	}
	

	private void createAccordionFunctionality(int counter, int ammountOfThis) {
		groupsTableContainer.getElement().setAttribute("id", "collapseGroups" + counter + "-" + ammountOfThis);
		panelHeaderGroups.getElement().setAttribute("data-target","#collapseGroups" + counter + "-" + ammountOfThis);
	}


	private void bind(final String subjectCode, final SubjectAccordionViewImpl accordion) {
		
		final ComplementaryValueViewImpl view = this;
		
		listBoxCareers.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				presenter.onAccordionClicked(subjectCode, accordion, view);
			}
			
		});
		
		onGroupsShows = groupsTableContainer.addShowHandler(new ShowHandler(){

			@Override
			public void onShow(ShowEvent showEvent) {
				presenter.onGroupsAccordionShows(subjectCode, view);
				onGroupsShows.removeHandler();
			}
			
		});
		
	}


	@Override
	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void init() {
		
		mainHtmlPanel.addStyleName("complementaryValue-accordionContainer");
		
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
		
	}
	
	public void setCareerList(List<Career> careers, String careerCodeSelected) {
		int index = -1;
		boolean found = false;
		for(Career career : careers){
			if(found == false && careerCodeSelected.isEmpty() == false){				
				index ++;
			}
			if(career.getCode().equals(careerCodeSelected)) found = true;
			
			listBoxCareers.addItem(career.getName(), career.getCode());
		}
		
		if(index != -1 ){			
			listBoxCareers.setItemSelected(index, true);
		}
	}
	
	public void setSubjectGroupName(String s) {
		labelSubjectGroup.setText("Agrupación: " + s);
	}
	
	public void addRequisite(String type, final String name, final String code, final SubjectAccordionViewImpl accordion, boolean makeStatic) {
		
		final ComplementaryValueViewImpl view = this;
		final String careerCode = listBoxCareers.getSelectedValue();
		
		Widget w = null;
		if(makeStatic == false){			
			Anchor a = new Anchor(name);
			a.getElement().setAttribute("code", code);
			a.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					presenter.addComplementaryValueView(accordion, name, code, careerCode);
				}
			});
			w = a.asWidget();
		}else{
			HTML a = new HTML(name);
			a.getElement().setAttribute("code", code);
			a.getElement().setAttribute("style", "cursor: default");
			w = a.asWidget();
		}
		
		if(type == "pre"){
			if(preRequisitesPanel.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") preRequisitesPanel.remove(0);
			
			preRequisitesPanel.add(w);
			preRequisitesPanel.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
			
		}
		else {
			if(coRequisitesPanel.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") coRequisitesPanel.remove(0);
			
			coRequisitesPanel.add(w);
			coRequisitesPanel.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		}
		
		
	}
	
	public void addAntiRequisite(String type, final String name, final String code, final SubjectAccordionViewImpl accordion, boolean makeStatic) {
		
		final ComplementaryValueViewImpl view = this;
		final String careerCode = listBoxCareers.getSelectedValue();
		
		Button b = new Button(name);
		b.getElement().setAttribute("code", code);
		b.getElement().setAttribute("style", "outline:none");
		b.addStyleName("btn btn-default btn-xs");
		if(makeStatic == false){	
			b.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					presenter.addComplementaryValueView(accordion, name, code, careerCode);
				}
			});
		}else{
			b.setActive(false);			
		}
		
		if(type=="pre"){			
			if(antiPreRequisite.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") antiPreRequisite.remove(0);
			
			antiPreRequisite.add(b);	
		}
		else {
			if(antiCoRequisite.getWidget(0).getElement().getClassName()=="fa fa-refresh fa-spin") antiCoRequisite.remove(0);
			
			antiCoRequisite.add(b);
		}
	}
	
	public void addGroup(String group, String professor, String professorGrade,
			String groupGrade, String averageGrade, String freeSpaces,
			String totalSpaces, String L, String classRoomL, String M, String classRoomM, String C, String classRoomC, String J, String classRoomJ,
			String V, String classRoomV, String S, String classRoomS, String D, String classRoomD) {
		
		if(groupTableContainer.getWidget(1).getElement().getClassName()=="fa fa-refresh fa-spin") groupTableContainer.remove(1);
		
		Grid groupTable = new Grid(1,13);
		groupTable.addStyleName("table table-hover complementaryValue groupsTable");
		
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
		groupTable.getCellFormatter().getElement(0, 6).setTitle(classRoomL);
		groupTable.getCellFormatter().getElement(0, 7).setTitle(classRoomM);
		groupTable.getCellFormatter().getElement(0, 8).setTitle(classRoomC);
		groupTable.getCellFormatter().getElement(0, 9).setTitle(classRoomJ);
		groupTable.getCellFormatter().getElement(0, 10).setTitle(classRoomV);
		groupTable.getCellFormatter().getElement(0, 11).setTitle(classRoomS);
		groupTable.getCellFormatter().getElement(0, 12).setTitle(classRoomD);
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
		groupTable.getCellFormatter().getElement(0, 6).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 7).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 8).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 9).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 10).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 11).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 12).setClassName("text-center complementaryValue days");
		groupTable.getCellFormatter().getElement(0, 6).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 7).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 8).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 9).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 10).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 11).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 12).setAttribute("data-toggle", "tooltip");
		groupTable.getCellFormatter().getElement(0, 6).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 7).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 8).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 9).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 10).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 11).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 12).setAttribute("data-placement", "top");
		groupTable.getCellFormatter().getElement(0, 6).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 7).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 8).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 9).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 10).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 11).setAttribute("data-container", "body");
		groupTable.getCellFormatter().getElement(0, 12).setAttribute("data-container", "body");
//		data-toggle="tooltip" data-placement="top"
		
		groupTable.getElement().setAttribute("style", "margin:0px; vertical-aling:bottom");
		
		groupTableContainer.add(groupTable);
		
	}
	
	public void hasNoPrerequisites() {
		preRequisitesPanel.clear();
		HTML none = new HTML("-");
		preRequisitesPanel.add(none);
	}

	public void hasNoCorequisites() {
		coRequisitesPanel.clear();
		HTML none = new HTML("-");
		coRequisitesPanel.add(none);
	}


	@Override
	public void isNoCorequisite() {
		// TODO Auto-generated method stub
		antiCoRequisite.clear();
		HTML html = new HTML("-");
		antiCoRequisite.add(html);
	}


	@Override
	public void isNoPrerequisite() {
		antiPreRequisite.clear();
		HTML html = new HTML("-");
		antiPreRequisite.add(html);
	}


	@Override
	public void showGroupError() {
		Alert alert = new Alert("Error descargando los grupos desde el sia");
		alert.setType(AlertType.DANGER);
		groupTableContainer.add(alert);
	}
	
	public void hasNoGroups(){
		groupTableContainer.getWidget(1).removeFromParent();
		HTML html = new HTML("-");
		groupTableContainer.add(html);
	}

}
