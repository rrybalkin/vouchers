package com.romansun.gui.controller.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.hibernate.logic.Visitor;
import com.romansun.printing.data.ActualReportData;
import com.romansun.printing.data.ReportData;
import com.romansun.printing.data.StoredReportData;
import com.romansun.printing.writer.IReportWriter;
import com.romansun.printing.writer.WriterFactory;
import com.romansun.reports.logic.Report;

public class FourthTabController extends AbstractController implements Initializable, Observer {
	private final static Logger LOG = Logger.getLogger(FourthTabController.class);
	private final static String PRINTING_REPORT_BY_ACTUAL_DATA = "Формирование отчета по текущим актуальным данным";
	private final static String GREEN_COLOR = "#008009", RED_COLOR = "#d91548";
	protected Report printingReport;
	
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		ThirdTabController.addObserver(this);
		
		cbEnableEmptyVisitors.getItems().clear();
		cbEnableEmptyVisitors.getItems().addAll(new String[]{"Нет", "Да"});
		cbEnableEmptyVisitors.getSelectionModel().select(0);
		
		cbReportFormat.getItems().clear();
		cbReportFormat.getItems().addAll(config.PRINT_REPORT_FORMATS);
		cbReportFormat.getSelectionModel().select(0);
		
		seeReport.visibleProperty().set(false);
		
		lblReportInfo.setTextFill(Color.web(GREEN_COLOR));
		resetReportInfo();
	}
	
	@FXML
	private ComboBox<String> cbEnableEmptyVisitors;
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
	private Button btnCreateReport;
	@FXML
	private Hyperlink seeReport;
	
	@FXML
	protected void createReport(ActionEvent event) {
		LOG.debug("Start forming report ...");
		String reportType = cbReportFormat.getSelectionModel().getSelectedItem();
		try 
		{
			if (!validate()) return;
			ReportData reportData = buildReportData();
			String reportDate = null;
			if (printingReport != null) {
				String storedReportName = printingReport.getName();
				reportDate = storedReportName.substring(0, storedReportName.indexOf('.'));
			} else {
				reportDate = DateTime.now().monthOfYear().getAsText(new Locale("ru"))
					+ " " + DateTime.now().getYear();
			}
			
			IReportWriter writer = WriterFactory.getWriter(reportType, config.PATH_TO_REPORTS);
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
	
	@FXML //Method for process click on hyperlink
	protected void hlAction(ActionEvent event) {
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
	protected boolean validate() {
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
			Double.parseDouble(costOfLunch);
			Double.parseDouble(costOfDinner);
		} catch (Exception e) {
			LOG.error("Field costOfLunch = " + costOfLunch + " or costOfDinner = " + costOfDinner + " is incorrect!");
			Dialog.showError("Поля \"Стоимость обеда\" и \"Стоимость ужина\" должны содержать только числа!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method for building report data according to the selected type of report
	 * @return data for forming report
	 * @throws Exception
	 */
	protected ReportData buildReportData() throws Exception {
		ReportData reportData = null;
		
		boolean ignoreEmptyVisitors = 
				("НЕТ".equalsIgnoreCase(cbEnableEmptyVisitors.getSelectionModel().getSelectedItem())) ? true : false;
		double costOfLunch = Double.parseDouble(txtCostOfLunch.getText());
		double costOfDinner = Double.parseDouble(txtCostOfDinner.getText());
		
		if (printingReport != null) {
			reportData = new StoredReportData(printingReport, costOfLunch, costOfDinner, ignoreEmptyVisitors);
		} else {
			reportData = new ActualReportData(
					new ArrayList<Visitor>(dao.getVisitorDAO().getAllVisitors()), 
					costOfLunch, 
					costOfDinner, 
					ignoreEmptyVisitors
					);
		}
		
		return reportData;
	}
	
	protected void resetReportInfo() {
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
