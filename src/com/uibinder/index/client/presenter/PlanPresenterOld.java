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
public class PlanPresenterOld implements Presenter, PlanView.Presenter, SiaSummaryView.Presenter, WarningDeleteSubjectView.Presenter {
	
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private SiaSummaryViewImpl siaSummaryView;
	private WarningDeleteSubjectViewImpl warningDeleteSubjectView = new WarningDeleteSubjectViewImpl();
	private SearchSubjectViewImpl searchSubjectView = new SearchSubjectViewImpl();
	private final SUNServiceAsync rpcService;
	
	private HashMap<SubjectWidget,SemesterWidget> subjectWidgetsBySemester = new HashMap<SubjectWidget, SemesterWidget>();
	private List<SubjectWidget> subjectWidgetList = new ArrayList<SubjectWidget>();
	private List<SemesterWidget> semesterWidgetList = new ArrayList<SemesterWidget>();
	
	private List<SubjectValues> subjectValuesList = new ArrayList<SubjectValues>();
	private List<Semester> semesterList = new ArrayList<Semester>();
	private HashMap<SubjectValues, Semester> subjectValuesBySemester = new HashMap<SubjectValues, Semester>();
	
	private BiMap<SubjectValues, SubjectWidget> subjectAndWidgetBiMap = HashBiMap.create();
	private HashMap<SubjectValues, Subject> subjectAndValuesMap = new HashMap<SubjectValues, Subject>(); 
	private BiMap<Semester, SemesterWidget> semesterMap = HashBiMap.create(); 
	
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
	public PlanPresenterOld(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView){
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView; 
		
		setPlan(new Plan());
		
		addDefaultToCredits(null);
		
		if(planWidget == null) init();
	}
	
	/**
	 * Constructor to create a Plan based on one plan send by user
	 * 
	 * @param eventBus
	 * @param view
	 * @param plan
	 */
	public PlanPresenterOld(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView, Plan plan){
		
		setPlan(plan);

		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView;
		
		addDefaultToCredits(plan.getCareerCode());
		
		if(planWidget == null) init();
		
	}

	/**
	 * Constructor to create a Plan based on one career
	 * 
	 * @param eventBus
	 * @param view
	 * @param career
	 */
	public PlanPresenterOld(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView, String career){
		
		setPlan(plan);
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		this.siaSummaryView = siaSummaryView;
		
		addDefaultToCredits(career);
		
		if(planWidget == null) init();
	}
	
	private void addClickHandlerAddSemester(){
		planWidget.getLabelAddSemester().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onClickAddSemester();
			}});
		planWidget.getImageAddSemester().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onClickAddSemester();
			}
			
		});
	}
	
	private void addClickHandlerAddSubject(SemesterWidget semester){
		semester.getAddButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				searchSubjectView.showIt();
			}});
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

	private void onClickAddSemester(){
		createSemesterWidget();
	}
	
	private void updateSemesterNumbersByRemoved(int semesterDeleted){
		for(int i= semesterDeleted; i < semesters;i++){
			semesterWidgetList.get(i).setSemester(i);
		}
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
		
		addClickHandlerAddSemester();
		
		//creating the Summary
		//siaSummary = new SiaSummary();
		
		/************* to remove later on **************/
		//It is just for design purposes
		
		createSemesterWidgets(2);
		
		//rpcService.getSubjectByCode(123, chargeSubjectByCode);
		
		//TODO Delete it
		createSubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI", "0000r42", 3, 3.2, true, 0, 0);
		createSubjectWidget("La globalización es buena", "00s0r42", 3, 5, true, 2, 1);
		createSubjectWidget("Si toca, toca!", "0asf0r42", 3, true, 1, 0);
		createSubjectWidget("Avanza más la Uniminuto que este proyecto", "021r42", 3, 4.2, true, 3, 2);

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
	
	/**
	 * This method fills the necessary credits in each type  to be able to graduate,
	 * if careerCode == null then it will have infinite credits 
	 * @param careerCode
	 */
	private void addDefaultToCredits(String careerCode){
		if(careerCode==null){
			foundationCredits[2] = 999;
			disciplinaryCredits[2] = 999;
			freeElectionCredits[2] = 999;
			levelingCredits[2] = 999;
			totalCredits[3] = 999;
		}else{
			//TODO get by a service the amount of credits needed to graduate for every type
			foundationCredits[2] = 60;
			disciplinaryCredits[2] = 60;
			freeElectionCredits[2] = 60;
			levelingCredits[2] = 12;
			totalCredits[3] = foundationCredits[2] + disciplinaryCredits[2] + freeElectionCredits[2] + levelingCredits[2];
		}
		
	}
	
	/**
	 * it creates a subject with a grade from zero and it makes the subject draggable too,
	 * @param name
	 * @param code
	 * @param credits
	 * @param isObligatory
	 * @param type
	 * @param semester
	 */
	private void createSubjectWidget(String name, String code, int credits, double grade ,boolean isObligatory, int type, int semester){
		SubjectWidget subject = new SubjectWidget(name,code,credits,grade,isObligatory,type);
		createSubjectWidgetGeneric(subject, semester);
	}
	
	/**
	 * it creates a subject without a grade from zero and it makes the subject draggable too,
	 * @param name
	 * @param code
	 * @param credits
	 * @param isObligatory
	 * @param type
	 * @param semester
	 */
	private void createSubjectWidget(String name, String code, int credits, boolean isObligatory, int type, int semester){
		SubjectWidget subject = new SubjectWidget(name,code,credits,isObligatory,type);
		createSubjectWidgetGeneric(subject, semester);
	}
	
	/**
	 * The generic method, the procedures that both creatSubject have in common 
	 * @param subject
	 * @param semester
	 */
	private void createSubjectWidgetGeneric(SubjectWidget subject, int semester){
		makeSubjectWidgetDraggable(subject);
		subjectWidgetList.add(subject); //adding the subject to its list
		
		addSubjectWidget(subject, semester);		
	}
	
	/**
	 * To confirm the deletion of the subject
	 * @param code
	 */
	private void confirmDeleteSubject(String code){
		SubjectWidget subjectToSend = getSubjectWidgetByCodeFromList(code);
		warningDeleteSubjectView.setSubject(code, subjectToSend.getName(), subjectToSend.getGrade(), subjectToSend.getCredits(), subjectToSend.isOblig(), subjectToSend.getType());
		warningDeleteSubjectView.showIt();		
	}
	
	/**
	 * When it was confirmed that the subject must be deleted
	 * @param b
	 * @param code
	 */
	public void confirmedDeleteSubject(String code){
		deleteSubjectWidget(getSubjectWidgetByCodeFromList(code));
		warningDeleteSubjectView.hideIt();
	}
	
	private void deleteSubjectWidget(SubjectWidget subject){
		if(subjectWidgetList.contains(subject)==true){
			subjectWidgetList.remove((subjectWidgetList.indexOf(subject)));
			subjects--;
		}
		
		if(subjectWidgetsBySemester.containsKey(subject)==true){
			//remove the credits
			setCredits(subjectWidgetsBySemester.get(subject), -subject.getCredits(), subject.getType(), subject.getApproved(), subject.getTaken());
			subjectWidgetsBySemester.remove(subject);
		}
		
		dragController.makeNotDraggable(subject);
		subject.removeFromParent();
		
	}
	
	public void subjectMoved(SubjectWidget subject, SemesterWidget semester){
		
		int semesterNumber = semesterWidgetList.indexOf(semester);
		
		//creating enough semesters to be able to add the subject to its semester
		if(semesters < semesterNumber){
			createSemesterWidgets(semesterNumber - semesters);
		}
		
		//if Subject already exist
		if(subjectWidgetList.contains(subject)==true){
			
			SemesterWidget semesterTo = semester;
			
			if(subjectWidgetsBySemester.containsKey(subject)==true){ //means that this subject is not new and it is coming from some semester				
				SemesterWidget semesterFrom = subjectWidgetsBySemester.get(subject);
				
				//getting the credits from its original semester and removing the ones from the subject which is leaving
				setCredits(semesterFrom, - subject.getCredits(), subject.getType(), subject.getApproved(), subject.getTaken());
			}
			
			//Add the new information about its semester
			setCredits(semesterTo, subject.getCredits(), subject.getType(), subject.getApproved(), subject.getTaken());
			
			subjectWidgetsBySemester.put(subject, semesterTo);

		}
		
	}
	
	private void setCredits(Semester semester, int creditsValue, int typology, boolean approved, boolean taken){
		if(semesterMap.containsKey(semester) == true){
			setCredits(semester, semesterMap.get(semester), creditsValue, typology, approved, taken);
		}
	}
	
	private void setCredits(SemesterWidget semesterWidget, int creditsValue, int typology, boolean approved, boolean taken){
		if(semesterMap.containsValue(semesterWidget)==true){
			setCredits(semesterMap.inverse().get(semesterWidget), semesterWidget, creditsValue, typology, approved, taken);
		}
	}
	
	/**
	 * TODO add the part of approved credits and necessary credits (if it was taken)
	 * TODO update the view (siaSummary)
	 * 
	 * @param semester
	 * @param creditsValue
	 * @param typology
	 */
	private void setCredits(Semester semester, SemesterWidget semesterWidget, int creditsValue, int typology, boolean approved, boolean taken){
		if(semesterMap.containsKey(semester) == true){
			
			credits.put(semester, credits.get(semester) + creditsValue);
			semesterWidget.setCredits(credits.get(semester));
			
			totalCredits[0] += creditsValue;
			if(approved) totalCredits[1] += creditsValue;
			if(taken == true || approved == true) totalCredits[2] += creditsValue;
			
			siaSummaryView.setAvance(totalCredits[1]/totalCredits[3]);
			siaSummaryView.setAdditionalyCredits(((totalCredits[1]*2-(totalCredits[2]-totalCredits[1]) > 80) ? 80 : totalCredits[1]*2-(totalCredits[2]-totalCredits[1])));
			siaSummaryView.setApprovedCredits(totalCredits[1]);
			siaSummaryView.setGPA(getGPA());
			
			
			switch(typology){
			//Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4 Añadir para posgrado
			case 0: //Nivelación 
				levelingCredits[0] += creditsValue;
				if(approved) levelingCredits[1] += creditsValue;
				siaSummaryView.setLevelingCredits(levelingCredits[1], levelingCredits[2]);
				break;
			case 1: //Fundamentación
				foundationCredits[0] += creditsValue;
				if(approved) foundationCredits[1] += creditsValue;
				siaSummaryView.setFoundationCredits(foundationCredits[1], foundationCredits[2]);
				break;
			case 2: //Disciplinary
				disciplinaryCredits[0] += creditsValue;
				if(approved) disciplinaryCredits[1] += creditsValue;
				siaSummaryView.setDisciplinaryCredits(disciplinaryCredits[1], disciplinaryCredits[2]);
				break;
			case 3: //Libre
				freeElectionCredits[0] += creditsValue;
				if(approved) freeElectionCredits[1] += creditsValue;
				siaSummaryView.setFreeElectionCredits(freeElectionCredits[1], freeElectionCredits[2]);
				break;
			}
			
		}
		updateTotalsOnSiaSummaryView();
	}
	
	private void makeSubjectWidgetDraggable(SubjectWidget subject){
		dragController.makeDraggable(subject, subject.getNameLabel());
		dragController.makeDraggable(subject, subject.getCodeLabel());
		dragController.makeDraggable(subject, subject.getCreditsLabel());
		dragController.makeDraggable(subject, subject.getGradeLabel());
		dragController.makeDraggable(subject, subject.getObligatorinessLabel());
		dragController.makeDraggable(subject, subject.getTypeLabel());
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
		if(getSubjectWidgetByCodeFromList(code) != null){
			subjectMoved(getSubjectWidgetByCodeFromList(code), semester);
		}
	}
	
	/**
	 * This method is usually used by the pickUpController when some subject is dropped outside of any dropController
	 * @param code
	 */
	public void onSubjectDelete(String code){
		confirmDeleteSubject(code);
		//deleteSubject(getSubjectByCodeFromList(code));
	}
	
	/**
	 * When this method is called it is STRICTLY necessary to have and IF statement when this method return null.
	 * 
	 * @param code
	 * @return
	 */
	private SubjectWidget getSubjectWidgetByCodeFromList(String code){
		SubjectWidget subjectToReturn = null;
		for(SubjectWidget subject : subjectWidgetList){
			if(subject.getCode().equals(code)){
				subjectToReturn = subject;
			}
		}
		return subjectToReturn;
	}
	
	private double getGPA(){
		double gpa = 0;
		for(SubjectWidget subject : subjectWidgetList){
			if(subject.getTaken()==true){
				gpa += subject.getCredits()*subject.getGrade();
			}
		}
		gpa = (double) Math.round((gpa/totalCredits[2]) * 100) / 100;;
		return gpa;
	}
	
	private void updateTotalsOnSiaSummaryView(){
		siaSummaryView.setTotalApproved(totalCredits[1]);
		siaSummaryView.setTotalNecessary(totalCredits[3]);
		siaSummaryView.setTotalPerCent(Math.round(totalCredits[1]/totalCredits[3]));
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		//this.plan = plan;
		plan = new Plan();
		generatePlanFromPlan(plan);
	}

	private void generatePlanFromPlan(Plan plan) {		
		if(plan != null){			
			List<Semester> semesterListPlan = plan.getSemesters();
			Career careerPlan = plan.getCareer();
			Double papa = plan.getPAPA();
			Map<SubjectValues, Subject> subjectMapPlan = plan.getSubjectValuesAndSubjectMap();
			
			for(Semester semester : semesterListPlan){
				for(SubjectValues subjectValues : semester.getSubjects()){
					addSubject(subjectMapPlan.get(subjectValues), subjectValues, semester);
				}
			}
		}
		//this.plan.setCareer(career);
		
	}
	
	private void addSubject(Subject subject, SubjectValues subjectValues, Semester semester){
		addSubjectControl(subject, subjectValues, semester);
		addSubjectWidget(subjectValues, subject, semester);
		setCredits(semester, subject.getCredits(), 0, false, false);
		subjects++;
	}
	
	private void addSubjectControl(Subject subject, SubjectValues subjectValues, Semester semester){
		subjectValuesList.add(subjectValues);
		subjectValuesBySemester.put(subjectValues, semester);
		subjectAndValuesMap.put(subjectValues, subject);
		subjects ++;
	}
	
	private void addSubjectWidget(SubjectValues subjectValues, Subject subject, Semester semester){
		SubjectWidget subjectWidget = new SubjectWidget(subject.getName(), subject.getCode(), subject.getCredits(), true, 0);
		addSubjectWidget(subjectWidget, semesterList.indexOf(semester));
	}
	
	
	/**
	 * This method finds the semesterWidget which correspond to the int semester and call addSubject(subjectW, SemesterW)
	 * @param subject
	 * @param semester
	 */
	private void addSubjectWidget(SubjectWidget subject, int semester){
		if(semesterWidgetList.size()<=semester){
			createSemesters(semester - semesterWidgetList.size()+1);
		}
		addSubjectWidget(subject, semesterWidgetList.get(semester));
	}
	
	/**
	 * Add a semester to its corresponding semester, it also takes care of the credits part
	 * @param subject
	 * @param semester
	 */
	private void addSubjectWidget(SubjectWidget subject, SemesterWidget semester){
		subjectWidgetsBySemester.put(subject, semester);
		semester.addSubject(subject);
	}
	
	private void deleteSubjectControl(Subject subject){
		
	}
	
	private void addSemester(Semester semester){
		addSemester(semester);
		createSemesterWidget();
		credits.put(semester, 0);
		semesters++;
	}
	
	private void createSemester(){
		addSemester(new Semester(null));
	}

	private void createSemesterWidget(){
		
		SemesterWidget semester = new SemesterWidget(semesters, this);
		semester.setSemester(semesters);
		semester.setCredits(0);

		planWidget.add(semester);
		semesterWidgetList.add(semester);
		
		addClickHandlerDeleteSemesterButton(semester);
		addClickHandlerAddSubject(semester);
		
		//dropController = new SemesterDropController(semester.getMainPanel(), semester, this);
		SemesterDropController dropController = new SemesterDropController(semester.getMainPanel(), semester, this);		
		dragController.registerDropController(dropController);
		semesterDropControllerList.add(dropController);
		controllersBySemester.put(semester, dropController);
		
	}

	private void createSemesters(int k){
		for(int i = 0; i<k; i++){
			createSemester();
		}
	}
	
	private void addSemesterControl(Semester semester){
		semesterList.add(semester);
	}
	
	private void deleteSemesterControl(int semesterNumber){
		
	}
	
	private void deleteSemester(int semesterNumber){
		deleteSemesterWidget(semesterNumber);
		deleteSemesterControl(semesterNumber);
	}
	
	private void deleteSemesterWidget(int semester) {
		//Getting all the subjects of the semester
		
		SemesterWidget semesterWidget = semesterWidgetList.get(semester);
		
		//true is it has some subjets on that semester
		if(subjectWidgetsBySemester.containsValue(semesterWidget)){
			//DELTE ALL THE SUBJECTS
			/*for(SubjectWidget subject : subjectsBySemester.keySet()){
				if(subjectsBySemester.get(subject) == semesterWidget){
					deleteSubject(subject);					
				}
			}*/
			//TODO Improve the algorithm, is completely wrong!
			for(int i=0;i<subjectWidgetList.size();i++){
				//DELTE ALL THE SUBJECTS
				if(subjectWidgetsBySemester.get(subjectWidgetList.get(i)) == semesterWidget){
					deleteSubjectWidget(subjectWidgetList.get(i));
					i--;
				}
			}
		}
		
		if(semesterWidgetList.contains(semesterWidget)==true && semesterWidgetList.size()>1){
			semesterWidgetList.remove((semesterWidgetList.indexOf(semesterWidget)));
			semesters--;
			
			dragController.unregisterDropController(controllersBySemester.get(semesterWidget));
			planWidget.remove(semesterWidget);
			
			//Update the number of the semesters
			updateSemesterNumbersByRemoved(semester);
		}			
			
	}

}