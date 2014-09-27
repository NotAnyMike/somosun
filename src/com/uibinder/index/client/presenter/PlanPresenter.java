package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.xml.internal.ws.api.server.Container;
import com.uibinder.index.client.view.PlanView;
import com.uibinder.index.client.view.PlanViewImpl;
import com.uibinder.index.client.widget.PlanWidget;
import com.uibinder.index.client.widget.SubjectWidget;

public class PlanPresenter implements Presenter, PlanView.Presenter {
	private final HandlerManager eventBus;
	private PlanViewImpl view;
	private PlanWidget plan;
	
	private VerticalPanel subContainer = new VerticalPanel();
	
	public PlanPresenter(HandlerManager eventBus, PlanViewImpl view){
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		
		if(plan == null) init();
	}
	
	private void init() {
		plan = new PlanWidget();
		plan.addSemester();
		
		/************* to remove later on **************/
		//It is just for design purposes
		
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		plan.addSemester();
		SubjectWidget subject1 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject2 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject3 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
		SubjectWidget subject4 = new SubjectWidget("Introducciónallavidasocialdemamertos peruanos del siglo XI","0000r42",0,true,1);
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
		plan.addSubject(0, subject1);
		plan.addSubject(0, subject14);
		plan.addSubject(0, subject2);
		plan.addSubject(0, subject3);
		plan.addSubject(1, subject4);
		plan.addSubject(2, subject5);
		plan.addSubject(3, subject6);
		plan.addSubject(4, subject7);
		plan.addSubject(5, subject8);
		plan.addSubject(6, subject9);
		plan.addSubject(7, subject10);
		plan.addSubject(8, subject11);
		plan.addSubject(9, subject12);
		plan.addSubject(10, subject13);
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		addWidgets(container);
		
	}

	private void addWidgets(HasWidgets container) {
		
		container.add(view.asWidget());
		
		subContainer.addStyleName("subContainerForPlan");
		subContainer.add(plan);
		
		subContainer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		
		container.add(subContainer);
	}

}
