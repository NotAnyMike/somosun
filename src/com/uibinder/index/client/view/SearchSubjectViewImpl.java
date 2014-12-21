package com.uibinder.index.client.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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

}
