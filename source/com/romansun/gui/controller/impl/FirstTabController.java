package com.romansun.gui.controller.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

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

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.hibernate.logic.Association;
import com.romansun.hibernate.logic.Talon;
import com.romansun.hibernate.logic.Visitor;

public class FirstTabController extends AbstractController implements
		Initializable, Observer {
	private final static Logger LOG = Logger
			.getLogger(FirstTabController.class);
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
		initCounters(3);

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
	private void addTalon() {
		Integer lunches = countLunches.getValue();
		Integer dinners = countDinners.getValue();
		Talon talon = chooseVisitor.getTalon();
		talon.setCount_dinner(talon.getCount_dinner() + dinners);
		talon.setCount_lunch(talon.getCount_lunch() + lunches);
		try {
			dao.getTalonDAO().updateTalonForVisitor(chooseVisitor, talon);
			LOG.info("Добавлен новый талон для посетителя "
					+ chooseVisitor.getId() + ", обедов="
					+ talon.getCount_lunch() + ", ужинов="
					+ talon.getCount_dinner());
		} catch (Exception e) {
			LOG.error("Ошибка при добавлении талона: ", e);
		}
		countLunches.getSelectionModel().selectFirst();
		countDinners.getSelectionModel().selectFirst();
		loadInfoAboutVisitor();
	}

	@FXML
	private void changeFilter() {
		Association newFilter = filter.getValue();
		if (chooseFilter != null && newFilter != null
				&& !newFilter.getName().equals(chooseFilter.getName())) {
			chooseFilter = newFilter;
			loadVisitors();
		}
	}

	@FXML
	private void updateVisitor() {
		Visitor newVisitor = new Visitor();
		String firstname = txtFirstname.getText();
		String lastname = txtLastname.getText();
		String middlename = txtMiddlename.getText();
		Association association = cbGroups.getValue();
		if (!lastname.isEmpty() && !firstname.isEmpty()) {
			newVisitor.setFirstname(upFirst(firstname));
			newVisitor.setLastname(upFirst(lastname));
			newVisitor.setMiddlename(upFirst(middlename));
			newVisitor.setAssociation(association);
			newVisitor.setTalon(chooseVisitor.getTalon());
			newVisitor.setId(chooseVisitor.getId());
			try {
				dao.getVisitorDAO().updateVisitor(chooseVisitor.getId(),
						newVisitor);
				chooseVisitor = newVisitor;
				loadVisitors();
				loadInfoAboutVisitor();
				clickOnTitledPane();
				LOG.info("Изменены данные о посетителе "
						+ chooseVisitor.getId());
			} catch (Exception e) {
				LOG.error("Ошибка при изменении данных о посетителе: ", e);
			}
		} else {
			Dialog.showError("Поля, помеченные * не могут быть пустыми!");
		}
	}

	@FXML
	private void keyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			int answer = Dialog.showQuestion(
					"Вы уверены, что хотите удалить выбранного посетителя?",
					event);
			if (answer == 1 /* YES */) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						dao.getVisitorDAO().deleteVisitor(visitor);
						dao.getTalonDAO().deleteTalon(visitor.getTalon());
						loadVisitors();
						LOG.info("Удален посетитель " + visitor.getId());
					}
				} catch (Exception e) {
					LOG.error("Ошибка при удалении посетителя: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.L /* Lunches */) {
			int answer = Dialog
					.showQuestion(
							"Вы уверены, что хотите сбросить обеды для выбранного посетителя?",
							event);
			if (answer == 1 /* YES */) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						dao.getTalonDAO().resetLunchById(
								visitor.getTalon().getId());
						loadVisitors();
						chooseVisitor.getTalon().setCount_lunch(0);
						loadInfoAboutVisitor();
						LOG.info("Сброшены обеды для посетителя "
								+ visitor.getId());
					}
				} catch (Exception e) {
					LOG.error("Ошибка при удалении обедов для посетителя: ", e);
				}
			}
		} else if (event.getCode() == KeyCode.D /* Dinners */) {
			int answer = Dialog
					.showQuestion(
							"Вы уверены, что хотите сбросить ужины для выбранного посетителя?",
							event);
			if (answer == 1 /* YES */) {
				try {
					Visitor visitor = listVisitors.getSelectionModel()
							.getSelectedItem();
					if (visitor != null) {
						dao.getTalonDAO().resetDinnerById(
								visitor.getTalon().getId());
						loadVisitors();
						chooseVisitor.getTalon().setCount_dinner(0);
						loadInfoAboutVisitor();
						LOG.info("Сброшены ужины для посетителя "
								+ visitor.getId());
					}
				} catch (Exception e) {
					LOG.error("Ошибка при удалении ужинов для посетителя: ", e);
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
					FirstTabController.chooseVisitor = visitor;
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
			if (chooseVisitor != null) {
				txtLastname.setText(chooseVisitor.getLastname());
				txtFirstname.setText(chooseVisitor.getFirstname());
				txtMiddlename.setText(chooseVisitor.getMiddlename());
				Integer selIndex = null;
				for (int i = 0; i < cbGroups.getItems().size(); i++) {
					if (cbGroups.getItems().get(i).getName()
							.equals(chooseVisitor.getAssociation().getName()))
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
			} else {
				visitors = dao.getVisitorDAO().getVisitorsByCriteria(
						chooseFilter, null);
			}
			listVisitors.getItems().addAll(sortCollection(visitors));
			LOG.info("Загружены посетители с фильтром="
					+ chooseFilter.getName());
		} catch (Exception e) {
			LOG.error("Ошибка при загрузке посетителей с фильтром="
					+ chooseFilter.getName() + ": ", e);
		}
	}

	private void loadVisitorsForMask(String mask) {
		listVisitors.getItems().clear();
		try {
			Collection<Visitor> visitors = dao
					.getVisitorDAO()
					.getVisitorsByCriteria(
							((chooseFilter.getId() != -1) ? chooseFilter : null),
							mask);
			listVisitors.getItems().addAll(sortCollection(visitors));
			LOG.info("Загружены посетители с фильтром="
					+ chooseFilter.getName() + " и маской=" + mask);
		} catch (Exception e) {
			LOG.error("Ошибка при загрузке посетителей с фильтром="
					+ chooseFilter.getName() + " и маской=" + mask + ": ", e);
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
			Collection<Association> associations = dao.getAssociationDAO()
					.getAllAssociations();
			filter.getItems().addAll(associations);
			filter.getSelectionModel().selectFirst();
			chooseFilter = filter.getValue();
			cbGroups.getItems().addAll(associations);
			LOG.info("Загружены все фильтры и группы");
		} catch (Exception e) {
			LOG.error("Ошибка при загрузке фильтров и групп: ", e);
		}
	}

	private void loadInfoAboutVisitor() {
		if (chooseVisitor != null) {
			// Set data in Labels
			lblFIO.setText("ФИО: " + chooseVisitor.getLastname() + " "
					+ chooseVisitor.getFirstname() + " "
					+ chooseVisitor.getMiddlename());
			lblGroup.setText("Группа: "
					+ chooseVisitor.getAssociation().getName());
			Talon talon = chooseVisitor.getTalon();
			lblLaunches
					.setText("Колличество обедов: " + talon.getCount_lunch());
			lblDinners
					.setText("Колличество ужинов: " + talon.getCount_dinner());

			titledPane.setDisable(false);
			btnAdd.setDisable(false);
			countLunches.setDisable(false);
			countDinners.setDisable(false);
		}
	}

	private void initCounters(int max) {
		List<Integer> counts = new ArrayList<Integer>();
		for (int i = 0; i <= max; i++) {
			counts.add(new Integer(i));
		}
		countLunches.getItems().clear();
		countDinners.getItems().clear();
		countLunches.getItems().addAll(counts);
		countLunches.getSelectionModel().selectFirst();
		countDinners.getItems().addAll(counts);
		countDinners.getSelectionModel().selectFirst();
	}

	private List<Visitor> sortCollection(Collection<Visitor> collection) {
		List<Visitor> visitors = new ArrayList<Visitor>(collection);
		Collections.sort(visitors, new Comparator<Visitor>() {
			public int compare(Visitor v1, Visitor v2) {
				String fio_1 = v1.getLastname() + " " + v1.getFirstname() + " "
						+ v1.getMiddlename();
				String fio_2 = v2.getLastname() + " " + v2.getFirstname() + " "
						+ v2.getMiddlename();
				return fio_1.compareTo(fio_2);
			}
		});
		return visitors;
	}

	@Override
	public void update(Observable o, Object param) {
		loadFilters();
		loadVisitors();
	}
}
