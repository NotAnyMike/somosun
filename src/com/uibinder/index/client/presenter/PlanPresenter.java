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
import com.uibinder.index.client.view.SiaSummary;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.client.dnd.PickUpDragController;
import com.uibinder.index.client.dnd.SemesterDropController;
import com.uibinder.index.shared.control.Plan;
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
	private final SUNServiceAsync rpcService;
	
	//Control classes
	private Plan plan;

	//Widget classes
	private PlanWidget planWidget;
	private SiaSummary siaSummary;
	
	private VerticalPanel subContainer = new VerticalPanel();
	private SemesterDropController dropController; 
	
	//Dnd Stuff
	private PickUpDragController dragController;
	private AbsolutePanel boundaryPanel;
	//This one is to allow or not drag items wherever
	private final boolean allowDroppingOnBoundaryPanel = false;
	
	/**
	 * This is the constructor to create an empty plan, that means no subjects
	 * at all, to make it 100% customizable for the user
	 * 
	 * @param eventBus
	 * @param view
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		plan = new Plan();
		
		if(planWidget == null) init();
	}
	
	/**
	 * Constructor to create a Plan based on one plan send by user
	 * 
	 * @param eventBus
	 * @param view
	 * @param plan
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, Plan plan){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		this.plan = plan;
		
		if(planWidget == null) init();
	}
	
	/**
	 * Constructor to create a Plan based on one career
	 * 
	 * @param eventBus
	 * @param view
	 * @param career
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, String career){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		this.plan = plan;
		
		if(planWidget == null) init();
	}
	
	private void init() {
		
		//Create the dnd stuff
		boundaryPanel = new AbsolutePanel();
		dragController = new PickUpDragController(boundaryPanel, allowDroppingOnBoundaryPanel);
		
		planWidget = new PlanWidget();
		
		//creating the Summary
		siaSummary = new SiaSummary();
		
		/************* to remove later on **************/
		//It is just for design purposes
		
		createSemesters(11);
		
		rpcService.getSubjectByCode(123, chargeSubjectByCode);
		
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
		planWidget.addSubject(0, subject1);
		planWidget.addSubject(0, subject2);
		planWidget.addSubject(0, subject3);
		planWidget.addSubject(1, subject4);
		planWidget.addSubject(2, subject5);
		planWidget.addSubject(3, subject6);
		planWidget.addSubject(4, subject7);
		planWidget.addSubject(5, subject8);
		planWidget.addSubject(6, subject9);
		planWidget.addSubject(7, subject10);
		planWidget.addSubject(8, subject11);
		planWidget.addSubject(9, subject12);
		planWidget.addSubject(10, subject13);
	}
	
	//this method must be deleted later on
	private void methodToBeDeleted(Subject result) {
		SubjectWidget subjectxxx = new SubjectWidget(result.getName(),result.getCode(),result.getCredits(),true,1);;
		makeSubjectDraggable(subjectxxx);
		planWidget.addSubject(1, subjectxxx);
	}
	
	/**
	 * The callback function the one that will be run when the call returns 
	 */
	AsyncCallback<Subject> chargeSubjectByCode = new AsyncCallback<Subject>(){
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
		planWidget.addSemester();
		dropController = new SemesterDropController(planWidget.getMainPanelFromSemester(planWidget.getNumberOfSemesters()-1), planWidget.getSemester(planWidget.getNumberOfSemesters()-1));
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
		boundaryPanel.add(planWidget);
		subContainer.add(boundaryPanel);
		
		subContainer.add(siaSummary.asWidget());
		
		subContainer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		
		container.add(subContainer);
	}

}
