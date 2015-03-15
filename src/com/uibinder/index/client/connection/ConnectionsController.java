package com.uibinder.index.client.connection;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.widget.SubjectWidget;

public class ConnectionsController {
	
	private PreConnectionsController preConnectionsController;
	private CoConnectionsController coConnectionsController;
	private VerticalPanel container;
	
	public ConnectionsController(){
		preConnectionsController = new PreConnectionsController(container);
		coConnectionsController = new CoConnectionsController(container);
	}
	
	public ConnectionsController(VerticalPanel container){
		this.container = container;
		preConnectionsController = new PreConnectionsController(container);
		coConnectionsController = new CoConnectionsController(container);
	}
	
	public void setContainer(VerticalPanel subContainer){
		this.container = subContainer;
	}
	
	public void update(SubjectWidget s){
		preConnectionsController.update(s);
		coConnectionsController.update(s);
	}
	
	public void update(){
		preConnectionsController.update();
		coConnectionsController.update();
	}
	
	public void delete(){
		preConnectionsController.delete();
		coConnectionsController.delete();
	}
	
	/**
	 * 
	 * 
	 * @param from
	 * @param to
	 * @param type (pre, co) pre is the default option
	 */
	public void addConnection(SubjectWidget from, SubjectWidget to, String type){
		switch(type){
		case "co":
				coConnectionsController.addConnection(from, to);
				break;
		case "pre":
		default:
				preConnectionsController.addConnection(from, to);
				break;
		}
	}

}
