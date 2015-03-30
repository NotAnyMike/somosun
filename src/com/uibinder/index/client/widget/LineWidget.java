package com.uibinder.index.client.widget;

import com.google.gwt.user.client.ui.SimplePanel;

public class LineWidget extends SimplePanel{
	
	private int x1=0;
	private int x2=0;
	private int y1=0;
	private int y2=0;
	private double top = 0;
	private double left = 0;
	private double width = 0;
	private double angle = 0;
	
	public LineWidget(int x1, int y1, int x2, int y2){
		this.addStyleName("dline");
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		update();
	}
	
	public void hide(){
		this.setVisible(false);
	}
	
	public void show(){
		this.setVisible(true);
	}
	
	public void setPoints(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		update();
	}
	
	public void setInitialPoint(int x1, int y1){
		this.x1 = x1;
		this.y1 = y1;
		update();
	}
	
	public void setFinalPoint(int x2, int y2){
		this.x2 = x2;
		this.y2 = y2;
		update();
	}
	
	private void update() {
		width = Math.pow(Math.pow(x1-x2,2)+Math.pow(y1-y2,2),0.5);
		angle = Math.atan2(y2-y1, x2-x1);
		top = (y2-width/2*(Math.sin(angle)));
		left = (x1 - (width/2)*(1-Math.cos(angle)));
		this.getElement().setAttribute("style", "width: "+ width + "px; top:" + top + "px;left:" + left  + "px; -webkit-transform: rotate(" + angle +"rad); -moz-transform: rotate(" + angle + "rad);");
	}

}
