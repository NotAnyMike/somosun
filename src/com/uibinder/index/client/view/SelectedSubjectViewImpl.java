package com.uibinder.index.client.view;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

public class SelectedSubjectViewImpl extends Composite implements SelectedSubjectView {
	
	Presenter presenter;
	
	@UiField Button toRemoveButton;
	@UiField InlineHTML subjectSelectedLabel;

	private static selectedSubjectUiBinder uiBinder = GWT
			.create(selectedSubjectUiBinder.class);

	@UiTemplate("SelectedSubjectView.ui.xml")
	interface selectedSubjectUiBinder extends UiBinder<Widget, SelectedSubjectViewImpl> {
	}

	public SelectedSubjectViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		fill();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setTexts(String name, String code, String careerCode) {
		
		subjectSelectedLabel.setText(name);
		subjectSelectedLabel.getElement().setAttribute("name", name);
		subjectSelectedLabel.getElement().setAttribute("code", code);
		subjectSelectedLabel.getElement().setAttribute("career", careerCode);
		
	}
	
	private void fill() {
		
		toRemoveButton.setIcon(IconType.TIMES);
		toRemoveButton.setType(ButtonType.DANGER);
		toRemoveButton.setSize(ButtonSize.EXTRA_SMALL);
		toRemoveButton.getElement().setAttribute("style", "margin-bottom:3px;");
		toRemoveButton.setTitle("deseleccionar");
		
	}
	
	@Override
	public String getCode() {
		return subjectSelectedLabel.getElement().getAttribute("code");
	}
	
	@Override
	public void remove() {
		this.removeFromParent();
	}
	
	/********** EVENT HANDLERS *************/
	
	@UiHandler("toRemoveButton")
	public void onToRemoveButtonClicked(ClickEvent event){
		
		String name = subjectSelectedLabel.getElement().getAttribute("name");
		String code = subjectSelectedLabel.getElement().getAttribute("code");
		String career = subjectSelectedLabel.getElement().getAttribute("career");
		
		presenter.onSpecificSubjectUnselected(name, code, career);
		
	}

}
