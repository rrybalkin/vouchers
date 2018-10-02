package com.romansun.gui.controller.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.apache.log4j.Logger;

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.Dialog;
import com.romansun.hibernate.entity.Association;
import com.romansun.hibernate.entity.Talon;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.utils.Utils;

public class FirstTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(FirstTabController.class);

	private static final int MAX_COUNT = 3;

	private static Visitor choosenVisitor;
	private static Association chooseFilter;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Add observer in SecondTabController
		SecondTabController.addObserver(this);
		MainWindowController.addObserver(this);

		// remember the initial labels text
		this.lblFIOText = lblFIO.getText();
		this.lblGroupText = lblGroup.getText();
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
				new ChangeListener<Number>() {
					public void changed(ObservableValue<? extends Number> arg0,
							Number oldValue, Number newValue) {
						String mask = txtMask.getText();
						if (mask != null && !mask.isEmpty()) {
							loadVisitorsForMask(mask);
						} else {
							loadVisitors();
						}
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
	private Label lblLunches;
	private String lblLunchesText;
	@FXML
	private Label lblDinners;
	private String lblDinnersText;
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
	protected void addTalon() {
		Integer lunches = countLunches.getValue();
		Integer dinners = countDinners.getValue();
		Talon talon = choosenVisitor.getTalon();
		talon.setDinners(talon.getDinners() + dinners);
		talon.setLunches(talon.getLunches() + lunches);
		try {
			daoFactory.getTalonDAO().update(talon);
		} catch (Exception e) {
			Dialog.showError(e.getLocalizedMessage());
			LOG.error("Error: ", e);
		}
		countLunches.getSelectionModel().selectFirst();
		countDinners.getSelectionModel().selectFirst();
		loadInfoAboutVisitor();
	}

	@FXML
	protected void changeFilter() {
		Association newFilter = filter.getValue();
		if (chooseFilter != null && newFilter != null && !newFilter.getName().equals(chooseFilter.getName())) {
			chooseFilter = newFilter;
			loadVisitors();
		}
	}

	@FXML
	protected void updateVisitor() {
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
			newVisitor.setTalon(choosenVisitor.getTalon());
			newVisitor.setId(choosenVisitor.getId());
			try {
				visitorsDAO.update(newVisitor);
				choosenVisitor = newVisitor;
				loadVisitors();
				loadInfoAboutVisitor();
				clickOnTitledPane();
				Dialog.showInfo("Visitor updated.");
			} catch (Exception e) {
				Dialog.showError("Error while updating visitor: " + e.getLocalizedMessage());
				LOG.error("Error while updating visitor: ", e);
			}
		} else {
			Dialog.showError("Required visitor fields must be filled!");
		}
	}

	@FXML
	private void keyPressed(KeyEvent event) {
		final Visitor selectedVisitor = listVisitors.getSelectionModel().getSelectedItem();
		if (selectedVisitor == null) {
			LOG.error("keyPressed event when no selected visitor in the list");
			return;
		}

		if (event.getCode() == KeyCode.DELETE) {
			int answer = Dialog.showQuestion("Are you sure to delete visitor " + selectedVisitor.getFIO() + " ?", event);
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
					Dialog.showError("Error while deleting visitor: " + e.getMessage());
					LOG.error("Error while deleting visitor: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.L /* Lunches */) {
			int answer = Dialog.showQuestion("Are you sure to reset lunches for selected visitor " + selectedVisitor.getFIO() + " ?", event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						daoFactory.getTalonDAO().resetLunchById(visitor.getTalon().getId());
						loadVisitors();
						if (choosenVisitor != null && choosenVisitor.equals(visitor)) {
							choosenVisitor.getTalon().setLunches(0);
							loadInfoAboutVisitor();
						}
						LOG.info("Lunches for visitor = [" + visitor + "] were reset");
					}
				} catch (Exception e) {
					Dialog.showError("Error while reset lunches: " + e.getMessage());
					LOG.error("Error while doing reset lunches: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.D /* Dinners */) {
			int answer = Dialog.showQuestion("Are you sure to reset dinners for selected visitor " + selectedVisitor.getFIO() + " ?", event);
			if (answer == Dialog.YES) {
				try {
					Visitor visitor = listVisitors.getSelectionModel().getSelectedItem();
					if (visitor != null) {
						daoFactory.getTalonDAO().resetDinnerById(
								visitor.getTalon().getId());
						loadVisitors();
						if (choosenVisitor != null && choosenVisitor.equals(visitor)) {
							choosenVisitor.getTalon().setDinners(0);
							loadInfoAboutVisitor();
						}
						LOG.info("Dinners for visitor = [" + visitor + "] were reset");
					}
				} catch (Exception e) {
					Dialog.showError("Error while reset dinners: " + e.getMessage());
					LOG.error("Error while doing reset dinners: ", e);
				}
			}
		}
	}

	@FXML
	private void chooseVisitor(MouseEvent mouseEvent) {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			if (mouseEvent.getClickCount() == 2) {
				Visitor visitor = listVisitors.getSelectionModel()
						.getSelectedItem();
				if (visitor != null) {
					FirstTabController.choosenVisitor = visitor;
					loadInfoAboutVisitor();
					clickOnTitledPane();
				}
			}
		}
	}

	@FXML
	private void clickOnTitledPane() {
		Boolean expanded = titledPane.expandedProperty().get();
		if (expanded) {
			// Set data in Fields
			if (choosenVisitor != null) {
				txtLastName.setText(choosenVisitor.getLastName());
				txtFirstName.setText(choosenVisitor.getFirstName());
				txtMiddleName.setText(choosenVisitor.getMiddleName());
				Integer selIndex = null;
				for (int i = 0; i < cbGroups.getItems().size(); i++) {
					if (cbGroups.getItems().get(i).getName().equals(choosenVisitor.getAssociation().getName()))
						selIndex = i;
				}
				cbGroups.getSelectionModel().select(selIndex);
			}
		}
	}

	private void loadVisitors() {
		listVisitors.getItems().clear();
		try {
			Collection<Visitor> visitors;
			String mask = txtMask.getText();
			if (mask != null && mask.length() != 0) {
				loadVisitorsForMask(mask);
			} else {
				if (chooseFilter.getId() == -1L) {
					visitors = visitorsDAO.getAll();
				} else {
					visitors = visitorsDAO.getVisitorsByCriteria(chooseFilter, null);
				}
				listVisitors.getItems().addAll(sortCollection(visitors));
				LOG.info("Visitors by association = [" + chooseFilter + "] were loaded");
				lblInfo.setText("All visitors: " + visitors.size());
			}
		} catch (Exception e) {
			LOG.error("Error while loading visitors by association = ["
					+ chooseFilter + "]: ", e);
		}
	}

	private void loadVisitorsForMask(String mask) {
		listVisitors.getItems().clear();
		try {
			Collection<Visitor> visitors = visitorsDAO.getVisitorsByCriteria(
							((chooseFilter.getId() != -1) ? chooseFilter : null),
							mask);
			listVisitors.getItems().addAll(sortCollection(visitors));
			
			LOG.info("Visitors by association = [" + chooseFilter + "] and mask = " + mask + " were loaded");
			lblInfo.setText("All visitors: " + visitors.size());
		} catch (Exception e) {
			LOG.error("Error while loading visitors by association = ["
					+ chooseFilter + "] and mask = " + mask + ": ", e);
		}
	}

	private void loadFilters() {
		filter.getItems().clear();
		cbGroups.getItems().clear();
		// Create filter "All visitors"
		Association all = new Association();
		all.setId(-1L);
		all.setName("All visitors");
		filter.getItems().add(all);
		try {
			Collection<Association> associations = daoFactory.getAssociationDAO().getAll();
			filter.getItems().addAll(associations);
			filter.getSelectionModel().selectFirst();
			chooseFilter = filter.getValue();
			cbGroups.getItems().addAll(associations);
		} catch (Exception e) {
			LOG.error("Error: ", e);
		}
	}

	private void loadInfoAboutVisitor() {
		if (choosenVisitor != null) {
			lblFIO.setText(lblFIOText + " " + choosenVisitor.getLastName() + " "
					+ choosenVisitor.getFirstName() + " "
					+ choosenVisitor.getMiddleName());
			lblGroup.setText(lblGroupText + " " + choosenVisitor.getAssociation().getName());
			Talon talon = choosenVisitor.getTalon();
			lblLunches.setText(lblLunchesText + " " + talon.getLunches());
			lblDinners.setText(lblDinnersText + " " + talon.getDinners());

			titledPane.setDisable(false);
			btnAdd.setDisable(false);
			countLunches.setDisable(false);
			countDinners.setDisable(false);
		}
	}

	private void initCounters() {
		List<Integer> counts = new ArrayList<>();
		for (int i = 0; i <= MAX_COUNT; i++) {
			counts.add(i);
		}
		countLunches.getItems().clear();
		countDinners.getItems().clear();
		countLunches.getItems().addAll(counts);
		countLunches.getSelectionModel().selectFirst();
		countDinners.getItems().addAll(counts);
		countDinners.getSelectionModel().selectFirst();
	}

	private List<Visitor> sortCollection(Collection<Visitor> collection) {
		List<Visitor> visitors = new ArrayList<>(collection);
		visitors.sort((v1, v2) -> {
            String fio_1 = v1.getLastName() + " " + v1.getFirstName() + " "
                    + v1.getMiddleName();
            String fio_2 = v2.getLastName() + " " + v2.getFirstName() + " "
                    + v2.getMiddleName();
            return fio_1.compareTo(fio_2);
        });
		return visitors;
	}

	@Override
	public void update(Observable o, Object param) {
		loadFilters();
		loadVisitors();
	}
}
