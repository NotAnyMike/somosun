package com.uibinder.index.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AboutGedadViewImpl extends Composite implements AboutGedadView {
	
	private Presenter presenter;

	private static AboutGedadViewUiBinder uiBinder = GWT
			.create(AboutGedadViewUiBinder.class);

	@UiTemplate("AboutGedadView.ui.xml")
	interface AboutGedadViewUiBinder extends UiBinder<Widget, AboutGedadViewImpl> {
	}

	public AboutGedadViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}

}
