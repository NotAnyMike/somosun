package com.somosun.plan.client.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.somosun.plan.client.index.event.ContinueDefaultCareerEvent;
import com.somosun.plan.client.index.event.ContinueDefaultCareerEventHandler;
import com.somosun.plan.client.index.event.GenerateAcademicHistoryFromStringEvent;
import com.somosun.plan.client.index.event.GenerateAcademicHistoryFromStringEventHandler;
import com.somosun.plan.client.index.event.LoadPlanEvent;
import com.somosun.plan.client.index.event.LoadPlanEventHandler;
import com.somosun.plan.client.index.event.NewEmptyPlanEvent;
import com.somosun.plan.client.index.event.NewEmptyPlanEventHandler;
import com.somosun.plan.client.index.event.SavePlanAsDefaultEvent;
import com.somosun.plan.client.index.event.SavePlanAsDefaultHandler;
import com.somosun.plan.client.index.presenter.AboutUsPresenter;
import com.somosun.plan.client.index.presenter.AnnouncementPresenter;
import com.somosun.plan.client.index.presenter.ContactUsPresenter;
import com.somosun.plan.client.index.presenter.CreatePresenter;
import com.somosun.plan.client.index.presenter.IndexPresenter;
import com.somosun.plan.client.index.presenter.LoadingPresenter;
import com.somosun.plan.client.index.presenter.PlanPresenter;
import com.somosun.plan.client.index.presenter.Presenter;
import com.somosun.plan.client.index.presenter.TopBarPresenter;
import com.somosun.plan.client.index.service.LoginService;
import com.somosun.plan.client.index.service.LoginServiceAsync;
import com.somosun.plan.client.index.service.SUNServiceAsync;
import com.somosun.plan.client.index.view.AboutUsViewImpl;
import com.somosun.plan.client.index.view.AnnouncementViewImpl;
import com.somosun.plan.client.index.view.ContactUsViewImpl;
import com.somosun.plan.client.index.view.CreateViewImpl;
import com.somosun.plan.client.index.view.IndexViewImpl;
import com.somosun.plan.client.index.view.LoadingViewImpl;
import com.somosun.plan.client.index.view.PlanViewImpl;
import com.somosun.plan.client.index.view.SiaSummaryViewImpl;
import com.somosun.plan.client.index.view.TopBarViewImpl;
import com.somosun.plan.shared.LoginInfo;
import com.somosun.plan.shared.RandomPhrase;
import com.somosun.plan.shared.control.Career;
import com.somosun.plan.shared.control.Plan;
import com.somosun.plan.shared.control.Semester;
import com.somosun.plan.shared.control.SemesterValue;
import com.somosun.plan.shared.control.Student;

public class AppController implements Presenter, ValueChangeHandler<String> {
	
	// Creating all the views that will exist once for all to save them to let users go back to their view
	private IndexPresenter indexPresenter;
	private TopBarPresenter topBarPresenter;
	private CreatePresenter createPresenter;
	private PlanPresenter planPresenter;
	private AboutUsPresenter aboutUsPresenter;
	private AnnouncementPresenter announcementPresenter;
	private LoadingPresenter loadingPresenter;
	private ContactUsPresenter contactUsPresenter;
	
	private IndexViewImpl indexView;
	private TopBarViewImpl topBarView;
	private CreateViewImpl createView;
	private PlanViewImpl planView;
	private AboutUsViewImpl aboutUsView;
	private AnnouncementViewImpl announcementView;
	private SiaSummaryViewImpl siaSummaryView;
	private LoadingViewImpl loadingView;
	private ContactUsViewImpl contactUsView;
	
	//Taking care of the random phrase funtionality variables 
	private List<RandomPhrase> phrases;
	private RandomPhrase phrase;
	
	private final HandlerManager eventBus;
	private final SUNServiceAsync rpcService;
	private HasWidgets container;
	private String token;
	private String lastToken;
	
	private LoginInfo loginInfo = new LoginInfo();
	private Student student;
	private LoginServiceAsync loginService;
	
	/************** AsyncCallback variables *****************/
	
	private AsyncCallback savedPlanAsDefault = new AsyncCallback(){

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Sorry, we could not save the Plan as Default");
		}

		@Override
		public void onSuccess(Object result) {
			Window.alert("Plan saved as default");
		}
		
	};
	
	/************** AsyncCallback variables *****************/
	
	public AppController(SUNServiceAsync rpcService, HandlerManager eventBus){
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		bind();
		getRandomPhrases();
	}
	
	private void bind(){
		History.addValueChangeHandler(this);
		
		eventBus.addHandler(GenerateAcademicHistoryFromStringEvent.TYPE, new GenerateAcademicHistoryFromStringEventHandler(){
			@Override
			public void doGenerateAcademicHistoryFromString(String academicHisotry){
				generateAcademicHistoryFromString(academicHisotry);
			}
		});
		
		eventBus.addHandler(ContinueDefaultCareerEvent.TYPE, new ContinueDefaultCareerEventHandler(){
			@Override
			public void onContinueDefaultCareerButtonClicked(String careerCode) {
				genereteDefaultPlan(careerCode);
			}
		});
		
		eventBus.addHandler(SavePlanAsDefaultEvent.TYPE, new SavePlanAsDefaultHandler(){
			@Override
			public void onSavePlanAsDefault(Plan plan) {
				savePlanAsDefault(plan);
			}
			
		});
		
		eventBus.addHandler(NewEmptyPlanEvent.TYPE, new NewEmptyPlanEventHandler(){
			public void onNewEmptyPlanButtonClicked(String careerCode) {
				generateEmptyPlan(careerCode);
			}
		});
		
		eventBus.addHandler(LoadPlanEvent.TYPE, new LoadPlanEventHandler(){
			public void onLoadPlanEventHandler(String planId) {
				loadSpecificPlan(planId);
			}
		});
	}
	
	private void genereteDefaultPlan(String careerCode){
		showLoadingPage("Cargando el plan por default ...");
		
		
		rpcService.getPlanDefault(careerCode, new AsyncCallback<Plan>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry we got a problem");
			}

			@Override
			public void onSuccess(Plan result) {
				Plan plan = result;
				setPlanPresenter(plan);
				//planPresenter.setPlan(plan);
				History.newItem("plan");
			}
			
		});
	
	}
	
	private void generateEmptyPlan(String careerCode) {
		showLoadingPage("Creando un plan vacío ... ");
		/**
		 * use the rpcService.createEmptyPlan(careerCode);
		 */

		rpcService.getCareerToUse(careerCode, new AsyncCallback<Career>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry we got an error");
				History.newItem("create");
			}

			@Override
			public void onSuccess(Career result) {
				createEmptyPlan(result);
			}

		});
		
	}
	
	private void generateAcademicHistoryFromString(String academicHistory){
		showLoadingPage("Creando tu plan, buscando y descargando la información ... ");
		rpcService.generatePlanFromAcademicHistory(academicHistory, new AsyncCallback<Plan>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Unable to generate plan from academic history");
				Window.alert("Hubo un error, asegúrate de que copiaste bien la información y vuelve a intentar");
				History.fireCurrentHistoryState();
			}
			
			@Override
			public void onSuccess(Plan result) {
				
				GWT.log("Plan from academic history generated");
				setPlanPresenter(result);
				
			}
			
		}); 
	}
	
	@Override
	public void go(HasWidgets container){
		this.container = container;
		
		if(History.getToken().equals("")){
			History.newItem("index");
		} else {
			History.fireCurrentHistoryState();
		}
		
		getLoginInfo();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) 
	{
		
		token = event.getValue();
		
		if(token != null){
			
			if(topBarView == null){
				topBarView = new TopBarViewImpl();
				topBarPresenter = new TopBarPresenter(rpcService, eventBus, topBarView);
				topBarPresenter.go(RootPanel.get("topArea"));
			}
			
			if(lastToken.equals("plan")){
				if(planPresenter != null){
					savePlan();
					RootPanel.get("mainContainer").removeStyleName("noSelect");
				}
			}
			
			/************ Restricting access to just non-blocked users **************/
			if(loginInfo.isLoggedIn() == false || student.isBlocked()){
				if(token.equals("plan") ||  token.equals("create")){
					token = "aboutUs";
					History.replaceItem("aboutUs");
				}
			}
			
			/**
			 * This part will take care of the stability of the plan widget when its height changes due
			 * to the changes on the number of max subjects on one semester
			 */
			if(token.equals("plan")){
				if(RootPanel.get("centerArea").getElement().getAttribute("valign") != "top")
				RootPanel.get("centerArea").getElement().setAttribute("valign","top");
			}else if(RootPanel.get("centerArea").getElement().getAttribute("valign")!="middle"){
				RootPanel.get("centerArea").getElement().setAttribute("valign","middle");
			}
			
			if(token.equals("create")){
				if(createView == null){
					createView = new CreateViewImpl();
				}
				if(createPresenter == null){
					createPresenter = new CreatePresenter(rpcService, eventBus, createView);
					createPresenter.loadPlans(); 
				}
				if(student == null){					
					createPresenter.showWarning();
				}else{
					createPresenter.hideWarning();
				}
				createPresenter.loadPlans();
				createPresenter.go(RootPanel.get("centerArea"));
			} else if(token.equals("plan")) {
				if(planView != null && planPresenter != null){
					if(student == null){
						siaSummaryView.showWarning();
					}else{
						siaSummaryView.hideWarning();						
					}
					RootPanel.get("mainContainer").addStyleName("noSelect");
					planPresenter.go(RootPanel.get("centerArea"));
					planPresenter.showToolTip();
				}else{
					History.newItem("create");
				}
			} else if(token.equals("aboutUs")) {
				if(aboutUsView == null){
					aboutUsView = new AboutUsViewImpl();
				}
				if(aboutUsPresenter == null){
					aboutUsPresenter = new AboutUsPresenter(rpcService, eventBus, aboutUsView);
				}
				aboutUsPresenter.go(RootPanel.get("centerArea"));
			} else if(token.equals("announcement")) {
				if(aboutUsView == null){
					announcementView = new AnnouncementViewImpl();
				}
				if(aboutUsPresenter == null){
					announcementPresenter = new AnnouncementPresenter(rpcService, eventBus, announcementView);
				}
				announcementPresenter.go(RootPanel.get("centerArea"));
			} else if(token.contains("contactUs")){
				if(contactUsView == null){
					contactUsView = new ContactUsViewImpl();
				}
				if(contactUsPresenter == null){
					contactUsPresenter = new ContactUsPresenter(rpcService, eventBus, contactUsView);
				}
				String type = "";
				if(token.contains("type=error")) type = "error";
				if(token.contains("type=other")) type = "other";
				contactUsPresenter.setType(type);
				contactUsPresenter.go(RootPanel.get("centerArea"));
			} else {
				if(indexView == null){
					indexView = new IndexViewImpl();
				}
				if(indexPresenter == null){
					indexPresenter = new IndexPresenter(rpcService, eventBus, indexView);					
				}
				indexPresenter.go(RootPanel.get("centerArea"));
			}
			setLabelsOnTopBar(token);
			lastToken = token;
		}
		
	}

	private void setLabelsOnTopBar(String token) {
		if(token.equals("dnd")){
			topBarPresenter.setNameOfThePage("You shouldn't be here!");
		} else if (token.equals("create")){
			topBarPresenter.setNameOfThePage("Plan de Estudios");
		} else if(token.equals("plan")){
			topBarPresenter.setNameOfThePage("Plan de Estudios");
		} else if(token.equals("aboutUs") || token.equals("announcement")){
			phrase = getAPhrase();
			topBarPresenter.setNameOfThePage(phrase.getRandomPhrase(), phrase.getAuthor());
		} else {
			topBarPresenter.setNameOfThePage("");
		}
	}
	
	private void getRandomPhrases(){
		rpcService.getRandomPhrase(gettingRandomPhrase);
	}
	
	private RandomPhrase getAPhrase(){
		
		if(phrases==null){
			getRandomPhrases();
			RandomPhrase toReturn = new RandomPhrase("Estudia para la vida, no para los exámenes", "Anónimo");
			return toReturn;
		}else{
			
			Random randomNumberClass = new Random();
			double randomNumberDouble = Math.floor(randomNumberClass.nextDouble()*phrases.size());
			int radomNumber = (int) randomNumberDouble;
			RandomPhrase current = phrases.get(radomNumber);
			return current;
			
		}

	}
	
	AsyncCallback<List<RandomPhrase>> gettingRandomPhrase = new AsyncCallback<List<RandomPhrase>>(){

		@Override
		public void onFailure(Throwable caught) {
			//TODO what to do?
		}

		@Override
		public void onSuccess(List<RandomPhrase> result) {
			phrases = result;
		}
		
	};
	
	private void loadLogin(boolean isThereAUrl){
		if(isThereAUrl == true){
			if(loginInfo.isLoggedIn()==true){
				topBarPresenter.setLogOutUrl(loginInfo.getLogoutUrl());
				topBarPresenter.setUserName(student.getName());
			} else {
				topBarPresenter.setLogInUrl(loginInfo.getLoginUrl());
				topBarPresenter.setUserName("invitado");
			}
		} else {
			topBarPresenter.setUserName("invitado");
		}
		topBarPresenter.showAdminLink((student == null ? false : student.isAdmin()));
	}
	
	public void getLoginInfo(){
		loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL() + "#" + token, new AsyncCallback<LoginInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				loginInfo.setLoggedIn(false);
				loginInfo = null;
				student = null;
				loadLogin(false);
				if(createPresenter != null)	{
					createPresenter.showWarning();
				}
				if(siaSummaryView != null){
					siaSummaryView.showWarning();
				}
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				student = loginInfo.getStudent();
				loadLogin(true);
				if(createPresenter != null){
					if(loginInfo.isLoggedIn())	{
						createPresenter.hideWarning();
					}else{
						createPresenter.showWarning();
					}					
				}
				if(siaSummaryView != null){
					if(loginInfo.isLoggedIn()){
						siaSummaryView.hideWarning();
					}else{
						siaSummaryView.showWarning();
					}
					
				}
			}});
	}
	
	private void showLoadingPage(String label)
	{
		if(loadingView == null){
			loadingView = new LoadingViewImpl();
		}
		if(loadingPresenter == null){
			loadingPresenter = new LoadingPresenter(rpcService, eventBus, loadingView);
			loadingPresenter.setLabel(label);
		}
		loadingPresenter.go(RootPanel.get("centerArea"));
	}
	
	private void savePlanAsDefault(Plan plan) {
		rpcService.savePlanAsDefault(student, plan, savedPlanAsDefault);
	}
	
	private void createEmptyPlan(Career result) {
		Plan plan = new Plan();
		plan.setCareer(result);
		List<Semester> semesters = new ArrayList<Semester>();
		for(int x = 0; x < 10; x++){
			Semester s = new Semester(/*Integer.toString(x)*/);
			semesters.add(s);
		}
		plan.setSemesters(semesters);
		
		setPlanPresenter(plan);
		
	}
	
	private void setPlanPresenter(Plan plan){
		
		rpcService.getCurrentSemesterValue(new AsyncCallback<SemesterValue>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Error getting the current semesterValue");
			}

			@Override
			public void onSuccess(SemesterValue result) {
				if(planPresenter != null && planPresenter.getPlan() != null){					
					planPresenter.setCurrentSemesterValue(result);
				}
			}
			
		});
		
		planView = new PlanViewImpl();
		siaSummaryView = new SiaSummaryViewImpl();
			
		planPresenter = new PlanPresenter(rpcService, eventBus, planView, siaSummaryView, student);
		
		if(student == null || student.isAdmin()==false){
			planPresenter.deleteAdminButtons();
		}
		
		if(student == null){
			siaSummaryView.showWarning();
		}else{
			siaSummaryView.hideWarning();
		}
		
		planPresenter.setPlan(plan);
		
		History.newItem("plan");
	}
	
	private void loadSpecificPlan(String planId){
		
		showLoadingPage("Loading your plan ...");
		
		rpcService.getPlanByUser(planId, new AsyncCallback<Plan>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error loading the plan selected");
				Window.Location.reload();
			}

			@Override
			public void onSuccess(Plan result) {
				if(result != null){			
					GWT.log("onSuccess activated");
					setPlanPresenter(result);
				}
			}
			
		});
		
	}

	@SuppressWarnings("unchecked")
	private void savePlan() {
		if(planPresenter != null){
			
			Plan plan = planPresenter.getPlan();
			
			if(plan != null){				
				
				boolean toSave = false;
				
				if(plan.getSemesters() != null){
					for(Semester s : plan.getSemesters()){
						if(s.getSubjects() != null && s.getSubjects().size() > 0){
							toSave = true;
							break;
						}
					}
				}
				
				//It deserves to be saved
				if(toSave){
					
					if(plan.getName() == null || plan.getName().isEmpty() == true){
						plan.setName(plan.getCareer().getName());
					}
					
					rpcService.savePlan(student, plan, new AsyncCallback(){
						
						@Override
						public void onFailure(Throwable caught) {
							GWT.log("Error saving plan - appController");
						}
						
						@Override
						public void onSuccess(Object result) {
							GWT.log("Plan saved - appController");
						}
						
					});
					
				}

			}
		}
		
	}
}
