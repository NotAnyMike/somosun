package com.uibinder.index.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopBarViewImpl extends Composite implements TopBarView {
	private Presenter presenter;
	
	@UiField Label pageNameLabel;
	@UiField Label userNameLabel;

	private static TopBarViewUiBinder uiBinder = GWT
			.create(TopBarViewUiBinder.class);

	@UiTemplate("TopBarView.ui.xml")
	interface TopBarViewUiBinder extends UiBinder<Widget, TopBarViewImpl> {
	}

	public TopBarViewImpl() {
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

	@Override
	public void setUserName(String s) {
		userNameLabel.setText(s);
	}
	
	@Override
	public void setMainLabel(String s) {
		pageNameLabel.setText(s);
	}

	@Override
	public void setMainLabelTitle(String s) {
		pageNameLabel.setTitle(s);
	}

}
