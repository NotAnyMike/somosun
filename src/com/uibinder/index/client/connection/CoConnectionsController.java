package com.uibinder.index.client.connection;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class CoConnectionsController extends SuperConnectionsController{

	public CoConnectionsController(PlanPresenter p) {
		super(p);
	}
	
	@Override
	public void addConnection(SubjectWidget from, SubjectWidget to){
		if(connections.containsEntry(from, to) == false && connections.containsEntry(to, from)==false){ //TODO check if the two conditions are the same
			super.addConnection(from, to);
			createConnections(from, to);			
		}
	}

	private void createConnections(SubjectWidget from, SubjectWidget to) {
		
		double h1 = (((connections.get(from).size()-1 & 1) == 1) ? -1 : 1)*(connections.get(from).size()-1+(((connections.get(from).size()-1)==1) ? 1 : 0));
		double h2 = (((connections.get(to).size()-1 & 1) == 1) ? -1 : 1)*(connections.get(to).size()-1+(((connections.get(to).size()-1)==1) ? 1 : 0));
		
		Double x1 = from.getAbsoluteLeft() + (from.getOffsetWidth() + 10*h1)/2;
		Double x2 = to.getAbsoluteLeft() + (to.getOffsetWidth() + 10*h2)/2;
		
		if(from.getAbsoluteLeft() == to.getAbsoluteLeft()){
			//One line
			if(from.getAbsoluteTop() < to.getAbsoluteTop()){
				//goes down
				createLine(x1.intValue(), from.getAbsoluteTop() + from.getOffsetHeight(), x2.intValue(), to.getAbsoluteTop(), from, to);
			}else{
				//goes up
				createLine(x1.intValue(), from.getAbsoluteTop(), x2.intValue(), to.getAbsoluteTop()+to.getOffsetHeight(), from, to);
			}
		}else{
			//three lines
			if(from.getAbsoluteTop() < to.getAbsoluteTop()){
				//goes down
				createLine(x1.intValue(), from.getAbsoluteTop()+from.getOffsetHeight(), x1.intValue(), from.getAbsoluteTop()+from.getOffsetHeight()+10,from, to);
				createLine(x1.intValue(), from.getAbsoluteTop()+from.getOffsetHeight()+10, x2.intValue(), to.getAbsoluteTop()-10,from, to);
				createLine(x2.intValue(), to.getAbsoluteTop()-10, x2.intValue(), to.getAbsoluteTop(),from, to);
			} else {
				//goes up
				createLine(x1.intValue(), from.getAbsoluteTop(), x1.intValue(), from.getAbsoluteTop()-10,from, to);
				createLine(x1.intValue(), from.getAbsoluteTop()-10, x2.intValue(), to.getAbsoluteTop()+to.getOffsetHeight()+10,from, to);
				createLine(x2.intValue(), to.getAbsoluteTop()+to.getOffsetHeight()+10, x2.intValue(), to.getAbsoluteTop()+to.getOffsetHeight(),from, to);
			}
		}
	}

}
