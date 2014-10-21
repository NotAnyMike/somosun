package com.uibinder.index.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.uibinder.index.client.event.AboutUsEvent;
import com.uibinder.index.client.event.AboutUsEventHandler;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEventHandler;
import com.uibinder.index.client.presenter.AboutGedadPresenter;
import com.uibinder.index.client.presenter.CreatePresenter;
import com.uibinder.index.client.presenter.DndPresenter;
import com.uibinder.index.client.presenter.IndexPresenter;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.presenter.Presenter;
import com.uibinder.index.client.presenter.TopBarPresenter;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.AboutGedadViewImpl;
import com.uibinder.index.client.view.DndViewImpl;
import com.uibinder.index.client.view.IndexViewImpl;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.view.TopBarViewImpl;
import com.uibinder.index.client.view.CreateViewImpl;

public class AppController implements Presenter, ValueChangeHandler<String> {

	/*
	* Creating all the views that will exist once for all to save them to let users go back to their view
	*/
	Presenter indexPresenter = null;
	Presenter topBarPresenter = null;
	Presenter dndPresenter = null;
	Presenter createPresenter = null;
	Presenter planPresenter = null;
	
	private final HandlerManager eventBus;
	private final SUNServiceAsync rpcService;
	
	private HasWidgets container;
	private IndexViewImpl indexView;
	private TopBarViewImpl topBarView;
	private DndViewImpl dndView;
	private CreateViewImpl createView;
	private PlanViewImpl planView;
	
	public AppController(SUNServiceAsync rpcService, HandlerManager eventBus){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		bind();
	}
	
	private void bind(){
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(AboutUsEvent.TYPE, 
				new AboutUsEventHandler(){
					@Override
					public void onAboutUs(AboutUsEvent event){
						doOnAboutUs();
					}
				}
				);
		eventBus.addHandler(GenerateAcademicHistoryFromStringEvent.TYPE, 
				new GenerateAcademicHistoryFromStringEventHandler(){
					@Override
					public void DoGenerateAcademicHistoryFromString(String academicHisotry){
						generateAcademicHistoryFromString(academicHisotry);
					}
		});
	}
	
	public void generateAcademicHistoryFromString(String academicHistory){
		Window.alert("This should create an academic history from the string: " + academicHistory);
		//TODO call the method cesar is working on to get the academic history
	}
	
	public void doOnAboutUs(){
		Window.alert("hola");
	}
	
	@Override
	public void go(HasWidgets container){
		this.container = container;
		
		if(History.getToken().equals("")){
			History.newItem("index");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		
		String token = event.getValue();
		
		if(token != null){
			
			if(topBarView == null){
				topBarView = new TopBarViewImpl();
				topBarPresenter = new TopBarPresenter(rpcService, eventBus, topBarView);
				topBarPresenter.go(RootPanel.get("topArea"));
			}
			
			/**
			 * This part will take care of the stability of the plan widget when its height changes due
			 * to the changes on the number of max subjects on one semester
			 */
			if(token.equals("plan")){
				if(RootPanel.get("centerArea").getElement().getAttribute("valign") != "top")
				RootPanel.get("centerArea").getElement().setAttribute("valign","top");
			}else if(RootPanel.get("centerArea").getElement().getAttribute("valign")!="middle"){
				RootPanel.get("centerArea").getElement().setAttribute("valign","middle");
			}
			
			if(token.equals("dnd")){ //dnd stands for Drag and Drop
				if(dndView == null){
					dndView = new DndViewImpl();
				}
				if(dndPresenter == null){
					dndPresenter = new DndPresenter(rpcService, eventBus, dndView);
				}
				dndPresenter.go(RootPanel.get("centerArea"));					
			} else if(token.equals("create")){
				if(createView == null){
					createView = new CreateViewImpl();
				}
				if(createPresenter == null){
					createPresenter = new CreatePresenter(rpcService, eventBus, createView);					
				}
				createPresenter.go(RootPanel.get("centerArea"));
			} else if(token.equals("plan")) {
				if(planView == null){
					planView = new PlanViewImpl();
				}
				if(planPresenter == null){
					planPresenter = new PlanPresenter(rpcService, eventBus, planView);
				}
				planPresenter.go(RootPanel.get("centerArea"));
			} else if(token.equals("gedad")) {
				AboutGedadViewImpl aboutGedadView = new AboutGedadViewImpl();
				Presenter aboutGedadPresenter = new AboutGedadPresenter(rpcService, eventBus, aboutGedadView);
				aboutGedadPresenter.go(RootPanel.get("centerArea"));
			} else {
				if(indexView == null){
					indexView = new IndexViewImpl();
				}
				if(indexPresenter == null){
					indexPresenter = new IndexPresenter(rpcService, eventBus, indexView);					
				}
				indexPresenter.go(RootPanel.get("centerArea"));
			}
			setLabelsOnTopBar(token);
		}
		
	}

	private void setLabelsOnTopBar(String token) {
		if(token.equals("dnd")){
			topBarView.setMainLabel("You shouldn't be here!");
		} else if (token.equals("create")){
			topBarView.setMainLabel("Plan de Estudios");
		} else if(token.equals("plan")){
			topBarView.setMainLabel("Plan de Estudios");
		} else if(token.equals("gedad")){
			topBarView.setMainLabel("Estudia para la vida, no para los exámenes!");
		} else {
			topBarView.setMainLabel("");
		}
		topBarView.setUserName("Invitado");
	}
	
}
