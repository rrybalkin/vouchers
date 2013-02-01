package com.romansun.gui.controller.impl;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import com.romansun.gui.controller.AbstractController;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public class FirstTabController extends AbstractController implements Initializable, Observer {
	private static Visitor chooseVisitor;
	private static Association chooseFilter;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Add observer in SecondTabController
		SecondTabController.addObserver(this);
		// Initialize ComboBox "Filter", ComboBox "Groups"
		loadFilters();
		// Initialize ListView "Visitors"
		loadVisitors();
		
		// Initialize ComboBoxes for count dinners and lunches
		Integer[] counts = {0, 1, 2, 3};
		countLunches.getItems().clear();
		countDinners.getItems().clear();
		countLunches.getItems().addAll(counts);
		countLunches.getSelectionModel().selectFirst();
		countDinners.getItems().addAll(counts);
		countDinners.getSelectionModel().selectFirst();
		
		// Initialize listener for input mask
		txtMask.caretPositionProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				String mask = txtMask.getText();
				if (mask!=null && !mask.isEmpty()) {
					loadVisitorsForMask(mask);
				}
				else {
					loadVisitors();
				}
			}});
	}
	
	@FXML
	private ComboBox<Association> filter;
	@FXML
	private TextField txtMask;
	@FXML
	private ListView<Visitor> listVisitors;
	@FXML
	private Label lblFIO;
	@FXML
	private Label lblGroup;
	@FXML
	private Label lblLaunches;
	@FXML
	private Label lblDinners;
	@FXML
	private ComboBox<Integer> countLunches;
	@FXML
	private ComboBox<Integer> countDinners;
	@FXML
	private Button btnAdd;
	@FXML
	private TextField txtLastname;
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtMiddlename;
	@FXML
	private ComboBox<Association> cbGroups;
	@FXML
	private TitledPane titledPane;
	
	@FXML
	private void addTalon(ActionEvent event) {
		Integer lunches = countLunches.getValue();
		Integer dinners = countDinners.getValue();
		Talon talon = chooseVisitor.getTalon();
		talon.setCount_dinner(talon.getCount_dinner() + dinners);
		talon.setCount_lunch(talon.getCount_lunch() + lunches);
		try {
			dao.getTalonDAO().updateTalonForVisitor(chooseVisitor, talon);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		countLunches.getSelectionModel().selectFirst();
		countDinners.getSelectionModel().selectFirst();
		loadInfoAboutVisitor();
	}

	@FXML 
	private void changeFilter(ActionEvent event) {
		Association newFilter = filter.getValue();
		if (chooseFilter != null && newFilter != null && !newFilter.getName().equals(chooseFilter.getName())) {
			chooseFilter = newFilter;
			loadVisitors();
		}
	}
	
	@FXML
	private void updateVisitor(ActionEvent event) {
		Visitor newVisitor = new Visitor();
		String firstname = txtFirstname.getText();
		newVisitor.setFirstname(firstname);
		String lastname = txtLastname.getText();
		newVisitor.setLastname(lastname);
		String middlename = txtMiddlename.getText();
		newVisitor.setMiddlename(middlename);
		Association association = cbGroups.getValue();
		newVisitor.setAssociation(association);
		newVisitor.setTalon(chooseVisitor.getTalon());
		newVisitor.setId(chooseVisitor.getId());
		try {
			dao.getVisitorDAO().updateVisitor(chooseVisitor.getId(), newVisitor);
			chooseVisitor = newVisitor;
			loadVisitors();
			loadInfoAboutVisitor();
			clickOnTitledPane(null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void deleteVisitor(ActionEvent event) {
		try {
			dao.getVisitorDAO().deleteVisitor(chooseVisitor);
			loadVisitors();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void chooseVisitor(MouseEvent mouseEvent) {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			if (mouseEvent.getClickCount() == 2) {
				Visitor visitor = listVisitors.getSelectionModel().getSelectedItem();
				FirstTabController.chooseVisitor = visitor;
				loadInfoAboutVisitor();
				clickOnTitledPane(null);
			}
		}
	}
	
	@FXML
	private void clickOnTitledPane(MouseEvent event) {
		Boolean expanded = titledPane.expandedProperty().get();
		if (expanded) {
			// Set data in Fields
			if (chooseVisitor != null) {
				txtLastname.setText(chooseVisitor.getLastname());
				txtFirstname.setText(chooseVisitor.getFirstname());
				txtMiddlename.setText(chooseVisitor.getMiddlename());
				Integer selIndex = null;
				for (int i=0; i < cbGroups.getItems().size(); i++) {
					if (cbGroups.getItems().get(i).getName().equals(chooseVisitor.getAssociation().getName()))
							selIndex = i;
				}
				cbGroups.getSelectionModel().select(selIndex);
			}
		}
	}
	
	private void loadVisitors() {
		listVisitors.getItems().clear();
		try {
			Collection<Visitor> visitors = null;
			if (chooseFilter.getId() == -1L) {
				visitors = dao.getVisitorDAO().getAllVisitors();
			}
			else {
				visitors = dao.getVisitorDAO().getVisitorsByCriteria(chooseFilter, null);
			}
			listVisitors.getItems().addAll(visitors);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadVisitorsForMask(String mask) {
		listVisitors.getItems().clear();
		try {
			Collection<Visitor> visitors = dao.getVisitorDAO().getVisitorsByCriteria(((chooseFilter.getId()!=-1) ? chooseFilter : null), mask);
			listVisitors.getItems().addAll(visitors);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadFilters() {
		filter.getItems().clear();
		cbGroups.getItems().clear();
		// Create filter "All visitors"
		Association all = new Association();
		all.setId(-1L);
		all.setName("Все посетители");
		filter.getItems().add(all);
		try {
			Collection<Association> associations = dao.getAssociationDAO().getAllAssociations();
			filter.getItems().addAll(associations);
			filter.getSelectionModel().selectFirst();
			chooseFilter = filter.getValue();
			cbGroups.getItems().addAll(associations);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void loadInfoAboutVisitor() {
		// Set data in Labels
		lblFIO.setText("ФИО: " + chooseVisitor.getLastname() + " "
				+ chooseVisitor.getFirstname() + " "
				+ chooseVisitor.getMiddlename());
		lblGroup.setText("Группа: " + chooseVisitor.getAssociation().getName());
		Talon talon = chooseVisitor.getTalon();
		lblLaunches.setText("Колличество обедов: " + talon.getCount_lunch());
		lblDinners.setText("Колличество ужинов: " + talon.getCount_dinner());

		titledPane.setDisable(false);
		btnAdd.setDisable(false);
		countLunches.setDisable(false);
		countDinners.setDisable(false);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		loadFilters();
		loadVisitors();
	}
}
