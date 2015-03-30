package com.uibinder.index.client.connection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class PosConnectionsController extends SuperConnectionsController{
	
	//To save the pos-connections
	private Multimap<SubjectWidget, SubjectWidget> connectionsPos;

	public PosConnectionsController(PlanPresenter p) {
		super(p);
		connectionsPos = HashMultimap.create();
	}
	
	public void createConnections(SubjectWidget from, SubjectWidget to){
		double h1 = (((connections.get(from).size()-1) & 1) == 1 ? -1 : 1)*(connections.get(from).size()-1+((((connections.get(from).size()-1) & 1)==1) ? 1 : 0));
		double h2 = (((connectionsPos.get(to).size()-1) & 1) == 1 ? -1 : 1)*(connectionsPos.get(to).size()-1+((((connectionsPos.get(to).size()-1) & 1)==1) ? 1 : 0));
		
		Double y1 = from.getAbsoluteTop() + (from.getOffsetHeight() + h1*10)/2; 
		Double y2 = to.getAbsoluteTop() + (to.getOffsetHeight() + h2*10)/2;
		//If true means that they are at the same level
		if(from.getAbsoluteTop() == to.getAbsoluteTop()){
			//if true means that they are in the same or more advanced semester
			if(from.getAbsoluteLeft()<=to.getAbsoluteLeft()){
				//create one line
				createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth(), y1.intValue(), to.getAbsoluteLeft(), y2.intValue(),from, to);
			}else{
				//create four lines
				createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth(),y1.intValue(),from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue(), from, to);
				createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue(),from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue()+from.getElement().getClientHeight()/2+10, from, to);
				createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue()+from.getElement().getClientHeight()/2+10,to.getAbsoluteLeft()-10, y2.intValue(), from, to);
				createLine(to.getAbsoluteLeft()-10, y2.intValue(),to.getAbsoluteLeft(), y2.intValue(), from, to);
			}
		}else{
			//create three lines
			createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth(),y1.intValue(),from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue(), from, to);
			createLine(from.getElement().getAbsoluteLeft() + from.getOffsetWidth()+10,y1.intValue(),to.getAbsoluteLeft()-10, y2.intValue(), from, to);
			createLine(to.getAbsoluteLeft()-10, y2.intValue(),to.getAbsoluteLeft(), y2.intValue(), from, to);
		}
	}
	
	@Override
	public void addConnection(SubjectWidget from, SubjectWidget to){
		if(connections.containsEntry(from, to) == false && connections.containsEntry(to, from)==false){ //TODO check if the two conditions are the same
			super.addConnection(from, to);
			connectionsPos.put(to, from);
			createConnections(from, to);			
		}
	}

}
