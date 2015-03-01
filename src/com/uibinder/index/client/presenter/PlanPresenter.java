package com.uibinder.index.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.client.dnd.PickUpDragController;
import com.uibinder.index.client.dnd.SemesterDropController;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.PlanView;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.view.SearchSubjectViewImpl;
import com.uibinder.index.client.view.SiaSummaryView;
import com.uibinder.index.client.view.SiaSummaryViewImpl;
import com.uibinder.index.client.view.WarningDeleteSubjectView;
import com.uibinder.index.client.view.WarningDeleteSubjectViewImpl;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SemesterWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Semester;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

/**
 * @autor Mike W 
 * 
 * This manage all the UI regarding the plan display at the moment, 
 * perhaps later on it will be expanded to some broader functions
 * 
 * Be very precise with the subjects' code, because it will work (no errors will be made) when all the codes are different.   
 */
public class PlanPresenter implements Presenter, PlanView.Presenter, SiaSummaryView.Presenter, WarningDeleteSubjectView.Presenter {
	
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private SiaSummaryViewImpl siaSummaryView;
	private WarningDeleteSubjectViewImpl warningDeleteSubjectView = new WarningDeleteSubjectViewImpl();
	private SearchSubjectViewImpl searchSubjectView = new SearchSubjectViewImpl();
	private final SUNServiceAsync rpcService;
	
	private List<SubjectWidget> subjectWidgetList = new ArrayList<SubjectWidget>();
	private List<SemesterWidget> semesterWidgetList = new ArrayList<SemesterWidget>();
	
	private List<SubjectValues> subjectValuesList = new ArrayList<SubjectValues>();
	private HashMap<SubjectValues, Subject> valuesAndSubjectMap = new HashMap<SubjectValues, Subject>(); 
	private BiMap<SubjectValues, SubjectWidget> subjectValuesAndWidgetBiMap = HashBiMap.create();
	
	private List<Semester> semesterList = new ArrayList<Semester>();
	private BiMap<Semester, SemesterWidget> semesterAndWidgetBiMap = HashBiMap.create(); 
	private HashMap<SubjectValues, Semester> subjectValuesAndSemesterMap = new HashMap<SubjectValues, Semester>();	
	
	private int semesters = 0;
	private int subjects = 0;
	private HashMap<Semester, Integer> credits = new HashMap<Semester, Integer>();
	private HashMap<SemesterWidget, SemesterDropController> controllersBySemester= new HashMap<SemesterWidget, SemesterDropController>();
	private List<SemesterDropController> semesterDropControllerList = new ArrayList<SemesterDropController>();

	//Credits variables
	/**
	 * Here variable[0] = total of the plan, [1] Approved until now, [2]taken until now, [3]Total of the plan (a final an default value which depends on the career)
	 */
	private int[] totalCredits = {0,0,0,0};
	/**
	 * Here variable[0] = total, [1] Approved, [2]Necessary to graduate, the [2] item is a default and it is a final value set a the begging.
	 */
	private int[] foundationCredits = {0,0,0};
	private int[] disciplinaryCredits = {0,0,0};
	private int[] freeElectionCredits = {0,0,0};
	private int[] levelingCredits = {0,0,0};
	
	//Control classes
	private Plan plan = null;

	//Widget classes
	private PlanWidget planWidget;
	//private SiaSummary siaSummary;
	
	private VerticalPanel subContainer = new VerticalPanel();
	
	//Dnd Stuff
	private PickUpDragController dragController;
	private AbsolutePanel boundaryPanel;
	//This one is to allow or not drag items wherever
	private final boolean allowDroppingOnBoundaryPanel = false;
	
	
	/**
	 * This is the constructor to create an empty plan, that means no subjects
	 * at all, to make it 100% customizable for the user
	 * 
	 * 
	 * @param eventBus
	 * @param view
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView){
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView; 
		
	}
	
	/**
	 * Constructor to create a Plan based on one plan send by user
	 * 
	 * @param eventBus
	 * @param view
	 * @param plan
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView, Plan plan){

		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView;
		
		setPlan(plan);		
	}

	/**
	 * Constructor to create a Plan based on one career
	 * 
	 * @param eventBus
	 * @param view
	 * @param career
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView, String career){
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView;
		
		setPlan(plan);
		
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		addWidgets(container);
	}
	
	private void init() {
		
		//Create the dnd stuff
		boundaryPanel = new AbsolutePanel();
		dragController = new PickUpDragController(boundaryPanel, allowDroppingOnBoundaryPanel, this);
		
		planWidget = new PlanWidget();
		
	}
	
	private void addWidgets(HasWidgets container) {
		
		container.add(view.asWidget());
		
		subContainer.addStyleName("subContainerForPlan");
		boundaryPanel.add(planWidget);
		subContainer.add(boundaryPanel);
		
		subContainer.add(siaSummaryView.asWidget());
		
		subContainer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		
		container.add(warningDeleteSubjectView);
		container.add(searchSubjectView);
		warningDeleteSubjectView.hideIt();
		searchSubjectView.hideIt();
		container.add(subContainer);
		
	}
	
	private void makeSubjectWidgetDraggable(SubjectWidget subject){
		
		dragController.makeDraggable(subject, subject.getNameLabel());
		dragController.makeDraggable(subject, subject.getCodeLabel());
		dragController.makeDraggable(subject, subject.getCreditsLabel());
		dragController.makeDraggable(subject, subject.getGradeLabel());
		dragController.makeDraggable(subject, subject.getObligatorinessLabel());
		dragController.makeDraggable(subject, subject.getTypeLabel());
		
	}
	
	public Plan getPlan() {
		return plan;
	}

	/**
	 * This method will create all widgets and control classes needed
	 * 
	 * @param plan2
	 */
	public void setPlan(Plan plan2) {
		init();
		this.plan = new Plan();
		
		setCareer(plan2.getCareer());
		
		if(plan2.getSemesters() != null) {
			List<Semester> semesterListPlan = plan2.getSemesters();
			Map<SubjectValues, Subject> subjectMapPlan = plan2.getSubjectMap();
			List<SubjectValues> subjectValuesListPlan = null;
			
			//List<Semester> semesterList2 = new ArrayList<Semester>();
			//List<SubjectValues> subjectValuesList2 = new ArrayList<SubjectValues>();
			//Map<SubjectValues, Subject> subjectMap2 = new HashMap<SubjectValues, Subject>();
			
			for(Semester semester2 : semesterListPlan){
				createSemester(semester2);
				for(SubjectValues subjectValues2 : semester2.getSubjects()){
					createSubject(subjectMapPlan.get(subjectValues2) ,subjectValues2, semester2);
				}
			}
		}
		
	}

	private void setCareer(Career career) {
		this.plan.setCareer(career);
		foundationCredits[2] = career.getFoudationCredits();
		freeElectionCredits[2] = career.getFreeElectionCredits();
		disciplinaryCredits[2] = career.getDisciplinaryCredits();
	}

	private void createSemester(Semester semester) {
		
		semesterList.add(semester);
		credits.put(semester, 0);
		SemesterWidget semesterW = new SemesterWidget(semesterList.indexOf(semester), this);
		semesterW.setCredits(0);
		semesterWidgetList.add(semesterW);
		semesterAndWidgetBiMap.put(semester, semesterW);
		semesters++;
		planWidget.add(semesterW);
		
		addClickHandlerDeleteSemesterButton(semesterW);
		
		SemesterDropController dropController = new SemesterDropController(semesterW.getMainPanel(), semesterW, this);		
		dragController.registerDropController(dropController);
		semesterDropControllerList.add(dropController);
		controllersBySemester.put(semesterW, dropController);
		
	}
	
	private void createSubject(Subject subject2, SubjectValues subjectValues2, Semester semester2) {
		
		subjectValuesList.add(subjectValues2);
		valuesAndSubjectMap.put(subjectValues2, subject2);
		
		SubjectWidget subjectWidget = new SubjectWidget(valuesAndSubjectMap.get(subjectValues2).getName(), valuesAndSubjectMap.get(subjectValues2).getCode(), valuesAndSubjectMap.get(subjectValues2).getCredits(), subjectValues2.getGrade(), subjectValues2.isObligatoriness(), subjectValues2.getTypology());
		subjectWidgetList.add(subjectWidget);
		makeSubjectWidgetDraggable(subjectWidget);
		semesterAndWidgetBiMap.get(semester2).addSubject(subjectWidget);
		subjectValuesAndWidgetBiMap.put(subjectValues2, subjectWidget);
		subjectValuesAndSemesterMap.put(subjectValues2, semester2);
		
		updateCredits(subjectValues2, semester2, true);
		
	}

	private void updateCredits(SubjectValues subjectValues2, Semester semester2, boolean toAdd) {
		int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		if(toAdd == true){
			credits.put(semester2, credits.get(semester2) + creditsValue);
			semesterAndWidgetBiMap.get(semester2).setCredits(credits.get(semester2)+creditsValue);
			totalCredits[0] += creditsValue;
			if(subjectValues2.isTaken()) totalCredits[2]+= creditsValue;
			if(subjectValues2.getGrade()>=3) totalCredits[1] += creditsValue;
		} else {
			credits.put(semester2, credits.get(semester2) - creditsValue);
			semesterAndWidgetBiMap.get(semester2).setCredits(credits.get(semester2)-creditsValue);
			totalCredits[0] -= creditsValue;
			if(subjectValues2.isTaken()) totalCredits[2] -= creditsValue;
			if(subjectValues2.getGrade()>=3) totalCredits[1] -= creditsValue;
		}
		switch(subjectValues2.getTypology()){
		case "N":
		case "n":
			updateCreditsLeveling(subjectValues2, semester2, toAdd);
			break;
		case "L":
		case "l":
			updateCreditsFreeElection(subjectValues2, semester2, toAdd);
			break;
		case "D":
		case "d":
			updateCreditsDisciplinary(subjectValues2, semester2, toAdd);
			break;
		case "F":
		case "f":
			updateCreditsFoundation(subjectValues2, semester2, toAdd);
		}
		updateSiaSummary();
	}

	private void updateSiaSummary() {
		// TODO Auto-generated method stub
		
	}

	private void updateCreditsFoundation(SubjectValues subjectValues2, Semester semester2, boolean toAdd) {
		int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		if(toAdd== true){
			foundationCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) foundationCredits[1] += creditsValue;
		}else{
			foundationCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) foundationCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsDisciplinary(SubjectValues subjectValues2,	Semester semester2, boolean toAdd) {
		int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		if(toAdd== true){
			disciplinaryCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) disciplinaryCredits[1] += creditsValue;
		}else{
			disciplinaryCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) disciplinaryCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsFreeElection(SubjectValues subjectValues2, Semester semester2, boolean toAdd) {
		int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		if(toAdd== true){
			freeElectionCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) freeElectionCredits[1] += creditsValue;
		}else{
			freeElectionCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) freeElectionCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsLeveling(SubjectValues subjectValues2, Semester semester2, boolean toAdd) {
		int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		if(toAdd== true){
			levelingCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) levelingCredits[1] += creditsValue;
		}else{
			levelingCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) levelingCredits[1] -= creditsValue;
		}
		
	}
	
	private void addClickHandlerDeleteSemesterButton(SemesterWidget semester){
		semester.getDeleteSemesterButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				deleteSemesterClicked(event);
			}
			});
	}
	
	private void deleteSemesterClicked(ClickEvent event) {
		Widget w = (Widget) event.getSource();
		deleteSemester(Integer.valueOf(w.getElement().getAttribute("semester")));
	}

	private void deleteSemester(Integer valueOfSemester) {
		//deleting all subjects from a semester
		Semester semester = semesterList.get(valueOfSemester);
		for(int i=0; i<semester.getSubjects().size(); i++){
			deleteSubject(semester.getSubjects().get(i));
			i--;
		}
		
	}

	private void deleteSubject(SubjectValues subjectValues) {

		Subject subject = valuesAndSubjectMap.get(subjectValues);
		Semester semester = subjectValuesAndSemesterMap.get(subjectValues);
		
		semester.deleteSubject(subjectValues);
		//semesterAndWidgetBiMap.get(semester2).addSubject(subjectWidget); the next line replaces this one
		subjectValuesAndWidgetBiMap.get(subjectValues).removeFromParent();
		//makeSubjectWidgetDraggable(subjectWidget); sobra
		subjectWidgetList.remove(subjectValuesAndWidgetBiMap.get(subjectValues));
		
		valuesAndSubjectMap.remove(subjectValues);
		/*if(valuesAndSubjectMap.containsValue(subject) == false){ //means that there is no other subjectValue for a x subject, so subject must be deleted too
			subjectValuesList.remove(subject);			
		}*/
		subjectValuesList.remove(subjectValues);
		subjectValuesAndSemesterMap.remove(subjectValues);
		subjectValuesAndWidgetBiMap.remove(subjectValues);
		
		updateCredits(subjectValues, semester, false);
		
	}
}