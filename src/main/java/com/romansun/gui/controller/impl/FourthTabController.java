package com.romansun.gui.controller.impl;

import com.romansun.gui.Dialog;
import com.romansun.gui.controller.AbstractController;
import com.romansun.printing.data.ActualReportData;
import com.romansun.printing.data.ReportData;
import com.romansun.printing.data.StoredReportData;
import com.romansun.printing.writer.IReportWriter;
import com.romansun.printing.writer.WriterFactory;
import com.romansun.reporting.logic.Report;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FourthTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(FourthTabController.class);

	private final static String PRINTING_REPORT_BY_ACTUAL_DATA = "Формирование отчета по текущим актуальным данным";
	private final static String GREEN_COLOR = "#008009", RED_COLOR = "#d91548";

	private Report printingReport;
	private Integer month;
	private Integer year;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		ThirdTabController.addObserver(this);

		cbEnableEmptyVisitors.getItems().clear();
		cbEnableEmptyVisitors.getItems().addAll(Boolean.FALSE, Boolean.TRUE);
		cbEnableEmptyVisitors.setConverter(new StringConverter<Boolean>() {
			@Override
			public String toString(Boolean o) {
				return o ? "Да" : "Нет";
			}

			@Override
			public Boolean fromString(String string) {
				return "ДА".equalsIgnoreCase(string) ? Boolean.TRUE : Boolean.FALSE;
			}
		});
		cbEnableEmptyVisitors.getSelectionModel().select(0);

		cbReportFormat.getItems().clear();
		cbReportFormat.getItems().addAll(config.printReportFormats);
		cbReportFormat.getSelectionModel().select(0);

		seeReport.visibleProperty().set(false);

		lblReportInfo.setTextFill(Color.web(GREEN_COLOR));
		resetReportInfo();

		month = new DateTime().getMonthOfYear();
		year = new DateTime().getYear();

		cbMonth.setConverter(new MonthConverter());
		cbMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		cbMonth.getSelectionModel().select(month);

		cbYear.setConverter(new YearConverter());
		cbYear.getItems().addAll(year-1, year, year+1);
		cbYear.getSelectionModel().select(year);

		cbMonth.valueProperty().addListener((o, oldValue, newValue) -> month = newValue);
		cbYear.valueProperty().addListener((o, oldValue, newValue) -> year = newValue);
	}

	@FXML
	private ComboBox<Boolean> cbEnableEmptyVisitors;
	@FXML
	private ComboBox<String> cbReportFormat;
	@FXML
	private Label lblReportInfo;
	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtCostOfLunch;
	@FXML
	private TextField txtCostOfDinner;
	@FXML
	private Hyperlink seeReport;
	@FXML
	private ComboBox<Integer> cbMonth;
	@FXML
	private ComboBox<Integer> cbYear;

	@FXML
	protected void createReport() {
		LOG.debug("Start generating report ...");
		String reportType = cbReportFormat.getSelectionModel().getSelectedItem();
		try
		{
			if (!validate()) return;
			ReportData reportData = buildReportData();
			String reportDate;
			if (printingReport != null) {
				String storedReportName = printingReport.getName();
				reportDate = storedReportName.substring(0, storedReportName.indexOf('.'));
			} else {
				reportDate = MonthConverter.getMonthNameByIndex(cbMonth.getSelectionModel().getSelectedItem())
						+ " " + cbYear.getSelectionModel().getSelectedItem();
			}

			IReportWriter writer = WriterFactory.getWriter(reportType, config.pathToReports);
			if (writer == null) {
				throw new RuntimeException("Этот тип отчета в настоящий момент не поддерживается!");
			}
			File report = writer.generateReport(reportData, reportDate);

			if (report != null && report.exists()) {
				lblStatus.setTextFill(Color.web(GREEN_COLOR));
				lblStatus.setText("Отчет успешно сформирован: " + report.getAbsolutePath());
				seeReport.visibleProperty().set(true);
				seeReport.setUserData(report.getAbsoluteFile());
			} else {
				throw new RuntimeException("Файл с отчетом не найден!");
			}

			resetReportInfo();
		}
		catch (Exception e)
		{
			LOG.error("Error while forming report with reportType = '" + reportType + "': ", e);

			lblStatus.setTextFill(Color.web(RED_COLOR));
			lblStatus.setText("Во время формирования отчета возникли ошибки: " + e.getMessage());
		}
	}

	@FXML // process click on hyperlink
	protected void hlAction() {
		File file = (File) seeReport.getUserData();
		try {
			Desktop.getDesktop().open(file);
			lblStatus.setText("");
		} catch (IOException e) {
			LOG.error("Error while tries to open report by address = " + file.getAbsolutePath() + ": ", e);
		}
	}

	/**
	 * Method for validating states of fields before start of forming report
	 * @return boolean true - validation is passed, false - validation is failed
	 */
	private boolean validate() {
		String costOfLunch = txtCostOfLunch.getText();
		String costOfDinner = txtCostOfDinner.getText();

		if (costOfLunch == null || costOfLunch.length() == 0) {
			Dialog.showError("Поле \"Стоимость обеда\" должно быть заполнено!");
			return false;
		} else if (costOfDinner == null || costOfDinner.length() == 0) {
			Dialog.showError("Поле \"Стоимость ужина\" должно быть заполнено!");
			return false;
		}

		try {
			double lunches = Double.parseDouble(costOfLunch);
			double dinners = Double.parseDouble(costOfDinner);
			LOG.debug("Fields validated: lunches = " + lunches + ", dinners = " + dinners);
		} catch (NumberFormatException e) {
			LOG.error("Field costOfLunch = " + costOfLunch + " or costOfDinner = " + costOfDinner + " is incorrect!");
			Dialog.showError("Поля \"Стоимость обеда\" и \"Стоимость ужина\" должны содержать только числа!");
			return false;
		}

		return true;
	}

	/**
	 * Method for building report data according to the selected type of report
	 * @return data for forming report
	 */
	private ReportData buildReportData() throws Exception {
		ReportData reportData;

		boolean ignoreEmptyVisitors = cbEnableEmptyVisitors.getSelectionModel().getSelectedItem();
		double costOfLunch = Double.parseDouble(txtCostOfLunch.getText());
		double costOfDinner = Double.parseDouble(txtCostOfDinner.getText());

		if (printingReport != null) {
			reportData = new StoredReportData(printingReport, costOfLunch, costOfDinner, ignoreEmptyVisitors);
		} else {
			reportData = new ActualReportData(
					new ArrayList<>(daoFactory.getVisitorDAO().getAll()),
					costOfLunch,
					costOfDinner,
					ignoreEmptyVisitors
			);
		}

		// sorting report data
		reportData.sort((o1, o2) -> {
            String fio1 = o1.getVisitorLastname() + " " + o1.getVisitorFirstname();
            String fio2 = o2.getVisitorLastname() + " " + o2.getVisitorFirstname();

            return fio1.compareToIgnoreCase(fio2);
        });

		return reportData;
	}

	private void resetReportInfo() {
		lblReportInfo.setText(PRINTING_REPORT_BY_ACTUAL_DATA);
		printingReport = null;
	}

	@Override
	public void update(Observable o, Object obj) {
		if (obj != null && obj instanceof Report) {
			printingReport = (Report) obj;
			lblReportInfo.setText("Формирование сохраненного отчета " + printingReport.getName());
		} else {
			LOG.debug("Argument 'object' in update method is null or isn't instance of clazz 'Report'");
		}
	}
}
