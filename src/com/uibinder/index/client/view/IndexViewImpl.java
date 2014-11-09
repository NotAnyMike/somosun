package com.uibinder.index.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class IndexViewImpl extends Composite implements IndexView {
	
	@UiTemplate("IndexView.ui.xml")
	interface IndexViewUiBinder extends UiBinder<Widget, IndexViewImpl> {}
	private static IndexViewUiBinder uiBinder = GWT
			.create(IndexViewUiBinder.class);
	
	@UiField Anchor createButton;
	@UiField Anchor commingSoonButton;
	@UiField Anchor aboutUsButton;
	@UiField Anchor announcementButton;

	private Presenter presenter;
	
	
	public IndexViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}
}
