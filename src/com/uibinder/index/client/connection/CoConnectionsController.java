package com.uibinder.index.client.connection;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class CoConnectionsController extends SuperConnectionsController{

	public CoConnectionsController(PlanPresenter p) {
		super(p);
	}
	
	public void addConnection(SubjectWidget from, SubjectWidget to){
		Connection c = super.createConnectionObject(from, to);	
		if(c != null){
			createConnections(c);						
		}
	}

	private void createConnections(Connection c) {
		
		double h1 = (((connections.get(c.getFrom()).size()-1 & 1) == 1) ? -1 : 1)*(connections.get(c.getFrom()).size()-1+(((connections.get(c.getFrom()).size()-1)==1) ? 1 : 0));
		double h2 = (((connections.get(c.getTo()).size()-1 & 1) == 1) ? -1 : 1)*(connections.get(c.getTo()).size()-1+(((connections.get(c.getTo()).size()-1)==1) ? 1 : 0));
		
		Double x1 = c.getFrom().getAbsoluteLeft() + (c.getFrom().getOffsetWidth() + 10*h1)/2;
		Double x2 = c.getTo().getAbsoluteLeft() + (c.getTo().getOffsetWidth() + 10*h2)/2;
		
		if(c.getFrom().getAbsoluteLeft() == c.getTo().getAbsoluteLeft()){
			//One line
			if(c.getFrom().getAbsoluteTop() < c.getTo().getAbsoluteTop()){
				//goes down
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop() + c.getFrom().getOffsetHeight(), x2.intValue(), c.getTo().getAbsoluteTop(), c);
			}else{
				//goes up
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop(), x2.intValue(), c.getTo().getAbsoluteTop()+c.getTo().getOffsetHeight(), c);
			}
		}else{
			//three lines
			if(c.getFrom().getAbsoluteTop() < c.getTo().getAbsoluteTop()){
				//goes down
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop()+c.getFrom().getOffsetHeight(), x1.intValue(), c.getFrom().getAbsoluteTop()+c.getFrom().getOffsetHeight()+10,c);
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop()+c.getFrom().getOffsetHeight()+10, x2.intValue(), c.getTo().getAbsoluteTop()-10,c);
				createLine(x2.intValue(), c.getTo().getAbsoluteTop()-10, x2.intValue(), c.getTo().getAbsoluteTop(),c);
			} else {
				//goes up
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop(), x1.intValue(), c.getFrom().getAbsoluteTop()-10,c);
				createLine(x1.intValue(), c.getFrom().getAbsoluteTop()-10, x2.intValue(), c.getTo().getAbsoluteTop()+c.getTo().getOffsetHeight()+10,c);
				createLine(x2.intValue(), c.getTo().getAbsoluteTop()+c.getTo().getOffsetHeight()+10, x2.intValue(), c.getTo().getAbsoluteTop()+c.getTo().getOffsetHeight(),c);
			}
		}
	}

}
