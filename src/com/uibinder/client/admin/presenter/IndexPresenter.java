package com.uibinder.client.admin.presenter;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
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

		view.setPresenter(this);
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

	/**************************** HANDLERS ****************************/
	
	@Override
	public void onDeleteAllDefaultPlans() {
		rpcAdminService.deleteAllDefaultPlans(new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error happend while deleting all the default plans - some were deleted");
				GWT.log("Error happend while deleting all the default plans - some were deleted");
			}

			@Override
			public void onSuccess(Object result) {
				Window.alert("All defautl plans were deleted");
				GWT.log("All defautl plans were deleted");
			}
			
		});
	}

	@Override
	public void onDeleteCertainDefaultPlanButton(final String careerCode) {
		rpcAdminService.deleteDefaultPlan(careerCode, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error happend while deleting default plan for career " + careerCode + " - plan not deleted");
				GWT.log("Error happend while deleting default plan for career " + careerCode + " - plan not deleted");
			}

			@Override
			public void onSuccess(Object result) {
				Window.alert("default plan for career " + careerCode + " was deleted");
				GWT.log("default plan for career " + careerCode + " was deleted");
			}
			
		});
	}

	@Override
	public void onResetAllHasAnalysisButton() {
		rpcAdminService.resetAllHasAnalysis(new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error happend while reseting all hasAnalysis fields");
				GWT.log("Error happend while reseting all hasAnalysis fields");
			}

			@Override
			public void onSuccess(Object result) {
				Window.alert("All hasAnalysis field have been reseted");
				GWT.log("All hasAnalysis field have been reseted");
			}
			
		});
	}

	public void onResetCertainHasAnalysisButton(String careerCode) {
		rpcAdminService.resetCertainHasAnalysis(careerCode, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error happend while reseting one hasAnalysis fields");
				GWT.log("Error happend while reseting one hasAnalysis fields");
			}

			@Override
			public void onSuccess(Object result) {
				Window.alert("hasAnalysis field has been reseted");
				GWT.log("hasAnalysis field has been reseted");
			}
			
		});
		
	}

	public void onResetUpdateCareersButton() {
		rpcAdminService.resetCareer(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("error while getting careers from the sia");
				GWT.log("error while getting careers from the sia");
			}
			public void onSuccess(Object result) {
				Window.alert("Careers updated from the sia");
				GWT.log("Careers updated from the sia");
			}
		});
	}

	public void onDeleteAllPlansButton() {
		rpcAdminService.deleteAllPlans(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("error while deleting all plans - some were deleted");
				GWT.log("error while deleting all plans - some were deleted");
			}
			public void onSuccess(Object result) {
				Window.alert("All plans deleted");
				GWT.log("All plans deleted");
			}
			
		});
	}

	public void onDeleteAllComplementaryValuesButton() {
		rpcAdminService.deleteAllComplementaryValues(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("error while deleting all complementaryValues - some were deleted");
				GWT.log("error while deleting all complementaryValues - some were deleted");
			}
			public void onSuccess(Object result) {
				Window.alert("All complementaryValues deleted");
				GWT.log("All complementaryValues deleted");
			}
			
		});
	}

	@Override
	public void onDeleteCertainComplementaryValuesButton(final String careerCode) {
		rpcAdminService.deleteCertainComplementaryValues(careerCode, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("error while deleting all complementaryValues for career " + careerCode + "- Some were deleted");
				GWT.log("error while deleting all complementaryValues for career " + careerCode + "- Some were deleted");
			}
			public void onSuccess(Object result) {
				Window.alert("All complementaryValues for career " + careerCode + " deleted");
				GWT.log("All complementaryValues for career " + careerCode + " deleted");
			}
			
		});
	}

	@Override
	public void onDeleteAllSubjectsButton() {
		rpcAdminService.deleteAllSubjects(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error deleting all the subjects - Some were deleted");
				GWT.log("Error deleting all the subjects - Some were deleted");
			}
			public void onSuccess(Object result) {
				Window.alert("All subjects deleted");
				GWT.log("All subjects deleted");
			}
			
		});
	}

	@Override
	public void onDeleteAllSubjectGroupsButton() {
		rpcAdminService.deleteAllSubjectGroup(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error deleting all the subjectGroups - some were deleted");
				GWT.log("Error deleting all the subjectGroups - some were deleted");
			}
			public void onSuccess(Object result) {
				Window.alert("All subjectGroups deleted");
				GWT.log("All subjectGroups deleted");
			}
			
		});
	}

	public void onResetAllHasDefaultButton() {
		rpcAdminService.resetAllHasDefaultField(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error reseting the hasDefautl field of all careers");
				GWT.log("Error reseting the hasDefautl field of all careers");
			}
			public void onSuccess(Object result) {
				Window.alert("No career has defautl now");
				GWT.log("No career has defautl now");
			}
			
		});
	}

	@Override
	public void onResetCertainHasDefaultButton(final String careerCode) {
		rpcAdminService.resetCertainHasDefaultField(careerCode, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error reseting the hasDefautl field of " + careerCode);
				GWT.log("Error reseting the hasDefautl field of " + careerCode);
			}
			public void onSuccess(Object result) {
				Window.alert(careerCode + " has no default");
				GWT.log(careerCode + " has no default");
			}
			
		});
	}

	@Override
	public void onDeleteAllSemestersButton() {
		rpcAdminService.deleteAllSemesters(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error deleting all the semesters");
				GWT.log("Error deleting all the semesters");
			}
			public void onSuccess(Object result) {
				Window.alert("All semester were deleted");
				GWT.log("All semester were deleted");
			}
			
		});
	}

	@Override
	public void onDeleteAllSubjectValueButton() {
		rpcAdminService.deleteAllSubjectValue(new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error deleting all the subjectValues");
				GWT.log("Error deleting all the subjectValues");
			}
			public void onSuccess(Object result) {
				Window.alert("All subjectValues were deleted");
				GWT.log("All subjectValues were deleted");
			}
			
		});
	}

	@Override
	public void onMakeUserAdminButton(final String userName) {
		rpcAdminService.makeUserAdmin(userName, new AsyncCallback(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error making userName: " + userName + " admin");
				GWT.log("Error making userName: " + userName + " admin");
			}
			@Override
			public void onSuccess(Object result) {
				Window.alert("UserName: " + userName + " is admin");
				GWT.log("UserName: " + userName + " is admin");
			}
		});
	}

	@Override
	public void onBlockUnblockUser(final String userName) {
		rpcAdminService.blockUnblockUser(userName, new AsyncCallback(){
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error blocking userName: " + userName);
				GWT.log("Error blocking userName: " + userName);
			}
			@Override
			public void onSuccess(Object result) {
				Window.alert("UserName: " + userName + " is (un)blocked");
				GWT.log("UserName: " + userName + " is (un)blocked");
			}
		});
	}
	
}
