package com.uibinder.index.client.view;

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
	public void setGPA(double x) {
		gpaLabel.setText(""+x);
	}

	@Override
	public void setAvance(int x) {
		avanceLabel.setText(x*100 + "%");
	}

	@Override
	public void setApprovedCredits(int x) {
		approvedLabel.setText(""+x);
	}

	@Override
	public void setAdditionalyCredits(int x) {
		aditionalLabel.setText("" + x);
	}

	@Override
	public void setFoundationCredits(int approved, int necessary) {
		foundationCreditsApprovedLabel.setText(""+approved);
		foundationCreditsNecessaryLabel.setText(""+necessary);
		foundationCreditsPerCentLabel.setText(approved/necessary*100+"%");
	}

	@Override
	public void setDisciplinaryCredits(int approved, int necessary) {
		disciplinaryCreditsApprovedLabel.setText(""+approved);
		disciplinaryCreditsNecessaryLabel.setText(""+necessary);
		disciplinaryCreditsPerCentLabel.setText(approved/necessary*100+"%");
	}

	@Override
	public void setFreeElectionCredits(int approved, int necessary) {
		freeElectionCreditsApprovedLabel.setText(""+approved);
		freeElectionCreditsNecessaryLabel.setText(""+necessary);
		freeElectionCreditsPerCentLabel.setText(approved/necessary*100+"%");
	}

	@Override
	public void setLevelingCredits(int approved, int necessary) {
		levelingCreditsApprovedLabel.setText(""+approved);
		levelingCreditsNecessaryLabel.setText(""+necessary);
		levelingCreditsPerCentLabel.setText(approved/necessary*100+"%");
	}

	@Override
	public void setTotalApproved(int x) {
		totalApprovedLabel.setText("" + x);
	}

	@Override
	public void setTotalNecessary(int x) {
		totalNecessaryLabel.setText(""+ x);
	}

	@Override
	public void setTotalPerCent(int x) {
		totalPerCentLabel.setText(x + "%");
	}

	public void setDefaultFreeElectionCredits(int defaultCredits) {
		freeElectionCreditsNecessaryLabel.setText("" + defaultCredits);
	}
	
	public void setDefaultLevelingCredits(int defaultCredits) {
		levelingCreditsNecessaryLabel.setText("" + defaultCredits);
	}
	
	public void setDefaultFoundationCredits(int defaultCredits) {
		foundationCreditsNecessaryLabel.setText("" + defaultCredits);
	}
	
	public void setDefaultDisciplinaryCredits(int defaultCredits) {
		disciplinaryCreditsNecessaryLabel.setText("" + defaultCredits);
	}
	
	public void setApprovedFreeElectionCredits(int defaultCredits) {
		freeElectionCreditsApprovedLabel.setText("" + defaultCredits);
	}
	
	public void setApprovedLevelingCredits(int defaultCredits) {
		levelingCreditsApprovedLabel.setText("" + defaultCredits);
	}
	
	public void setApprovedFoundationCredits(int defaultCredits) {
		foundationCreditsApprovedLabel.setText("" + defaultCredits);
	}
	
	public void setApprovedDisciplinaryCredits(int defaultCredits) {
		disciplinaryCreditsApprovedLabel.setText("" + defaultCredits);
	}
	
	public void setPercentageDisciplinaryCredits(int percentage) {
		disciplinaryCreditsPerCentLabel.setText(percentage +"%");
	}

	public void setPercentageFreeElectionCredits(int percentage) {
		freeElectionCreditsPerCentLabel.setText(percentage +"%");
	}
	
	public void setPercentageFoundationCredits(int percentage) {
		foundationCreditsPerCentLabel.setText(percentage +"%");
	}
	
	public void setPercentageLevelingCredits(int percentage) {
		levelingCreditsPerCentLabel.setText(percentage +"%");
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
