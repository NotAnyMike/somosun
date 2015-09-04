package com.somosun.plan.client.index.view;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;

public class SearchSubjectViewImpl extends Composite implements SearchSubjectView{
	
	@UiField PanelGroup accordionContainer;
	
	@UiField ListBox listBoxCareersToSearch;
	@UiField Button finalizarButton;
	@UiField Button searchButton;
	@UiField Button cancelButton;
	@UiField TextBox searchField;
	
	@UiField Radio radioButton1;
	@UiField Radio radioButton2;
	@UiField Radio radioButton3;
	@UiField Radio radioButton4;
	@UiField Radio radioButton5;
	
	@UiField VerticalPanel subjectsSelectedVerticalPanel;
	
	@UiField FormGroup checkButtons;
	@UiField Pagination pagination;
	
	private PlanPresenter presenter;
			
	private static SearchSubjectViewUiBinder uiBinder = GWT
			.create(SearchSubjectViewUiBinder.class);

	@UiTemplate("SearchSubjectView.ui.xml")
	interface SearchSubjectViewUiBinder extends
			UiBinder<Widget, SearchSubjectViewImpl> {
	}

	public SearchSubjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		accordionContainer.getElement().setAttribute("style", "width:804px");
	}

	@Override
	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
	}
	
	public void fill(){
		
		radioButton1.getElement().setAttribute("style", "margin-bottom:0px; margin-top:0px");
		radioButton1.setValue(true);
		radioButton2.getElement().setAttribute("style", "margin-bottom:0px");
		radioButton3.getElement().setAttribute("style", "margin-bottom:0px");
		radioButton4.getElement().setAttribute("style", "margin-bottom:0px");
		radioButton5.getElement().setAttribute("style", "margin-bottom:0px");
		subjectsSelectedVerticalPanel.setStyleName("table");
		subjectsSelectedVerticalPanel.addStyleName("table-hover table-condensed");
		subjectsSelectedVerticalPanel.getElement().setAttribute("style", "margin:0px");
		
	}
	
	private String getCheckBoxesValue() {
		if(radioButton1.getValue() == true) return radioButton1.getFormValue();
		else if(radioButton2.getValue() == true) return radioButton2.getFormValue();
		else if(radioButton3.getValue() == true) return radioButton3.getFormValue();
		else if(radioButton4.getValue() == true) return radioButton4.getFormValue();
		else return radioButton5.getFormValue();
	}

	@Override
	public void hideIt() {
		this.asWidget().setVisible(false);
	}

	@Override
	public void showIt() {
		this.asWidget().setVisible(true);
		searchField.setFocus(true);
	}

	public void addCareerToListBox(String name, String code) {
		listBoxCareersToSearch.addItem(name, code);
	}

	@Override
	public void addSubject(SubjectAccordionView view) {
		accordionContainer.add(view.asWidget());
	}

	@Override
	public int getSubjectsAmmount() {
		return accordionContainer.getWidgetCount();
	}

	@Override
	public void setCareerList(List<Career> careers, Career careerSelected) {
		addCareerToListBox("Todas", "");
		for(Career career : careers){
			addCareerToListBox(SomosUNUtils.addCarrerCodeToString(career), career.getCode());
			if(careerSelected != null) 
			{
				if(career.getCode().equals(careerSelected.getCode()))
				{
					int index = careers.indexOf(career);
					listBoxCareersToSearch.setItemSelected(index+1, true);
				}
				
			}
		}
	}

	public void addPage(Widget page) {
		pagination.add(page);
	}

	public void clearPagination() {
		pagination.clear();
	}

	public void clearAccordionContainer() {
		accordionContainer.clear();
	}

	@Override
	public void addSelectedSubject(Widget w) {
		subjectsSelectedVerticalPanel.add(w);
	}
	
	@Override
	public void clear() {
		
		subjectsSelectedVerticalPanel.clear();
		accordionContainer.clear();
		searchField.clear();
		pagination.clear();
		
	}
	
	@Override
	public void setSemester(String semester) {
		finalizarButton.getElement().setAttribute("semester", semester);
	}
	
	public String getCareerCodeSelected(){
		return listBoxCareersToSearch.getSelectedValue();
	}
	
	/********************* EVENTS *********************/
	
	@UiHandler("finalizarButton")
	public void onFinalizarButtonClick(ClickEvent event){
		hideIt();
		presenter.onFinalizarButtonClick(finalizarButton.getElement().getAttribute("semester"));
	}
	
	@UiHandler("searchButton")
	public void onSearchButtonClick(ClickEvent event){
		presenter.onSearchButtonClicked(searchField.getText(), listBoxCareersToSearch.getSelectedValue(), getCheckBoxesValue(), 1);
	}
	
	@UiHandler("cancelButton")
	public void onCancelButtonClick(ClickEvent e){
		presenter.hideSearchBox();
	}

}
