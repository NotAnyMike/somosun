package com.uibinder.index.client.connection;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.SubjectWidget;

public class ConnectionsController {
	
	private final int TOTAL_HEIGHT = 154;
	private final int TOTAL_WIDTH = 200; //never set to zero
	
	PlanPresenter p = null;
	/*private List<Connection> prerequisitesList = new ArrayList<Connection>();
	private List<Connection> corequisitesList = new ArrayList<Connection>();
	private List<Connection> prerequisitesOfList = new ArrayList<Connection>();
	private List<Connection> corequisitesOfList = new ArrayList<Connection>();*/
	private Multimap<SubjectWidget, Connection> fromList;
	private Multimap<SubjectWidget, Connection> toList;
	private List<Connection> connectionList;
	
	public ConnectionsController(PlanPresenter p){
		this.p = p;
		fromList = HashMultimap.create();
		toList = HashMultimap.create();
		connectionList = new ArrayList<Connection>();
	}
	
	/**
	 * To model a pos-requisite connection model a pre-requisito connection backwards 
	 * 
	 * @param from
	 * @param to
	 * @param type (pre, co) pre is the default option
	 */
	public void addConnection(SubjectWidget from, SubjectWidget to, String type){
		int topInit = 0;
		int leftInit = 0;
		int topFinal = 0;
		int leftFinal = 0;
		int x1 = from.getAbsoluteLeft();
		int y1 = from.getAbsoluteTop();
		int x2 = to.getAbsoluteLeft() - x1;
		int y2 = y1 - to.getAbsoluteTop();
		
		int connectionsFrom = 0;
		int connectionsTo = 0;
		
		Connection c = getConnection(from, to, type);
		
		if(y2 <= f(x2) && y2 >= g(x2)){
			
			//A: RightInit and LeftFinal
			connectionsFrom = 0;//getConnectionsFromRight(from);
			connectionsTo = getConnectionsFromLeft(to, from);
			topInit = y1+(from.getOffsetHeight())/2;// + 10*(((connectionsFrom & 1)==1)? -1 : 1)*(connectionsFrom + (((connectionsFrom & 1 )==1) ? 1 : 2)))/2;
			leftInit = x1 + from.getOffsetWidth();
			topFinal = to.getAbsoluteTop() + (to.getOffsetHeight() + 10*(((connectionsTo & 1)==1)? -1 : 1)*(connectionsTo + (((connectionsTo & 1 )==1) ? 1 : 2)))/2;
			leftFinal = to.getAbsoluteLeft();
			c.setOutRight();
			c.setInLeft();
			if(c.getInLeft() != 0 && c.getPosition() == null && connectionsTo != 0){
				connectionsTo = connectionsTo+1;
			}
			c.setPosition(connectionsTo);
			
		}else if(y2 < f(x2) && y2 < g(x2)){
		
			//B : DownInit and TopFinal
			connectionsFrom = 0;//getConnectionsFromDown(from);
			connectionsTo = getConnectionsFromUp(to, from);
			topInit = y1+from.getOffsetHeight();
			leftInit = x1 +(from.getOffsetWidth())/2;// + 10*(((connectionsFrom & 1)==1)? -1 : 1)*(connectionsFrom + (((connectionsFrom & 1 )==1) ? 1 : 0)))/2;
			topFinal = to.getAbsoluteTop();
			leftFinal = to.getAbsoluteLeft()+(to.getOffsetWidth() + 10*(((connectionsTo & 1)==1)? -1 : 1)*(connectionsTo + (((connectionsTo & 1 )==1) ? 1 : 2)))/2;
			c.setOutDown();
			c.setInUp();
			if(c.getInUp() != 0 && c.getPosition() == null && connectionsTo != 0){
				connectionsTo = connectionsTo+1;
			}
			c.setPosition(connectionsTo);
			
		}else if(y2 >= f(x2) && y2 <= g(x2)){
			
			//D : LeftInit and RightFinal
			connectionsFrom = 0;//getConnectionsFromLeft(from);
			connectionsTo = getConnectionsFromRight(to, from);
			topInit = y1+(from.getOffsetHeight())/2;// + 10*(((connectionsFrom & 1)==1)? -1 : 1)*(connectionsFrom + (((connectionsFrom & 1 )==1) ? 1 : 0)))/2;
			leftInit = x1;
			topFinal = to.getAbsoluteTop() + (to.getOffsetHeight() + 10*(((connectionsTo & 1)==1)? -1 : 1)*(connectionsTo + (((connectionsTo & 1 )==1) ? 1 : 2)))/2;
			leftFinal = to.getAbsoluteLeft() + to.getOffsetWidth();
			c.setOutLeft();
			c.setInRight();
			if(c.getInRight() != 0 && c.getPosition() == null && connectionsTo != 0){
				connectionsTo = connectionsTo+1;
			}
			c.setPosition(connectionsTo);
			
		}else{
			
			//C : TopInit and DownFinal
			connectionsFrom = 0;//getConnectionsFromUp(from);
			connectionsTo = getConnectionsFromDown(to, from);
			topInit = y1;
			leftInit = x1 +(from.getOffsetWidth())/2;// + 10*(((connectionsFrom & 1)==1)? -1 : 1)*(connectionsFrom + (((connectionsFrom & 1 )==1) ? 1 : 0)))/2;
			topFinal = to.getAbsoluteTop() + to.getOffsetHeight();
			leftFinal = to.getAbsoluteLeft()+(to.getOffsetWidth() + 10*(((connectionsTo & 1)==1)? -1 : 1)*(connectionsTo + (((connectionsTo & 1 )==1) ? 1 : 0)))/2;
			c.setOutUp();
			c.setInDown();
			if(c.getInDown() != 0 && c.getPosition() == null && connectionsTo != 0){
				connectionsTo = connectionsTo+1;
			}
			c.setPosition(connectionsTo);
			
		}
		
		
		if(c.getLine() == null){
			
			LineWidget line = new LineWidget(leftInit, topInit, leftFinal, topFinal);
			c.setLine(line);
			p.addLine(line);
			Icon i = new Icon();
			i.setType(IconType.CHEVRON_RIGHT);
			i.getElement().setAttribute("style", "position:absolute; z-index:200");
			c.setArrow(i);
			p.addIcon(i);
			
		}
		constructPositions(leftInit, topInit, leftFinal, topFinal, c);			
		
	}
	
	private void constructPositions(int x1, int y1, int x2, int y2, Connection c) {
		
		double top = 0;
		double left = 0;
		double width = 0;
		double angle = 0;
		
		width = Math.pow(Math.pow(x1-x2,2)+Math.pow(y1-y2,2),0.5);
		angle = Math.atan2(y2-y1, x2-x1);
		top = (y2-width/2*(Math.sin(angle)));
		left = (x1 - (width/2)*(1-Math.cos(angle)));
		
		c.setPoints(width, top, left, angle, x2, y2);
		
	}

	private Connection getConnection(SubjectWidget to, SubjectWidget from, String type) {
		Connection c = null; 
		for(Connection cTemporary : fromList.get(to)){
			if(cTemporary.contains(to, from) == true) c = cTemporary;
		}
		for(Connection cTemporary : fromList.get(from)){
			if(cTemporary.contains(to, from) == true) c = cTemporary;
		}
		if(c==null) {
			c = new Connection(to,from,type);
			connectionList.add(c);
			fromList.put(to, c);
			toList.put(from, c);
		}
		return c;
	}

	private int getConnectionsFromLeft(SubjectWidget to, SubjectWidget from) {
		Integer times = 0;
		Integer timesOriginal = null;	
		for(Connection c : toList.get(to)){
			if(c.contains(to, from) && c.getInLeft() != 0){
				timesOriginal = c.getPosition();
				break;
			}
			times = times + c.getInLeft(); 
		}
		return (timesOriginal == null ? times : timesOriginal);
	}
	
	private int getConnectionsFromRight(SubjectWidget to, SubjectWidget from) {
		Integer times = 0;
		Integer timesOriginal = null;	
		for(Connection c : toList.get(to)){
			if(c.contains(to, from) && c.getInRight() != 0){
				timesOriginal = c.getPosition();
				break;
			}
			times = times + c.getInRight(); 
		}
		return (timesOriginal == null ? times : timesOriginal);
	}
	
	private int getConnectionsFromUp(SubjectWidget to, SubjectWidget from) {
		Integer times = 0;
		Integer timesOriginal = null;	
		for(Connection c : toList.get(to)){
			if(c.contains(to, from) && c.getInUp() != 0){
				timesOriginal = c.getPosition();
				break;
			}
			times = times + c.getInUp(); 
		}
		return (timesOriginal == null ? times : timesOriginal);
	}
	
	private int getConnectionsFromDown(SubjectWidget to, SubjectWidget from) {
		Integer times = 0;
		Integer timesOriginal = null;	
		for(Connection c : toList.get(to)){
			if(c.contains(to, from) && c.getInDown() != 0){
				timesOriginal = c.getPosition();
				break;
			}
			times = times + c.getInDown(); 
		}
		return (timesOriginal == null ? times : timesOriginal);
	}

	private double f(int x){
		double m = (double) (TOTAL_HEIGHT)/(TOTAL_WIDTH);
		return m*x;
	}
	
	private double g(int x){
		double m = (double) (TOTAL_HEIGHT)/(TOTAL_WIDTH);
		return -m*x;
	}

	public void hideArrows() {
		for(Connection c : connectionList){
			c.hideLines();
		}
	}

}
