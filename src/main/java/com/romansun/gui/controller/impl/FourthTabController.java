package com.romansun.gui.controller.impl;

import com.romansun.gui.Dialog;
import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.controller.Converters;
import com.romansun.printing.data.ActualReportData;
import com.romansun.printing.data.ReportData;
import com.romansun.printing.data.StoredReportData;
import com.romansun.printing.writer.ReportType;
import com.romansun.printing.writer.ReportWriter;
import com.romansun.printing.writer.ReportWriterFactory;
import com.romansun.reporting.logic.Report;
import com.romansun.utils.Messages;
import com.sun.org.apache.regexp.internal.RE;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class FourthTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(FourthTabController.class);

	private final static String GREEN_COLOR = "#008009", RED_COLOR = "#d91548";

	private Report printingReport;
	private Integer month;
	private Integer year;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		ThirdTabController.addObserver(this);

		cbEnableEmptyVisitors.getItems().clear();
		cbEnableEmptyVisitors.setConverter(Converters.YES_NO_TO_BOOLEAN);
		cbEnableEmptyVisitors.getItems().addAll(Boolean.FALSE, Boolean.TRUE);
		cbEnableEmptyVisitors.getSelectionModel().select(0);

		cbReportFormat.getItems().clear();
		cbReportFormat.getItems().addAll(ReportType.values());
		cbReportFormat.setConverter(Converters.REPORT_TYPE_CONVERTER);
		cbReportFormat.getSelectionModel().select(0);

		seeReport.visibleProperty().set(false);

		lblReportInfo.setTextFill(Color.web(GREEN_COLOR));
		resetReportInfo();

		month = new DateTime().getMonthOfYear();
		year = new DateTime().getYear();

		cbMonth.setConverter(Converters.MONTHS_CONVERTER);
		cbMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		cbMonth.getSelectionModel().select(month);

		cbYear.setConverter(Converters.YEARS_CONVERTER);
		cbYear.getItems().addAll(year-1, year, year+1);
		cbYear.getSelectionModel().select(year);

		cbMonth.valueProperty().addListener((o, oldValue, newValue) -> month = newValue);
		cbYear.valueProperty().addListener((o, oldValue, newValue) -> year = newValue);
	}

	@FXML
	private ComboBox<Boolean> cbEnableEmptyVisitors;
	@FXML
	private ComboBox<ReportType> cbReportFormat;
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
	void createReport() {
		LOG.debug("Start generating report ...");
		ReportType reportType = cbReportFormat.getSelectionModel().getSelectedItem();
		try
		{
			if (!validate()) return;
			ReportData reportData = buildReportData();
			String reportDate;
			if (printingReport != null) {
				String storedReportName = printingReport.getName();
				reportDate = storedReportName.substring(0, storedReportName.indexOf('.'));
			} else {
				reportDate = Converters.MONTHS_CONVERTER.toString(cbMonth.getSelectionModel().getSelectedItem())
						+ " " + cbYear.getSelectionModel().getSelectedItem();
			}

			ReportWriter writer = ReportWriterFactory.getWriter(reportType);
			if (writer == null) {
				Dialog.showWarning(Messages.get("dialog.warn.type-report-is-not-supported"));
				return;
			}
			File report = writer.generateReport(reportType, reportData, reportDate);

			if (report != null && report.exists()) {
				lblStatus.setTextFill(Color.web(GREEN_COLOR));
				lblStatus.setText(Messages.get("label.report-is-ready", report.getAbsolutePath()));
				seeReport.visibleProperty().set(true);
				seeReport.setUserData(report.getAbsoluteFile());
			} else {
				Dialog.showWarning("dialog.warn.report-file-not-found");
				return;
			}

			resetReportInfo();
		}
		catch (Exception e) {
			Dialog.showErrorOnException(e);
			lblStatus.setTextFill(Color.valueOf(RED_COLOR));
			lblStatus.setText(Messages.get("label.error-on-report-generating"));
			LOG.error("Error while forming report with reportType = '" + reportType + "': ", e);
		}
	}

	@FXML // process click on hyperlink
	void hlAction() {
		File file = (File) seeReport.getUserData();
		try {
			Desktop.getDesktop().open(file);
			lblStatus.setText("");
		}
		catch (IOException e) {
			Dialog.showErrorOnException(e);
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
			Dialog.showWarning(Messages.get("dialog.warn.lunch-price-must-be-filled"));
			return false;
		} else if (costOfDinner == null || costOfDinner.length() == 0) {
			Dialog.showWarning(Messages.get("dialog.warn.dinner-price-must-be-filled"));
			return false;
		}

		try {
			double lunches = Double.parseDouble(costOfLunch);
			double dinners = Double.parseDouble(costOfDinner);
			LOG.debug("Fields validated: lunches = " + lunches + ", dinners = " + dinners);
		} catch (NumberFormatException e) {
			LOG.error("Field costOfLunch = " + costOfLunch + " or costOfDinner = " + costOfDinner + " is incorrect!");
			Dialog.showWarning(Messages.get("dialog.warn.prices-must-be-numeric"));
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

		boolean includeEmptyVisitors = cbEnableEmptyVisitors.getSelectionModel().getSelectedItem();
		double costOfLunch = Double.parseDouble(txtCostOfLunch.getText());
		double costOfDinner = Double.parseDouble(txtCostOfDinner.getText());

		if (printingReport != null) {
			reportData = new StoredReportData(printingReport, costOfLunch, costOfDinner, includeEmptyVisitors);
		} else {
			reportData = new ActualReportData(
					new ArrayList<>(daoFactory.getVisitorDAO().getAll()),
					costOfLunch,
					costOfDinner,
					includeEmptyVisitors
			);
		}

		// sorting report data
		reportData.sort((o1, o2) -> {
            String fio1 = o1.getLastName() + " " + o1.getFirstName();
            String fio2 = o2.getLastName() + " " + o2.getFirstName();

            return fio1.compareToIgnoreCase(fio2);
        });

		return reportData;
	}

	private void resetReportInfo() {
		lblReportInfo.setText(Messages.get("label.report-based-on-current-data"));
		printingReport = null;
	}

	@Override
	public void update(Observable o, Object obj) {
		if (obj instanceof Report) {
			printingReport = (Report) obj;
			lblReportInfo.setText(Messages.get("label.report-based-on-saved-data", printingReport.getName()));
		} else {
			LOG.debug("Argument 'object' in update method is null or isn't instance of clazz 'Report'");
		}
	}
}
