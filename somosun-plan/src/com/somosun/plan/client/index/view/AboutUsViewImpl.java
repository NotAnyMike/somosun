package com.somosun.plan.client.index.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.presenter.Presenter;

public class AboutUsViewImpl extends Composite implements AboutUsView {
	
	private Presenter presenter;

	private static AboutUsViewUiBinder uiBinder = GWT
			.create(AboutUsViewUiBinder.class);

	@UiTemplate("AboutUsView.ui.xml")
	interface AboutUsViewUiBinder extends UiBinder<Widget, AboutUsViewImpl> {
	}

	public AboutUsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
