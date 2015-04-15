package com.uibinder.index.client.view;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;

public class SearchSubjectViewImpl extends Composite implements SearchSubjectView{
	
	@UiField PanelHeader header;
	@UiField Label codeFieldSearchSubject;
	@UiField Label nameFieldSearchSubject;
	@UiField HorizontalPanel infoHolderSearchSubject;
	@UiField Label creditsFieldSearchSubject;
	@UiField Label typeFieldSearchSubject;
	@UiField PanelHeader header1;
	@UiField Label codeFieldSearchSubject1;
	@UiField Label nameFieldSearchSubject1;
	@UiField HorizontalPanel infoHolderSearchSubject1;
	@UiField Label creditsFieldSearchSubject1;
	@UiField Label typeFieldSearchSubject1;
	@UiField PanelHeader header2;
	@UiField Label codeFieldSearchSubject2;
	@UiField Label nameFieldSearchSubject2;
	@UiField HorizontalPanel infoHolderSearchSubject2;
	@UiField Label creditsFieldSearchSubject2;
	@UiField Label typeFieldSearchSubject2;
	@UiField PanelHeader header3;
	@UiField Label codeFieldSearchSubject3;
	@UiField Label nameFieldSearchSubject3;
	@UiField HorizontalPanel infoHolderSearchSubject3;
	@UiField Label creditsFieldSearchSubject3;
	@UiField Label typeFieldSearchSubject3;
	@UiField Button finalizarButton;
	
	@UiField ListBox listBoxCareers;
	@UiField Label labelSubjectGroup;
	@UiField VerticalPanel preRequisitesPanel;
	@UiField VerticalPanel coRequisitesPanel;
	@UiField FlowPanel antiPreRequisite;
	@UiField FlowPanel antiCoRequisite;
	
	@UiField Grid gradesTable;
	@UiField Grid spacesTable;
	@UiField Grid titleSpacesTable;
	
	private List<Button> buttons = new ArrayList<Button>();
	private List<Anchor> anchors = new ArrayList<Anchor>();
	
	private PlanPresenter presenter;
	private Label label = new Label("hola");
	private StackLayoutPanel test1 = new StackLayoutPanel(Unit.PX);
			
	private static SearchSubjectViewUiBinder uiBinder = GWT
			.create(SearchSubjectViewUiBinder.class);

	@UiTemplate("SearchSubjectView.ui.xml")
	interface SearchSubjectViewUiBinder extends
			UiBinder<Widget, SearchSubjectViewImpl> {
	}

	public SearchSubjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(PlanPresenter presenter) {
		this.presenter = presenter;
	}
	
	public void fill(){
		codeFieldSearchSubject.addStyleName("codeFieldSearchSubject");
		nameFieldSearchSubject.addStyleName("nameFieldSearchSubject");
		infoHolderSearchSubject.addStyleName("infoHolderSearchSubject");
		creditsFieldSearchSubject.addStyleName("creditsFieldSearchSubject");
		typeFieldSearchSubject.addStyleName("typeFieldSearchSubject");
		codeFieldSearchSubject1.addStyleName("codeFieldSearchSubject");
		nameFieldSearchSubject1.addStyleName("nameFieldSearchSubject");
		infoHolderSearchSubject1.addStyleName("infoHolderSearchSubject");
		creditsFieldSearchSubject1.addStyleName("creditsFieldSearchSubject");
		typeFieldSearchSubject1.addStyleName("typeFieldSearchSubject");
		codeFieldSearchSubject2.addStyleName("codeFieldSearchSubject");
		nameFieldSearchSubject2.addStyleName("nameFieldSearchSubject");
		infoHolderSearchSubject2.addStyleName("infoHolderSearchSubject");
		creditsFieldSearchSubject2.addStyleName("creditsFieldSearchSubject");
		typeFieldSearchSubject2.addStyleName("typeFieldSearchSubject");
		codeFieldSearchSubject3.addStyleName("codeFieldSearchSubject");
		nameFieldSearchSubject3.addStyleName("nameFieldSearchSubject");
		infoHolderSearchSubject3.addStyleName("infoHolderSearchSubject");
		creditsFieldSearchSubject3.addStyleName("creditsFieldSearchSubject");
		typeFieldSearchSubject3.addStyleName("typeFieldSearchSubject");
		
		
		//clases necesarias
		listBoxCareers.addStyleName("form-control");
		preRequisitesPanel.addStyleName("table table-hover text-center");
		coRequisitesPanel.addStyleName("table table-hover text-center");
		preRequisitesPanel.getElement().setAttribute("style", "margin-bottom:0px");
		coRequisitesPanel.getElement().setAttribute("style", "margin-bottom:0px");
		spacesTable.getElement().setAttribute("style", "width:100%");
		titleSpacesTable.getElement().setAttribute("style", "width:100%");
		
		gradesTable.getElement().setAttribute("style", "width:100%");
		gradesTable.insertRow(1);
		gradesTable.getColumnFormatter().setWidth(0, "45px");
		//gradesTable.getColumnFormatter().setWidth(1, "242px");
		gradesTable.getColumnFormatter().setWidth(2, "50px");
		gradesTable.getColumnFormatter().setWidth(3, "45px");
		gradesTable.getColumnFormatter().setWidth(4, "55px");
		gradesTable.getColumnFormatter().setWidth(5, "60px");
		gradesTable.getColumnFormatter().setWidth(6, "36px");
		gradesTable.getColumnFormatter().setWidth(7, "36px");
		gradesTable.getColumnFormatter().setWidth(8, "36px");
		gradesTable.getColumnFormatter().setWidth(9, "36px");
		gradesTable.getColumnFormatter().setWidth(10, "36px");
		gradesTable.getColumnFormatter().setWidth(11, "36px");
		gradesTable.getColumnFormatter().setWidth(12, "36px");


		setSubjectGroupName("Agrupación: Mamertismo cualitativo");
		
		addRequisite("pre", "Cálculo I", "0");
		addRequisite("pre", "Microeconomía I", "0");
		addRequisite("co", "Microeconomía II", "0");
		
		addAntiRequisite("pre", "Modelación dinámica","0");
		addAntiRequisite("pre", "Modelación microdiferencial","0");
		addAntiRequisite("co", "Micro dinámica","0");
		addAntiRequisite("co", "Ecuaciones mamertas","0");
		addAntiRequisite("co", "Mamertos del siglo XXI: su discurso y reproducción","0");
		addAntiRequisite("co", "Logica de los paros: el bloqueo como herramienta fundamental","0");
		
		
		finalizarButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				hideIt();
			}});
	}

	@Override
	public void hideIt() {
		this.asWidget().setVisible(false);
	}

	@Override
	public void showIt() {
		this.asWidget().setVisible(true);
	}

	public void addCareerToListBox(String name, String code) {
		listBoxCareers.addItem(name, code);
	}

	@Override
	public void addAntiRequisite(String type, String name, String code) {
		Button b = new Button(name);
		b.getElement().setAttribute("code", code);
		b.addStyleName("btn btn-default btn-xs");
		buttons.add(b);
		
		if(type=="pre")
			antiPreRequisite.add(b);	
		else 
			antiCoRequisite.add(b);
	}

	@Override
	public void addRequisite(String type, String name, String code) {
		Anchor a = new Anchor(name);
		a.getElement().setAttribute("code", code);
		anchors.add(a);
		if(type == "pre"){
			preRequisitesPanel.add(a);
			preRequisitesPanel.setCellHorizontalAlignment(a, HasHorizontalAlignment.ALIGN_CENTER);
		}
		else {
			coRequisitesPanel.add(a);
			coRequisitesPanel.setCellHorizontalAlignment(a, HasHorizontalAlignment.ALIGN_CENTER);
		}
	}

	@Override
	public void setSubjectGroupName(String s) {
		labelSubjectGroup.setText("Agrupación: " + s);
	}

}
