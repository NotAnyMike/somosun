package com.uibinder.index.client.presenter;

import java.awt.Window;

import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.service.SUNService;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.PlanView;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.client.dnd.PickUpDragController;
import com.uibinder.index.shared.control.Subject;

/**
 * @autor Mike W 
 * 
 * This manage all the UI regarding the plan display at the moment, 
 * perhaps later on it will be expanded to some broader functions
 */
public class PlanPresenter implements Presenter, PlanView.Presenter {
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private PlanWidget plan;
	
	private VerticalPanel subContainer = new VerticalPanel();
	private VerticalPanelDropController dropController; 
	
	//Dnd Stuff
	private PickUpDragController dragController;
	private AbsolutePanel boundaryPanel;
	//This one is to allow or not drag items wherever
	private final boolean allowDroppingOnBoundaryPanel = false;
	
	public PlanPresenter(HandlerManager eventBus, PlanViewImpl view){
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		if(plan == null) init();
	}
	
	private void init() {
		//Create the dnd stuff
		boundaryPanel = new AbsolutePanel();
		dragController = new PickUpDragController(boundaryPanel, allowDroppingOnBoundaryPanel);
		
		plan = new PlanWidget();
		
		/************* to remove later on **************/
		//It is just for design purposes
		
		createSemesters(11);
		
		SUNServiceAsync sunServiceSvc = GWT.create(SUNService.class);
		
		sunServiceSvc.getSubjectByCode(123, callback);
		
		SubjectWidget subject1 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject2 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject3 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject4 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		makeSubjectDraggable(subject1);
		makeSubjectDraggable(subject2);
		makeSubjectDraggable(subject3);
		makeSubjectDraggable(subject4);
		SubjectWidget subject5 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject6 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject7 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject8 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject9 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject10 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject11 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject12 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject13 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject14 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		plan.addSubject(0, subject1);
		plan.addSubject(0, subject2);
		plan.addSubject(0, subject3);
		plan.addSubject(1, subject4);
		plan.addSubject(2, subject5);
		plan.addSubject(3, subject6);
		plan.addSubject(4, subject7);
		plan.addSubject(5, subject8);
		plan.addSubject(6, subject9);
		plan.addSubject(7, subject10);
		plan.addSubject(8, subject11);
		plan.addSubject(9, subject12);
		plan.addSubject(10, subject13);
	}
	
	//this method must be deleted later on
	private void methodToBeDeleted(Subject result) {
		SubjectWidget subjectxxx = new SubjectWidget(result.getName(),result.getCode(),result.getCredits(),true,1);;
		makeSubjectDraggable(subjectxxx);
		plan.addSubject(1, subjectxxx);
	}
	
	/**
	 * The callback function the one that will be run when the call returns 
	 */
	AsyncCallback<Subject> callback = new AsyncCallback<Subject>(){
		public void onFailure(Throwable caught){
			//Window.alert("RPC to sendEmail() failed.");
		}

		@Override
		public void onSuccess(Subject result) {
			methodToBeDeleted(result);
		}

	};


	@Override
	public void go(HasWidgets container) {
		container.clear();
		addWidgets(container);
	}
	
	private void createSemester(){
		plan.addSemester();
		dropController = new VerticalPanelDropController(plan.getMainPanelFromSemester(plan.getNumberOfSemesters()-1));
		dragController.registerDropController(dropController);
	}

	private void createSemesters(int k){
		for(int i = 0; i<k; i++){
			createSemester();
		}
	}
	
	private void makeSubjectDraggable(SubjectWidget subject){
		dragController.makeDraggable(subject, subject.getNameLabel());
		dragController.makeDraggable(subject, subject.getCodeLabel());
		dragController.makeDraggable(subject, subject.getCreditsLabel());
		dragController.makeDraggable(subject, subject.getGradeLabel());
		dragController.makeDraggable(subject, subject.getObligatorinessLabel());
		dragController.makeDraggable(subject, subject.getTypeLabel());
	}
	
	private void addWidgets(HasWidgets container) {
		
		container.add(view.asWidget());
		
		subContainer.addStyleName("subContainerForPlan");
		boundaryPanel.add(plan);
		subContainer.add(boundaryPanel);
		
		subContainer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		
		container.add(subContainer);
	}

}
