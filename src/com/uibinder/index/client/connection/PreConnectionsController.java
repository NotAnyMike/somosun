package com.uibinder.index.client.connection;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.widget.SubjectWidget;

public class PreConnectionsController extends SuperConnectionsController{

	public PreConnectionsController(VerticalPanel container) {
		super(container);
	}
	
	public void createConnections(SubjectWidget from, SubjectWidget to){
		double H1 = connections.get(from).size() + ( (connections.get(from).size() & 1) == 0 ? 0 : 1);
		double H2 = connections.get(to).size() + ( (connections.get(to).size() & 1) == 0 ? 0 : 1);
		
		//If true means that they are at the same level
		if(from.getAbsoluteTop() == to.getAbsoluteTop()){
			//if true means that they are in the same or more advanced semester
			if(from.getAbsoluteLeft()<=to.getAbsoluteLeft()){
				//create one line
				Double x1 =  (from.getAbsoluteTop() + (HEIGHT + H1*5)/2); 
				createLine(x1.intValue(), from.getAbsoluteTop() , 700,500);
			}else{
				//create four lines
			}
		}else{
			//create three lines
		}
	}
	
	@Override
	public void addConnection(SubjectWidget from, SubjectWidget to){
		super.addConnection(from, to);
		createConnections(from, to);			
		if(connections.containsEntry(from, to) == false && connections.containsEntry(to, from)==false){
		}
	}

}
