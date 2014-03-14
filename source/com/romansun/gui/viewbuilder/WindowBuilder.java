package com.romansun.gui.viewbuilder;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

import com.romansun.config.Resources;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WindowBuilder extends Application {
	private static final Logger LOG = Logger.getLogger(WindowBuilder.class);

	@Override
	public void start(Stage stage) throws Exception {
		try {
			File mainWindow = new File("resource/fxml/main_window.fxml");
			if (!mainWindow.exists()) {
				mainWindow = Resources.getInstance().getMainWindowFXML();
				LOG.info("Внешний файл main_window.fxml не был найден - используется внутренний");
			}
			URL fxmlURL = mainWindow.toURL();
			if (fxmlURL == null) {
				throw new IllegalArgumentException("FXML file cannot be load");
			}
			AnchorPane mainFrame = FXMLLoader.load(fxmlURL);
			Scene scene = new Scene(mainFrame);
			stage.setScene(scene);
			stage.setTitle("Учет талонов");
			File icon = new File("resource/icon.png");
			if (!icon.exists()) {
				icon = Resources.getInstance().getIcon();
				LOG.info("Внешний файл icon.png не найден - используется внутренний");
			}
			stage.getIcons().add(new Image(icon.toURL().toString()));
			stage.show();
		} catch (Exception e) {
			LOG.error("Ошибка при выполнении загрузки главного окна: ", e);
		}
	}
	
	public void show() {
		Application.launch(WindowBuilder.class);
	}
}
