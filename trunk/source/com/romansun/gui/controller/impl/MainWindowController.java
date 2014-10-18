package com.romansun.gui.controller.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.romansun.cache.EntityCache;
import com.romansun.config.Resources;
import com.romansun.gui.controller.AbstractController;
import com.romansun.gui.utils.Dialog;
import com.romansun.hibernate.logic.Visitor;
import com.romansun.reports.ReportBuilder;
import com.romansun.reports.ReportsSaver;
import com.romansun.reports.logic.Report;

public class MainWindowController extends AbstractController implements Initializable {
	private final static String SETTINGS_WINDOW_NAME = "Settings";
	private final static Logger LOG = Logger.getLogger(MainWindowController.class);
	
	private static Observable observable = new Observable() {
		public void notifyObservers(Object arg) {
			setChanged();
			super.notifyObservers(arg);
		}
	};
	
	public static void addObserver(Observer o) {
		observable.addObserver(o);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			mainTabPane = tabPane;
			
			// loading tabs and add to TabPane
			File firstTabFile = new File("resource/fxml/first_tab.fxml");
			if (!firstTabFile.exists()) {
				firstTabFile = Resources.getInstance().getFirstTabFXML();
				LOG.info("Внешний файл first_tab.fxml не найден, используется внутренний");
			}
			File secondTabFile = new File("resource/fxml/second_tab.fxml");
			if (!secondTabFile.exists()) {
				secondTabFile = Resources.getInstance().getSecondTabFXML();
				LOG.info("Внешний файл second_tab.fxml не найден, используется внутренний");
			}
			File thirdTabFile = new File("resource/fxml/third_tab.fxml");
			if (!thirdTabFile.exists()) {
				thirdTabFile = Resources.getInstance().getThirdTabFXML();
				LOG.info("Внешний файл third_tab.fxml не найден, используется внутренний");
			}
			File fourthTabFile = new File("resource/fxml/fourth_tab.fxml");
			if (!fourthTabFile.exists()) {
				fourthTabFile = Resources.getInstance().getFourthTabFXML();
				LOG.info("Внешний файл fourth_tab.fxml не найден, используется внутренний");
			}
			
			firstTab.setContent(loadTab(firstTabFile.toURL(), null));
			secondTab.setContent(loadTab(secondTabFile.toURL(), null));
			thirdTab.setContent(loadTab(thirdTabFile.toURL(), null));
			fourthTab.setContent(loadTab(fourthTabFile.toURL(), null));
			LOG.info("Все табы были успешно загружены");
		} catch (Exception e) {
			LOG.error("Ошибка при загрузке Tabs: ", e);
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
	private void resetTalons() {
		int answer = Dialog.showQuestion("Вы уверены, что хотите сбросить обеды и ужины для всех посетителей?", null);
		if (answer == 1 /*YES*/) {
			// Create report
			Report report = null;
			try {
				Collection<Visitor> visitors = visitorsDAO.getAll();
				ReportBuilder builder = new ReportBuilder();
				report = builder.buildReport(new ArrayList<Visitor>(visitors));
				ReportsSaver saver = new ReportsSaver(PATH_TO_REPORTS, report);
				saver.saveReport();
				// Reset talons
				dao.getTalonDAO().resetAllTalons();
				if (config.useVisitorsCache) {
					// reload cache
					((EntityCache) visitorsDAO).reload();
				}
				// Call to observer
				observable.notifyObservers();
				LOG.info("Все обеды и ужины были сброшены, отчет " + report.getName() + " сформирован");
			} catch (Exception e) {
				LOG.error("Ошибка при сбросе талонов и создании отчета: " , e);
			}
		}
		
	}
	
	@FXML
	private void clickOnSettings() {
		File settingsWindow = new File("resource/fxml/settings_window.fxml");
		if (!settingsWindow.exists()) {
			settingsWindow = Resources.getInstance().getFourthTabFXML();
			LOG.info("Внешний файл settings_window.fxml не найден, используется внутренний");
		}
		DialogBuilder settingWindowBuilder = 
				new DialogBuilder(settingsWindow, SETTINGS_WINDOW_NAME);
		settingWindowBuilder.show();
	}
	
	@FXML
	private void clickOnClose() {
		System.exit(0);
	}
	
	@FXML
	private void clickOnAbout() {
		Dialog.showInfo("Программа \"Учет талонов\" предназначена для заведующих столовой." +
				"\nАвтор программы: Рыбалкин Роман Александрович" +
				"\nПо всем вопросам обращаться по адресу roman.rybalkin24@gmail.com" +
				"\nВсе права защищены ©");
	}
	
	// method for loading Tab from fxml-file
	private AnchorPane loadTab(URL path_to_tab, Object controller) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(path_to_tab);
    	fxmlLoader.setController(controller);
    	fxmlLoader.load();
    	AnchorPane pane = fxmlLoader.getRoot();
    	
    	return pane;
	}
	
	public class DialogBuilder {

		private Stage stage = null;
		protected String dialogFXML;
		protected String dialogName;

		public DialogBuilder() {
			dialogFXML = null;
			dialogName = "";
		}

		public DialogBuilder(File f, String dialogName) {
			stage = new Stage();
			URL fxmlURL = null;
			try {
				fxmlURL = f.toURL();
				if (fxmlURL == null) {
					throw new IllegalArgumentException("FXML file cannot be load");
				}
				AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
				Scene scene = new Scene(mainFrame);
				stage.setScene(scene);
				stage.setTitle(dialogName);
			} catch (Exception e) {
				LOG.error("Error in DialogBuilder:", e);
			}
		}

		public void show() {
			if (stage == null)
				throw new RuntimeException("Dialog cannot be instantiated");
			stage.show();
		}

	}
}
