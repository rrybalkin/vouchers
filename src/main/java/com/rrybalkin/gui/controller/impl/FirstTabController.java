package com.rrybalkin.gui.controller.impl;

import com.rrybalkin.gui.Dialog;
import com.rrybalkin.gui.controller.AbstractController;
import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.entity.Talon;
import com.rrybalkin.hibernate.entity.Visitor;
import com.rrybalkin.utils.Messages;
import com.rrybalkin.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;

public class FirstTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(FirstTabController.class);

	private static final int MAX_COUNT = 3;

	private Visitor chosenVisitor;
	private Association chosenFilter;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Add observer in SecondTabController
		SecondTabController.addObserver(this);
		MainWindowController.addObserver(this);

		// remember the initial labels text
		this.lblFIOText = lblFIO.getText();
		this.lblGroupText = lblGroup.getText();
		this.lblBreakfastsText = lblBreakfasts.getText();
		this.lblLunchesText = lblLunches.getText();
		this.lblDinnersText = lblDinners.getText();

		// Initialize ComboBox "Filter", ComboBox "Groups"
		loadFilters();
		// Initialize ListView "Visitors"
		loadVisitors();
		// Initialize ComboBoxes for count dinners and lunches
		initCounters();

		// Initialize listener for input mask
		txtMask.caretPositionProperty().addListener(
				(arg01, oldValue, newValue) -> {
                    String mask = txtMask.getText();
                    if (mask != null && !mask.isEmpty()) {
                        loadVisitorsForMask(mask);
                    } else {
                        loadVisitors();
                    }
                });
	}

	@FXML
	private ComboBox<Association> filter;
	@FXML
	private TextField txtMask;
	@FXML
	private ListView<Visitor> listVisitors;
	@FXML
	private Label lblFIO;
	private String lblFIOText;
	@FXML
	private Label lblGroup;
	private String lblGroupText;
	@FXML
	private Label lblBreakfasts;
	private String lblBreakfastsText;
	@FXML
	private Label lblLunches;
	private String lblLunchesText;
	@FXML
	private Label lblDinners;
	private String lblDinnersText;
	@FXML
	private ComboBox<Integer> countBreakfasts;
	@FXML
	private ComboBox<Integer> countLunches;
	@FXML
	private ComboBox<Integer> countDinners;
	@FXML
	private Button btnAdd;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtLastName;
	@FXML
	private TextField txtMiddleName;
	@FXML
	private ComboBox<Association> cbGroups;
	@FXML
	private TitledPane titledPane;
	@FXML
	private Label lblInfo;

	@FXML
	void addTalon() {
		Integer breakfasts = countBreakfasts.getValue();
		Integer lunches = countLunches.getValue();
		Integer dinners = countDinners.getValue();
		Talon talon = chosenVisitor.getTalon();
		talon.setBreakfasts(talon.getBreakfasts() + breakfasts);
		talon.setDinners(talon.getDinners() + dinners);
		talon.setLunches(talon.getLunches() + lunches);
		try {
			daoFactory.getTalonDAO().update(talon);
		} catch (Exception e) {
			Dialog.showErrorOnException(e);
			LOG.error("Error while adding new talon: " + talon, e);
		}
		countBreakfasts.getSelectionModel().selectFirst();
		countLunches.getSelectionModel().selectFirst();
		countDinners.getSelectionModel().selectFirst();
		loadInfoAboutVisitor();
	}

	@FXML
	void changeGroup() {
		Association newFilter = filter.getValue();
		if (chosenFilter != null && newFilter != null && !newFilter.getName().equals(chosenFilter.getName())) {
			LOG.debug("Changing filter from " + chosenFilter + " to " + newFilter + " ...");
			chosenFilter = newFilter;
			loadVisitors();
		}
	}

	@FXML
	void updateVisitor() {
		Visitor newVisitor = new Visitor();
		final String firstName = txtFirstName.getText();
		final String lastName = txtLastName.getText();
		final String middleName = txtMiddleName.getText();
		final Association association = cbGroups.getValue();
		if (!lastName.isEmpty() && !firstName.isEmpty()) {
			newVisitor.setFirstName(Utils.upFirst(firstName));
			newVisitor.setLastName(Utils.upFirst(lastName));
			newVisitor.setMiddleName(Utils.upFirst(middleName));
			newVisitor.setAssociation(association);
			newVisitor.setTalon(chosenVisitor.getTalon());
			newVisitor.setId(chosenVisitor.getId());
			try {
				visitorsDAO.update(newVisitor);
				chosenVisitor = newVisitor;
				loadVisitors();
				loadInfoAboutVisitor();
				clickOnTitledPane();
				Dialog.showInfo(Messages.get("dialog.info.visitor-updated"));
			} catch (Exception e) {
				Dialog.showErrorOnException(e);
				LOG.error("Error while updating visitor: " + chosenVisitor, e);
			}
		} else {
			Dialog.showWarning(Messages.get("dialog.warn.visitor-empty-fields"));
		}
	}

	@FXML
	void keyPressed(KeyEvent event) {
		final Visitor selectedVisitor = listVisitors.getSelectionModel().getSelectedItem();
		if (selectedVisitor == null) {
			LOG.warn("keyPressed event when no selected visitor in the list");
			return;
		}

		if (event.getCode() == KeyCode.DELETE) {
			int answer = Dialog.showQuestion(Messages.get("dialog.question.do-visitor-delete", selectedVisitor.getFIO()), event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						visitorsDAO.delete(visitor);
						daoFactory.getTalonDAO().delete(visitor.getTalon());
						loadVisitors();
						LOG.info("Visitor = " + visitor.getId() + " was deleted");
					}
				} catch (Exception e) {
					Dialog.showErrorOnException(e);
					LOG.error("Error while deleting visitor: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.P /* Breakfasts */) {
			int answer = Dialog.showQuestion(Messages.get("dialog.question.do-visitor-breakfasts-reset", selectedVisitor.getFIO()), event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel().getSelectedItem();
					if (visitor != null) {
						daoFactory.getTalonDAO().resetBreakfastById(visitor.getTalon().getId());
						loadVisitors();
						if (chosenVisitor != null && chosenVisitor.equals(visitor)) {
							chosenVisitor.getTalon().setBreakfasts(0);
							loadInfoAboutVisitor();
						}
						LOG.info("Breakfasts for visitor = [" + visitor + "] were reset");
					}
				} catch (Exception e) {
					Dialog.showErrorOnException(e);
					LOG.error("Error while doing reset lunches: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.J /* Lunches */) {
			int answer = Dialog.showQuestion(Messages.get("dialog.question.do-visitor-lunches-reset", selectedVisitor.getFIO()), event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						daoFactory.getTalonDAO().resetLunchById(visitor.getTalon().getId());
						loadVisitors();
						if (chosenVisitor != null && chosenVisitor.equals(visitor)) {
							chosenVisitor.getTalon().setLunches(0);
							loadInfoAboutVisitor();
						}
						LOG.info("Lunches for visitor = [" + visitor + "] were reset");
					}
				} catch (Exception e) {
					Dialog.showErrorOnException(e);
					LOG.error("Error while doing reset lunches: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.E /* Dinners */) {
			int answer = Dialog.showQuestion(Messages.get("dialog.question.do-visitor-dinners-reset", selectedVisitor.getFIO()), event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel().getSelectedItem();
					if (visitor != null) {
						daoFactory.getTalonDAO().resetDinnerById(
								visitor.getTalon().getId());
						loadVisitors();
						if (chosenVisitor != null && chosenVisitor.equals(visitor)) {
							chosenVisitor.getTalon().setDinners(0);
							loadInfoAboutVisitor();
						}
						LOG.info("Dinners for visitor = [" + visitor + "] were reset");
					}
				} catch (Exception e) {
					Dialog.showErrorOnException(e);
					LOG.error("Error while doing reset dinners: ", e);
				}
			}
		}
	}

	@FXML
	void chooseVisitor(MouseEvent mouseEvent) {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			if (mouseEvent.getClickCount() == 2) {
				Visitor visitor = listVisitors.getSelectionModel()
						.getSelectedItem();
				if (visitor != null) {
					this.chosenVisitor = visitor;
					loadInfoAboutVisitor();
					clickOnTitledPane();
				}
			}
		}
	}

	@FXML
	void clickOnTitledPane() {
		boolean expanded = titledPane.expandedProperty().get();
		if (expanded) {
			// Set data in Fields
			if (chosenVisitor != null) {
				txtLastName.setText(chosenVisitor.getLastName());
				txtFirstName.setText(chosenVisitor.getFirstName());
				txtMiddleName.setText(chosenVisitor.getMiddleName());
				int selIndex = 0;
				for (int i = 0; i < cbGroups.getItems().size(); i++) {
					if (cbGroups.getItems().get(i).getName().equals(chosenVisitor.getAssociation().getName()))
						selIndex = i;
				}
				cbGroups.getSelectionModel().select(selIndex);
			}
		}
	}

	private void loadVisitors() {
		listVisitors.getItems().clear();
		try {
			List<Visitor> visitors;
			String mask = txtMask.getText();
			if (mask != null && mask.length() != 0) {
				loadVisitorsForMask(mask);
			} else {
				if (chosenFilter.getId() == -1L) {
					visitors = visitorsDAO.getAll();
				} else {
					visitors = visitorsDAO.getVisitorsByCriteria(chosenFilter, null);
				}
				listVisitors.getItems().addAll(sortVisitors(visitors));
				LOG.info("Visitors by association = [" + chosenFilter + "] were loaded");
				lblInfo.setText(Messages.get("label.all-visitors", visitors.size()));
			}
		} catch (Exception e) {
			LOG.error("Error while loading visitors by association = ["
					+ chosenFilter + "]: ", e);
		}
	}

	private void loadVisitorsForMask(String mask) {
		listVisitors.getItems().clear();
		try {
			List<Visitor> visitors = visitorsDAO.getVisitorsByCriteria(
							((chosenFilter.getId() != -1) ? chosenFilter : null),
							mask);
			listVisitors.getItems().addAll(sortVisitors(visitors));
			
			LOG.info("Visitors by association = [" + chosenFilter + "] and mask = " + mask + " were loaded");
			lblInfo.setText(Messages.get("label.all-visitors", visitors.size()));
		} catch (Exception e) {
			LOG.error("Error while loading visitors by association = ["
					+ chosenFilter + "] and mask = " + mask + ": ", e);
		}
	}

	private void loadFilters() {
		filter.getItems().clear();
		cbGroups.getItems().clear();
		// Create filter "All visitors"
		final Association all = new Association();
		all.setId(-1L);
		all.setName(Messages.get("group.all-visitors"));
		filter.getItems().add(all);
		try {
			List<Association> groups = daoFactory.getAssociationDAO().getAll();
			groups = sortGroups(groups);
			filter.getItems().addAll(groups);
			filter.getSelectionModel().selectFirst();
			chosenFilter = filter.getValue();
			cbGroups.getItems().addAll(groups);
		} catch (Exception e) {
			LOG.error("Error while loading filters: ", e);
		}
	}

	private void loadInfoAboutVisitor() {
		if (chosenVisitor != null) {
			lblFIO.setText(lblFIOText + " " + chosenVisitor.getLastName() + " "
					+ chosenVisitor.getFirstName() + " "
					+ chosenVisitor.getMiddleName());
			lblGroup.setText(lblGroupText + " " + chosenVisitor.getAssociation().getName());
			Talon talon = chosenVisitor.getTalon();
			lblBreakfasts.setText(lblBreakfastsText + " " + talon.getBreakfasts());
			lblLunches.setText(lblLunchesText + " " + talon.getLunches());
			lblDinners.setText(lblDinnersText + " " + talon.getDinners());

			titledPane.setDisable(false);
			btnAdd.setDisable(false);
			countBreakfasts.setDisable(false);
			countLunches.setDisable(false);
			countDinners.setDisable(false);
		}
	}

	private void initCounters() {
		List<Integer> counts = new ArrayList<>();
		for (int i = 0; i <= MAX_COUNT; i++) {
			counts.add(i);
		}
		for (ComboBox<Integer> cb : Arrays.asList(countBreakfasts, countLunches, countDinners)) {
			cb.getItems().clear();
			cb.getItems().addAll(counts);
			cb.getSelectionModel().selectFirst();
		}
	}

	@Override
	public void update(Observable o, Object param) {
		loadFilters();
		loadVisitors();
	}
}
