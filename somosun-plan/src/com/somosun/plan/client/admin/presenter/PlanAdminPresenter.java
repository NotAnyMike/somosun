package com.somosun.plan.client.admin.presenter;

import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.admin.service.AdminServiceAsync;
import com.somosun.plan.client.admin.view.PlanAdminView;
import com.somosun.plan.client.admin.view.PlanAdminViewImpl;
import com.somosun.plan.client.index.presenter.Presenter;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SubjectValue;

public class PlanAdminPresenter implements Presenter, PlanAdminView.Presenter {

	private HandlerManager eventBus;
	private SUNServiceAsync rpcService;
	private AdminServiceAsync rpcAdminService;
	private PlanAdminViewImpl view;
	
	public PlanAdminPresenter(HandlerManager eventBus, SUNServiceAsync rpcService, AdminServiceAsync rpcAdminService, PlanAdminViewImpl view) {
		this.eventBus = eventBus;
		this.rpcService = rpcService;
		this.rpcAdminService = rpcAdminService;
		this.view = view;
		view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void showPlansByUser(String username) {
		view.clearPlanContainer();
		rpcAdminService.getPlansByUser(username, new AsyncCallback<List<Plan>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error retrieving the plans for the user");
			}

			@Override
			public void onSuccess(List<Plan> result) {
				for(Plan p : result){					
					String s = getStringFromPlan(p);
					view.addPlanString(s);
				}
			}
			
		});
	}
	
	private String getStringFromPlan(Plan p){
		
		int total = 0;
		int withGrade = 0;
		
		for(Semester s : p.getSemesters()){
			for(SubjectValue sV : s.getSubjects()){
				total ++;
				if(sV.isTaken()) withGrade ++;
			}
		}
		
		String toReturn = "Id: " + p.getId() +
				"<br/>Plan Name:" + p.getName() +
				"<br/>Username: " + (p.getUser() != null ? p.getUser().getUsername() : "none") + 
				"<br/>User id: " + (p.getUser() != null ? p.getUser().getIdSun() : "none") + 
				"<br/>Career code: " + p.getCareerCode() + 
				"<br/>Semesters: " + p.getSemesters().size() +
				"<br/>Total number of subjects: " + total +
				"<br/>Number of subjects with grade: " + withGrade;
		return toReturn;
	}

}
