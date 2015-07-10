package com.uibinder.index.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.uibinder.index.shared.control.Subject;
import com.uibinder.index.shared.control.SubjectValues;

public class SubjectWidget extends FlowPanel implements HasClickHandlers{
	
	private static final String ENTER_HTMLCODE = "&#xA;";//"&#013;";
	
	private static final String[] TYPENESS = {"N", "F", "D", "L", "P", "-"};
	private static final String[] TYPENESS_STYLE = {"nivelacion", "fundamentacion", "disciplinar", "libreEleccion", "otra", "otra"};
	private static final String[] TYPENESS_NAMES = {"Nivelación", "Fundamentación", "Disciplinar", "Libre Elección", "Otra", "Otra"};
	
	private String name = null;
	private String code = null;
	private String publicId = null;
	private boolean obligatoriness = false;
	private int credits = 0;
	private double grade = 0.0;
	private boolean approved = false; //it could happen that it took the class and fail it
	private boolean taken = false; //because to calculate the credits requirement I need to know if it was(not) taken and (not) passed/failed  
	private int type = 0;
	private String subjectGroup = null;
	private boolean selected = false;
	
	private FlowPanel bottomPart = new FlowPanel();
	private Label nameLabel = new Label();
	private Label codeLabel = new Label();
	private Label creditsLabel = new Label();
	private Label obligatorinessLabel = new Label();
	private Label gradeLabel = new Label();
	private Label typeLabel = new Label();
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
	public SubjectWidget(String name, String code, int credits, double grade, boolean obligatoriness, int type, String publicId){
		this.setName(name);
		this.setCode(code);
		this.setCredits(credits);
		this.setGrade(grade);
		this.setApproved(((grade >= 3) ? true : false));
		this.setTaken(false);
		this.setObligatoriness(obligatoriness);
		this.setType(type);
		this.setPublicId(publicId);
		
		//setAttributes();
		
		joinWidgets();
	}
	
	public SubjectWidget(String name, String code, int credits, double grade, boolean obligatoriness, String type, String publicId, String subjectGroup){
		this.setName(name);
		this.setCode(code);
		this.setCredits(credits);
		this.setGrade(grade);
		this.setApproved(((grade >= 3) ? true : false));
		this.setTaken(false);
		this.setObligatoriness(obligatoriness);
		this.setType(getTypeFromString(type));
		this.setPublicId(publicId);
		this.setSubjectGroup(subjectGroup);
		
		//setAttributes();
		
		joinWidgets();
	}
	
	/**
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4 Añadir para posgrado
	 * @param name
	 * @param code
	 * @param credits
	 * @param obligatoriness
	 * @param type
	 */
	public SubjectWidget(String name, String code, int credits, boolean obligatoriness, int type, String publicId){
		this.setName(name);
		this.setCode(code);
		this.setCredits(credits);
		this.setGrade(0.0);
		this.setApproved(false);
		this.setTaken(false);
		this.setObligatoriness(obligatoriness);
		this.setType(type);
		this.setPublicId(publicId);
		
		//setAttributes();
		
		joinWidgets();
	}
	
	/**
	 * TODO: fix the obligatoriness
	 * @param s
	 */
	public SubjectWidget(Subject s, SubjectValues sV){
		this.setName(s.getName());
		this.setCode(s.getCode());
		this.setCredits(s.getCredits());
		this.setGrade(0.0);
		this.setApproved(true);
		this.setTaken(sV.isTaken());
		this.setObligatoriness(sV.getComplementaryValues().isMandatory());
		this.setType(getTypeFromString(sV.getComplementaryValues().getTypology()));
		this.setPublicId(sV.getSubjectValuesPublicId());
		
		//setAttributes();
		
		joinWidgets();
	}

	/**
	 * will return: <br></br>
	 * p -> 0 <br></br>
	 * b -> 1 <br></br> 
	 * c -> 2 <br></br>
	 * l -> 3 <br></br>
	 * 
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4 Añadir para posgrado
	 * <br></br>
	 * 
	 * @param typology
	 * @return
	 */
	private int getTypeFromString(String typology) {
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
		
		bottomPart.add(gradeLabel);
		bottomPart.add(creditsLabel);
		bottomPart.add(obligatorinessLabel);
		bottomPart.add(typeLabel);
		
		this.add(codeLabel);	
		this.add(nameLabel);
		this.add(bottomPart);
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
		typeLabel.addStyleName(TYPENESS_STYLE[((getType()>4) ? 4 : getType())]);
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
	
	public String getName(){
		return name;
	}
	
	public String getCode(){
		return code;
	}
	
	public boolean getTaken(){
		return isTaken();
	}
	
	public boolean getApproved(){
		return isApproved();
	}
	
	public double getGrade(){
		return grade;
	}
	
	public boolean isOblig(){
		return isObligatoriness();
	}
	
	/**
	 * Type = 0 Nivelación, 1 Fundamentación, 2 Disiplinar, 3 libre elección, 4 Añadir para posgrado
	 */
	public int getType(){
		return type;
	}

	public String getPublicId() {
		return publicId;
	}

	@Override
	public HandlerRegistration addClickHandler(
	        ClickHandler handler)
	    {
	        return addDomHandler(handler, ClickEvent.getType());
	    }

	public String getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(String subjectGroup) {
		this.subjectGroup = subjectGroup;
		codeLabel.setTitle("Agrupación: " + subjectGroup);
	}

	public void setName(String name) {
		this.name = name;
		this.getElement().setAttribute("name", getName());
		nameLabel.setText(name);
	}

	public void setCode(String code) {
		this.code = code;
		this.getElement().setAttribute("code", getCode());
		codeLabel.setText(code);
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
		this.getElement().setAttribute("publicId", "" + getPublicId());
	}

	public boolean isObligatoriness() {
		return obligatoriness;
	}

	public void setObligatoriness(boolean obligatoriness) {
		this.obligatoriness = obligatoriness;
		this.getElement().setAttribute("obligatoriness", Boolean.toString(isObligatoriness()));
		obligatorinessLabel.setText("oblig: " + ((isObligatoriness() == true) ? "Si" : "No"));
		obligatorinessLabel.setTitle("Es obligatoria: " + ((isObligatoriness() == true) ? "Si" : "No"));
	}

	public void setCredits(int credits) {
		this.credits = credits;
		this.getElement().setAttribute("credits", Integer.toString(getCredits()));
		creditsLabel.setText("c: " + Integer.toString(getCredits()));
		creditsLabel.setTitle("créditos: " + getCredits());
	}

	public void setGrade(double grade) {
		this.grade = grade;
		this.getElement().setAttribute("grade", Double.toString(getGrade()));
		gradeLabel.setText(isTaken() == false ? "-" : Double.toString(getGrade()));
		gradeLabel.setTitle((getGrade()==0) ? "Nota no registrada" : "nota de la clase: " + getGrade());
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
		this.getElement().setAttribute("approved", ((isApproved() == true) ? "1" : "0"));
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
		this.getElement().setAttribute("taken", ((isTaken() == true) ? "1" : "0"));
	}

	public void setType(int type) {
		this.type = type;
		this.getElement().setAttribute("type", Integer.toString(getType()));
		typeLabel.setText((getType()>4) ? "-" : TYPENESS[getType()]);
		typeLabel.setTitle("Tipología: " + TYPENESS_NAMES[((getType()>4) ? 4 : getType())]);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
