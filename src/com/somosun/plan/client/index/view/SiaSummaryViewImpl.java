package com.somosun.plan.client.index.view;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.VerticalButtonGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SiaSummaryViewImpl extends Composite implements SiaSummaryView {
	
	private Presenter presenter;
	@UiField Label gpaLabel;
	@UiField Label avanceLabel;
	@UiField Label approvedLabel;
	@UiField Label aditionalLabel;
	@UiField Label foundationCreditsApprovedLabel;
	@UiField Label foundationCreditsNecessaryLabel;
	@UiField Label foundationCreditsPerCentLabel;
	@UiField Label disciplinaryCreditsApprovedLabel;
	@UiField Label disciplinaryCreditsNecessaryLabel;
	@UiField Label disciplinaryCreditsPerCentLabel;
	@UiField Label freeElectionCreditsApprovedLabel;
	@UiField Label freeElectionCreditsNecessaryLabel;
	@UiField Label freeElectionCreditsPerCentLabel;
	@UiField Label levelingCreditsApprovedLabel;
	@UiField Label levelingCreditsNecessaryLabel;
	@UiField Label levelingCreditsPerCentLabel;
	@UiField Label totalApprovedLabel;
	@UiField Label totalNecessaryLabel;
	@UiField Label totalPerCentLabel;
	
	@UiField HTMLPanel htmlPanelWarning;
	
	@UiField VerticalButtonGroup verticalButtonGroupMenu;
	@UiField ButtonGroup adminButtonsButtonGroup;
	
	@UiField AnchorListItem savePlanAsDefaultButton;
	@UiField AnchorListItem addMandatorySubjectsButton;
	
	@UiField AnchorButton savePlanNameButton;
	@UiField AnchorButton newPlanButton;
	@UiField AnchorButton deletePlanButton;

	@UiTemplate("SiaSummaryView.ui.xml")
	interface SiaSummaryViewUiBinder extends UiBinder<Widget, SiaSummaryViewImpl> {}
	private static SiaSummaryViewUiBinder uiBinder = GWT
			.create(SiaSummaryViewUiBinder.class);

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public SiaSummaryViewImpl(){
		initWidget(uiBinder.createAndBindUi(this));
		
		newPlanButton.getElement().setAttribute("style", "color:#000;");
	}
	
	@Override
	public Widget asWidget(){
		return this;
	}

	@Override
	public void setGPA(String x) {
		gpaLabel.setText(x);
	}

	@Override
	public void setAvance(String x) {
		avanceLabel.setText(x);
	}

	@Override
	public void setApprovedCredits(String x) {
		approvedLabel.setText(x);
	}

	@Override
	public void setAdditionalyCredits(String x) {
		aditionalLabel.setText(x);
	}

	@Override
	public void setFoundationCredits(String approved, String necessary, String percentage) {
		foundationCreditsApprovedLabel.setText(approved);
		foundationCreditsNecessaryLabel.setText(necessary);
		foundationCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setDisciplinaryCredits(String approved, String necessary, String percentage) {
		disciplinaryCreditsApprovedLabel.setText(""+approved);
		disciplinaryCreditsNecessaryLabel.setText(""+necessary);
		disciplinaryCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setFreeElectionCredits(String approved, String necessary, String percentage) {
		freeElectionCreditsApprovedLabel.setText(approved);
		freeElectionCreditsNecessaryLabel.setText(necessary);
		freeElectionCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setLevelingCredits(String approved, String necessary, String percentage) {
		levelingCreditsApprovedLabel.setText(approved);
		levelingCreditsNecessaryLabel.setText(necessary);
		levelingCreditsPerCentLabel.setText(percentage);
	}
	@Override
	public void setFoundationCredits(String approved, String percentage) {
		foundationCreditsApprovedLabel.setText(approved);
		foundationCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setDisciplinaryCredits(String approved, String percentage) {
		disciplinaryCreditsApprovedLabel.setText(""+approved);
		disciplinaryCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setFreeElectionCredits(String approved, String percentage) {
		freeElectionCreditsApprovedLabel.setText(approved);
		freeElectionCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setLevelingCredits(String approved, String percentage) {
		levelingCreditsApprovedLabel.setText(approved);
		levelingCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void setTotalApproved(String x) {
		totalApprovedLabel.setText(x);
	}

	@Override
	public void setTotalNecessary(String x) {
		totalNecessaryLabel.setText(x);
	}

	@Override
	public void setTotalPerCent(String x) {
		totalPerCentLabel.setText(x);
	}

	public void setDefaultFreeElectionCredits(String defaultCredits) {
		freeElectionCreditsNecessaryLabel.setText("" + defaultCredits);
	}
	
	public void setDefaultLevelingCredits(String defaultCredits) {
		levelingCreditsNecessaryLabel.setText(defaultCredits);
	}
	
	public void setDefaultFoundationCredits(String defaultCredits) {
		foundationCreditsNecessaryLabel.setText(defaultCredits);
	}
	
	public void setDefaultDisciplinaryCredits(String defaultCredits) {
		disciplinaryCreditsNecessaryLabel.setText(defaultCredits);
	}
	
	public void setApprovedFreeElectionCredits(String defaultCredits) {
		freeElectionCreditsApprovedLabel.setText(defaultCredits);
	}
	
	public void setApprovedLevelingCredits(String defaultCredits) {
		levelingCreditsApprovedLabel.setText(defaultCredits);
	}
	
	public void setApprovedFoundationCredits(String defaultCredits) {
		foundationCreditsApprovedLabel.setText(defaultCredits);
	}
	
	public void setApprovedDisciplinaryCredits(String defaultCredits) {
		disciplinaryCreditsApprovedLabel.setText(defaultCredits);
	}
	
	public void setPercentageDisciplinaryCredits(String percentage) {
		disciplinaryCreditsPerCentLabel.setText(percentage);
	}

	public void setPercentageFreeElectionCredits(String percentage) {
		freeElectionCreditsPerCentLabel.setText(percentage);
	}
	
	public void setPercentageFoundationCredits(String percentage) {
		foundationCreditsPerCentLabel.setText(percentage);
	}
	
	public void setPercentageLevelingCredits(String percentage) {
		levelingCreditsPerCentLabel.setText(percentage);
	}

	@Override
	public void deleteAdminButtons() {
		adminButtonsButtonGroup.removeFromParent();
	}
	
	@Override
	public void showWarning() {
		htmlPanelWarning.asWidget().setVisible(true);
		verticalButtonGroupMenu.asWidget().setVisible(false);
	}

	@Override
	public void hideWarning() {
		htmlPanelWarning.asWidget().setVisible(false);
		verticalButtonGroupMenu.asWidget().setVisible(true);
	}
	
	/***************** Handlers *********************/
	
	@UiHandler("savePlanAsDefaultButton")
	public void onSavePlanAsDefaultClicked(ClickEvent event){
		presenter.onSavePlanAsDefaultClicked();
	}
	
	@UiHandler("addMandatorySubjectsButton")
	public void onAddMandatorySubjectsButton(ClickEvent event){
		presenter.onAddMandatorySubjectsButton();
	}

	@UiHandler("savePlanNameButton")
	public void onSavePlanClick(ClickEvent e) {
		presenter.showSavePlanPopup();
	}
	
	@UiHandler("deletePlanButton")
	public void onDeletePlanButtonClicked(ClickEvent e){
		presenter.onDeletePlanButtonClicked();
	}

}