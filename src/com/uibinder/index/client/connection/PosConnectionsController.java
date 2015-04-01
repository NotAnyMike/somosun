package com.uibinder.index.client.connection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class PosConnectionsController extends SuperConnectionsController{
	
	//To save the pos-connections
	

	public PosConnectionsController(PlanPresenter p) {
		super(p);
	}
	
	public void addConnection(SubjectWidget from, SubjectWidget to){
		Connection c = super.createConnectionObject(from, to);	
		if(c != null){
			createConnections(c);						
		}
	}
	
	public void createConnections(Connection c){
		double h1 = (((connections.get(c.getFrom()).size()-1) & 1) == 1 ? -1 : 1)*(connections.get(c.getFrom()).size()-1+((((connections.get(c.getFrom()).size()-1) & 1)==1) ? 1 : 0));
		double h2 = (((connections2.get(c.getTo()).size()-1) & 1) == 1 ? -1 : 1)*(connections2.get(c.getTo()).size()-1+((((connections2.get(c.getTo()).size()-1) & 1)==1) ? 1 : 0));
		
		Double y1 = c.getFrom().getAbsoluteTop() + (c.getFrom().getOffsetHeight() + h1*10)/2; 
		Double y2 = c.getTo().getAbsoluteTop() + (c.getTo().getOffsetHeight() + h2*10)/2;
		//If true means that they are at the same level
		if(c.getFrom().getAbsoluteTop() == c.getTo().getAbsoluteTop()){
			//if true means that they are in the same or more advanced semester
			if(c.getFrom().getAbsoluteLeft()<=c.getTo().getAbsoluteLeft()){
				//create one line
				createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth(), y1.intValue(), c.getTo().getAbsoluteLeft(), y2.intValue(),c);
			}else{
				//create four lines
				createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth(),y1.intValue(),c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue(), c);
				createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue(),c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue()+c.getFrom().getElement().getClientHeight()/2+10, c);
				createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue()+c.getFrom().getElement().getClientHeight()/2+10,c.getTo().getAbsoluteLeft()-10, y2.intValue(), c);
				createLine(c.getTo().getAbsoluteLeft()-10, y2.intValue(),c.getTo().getAbsoluteLeft(), y2.intValue(), c);
			}
		}else{
			//create three lines
			createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth(),y1.intValue(),c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue(), c);
			createLine(c.getFrom().getElement().getAbsoluteLeft() + c.getFrom().getOffsetWidth()+10,y1.intValue(),c.getTo().getAbsoluteLeft()-10, y2.intValue(), c);
			createLine(c.getTo().getAbsoluteLeft()-10, y2.intValue(),c.getTo().getAbsoluteLeft(), y2.intValue(), c);
		}
	}

}
