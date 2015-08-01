package com.uibinder.client.index.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.connection.ConnectionsController;
import com.uibinder.client.index.dnd.PickUpDragController;
import com.uibinder.client.index.dnd.SemesterDropController;
import com.uibinder.client.index.event.GradeChangeEvent;
import com.uibinder.client.index.event.GradeChangeEventHandler;
import com.uibinder.client.index.event.SavePlanAsDefaultEvent;
import com.uibinder.client.index.service.SUNServiceAsync;
import com.uibinder.client.index.view.DefaultSubjectCreationView;
import com.uibinder.client.index.view.DefaultSubjectCreationViewImpl;
import com.uibinder.client.index.view.PlanView;
import com.uibinder.client.index.view.PlanViewImpl;
import com.uibinder.client.index.view.SearchSubjectViewImpl;
import com.uibinder.client.index.view.SelectedSubjectView;
import com.uibinder.client.index.view.SelectedSubjectViewImpl;
import com.uibinder.client.index.view.SiaSummaryView;
import com.uibinder.client.index.view.SiaSummaryViewImpl;
import com.uibinder.client.index.view.SubjectAccordionView;
import com.uibinder.client.index.view.SubjectAccordionViewImpl;
import com.uibinder.client.index.view.WarningDeleteSubjectView;
import com.uibinder.client.index.view.WarningDeleteSubjectViewImpl;
import com.uibinder.client.index.widget.LineWidget;
import com.uibinder.client.index.widget.PlanWidget;
import com.uibinder.client.index.widget.SemesterWidget;
import com.uibinder.client.index.widget.SubjectWidget;
import com.uibinder.shared.SiaResultSubjects;
import com.uibinder.shared.SomosUNUtils;
import com.uibinder.shared.control.Career;
import com.uibinder.shared.control.ComplementaryValue;
import com.uibinder.shared.control.Plan;
import com.uibinder.shared.control.Semester;
import com.uibinder.shared.control.Student;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectGroup;
import com.uibinder.shared.control.SubjectValue;
import com.uibinder.shared.values.TypologyCodes;

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
DefaultSubjectCreationView.Presenter{
	
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
		
		setCareer(plan.getCareer());
		
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
		
		if(save == true){			
			planChanged("NewSemester");
		}
		
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
			rpcService.getComplementaryValuesFromMisPlanes(plan.getCareerCode(), sV.getComplementaryValues().getSubject().getCode(), new AsyncCallback<ComplementaryValue>(){
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
		
		planChanged("SemesterDelete");
		
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
		plan.calculateGpa();
		siaSummaryView.setGPA(SomosUNUtils.getOneDecimalPointString(plan.getGpa()));
		siaSummaryView.setApprovedCredits("" + totalCredits[1]);
		siaSummaryView.setAdditionalyCredits("" + getAdditionalCredits());
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
	
	protected void loadSubjectsToSearchView(SiaResultSubjects result, String careerCode) {
		
		addSubjectsToSearchView(result.getSubjectList(), careerCode);
		createPagination(result);
		showToolTip();
				
	}

	private void addSubjectsToSearchView(List<Subject> subjectList, String careerCode) {
		
		searchSubjectView.clearAccordionContainer();
		accordions.clear();
		
		for(Subject s : subjectList){
			SubjectAccordionViewImpl accordion = new SubjectAccordionViewImpl(searchSubjectView.getSubjectsAmmount());
			accordion.setPresenter(this);
			accordion.setCareerList(careers);
			accordion.setHeader(s.getCode(), s.getName(), "L", Integer.toString(s.getCredits()), careerCode);
			accordion.setSubjectGroupName("Asignaturas de relleno");
			accordions.add(accordion);
			
			SelectedSubjectViewImpl selected = getSelectedSubjectView(s.getCode());
			if(selected != null){
				accordion.changeState();
			}
			
			searchSubjectView.addSubject(accordion);
		}
		
		avoidAccordionPropagation();
		
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
	
	private void showDefaultSubectCreationView(String s){
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
	
	/************************************************************/

	/*********************** Behaviors **************************/

	public void onSpecificSubjectSelected(String subjectName, String subjectCode, String careerCode) {
		
		SelectedSubjectViewImpl sSV = new SelectedSubjectViewImpl();
		sSV.setPresenter(this);
		sSV.setTexts(subjectName, subjectCode, careerCode);
		selectedSubjects.add(sSV);
		
		SubjectAccordionViewImpl accordion = getAccordion(subjectCode);
		accordion.changeState();
		
		searchSubjectView.addSelectedSubject(sSV.asWidget());
		
	}
	
	public void onSpecificSubjectUnselected(String subjectName, String subjectCode, String careerCode) {
		
		//remove the subjectSelected from the box in the view
		SelectedSubjectViewImpl selected = getSelectedSubjectView(subjectCode);
		selected.remove();
		selectedSubjects.remove(selected);
		
		//changeState of the accordion
		SubjectAccordionViewImpl accordion = getAccordion(subjectCode);
		accordion.changeState();
		//accordions.remove(accordion);
		
	}

	private void onClickAddSemester() {
		if(semesters <=20){
			createSemester(new Semester());
		}
	}

	public void onSearchButtonClicked(String s, final String careerCode, String type, int page) {
		
		accordions.clear();
		
		if(type == "all") {
			type = "";
		}
		rpcService.getSubjectsFromSia(s, type, careerCode, "bog", page, getStudent(), new AsyncCallback<SiaResultSubjects>(){
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry, we couldn't connect to SIA");
			}
			
			@Override
			public void onSuccess(SiaResultSubjects result) {
				loadSubjectsToSearchView(result, careerCode);
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
				selectedSubjectCodeStrings.add(selectedSubjectsViewImpl.getCode());
				selectedSubjectCareerStrings.add(selectedSubjectsViewImpl.getCareer());
			}
		}
		
		selectedSubjects.clear();

		if(selectedSubjectCodeStrings.size() > 0){			
			rpcService.getComplementaryValues(selectedSubjectCodeStrings, selectedSubjectCareerStrings, new AsyncCallback<List<ComplementaryValue>>(){
				
				public void onFailure(Throwable caught) {
					Window.alert("Sorry, we were unable to add the selected subjects, please try again");				
				}
				
				public void onSuccess(List<ComplementaryValue> result) {
					addSubjectsToPlan(result, semesterString);
				}
				
			});
		}
		
		//TODO do something with selectedDefaultSubjectCodeStrings and the other list
		if(selectedDefaultSubjectCodeStrings.size() > 0){			
			showDefaultSubectCreationView(semesterString);
		}
		
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
		view.showCurtain();
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
		
		String s = "";
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
	
}