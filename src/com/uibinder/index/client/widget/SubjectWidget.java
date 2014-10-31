package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SubjectWidget extends FlowPanel {
	
	private static final String[] TYPENESS = {"N", "B", "C", "L", "P", "-"};
	private static final String[] TYPENESS_STYLE = {"nivelacion", "fundamentacion", "disciplinar", "libreEleccion", "otra", "otra"};
	private static final String[] TYPENESS_NAMES = {"Nivelación", "Fundamentación", "Disciplinar", "Libre Elección", "Otra", "Otra"};
	
	private String name = null;
	private String code = null;
	private boolean obligatoriness = false;
	private int credits = 0;
	private double grade = 0.0;
	private int type = 0;
	private boolean selected = false;
	
	private FlowPanel bottomPart = new FlowPanel();
	private Label obligatorinessLabel;
	private Label codeLabel;
	private Label nameLabel;
	private Label creditsLabel;
	private Label gradeLabel;
	private Label typeLabel;
	private List<SubjectWidget> preList = new ArrayList<SubjectWidget>();
	private List<SubjectWidget> posList = new ArrayList<SubjectWidget>();
	private List<SubjectWidget> coList = new ArrayList<SubjectWidget>();
	
	/*
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4 Añadir para posgrado
	 */
	public SubjectWidget(String name, String code, int credits, double grade, boolean obligatoriness, int type){
		this.name = name;
		this.code = code;
		this.credits = credits;
		this.grade = grade;
		this.obligatoriness = obligatoriness;
		this.type = type;
		
		setAttributes();
		
		createWidget();
	}
	
	public SubjectWidget(String name, String code, int credits, boolean obligatoriness, int type){
		this.name = name;
		this.code = code;
		this.credits = credits;
		this.grade = 0.0;
		this.obligatoriness = obligatoriness;
		this.type = type;
		
		setAttributes();
		
		createWidget();
	}

	/**
	 * This method adds the attributes to the HTML code to be read later on by the dnd controllers
	 */
	private void setAttributes() {
		
		this.getElement().setAttribute("name", name);
		this.getElement().setAttribute("code", code);
		this.getElement().setAttribute("Credits", Integer.toString(credits));
		this.getElement().setAttribute("grade", Double.toString(grade));
		this.getElement().setAttribute("obligatoriness", Boolean.toString(obligatoriness));
		this.getElement().setAttribute("type", Integer.toString(type));
		
	}

	private void createWidget() {
		nameLabel = new Label(name);
		codeLabel = new Label(code);
		creditsLabel = new Label("cr: " + Integer.toString(credits));
		gradeLabel = new Label((grade == 0) ? "-" : Double.toString(grade));
		obligatorinessLabel = new Label("oblig: " + ((obligatoriness == true) ? "Si" : "No"));
		
		typeLabel = new Label((type>4) ? "-" : TYPENESS[type]);
		
		addTitles();
		
		AddStyles();
		
		bottomPart.add(gradeLabel);
		bottomPart.add(creditsLabel);
		bottomPart.add(obligatorinessLabel);
		bottomPart.add(typeLabel);
		
		this.add(codeLabel);	
		this.add(nameLabel);
		this.add(bottomPart);
	}
	
	private void addTitles() {
		
		typeLabel.setTitle("Tipología: " + TYPENESS_NAMES[((type>4) ? 4 : type)]);
		obligatorinessLabel.setTitle("Es obligatoria: " + ((obligatoriness == true) ? "Si" : "No"));
		creditsLabel.setTitle("créditos: " + credits);
		gradeLabel.setTitle((grade==0) ? "Nota no registrada" : "nota de la clase: " + grade);
	
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
		typeLabel.addStyleName(TYPENESS_STYLE[((type>4) ? 4 : type)]);
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
	public int getCredits() {
		return credits;
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
	
	public String getCode(){
		return code;
	}
}
