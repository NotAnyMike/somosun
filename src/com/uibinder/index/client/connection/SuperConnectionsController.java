package com.uibinder.index.client.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.SubjectWidget;

public class SuperConnectionsController {

	protected PlanPresenter presenter;
	protected List<SubjectWidget> subjectWidgetsList;
	protected List<Connection> connectionList;
	protected List<LineWidget> lines;
	protected Multimap<SubjectWidget, Connection> connections;
	protected Multimap<SubjectWidget, Connection> connections2;
	
	public SuperConnectionsController(PlanPresenter presenter){
		connections = HashMultimap.create();
		connections2 = HashMultimap.create();
		lines =  new ArrayList<LineWidget>();
		subjectWidgetsList = new ArrayList<SubjectWidget>();
		connectionList = new ArrayList<Connection>();
		this.presenter = presenter;
	}

	public void update(){
		if(lines.size()!=0){
			for(LineWidget line : lines){
				//line.setPoints(x1, y1, x2, y2);;
			}			
		}
	}
	
	public void update(SubjectWidget s){
		//TODO 
	}
	
	public void hide(){
		if(lines.size()!=0){
			for(LineWidget line : lines){
				line.hide();
			}			
		}
	}
	
	/**
	 * TODO: take into account that I have to check both values of the multimap, because s could be the toSubject, then it will appear as a value and not as a key
	 * @param s
	 */
	public void hide(SubjectWidget s){
		if(lines.size()!=0){
			for(Connection c : connections.get(s)){
				c.hideLines();
			}
			for(Connection c : connections2.get(s)){
				c.hideLines();
			}
		}
	}
	
	public void show(){
		if(lines.size()!=0){
			for(LineWidget line : lines){
				line.show();
			}			
		}
	}
	
	/**
	 * TODO: take into account that I have to check both values of the multimap, because s could be the toSubject, then it will appear as a value and not as a key
	 * 
	 * @param s
	 */
	public void show(SubjectWidget s){
		if(lines.size()!=0){
			for(Connection c : connections.get(s)){
				c.showLines();
			}
			for(Connection c : connections2.get(s)){
				c.showLines();
			}
		}
	}
	
	public void delete(){
		if(lines.size()!=0){
			//TODO			
		}
	}
	
	/**
	 * TODO: take into account that I have to check both values of the multimap, because s could be the toSubject, then it will appear as a value and not as a key
	 * 
	 * @param s
	 */
	public void delete(SubjectWidget s){
		if(lines.size()!=0){
			for(Connection c : connections.get(s)){
				c.deleteLines();
				connections.remove(s, c);
			}
			for(Connection c : connections2.get(s)){
				c.deleteLines();
				connections2.remove(s, c);
			}
		}
	}
	
	protected void createLine(int x1, int y1, int x2, int y2, Connection c){
		LineWidget line = new LineWidget(x1,y1,x2,y2);
		lines.add(line);
		c.addLine(line);
		presenter.addLine(line);
	}
	
	protected Connection createConnectionObject(SubjectWidget from, SubjectWidget to){
		//TODO: see if it is using createConnections from the super class or the inherited class, it must use the inherited class's method
		if(subjectWidgetsList.contains(from) == false) subjectWidgetsList.add(from);
		if(subjectWidgetsList.contains(to) == false) subjectWidgetsList.add(to);
		boolean isNew = true;
		Connection connection = null;
		for(Connection c: connections.get(from)){
			if(c.contains(from, to)==true || c.contains(to, from)){
				isNew = false;
				break;
			}
		}
		if(isNew == true){
			connection = new Connection(from, to);
			connectionList.add(connection);
		}
		if(connection !=null){
			connections.put(from, connection);
			connections2.put(to, connection);
		}
		return connection;
	}
}
