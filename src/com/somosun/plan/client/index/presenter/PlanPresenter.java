package com.somosun.plan.client.index.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListItem;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.somosun.plan.client.index.connection.ConnectionsController;
import com.somosun.plan.client.index.dnd.PickUpDragController;
import com.somosun.plan.client.index.dnd.SemesterDropController;
import com.somosun.plan.client.index.event.SavePlanAsDefaultEvent;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.ComplementaryValueView;
import com.somosun.plan.client.index.view.ComplementaryValueViewImpl;
import com.somosun.plan.client.index.view.DefaultSubjectCreationView;
import com.somosun.plan.client.index.view.DefaultSubjectCreationViewImpl;
import com.somosun.plan.client.index.view.PlanView;
import com.somosun.plan.client.index.view.PlanViewImpl;
import com.somosun.plan.client.index.view.SearchSubjectViewImpl;
import com.somosun.plan.client.index.view.SelectedSubjectView;
import com.somosun.plan.client.index.view.SelectedSubjectViewImpl;
import com.somosun.plan.client.index.view.SiaSummaryView;
import com.somosun.plan.client.index.view.SiaSummaryViewImpl;
import com.somosun.plan.client.index.view.SubjectAccordionView;
import com.somosun.plan.client.index.view.SubjectAccordionViewImpl;
import com.somosun.plan.client.index.view.WarningDeleteSubjectView;
import com.somosun.plan.client.index.view.WarningDeleteSubjectViewImpl;
import com.somosun.plan.client.index.widget.LineWidget;
import com.somosun.plan.client.index.widget.PlanWidget;
import com.somosun.plan.client.index.widget.SemesterWidget;
import com.somosun.plan.client.index.widget.SubjectWidget;
import com.somosun.plan.shared.CompletePlanInfo;
import com.somosun.plan.shared.SiaResultGroups;
import com.somosun.plan.shared.SiaResultSubjects;
import com.somosun.plan.shared.SomosUNUtils;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.ComplementaryValue;
import com.somosun.plan.shared.control.Group;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Student;
import com.somosun.plan.shared.control.Subject;
import com.somosun.plan.shared.control.SubjectGroup;
import com.somosun.plan.shared.control.SubjectValue;
import com.somosun.plan.shared.values.TypologyCodes;

/**
 * @autor Mike W 
 * 
 * This manage all the UI regarding the plan display at the moment, 
 * perhaps later on it will be expanded to some broader functions
 * 
 * Be very precise with the subjects' code, because it will work (no errors will be made) when all the codes are different.   
 */
public class PlanPresenter implements Presenter,
PlanView.Presenter, 
SiaSummaryView.Presenter, 
WarningDeleteSubjectView.Presenter, 
SubjectAccordionView.Presenter, 
SelectedSubjectView.Presenter,
DefaultSubjectCreationView.Presenter,
ComplementaryValueView.Presenter{
	
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private SiaSummaryViewImpl siaSummaryView;
	private WarningDeleteSubjectViewImpl warningDeleteSubjectView = new WarningDeleteSubjectViewImpl();
	private SearchSubjectViewImpl searchSubjectView = new SearchSubjectViewImpl();
	private DefaultSubjectCreationViewImpl defaultSubjectCreationView = new DefaultSubjectCreationViewImpl(); 
	private final SUNServiceAsync rpcService;
	private Student student = null;
	
	private List<SubjectWidget> subjectWidgetList = new ArrayList<SubjectWidget>();
	private List<SemesterWidget> semesterWidgetList = new ArrayList<SemesterWidget>();
	
	private List<SubjectValue> subjectValuesList = new ArrayList<SubjectValue>(); 
	private BiMap<SubjectValue, SubjectWidget> subjectValuesAndWidgetBiMap = HashBiMap.create();
	
	private List<Semester> semesterList = new ArrayList<Semester>();
	private BiMap<Semester, SemesterWidget> semesterAndWidgetBiMap = HashBiMap.create(); 
	private HashMap<SubjectValue, Semester> subjectValuesAndSemesterMap = new HashMap<SubjectValue, Semester>();	
	
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
	 * Here variable[0] = total, [1] Approved, [2]Necessary to graduate, the [2] item is a default and it is a final value set a the beginning, [3] approved and needed, i.e. in my case I have aproved more than needed and some are not obligatory, so those don't count.
	 */
	private int[] foundationCredits = {0,0,0,0};
	private int[] disciplinaryCredits = {0,0,0,0};
	private int[] freeElectionCredits = {0,0,0,0};
	private int[] levelingCredits = {0,0,0,0};
	
	//Control classes
	private Plan plan = new Plan();

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
	
	private SubjectValue subjectValuesSelected = null;
	private List<Career> careers = new ArrayList<Career>();
	
	private List<SubjectAccordionViewImpl> accordions = new ArrayList<SubjectAccordionViewImpl>();
	private List<SelectedSubjectViewImpl> selectedSubjects = new ArrayList<SelectedSubjectViewImpl>();
	
	private boolean changeNameAsked = false;
	
	private SemesterValue currentSemesterValue = null;
	
	//in order to control the accordions
	Map<SubjectAccordionViewImpl, ComplementaryValueViewImpl> mapAccordion = new HashMap<SubjectAccordionViewImpl, ComplementaryValueViewImpl>();
	
	/********************** asyncCallbacks variables *******************************/
	
	AsyncCallback<List<Career>> asyncGetCareers = new AsyncCallback<List<Career>>() {
		
		public void onFailure(Throwable caught) {
		}
		
		public void onSuccess(List<Career> result) {
			careers = result;
			addCareersToListBox(careers);			
		}
	};
		
	
	AsyncCallback<List<ComplementaryValue>> asyncGetComplementaryValuesByCareer = new AsyncCallback<List<ComplementaryValue>>(){
		
		public void onFailure(Throwable caught) {
		}
		
		public void onSuccess(List<ComplementaryValue> result) {
			setComplementaryValues(result);
		}
		
	};
	
	/********************** asyncCallbacks variables *******************************/
	
	
	/**
	 * This is the constructor to create an empty plan, that means no subjects
	 * at all, to make it 100% customizable for the user
	 * 
	 * 
	 * @param eventBus
	 * @param view
	 */
	public PlanPresenter(SUNServiceAsync rpcService, HandlerManager eventBus, PlanViewImpl view, SiaSummaryViewImpl siaSummaryView, Student student){
		
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		this.setStudent(student);
		warningDeleteSubjectView.setPresenter(this);
		searchSubjectView.setPresenter(this);
		searchSubjectView.fill();
		defaultSubjectCreationView.setPresenter(this);
		this.siaSummaryView = siaSummaryView;
		siaSummaryView.setPresenter(this);
		connectionsController = new ConnectionsController(this);
		
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		addWidgets(container);
		addFunctionsToSecondaryMenu();
		makeDropdownButtonsWork();
	}
	
	private void init() {
		
		//Create the dnd stuff
		boundaryPanel = new AbsolutePanel();
		dragController = new PickUpDragController(boundaryPanel, allowDroppingOnBoundaryPanel, this);
		dragController.setBehaviorDragStartSensitivity(1);
		
		planWidget = new PlanWidget();
		
		addClickHandlersToPlan();
		
	}
	
	private void addWidgets(HasWidgets container) {
		
		container.add(view.asWidget());
		
		subContainer.addStyleName("subContainerForPlan");
		boundaryPanel.add(planWidget);
		subContainer.add(boundaryPanel);
		
		subContainer.add(siaSummaryView.asWidget());

		subContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		container.add(warningDeleteSubjectView);
		container.add(searchSubjectView);
		container.add(defaultSubjectCreationView);
		warningDeleteSubjectView.hideIt();
		searchSubjectView.hideIt();
		container.add(subContainer);
		
		addEventListenerOnChangeToSiaSummary();
		addClickSearchField();
		selectCurrentSemesterActions();
		
	}
	
	private void makeSubjectWidgetDraggable(SubjectWidget subject){
		
		dragController.makeDraggable(subject, subject.getNameLabel());
		dragController.makeDraggable(subject, subject.getCodeLabel());
		dragController.makeDraggable(subject, subject.getCreditsLabel());
		dragController.makeDraggable(subject, subject.getGradeLabel());
		dragController.makeDraggable(subject, subject.getObligatorinessLabel());
		dragController.makeDraggable(subject, subject.getTypeLabel());
		
	}
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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
		//this.plan.setSemesters(semesterList); // then the semesters in plan2 become semesterList
		//this.plan = new Plan();
		
		//rpcService.getComplementaryValuesFromMisPlanes(plan.getCareerCode(), asyncGetComplementaryValuesByCareer);
		if(plan != null){
			
			setCareer(plan.getCareer());
			
			/********* taking care of the complete-plan button **********/
			if(plan.getCareer().hasAnalysis() == false && plan.getCareer().hasDefault() == false){
				if(siaSummaryView != null){
					siaSummaryView.removeCompletePlanButton();
				}
			}
			/************************************************************/
			
			if(plan2.getSemesters() != null) {
				List<Semester> semesterListPlan = plan2.getSemesters();
				//Map<SubjectValues, Subject> subjectMapPlan = plan2.getValuesAndSubjectMap();
				//List<SubjectValues> subjectValuesListPlan = null;
				
				//List<Semester> semesterList2 = new ArrayList<Semester>();
				//List<SubjectValues> subjectValuesList2 = new ArrayList<SubjectValues>();
				//Map<SubjectValues, Subject> subjectMap2 = new HashMap<SubjectValues, Subject>();
				
				for(Semester semester2 : semesterListPlan){
					createSemester(semester2, false);
					for(SubjectValue subjectValues2 : semester2.getSubjects()){
						if(subjectValues2 != null){
							if(subjectValues2.getComplementaryValues() != null){
								if(subjectValues2.getComplementaryValues().getSubject() != null ){
									createSubject(subjectValues2.getComplementaryValues().getSubject() ,subjectValues2, semester2);								
								}
							}
						}
					}
				}
				
				plan.setSemesters(semesterList);
				
				getCareers("bog");
				
			}
		
		}
	}

	private void setCareer(Career career) {
		//this.plan.setCareer(career);
		foundationCredits[2] = career.getFoudationCredits();
		freeElectionCredits[2] = career.getFreeElectionCredits();
		disciplinaryCredits[2] = career.getDisciplinaryCredits();
	}

	private void createSemester(Semester semester){
		createSemester(semester, true);
	}
	
	private void createSemester(Semester semester, boolean save) {
		
		semesterList.add(semester);
		credits.put(semester, 0);
		SemesterWidget semesterW = new SemesterWidget(semesterList.indexOf(semester), this);
		semesterW.setCredits(0);
		semesterWidgetList.add(semesterW);
		semesterAndWidgetBiMap.put(semester, semesterW);
		semesters++;
		planWidget.addSemesterWidget(semesterW);
		
		addClickHandlerDeleteSemesterButton(semesterW);
		
		SemesterDropController dropController = new SemesterDropController(semesterW.getMainPanel(), semesterW, this);		
		dragController.registerDropController(dropController);
		semesterDropControllerList.add(dropController);
		controllersBySemester.put(semesterW, dropController);
		
		addClickHandlerAddSubject(semesterW);
		
		if(getCurrentSemesterValue() != null && semesterList.get(0).getSemesterValue() != null){	
			setSemesterValuesForSemesters(getPositionOfCurrentSemester(), save);
			setSemesterValuesForWidgets(getPositionOfCurrentSemester());
		}
		
		if(save == true){			
			planChanged("NewSemester");
		}

		siaSummaryView.setMaxSemesterInForm(plan.getSemesters().size());
		
	}
	
	public void planChanged(String triggered){
		GWT.log(triggered);
		savePlan();
	}

	private void createSubject(Subject subject, SubjectValue subjectValue, Semester semester) {
		
		//To control where and what subject is dropped
		increasingSubjectValuesCounter++;
		subjectValue.setSubjectValuesPublicId((subject.getCode() == null ? "" : subject.getCode()) + increasingSubjectValuesCounter);
		
		if(subjectValuesList.contains(subjectValue)==false) {
			subjectValuesList.add(subjectValue);
			if(subjectTimesUpdated.containsKey(subject) == false) subjectTimesUpdated.put(subject, 0);
		}
		
		if(semester.getSubjects().contains(subjectValue) == false) semester.addSubject(subjectValue);
		//TODO avoid the same subject twice in the same semester
		
		SubjectWidget subjectWidget = new SubjectWidget(subject.getName(), subject.getCode(), subject.getCredits(), subjectValue.getComplementaryValues().isMandatory(), subjectValue.getComplementaryValues().getTypology(), subjectValue.getSubjectValuesPublicId(), (subjectValue.getComplementaryValues().getSubjectGroup() != null ? subjectValue.getComplementaryValues().getSubjectGroup().getName() : ""), this);
		if(subjectValue.isTaken() == true){
			if(subjectValue.getComplementaryValues().getSubject().isApprovenType() == true){
				if(subjectValue.getGrade() >= 3){					
					subjectWidget.setGrade("AP");
				}else{
					subjectWidget.setGrade("NA");
				}
			}else{
				double gradeFixed = (double) Math.round(subjectValue.getGrade() * 10) / 10;
				String gradeString = NumberFormat.getFormat("0.0").format(gradeFixed);
				subjectWidget.setGrade(gradeString);
			}
		}
		String codeTitle = "Agrupación: "+  (subjectValue.getComplementaryValues().getSubjectGroup() != null ? subjectValue.getComplementaryValues().getSubjectGroup().getName() : "Unknown");
		subjectWidget.setCodeLabel(codeTitle);
		
		subjectWidgetList.add(subjectWidget);
		makeSubjectWidgetDraggable(subjectWidget);
		semesterAndWidgetBiMap.get(semester).addSubject(subjectWidget);
		subjectValuesAndWidgetBiMap.put(subjectValue, subjectWidget);
		subjectValuesAndSemesterMap.put(subjectValue, semester);
		
		updateCredits(subjectValue, semester, true);
		
		addClickHandlerSubjectWidget(subjectWidget);
		showToolTip();
		
	}

	public void moveArrows(String publicId){
		SubjectValue sV = getSubjectValuesByPublicId(publicId);
		if(sV != null){
			//if((subjectValuesSelected.equals(sV) == true || areRelated(sV, subjectValuesSelected) == true) || true)
			showConnections(subjectValuesSelected);
		}
	}

	private boolean areRelated(SubjectValue sV, SubjectValue sV2) {
		boolean toReturn = false;
		List<Subject> relatedSubjects = new ArrayList<Subject>();
		relatedSubjects.addAll(sV.getComplementaryValues().getListPrerequisites());
		relatedSubjects.addAll(sV.getComplementaryValues().getListCorequisites());
		relatedSubjects.addAll(sV.getComplementaryValues().getListPrerequisitesOf());
		relatedSubjects.addAll(sV.getComplementaryValues().getListCorequisitesOf());
		for(Subject s : relatedSubjects){
			if(s.equals(sV2.getComplementaryValues().getSubject())){
			//OLD if(s.equals(valuesAndSubjectMap.get(sV2))){
				toReturn = true;
				break;
			}
		}
		return toReturn;
	}

	private void showConnections(SubjectValue sV) {
		if(sV !=null){
			for(Subject s : sV.getComplementaryValues().getListPrerequisites()){
				for(SubjectValue sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sVTempo), subjectValuesAndWidgetBiMap.get(sV), "pre");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListCorequisites()){
				for(SubjectValue sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sVTempo), subjectValuesAndWidgetBiMap.get(sV), "co");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListPrerequisitesOf()){
				for(SubjectValue sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sV), subjectValuesAndWidgetBiMap.get(sVTempo), "pre");
				}
			}
			for(Subject s : sV.getComplementaryValues().getListCorequisitesOf()){
				for(SubjectValue sVTempo : getSubjectValuesBySubject(s)){
					connectionsController.addConnection(subjectValuesAndWidgetBiMap.get(sV), subjectValuesAndWidgetBiMap.get(sVTempo), "co");
				}
			}
		}
	}

	private void hideArrows() {
		connectionsController.hideArrows();
	}

	private void modifyOpacity(SubjectValue sV) {
		boolean isRelated = false;
		List<Subject> subjectRelatedList = new ArrayList<Subject>();
		ComplementaryValue cV = sV.getComplementaryValues();
		
		subjectRelatedList.addAll(cV.getListPrerequisites());
		subjectRelatedList.addAll(cV.getListCorequisites());
		subjectRelatedList.addAll(cV.getListPrerequisitesOf());
		subjectRelatedList.addAll(cV.getListCorequisitesOf());
		for(SubjectValue sVTemporary : subjectValuesList){
			isRelated = false;
			for(Subject sTemporary : subjectRelatedList){
				if(sTemporary.equals(sVTemporary.getComplementaryValues().getSubject())==true){
				//OLD if(sTemporary.equals(valuesAndSubjectMap.get(sVTemporary))==true){
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

	private void getComplementaryValues(SubjectValue sV) {
		//int timesUpdated = subjectTimesUpdated.get(valuesAndSubjectMap.get(sV));
		int timesUpdated = subjectTimesUpdated.get(sV.getComplementaryValues().getSubject());
		if(timesUpdated < LIMIT_TO_REQUISITES_UPDATES){
			//OLD subjectTimesUpdated.put(valuesAndSubjectMap.get(sV), timesUpdated+1);
			subjectTimesUpdated.put(sV.getComplementaryValues().getSubject(), timesUpdated+1);
			rpcService.getComplementaryValueFromMisPlanes(plan.getCareerCode(), sV.getComplementaryValues().getSubject().getCode(), new AsyncCallback<ComplementaryValue>(){
			//OLD rpcService.getComplementaryValues(plan.getCareerCode(), valuesAndSubjectMap.get(sV).getCode(), new AsyncCallback<ComplementaryValues>(){
				
				@Override
				public void onFailure(Throwable caught) {
					//Window.alert("sorry, we got an error while updating the requisites of a subject");
				}
	
				@Override
				public void onSuccess(ComplementaryValue result) {
					boolean isOldNull = false;
					if(result != null){
						List<SubjectValue> list = getSubjectValuesBySubject(result.getSubject());
						for(SubjectValue sV : list){
							if(sV.getComplementaryValues()== null || sV.getComplementaryValues().getListCorequisites().size() == 0 || sV.getComplementaryValues().getListPrerequisitesOf().size() == 0 || sV.getComplementaryValues().getListCorequisitesOf().size() == 0 || sV.getComplementaryValues().getListPrerequisites().size() == 0) isOldNull = true;//TODO: show arrows and opacity again
							sV.setComplementaryValues(result);
							//the main subject is pre requisite of some subject, and it is adding it to them
							for(Subject s : result.getListPrerequisites()){
								for(SubjectValue sVTemporary : getSubjectValuesBySubject(s)){
									sVTemporary.getComplementaryValues().addPrerequisiteOf(result.getSubject());
								}
							}
							//the main subject is co requisite of some subject, and it is adding it to them
							for(Subject s : result.getListCorequisites()){
								for(SubjectValue sVTemporary : getSubjectValuesBySubject(s)){
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
	
	private List<SubjectValue> getSubjectValuesBySubject(Subject s){
		List<SubjectValue> list = new ArrayList<SubjectValue>();
		for(SubjectValue sV : subjectValuesList){
			//if(valuesAndSubjectMap.get(sV).equals(s) == true){
			if(sV.getComplementaryValues().getSubject().equals(s) == true){
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
			Career careerSelected = (this.plan != null ? (this.plan.getCareer() != null ? this.plan.getCareer() : null) : null);
			searchSubjectView.setCareerList(careers, careerSelected);
		}
	}
	
	public void deleteAllOpacities() {
		for(SubjectWidget sW : subjectWidgetList){
			sW.getElement().setAttribute("style", "opacity:1");
		}
	}

	private void updateCredits(SubjectValue subjectValues2, Semester semester2, boolean toAdd) {
		
		//OLD if(valuesAndSubjectMap.containsKey(subjectValues2) == true){
		if(subjectValues2.getComplementaryValues() != null){
			if(subjectValues2.getComplementaryValues().getSubject() != null){
				//UNTIL HERE IS THE NEW CODE
				
				//OLD int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
				int creditsValue = subjectValues2.getComplementaryValues().getSubject().getCredits();
				
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
				switch(SomosUNUtils.getTypology(subjectValues2.getComplementaryValues().getTypology())){
				case TypologyCodes.NIVELACION:
					updateCreditsLeveling(subjectValues2, semester2, toAdd);
					break;
				case TypologyCodes.LIBRE_ELECCION:
					updateCreditsFreeElection(subjectValues2, semester2, toAdd);
					break;
				case TypologyCodes.PROFESIONAL:
					updateCreditsDisciplinary(subjectValues2, semester2, toAdd);
					break;
				case TypologyCodes.FUNDAMENTACION:
					updateCreditsFoundation(subjectValues2, semester2, toAdd);
				}
				updateSiaSummary();
				
			}
		}
	}

	private void updateCreditsFoundation(SubjectValue subjectValues2, Semester semester2, boolean toAdd) {
		//OLD int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		int creditsValue = subjectValues2.getComplementaryValues().getSubject().getCredits();
		if(toAdd== true){
			foundationCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) foundationCredits[1] += creditsValue;
		}else{
			foundationCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) foundationCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsDisciplinary(SubjectValue subjectValues2,	Semester semester2, boolean toAdd) {
		int creditsValue = subjectValues2.getComplementaryValues().getSubject().getCredits();
		if(toAdd== true){
			disciplinaryCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) disciplinaryCredits[1] += creditsValue;
		}else{
			disciplinaryCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) disciplinaryCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsFreeElection(SubjectValue subjectValues2, Semester semester2, boolean toAdd) {
		int creditsValue = subjectValues2.getComplementaryValues().getSubject().getCredits();
		if(toAdd== true){
			freeElectionCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) freeElectionCredits[1] += creditsValue;
		}else{
			freeElectionCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) freeElectionCredits[1] -= creditsValue;
		}
	}

	private void updateCreditsLeveling(SubjectValue subjectValues2, Semester semester2, boolean toAdd) {
		//OLD int creditsValue = valuesAndSubjectMap.get(subjectValues2).getCredits();
		int creditsValue = subjectValues2.getComplementaryValues().getSubject().getCredits();
		if(toAdd== true){
			levelingCredits[0]+= creditsValue;
			if(subjectValues2.getGrade()>=3) levelingCredits[1] += creditsValue;
		}else{
			levelingCredits[0]-= creditsValue;
			if(subjectValues2.getGrade()>=3) levelingCredits[1] -= creditsValue;
		}
		
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
		updateSemesterValues();
		
		semesters--;
		if(semesters != semesterList.size()){
			GWT.log("The number of semesters do not match");
		}
		
		planChanged("SemesterDelete");
		
		siaSummaryView.setMaxSemesterInForm(plan.getSemesters().size());
		
	}

	/**
	 * Will adjust every semesterValue to the current SemesterValue, if there is no semester with the currentSemesterValue values, then deletes all the semesterValue
	 */
	private void updateSemesterValues() {
		//Get current semester
		if(getCurrentSemesterValue() != null){
			if(semesterList.size() > 0 && semesterList.get(0).getSemesterValue() != null){
				//Be careful, the semester deleted could be the current semester
				Integer position = null;
				for(int pos = 0; pos < semesterList.size(); pos ++){
					Semester semester = semesterList.get(pos);
					if(semester.getSemesterValue().equals(getCurrentSemesterValue()) == true){
						position = pos;
						break;
					}
				}
				
				if(position != null){
					for(int pos = 0; pos < semesterList.size(); pos++){
						Semester semester = semesterList.get(pos);
						if(pos == position){
							semester.setSemesterValue(getCurrentSemesterValue());
						}else{							
							semester.setSemesterValue(new SemesterValue(getCurrentSemesterValue(), pos-position));
						}
					}
				}else{
					for(int pos = 0; pos < semesterList.size(); pos++){
						Semester semester = semesterList.get(pos);
						semester.setSemesterValue(null);
					}
				}
			}
		}
		
	}

	private void savePlan() {
		if(plan.getName() != null && plan.getName().isEmpty() == false){
			rpcService.savePlan(student, plan, new AsyncCallback<Long>(){

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Error saving the plan - PlanPresenter");
				}

				@Override
				public void onSuccess(Long result) {
					if(plan.getId() == null){
						plan.setId(result);
					}
					GWT.log("Plan saved - PP");
				}
				
			});
		}else if(changeNameAsked == false){
			changeNameAsked = true;
			showSavePlanPopup();
		}
	}

	/**
	 * this method will be called to update the number of the semesters and to updated its number in the attribute of the widget
	 */
	private void updateSemestersNumber() {
		for(Semester s : semesterList){
			semesterAndWidgetBiMap.get(s).setSemester(semesterList.indexOf(s));
		}
	}

	private void deleteSubject(SubjectValue subjectValues) {
		
		//OLD Subject subject = valuesAndSubjectMap.get(subjectValues);
		Subject subject = subjectValues.getComplementaryValues().getSubject();
		SubjectWidget subjectW = subjectValuesAndWidgetBiMap.get(subjectValues);
		Semester semester = subjectValuesAndSemesterMap.get(subjectValues);
		
		updateCredits(subjectValues, semester, false);
		
		if(semester.getSubjects().contains(subjectValues) == true) semester.deleteSubject(subjectValues);
		//semesterAndWidgetBiMap.get(semester2).addSubject(subjectWidget); the next line replaces this one
		subjectValuesAndWidgetBiMap.get(subjectValues).removeFromParent();
		//makeSubjectWidgetDraggable(subjectWidget); sobra
		subjectWidgetList.remove(subjectValuesAndWidgetBiMap.get(subjectValues));
		
		//OLD valuesAndSubjectMap.remove(subjectValues);
		//NEW is empty
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
		SubjectValue subjectValues = getSubjectValuesByPublicId(publicId);
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
	private SubjectValue getSubjectValuesByPublicId(String publicId){
		SubjectValue subjectValuesToReturn = null;
		if(subjectValuesList.isEmpty() == false){
			for(SubjectValue subjectValues : subjectValuesList){
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
	
	private void updateDefaultCredits() {
		siaSummaryView.setDefaultFoundationCredits("" + foundationCredits[2]);
		siaSummaryView.setDefaultFreeElectionCredits("" + freeElectionCredits[2]);
		siaSummaryView.setDefaultDisciplinaryCredits("" + disciplinaryCredits[2]);
		siaSummaryView.setDefaultLevelingCredits("" + levelingCredits[2]);
		siaSummaryView.setTotalNecessary("" + (foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]));
	}

	private void updateApprovedCredits() {
		siaSummaryView.setApprovedFoundationCredits("" + foundationCredits[1]);
		siaSummaryView.setApprovedFreeElectionCredits("" + freeElectionCredits[1]);
		siaSummaryView.setApprovedDisciplinaryCredits("" + disciplinaryCredits[1]);
		siaSummaryView.setApprovedLevelingCredits("" + levelingCredits[1]);
		siaSummaryView.setTotalApproved("" + (foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1]));
	}

	private void updatePercentageCredits() {
		double percentageDisciplinaryDouble = (double) disciplinaryCredits[1]/disciplinaryCredits[2]*100;
		double percentageFoundationDouble = (double) foundationCredits[1]/foundationCredits[2]*100;
		double percentageFreeDouble = (double) freeElectionCredits[1]/freeElectionCredits[2]*100;
		double percentageLevelingDouble = (double) levelingCredits[1]/levelingCredits[2]*100;
		
		if(percentageDisciplinaryDouble > 100) percentageDisciplinaryDouble = 100;
		if(percentageFoundationDouble > 100) percentageFoundationDouble = 100;
		if(percentageFreeDouble > 100) percentageFreeDouble = 100;
		if(percentageLevelingDouble > 100) percentageLevelingDouble = 100;
		
		siaSummaryView.setPercentageDisciplinaryCredits(SomosUNUtils.getOneDecimalPointString(percentageDisciplinaryDouble) + "%");
		siaSummaryView.setPercentageFoundationCredits(SomosUNUtils.getOneDecimalPointString(percentageFoundationDouble) + "%");
		siaSummaryView.setPercentageFreeElectionCredits(SomosUNUtils.getOneDecimalPointString(percentageFreeDouble) + "%");
		siaSummaryView.setPercentageLevelingCredits(SomosUNUtils.getOneDecimalPointString(percentageLevelingDouble) + "%");
		
		if(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]!=0){
			double totalPercentageDouble = (double) ((double) (foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1])/(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]))*100;
			double avanceDouble = (double) ((double)(foundationCredits[1]+freeElectionCredits[1]+disciplinaryCredits[1]+levelingCredits[1])/(foundationCredits[2]+freeElectionCredits[2]+disciplinaryCredits[2]+levelingCredits[2]))*100;
			
			if(totalPercentageDouble > 100) totalPercentageDouble = 100;
			if(avanceDouble > 100) avanceDouble = 100;
			
			String totalPercentageString = SomosUNUtils.getOneDecimalPointString(totalPercentageDouble);
			String avanceString = SomosUNUtils.getOneDecimalPointString(avanceDouble);
			
			siaSummaryView.setTotalPerCent(totalPercentageString + "%");
			siaSummaryView.setAvance(avanceString + "%");
		} else {
			siaSummaryView.setTotalPerCent("0%");
			siaSummaryView.setAvance("0%");
		}
	}

	private void updateSmallSummary() {
		//updateAllCredits(); FIXME uncomment when method ready
		plan.calculateGpa();
		siaSummaryView.setGPA(SomosUNUtils.getOneDecimalPointString(plan.getGpa()));
		siaSummaryView.setApprovedCredits("" + totalCredits[1]);
		siaSummaryView.setAdditionalyCredits("" + getAdditionalCredits());
	}
	
	/**
	 * This method will update based on the plan and its subjectGroups the four arrays of credits
	 */
	private void updateAllCredits() {
		/*
		 *  [0] Total
		 *  [1] Approved
		 *  [2]Necessary to graduate,the [2] item is a default and it is a final value set a the beginning, 
		 *  [3] approved and needed, i.e. in my case I have aproved more than needed and some are not obligatory, so those don't count.
		 */
		
		//reset the array
		Arrays.fill(foundationCredits, 0);
		Arrays.fill(freeElectionCredits, 0);
		Arrays.fill(disciplinaryCredits, 0);
		Arrays.fill(levelingCredits, 0);
		
		//Setting the default credits
		foundationCredits[2] = plan.getCareer().getFoudationCredits();
		freeElectionCredits[2] = plan.getCareer().getFreeElectionCredits();
		disciplinaryCredits[2] = plan.getCareer().getDisciplinaryCredits();
		levelingCredits[2] = 0; //Don't know yet if this number is greater
		
		int foundationOptative = 0;
		int freeElectionOptative = 0;
		int disciplinaryOptative = 0;
		int levelingOptative = 0;
		
		int[] array = null;
		int optative = 0;
		for(Semester semester : plan.getSemesters()){
			for(SubjectValue subjectValue : semester.getSubjects()){
				String typology = subjectValue.getComplementaryValues().getTypology();
				
				//Assigning the right array to @param array
				switch(typology){
					case TypologyCodes.FUNDAMENTACION:
						optative = foundationOptative;
						array = foundationCredits;
						break;
					case TypologyCodes.LIBRE_ELECCION:
						optative = freeElectionOptative;
						array = freeElectionCredits;
						break;
					case TypologyCodes.NIVELACION:
						optative = levelingOptative;
						array = levelingCredits;
						break;
					case TypologyCodes.PROFESIONAL:
						optative = disciplinaryOptative;
						array = disciplinaryCredits;
						break;
				}
				
				int credits = subjectValue.getComplementaryValues().getSubject().getCredits();
				
				//Add it to the total 
				array[0] += credits;
				
				if(subjectValue.isTaken() && subjectValue.getGrade() >= 3){
					array[1] += credits;
				}
				if(subjectValue.isTaken() && subjectValue.getGrade() >= 3){						
					if(subjectValue.getComplementaryValues().isMandatory()){
						array[3] += credits;
					}else{
						optative += credits;
					}
				}
				
				//TODO finish this method
				
			}
		}
	}

	private int getAdditionalCredits() {
		int toReturn = 0;
		int toRemove = 0;
		
		for(Semester semester : plan.getSemesters()){
			for(SubjectValue subjectValue : semester.getSubjects()){
				if(subjectValue.isTaken()){
					if(subjectValue.getGrade() >=3){
						toReturn += subjectValue.getComplementaryValues().getSubject().getCredits();
					}else{
						toRemove += subjectValue.getComplementaryValues().getSubject().getCredits();
					}
				}
			}
		}
		toReturn *=2;
		if(toReturn > 80) toReturn = 80;
		
		toReturn -=toRemove;
		
		return toReturn;
	}

	public void addLine(LineWidget l){
		subContainer.add(l);
	}

	private void addClickHandlersToPlan(){
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
	
	private void addClickHandlerAddSubject(final SemesterWidget semesterW){
		semesterW.getAddButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				onClickAddSubject(semesterW.getAddButton().getElement().getAttribute("semester"));
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
				Widget w = (Widget) event.getSource();
				int semester = Integer.valueOf(w.getElement().getAttribute("semester"));
				onDeleteSemesterClicked(semester);
			}
			});
	}

	private void confirmDeleteSubject(SubjectValue subjectValuesToDelete) {
		//OLD Subject subjectToDelete = valuesAndSubjectMap.get(subjectValuesToDelete);
		Subject subjectToDelete = subjectValuesToDelete.getComplementaryValues().getSubject();
		warningDeleteSubjectView.setSubject(subjectValuesToDelete, subjectToDelete);
		warningDeleteSubjectView.showIt();
	}

	public void confirmedDeleteSubject(SubjectValue sV) {
		deleteSubject(sV);
		planChanged("SubjectDelete");
		warningDeleteSubjectView.hideIt();
	}

	public SubjectValue getSubjectValuesSelected() {
		return subjectValuesSelected;
	}

	public void setSubjectValuesSelected(SubjectValue subjectValuesSelected) {
		this.subjectValuesSelected = subjectValuesSelected;
	}

	public void addIcon(Icon i) {
		subContainer.add(i);
	}	
	
	protected void loadSubjectsToSearchView(SiaResultSubjects result, String careerCode, String searchText, String type) {
		
		addSubjectsToSearchView(result.getSubjectList(), careerCode);
		createPagination(result, searchText, type, careerCode);
		showToolTip();
				
	}

	private void addSubjectsToSearchView(List<Subject> subjectList, String careerCode) {
		
		searchSubjectView.clearAccordionContainer();
		accordions.clear();
		
		for(Subject s : subjectList){
			SubjectAccordionViewImpl accordion = new SubjectAccordionViewImpl(searchSubjectView.getSubjectsAmmount());
			accordion.setPresenter(this);
			accordion.setHeader(s.getCode(), s.getName(), "L", Integer.toString(s.getCredits()), careerCode);

			ComplementaryValueViewImpl complementaryValueView = new ComplementaryValueViewImpl(s.getName(), s.getCode(), accordion, searchSubjectView.getSubjectsAmmount(), 0);
			complementaryValueView.setPresenter(this);
			complementaryValueView.setSubjectGroupName("-");
			
			//mapAccordion.put(accordion, complementaryValueView);
			accordion.addComplementaryView(complementaryValueView);
			
			accordions.add(accordion);
			
			SelectedSubjectViewImpl selected = getSelectedSubjectView(s.getCode());
			if(selected != null){
				accordion.changeState();
			}
			
			searchSubjectView.addSubject(accordion);
		}
		
		avoidAccordionPropagation();
		
	}

	private void createPagination(SiaResultSubjects result, final String searchText, final String type, final String careerCode) {
		searchSubjectView.clearPagination();
		
		
		int startFrom = result.getPage();
		if(startFrom + 5 > result.getNumPaginas()){
			startFrom = result.getNumPaginas()-5;
		}
		if(startFrom>6){
			startFrom -=5;
		}else{
			startFrom = 0;
		}
		
		int until = result.getNumPaginas()-startFrom;
		if(until > 10) until = 10;
		
		if(startFrom != 0){
			

			AnchorListItem a = new AnchorListItem("1");
			searchSubjectView.addPage(a);
			
			a.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					onSearchButtonClicked(searchText, careerCode, type, 1);
				}
				
			});
			
			//Show ... and the first one
			Label label = new Label("...");
			ListItem item = new ListItem();
			item.add(label);
			label.removeStyleName("label");
			searchSubjectView.addPage(item);
		}
		
		//OLD for(int x = 0; x < result.getNumPaginas(); x++){
		for(int x = 0; x < until; x++){
			final int page = startFrom + x + 1;
			AnchorListItem a = new AnchorListItem("" + page);
			
			a.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					onSearchButtonClicked(searchText, careerCode, type, page);
				}
				
			});
			
			if(startFrom + x + 1 == result.getPage()) a.setActive(true);
			else a.setActive(false); 
			
			a.setEnabled(true);
			searchSubjectView.addPage(a);
		}
		
		if(result.getNumPaginas() > (startFrom+10)){
			//Show ... and the last one
			final int lastPage = result.getNumPaginas();

			if(result.getPage()+6 != result.getNumPaginas()){				
				Label label = new Label("...");
				ListItem item = new ListItem();
				item.add(label);
				label.removeStyleName("label");
				searchSubjectView.addPage(item);
			}
			
			AnchorListItem a = new AnchorListItem("" + lastPage);
			searchSubjectView.addPage(a);
			
			a.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					onSearchButtonClicked(searchText, careerCode, type, lastPage);
				}
				
			});
		}
		
	}

	/**
	 * This method wil get the complementaryValues for the subjects and subjectsValues and add it to them.
	 * 
	 * @param listCV
	 */
	private void setComplementaryValues(List<ComplementaryValue> listCV) {
		//TODO get the complementaryValue to that subject
		ComplementaryValue cV = null;
	}
	
	public void deleteAdminButtons() {
		siaSummaryView.deleteAdminButtons();
	}
	
	private SubjectAccordionViewImpl getAccordion(String code){
		
		SubjectAccordionViewImpl accordion = null;
		
		if(accordions != null){
			if(accordions.size() != 0){
				for(SubjectAccordionViewImpl aT : accordions){
					if(aT.getCode().equals(code) == true){
						accordion = aT;
						break;
					}
				}
			}
		}
		
		return accordion;
	}
	
	private void changeSubheadersState(String subjectCode){
		
		if(accordions != null){
			if(accordions.size() != 0){
				for(SubjectAccordionViewImpl aT : accordions){
					if(aT.getCodeFromSubheader().equals(subjectCode) == true){
						aT.changeSubheaderState();
					}
				}
			}
		}
	}

	private SelectedSubjectViewImpl getSelectedSubjectView(String code) {
		
		SelectedSubjectViewImpl selected = null;
		
		if(accordions != null){
			if(accordions.size() != 0){
				for(SelectedSubjectViewImpl sT : selectedSubjects){
					if(sT.getCode().equals(code) == true){
						selected = sT;
						break;
					}
				}
			}
		}
		
		return selected;
	}
		
	private void addSubjectsToPlan(List<ComplementaryValue> result, String semesterString) {
		addSubjectsToPlan(result, semesterString, true);
	}
	
	private void addSubjectsToPlan(List<ComplementaryValue> result, String semesterString, boolean toSave) {
		
		int semesterNumber = Integer.valueOf(semesterString);
		Semester semester = semesterList.get(semesterNumber);
		
		/**
		 * for each
		 * 1. Create a subjectValues obj
		 * 2. Add the cV to the sV
		 * 3. call createSUbject(subject, CV, semester) 
		 */
		for(ComplementaryValue cVT : result){
			SubjectValue sV = new SubjectValue(0.0 , false, cVT);
			createSubject(sV.getComplementaryValues().getSubject(), sV, semester);
		}
		
		if(toSave == true){			
			planChanged("NewSubjects");
		}

	}
	
	private void showDefaultSubjectCreationView(String s){
		//TODO
		defaultSubjectCreationView.clear();
		defaultSubjectCreationView.showIt();
		defaultSubjectCreationView.setSemester(s);
		
		rpcService.getSubjectGroups(plan.getCareerCode(), new AsyncCallback<List<SubjectGroup>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry, an error ocurred, take a picture of you with this error and send it to @MikeWoodcockC");
			}

			@Override
			public void onSuccess(List<SubjectGroup> result) {
				defaultSubjectCreationView.addEntriesToList(result);
			}
			
		});
	}
	
	private void loadMandatoryValues(List<ComplementaryValue> list){
		
		if(plan != null && list != null){
			for(int x = 0; x < 10; x++){
				if(list.isEmpty() == false){					
					List<ComplementaryValue> cVToAdd = new ArrayList<ComplementaryValue>(); 
					for(int y = 0; y < 5; y++){
						if(list.isEmpty() == false){						
							cVToAdd.add(list.get(0));
							list.remove(0);
						}else{
							break;
						}
					}
					boolean toSave = false;
					if(list.size() == 0 || x == 9){
						toSave = true;
					}
					if(cVToAdd.isEmpty() == false) addSubjectsToPlan(cVToAdd, Integer.toString(x), toSave);
				}else{
					break;
				}
			}
		}
		
	}
	
	public void deletePlanConfirmed(){
		view.hidePopups();
		if(plan.getId() != null){
			String planId = plan.getId().toString();
			rpcService.deletePlanFromUser(planId, new AsyncCallback(){
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("error deleting");
				}
				
				@Override
				public void onSuccess(Object result) {
					setPlan(null); //important... because when it returns to the create and if the last token was plan it automatically saves the plan, but if the plan is null then it does not save anything
					Window.alert("Plan deleted");
					Window.Location.assign("#create");
				}
			});
		}else{
			Window.Location.assign("#create");
		}
	}

	public void deleteSemesterConfirmed(int semester){
		view.hidePopups();
		deleteSemester(semester-1);
	}
	
	private void populateAccordionWithComplementaryValue(SubjectAccordionViewImpl accordion, ComplementaryValueViewImpl cVView, ComplementaryValue complementaryValue) {
		//SubjectGroup, pre-co requisites (Of) TODO
		
		if(complementaryValue != null && complementaryValue.getSubject() != null){
			
			String subjectGroupName = null;
			if(complementaryValue != null && complementaryValue.getSubjectGroup() != null){			
				subjectGroupName =complementaryValue.getSubjectGroup().getName();
			}else{
				subjectGroupName = "Desconocido";
			}
			cVView.setSubjectGroupName(subjectGroupName);
			
			if(complementaryValue.getSubject().isDummy()){ //|| complementaryValue.getSubject().isDefault() || complementaryValue.getSubject().isDummy()){
				cVView.showUnavailableWarning();
			}
			
			
			if(complementaryValue != null && complementaryValue.getPrerequisitesLists() != null && complementaryValue.getPrerequisitesLists().size() > 0){
				for(List<Subject> list: complementaryValue.getPrerequisitesLists()){
					for(Subject subject : list){
						boolean makeStatic = false;
						boolean makeUnavailable = false;
						if(subject.isDummy()) makeUnavailable = true;
						if(subject.isSpecial() || subject.isDefault()) makeStatic = true;
						cVView.addRequisite("pre", subject.getName(), subject.getCode(), complementaryValue.getCareer().getCode(), accordion, makeStatic, makeUnavailable);
					}
				}
			}else{
				cVView.hasNoPrerequisites();
			}
			
			if(complementaryValue != null && complementaryValue.getCorequisitesLists() != null && complementaryValue.getCorequisitesLists().size() > 0){
				for(List<Subject> list: complementaryValue.getCorequisitesLists()){
					for(Subject subject : list){
						boolean makeStatic = false;
						boolean makeUnavailable = false;
						if(subject.isDummy()) makeUnavailable = true;
						if(subject.isSpecial()|| subject.isDefault()) makeStatic = true;
						cVView.addRequisite("co", subject.getName(), subject.getCode(), complementaryValue.getCareer().getCode(), accordion, makeStatic, makeUnavailable);
					}
				}
			}else{
				cVView.hasNoCorequisites();
			}
			
			if(complementaryValue != null && complementaryValue.getListCorequisitesOf() != null && complementaryValue.getListCorequisitesOf().size() > 0){
				for(Subject subject : complementaryValue.getListCorequisitesOf()){
					boolean makeStatic = false;
					if(subject.isDummy() || subject.isSpecial() || subject.isDefault()) makeStatic = true;
					cVView.addAntiRequisite("co", subject.getName(), subject.getCode(), complementaryValue.getCareer().getCode(), accordion, makeStatic);
				}
			}else{
				cVView.isNoCorequisite();
			}
			
			if(complementaryValue != null && complementaryValue.getListPrerequisitesOf() != null && complementaryValue.getListPrerequisitesOf().size() > 0){
				for(Subject subject : complementaryValue.getListPrerequisitesOf()){
					boolean makeStatic = false;
					if(subject.isDummy() || subject.isSpecial() || subject.isDefault()) makeStatic = true;
					cVView.addAntiRequisite("pre", subject.getName(), subject.getCode(), complementaryValue.getCareer().getCode(), accordion, makeStatic);
				}
			}else{
				cVView.isNoPrerequisite();
			}
		}else{
//			cVView.showErrorWarning();
			cVView.hasNoPrerequisites();
			cVView.hasNoCorequisites();
			cVView.isNoCorequisite();
			cVView.isNoPrerequisite();
		}
		
	}
	
	private void populateAccordionWithGroups(ComplementaryValueViewImpl view, SiaResultGroups result) {
		
		if(result != null && result.getGroups() != null && result.getGroups().size() > 0){
			for(Group group : result.getGroups()){
				String teacherName = group.getTeacher().getName();
				if(teacherName.trim().isEmpty()) teacherName = "Unknown";
				String[] daysTime = {"","","","","","",""};
				String[] classRoom = {"","","","","","",""};
				
				for(Block block : group.getSchedule()){
					daysTime[block.getDay()] = daysTime[block.getDay()].concat(block.getStartHour() + "-" + block.getEndHour() + " ");
					classRoom[block.getDay()] = classRoom[block.getDay()].concat(block.getClassRoom() + " ");
				}
				
				view.addGroup("" + group.getGroupNumber(), teacherName, "-", "-", "-", "" + group.getFreePlaces(), "" + group.getTotalPlaces(), daysTime[0], classRoom[0], daysTime[1], classRoom[1], daysTime[2], classRoom[2], daysTime[3], classRoom[3], daysTime[4], classRoom[4], daysTime[5], classRoom[5], daysTime[6], classRoom[6]);
				GWT.log(group.getFreePlaces() + " " + group.getTotalPlaces());
			}
		}else{
			view.hasNoGroups();
		}
		
		showToolTip();
		
	}
	
	/**
	 * This method will work if there currentSemesterValue is not empty and if the first semester has not a semesterValue null;
	 */
	public void setPlanCurrentSemester(){
		if(getCurrentSemesterValue() != null && semesterList != null && semesterList.get(0) != null && semesterList.get(0).getSemesterValue() != null){
			setPlanCurrentSemester(getPositionOfCurrentSemester());
		}
	}
	
	private Integer getPositionOfCurrentSemester(){
		Integer position = null;
		if(getCurrentSemesterValue() != null){
			position = 0;
			for(Semester s : semesterList){
				if(s.getSemesterValue().equals(getCurrentSemesterValue()) == true){
					break;
				}
				position++;
			}
		}
		return position;
	}
	
	/**
	 * This method will add to every semester its corresponding semesterValue
	 * @param currentSemesterNumber
	 */
	public void setPlanCurrentSemester(int currentSemesterNumber){
		if(semesters >= currentSemesterNumber && currentSemesterNumber >= 0){
			setSemesterValuesForWidgets(currentSemesterNumber);
			setSemesterValuesForSemesters(currentSemesterNumber, true);	
		}
	}
	
	/**
	 * This method takes care of adding the semesterValue attribute to the Semesters objects
	 * @param currentSemesterNumber
	 */
	private void setSemesterValuesForSemesters(final int currentSemesterNumber, final boolean save){
		
		if(getCurrentSemesterValue() == null){
			rpcService.getCurrentSemesterValue(new AsyncCallback<SemesterValue>(){

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Error getting the current semesterValue");
				}

				@Override
				public void onSuccess(SemesterValue result) {
					setCurrentSemesterValue(result);
					if(result != null){						
						setSemesterValuesForSemesters(currentSemesterNumber, save);
					}
				}
				
			});
		}else{
			
			if(getCurrentSemesterValue() != null){
				for(int pos = 0; pos < semesters; pos++){
					Semester semester = semesterList.get(pos);
					if(pos == currentSemesterNumber) {
						semester.setSemesterValue(getCurrentSemesterValue());
					}else {
						SemesterValue notCurrentSemesterValue = new SemesterValue(getCurrentSemesterValue(), pos - (currentSemesterNumber));
						semester.setSemesterValue(notCurrentSemesterValue);
					}
				}
				if(save){					
					planChanged("SemesterValues updated");
				}
			}
			
		}
		
	}
	
	/**
	 * This method takes care of adding/removing the style "current" to the semesters widgets
	 * @param currentSemesterNumber
	 */
	private void setSemesterValuesForWidgets(int currentSemesterNumber){
		if(currentSemesterNumber < 0){ 
			currentSemesterNumber = currentSemesterNumber*(-1);
		}
		for(int pos = 0; pos < semesters; pos++){
			Semester semester = semesterList.get(pos);
			SemesterWidget semesterWidget = semesterAndWidgetBiMap.get(semester);
			SemesterDropController s = controllersBySemester.get(semesterWidget);
			if(pos == currentSemesterNumber) {
				semesterWidget.setAsCurrent();
			}else {
				semesterWidget.unsetAsCurrent();
			}
		}
		selectCurrentSemesterPanel();
		
	}
	
	public SemesterValue getCurrentSemesterValue() {
		return currentSemesterValue;
	}
	
	/**
	 * This method set the currentSemesterValue and if the first semester has a semesterValue updates all the semesters
	 * @param semesterValue
	 */
	public void setCurrentSemesterValue(SemesterValue currentSemesterValue) {
		if(currentSemesterValue != null) {
			this.currentSemesterValue = currentSemesterValue;
			if(siaSummaryView != null){
				siaSummaryView.setLabelCurrentSemesterLabel(currentSemesterValue.toString());
			}
			if(semesterList != null && semesterList.get(0) != null && semesterList.get(0).getSemesterValue() != null){
				setPlanCurrentSemester();
			}
		}
	}
	
	/**
	 * This Method will complete the semester, if no current semester is provided it will use the first empty semester following the last non-empty semester or
	 * will create one if there is no empty semesters, if there is a current semester, the following semester will be used to add the subjects.
	 * <br/>
	 * Semesters will be created if it is necessary, each semester will have at most 6 subjects,  
	 */
	public void completePlan(){
		
		/**
		 * 1. Get the info to work with
		 * 
		 * 1.A There is a plan default
		 * 1.A.1 Get default career
		 * 1.A.2 Get SubjectGroups
		 * 
		 * 1.B There is not planDefault
		 * 1.B.1 Get mandatory subjects
		 * 1.B.2 Get SubjectGroups
		 * 
		 * 
		 * 2. Figure out in which case are we in (Select_Semester)
		 * 2.A There is currentSemester, therefore subjects will be added in the next semester (currentSemester + 1semester)
		 * 2.B There is no currentSemester, therefore subjects will be added in the first semester after the last non-empty semester
		 * 
		 * 
		 * 3. Start adding the subjects
		 * 3.1 Add mandatory subjects in the order of the default plan if exits
		 * 3.1.1 Check for requisites
		 * 3.2 Add default subjects(left <- if there was a default plan) [optativas]
		 * 3.2.1 Add default subjects in the closest semester to the Select_Semester which has less than 6 subjects until a maximum of 6 subjects per semester
		 */
		
		if(plan.getCareer() != null && (plan.getCareer().hasAnalysis() || plan.getCareer().hasDefault())){
			/************ 1. Getting the info ************/
			GWT.log("--Getting the completePlanInfo");
			rpcService.getCompletePlanInfo(plan.getCareerCode(), new AsyncCallback<CompletePlanInfo>(){

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Error getting the completePlanInfo");
					Window.alert("Sorry, something happening while we were retrieving the infomation");
				}

				@Override
				public void onSuccess(CompletePlanInfo result) {
					GWT.log("--Successful retreaving completePlanInfo");
					completePlan_2(result);
				}
				
			});
		}
	}
	
	/**
	 * 2. Figure out in which case are we in (selectedSemester)
	 * 2.A There is currentSemester, therefore subjects will be added in the next semester (currentSemester + 1semester)
	 * 2.B There is no currentSemester, therefore subjects will be added in the first semester after the last non-empty semester
	 * 
	 * 
	 * 3. Start adding the subjects
	 * 3.1 Add mandatory subjects in the order of the default plan if exits
	 * 3.1.1 Check for requisites
	 * 3.2 Add default subjects(left <- if there was a default plan) [optativas]
	 * 3.2.1 Add default subjects in the closest semester to the Select_Semester which has less than 6 subjects until a maximum of 6 subjects per semester
	 * 
	 * @param completePlanInfo
	 */
	private void completePlan_2(final CompletePlanInfo completePlanInfo) {
		/****** 2. Figure out in which case are we in ******/
		
		GWT.log("--In completePlan_2");
	
		if(completePlanInfo.getCareer() != null){
			if(semesterList.get(0).getSemesterValue() != null){
				/****** 2.A There is current Semester ******/
				GWT.log("--There is currentSemester");
				if(currentSemesterValue != null){
					completePlan_3(getPositionOfCurrentSemester()+1, completePlanInfo);
				}else{
					//get Current semester, once finished with it run competePlan_2A
					rpcService.getCurrentSemesterValue(new AsyncCallback<SemesterValue>(){

						@Override
						public void onFailure(Throwable caught) {
							GWT.log("Error getting the current semesterValue");
						}

						@Override
						public void onSuccess(SemesterValue result) {
							if(result != null){						
								setCurrentSemesterValue(result);
								setSemesterValuesForSemesters(getPositionOfCurrentSemester(), false);
								completePlan_3(getPositionOfCurrentSemester()+1, completePlanInfo);
							}
						}
						
					});
				}
				/*******************************************/
			}else{
				/****** 2.B There is no current Semester ******/
				
				//get the closest empty semester to the last non-empty semester
				int firstEmptySemesterPosition = semesterList.size();
				for(int semesterPosition = semesterList.size()-1; semesterPosition >=0; semesterPosition--){
					Semester semester = semesterList.get(semesterPosition);
					if(semester.getSubjects().size() == 0){
						firstEmptySemesterPosition = semesterPosition;
						break;
					}
				}
				//Create semesters if none is empty
				if(firstEmptySemesterPosition == semesterList.size()){
					createSemester(new Semester());
				}
				//use completPlan_3(, result);
				completePlan_3(firstEmptySemesterPosition, completePlanInfo);
				
				/**********************************************/
			}
		}
		/***************************************************/
	}
	
	/**
	 * 3. Start adding the subjects
	 * 
	 * BULLSHIT!!!! TAKE INTO ACCOUNT THE CO-REQUISITES
	 * 
	 * 3.1.A: There is not DefautlPlan
	 * 3.1.A.1 Generate the simultaneous equations
	 * 3.1.A.1.1 Create the singles equations
	 * 3.1.A.1.1.1 Create the variables and add them to a map [ComplementaryValue, variable]
	 * 3.1.A.1.1.1.1 Take the subjects already in the plan as given (constants and no variables) and set them into the map (min semester will be the current semester)
	 * 3.1.A.1.1.1.2 If not given then create the equation, if there is no such complementaryValue download it
	 * 3.1.A.2 Solve the simultaneous equations 
	 * 3.1.A.3 Add the subjects to the plan
	 * 3.1.A.3.1 If the subject is already in the plan take it as given 
	 * 
	 * 3.1.B: There is DefaultPlan
	 * 3.1.A.1 Delete the complementaryValues already in the plan from the DefaultPlan
	 * 3.1.A.2 Get the Simultaneous equations and solve them 
	 * 
	 * 3.3 Add default subjects (subject left <- if there was a default plan) [optativas]
	 * 3.3.1 Add default subjects in the closest semester to the Select_Semester which has less than 6 subjects
	 * 
	 * @param positionSemesterToStart
	 * @param completePlanInfo
	 */
	private void completePlan_3(int positionSemesterToStart, CompletePlanInfo completePlanInfo) {
		
		if((completePlanInfo.getPlanDefautl() != null || completePlanInfo.getMandatoryComplementaryValues() != null) && completePlanInfo.getSubjectGroups() != null){
			
			//TODO calcular el semestre desde donde comenzar
			int currentSemesterNumber = getCurrentSemesterNumber() + 1;
			
			if(completePlanInfo.getPlanDefautl() != null){				
				GWT.log("Default plan has found");
			}else if(completePlanInfo.getMandatoryComplementaryValues() != null){
				GWT.log("No default plan found");
				/************ Create the simultaneous equations ************/
				/***************** 3.1.A.1.1.1 ******************/

				Map<Subject, Integer> variables = new HashMap<Subject, Integer>();
				//Copy the list in order to delete the ones done
				List<ComplementaryValue> mandatoryComplementaryValues_copy = new ArrayList<ComplementaryValue>(completePlanInfo.getMandatoryComplementaryValues());
				
				//Change this to an ITERABLE
				int position = 0;
				boolean noneAdded = true;
				boolean specialLap = false; //this is when a cV has no 
				while(mandatoryComplementaryValues_copy.size() > 0){
					if(position >= mandatoryComplementaryValues_copy.size()) {
						position = 0;
						//In order to avoid infinite loops
						if(noneAdded == true && specialLap == true) break;
						if(noneAdded == true) specialLap = true;
						noneAdded = true;
					}
					ComplementaryValue cV = mandatoryComplementaryValues_copy.get(position);
					//Search if it has already a variable
					int x = getVariableForSimultaneousEquationsMap(variables, cV.getSubject().getCode());
					if(x == -1){
						//search if it is already in the plan, and make it a constant
						int lastSemester = getLastSemesterForASubject(cV.getSubject().getCode()); 
						if(lastSemester != -1){
							//create the constant
							x = lastSemester;
							variables.put(cV.getSubject(),x);
							
							//delete the cV from the mandatoryComplementaryValues_copy
							mandatoryComplementaryValues_copy.remove(cV);
						}else{
							//if not, and if every requisite is a constant, calculate the constant
							int minNumberSemesterForPrerequisite = getMinSemesterNumber(mandatoryComplementaryValues_copy, variables, cV, currentSemesterNumber, specialLap, true);
							int minNumberSemesterForCorequisite = getMinSemesterNumber(mandatoryComplementaryValues_copy, variables, cV, currentSemesterNumber, specialLap, false);
							
							int minNumberSemesterForRequisite = -1;
							if(minNumberSemesterForCorequisite > minNumberSemesterForPrerequisite) minNumberSemesterForRequisite = minNumberSemesterForCorequisite;
							else minNumberSemesterForRequisite = minNumberSemesterForPrerequisite;  
							
							if(minNumberSemesterForRequisite == -1 && cV.getListPrerequisites().size() == 0 && cV.getListCorequisites().size() == 0) 
								minNumberSemesterForRequisite = currentSemesterNumber;
							if(minNumberSemesterForRequisite != -1){								
								/******** Adding the variable to the list of variables *******/
								x = minNumberSemesterForRequisite;
								variables.put(cV.getSubject(),x);
								/*************************************************************/
								
								//delete the cV from the mandatoryComplementaryValues_copy
								mandatoryComplementaryValues_copy.remove(cV);
								
								/*** In order to take care of the infinite loop and the non-existence of the requisite ****/
								noneAdded = false;
								specialLap = false;
								/******************************************************************************************/
							}else{
								position++;
								continue;
							}
						}
					}
				}
				
				
				/***** Order them in descending order ******/
				//The first subject will be the one of the farthest semester
				int greaterNumber = -1;
				
				/**** Recognition round *****/
				for(int y : variables.values()){
					if(y > greaterNumber) greaterNumber = y;
				}
				/****************************/
				/*******************************************/
				
				/****** Re-order taking into account the variable ******/
				boolean passed = true;
				mandatoryComplementaryValues_copy = new ArrayList<ComplementaryValue>(completePlanInfo.getMandatoryComplementaryValues());
				List<Subject> variablesList = new ArrayList<Subject>(variables.keySet());
				while(greaterNumber != -1){
					passed = true;
					int pos = 0;
					Subject s = null;
					while(pos < variablesList.size()){
						if(s == null) s = variablesList.get(pos);
						else s = variablesList.get(pos);
						
						//Do stuff
						int x = variables.get(s);
						if(x == greaterNumber){
							passed = false;
							//do stuff in a in-loop
							ComplementaryValue cV = getCvBySubjectInCVList(s.getCode(), mandatoryComplementaryValues_copy);
							if(cV != null){
								arrangeBySemesterMaxNumberOfSubjectAllowed(s, cV, mandatoryComplementaryValues_copy, variables, variablesList, currentSemesterNumber);
							}
						}
						pos = variablesList.indexOf(s) +1;
					}
					
					if(passed == true){						
						greaterNumber--;
						passed = true;
					}
				}
				/*******************************************************/
				
				
				/************************************************/
				/***********************************************************/				
			}
			
		}
	}

	private void arrangeBySemesterMaxNumberOfSubjectAllowed(Subject s, ComplementaryValue cV, List<ComplementaryValue> mandatoryComplementaryValues_copy, Map<Subject, Integer> variables,List<Subject> variablesList , int currentSemesterNumber) {
		int x = getLastSemesterForASubject(s.getCode());
		if(x == -1){
			x = getVariableForSimultaneousEquationsMap(variables, s.getCode());
			if(x != -1){
				
				for(Subject sRequisite : cV.getListPrerequisites()){
					ComplementaryValue cVRequisite = getCvBySubjectInCVList(sRequisite.getCode(), mandatoryComplementaryValues_copy);
					if(cVRequisite != null){		
						//Get sRequisite from the variables
						sRequisite = getSubjectFromVariables(sRequisite.getCode(), variablesList);
						if(sRequisite != null){					
							arrangeBySemesterMaxNumberOfSubjectAllowed(sRequisite, cVRequisite, mandatoryComplementaryValues_copy, variables, variablesList, currentSemesterNumber);
						}
					}
				}
				
				int minNumberSemesterForPrerequisite = getMinSemesterNumber(mandatoryComplementaryValues_copy, variables, cV, currentSemesterNumber, false, true);
				
				//Get the maxPrerequisiteSemesterNumber, the maxCorequisiteSemesterNumber, select the total
				if(minNumberSemesterForPrerequisite > x){
					x = minNumberSemesterForPrerequisite;
				}
				
				//See if the maxTotalSemesterNumber has less than 6 subjects in it, else search for the closest following semester which has less than 6
				int subjectsInSemester = getNumberOfSubjectsInSemester(x);
				while(subjectsInSemester >= 6){
					x++;
					subjectsInSemester = getNumberOfSubjectsInSemester(x);
				}
				
				variables.put(s, x);
				
				/****** Add them to the plan ******/
				List<ComplementaryValue> temporaryList = new ArrayList<ComplementaryValue>();
				temporaryList.add(cV);
				addSubjectsToPlan(temporaryList, "" + x, false);						
				/**********************************/
			}
		}
		//set the variable to that semester
		variablesList.remove(s);
		
		
	}

	private Subject getSubjectFromVariables(String subjectCode, List<Subject> variablesList) {
		Subject toReturn = null;
		
		for(Subject s : variablesList){
			if(s.getCode().equals(subjectCode)){
				toReturn = s;
				break;
			}
		}
		
		return toReturn;
	}

	/**
	 * This method will return the number of subjects for the @param semesterNumber-th semester, e.g. semesterNumber = 0, will return the number of subjects for the first semester
	 * if the semester does not exist then return -1
	 * 
	 * 
	 * @param semesterNumber
	 * @return
	 */
	private int getNumberOfSubjectsInSemester(int semesterNumber) {
		int toReturn = -1;

		if(semesterNumber < semesterList.size())
			toReturn = semesterList.get(semesterNumber).getSubjects().size();
		
		return toReturn;
	}

	/**
	 * 
	 * @param mandatoryComplementaryValues_copy
	 * @param cV
	 * @param firstAvailableSemesterNumber
	 * @param specialLap
	 * @param isPrerequisite
	 * @return
	 */
	private int getMinSemesterNumber(List<ComplementaryValue> mandatoryComplementaryValues_copy, Map<Subject, Integer> variables, ComplementaryValue cV, int firstAvailableSemesterNumber, boolean specialLap, boolean isPrerequisite) {
		int maxOfMinsNumberSemesterForRequisite = -1;
		
		List<List<Subject>> list = null;
		if(isPrerequisite) list = cV.getPrerequisitesLists();
		else list = cV.getCorequisitesLists();
		
		if(list != null){			
			for(List<Subject> sRequisites : list){
				//This is an AND requisite
				int minNumberSemesterForOrRequisite = -1;
				for(Subject sRequisite : sRequisites){
					//Get the current semester 
					int currentRequisiteSemesterNumber = getVariableForSimultaneousEquationsMap(variables, sRequisite.getCode());//getLastSemesterForASubject(sRequisite.getCode());
					
					//because these are pre-requisites
					if(currentRequisiteSemesterNumber != -1 && isPrerequisite == true) currentRequisiteSemesterNumber++; 
					
					if(specialLap == true && currentRequisiteSemesterNumber == -1){
						if(getCvBySubjectInCVList(sRequisite.getCode(), mandatoryComplementaryValues_copy) == null){
							currentRequisiteSemesterNumber = firstAvailableSemesterNumber;
						}
					}
					//This is an OR requisite
					//Select the lowest number here
					if((minNumberSemesterForOrRequisite == -1 || currentRequisiteSemesterNumber < minNumberSemesterForOrRequisite) && currentRequisiteSemesterNumber != -1){
						minNumberSemesterForOrRequisite = currentRequisiteSemesterNumber; 
					}
				}
				//Because the result in minNumberSemesterForOrRequisite is the lowest semester number between the OR requisite, and it will represent a AND requsitie
				if(minNumberSemesterForOrRequisite == -1) {
					maxOfMinsNumberSemesterForRequisite = -1;
					break;
				}
				if(maxOfMinsNumberSemesterForRequisite < minNumberSemesterForOrRequisite){
					maxOfMinsNumberSemesterForRequisite = minNumberSemesterForOrRequisite;
				}
			}
			
			if(maxOfMinsNumberSemesterForRequisite != -1 && maxOfMinsNumberSemesterForRequisite < firstAvailableSemesterNumber) maxOfMinsNumberSemesterForRequisite = firstAvailableSemesterNumber;
		}
		
		return maxOfMinsNumberSemesterForRequisite;
	}

	/**
	 * This method will return true if in @param listCV is any subject with the same code as @param subjectCode
	 * 
	 * @param subjectCode
	 * @param listCV
	 * @return
	 */
	private ComplementaryValue getCvBySubjectInCVList(String subjectCode,	List<ComplementaryValue> listCV) {
		ComplementaryValue toReturn = null;
		
		if(subjectCode != null)
		for(ComplementaryValue cV : listCV){
			if(cV.getSubject().getCode().equals(subjectCode) == true){
				toReturn = cV;
				break;
			}
		}
		
		return toReturn;
	}

	
	private int getCurrentSemesterNumber() {
		int toReturn = -1;
		
		if(currentSemesterValue != null){
			for(Semester s : semesterList){
				if(s.getSemesterValue().equals(currentSemesterValue)){
					toReturn = semesterList.indexOf(s);
					break;
				}
			}
		}
		
		return toReturn;
	}

	/**
	 * Will return the number of the last approved subject found in the plan, if there is no such subject approved return -1
	 * 
	 * @param subjectCode
	 * @return
	 */
	private int getLastSemesterForASubject(String subjectCode) {
		int semesterNumber = -1;
		//Check if the subject Exists and is approved
		for(SubjectValue sV : subjectValuesList){
			if(sV.getComplementaryValues().getSubject().getCode().trim().equals(subjectCode) == true){
				if(sV.getGrade() >= 3 || sV.isTaken() == false){
					int semesterNumber_T = semesterList.indexOf(subjectValuesAndSemesterMap.get(sV));
					if(semesterNumber_T > semesterNumber){
						semesterNumber = semesterNumber_T;
					}
				}
			}
		}
		
		//Get the semester number
		return semesterNumber ;
	}

	/**
	 * This method will return the integer or variable corresponding to the complementary value from the simultaneous equations
	 * 
	 * @param mapCVI
	 * @param careerCode
	 * @param subjectCode
	 * @return
	 */
	private Integer getVariableForSimultaneousEquationsMap(Map<Subject, Integer> mapSI, String subjectCode) {
		Integer toReturn = -1;
		
		for(Subject s: mapSI.keySet()){
			if(s.getCode().equals(subjectCode) == true){
				toReturn = mapSI.get(s);
				break;
			}
		}
		
		return toReturn;
	}
	

	/**
	 * Will show the curtain from view
	 */
	public void showCurtain(){
		view.showCurtain();
	}
	
	/**
	 * Will hide the curtain from view
	 */
	public void hideCurtain(){
		view.hideCurtain();
	}
	
	/******************** JQUERY FUNCTIONS *********************/

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

	public static native void avoidAccordionPropagation() /*-{
		$wnd.stopPropagationOfClickOnSelectSubject()
	}-*/;

	public static native void addEventListenerOnChangeToSiaSummary() /*-{
		$wnd.addEventListenerOnScroll()
	}-*/;
	
	public static native void hideAndUpdateTooltips() /*-{
		$wnd.hideAndUpdateTooltips()
	}-*/;
	
	public static native void makeDropdownButtonsWork() /*-{
		$wnd.makeDropdownButtonsWork();
	}-*/;
	
	public static native void addFunctionsToSecondaryMenu() /*-{
		$wnd.addFunctionsToSecondaryMenu();
	}-*/;
	
	/**
	 * This method will create the events make the part about selecting the current semester work
	 */
	public static native void selectCurrentSemesterActions() /*-{
		$wnd.selectCurrentSemesterActions();
	}-*/;
	
	public static native void selectCurrentSemesterPanel() /*-{
		$wnd.selectCurrentSemesterPanel();
	}-*/;
	
	/************************************************************/

	/*********************** Behaviors **************************/
	
	public void onSpecificSubjectSelected(String subjectName, String subjectCode, String careerCode) {
		
		SelectedSubjectViewImpl sSV = new SelectedSubjectViewImpl();
		sSV.setPresenter(this);
		sSV.setTexts(subjectName, subjectCode, careerCode);
		selectedSubjects.add(sSV);
		
		SubjectAccordionViewImpl accordion = getAccordion(subjectCode);
		if(accordion != null){			
			accordion.changeState();
		}
		
		changeSubheadersState(subjectCode);
		
		searchSubjectView.addSelectedSubject(sSV.asWidget());
		
	}
	
	public void onSpecificSubjectUnselected(String subjectName, String subjectCode, String careerCode) {
		
		//remove the subjectSelected from the box in the view
		SelectedSubjectViewImpl selected = getSelectedSubjectView(subjectCode);
		selected.remove();
		selectedSubjects.remove(selected);
		
		//changeState of the accordion
		SubjectAccordionViewImpl accordion = getAccordion(subjectCode);
		if(accordion != null){			
			accordion.changeState();
		}
		
		changeSubheadersState(subjectCode);
		
	}

	private void onClickAddSemester() {
		if(semesters <=20){
			createSemester(new Semester());
		}
	}

	public void onSearchButtonClicked(final String searchText, final String careerCode, final String type, int page) {
		
		accordions.clear();

		rpcService.getSubjectsFromSia(searchText, type, careerCode, "bog", page, getStudent(), new AsyncCallback<SiaResultSubjects>(){
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry, we couldn't connect to SIA");
			}
			
			@Override
			public void onSuccess(SiaResultSubjects result) {
				loadSubjectsToSearchView(result, careerCode, searchText, type);
			}
			
		});
		
	}

	public void onSubjectDelete(String publicid) {
		SubjectValue subjectValuesToDelete = getSubjectValuesByPublicId(publicid);
		if(subjectValuesToDelete != null){
			confirmDeleteSubject(subjectValuesToDelete);
		}
		
	}
	
	private void onSubjectWidgetClicked(String publicId) {
		SubjectValue sV = getSubjectValuesByPublicId(publicId);
		if(subjectValuesSelected != null) {
			deleteAllOpacities();
			hideArrows();
		}
		if(sV.equals(subjectValuesSelected)==false){
			subjectValuesSelected = sV;
			
			//get complementary values from sia
			//getComplementaryValues(sV); //TODO uncomment or delete
			
			//Show/Create lines
			showConnections(sV);
			
			//Reduce/Increase opacity
			modifyOpacity(sV);
		}else{
			setSubjectValuesSelected(null);
		}
	}
	
	private void onClickAddSubject(String semester){
		searchSubjectView.setSemester(semester);
		searchSubjectView.showIt();
		showToolTip();
		arrangeTopOfSearchSubjectView();
	}
	
	@Override
	public void onSavePlanAsDefaultClicked() {
		eventBus.fireEvent(new SavePlanAsDefaultEvent(this.getPlan()));
	}
	
	public void onFinalizarButtonClick(final String semesterString) {
		
		searchSubjectView.clear();
		accordions.clear();
		
		//TODO
		/**
		 * read the slectedSubject List, for each subject, create a subjectValue and add its complementaryValue
		 */
		List<String> selectedSubjectCodeStrings = new ArrayList<String>();
		List<String> selectedSubjectCareerStrings = new ArrayList<String>();
		List<String> selectedDefaultSubjectCodeStrings = new ArrayList<String>();
		List<String> selectedDefaultSubjectCareerStrings = new ArrayList<String>();
		for(SelectedSubjectViewImpl selectedSubjectsViewImpl : selectedSubjects){
			if(selectedSubjectsViewImpl.getCode().equals(SomosUNUtils.LIBRE_CODE) || selectedSubjectsViewImpl.getCode().equals(SomosUNUtils.OPTATIVA_CODE)){
				selectedDefaultSubjectCodeStrings.add(selectedSubjectsViewImpl.getCode());
				selectedDefaultSubjectCareerStrings.add(selectedSubjectsViewImpl.getCareer());
			}else{				
				String code = selectedSubjectsViewImpl.getCode();
				String careerCode = selectedSubjectsViewImpl.getCareer();
				if(code != null && code.isEmpty() == false){ //in case the "All Careers" are selected
					
					if(careerCode == null || careerCode.isEmpty()){
						careerCode = "";
					}
					
					selectedSubjectCodeStrings.add(code);
					selectedSubjectCareerStrings.add(selectedSubjectsViewImpl.getCareer());
				}
			}
		}
		
		selectedSubjects.clear();
		

		if(selectedSubjectCodeStrings.size() > 0){			
			view.showLoadingSubjects();
			rpcService.getComplementaryValues(selectedSubjectCodeStrings, selectedSubjectCareerStrings, plan.getCareerCode(), new AsyncCallback<List<ComplementaryValue>>(){
				
				public void onFailure(Throwable caught) {
					Window.alert("Sorry, we were unable to add the selected subjects, please try again");
					view.hideLoadingSubjects();
				}
				
				public void onSuccess(List<ComplementaryValue> result) {
					addSubjectsToPlan(result, semesterString);
					view.hideLoadingSubjects();
				}
				
			});
		}
		
		//TODO do something with selectedDefaultSubjectCodeStrings and the other list
		if(selectedDefaultSubjectCodeStrings.size() > 0){			
			showDefaultSubjectCreationView(semesterString);
		}
		
	}
	
	public void hideSearchBox() {
		searchSubjectView.clear();
		accordions.clear();
		selectedSubjects.clear();
		searchSubjectView.hideIt();
	}
	
	public void onCreateDefaultSubjectButtonClick(String subjectGroupName, String credits, final String semester) {
		//TODO create 
		
		defaultSubjectCreationView.hideIt();
		rpcService.createDefaultSubject(subjectGroupName, credits, plan.getCareerCode(), student, new AsyncCallback<ComplementaryValue>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Subject not saved");
			}

			@Override
			public void onSuccess(ComplementaryValue result) {
				Window.alert("Done");
				List<ComplementaryValue> cVList = new ArrayList<ComplementaryValue>();
				cVList.add(result);
				addSubjectsToPlan(cVList, semester);
			}
			
		});
		
	}
	
	public void onAddMandatorySubjectsButton() {
		rpcService.getMandatoryComplementaryValues(plan.getCareerCode(), new AsyncCallback<List<ComplementaryValue>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error - mandatory subjects cannot be loaded.");
			}

			@Override
			public void onSuccess(List<ComplementaryValue> result) {
				loadMandatoryValues(result);				
			}
			
		});
	}

	@Override
	public void showSavePlanPopup() {
		showCurtain();
		if((plan.getName() != null ? plan.getName().isEmpty() == false : false)){
			view.setSugestionText(plan.getName());
		}else{
			view.setSugestionText("Plan de " + plan.getCareer().getName());
		}
		view.showChangeName();
	}

	@Override
	public void planNameChanged(String s) {
		plan.setName(s);
		planChanged("NewName");
		view.hidePopups();
	}
	
	public void onDeletePlanButtonClicked() {
		
		String s = null;
		if(plan.getName() != null && plan.getName().isEmpty() == false){
			s = plan.getName();
		}else{
			s = plan.getCareer().getName();
		}
		view.setPlan(s);
		view.showGeneralPopup();
		
	}
	
	private void onDeleteSemesterClicked(int semester) {
		view.setSemester(Integer.toString(semester+1));
		view.showGeneralPopup();
	}

	public void onGradeAdded(String publicId, String grade){
		if(publicId != null && publicId.isEmpty() == false){
			
			
			SubjectValue sV = getSubjectValuesByPublicId(publicId);
			
			grade = grade.trim().replaceAll(",", ".");
			Double gradeDouble = Double.valueOf(grade);
			
			if(sV != null && gradeDouble >= 0 && gradeDouble <= 5){
				
				sV.setTaken(true);
				sV.setGrade(gradeDouble);
				
				SubjectWidget sW = subjectValuesAndWidgetBiMap.get(sV);
				if(sW != null){
					if(sV.getComplementaryValues().getSubject().isApprovenType() == true){
						if(sV.getGrade() >= 3){
							sW.setGrade("AP");
						}else{
							sW.setGrade("NA");
						}
					}else{						
						String gradeString = SomosUNUtils.getOneDecimalPointString(sV.getGrade());
						sW.setGrade(gradeString);
					}
				}
				
				planChanged("NewGrade");
			}
		}

		hideAndUpdateTooltips();

	}
	
	@Override
	public void onAccordionClicked(String subjectCode, final SubjectAccordionViewImpl accordion, final ComplementaryValueViewImpl view) {
		
		//Get the complementaryValue
		rpcService.getComplementaryValueFromDb(searchSubjectView.getCareerCodeSelected(), subjectCode, new AsyncCallback<ComplementaryValue>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error getting the information");
				accordion.showError();
			}

			@Override
			public void onSuccess(ComplementaryValue result) {
				//Populate it  
				populateAccordionWithComplementaryValue(accordion, view, result);
			}

			
		});
		
	}
	
	public void onGroupsAccordionShows(String subjectCode, final ComplementaryValueViewImpl view){
		rpcService.getGroupsFromSia(subjectCode, "bog", new AsyncCallback<SiaResultGroups>(){

			@Override
			public void onFailure(Throwable caught) {
				view.showGroupError();
			}

			@Override
			public void onSuccess(SiaResultGroups result) {
				populateAccordionWithGroups(view, result);				
			}
			
		});

	}

	@Override
	public void addComplementaryValueView(final SubjectAccordionViewImpl accordion, final String name, final String code, final String careerCode) {
		
		accordion.showCurtain();
		
		final ComplementaryValueViewImpl newView = new ComplementaryValueViewImpl(name, code, accordion, accordion.getCounter(), accordion.getAmmountOfCVViews());
		newView.setPresenter(this);
		newView.setSubjectGroupName("-");
		newView.addStyleName("isRight");
		accordion.addComplementaryView(newView);
		
		//Get the complementaryValue
		rpcService.getComplementaryValueFromDb(plan.getCareerCode(), code, new AsyncCallback<ComplementaryValue>(){
			
			@Override
			public void onFailure(Throwable caught) {
				accordion.hideCurtain();
				GWT.log("error getting the information");
				accordion.showError();
			}
			
			@Override
			public void onSuccess(ComplementaryValue result) {
				accordion.hideCurtain();
				//Populate it  
				populateAccordionWithComplementaryValue(accordion, newView, result);
				accordion.goRight();
			}
			
			
		});
		
		
	}

	public boolean isSubjectSelected(String subjectCode) {
		boolean toReturn = false;
		if(subjectCode != null){			
			SelectedSubjectViewImpl selected = getSelectedSubjectView(subjectCode);
			if(selected != null){
				toReturn = true;
			}
		}
		return toReturn;
	}

	/**
	 * This method will be called by a click event
	 */
	public void onSelecteCurrentSemester(int currentSemesterNumber) {
		setPlanCurrentSemester(currentSemesterNumber-1);
	}
	
	/**
	 * This method will be used only when continue has been confirmed
	 */
	public void onCompletePlanClicked(){
		completePlan();
	}
}