package com.rrybalkin.gui.controller.impl;

import com.rrybalkin.gui.Dialog;
import com.rrybalkin.gui.controller.AbstractController;
import com.rrybalkin.gui.controller.Converters;
import com.rrybalkin.hibernate.entity.Association;
import com.rrybalkin.hibernate.entity.Visitor;
import com.rrybalkin.printing.data.ActualReportData;
import com.rrybalkin.printing.data.ReportData;
import com.rrybalkin.printing.data.StoredReportData;
import com.rrybalkin.printing.writer.ReportType;
import com.rrybalkin.printing.writer.ReportWriter;
import com.rrybalkin.printing.writer.ReportWriterFactory;
import com.rrybalkin.reporting.logic.Report;
import com.rrybalkin.utils.Messages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class FourthTabController extends AbstractController implements Initializable, Observer {
    private final static Logger LOG = Logger.getLogger(FourthTabController.class);

    private final static String GREEN_COLOR = "#008009", RED_COLOR = "#d91548";

    private Report printingReport;
    private Integer month;
    private Integer year;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        ThirdTabController.addObserver(this);

        seeReport.visibleProperty().set(false);

        lblReportInfo.setTextFill(Color.web(GREEN_COLOR));
        resetReportInfo();

        month = new DateTime().getMonthOfYear();
        year = new DateTime().getYear();

        cbMonth.setConverter(Converters.MONTHS_CONVERTER);
        cbMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        cbMonth.getSelectionModel().select(month);

        cbYear.setConverter(Converters.YEARS_CONVERTER);
        cbYear.getItems().addAll(year - 1, year);
        cbYear.getSelectionModel().select(year);

        cbMonth.valueProperty().addListener((o, oldValue, newValue) -> month = newValue);
        cbYear.valueProperty().addListener((o, oldValue, newValue) -> year = newValue);
    }

    @FXML
    private Label lblReportInfo;
    @FXML
    private Label lblStatus;
    @FXML
    private Hyperlink seeReport;
    @FXML
    private ComboBox<Integer> cbMonth;
    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    void createReport() {
        ReportType reportType = ReportType.XLS;
        ReportWriter writer = ReportWriterFactory.getWriter(reportType);
        if (writer == null) {
            Dialog.showWarning(Messages.get("dialog.warn.type-report-is-not-supported"));
            return;
        }

        try {
            if (!validate()) {
                LOG.warn("Validation failed, break generating process.");
            } else {
                if (printingReport != null) {
                    LOG.info("Start generating a stored report ...");
                    printStoredReport(writer);
                } else {
                    LOG.info("Start generating actual data report ...");
                    printActualDataReport(writer);
                }
            }
        } catch (Exception e) {
            Dialog.showErrorOnException(e);
            lblStatus.setTextFill(Color.valueOf(RED_COLOR));
            lblStatus.setText(Messages.get("label.error-on-report-generating"));
            LOG.error("Error while forming report with reportType = '" + reportType + "': ", e);
        } finally {
            resetReportInfo();
        }
    }

    private void printActualDataReport(ReportWriter writer) throws Exception {
        String reportDate = Converters.MONTHS_CONVERTER.toString(cbMonth.getSelectionModel().getSelectedItem())
                + " " + cbYear.getSelectionModel().getSelectedItem();

        List<Visitor> visitors = daoFactory.getVisitorDAO().getAll();
        Set<Association> reportedGroups = new HashSet<>();
        Map<Association, List<Visitor>> visitorsWithGroup = new HashMap<>();
        for (Visitor visitor : visitors) {
            Association visitorGroup = visitor.getAssociation();
            if (visitor.getTalon().getBreakfasts() > 0 ||
                visitor.getTalon().getDinners() > 0 ||
                visitor.getTalon().getLunches() > 0) {
                reportedGroups.add(visitorGroup);

                if (!visitorsWithGroup.containsKey(visitorGroup)) {
                    visitorsWithGroup.put(visitor.getAssociation(), new ArrayList<>());
                }
                visitorsWithGroup.get(visitorGroup).add(visitor);
            }
        }
        LOG.info("List of groups to report (have visits in this month): " + reportedGroups);
        for (Map.Entry<Association, List<Visitor>> entry : visitorsWithGroup.entrySet()) {
            LOG.info("Found " + entry.getValue().size() + " visitors with group = '" + entry.getKey().getName() + "'");
        }

        final List<File> reports = new ArrayList<>();
        ReportData allVisitorsData = sortReportData(new ActualReportData(visitors, false));
        File allVisitorsReport = writer.generateReport(allVisitorsData, reportDate, Messages.get("report.group.all"));
        LOG.info("Report with all visitors generated by path " + allVisitorsReport.getAbsolutePath());
        reports.add(allVisitorsReport);

        for (Association group : reportedGroups) {
            ReportData reportData = sortReportData(new ActualReportData(visitorsWithGroup.get(group), false));
            File report;
            if (Association.NO_GROUP.equalsIgnoreCase(group.getName())) {
                report = writer.generateReport(reportData, reportDate, Messages.get("report.group.no"));
                LOG.info("Report with no-group visitors generated by path " + report.getAbsolutePath());
            } else {
                report = writer.generateReport(reportData, reportDate, group.getName());
                LOG.info("Report with visitors of group='" + group.getName() + "' generated by path " + report.getAbsolutePath());
            }
            reports.add(report);
        }

        if (reports.size() > 0 && reports.get(0).exists()) {
            File report = reports.get(0);
            File reportFolder = report.getParentFile();
            lblStatus.setTextFill(Color.web(GREEN_COLOR));
            lblStatus.setText(Messages.get("label.report-is-ready", reportFolder.getAbsoluteFile()));
            seeReport.visibleProperty().set(true);
            seeReport.setUserData(reportFolder.getAbsoluteFile());
        } else {
            Dialog.showWarning("dialog.warn.report-file-not-found");
        }
    }

    private void printStoredReport(ReportWriter writer) throws Exception {
        ReportData reportData = sortReportData(new StoredReportData(printingReport, false));
        String storedReportName = printingReport.getName();
        String reportDate = storedReportName.substring(0, storedReportName.indexOf('.'));

        File report = writer.generateReport(reportData, reportDate, Messages.get("report.group.all"));
        if (report != null && report.exists()) {
            lblStatus.setTextFill(Color.web(GREEN_COLOR));
            lblStatus.setText(Messages.get("label.report-is-ready", report.getAbsolutePath()));
            seeReport.visibleProperty().set(true);
            seeReport.setUserData(report.getAbsoluteFile());
        } else {
            Dialog.showWarning("dialog.warn.report-file-not-found");
        }
    }

    @FXML
    void hlAction() {
        // handle a click on hyperlink
        File file = (File) seeReport.getUserData();
        try {
            Desktop.getDesktop().open(file);
            lblStatus.setText("");
        } catch (IOException e) {
            Dialog.showErrorOnException(e);
            LOG.error("Error while opening report by path = " + file.getAbsolutePath() + ": ", e);
        }
    }

    /**
     * Method for validating states of fields before start of forming report
     *
     * @return boolean true - validation is passed, false - validation is failed
     */
    private boolean validate() {
        final Integer selMonth = cbMonth.getSelectionModel().getSelectedItem();
        final Integer selYear = cbYear.getSelectionModel().getSelectedItem();
        final LocalDate now = LocalDate.now();
        if ((selMonth > now.getMonthOfYear() && selYear == now.getYear()) || selYear > now.getYear()) {
            Dialog.showWarning(Messages.get("dialog.warn.report-date-must-be-present"));
            return false;
        }
        return true;
    }

    /**
     * Method for sorting report data
     *
     * @return data for forming report
     */
    private ReportData sortReportData(ReportData reportData) {
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
