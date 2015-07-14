package com.uibinder.index.client;

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
import com.uibinder.index.client.event.ContinueDefaultCareerEvent;
import com.uibinder.index.client.event.ContinueDefaultCareerEventHandler;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEventHandler;
import com.uibinder.index.client.event.NewEmptyPlanEvent;
import com.uibinder.index.client.event.NewEmptyPlanEventHandler;
import com.uibinder.index.client.event.SavePlanAsDefaultEvent;
import com.uibinder.index.client.event.SavePlanAsDefaultHandler;
import com.uibinder.index.client.presenter.AboutUsPresenter;
import com.uibinder.index.client.presenter.AnnouncementPresenter;
import com.uibinder.index.client.presenter.CreatePresenter;
import com.uibinder.index.client.presenter.DndPresenter;
import com.uibinder.index.client.presenter.IndexPresenter;
import com.uibinder.index.client.presenter.LoadingPresenter;
import com.uibinder.index.client.presenter.PlanPresenter;
import com.uibinder.index.client.presenter.Presenter;
import com.uibinder.index.client.presenter.TopBarPresenter;
import com.uibinder.index.client.service.LoginService;
import com.uibinder.index.client.service.LoginServiceAsync;
import com.uibinder.index.client.service.SUNServiceAsync;
import com.uibinder.index.client.view.AboutUsViewImpl;
import com.uibinder.index.client.view.AnnouncementViewImpl;
import com.uibinder.index.client.view.CreateViewImpl;
import com.uibinder.index.client.view.DndViewImpl;
import com.uibinder.index.client.view.IndexViewImpl;
import com.uibinder.index.client.view.LoadingViewImpl;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.view.SiaSummaryViewImpl;
import com.uibinder.index.client.view.TopBarViewImpl;
import com.uibinder.index.shared.LoginInfo;
import com.uibinder.index.shared.RandomPhrase;
import com.uibinder.index.shared.control.Career;
import com.uibinder.index.shared.control.Plan;
import com.uibinder.index.shared.control.Student;

public class AppController implements Presenter, ValueChangeHandler<String> {

	// Creating all the views that will exist once for all to save them to let users go back to their view
	private IndexPresenter indexPresenter;
	private TopBarPresenter topBarPresenter;
	private DndPresenter dndPresenter;
	private CreatePresenter createPresenter;
	private PlanPresenter planPresenter;
	private AboutUsPresenter aboutUsPresenter;
	private AnnouncementPresenter announcementPresenter;
	private LoadingPresenter loadingPresenter;
	
	private IndexViewImpl indexView;
	private TopBarViewImpl topBarView;
	private DndViewImpl dndView;
	private CreateViewImpl createView;
	private PlanViewImpl planView;
	private AboutUsViewImpl aboutUsView;
	private AnnouncementViewImpl announcementView;
	private SiaSummaryViewImpl siaSummaryView;
	private LoadingViewImpl loadingView;
	
	//Taking care of the random phrase funtionality variables 
	private List<RandomPhrase> phrases;
	private RandomPhrase phrase;
	
	private final HandlerManager eventBus;
	private final SUNServiceAsync rpcService;
	private HasWidgets container;
	private String token;
	
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

			@Override
			public void onNewEmptyPlanButtonClicked(String careerCode) {
				generateEmptyPlan(careerCode);
			}
			
		});
	}
	
	private void genereteDefaultPlan(String careerCode){
		showLoadingPage();
		
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
		
		rpcService.getPlanDefault(careerCode, new AsyncCallback<Plan>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry we got a problem");
			}

			@Override
			public void onSuccess(Plan result) {
				Plan plan = result;
				planPresenter.setPlan(plan);
				History.newItem("plan");
			}
			
		});
	
	}
	
	private void generateEmptyPlan(String careerCode) {
		showLoadingPage();
		Window.alert("got it");
		/**
		 * use the rpcService.createEmptyPlan(careerCode);
		 */
		rpcService.getCareer(careerCode, new AsyncCallback<Career>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("sorry we got an error");
				History.newItem("create");
			}

			@Override
			public void onSuccess(Career result) {
			}
			
		});
		
	}
	
	private void generateAcademicHistoryFromString(String academicHistory){
		Window.alert("This should create an academic history from the string: " + academicHistory);
		//TODO 
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
			
			if(token.equals("dnd")){ //dnd stands for Drag and Drop
				if(dndView == null){
					dndView = new DndViewImpl();
				}
				if(dndPresenter == null){
					dndPresenter = new DndPresenter(rpcService, eventBus, dndView);
				}
				dndPresenter.go(RootPanel.get("centerArea"));					
			} else if(token.equals("create")){
				if(createView == null){
					createView = new CreateViewImpl();
				}
				if(createPresenter == null){
					createPresenter = new CreatePresenter(rpcService, eventBus, createView);					
				}
				if(student == null){					
					createPresenter.showWarning();
				}else{
					createPresenter.hideWarning();
				}
				createPresenter.go(RootPanel.get("centerArea"));
			} else if(token.equals("plan")) {
//				if(planView == null){
//					planView = new PlanViewImpl();
//					siaSummaryView = new SiaSummaryViewImpl();
//				}
//				if(planPresenter == null){
//					planPresenter = new PlanPresenter(rpcService, eventBus, planView, siaSummaryView, student);
//				}
				if(planView != null && planPresenter != null){
					planPresenter.go(RootPanel.get("centerArea"));
					if(student == null){
						siaSummaryView.showWarning();
					}else{
						siaSummaryView.hideWarning();						
					}
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
	
	private void showLoadingPage()
	{
		if(loadingView == null){
			loadingView = new LoadingViewImpl();
		}
		if(loadingPresenter == null){
			loadingPresenter = new LoadingPresenter(rpcService, eventBus, loadingView);
			loadingPresenter.setLabel("hola no sé que estoy escribiendo, bla bla bla, espero pasar de linea cuando esto sea muy largo");
		}
		loadingPresenter.go(RootPanel.get("centerArea"));
	}
	
	private void savePlanAsDefault(Plan plan) {
		rpcService.savePlanAsDefault(student, plan, savedPlanAsDefault);
	}
	
}
