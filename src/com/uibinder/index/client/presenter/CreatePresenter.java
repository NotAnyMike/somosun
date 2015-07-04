package com.uibinder.index.client.presenter;

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
import com.uibinder.index.client.event.ContinueDefaultCareerEvent;
import com.uibinder.index.client.event.ContinueDefaultCareerEventHandler;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.CreateView;
import com.uibinder.index.client.view.CreateViewImpl;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.control.Career;

public class CreatePresenter implements Presenter, CreateView.Presenter {
	
	private final SUNServiceAsync rpcService;
	private final HandlerManager eventBus;
	private CreateViewImpl view;
	
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
		if(careers == null){
			view.addCareerToListBox("No se pudieron cargar las careeras","-1");			
		} else {
			for(Career career : careers){
				String name = "(" + career.getCode() + ") " + career.getName().toUpperCase().charAt(0) + career.getName().substring(1);
				view.addCareerToListBox(name, career.getCode());
			}
		}
	}

	@Override
	public void onResetButtonClicked() {
		view.clearTextBoxCreate();
	}

	@Override
	public void onListBoxCreateChange(String career) {
		//TODO add here to deactivate the button of model Plan
	}

	@Override
	public void onContinueButtonClicked(String academicHistory) {
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
	
	
}
