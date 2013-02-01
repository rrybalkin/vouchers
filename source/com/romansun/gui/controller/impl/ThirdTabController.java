package com.romansun.gui.controller.impl;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import com.romansun.gui.controller.AbstractController;
import com.romansun.reports.logic.Report;

public class ThirdTabController extends AbstractController implements Initializable {
	private Integer month;
	private Integer year;
	{
		month = new Date().getMonth();
		year = new Date().getYear() + 1900;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadReports();
		cbMonth.setConverter(new MonthConverter());
		Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		cbMonth.getItems().addAll(months);
		cbMonth.getSelectionModel().select(month-1);
		
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
	}
	
	@FXML
	private ComboBox<Integer> cbMonth;
	@FXML
	private ComboBox<Integer> cbYear;
	@FXML
	private ListView<Report> lvReports;
	@FXML
	private TextField txtMask;
	
	private void loadReports() {
		List<Report> reports = reportsManager.getReportsByDate(month, year);
		lvReports.getItems().clear();
		lvReports.getItems().addAll(reports);
	}
	
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
}
