package com.somosun.plan.client.index.connection;

import org.gwtbootstrap3.client.ui.Icon;

import com.somosun.plan.client.index.widget.LineWidget;
import com.somosun.plan.client.index.widget.SubjectWidget;
import com.somosun.plan.shared.SomosUNUtils;

public class Connection {

	private SubjectWidget from;
	private SubjectWidget to;
	private String type;
	private LineWidget line = null;
	private Icon arrow = null;
	private Integer position = null;
	
	private int outUp = 0;
	private int outDown = 0;
	private int outLeft = 0;
	private int outRight = 0;
	private int inUp = 0;
	private int inDown = 0;
	private int inLeft = 0;
	private int inRight = 0;
	
	private final String CO = SomosUNUtils.COLOR_CO;
	private final String PRE = SomosUNUtils.COLOR_PRE;
	private final int Z_INDEX_LINE = 3;
	private final int Z_INDEX_ARROW = Z_INDEX_LINE+1;
	
	public Connection(SubjectWidget from, SubjectWidget to, String type){
		this.from = from;
		this.to = to;
		this.type = type;
	}
	
	public void addLine(LineWidget l){
		this.line = l;
	}
	
	public void setLinePositions(int x1, int y1, int x2, int y2){
		line.setPoints(x1, y1, x2, y2);
	}
	
	public boolean contains(SubjectWidget from, SubjectWidget to){
		if((this.from.equals(from) && this.to.equals(to)) || this.from.equals(to) && this.to.equals(from)){
			return true;
		}else{
			return false;
		}
	}

	public SubjectWidget getFrom() {
		return from;
	}

	public void setFrom(SubjectWidget from) {
		this.from = from;
	}

	public SubjectWidget getTo() {
		return to;
	}

	public void setTo(SubjectWidget to) {
		this.to = to;
	}

	public LineWidget getLine() {
		return line;
	}

	public void hideLines() {
		line.hide();
		arrow.setVisible(false);
	}
	
	public void showLines(){
		line.show();
	}

	public void deleteLines() {
		line.removeFromParent();
	}

	public int getOutUp() {
		return outUp;
	}

	public void setOutUp() {
		this.outUp = 1;
		this.outDown = 0;
	}

	public int getOutDown() {
		return outDown;
	}

	public void setOutDown() {
		this.outDown = 1;
		this.outUp = 0;
	}

	public int getOutLeft() {
		return outLeft;
	}

	public void setOutLeft() {
		this.outLeft = 1;
		this.outRight = 0;
	}

	public int getOutRight() {
		return outRight;
	}

	public void setOutRight() {
		this.outRight = 1;
		this.outLeft = 0;
	}

	public int getInUp() {
		return inUp;
	}

	public void setInUp() {
		this.inUp = 1;
		this.inDown = 0;
	}

	public int getInDown() {
		return inDown;
	}

	public void setInDown() {
		this.inDown = 1;
		this.inUp = 0;
	}

	public int getInLeft() {
		return inLeft;
	}

	public void setInLeft() {
		this.inLeft = 1;
		this.inRight = 0;
	}

	public int getInRight() {
		return inRight;
	}

	public void setInRight() {
		this.inRight = 1;
		this.inLeft = 0;
	}

	public void setLine(LineWidget line) {
		this.line = line;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Icon getArrow() {
		return arrow;
	}

	public void setArrow(Icon arrow) {
		this.arrow = arrow;
	}

	public void setPoints(double width, double top, double left, double angle, int x2, int y2) {
		this.line.getElement().setAttribute("style", "z-index:" + Z_INDEX_LINE + "; background-color: #" + (type == "pre" ? PRE : CO) + ";width: "+ width + "px; top:" + top + "px; left:" + left  + "px; -webkit-transform: rotate(" + angle +"rad); -moz-transform: rotate(" + angle + "rad);");
		this.arrow.getElement().setAttribute("style", "color: #" + (type == "pre"? PRE : CO) + "; z-index: " + Z_INDEX_ARROW + ";position:absolute; top:" + (y2-13/2/*-arrow.getElement().getOffsetHeight()/2*/) + "px; left:" + (x2-9/2/*-arrow.getElement().getOffsetWidth()/2*/) + "px; -webkit-transform: rotate(" + angle +"rad); -moz-transform: rotate(" + angle + "rad);");
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
}
