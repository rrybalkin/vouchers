package com.romansun.gui.controller.impl;

import com.romansun.cache.EntityCache;
import com.romansun.gui.Dialog;
import com.romansun.gui.controller.AbstractController;
import com.romansun.hibernate.entity.Visitor;
import com.romansun.reporting.ReportBuilder;
import com.romansun.reporting.ReportsSaver;
import com.romansun.reporting.logic.Report;
import com.romansun.utils.Messages;
import com.romansun.utils.Resources;
import com.romansun.utils.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@SuppressFBWarnings("DM_EXIT")
public class MainWindowController extends AbstractController implements Initializable {
	private final static Logger LOG = Logger.getLogger(MainWindowController.class);

	private static Observable observable = new Observable() {
		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
		}
	};

	static void addObserver(Observer o) {
		observable.addObserver(o);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			super.mainTabPane = tabPane;

			// loading tabs and add to TabPane
			File firstTabFile = Resources.getInstance().getResource(Resources.FIRST_TAB_FXML);
			File secondTabFile = Resources.getInstance().getResource(Resources.SECOND_TAB_FXML);
			File thirdTabFile = Resources.getInstance().getResource(Resources.THIRD_TAB_FXML);
			File fourthTabFile = Resources.getInstance().getResource(Resources.FOURTH_TAB_FXML);

			firstTab.setContent(loadTab(firstTabFile));
			secondTab.setContent(loadTab(secondTabFile));
			thirdTab.setContent(loadTab(thirdTabFile));
			fourthTab.setContent(loadTab(fourthTabFile));
		} catch (Exception e) {
			throw new IllegalStateException("Error in loading tabs", e);
		}
	}
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab firstTab;
	@FXML
	private Tab secondTab;
	@FXML
	private Tab thirdTab;
	@FXML
	private Tab fourthTab;

	@FXML
	void resetTalons() {
		int answer = Dialog.showQuestion(Messages.get("dialog.question.reset-all-talons"), null);
		if (answer == Dialog.YES) {
			// Create report
			Report report;
			try {
				Collection<Visitor> visitors = visitorsDAO.getAll();
				ReportBuilder builder = new ReportBuilder();
				report = builder.buildReport(new ArrayList<>(visitors));
				ReportsSaver saver = new ReportsSaver(PATH_TO_REPORTS, report);
				saver.saveReport();
				// Reset talons
				daoFactory.getTalonDAO().resetAllTalons();
				if (config.useVisitorsCache) {
					// reload cache
					((EntityCache) visitorsDAO).reload();
				}
				// Call to observer
				observable.notifyObservers();
			} catch (Exception e) {
				Dialog.showErrorOnException(e);
				LOG.error("Error while reset talons: " , e);
			}
		}

	}

	@FXML
	void clickOnClose() {
		System.exit(0);
	}

	@FXML
	void clickOnAbout() {
		Dialog.showInfo(Messages.get("dialog.info.app-about"));
	}

	private AnchorPane loadTab(File tabFile) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(tabFile.toURI().toURL());
    	fxmlLoader.setController(null);
    	fxmlLoader.load();

		return fxmlLoader.getRoot();
	}
}
