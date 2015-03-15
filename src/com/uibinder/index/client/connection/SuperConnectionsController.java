package com.uibinder.index.client.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.SubjectWidget;

public class SuperConnectionsController {

	protected List<SubjectWidget> subjectWidgetsList;
	protected List<LineWidget> lines;
	protected Multimap<SubjectWidget, SubjectWidget> connections;
	protected Multimap<SubjectWidget, LineWidget> subjectAndLineMultimap;
	protected VerticalPanel container;
	protected static int HEIGHT = 0;
	
	public SuperConnectionsController(VerticalPanel container){
		this.container = container;
		connections = HashMultimap.create();
		lines =  new ArrayList<LineWidget>();
		subjectWidgetsList = new ArrayList<SubjectWidget>();
		subjectAndLineMultimap = HashMultimap.create();
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
			if(subjectAndLineMultimap.containsKey(s) == true){
				for(LineWidget l : subjectAndLineMultimap.get(s)){
					l.hide();
				}
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
			if(subjectAndLineMultimap.containsKey(s) == true){
				for(LineWidget l : subjectAndLineMultimap.get(s)){
					l.show();
				}
			}
		}
	}
	
	public void delete(){
		if(lines.size()!=0){
			for(LineWidget line : lines){
				lines.remove(line);
				line.removeFromParent();
			}			
		}
	}
	
	/**
	 * TODO: take into account that I have to check both values of the multimap, because s could be the toSubject, then it will appear as a value and not as a key
	 * 
	 * @param s
	 */
	public void delete(SubjectWidget s){
		if(lines.size()!=0){
			if(subjectAndLineMultimap.containsKey(s) == true){
				for(LineWidget l : subjectAndLineMultimap.get(s)){
					l.removeFromParent();
				}
			}
		}
	}
	
	protected void createLine(int x1, int y1, int x2, int y2){
		LineWidget line = new LineWidget(0,0,500,500);
		lines.add(line);
		container.add(line);
	}
	
	protected void addConnection(SubjectWidget from, SubjectWidget to){
		//TODO: see if it is using createConnections from the super class or the inherited class, it must use the inherited class's method
		subjectWidgetsList.add(from);
		subjectWidgetsList.add(to);
	}
}
