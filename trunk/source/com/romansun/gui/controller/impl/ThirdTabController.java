package com.romansun.gui.controller.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.reports.ReportsManager;
import com.romansun.reports.logic.InfoVisitor;
import com.romansun.reports.logic.Report;

public class ThirdTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(ThirdTabController.class);
	private Integer month;
	private Integer year;
	private ReportsManager reportsManager;
	
	{
		reportsManager = new ReportsManager(PATH_TO_REPORTS);
		reportsManager.loadReports();
		month = new DateTime().getMonthOfYear()-1;
		year = new DateTime().getYear();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// add observer for Main Window
		MainWindowController.addObserver(this);
		loadReports();
		
		cbMonth.setConverter(new MonthConverter());
		Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		cbMonth.getItems().addAll(months);
		cbMonth.getSelectionModel().select(new Integer(month));
		
		cbYear.setConverter(new YearConverter());
		Integer[] years = {2012, 2013};
		cbYear.getItems().addAll(years);
		cbYear.getSelectionModel().select(year);
		
		cbMonth.valueProperty().addListener(new ChangeListener<Integer>() {
			public void changed(ObservableValue<? extends Integer> o,
					Integer oldValue, Integer newValue) {
				month = newValue;
				loadReports();
			}});
		
		cbYear.valueProperty().addListener(new ChangeListener<Integer>() {
			public void changed(ObservableValue<? extends Integer> o,
					Integer oldValue, Integer newValue) {
				year = newValue;
				loadReports();
			}});
		
		// Initialize table for report
		TableColumn columnFIO = tbReport.getColumns().get(0);
		TableColumn columnLunches = tbReport.getColumns().get(1);
		TableColumn columnDinners = tbReport.getColumns().get(2);
		columnFIO.setCellValueFactory((new PropertyValueFactory<InfoVisitor, String>("FIO")));
		columnLunches.setCellValueFactory((new PropertyValueFactory<InfoVisitor, Integer>("lunches")));
		columnDinners.setCellValueFactory((new PropertyValueFactory<InfoVisitor, Integer>("dinners")));
		
		// Initialize listener for input mask
		txtMask.caretPositionProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldValue, Number newValue) {
				String mask = txtMask.getText();
				Report report = lvReports.getSelectionModel().getSelectedItem();
				tbReport.getItems().clear();
				if (mask!=null && !mask.isEmpty()) {
					tbReport.getItems().addAll(filterByMask(report.getVisitors(), mask));
				} else {
					tbReport.getItems().addAll(report.getVisitors());
				}
			}});
	}
	
	@FXML
	private ComboBox<Integer> cbMonth;
	@FXML
	private ComboBox<Integer> cbYear;
	@FXML
	private ListView<Report> lvReports;
	@FXML
	private TextField txtMask;
	@FXML
	private TableView<InfoVisitor> tbReport;
	
	@FXML
	private void chooseReport(MouseEvent mouseEvent) {
		if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
			if (mouseEvent.getClickCount() == 2) {
				tbReport.getItems().clear();
				Report report = lvReports.getSelectionModel().getSelectedItem();
				tbReport.getItems().addAll(sortCollection(report.getVisitors()));
			}
		}
	}
	
	@FXML
	private void keyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			int answer = Dialog.showQuestion("Вы уверены, что хотите удалить выбранный отчет?", event);
			if (answer == 1 /*YES*/) {
				Report report = lvReports.getSelectionModel().getSelectedItem();
				boolean result = report.getFile().delete();
				if (result) {
					loadReports();
					LOG.info("Отчет " + report.getName()
							+ " был успешно удален");
				} else {
					LOG.error("При удалении отчета " + report.getName()
							+ " возникли проблемы. Отчет не был удален");
				}
			}
		}
	}
	
	private void loadReports() {
		List<Report> reports = reportsManager.getReportsByDate(month, year);
		lvReports.getItems().clear();
		lvReports.getItems().addAll(reports);
	}
	
	private List<InfoVisitor> filterByMask(List<InfoVisitor> visitors, String mask) {
		List<InfoVisitor> filterList = new ArrayList<InfoVisitor>();
		for (InfoVisitor v : visitors) {
			String fio = v.getFIO();
			if (fio.toLowerCase().contains(mask.toLowerCase())) {
				filterList.add(v);
			}
		}
		
		return filterList;
	}
	
	private List<InfoVisitor> sortCollection(Collection<InfoVisitor> collection) {
		List<InfoVisitor> visitors = new ArrayList<InfoVisitor>(collection);
		Collections.sort(visitors, new Comparator<InfoVisitor>(){
			public int compare(InfoVisitor v1, InfoVisitor v2) {
				return v1.getFIO().compareTo(v2.getFIO());
			}});
		return visitors;
	}
	
	/*
	 * Class Converter for ComboBox "Month"
	 */
	private class MonthConverter extends StringConverter<Integer> {

		private Map<String, Integer> months = new HashMap<String, Integer>();
		
		{
			months.put("Январь", 1);
			months.put("Февраль", 2);
			months.put("Март", 3);
			months.put("Апрель", 4);
			months.put("Май", 5);
			months.put("Июнь", 6);
			months.put("Июль", 7);
			months.put("Август", 8);
			months.put("Сентябрь", 9);
			months.put("Октябрь", 10);
			months.put("Ноябрь", 11);
			months.put("Декабрь", 12);
		}
		
		@Override
		public Integer fromString(String month) {
			return months.get(month);
		}

		@Override
		public String toString(Integer month) {
			for (String key : months.keySet()) {
				if (month.equals(months.get(key)))
					return key;
			}
			return null;
		}
	}
	
	/*
	 * Class Converter for ComboBox "Year"
	 */
	private class YearConverter extends StringConverter<Integer> {
		
		@Override
		public Integer fromString(String month) {
			return Integer.parseInt(month);
		}

		@Override
		public String toString(Integer month) {
			return month.toString();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		loadReports();
	}
}
