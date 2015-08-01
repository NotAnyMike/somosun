package com.uibinder.client.index.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.client.index.event.GradeChangeEvent;
import com.uibinder.client.index.presenter.PlanPresenter;
import com.uibinder.shared.control.Subject;
import com.uibinder.shared.control.SubjectValue;

public class SubjectWidget extends FlowPanel implements HasClickHandlers {
	
	private static final String ENTER_HTMLCODE = "&#xA;";//"&#013;";
	
	private static final String[] TYPENESS = {"N", "F", "D", "L", "P", "-"};
	private static final String[] TYPENESS_STYLE = {"nivelacion", "fundamentacion", "disciplinar", "libreEleccion", "otra", "otra"};
	private static final String[] TYPENESS_NAMES = { "Nivelación", "Fundamentación", "Disciplinar", "Libre Elección", "Otra", "Otra" };

	private PlanPresenter presenter = null;
	
	private String publicId = null;
	private boolean selected = false;
	// private String name = null;
	// private String code = null;
	// private boolean obligatoriness = false;
	// private int credits = 0;
	// private double grade = 0.0;
	// private boolean approved = false; //it could happen that it took the
	// class and fail it
	// private boolean taken = false; //because to calculate the credits
	// requirement I need to know if it was(not) taken and (not) passed/failed
	// private int type = 0;
	// private String subjectGroup = null;
	
	private FlowPanel bottomPart = new FlowPanel();
	private Label nameLabel = new Label();
	private Label codeLabel = new Label();
	private Label creditsLabel = new Label();
	private Label obligatorinessLabel = new Label();
	private Label gradeLabel = new Label();
	private Label typeLabel = new Label();
	private TextBox textBoxGrade = new TextBox();
	
	private List<SubjectWidget> preList = new ArrayList<SubjectWidget>();
	private List<SubjectWidget> posList = new ArrayList<SubjectWidget>();
	private List<SubjectWidget> coList = new ArrayList<SubjectWidget>();
	
	/**
	 * Type = 0 Leveling, 1 Foundations, 2 Disciplinary, 3 Free Election, 4 Add to post-grade.
	 * if it comes with grade means that the class was taken (= true)
	 * @param name
	 * @param code
	 * @param credits
	 * @param grade
	 * @param obligatoriness
	 * @param type
	 */
	public SubjectWidget(String name, String code, int credits, boolean obligatoriness, int type, String publicId, PlanPresenter presenter){
		
		this.setName(name);
		this.setCode(code);
		this.setCredits(credits);
		this.setGrade(null);
		this.setObligatoriness(obligatoriness);
		this.setType(type);
		this.setPublicId(publicId);
		this.presenter = presenter;
		
		//setAttributes();
		
		joinWidgets();
	}
	
	public SubjectWidget(String name, String code, int credits, boolean obligatoriness, String type, String publicId, String subjectGroup, PlanPresenter presenter){
		this.setName(name);
		this.setCode(code);
		this.setCredits(credits);
		this.setGrade(null);
		this.setObligatoriness(obligatoriness);
		this.setType(getTypeFromString(type));
		this.setPublicId(publicId);
		this.setSubjectGroup(subjectGroup);
		this.presenter = presenter;
		
		//setAttributes();
		
		joinWidgets();
	}
		
	/**
	 * TODO: fix the obligatoriness
	 * @param s
	 */
	public SubjectWidget(Subject s, SubjectValue sV, PlanPresenter presenter){
		this.setName(s.getName());
		this.setCode(s.getCode());
		this.setCredits(s.getCredits());
		this.setGrade(null);
		this.setObligatoriness(sV.getComplementaryValues().isMandatory());
		this.setType(getTypeFromString(sV.getComplementaryValues().getTypology()));
		this.setPublicId(sV.getSubjectValuesPublicId());
		this.presenter = presenter;
		
		//setAttributes();
		
		joinWidgets();
	}

	/**
	 * will return: <br>
	 * </br> p -> 0 <br>
	 * </br> b -> 1 <br>
	 * </br> c -> 2 <br>
	 * </br> l -> 3 <br>
	 * </br>
	 * 
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4
	 * Añadir para posgrado <br>
	 * </br>
	 * 
	 * @param typology
	 * @return
	 */
	private int getTypeFromString(String typology) {
		if(typology == null){
			GWT.debugger();
		}
		typology = typology.toLowerCase();
		int i = 0;
		switch(typology)
		{
		case "b":
			i = 1;
			break;
		case "c":
			i = 2;
			break;
		case "l":
			i = 3;
			break;
		}
		return i;
	}
	
	private void joinWidgets() {
		
		AddStyles();
		
		textBoxGrade.getElement().setAttribute("placeholder","3.9");
		
		textBoxGrade.setVisible(false);
		bottomPart.add(textBoxGrade);
		bottomPart.add(gradeLabel);
		bottomPart.add(creditsLabel);
		bottomPart.add(obligatorinessLabel);
		bottomPart.add(typeLabel);
		
		this.add(codeLabel);	
		this.add(nameLabel);
		this.add(bottomPart);
		
		addHandlers();
		
	}
	
	private void addHandlers() {
		
		gradeLabel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				gradeLabel.setVisible(false);
				textBoxGrade.setVisible(true);
				textBoxGrade.setFocus(true);
				setTextOfGradeText((gradeLabel.getText().equals("-") ? "" : gradeLabel.getText()));
				event.stopPropagation();
			}
		});
		
		textBoxGrade.addBlurHandler(new BlurHandler(){

			@Override
			public void onBlur(BlurEvent event) {
				textBoxGrade.setVisible(false);
				gradeLabel.setVisible(true);
				if(getTextOfGradeTextBox().isEmpty() == false){					
					presenter.onGradeAdded(publicId, getTextOfGradeTextBox());
				}
			}
			
		});
		
		textBoxGrade.addKeyUpHandler(new KeyUpHandler(){

			@Override
			public void onKeyUp(KeyUpEvent event) {
				int key = event.getNativeKeyCode();
				if(key == KeyCodes.KEY_ENTER){
					textBoxGrade.setFocus(false);
				}
			}
			
		});

	}

	private void AddStyles() {
		this.addStyleName("subjectBox");
		
		codeLabel.addStyleName("topSubjectBox");
		
		nameLabel.addStyleName("mainSubjectBox");
		nameLabel.addStyleName("wrapword");
		
		bottomPart.addStyleName("bottomSubjectBox");
		
		gradeLabel.addStyleName("gradeBottomSubjectBox");
		
		creditsLabel.addStyleName("creditsBottomSubjectBox");
		
		obligatorinessLabel.addStyleName("obligBottomSubjectBox");
		
		typeLabel.addStyleName("typeBottomSubjectBox");
		int type = Integer.valueOf(this.getElement().getAttribute("type"));
		typeLabel.addStyleName(TYPENESS_STYLE[((type > 4) ? 4 : type)]);
		
		textBoxGrade.setStyleName("textBoxGrade");
		textBoxGrade.setTitle("Enter o click por fuera para guardar");
		
		codeLabel.getElement().setAttribute("data-placement", "top");
		codeLabel.getElement().setAttribute("data-toggle", "tooltip");
		codeLabel.getElement().setAttribute("data-container", "body");
		
		gradeLabel.getElement().setAttribute("data-placement", "bottom");
		gradeLabel.getElement().setAttribute("data-toggle", "tooltip");
		gradeLabel.getElement().setAttribute("data-container", "body");
		
		creditsLabel.getElement().setAttribute("data-placement", "bottom");
		creditsLabel.getElement().setAttribute("data-toggle", "tooltip");
		creditsLabel.getElement().setAttribute("data-container", "body");
		
		obligatorinessLabel.getElement().setAttribute("data-placement", "bottom");
		obligatorinessLabel.getElement().setAttribute("data-toggle", "tooltip");
		obligatorinessLabel.getElement().setAttribute("data-container", "body");
		
		typeLabel.getElement().setAttribute("data-placement", "bottom");
		typeLabel.getElement().setAttribute("data-toggle", "tooltip");
		typeLabel.getElement().setAttribute("data-container", "body");
		
		textBoxGrade.getElement().setAttribute("data-placement", "bottom");
		textBoxGrade.getElement().setAttribute("data-toggle", "tooltip");
		textBoxGrade.getElement().setAttribute("data-container", "body");
		
	}
	
	public Widget getNameLabel(){
		return nameLabel.asWidget();
	}
	
	public Widget getCodeLabel(){
		return codeLabel.asWidget();
	}
	
	public Widget getCreditsLabel(){
		return creditsLabel.asWidget();
	}
	
	public Widget getGradeLabel(){
		return gradeLabel.asWidget();
	}
	
	public Widget getObligatorinessLabel(){
		return obligatorinessLabel.asWidget();
	}
	
	public Widget getTypeLabel(){
		return typeLabel.asWidget();
	}
	
	public String getCredits() {
		return creditsLabel.getText();
	}
	
	public void addPre(SubjectWidget subject){
		if(preList.contains(subject)==false){
			preList.add(subject);
		}
	}
	
	public void deletePre(SubjectWidget subject){
		if(preList.contains(subject)==true){
			preList.remove(subject);
		}
	}

	public void addPos(SubjectWidget subject){
		if(posList.contains(subject)==false){
			posList.add(subject);
		}
	}
	
	public void deletePos(SubjectWidget subject){
		if(posList.contains(subject)==true){
			posList.remove(subject);
		}
	}
	
	public void addCo(SubjectWidget subject){
		if(coList.contains(subject)==false){
			coList.add(subject);
		}
	}
	
	public void deleteCo(SubjectWidget subject){
		if(coList.contains(subject)==true){
			coList.remove(subject);
		}
	}
	
	public List<SubjectWidget> getPreList(){
		return preList;
	}
	
	public List<SubjectWidget> getCoList(){
		return coList;
	}
	
	public List<SubjectWidget> getPosList(){
		return posList;
	}
	
	public String getName(){
		return nameLabel.getText();
	}
	
	public String getCode(){
		return codeLabel.getText();
	}
	
	public String getGrade() {
		return gradeLabel.getText();
	}
	
	public String isOblig() {
		return obligatorinessLabel.getText();
	}
	
	/**
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4
	 * Añadir para posgrado
	 */
	public String getType() {
		return typeLabel.getText();
	}

	public String getPublicId() {
		return publicId;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	        return addDomHandler(handler, ClickEvent.getType());
	    }

	public void setSubjectGroup(String subjectGroup) {
		setCodeLabel(subjectGroup);
	}
	
	public void setCodeLabel(String s){
		codeLabel.setTitle(s);
	}

	public void setName(String name) {
		// this.name = name;
		this.getElement().setAttribute("name", name);
		nameLabel.setText(name);
	}

	public void setCode(String code) {
		// this.code = code;
		this.getElement().setAttribute("code", code);
		codeLabel.setText(code);
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
		this.getElement().setAttribute("publicId", "" + publicId);
	}

	public String isObligatoriness() {
		return this.obligatorinessLabel.getText();
	}

	public void setObligatoriness(boolean obligatoriness) {
		// this.obligatoriness = obligatoriness;
		this.getElement().setAttribute("obligatoriness",
				Boolean.toString(obligatoriness));
		obligatorinessLabel.setText("oblig: "
				+ ((obligatoriness == true) ? "Si" : "No"));
		obligatorinessLabel.setTitle("Es obligatoria: "
				+ ((obligatoriness == true) ? "Si" : "No"));
	}

	public void setCredits(int credits) {
		// this.credits = credits;
		this.getElement().setAttribute("credits", Integer.toString(credits));
		creditsLabel.setText("c: " + Integer.toString(credits));
		creditsLabel.setTitle("créditos: " + credits);
	}

	public void setGrade(String grade) {
		this.getElement().setAttribute("grade", (grade == null ? "" :grade));

		String title = null;

		if (grade == null) {
			grade = "-";
			title = "Nota no registrada, para registrarla haz click";
		} else if(grade.equals("AP")){
			title = "nota de la clase: Aprobada";
		} else if(grade.equals("NA")){
			title = "nota de la clase: No Aporbada";
		} else{
			title = "nota de la clase: " + grade;
		}
		
		gradeLabel.setText(grade);
		gradeLabel.setTitle(title);		
	}

	public void setType(int type) {
		// this.type = type;
		this.getElement().setAttribute("type", Integer.toString(type));
		typeLabel.setText((type > 4) ? "-" : TYPENESS[type]);
		typeLabel.setTitle("Tipología: " + TYPENESS_NAMES[((type > 4) ? 4 : type)]);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setTextOfGradeText(String s){
		textBoxGrade.setText(s);
	}
	
	public String getTextOfGradeTextBox(){
		return textBoxGrade.getText();
	}

}
