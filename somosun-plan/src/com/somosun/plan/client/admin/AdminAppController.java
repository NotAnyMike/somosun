package com.somosun.plan.client.admin;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.somosun.plan.client.admin.presenter.IndexPresenter;
import com.somosun.plan.client.admin.presenter.MessagesPresenter;
import com.somosun.plan.client.admin.presenter.PlanAdminPresenter;
import com.somosun.plan.client.admin.service.AdminServiceAsync;
import com.somosun.plan.client.admin.view.IndexViewImpl;
import com.somosun.plan.client.admin.view.MessagesViewImpl;
import com.somosun.plan.client.admin.view.PlanAdminViewImpl;
import com.somosun.plan.client.index.AppController;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.PlanViewImpl;
import com.somosun.plan.client.index.view.SiaSummaryViewImpl;
import com.somosun.plan.shared.control.Plan;

public class AdminAppController implements ValueChangeHandler<String> {

	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	private AdminServiceAsync rpcAdminService;
	
	private IndexPresenter indexPresenter;
	private MessagesPresenter messagesPresenter;
	private PlanAdminPresenter planAdminPresenter;
	private PlanPresenter planPresenter;
	
	private SiaSummaryViewImpl siaSummaryView;
	private IndexViewImpl indexView;
	private MessagesViewImpl messagesView;
	private PlanAdminViewImpl planAdminView;
	private PlanViewImpl planView;
	
	private String token;
	
	public AdminAppController(SUNServiceAsync rpcService, AdminServiceAsync rpcAdminService, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.rpcAdminService = rpcAdminService;
		bind();
	}
	
	private void bind() {
		History.addValueChangeHandler(this);
	}

	public void go(HasWidgets container){
		token = History.getToken();
		
		if(token.matches("(index)|(^$)") == true){
			History.fireCurrentHistoryState();
		}else{
			History.newItem("index");
		}
	}


	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		token = History.getToken();
		
		if(token.matches("(index)|(^$)") == true){
			if(indexView == null){
				indexView = new IndexViewImpl();
			}
			if(indexPresenter == null){
				indexPresenter = new IndexPresenter(rpcService, rpcAdminService, indexView);
			}
			indexPresenter.go(RootPanel.get("container"));
		}else if(token.contains("messages")){
			if(messagesView == null){
				messagesView = new MessagesViewImpl();
			}
			if(messagesPresenter == null){
				messagesPresenter = new MessagesPresenter(eventBus, rpcService, rpcAdminService, messagesView);
			}
			messagesPresenter.go(RootPanel.get("container"));
			if(token.contains("action=showAll")) messagesPresenter.showAllMessage();
			else if(token.contains("action=showError")) messagesPresenter.showAllErrors();
			else if(token.contains("action=showSuggestion")) messagesPresenter.showAllSuggestions();
			else if(token.contains("action=showOther")) messagesPresenter.showAllOthers();
			else if(token.contains("action=showId")) messagesPresenter.showId(token.substring(token.indexOf("id=")+3));
			else if(token.contains("action=showUsername")) messagesPresenter.showUsernameMessages(token.substring(token.indexOf("username=")+9));
		}else if(token.contains("planAdmin")){
			if(planAdminView == null){
				planAdminView = new PlanAdminViewImpl();
			}
			if(planAdminPresenter == null){
				planAdminPresenter = new PlanAdminPresenter(eventBus, rpcService, rpcAdminService, planAdminView);
			}
			planAdminPresenter.go(RootPanel.get("container"));
			if(token.contains("action=showUsername")) planAdminPresenter.showPlansByUser(token.substring(token.indexOf("username=")+9));
		}else if(token.contains("plan?id=")){
			rpcAdminService.getPlanById(Long.valueOf(token.substring(token.indexOf("id=") +3)), new AsyncCallback<Plan>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error loading the page");
				}

				@Override
				public void onSuccess(Plan result) {
					setPlanPresenter(result);
				}
				
			});
		}else{
			History.newItem("index");
		}
	}
	
	private void setPlanPresenter(Plan plan){
		
		planView = new PlanViewImpl();
		siaSummaryView = new SiaSummaryViewImpl();
			
		planPresenter = new PlanPresenter(rpcService, eventBus, planView, siaSummaryView, null);
		
		siaSummaryView.hideWarning();
		
		planPresenter.setPlan(plan);
		planPresenter.go(RootPanel.get("container"));
		
	}

}
