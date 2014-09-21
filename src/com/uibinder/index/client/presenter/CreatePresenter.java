package com.uibinder.index.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.uibinder.index.client.event.GenerateAcademicHistoryFromStringEvent;
import com.uibinder.index.client.view.CreateView;
import com.uibinder.index.client.view.CreateViewImpl;

public class CreatePresenter implements Presenter, CreateView.Presenter {
	private final HandlerManager eventBus;
	private CreateViewImpl view;
	
	public CreatePresenter(HandlerManager eventBus, CreateViewImpl view){
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		addCareersToListBox();
	}
	
	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	private void addCareersToListBox() {
		view.addCareerToListBox("Ingeniería de sistemas y computación","1");
		view.addCareerToListBox("Administración de empresas","2");
	}

	@Override
	public void onResetButtonClicked() {
		view.clearTextBoxCreate();
	}

	@Override
	public void onListBoxCreateChange(String career) {
		view.setTextOfContinueDefaultButton("Cargar plan de " + career);
	}

	@Override
	public void onContinueButtonClicked(String academicHistory) {
		eventBus.fireEvent(new GenerateAcademicHistoryFromStringEvent(academicHistory));
	}

}
