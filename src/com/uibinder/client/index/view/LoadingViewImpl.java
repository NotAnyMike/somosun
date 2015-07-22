package com.uibinder.client.index.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LoadingViewImpl extends Composite implements LoadingView{
	
	private Presenter presenter;
	
	@UiField Label explanationLabel;

	private static LoadingViewUiBinder uiBinder = GWT
			.create(LoadingViewUiBinder.class);

	@UiTemplate("LoadingView.ui.xml")
	interface LoadingViewUiBinder extends UiBinder<Widget, LoadingViewImpl> {
	}

	public LoadingViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setLabel(String s) {
		explanationLabel.setText(s);
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

}
