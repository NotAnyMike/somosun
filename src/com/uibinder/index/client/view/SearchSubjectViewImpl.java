package com.uibinder.index.client.view;

import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelHeader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
		header.setWidth("800px");
	}

}
