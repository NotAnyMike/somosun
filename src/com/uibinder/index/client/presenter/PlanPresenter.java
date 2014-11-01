package com.uibinder.index.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.uibinder.index.client.dnd.PickUpDragController;
import com.uibinder.index.client.dnd.SemesterDropController;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.PlanView;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.view.SiaSummary;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SemesterWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Subject;

/**
 * @autor Mike W 
 * 
 * This manage all the UI regarding the plan display at the moment, 
 * perhaps later on it will be expanded to some broader functions
 * 
 * Be very precise with the subjects' code, because it will work (no errors will be made) when all the codes are different.   
 */
public class PlanPresenter implements Presenter, PlanView.Presenter {
	
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private final SUNServiceAsync rpcService;
	
	private HashMap<SubjectWidget,SemesterWidget> subjectsBySemester = new HashMap<SubjectWidget, SemesterWidget>();
	private HashMap<SemesterWidget, Integer> credits = new HashMap<SemesterWidget, Integer>();
	private List<SubjectWidget> subjectList = new ArrayList<SubjectWidget>();
	private List<SemesterWidget> semesterList = new ArrayList<SemesterWidget>();
	private int semesters = 0;
	private int subjects = 0;
	private int totalCredits = 0;
	
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
		
		createSemesters(2);
		
		//rpcService.getSubjectByCode(123, chargeSubjectByCode);
		
		createSubject("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",3,true,1,0);

	}
	
	/**
	 * it creates a subject from zero and it makes the subject draggable too
	 * @param name
	 * @param code
	 * @param credits
	 * @param isObligatory
	 * @param type
	 * @param semester
	 */
	private void createSubject(String name, String code, int credits, boolean isObligatory, int type, int semester){
		SubjectWidget subject = new SubjectWidget(name,code,credits,isObligatory,type);
		makeSubjectDraggable(subject);
		subjectList.add(subject); //adding the subject to its list
		
		subjects++;
		addSubject(subject, semester);
	}
	
	private void deleteSubject(SubjectWidget subject){
		
		subject.removeFromParent();
		subjects--;

		if(subjectList.contains(subject)==true){
			subjectList.get(subjectList.indexOf(subject)).removeFromParent();
		}
		
		if(subjectsBySemester.containsKey(subject)==true){
			subjectsBySemester.remove(subject);
		}
		
	}
	
	/**
	 * This method is for times where we just know the number of the semester and nothing else
	 * @param subject
	 * @param semester
	 */
	private void addSubject(SubjectWidget subject, int semester){
		if(semesterList.size()<=semester){
			createSemesters(semester - semesterList.size()+1);
		}
		addSubject(subject, semesterList.get(semester));
	}
	
	private void addSubject(SubjectWidget subject, SemesterWidget semester){
		setCredits(semester, credits.get(semester) + subject.getCredits());
		subjectsBySemester.put(subject, semester);
		semester.addSubject(subject);
	}
	
	public void subjectMoved(SubjectWidget subject, SemesterWidget semester){
		
		int creditsTemp = 0;
		int semesterNumber = semesterList.indexOf(semester);
		
		//creating enough semesters to be able to add the subject to its semester
		if(semesters < semesterNumber){
			createSemesters(semesterNumber - semesters);
		}
		
		//if Subject already exist
		if(subjectList.contains(subject)==true){
			
			SemesterWidget semesterTo = semester;
			
			if(subjectsBySemester.containsKey(subject)==true){ //means that this subject is not new				
				SemesterWidget semesterFrom = subjectsBySemester.get(subject);
				
				//getting the credits from its original semester and removing the ones from the subject which is leaving
				setCredits(semesterFrom, - subject.getCredits());
			}
			
			creditsTemp = 0;
			
			//Add the new information about its semester
			setCredits(semesterTo, subject.getCredits());
			
			subjectsBySemester.put(subject, semesterTo);
			
		}
		
	}
	
	private void setCredits(SemesterWidget semester, int creditsValue){
		credits.put(semester, credits.get(semester) + creditsValue);
		semester.setCredits(credits.get(semester));
		
		totalCredits += creditsValue;
	}
	
	//this method must be deleted later on
	/*private void methodToBeDeleted(Subject result) {
		SubjectWidget subjectxxx = new SubjectWidget(result.getName(),result.getCode(),result.getCredits(),true,1);;
		makeSubjectDraggable(subjectxxx);
		planWidget.addSubject(1, subjectxxx);
	}*/
	
	/**
	 * The callback function the one that will be run when the call returns 
	 */
	/*AsyncCallback<Subject> chargeSubjectByCode = new AsyncCallback<Subject>(){
		public void onFailure(Throwable caught){
			//Window.alert("RPC to sendEmail() failed.");
		}

		@Override
		public void onSuccess(Subject result) {
			methodToBeDeleted(result);
		}

	};*/


	@Override
	public void go(HasWidgets container) {
		container.clear();
		addWidgets(container);
	}
	
	private void createSemester(){
		
		SemesterWidget semester = new SemesterWidget(semesters, this);
		semester.setSemester(semesters);
		semester.setCredits(0);

		planWidget.add(semester);
		semesterList.add(semester);
		credits.put(semester, 0);
		semesters++;
		
		dropController = new SemesterDropController(semester.getMainPanel(), semester, this);
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
	
	/**
	 * To keep the presenter and all the plan updated using the observer pattern (actually this is the quick and dirty way)
	 * 
	 * TODO: implement the interface of the Observer pattern, ie the Observer and Observable interface
	 *
	 * @param code
	 * @param semester
	 */
	public void updateSemesters(String code, SemesterWidget semester){
		if(getSubjectByCodeFromList(code) != null){
			subjectMoved(getSubjectByCodeFromList(code), semester);
		}
	}
	
	public void onSubjectDelete(SubjectWidget subject){
		//TODO update semesters and delete the subject
	}
	
	/**
	 * When this method is called it is STRICTLY necessary to have and IF statement when this method return null.
	 * 
	 * @param code
	 * @return
	 */
	private SubjectWidget getSubjectByCodeFromList(String code){
		SubjectWidget subjectToReturn = null;
		for(SubjectWidget subject : subjectList){
			if(subject.getCode().equals(code)){
				subjectToReturn = subject;
			}
		}
		return subjectToReturn;
	}

}
