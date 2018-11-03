package com.rrybalkin.gui.controller.impl;

import com.rrybalkin.gui.Dialog;
import com.rrybalkin.gui.controller.AbstractController;
import com.rrybalkin.gui.controller.Converters;
import com.rrybalkin.reporting.ReportsManager;
import com.rrybalkin.reporting.logic.InfoVisitor;
import com.rrybalkin.reporting.logic.Report;
import com.rrybalkin.utils.Messages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.*;

public class ThirdTabController extends AbstractController implements Initializable, Observer {
    private final static Logger LOG = Logger.getLogger(ThirdTabController.class);

    private Integer month;
    private Integer year;
    private ReportsManager reportsManager;

    {
        month = new DateTime().getMonthOfYear();
        year = new DateTime().getYear();
        reportsManager = new ReportsManager(PATH_TO_REPORTS);
        reportsManager.loadReports();
    }

    private static Observable observable = new Observable() {
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };

    static void addObserver(Observer o) {
        observable.addObserver(o);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // add observer for Main Window
        MainWindowController.addObserver(this);
        loadReports();

        cbMonth.setConverter(Converters.MONTHS_CONVERTER);
        cbMonth.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        cbMonth.getSelectionModel().select(month);

        cbYear.setConverter(Converters.YEARS_CONVERTER);
        cbYear.getItems().addAll(year - 1, year, year + 1);
        cbYear.getSelectionModel().select(year);

        cbMonth.valueProperty().addListener((o, oldValue, newValue) -> {
            month = newValue;
            loadReports();
        });

        cbYear.valueProperty().addListener((o, oldValue, newValue) -> {
            year = newValue;
            loadReports();
        });

        // Initialize table for report
        TableColumn columnFIO = tbReport.getColumns().get(0);
        TableColumn columnLunches = tbReport.getColumns().get(1);
        TableColumn columnDinners = tbReport.getColumns().get(2);
        columnFIO.setCellValueFactory((new PropertyValueFactory<InfoVisitor, String>("FIO")));
        columnLunches.setCellValueFactory((new PropertyValueFactory<InfoVisitor, Integer>("lunches")));
        columnDinners.setCellValueFactory((new PropertyValueFactory<InfoVisitor, Integer>("dinners")));

        // Initialize listener for input mask
        txtMask.caretPositionProperty().addListener((o, oldValue, newValue) -> {
            String mask = txtMask.getText();
            Report report = lvReports.getSelectionModel().getSelectedItem();
            tbReport.getItems().clear();
            if (mask != null && !mask.isEmpty()) {
                tbReport.getItems().addAll(filterByMask(report.getVisitors(), mask));
            } else {
                tbReport.getItems().addAll(report.getVisitors());
            }
        });

        initContextMenuForReports();
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
    void chooseReport(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                tbReport.getItems().clear();
                Report report = lvReports.getSelectionModel().getSelectedItem();
                tbReport.getItems().addAll(sortVisitors(report.getVisitors()));
            }
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            Report delReport = lvReports.getSelectionModel().getSelectedItem();
            int answer = Dialog.showQuestion(Messages.get("dialog.question.do-report-delete", delReport.getName()), event);
            if (answer == Dialog.YES) {
                if (delReport.getFile().delete()) loadReports();
            }
        }
    }

    private void loadReports() {
        List<Report> reports = reportsManager.getReportsByDate(month, year);
        lvReports.getItems().clear();
        lvReports.getItems().addAll(reports);
    }

    private List<InfoVisitor> filterByMask(List<InfoVisitor> visitors, String mask) {
        List<InfoVisitor> filterList = new ArrayList<>();
        for (InfoVisitor v : visitors) {
            String fio = v.getFIO();
            if (fio.toLowerCase(Locale.getDefault()).contains(mask.toLowerCase(Locale.getDefault()))) {
                filterList.add(v);
            }
        }
        return filterList;
    }

    private List<InfoVisitor> sortVisitors(Collection<InfoVisitor> collection) {
        List<InfoVisitor> visitors = new ArrayList<>(collection);
        visitors.sort(Comparator.comparing(InfoVisitor::getFIO));
        return visitors;
    }

    /*
     * Method for initializing context menu of items for stored reports
     */
    private void initContextMenuForReports() {
        final ContextMenu menu = new ContextMenu();
        MenuItem itemPrintReport = new MenuItem(Messages.get("label.print-report"));
        itemPrintReport.setOnAction(event -> {
            Report selectedReport = lvReports.getSelectionModel().getSelectedItem();
            LOG.debug("Action: printing the stored report = " + selectedReport.getName());
            observable.notifyObservers(selectedReport);
            mainTabPaneRef.get().getSelectionModel().select(3);
        });

        menu.getItems().add(itemPrintReport);

        lvReports.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                menu.show(lvReports, event.getScreenX(), event.getScreenY());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg1) {
        loadReports();
    }
}
