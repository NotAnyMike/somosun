package com.somosun.plan.client.admin.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.admin.presenter.PlanAdminPresenter;

public class PlanAdminViewImpl extends Composite implements PlanAdminView{

	private static PlanAdminViewUiBinder uiBinder = GWT.create(PlanAdminViewUiBinder.class);
	
	private PlanAdminPresenter presenter;
	
	@UiField VerticalPanel plansToShowVerticalPanel;

	@UiTemplate("PlanAdminView.ui.xml")
	interface PlanAdminViewUiBinder extends UiBinder<Widget, PlanAdminViewImpl> {
	}

	public PlanAdminViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(PlanAdminPresenter presenter) {
		this.presenter = presenter;
		init();
	}

	public void init() {
		plansToShowVerticalPanel.addStyleName("table table-striped");
		clearPlanContainer();
	}

	public void addPlanString(String s) {
		if(s != null){
			plansToShowVerticalPanel.add(new HTML(s));
		}
	}

	public void clearPlanContainer() {
		plansToShowVerticalPanel.clear();
	}

}
