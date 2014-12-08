package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.presenter.PlanPresenter;

public class PlanWidget extends HorizontalPanel {
	
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
	
	@Override
	public void add(Widget w){
		super.insert(w, this.getWidgetCount()-1);
	}
	
}
