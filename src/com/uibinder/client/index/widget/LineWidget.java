package com.uibinder.client.index.widget;

import com.google.gwt.user.client.ui.SimplePanel;

public class LineWidget extends SimplePanel{
	
	public LineWidget(double width, double top, double left, double angle){
		this.addStyleName("dline");
		setPoints(width, top, left, angle);
	}

	public void hide(){
		this.setVisible(false);
	}
	
	public void show(){
		this.setVisible(true);
	}
	
	public void setPoints(double width, double top, double left, double angle){
		this.getElement().setAttribute("style", "width: "+ width + "px; top:" + top + "px; left:" + left  + "px; -webkit-transform: rotate(" + angle +"rad); -moz-transform: rotate(" + angle + "rad);");
	}

}
