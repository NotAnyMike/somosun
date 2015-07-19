package com.uibinder.index.client.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.event.GradeChangeEvent;
import com.uibinder.index.client.event.GradeChangeEventHandler;

public class PlanWidget extends HorizontalPanel implements HasChangeHandlers {
	
	private Image addSemesterImage = new Image("images/addSemester.png");
	private Label label = new Label(" add");
	private HorizontalPanel addSemesterPanel = new HorizontalPanel();
	
	public PlanWidget(){
		this.addStyleName("planPanel");
		
		addSemesterPanel.add(addSemesterImage);
		addSemesterPanel.add(label);
		addSemesterPanel.addStyleName("buttonCursor");
		super.add(addSemesterPanel);
		
	}
	
	public Label getLabelAddSemester(){
		return label;
	}
	
	public Image getImageAddSemester(){
		return addSemesterImage;
	}
	
	public void addSemesterWidget(SemesterWidget sW) {
		
		this.fireEvent(new GradeChangeEvent("SemesterAdd"));
		
		add(sW.asWidget());
	}

	@Override
	public void add(Widget w){
		super.insert(w, this.getWidgetCount()-1);
	}


	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}
	
}
