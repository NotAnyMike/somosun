package com.uibinder.client.admin.presenter;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.client.admin.service.AdminServiceAsync;
import com.uibinder.client.admin.view.IndexView;
import com.uibinder.client.admin.view.IndexViewImpl;
import com.uibinder.client.index.presenter.Presenter;
import com.uibinder.client.index.service.SUNServiceAsync;
import com.uibinder.shared.SomosUNUtils;
import com.uibinder.shared.control.Career;

public class IndexPresenter implements Presenter, IndexView.Presenter {

	private IndexViewImpl view;
	private SUNServiceAsync rpcService;
	private AdminServiceAsync rpcAdminService;
	
	private List<Career> careers;
	
	public IndexPresenter(SUNServiceAsync rpcService, AdminServiceAsync rpcAdminService, IndexViewImpl view){
		this.view = view;
		this.rpcService = rpcService;
		this.rpcAdminService = rpcAdminService;
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		
		if(careers==null){
			view.cleanLists();
			getCareers();
		}
	}
	
	private void getCareers(){
		rpcService.getCareers("bog", new AsyncCallback<List<Career>>(){

			@Override
			public void onFailure(Throwable caught) {
				view.addCareerToListBox("No se pudieron cargar las careeras","-1");
				GWT.log("unable to load the careers");
			}

			@Override
			public void onSuccess(List<Career> result) {
				addCareersToListBoxes(result);
			}
			
		});
	}
	
	private void addCareersToListBoxes(List<Career> careers) {
		this.careers = careers;
		if(careers == null){
			view.addCareerToListBox("No se pudieron cargar las careeras","-1");			
		} else {
			for(Career career : careers){
				String name = SomosUNUtils.addCarrerCodeToString(career);
				view.addCareerToListBox(name, career.getCode());
			}
		}
	}
	
}
