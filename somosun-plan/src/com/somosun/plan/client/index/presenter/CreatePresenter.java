package com.somosun.plan.client.index.presenter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.somosun.plan.client.index.event.ContinueDefaultCareerEvent;
import com.somosun.plan.client.index.event.ContinueDefaultCareerEventHandler;
import com.somosun.plan.client.index.event.GenerateAcademicHistoryFromStringEvent;
import com.somosun.plan.client.index.event.LoadPlanEvent;
import com.somosun.plan.client.index.event.NewEmptyPlanEvent;
import com.somosun.plan.client.index.service.SUNService;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.CreateView;
import com.somosun.plan.client.index.view.CreateViewImpl;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.PlanValuesResult;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Career;

public class CreatePresenter implements Presenter, CreateView.Presenter {
	
	private final SUNServiceAsync rpcService;
	private final HandlerManager eventBus;
	private CreateViewImpl view;
	private List<Career> careers = null;
	
	public CreatePresenter(SUNServiceAsync rpcService, HandlerManager eventBus, CreateViewImpl view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		getCareers("bog");
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	private void addCareersToListBox(List<Career> careers) {
		this.careers = careers;
		view.clearListBox();
		if(careers == null){
			view.addCareerToListBox("No se pudieron cargar las careeras","-1");			
		} else {
			for(Career career : careers){
				String name = SomosUNUtils.addCarrerCodeToString(career);
				view.addCareerToListBox(name, career.getCode());
			}
			onListBoxCreateChange(view.getCurrentDefaultCareerValue());
		}
	}

	@Override
	public void onResetButtonClicked() {
		view.clearTextBoxCreate();
	}

	@Override
	public void onListBoxCreateChange(String code) {
		Career c = getCareer(code);
		if(c != null){
			if(c.hasDefault() == false){
				view.setModelAnalyzedPlanButtonEnable(false);
			}
		}
	}

	private Career getCareer(String code) {
		Career career = null;
		for(Career cT : careers){
			if(cT.getCode().equals(code) == true){
				career = cT;
				break;
			}
		}
		return career;
	}

	@Override
	public void onContinueButtonClicked(String academicHistory) {
		view.clearTextBoxCreate();
		Window.alert("motherf*ckeR");
		eventBus.fireEvent(new GenerateAcademicHistoryFromStringEvent(academicHistory));
	}

	@Override
	public void getCareers(String sede) {
		rpcService.getCareers(sede, asyncGetCareers);
	}

	AsyncCallback<List<Career>> asyncGetCareers = new AsyncCallback<List<Career>>() {
		
		@Override
		public void onFailure(Throwable caught) {
			addCareersToListBox(null);
		}
		
		@Override
		public void onSuccess(List<Career> result) {
			addCareersToListBox(result);				
		}};

	public void onContinueDefaultButtonClick(String careerCode) {
		eventBus.fireEvent(new ContinueDefaultCareerEvent(careerCode));
	}

	@Override
	public void showWarning() {
		view.showWarning();
	}

	@Override
	public void hideWarning() {
		view.hideWarning();
	}

	@Override
	public void onNewAnalyzedPlanButtonClicked(String careerCode) {
		eventBus.fireEvent(new NewEmptyPlanEvent(careerCode));
	}
	
	public void onSelectPlanButtonClicked(String planId){
		eventBus.fireEvent(new LoadPlanEvent(planId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onDeletePlanButtonClicked(String planId) {
		rpcService.deletePlanFromUser(planId, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error deleting");
			}

			@Override
			public void onSuccess(Object result) {
				Window.alert("deleted");
				loadPlans();
			}
		});
	}
	
	public void loadPlans() {
		
		rpcService.getPlanValuesByUserLoggedIn(new AsyncCallback<List<PlanValuesResult>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error loading the user's plans");
			}

			@Override
			public void onSuccess(List<PlanValuesResult> result) {
				view.addPlans(result);
			}
			
		});
		
	}
	
	
	
}
