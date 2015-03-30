package com.uibinder.index.client.connection;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.widget.SubjectWidget;

public class ConnectionsController {
	
	private PosConnectionsController posConnectionsController;
	private CoConnectionsController coConnectionsController;
	private VerticalPanel container;
	
	public ConnectionsController(PlanPresenter p){
		posConnectionsController = new PosConnectionsController(p);
		coConnectionsController = new CoConnectionsController(p);
	}
	
	public void setContainer(VerticalPanel subContainer){
		this.container = subContainer;
	}
	
	public void update(SubjectWidget s){
		posConnectionsController.update(s);
		coConnectionsController.update(s);
	}
	
	public void update(){
		posConnectionsController.update();
		coConnectionsController.update();
	}
	
	public void delete(){
		posConnectionsController.delete();
		coConnectionsController.delete();
	}
	
	/**
	 * To model a pre-requisite connection model a pos-requisito connection backwards 
	 * 
	 * @param from
	 * @param to
	 * @param type (pos, co) pre is the default option
	 */
	public void addConnection(SubjectWidget from, SubjectWidget to, String type){
		switch(type){
		case "co":
				coConnectionsController.addConnection(from, to);
				break;
		case "pos":
		default:
				posConnectionsController.addConnection(from, to);
				break;
		}
	}

}
