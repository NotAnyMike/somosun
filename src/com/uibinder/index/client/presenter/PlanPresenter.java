package com.uibinder.index.client.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

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
import com.uibinder.index.client.view.SubjectAccordionView;
import com.uibinder.index.client.view.SubjectAccordionViewImpl;
import com.uibinder.index.client.view.WarningDeleteSubjectView;
import com.uibinder.index.client.view.WarningDeleteSubjectViewImpl;
import com.uibinder.index.client.widget.LineWidget;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SemesterWidget;
import com.uibinder.index.client.widget.SubjectWidget;
import com.uibinder.index.shared.SiaResultSubjects;
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
public class PlanPresenter implements Presenter, PlanView.Presenter, SiaSummaryView.Presenter, WarningDeleteSubjectView.Presenter, SubjectAccordionView.Presenter {
	
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
	
	//in order to control how many times a subject gets its requisites updated
	private final int LIMIT_TO_REQUISITES_UPDATES = 1;
	private HashMap<Subject, Integer> subjectTimesUpdated = new HashMap<Subject, Integer>();
	
	//In order to control the publicId of the subjectWidgets
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
	
	private SubjectValues subjectValuesSelected = null;
	private List<Career> careers = new ArrayList<Career>();
	
	AsyncCallback<List<Career>> asyncGetCareers = new AsyncCallback<List<Career>>() {
		
		@Override
		public void onFailure(Throwable caught) {
		}
		
		@Override
		public void onSuccess(List<Career> result) {
			careers = result;
			addCareersToListBox(careers);				
		}};
	
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
		
		getCareers("bog");
		
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
		
		getCareers("bog");
		
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
		
		getCareers("bog");
		
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
		
		addClickSearchField();
		
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
		
		if(subjectValuesList.contains(subjectValues)==false) {
			subjectValuesList.add(subjectValues);
			if(subjectTimesUpdated.containsKey(subject) == false) subjectTimesUpdated.put(subject, 0);
		}
		if(valuesAndSubjectMap.containsKey(subjectValues)==false) valuesAndSubjectMap.put(subjectValues, subject); //although the condition here can be removed because it can will just override it
		if(semester.getSubjects().contains(subjectValues) == false) semester.addSubject(subjectValues);
		
		SubjectWidget subjectWidget = new SubjectWidget(subject.getName(), subject.getCode(), subject.getCredits(), subjectValues.getGrade(), subjectValues.getComplementaryValues().isMandatory(), subjectValues.getComplementaryValues().getTypology(), subjectValues.getSubjectValuesPublicId());
		subjectWidgetList.add(subjectWidget);
		makeSubjectWidgetDraggable(subjectWidget);
		semesterAndWidgetBiMap.get(semester).addSubject(subjectWidget);
		subjectValuesAndWidgetBiMap.put(subjectValues, subjectWidget);
		subjectValuesAndSemesterMap.put(subjectValues, semester);
		
		updateCredits(subjectValues, semester, true);
		
		addClickHandlerSubjectWidget(subjectWidget);
		
	}
	
	public void moveArrows(String publicId){
		SubjectValues sV = getSubjectValuesByPublicId(publicId);
		if(sV != null){
			//if((subjectValuesSelected.equals(sV) == true || areRelated(sV, subjectValuesSelected) == true) || true)
			showConnections(subjectValuesSelected);
		}
	}

	private boolean areRelated(SubjectValues sV, SubjectValues sV2) {
		boolean toReturn = false;
		List<Subject> relatedSubjects = new ArrayList<Subject>();
		relatedSubjects.addAll(sV.getComplementaryValues().getListPrerequisites());
		relatedSubjects.addAll(sV.getComplementaryValues().getListCorequisites());
		relatedSubjects.addAll(sV.getComplementaryValues().getListPrerequisitesOf());
		relatedSubjects.addAll(sV.getComplementaryValues().getListCorequisitesOf());
		for(Subject s : relatedSubjects){
			if(s.equals(valuesAndSubjectMap.get(sV2))){
				toReturn = true;
				break;
			}
		}
		return toReturn;
	}

	private void showConnections(SubjectValues sV) {
		if(sV !=null){
			for(Subject s : sV.getComplementaryValues().getListPrerequisites()){
				for(SubjectValues sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sVTempo), subjectValuesAndWidgetBiMap.get(sV), "pre");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListCorequisites()){
				for(SubjectValues sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sVTempo), subjectValuesAndWidgetBiMap.get(sV), "co");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListPrerequisitesOf()){
				for(SubjectValues sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sV), subjectValuesAndWidgetBiMap.get(sVTempo), "pre");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListCorequisitesOf()){
				for(SubjectValues sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sV), subjectValuesAndWidgetBiMap.get(sVTempo), "co");
				}
			}
		}
	}

	private void hideArrows() {
		connectionsController.hideArrows();
	}

	private void modifyOpacity(SubjectValues sV) {
		boolean isRelated = false;
		List<Subject> subjectRelatedList = new ArrayList<Subject>();
		ComplementaryValues cV = sV.getComplementaryValues();
		
		subjectRelatedList.addAll(cV.getListPrerequisites());
		subjectRelatedList.addAll(cV.getListCorequisites());
		subjectRelatedList.addAll(cV.getListPrerequisitesOf());
		subjectRelatedList.addAll(cV.getListCorequisitesOf());
		for(SubjectValues sVTemporary : subjectValuesList){
			isRelated = false;
			for(Subject sTemporary : subjectRelatedList){
				if(sTemporary.equals(valuesAndSubjectMap.get(sVTemporary))==true){
					isRelated = true;
					subjectRelatedList.remove(sTemporary);
				}				
			}
			if(isRelated == false && sVTemporary.equals(sV)==false){
				subjectValuesAndWidgetBiMap.get(sVTemporary).getElement().setAttribute("style", "opacity:0.1");
			}else{
				subjectValuesAndWidgetBiMap.get(sVTemporary).getElement().setAttribute("style", "opacity:1");
			}
		}
	}

	private void getComplementaryValues(SubjectValues sV) {
		int timesUpdated = subjectTimesUpdated.get(valuesAndSubjectMap.get(sV));
		if(timesUpdated < LIMIT_TO_REQUISITES_UPDATES){
			subjectTimesUpdated.put(valuesAndSubjectMap.get(sV), timesUpdated+1); 
			rpcService.getComplementaryValues(plan.getCareerCode(), valuesAndSubjectMap.get(sV).getCode(), new AsyncCallback<ComplementaryValues>(){
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO delete this msg
					Window.alert("sorry, we got an error while updating the requisites of a subject");
				}
	
				@Override
				public void onSuccess(ComplementaryValues result) {
					boolean isOldNull = false;
					if(result != null){
						List<SubjectValues> list = getSubjectValuesBySubject(result.getSubject());
						for(SubjectValues sV : list){
							if(sV.getComplementaryValues()== null || sV.getComplementaryValues().getListCorequisites().size() == 0 || sV.getComplementaryValues().getListPrerequisitesOf().size() == 0 || sV.getComplementaryValues().getListCorequisitesOf().size() == 0 || sV.getComplementaryValues().getListPrerequisites().size() == 0) isOldNull = true;//TODO: show arrows and opacity again
							sV.setComplementaryValues(result);
							//the main subject is pre requisite of some subject, and it is adding it to them
							for(Subject s : result.getListPrerequisites()){
								for(SubjectValues sVTemporary : getSubjectValuesBySubject(s)){
									sVTemporary.getComplementaryValues().addPrerequisiteOf(result.getSubject());
								}
							}
							//the main subject is co requisite of some subject, and it is adding it to them
							for(Subject s : result.getListCorequisites()){
								for(SubjectValues sVTemporary : getSubjectValuesBySubject(s)){
									sVTemporary.getComplementaryValues().addCorequisiteOf(result.getSubject());
								}
							}
							if(isOldNull == true)  
								if(sV.equals(subjectValuesSelected) == true) {
									modifyOpacity(sV);
									showConnections(sV);
								}
						}
					}
					
				}
				
			});
		}
	}
	
	private List<SubjectValues> getSubjectValuesBySubject(Subject s){
		List<SubjectValues> list = new ArrayList<SubjectValues>();
		for(SubjectValues sV : subjectValuesList){
			if(valuesAndSubjectMap.get(sV).equals(s) == true){
				list.add(sV);
			}
		}
		return list;
	}

	public void getCareers(String sede) {
		rpcService.getCareers(sede, asyncGetCareers);
	}
	
	private void addCareersToListBox(List<Career> careers) {
		if(careers == null){
			searchSubjectView.addCareerToListBox("No se pudieron cargar las careeras","1");			
		} else {
			searchSubjectView.setCareerList(careers);
		}
	}
	
	public void deleteAllOpacities() {
		for(SubjectWidget sW : subjectWidgetList){
			sW.getElement().setAttribute("style", "opacity:1");
		}
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
			if(semesterList.contains(semester) == true) {
				semesterList.remove(semester);
			}
			semesterWidgetList.remove(semesterW);
		}
		
		updateSemestersNumber();
		
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(6), "pre");
		connectionsController.addConnection(subjectWidgetList.get(0), subjectWidgetList.get(7), "pre");
		
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
	 * This method gets triggered when a subject is by the user, dragged, it could be dragged in the same semester, but this method won't be triggered if it dropped it outside
	 * 
	 * @param code
	 * @param semester
	 */
	public void subjectMoved(String publicId, SemesterWidget semester) {
		SubjectValues subjectValues = getSubjectValuesByPublicId(publicId);
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
	private SubjectValues getSubjectValuesByPublicId(String publicId){
		SubjectValues subjectValuesToReturn = null;
		if(subjectValuesList.isEmpty() == false){
			for(SubjectValues subjectValues : subjectValuesList){
				if(subjectValues.getSubjectValuesPublicId().equals(publicId)){
					subjectValuesToReturn = subjectValues;
				}
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
	
	private void addClickHandlerAddSubject(SemesterWidget semesterW){
		semesterW.getAddButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onClickAddSubject();
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

	private void confirmDeleteSubject(SubjectValues subjectValuesToDelete) {
		Subject subjectToDelete = valuesAndSubjectMap.get(subjectValuesToDelete);
		warningDeleteSubjectView.setSubject(subjectValuesToDelete, subjectToDelete);
		warningDeleteSubjectView.showIt();
	}

	public void confirmedDeleteSubject(SubjectValues sV) {
		deleteSubject(sV);
		warningDeleteSubjectView.hideIt();
	}

	public SubjectValues getSubjectValuesSelected() {
		return subjectValuesSelected;
	}

	public void setSubjectValuesSelected(SubjectValues subjectValuesSelected) {
		this.subjectValuesSelected = subjectValuesSelected;
	}

	public void addIcon(Icon i) {
		// TODO Auto-generated method stub
		subContainer.add(i);
	}	
	
	protected void loadSubjectsToSearchView(SiaResultSubjects result) {
		addSubjectsToSearchView(result.getSubjectList());
		createPagination(result);
		showToolTip();
	}

	private void addSubjectsToSearchView(List<Subject> subjectList) {
		
		searchSubjectView.clearAccordionContainer();
		
		for(Subject s : subjectList){
			SubjectAccordionViewImpl v = new SubjectAccordionViewImpl(searchSubjectView.getSubjectsAmmount());
			v.setPresenter(this);
			v.setCareerList(careers);
			v.setHeader(s.getCode(), s.getName(), "L", Integer.toString(s.getCredits()));
			v.setSubjectGroupName("Asignaturas de relleno");
			searchSubjectView.addSubject(v);
		}
		
	}

	private void createPagination(SiaResultSubjects result) {
		searchSubjectView.clearPagination();
		
		AnchorListItem first = new AnchorListItem();
		first.setIcon(IconType.ANGLE_DOUBLE_LEFT);
		
		if(result.getPage() == 1) first.setEnabled(false);
		else first.setEnabled(true); 
		first.setActive(false);

		searchSubjectView.addPage(first);
		
		for(int x = 0; x < result.getNumPaginas(); x++){
			AnchorListItem a = new AnchorListItem(Integer.toString(x+1));
			
			if(x+1 == result.getPage()) a.setActive(true);
			else a.setActive(false); 
			
			a.setEnabled(true);
			searchSubjectView.addPage(a);
		}
		
		AnchorListItem last = new AnchorListItem();
		last.setIcon(IconType.ANGLE_DOUBLE_RIGHT);
		
		if(result.getPage() == result.getNumPaginas()) last.setEnabled(false);
		else last.setEnabled(true); 
		last.setActive(false);
		searchSubjectView.addPage(last);
	}

	/************ behaviors when clicked *******************/
	
	public void onAddSpecificSubjectClick(ClickEvent event) {
		//TODO
	}

	private void onClickAddSemester() {
		// TODO Auto-generated method stub
		if(semesters <=20){
			createSemester(new Semester());
		}
	}

	public void onSearchButtonClicked(String s, String careerCode, String type, int page) {
		
		rpcService.toTest(new AsyncCallback<String>(){
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		if(type == "all") {
			type = "";
		}
		rpcService.getSubjectsFromSia(s, type, careerCode, "bog", page, new AsyncCallback<SiaResultSubjects>(){
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry, we couldn't connect to SIA");
			}
			
			@Override
			public void onSuccess(SiaResultSubjects result) {
				loadSubjectsToSearchView(result);
			}
			
		});
	}

	public void onSubjectDelete(String publicid) {
		SubjectValues subjectValuesToDelete = getSubjectValuesByPublicId(publicid);
		if(subjectValuesToDelete != null){
			confirmDeleteSubject(subjectValuesToDelete);
		}
		
	}
	
	private void onSubjectWidgetClicked(String publicId) {
		SubjectValues sV = getSubjectValuesByPublicId(publicId);
		if(subjectValuesSelected != null) {
			deleteAllOpacities();
			hideArrows();
		}
		if(sV.equals(subjectValuesSelected)==false){
			subjectValuesSelected = sV;
			
			//get complementary values from sia
			getComplementaryValues(sV);
			
			//TODO: Show/Create lines
			showConnections(sV);
			
			//Reduce/Increase opacity
			modifyOpacity(sV);
		}else{
			setSubjectValuesSelected(null);
		}
	}
	
	private void onClickAddSubject(){
		searchSubjectView.showIt();
		showToolTip();
		arrangeTopOfSearchSubjectView();
	}
	
	/************ JQUERY FUNCTIONS ***************/

	/**
	 * This method makes that the SearchSubjectView appears right where it was clicked
	 */
	public static native void arrangeTopOfSearchSubjectView() /*-{
		$wnd.arrangeTopOfSearchBox();
	}-*/;
	
	/**
	 * This method takes care of showing the property "title" in a better way
	 */
	public static native void showToolTip() /*-{
	  $wnd.showTooltip();
	}-*/;
	
	/**
	 * This method takes care of making the searchBox work with enter
	 */
	public static native void addClickSearchField() /*-{
		$wnd.addClickSearchField();
	}-*/;
	
	/********************************************/

	
}