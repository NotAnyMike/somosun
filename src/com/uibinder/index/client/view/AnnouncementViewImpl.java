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

public class AnnouncementViewImpl extends Composite implements AnnouncementView {
	
	private Presenter presenter;

	private static AboutGedadViewUiBinder uiBinder = GWT
			.create(AboutGedadViewUiBinder.class);

	@UiTemplate("AnnouncementView.ui.xml")
	interface AboutGedadViewUiBinder extends UiBinder<Widget, AnnouncementViewImpl> {
	}

	public AnnouncementViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
