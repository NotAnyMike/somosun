package com.uibinder.index.client.presenter;

import java.util.ArrayList;
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
import com.uibinder.index.client.connection.ConnectionsController;
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
import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SemesterWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.ComplementaryValues;
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
	
	private int increasingSubjectValuesCounter = 0;

	//Credits variables
	/**
	 * Here variable[0] = total of the plan, [1] Approved until now, [2]taken until now, [3]Total of the plan (a final an default value which depends on the career)
	 */
	private int[] totalCredits = {0,0,0,0};
	/**
	 * Here variable[0] = total, [1] Approved, [2]Necessary to graduate, the [2] item is a default and it is a final value set a the beginning.
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
	
	private ConnectionsController connectionsController;

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
		connectionsController = new ConnectionsController(this);
		
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
		connectionsController = new ConnectionsController(this);
		
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
		connectionsController = new ConnectionsController(this);
		
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
		dragController.setBehaviorDragStartSensitivity(1);
		
		planWidget = new PlanWidget();
		
		addClickHandlerAddSemester();
		
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
		
		connectionsController.setContainer(subContainer);
		
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
		this.plan = plan2;
		//this.plan = new Plan();
		
		setCareer(plan.getCareer());
		
		if(plan.getSemesters() != null) {
			List<Semester> semesterListPlan = plan2.getSemesters();
			Map<SubjectValues, Subject> subjectMapPlan = plan2.getValuesAndSubjectMap();
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
		//this.plan.setCareer(career);
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
		
		addClickHandlerAddSubject(semesterW);
		
	}
	
	private void createSubject(Subject subject, SubjectValues subjectValues, Semester semester) {
		
		//To control where and what subject is dropped
		increasingSubjectValuesCounter++;
		subjectValues.setSubjectValuesPublicId(subject.getCode() + increasingSubjectValuesCounter);
		
		if(subjectValuesList.contains(subjectValues)==false) subjectValuesList.add(subjectValues);
		if(valuesAndSubjectMap.containsKey(subjectValues)==false) valuesAndSubjectMap.put(subjectValues, subject); //although the condition here can be removed because it can will just override it
		if(semester.getSubjects().contains(subjectValues) == false) semester.addSubject(subjectValues);
		
		SubjectWidget subjectWidget = new SubjectWidget(subject.getName(), subject.getCode(), subject.getCredits(), subjectValues.getGrade(), subjectValues.getComplementaryValues().isObligatoriness(), subjectValues.getComplementaryValues().getTypology(), subjectValues.getSubjectValuesPublicId());
		subjectWidgetList.add(subjectWidget);
		makeSubjectWidgetDraggable(subjectWidget);
		semesterAndWidgetBiMap.get(semester).addSubject(subjectWidget);
		subjectValuesAndWidgetBiMap.put(subjectValues, subjectWidget);
		subjectValuesAndSemesterMap.put(subjectValues, semester);
		
		updateCredits(subjectValues, semester, true);
		
		addClickHandlerSubjectWidget(subjectWidget);
		
	}
	
	private void onSubjectWidgetClicked(String publicId) {
		SubjectValues sV = getSubjectValuesByPublicIdFromList(publicId);
		ComplementaryValues cV = sV.getComplementaryValues();
		List<Subject> subjectRelatedList = new ArrayList<Subject>();
		//TODO: get complementary values from sia
		
		//TODO: Show/Create lines
		
		//TODO: Reduce/Increase opacity
		subjectRelatedList.addAll(cV.getListPrerequisites());
		subjectRelatedList.addAll(cV.getListCorequisites());
		subjectRelatedList.addAll(cV.getListPosrequisites());
		for(SubjectValues sVTemporary : subjectValuesList){
			if(subjectRelatedList.contains(valuesAndSubjectMap.get(sVTemporary))==false && sVTemporary != sV){
				subjectValuesAndWidgetBiMap.get(sVTemporary).getElement().setAttribute("style", "opacity:0.1");
			}
		}
		Window.alert(publicId);
	}

	private void updateCredits(SubjectValues subjectValues2, Semester semester2, boolean toAdd) {
		
		if(valuesAndSubjectMap.containsKey(subjectValues2) == true){ 
			int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
			
			if(toAdd == true){
				credits.put(semester2, credits.get(semester2) + creditsValue);
				semesterAndWidgetBiMap.get(semester2).setCredits(credits.get(semester2));
				totalCredits[0] += creditsValue;
				if(subjectValues2.isTaken()) totalCredits[2]+= creditsValue;
				if(subjectValues2.getGrade()>=3) totalCredits[1] += creditsValue;
			} else {
				credits.put(semester2, credits.get(semester2) - creditsValue);
				semesterAndWidgetBiMap.get(semester2).setCredits(credits.get(semester2));
				totalCredits[0] -= creditsValue;
				if(subjectValues2.isTaken()) totalCredits[2] -= creditsValue;
				if(subjectValues2.getGrade()>=3) totalCredits[1] -= creditsValue;
			}
			switch(subjectValues2.getComplementaryValues().getTypology()){
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
	
	private void deleteSemesterClicked(ClickEvent event) {
		Widget w = (Widget) event.getSource();
		deleteSemester(Integer.valueOf(w.getElement().getAttribute("semester")));
	}

	private void deleteSemester(Integer valueOfSemester) {
		//deleting all subjects from a semester
		Semester semester = semesterList.get(valueOfSemester);
		SemesterWidget semesterW = semesterAndWidgetBiMap.get(semester);

		while(semester.getSubjects().size() != 0){
			deleteSubject(semester.getSubjects().get(0));
		}
		
		if(semesterList.size()!=1){
			dragController.unregisterDropController(controllersBySemester.get(semesterW));
			semesterW.removeFromParent();
			credits.remove(semester);
			controllersBySemester.remove(semesterW);
			if(semesterAndWidgetBiMap.containsKey(semester) == true) semesterAndWidgetBiMap.remove(semester);
			if(semesterList.contains(semester) == true) semesterList.remove(semester);
			semesterWidgetList.remove(semesterW);
		}
		
		updateSemestersNumber();
		
		connectionsController.addConnection(subjectWidgetList.get(6), subjectWidgetList.get(0), "pre");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(8), "pre");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(7), "pre");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(10), "pre");
		connectionsController.addConnection(subjectWidgetList.get(11), subjectWidgetList.get(10), "pre");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(1), "co");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(2), "co");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(3), "co");
		connectionsController.addConnection(subjectWidgetList.get(4), subjectWidgetList.get(2), "co");
		connectionsController.addConnection(subjectWidgetList.get(4), subjectWidgetList.get(8), "co");
		
		Window.alert(subjectValuesList.get(7).getComplementaryValues().getListPrerequisites().toString());
		
		rpcService.toTest(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				Window.alert(result);
			}
			
		});
		
	}

	/**
	 * this method will be called to update the number of the semesters and to updated its number in the attribute of the widget
	 */
	private void updateSemestersNumber() {
		for(Semester s : semesterList){
			semesterAndWidgetBiMap.get(s).setSemester(semesterList.indexOf(s));
		}
	}

	private void deleteSubject(SubjectValues subjectValues) {
		
		Subject subject = valuesAndSubjectMap.get(subjectValues);
		SubjectWidget subjectW = subjectValuesAndWidgetBiMap.get(subjectValues);
		Semester semester = subjectValuesAndSemesterMap.get(subjectValues);
		
		updateCredits(subjectValues, semester, false);
		
		if(semester.getSubjects().contains(subjectValues) == true) semester.deleteSubject(subjectValues);
		//semesterAndWidgetBiMap.get(semester2).addSubject(subjectWidget); the next line replaces this one
		subjectValuesAndWidgetBiMap.get(subjectValues).removeFromParent();
		//makeSubjectWidgetDraggable(subjectWidget); sobra
		subjectWidgetList.remove(subjectValuesAndWidgetBiMap.get(subjectValues));
		
		valuesAndSubjectMap.remove(subjectValues);
		/*if(valuesAndSubjectMap.containsValue(subject) == false){ //means that there is no other subjectValue for a x subject, so subject must be deleted too
			subjectValuesList.remove(subject);			
		}*/
		
		subjectWidgetList.remove(subjectW);
		subjectValuesList.remove(subjectValues);
		subjectValuesAndSemesterMap.remove(subjectValues);
		subjectValuesAndWidgetBiMap.remove(subjectValues);
		
	}

	/**
	 * This mehtod gets triggered when a subject is by the user, dragged, it could be dragged in the same semester, but this method won't be triggered if it dropped it outside
	 * 
	 * @param code
	 * @param semester
	 */
	public void subjectMoved(String publicId, SemesterWidget semester) {
		SubjectValues subjectValues = getSubjectValuesByPublicIdFromList(publicId);
		int numberSemesterTo = semesterList.indexOf(semesterAndWidgetBiMap.inverse().get(semester));
		Semester semesterTo = semesterList.get(numberSemesterTo);
		int numberSemesterFrom = semesterList.indexOf(subjectValuesAndSemesterMap.get(subjectValues));
		Semester semesterFrom = semesterList.get(numberSemesterFrom);

		if(subjectValues != null){
			semesterFrom.deleteSubject(subjectValues);
			semesterTo.addSubject(subjectValues);
			subjectValuesAndSemesterMap.remove(subjectValues);
			subjectValuesAndSemesterMap.put(subjectValues, semesterTo);
			updateCredits(subjectValues, semesterFrom, false);
			updateCredits(subjectValues, semesterTo, true);
		}else{
			Window.alert("sorry we had a problem");
		}
		
	}
	
	/**
	 * When this method is called it is STRICTLY necessary to have and IF statement when this method return null.
	 * 
	 * @param idSubjectValue
	 * @return
	 */
	private SubjectValues getSubjectValuesByPublicIdFromList(String publicId){
		SubjectValues subjectValuesToReturn = null;
		for(SubjectValues subjectValues : subjectValuesList){
			if(subjectValues.getSubjectValuesPublicId().equals(publicId)){
				subjectValuesToReturn = subjectValues;
			}
		}
		return subjectValuesToReturn;
	}

	private void updateSiaSummary() {
		updateDefaultCredits();
		updateApprovedCredits();
		updatePercentageCredits();
		updateSmallSummary();
	}

	private void updatePercentageCredits() {
		if(disciplinaryCredits[2]!=0) siaSummaryView.setPercentageDisciplinaryCredits(disciplinaryCredits[1]/disciplinaryCredits[2]*100);
		else siaSummaryView.setPercentageDisciplinaryCredits(0);
		
		if(foundationCredits[2]!=0) siaSummaryView.setPercentageFoundationCredits(foundationCredits[1]/foundationCredits[2]*100);
		else siaSummaryView.setPercentageFoundationCredits(0);
		
		if(freeElectionCredits[2]!=0) siaSummaryView.setPercentageFreeElectionCredits(freeElectionCredits[1]/freeElectionCredits[2]*100);
		else siaSummaryView.setPercentageFreeElectionCredits(0);
		
		if(levelingCredits[2]!=0) siaSummaryView.setPercentageLevelingCredits(levelingCredits[1]/levelingCredits[2]*100);
		else siaSummaryView.setPercentageLevelingCredits(0);
		
		if(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]!=0){
			siaSummaryView.setTotalPerCent((foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1])/(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2])*100);
			siaSummaryView.setAvance((foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1])/(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2])*100);
		} else {
			siaSummaryView.setTotalPerCent(0);
			siaSummaryView.setAvance(0);
		}
	}

	private void updateSmallSummary() {
		siaSummaryView.setGPA(plan.getPAPA());
		siaSummaryView.setApprovedCredits(totalCredits[1]);
		siaSummaryView.setAdditionalyCredits(0); //TODO 
	}

	private void updateApprovedCredits() {
		siaSummaryView.setApprovedFoundationCredits(foundationCredits[1]);
		siaSummaryView.setApprovedFreeElectionCredits(freeElectionCredits[1]);
		siaSummaryView.setApprovedDisciplinaryCredits(disciplinaryCredits[1]);
		siaSummaryView.setApprovedLevelingCredits(levelingCredits[1]);
		siaSummaryView.setTotalApproved(foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1]);
	}

	private void updateDefaultCredits() {
		siaSummaryView.setDefaultFoundationCredits(foundationCredits[2]);
		siaSummaryView.setDefaultFreeElectionCredits(freeElectionCredits[2]);
		siaSummaryView.setDefaultDisciplinaryCredits(disciplinaryCredits[2]);
		siaSummaryView.setDefaultLevelingCredits(levelingCredits[2]);
		siaSummaryView.setTotalNecessary(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]);
	}
	
	public void addLine(LineWidget l){
		subContainer.add(l);
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

	private void onClickAddSemester() {
		// TODO Auto-generated method stub
		if(semesters <=20){
			createSemester(new Semester());
		}
	}
	
	private void addClickHandlerAddSubject(SemesterWidget semesterW){
		semesterW.getAddButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				searchSubjectView.showIt();
			}});
	}
	
	private void addClickHandlerSubjectWidget(SubjectWidget s){
		s.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				onSubjectWidgetClicked(event.getRelativeElement().getAttribute("publicId"));
			}			
		});
	}
	
	private void addClickHandlerDeleteSemesterButton(SemesterWidget semester){
		semester.getDeleteSemesterButton().addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				deleteSemesterClicked(event);
			}
			});
	}

	public void onSubjectDelete(String publicid) {
		SubjectValues subjectValuesToDelete = getSubjectValuesByPublicIdFromList(publicid);
		if(subjectValuesToDelete != null){
			confirmDeleteSubject(subjectValuesToDelete);
		}
		
	}

	private void confirmDeleteSubject(SubjectValues subjectValuesToDelete) {
		Subject subjectToDelete = valuesAndSubjectMap.get(subjectValuesToDelete);
		warningDeleteSubjectView.setSubject(subjectValuesToDelete, subjectToDelete);
		warningDeleteSubjectView.showIt();
	}

	public void confirmedDeleteSubject(SubjectValues sV) {
		deleteSubject(sV);
		warningDeleteSubjectView.hideIt();
	}
	

}