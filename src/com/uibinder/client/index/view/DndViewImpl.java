package com.uibinder.client.index.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DndViewImpl extends Composite implements DndView {
	
	private Presenter presenter;

	private static DadViewUiBinder uiBinder = GWT.create(DadViewUiBinder.class);

	@UiTemplate("DadView.ui.xml")
	interface DadViewUiBinder extends UiBinder<Widget, DndViewImpl> {
	}

	public DndViewImpl() {
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
